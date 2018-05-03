package com.hitales.markable.service;

import com.hitales.markable.Exception.NotAcceptableException;
import com.hitales.markable.common.Constant;
import com.hitales.markable.model.FileNameList;
import com.hitales.markable.util.ExcelTools;
import lombok.extern.slf4j.Slf4j;
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
import java.util.*;

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
        multipartFile.transferTo(destFile.getAbsoluteFile());
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

        Set<String> setMultiColumn = new HashSet<>();    //存多选的列名
        saveColumnData(setMultiColumn,fileName,row0,row1,startNum,endNum);

        int count=1;
        List<Document> listDoc= new ArrayList<>();
        while (rows.hasNext()) {
            count++;
            getDataObject(setMultiColumn,listDoc,fileName,row1,rows.next(),startNum,endNum);
            if(listDoc.size() == 100){
                mongoTemplate.insert(listDoc,"fileData");
                listDoc = new ArrayList<>();
            }
        }
        if(listDoc.size() > 0){
            mongoTemplate.insert(listDoc,"fileData");
        }
        saveFileNameList(fileName,count);
    }

    private void getDataObject(Set<String> setMultiColumn,List<Document> listDoc,String fileName,Row columnNameRow,Row dataRow,int start,int end)
    {
        Document document = new Document();
        Document docDatas = new Document();
        listDoc.add(document);

        document.put("fileName",fileName);
        document.put("datas",docDatas);
        for (int i = start; i < end; i++) {
            String key = ExcelTools.getCellValue(columnNameRow.getCell(i)).toString();
            if(setMultiColumn.contains(key)){
               String value=ExcelTools.getCellValue(dataRow.getCell(i)).toString();
               value = value.replace(Constant.USER_INPUT_SPLIT_CHAR,Constant.MUTI_INPUT_SPLIT_CHAR);
                List<String> values;
               if(value.length() == 0){
                  values = new ArrayList<>();
               }else{
                values= Arrays.asList(value.split(Constant.MUTI_INPUT_SPLIT_CHAR));
               }
               docDatas.put(key,values);
            }else{
            docDatas.put(key,ExcelTools.getCellValue(dataRow.getCell(i)));
            }
        }
    }
    private void saveColumnData(Set<String> setMultiColumn,String fileName,Row defineRow,Row columnNameRow,int start,int end){
        Document document = new Document();
        List<Document> listAttr= new ArrayList<>();
        document.put("fileName",fileName);
        document.put("columnList",listAttr);

        for (int i = start; i < end; i++) {
           Document document1 = new Document();
           listAttr.add(document1);
           List<String> options= new ArrayList<>();
           if(ExcelTools.getCellValue(columnNameRow.getCell(i)).toString().length() < 1) {
               throw new NotAcceptableException(String.format("Excel的第%d列名为空，无法保存！",i+1));
           }
           String columnName=ExcelTools.getCellValue(columnNameRow.getCell(i)).toString();
           document1.put("name",columnName);
           document1.put("type",getColumnType(setMultiColumn,columnName,ExcelTools.getCellValue(defineRow.getCell(i)).toString(),options));
           document1.put("options",options);
        }
        mongoTemplate.save(document,"columnAttr");
    }

    private int getColumnType(Set<String> setMultiColumn,String columnName,String cellValue,List<String> options){
       int type = 1; //1表示输入框
       cellValue = cellValue.trim();
       if(cellValue.equals("不可修改")) {
           return 0;//表示不可编辑
       }
       if(cellValue.startsWith("(")){
           type =2 ;  //表示下拉单选
           cellValue= cellValue.replaceAll("(|)","");
           fillOptions(cellValue,options);
       }else if(cellValue.startsWith("[")){
           type =3; //表示下拉多选
           setMultiColumn.add(columnName);
           cellValue = cellValue.replaceAll("[|]","");
           fillOptions(cellValue,options);
       }
       return type;
    }

    private void fillOptions(String cellValue, List<String> options){
        String[] values= cellValue.split(",");
        for(String str :values){
            options.add(str);
        }
    }

    private void saveFileNameList(String fileName,int count){
        Document document = new Document();

        document.put("fileName",fileName);
        document.put("count",count);
        mongoTemplate.save(document,"fileNameList");
    }
}
