package com.hitales.markable.model;

import lombok.Data;
import org.omg.CORBA.Object;

import java.util.Map;

@Data
public class ColumnAttrItem {
    private String name;

    private Integer type;

    private Map<Object,Object> options;
}