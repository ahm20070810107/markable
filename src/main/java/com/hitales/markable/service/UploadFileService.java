package com.hitales.markable.service;

import com.hitales.markable.Exception.NotAcceptableException;
import com.hitales.markable.model.FileNameList;
import com.hitales.markable.util.ExcelTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA
 * User:huangming
 * Date:2018/5/2
 * Time:下午4:27
 */
@Service
@Slf4j
public class UploadFileService {

   @Autowired
   private MongoTemplate mongoTemplate;

   public void saveFile(MultipartFile multipartFile,String fileSavePath,String uploadFileName,String uploadFileSuffix){
       FileOutputStream fos = null;
       FileInputStream fis = null;
       try {
           fis = (FileInputStream) multipartFile.getInputStream();
           fos = new FileOutputStream(new File( fileSavePath+ uploadFileName + ".") + uploadFileSuffix);
           byte[] temp = new byte[1024];
           int i = fis.read(temp);
           while (i != -1){
               fos.write(temp,0,temp.length);
               fos.flush();
               i = fis.read(temp);
           }
       }
       catch (IOException e) {
           log.error(e.getMessage());
           throw new NotAcceptableException("保存上传文件失败！");
       }
       finally {
           if (fis != null) {
               try { fis.close(); } catch (IOException e) { log.error(e.getMessage()); }
           }
           if (fos != null) {
               try { fos.close(); } catch (IOException e) { log.error(e.getMessage()); }
           }
       }
   }

    public void saveFile(MultipartFile multipartFile,String filePathName){
      try {
        File destFile = new File(filePathName);
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();// 新建文件夹
        }
        multipartFile.transferTo(destFile);
      }  catch (Exception e) {
        throw new NotAcceptableException("保存上传文件失败！");
      }
    }

    public void checkFileExists(String fileName){
        List<FileNameList> fileNameLists= mongoTemplate.find(new Query(Criteria.where("fileName").is(fileName)),FileNameList.class);
        if(fileNameLists.size() > 0 )
            throw new NotAcceptableException("文件名在服务器中已存在，请检查！");
    }

    public void saveExcelToMongo(String filePathName,String fileName){
        Workbook wb = ExcelTools.loadExcelFile(filePathName);
        dealSheet(wb.getSheetAt(0),fileName);
    }

    private void dealSheet(Sheet sheet,String fileName) {
        Iterator<Row> rows = sheet.rowIterator();
        Row row0 = rows.next();
        Row row1 = rows.next();

        int startNum = row1.getFirstCellNum() + 1;//第一列不使用
        int endNum = row1.getLastCellNum();

        int count=1;
        while (rows.hasNext()) {
            count++;
            Row row = rows.next();

        }
        saveColumnData(fileName,row0,row1,startNum,endNum);
        saveFileNameList(fileName,count);
    }

    private void getDataObject(Row columnNameRow,Row dataRow,int start,int end)
    {
        for (int i = start; i < end; i++) {

        }
    }
    private void saveColumnData(String fileName,Row defineRow,Row columnNameRow,int start,int end){
        Document document = new Document();
        List<Document> listAttr= new ArrayList<>();
        document.put("fileName",fileName);
        document.put("columnList",listAttr);

        for (int i = start; i < end; i++) {

        }
        mongoTemplate.save(document,"columnAttr");
    }
    private void saveFileNameList(String fileName,int count){
        Document document = new Document();

        document.put("fileName",fileName);
        document.put("count",count);
        mongoTemplate.save(document,"fileNameList");
    }
}