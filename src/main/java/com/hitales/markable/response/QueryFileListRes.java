package com.hitales.markable.response;

import lombok.Data;

import java.util.List;

@Data
public class QueryFileListRes {
    private List<String> fileList;
}
