package com.hitales.markable.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ColumnAttrItem {
    private String name;

    private Integer type;

    private List<String> options;
}
