package com.hitales.markable.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created with IntelliJ IDEA
 * User:huangming
 * Date:2018/5/2
 * Time:下午3:33
 */
@Data
@Document(collection = "fileNameList")
public class FileNameList {

    @Id
    private String id;

    private String fileName;

    private int count;
}
