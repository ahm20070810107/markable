package com.hitales.markable.controller;

import com.hitales.markable.model.ColumnAttr;
import com.hitales.markable.repository.ColumnAttrRepository;
import com.hitales.markable.service.DownloadService;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

@RestController
@Slf4j
@RequestMapping("/download")
public class DownloadController {

    @Autowired
    private DownloadService downloadService;

    @GetMapping("")
    public ResponseEntity<Resource> download(@RequestParam("filename") String filename) throws IOException {
        log.info("dpwnload start");
        return downloadService.download(filename);
    }
}
