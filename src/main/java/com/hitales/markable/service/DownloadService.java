package com.hitales.markable.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hitales.markable.common.Constant;
import com.hitales.markable.model.ColumnAttr;
import com.hitales.markable.model.FileData;
import com.hitales.markable.repository.ColumnAttrRepository;
import com.mongodb.client.MongoCursor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class DownloadService {

    @Autowired
    private ColumnAttrRepository columnAttrRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public ResponseEntity<Resource> download(String filename) throws IOException {

        String filePath = filename+".xlsx";
        // 声明一个工作薄
        XSSFWorkbook workBook = new XSSFWorkbook();
        // 生成一个表格
        XSSFSheet sheet = workBook.createSheet();
        // 创建表格标题行 第一行
        XSSFRow titleRow = sheet.createRow(0);
        ColumnAttr columnAttr = columnAttrRepository.findByFileName(filename);
        if (columnAttr != null) {
            int colunmCount = columnAttr.getColumnList().size();
            String arrayColumnName = null;
            Map<String,Integer> nameIndex = new HashMap<>();
            for (int i = 0; i < colunmCount; i++) {
                titleRow.createCell(i).setCellValue(columnAttr.getColumnList().get(i).getName());
                nameIndex.put(columnAttr.getColumnList().get(i).getName(),i);
                if (columnAttr.getColumnList().get(i).getType() == Constant.MULTI_TYPE) {
                    arrayColumnName = columnAttr.getColumnList().get(i).getName();
                }
            }
            Query query = getQuery(filename);
            MongoCursor<Document> iteratorCursor = mongoTemplate.getCollection("fileData").find(Document.parse(query.getQueryObject().toJson())).iterator();
            int i = 1;
            while (iteratorCursor.hasNext()) {
                Document fileDataDoc = iteratorCursor.next();
                fileDataDoc.remove("_id");
                ObjectMapper objectMapper = new ObjectMapper();
                FileData fileData  = objectMapper.readValue(fileDataDoc.toJson(),
                        FileData.class);
                Map<String, Object> datas = fileData.getDatas();
                if (datas != null) {
                    XSSFRow row = sheet.createRow(i++);
                    Set<String> keySet = datas.keySet();
                    if (keySet != null) {
                        final String multi_column_name = arrayColumnName;
                        keySet.forEach(key -> {
                            if(multi_column_name!=null&&multi_column_name.equals(key)){
                                String multiVale = datas.get(key).toString().toString().replaceFirst("\\[","").replace("]","");
                                row.createCell(nameIndex.get(key)).setCellValue(multiVale);
                            }
                            else{
                                row.createCell(nameIndex.get(key)).setCellValue(datas.get(key).toString());
                            }
                        });
                    }
                }
            }
        }

       File file = new File(filePath);
        //文件输出流
        FileOutputStream outStream = new FileOutputStream(file);
        workBook.write(outStream);
        outStream.flush();
        outStream.close();
        FileSystemResource fileSystemResource = new FileSystemResource(file);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        Date now = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy_MM_dd hh:MM");
        filename=filename+"-"+sf.format(now).replaceAll(":","").replace(" ","");
        headers.put("Content-disposition", Collections.singletonList(String
                        .format("attachment; filename=%s.xlsx", new String(filename.getBytes("UTF-8"),"iso-8859-1"))));
        return new ResponseEntity<>(fileSystemResource, headers, HttpStatus.OK);
    }

    private Query getQuery(String fileName) {
        Query query = new Query(Criteria.where("fileName").is(fileName));
        return query;
    }
}
