package com.hitales.markable.controller;

import com.hitales.markable.Exception.BadRequestException;
import com.hitales.markable.response.QueryFileListRes;
import com.hitales.markable.response.QueryListRes;
import com.hitales.markable.service.QueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/query")
public class QueryControler {

    @Autowired
    private QueryService queryService;


    @GetMapping("/getList")
    public QueryListRes getList(@RequestParam("filename") String filename, @RequestParam(name="page",required = false) int page, @RequestParam(name="pageSize",required =false) int pageSize){
        log.info("getList start");
        if(StringUtils.isEmpty(filename)){
            throw new BadRequestException("fileName 为空");
        }
        if(pageSize==0){
            log.info("pageSize 默认10条");
            pageSize=10;
        }
        return queryService.getList(filename,page,pageSize);
    }

    @GetMapping("/getFileList")
    public QueryFileListRes getFileList(){
        log.info("getFileList start");

        return queryService.getFileList();
    }
}
