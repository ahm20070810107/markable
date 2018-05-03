package com.hitales.markable.response;

import com.hitales.markable.model.ColumnAttrItem;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class QueryListRes {
    private int total;

    private List<ColumnAttrItemView> titles;

    private List<Map<String,Object>> list;
}
