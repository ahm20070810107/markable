package com.hitales.markable.controller;

import com.hitales.markable.service.UploadFileService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


/**
 * Created with IntelliJ IDEA
 * User:huangming
 * Date:2018/5/2
 * Time:下午3:33
 */
@RestController
@Slf4j
@RequestMapping(value = "/upload")
public class SaveUploadController {

    @Autowired
    private UploadFileService uploadFileService;

    @PostMapping("/uploadFile")
    @ApiOperation("操作实体添加表型接口")
    public void uploadFile(MultipartHttpServletRequest multiReq){
        String savedFilePath=".//uploadFiles//";

        MultipartFile multipartFile= multiReq.getFile("file");
        // 获取上传文件的路径
        String uploadFilePath = multipartFile.getOriginalFilename();
        log.info("uploadFlePath:" + uploadFilePath);
        // 截取上传文件的文件名
        String uploadFileName = uploadFilePath.substring(uploadFilePath.lastIndexOf('\\') + 1, uploadFilePath.lastIndexOf('.'));
        log.info("fileName:" + uploadFileName);
        // 截取上传文件的后缀
        String uploadFileSuffix = uploadFilePath.substring(uploadFilePath.lastIndexOf('.') + 1);
        log.info("uploadFileSuffix:" + uploadFileSuffix);
        //服务器保存的文件名
        String savedFileName=savedFilePath+uploadFileName+"."+uploadFileSuffix;

        uploadFileService.saveFile(multipartFile,savedFileName);

        uploadFileService.checkFileExists(uploadFileName);

        uploadFileService.saveExcelToMongo(savedFileName,uploadFileName);
    }



}
