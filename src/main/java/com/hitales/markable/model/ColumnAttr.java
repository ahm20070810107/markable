package com.hitales.markable.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class ColumnAttr {
    @Id
    private String id;

    private String fileName;

    private List<ColumnAttrItem> columnList;
}
