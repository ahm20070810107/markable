package com.hitales.markable.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Map;

@Data
public class FileData {
    @Id
    private String id;

    private String fileName;

    private Map<String,Object> datas;
}
