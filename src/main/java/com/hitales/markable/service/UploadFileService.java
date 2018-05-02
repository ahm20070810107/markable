package com.hitales.markable.service;

import com.hitales.markable.Exception.NotAcceptableException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

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
       
    }
}
