package com.hitales.markable.controller;

import com.hitales.markable.Exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/download")
public class DownloadController {

    @GetMapping("")
    public ResponseEntity<Resource> download(@RequestParam("filename") String filename){

        String filePath="";
        File file = FileUtils.getFile(filePath);
        FileSystemResource fileSystemResource = new FileSystemResource(file);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.put("Content-disposition", Collections.singletonList(String
                .format("attachment; filename=%s_%s_%s.xlsx"//, bugType,
                        /*projectId, entityType*/)));
        return new ResponseEntity<>(fileSystemResource, headers, HttpStatus.OK);
    }
}
