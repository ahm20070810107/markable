package com.hitales.markable.service;

import com.hitales.markable.Exception.BadRequestException;
import com.mongodb.client.result.DeleteResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;


/**
 * Created with IntelliJ IDEA
 * User:huangming
 * Date:2018/5/10
 * Time:上午11:46
 */

@Service
@Slf4j
public class DeleteService {

    @Autowired
    private MongoTemplate mongoTemplate;
    public long deleteFileData(String fileName){
        if(fileName== null || fileName.length() < 1){
            throw new BadRequestException("fileName不能为空！");
        }
        Query query= new Query(Criteria.where("fileName").is(fileName));
        mongoTemplate.remove(query,"columnAttr");
        mongoTemplate.remove(query,"fileNameList");
        DeleteResult deleteResult=mongoTemplate.remove(query,"fileData");
        return deleteResult.getDeletedCount();
    }
}
