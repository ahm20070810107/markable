package com.hitales.markable.response;

import lombok.Data;

import java.util.Map;

/**
 * Created with IntelliJ IDEA
 * User:huangming
 * Date:2018/5/3
 * Time:下午4:44
 */
@Data
public class ColumnAttrItemView {
    private String name;

    private Integer type;

    private Map<Object,Object> options;
}
