package com.hitales.markable.service;

import com.hitales.markable.model.ColumnAttr;
import com.hitales.markable.model.FileData;
import com.hitales.markable.model.FileNameList;
import com.hitales.markable.repository.ColumnAttrRepository;
import com.hitales.markable.repository.FileDataRepository;
import com.hitales.markable.repository.FileNameListRepository;
import com.hitales.markable.response.QueryFileListRes;
import com.hitales.markable.response.QueryListRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class QueryService {
    @Autowired
    private ColumnAttrRepository columnAttrRepository;

    @Autowired
    private FileDataRepository fileDataRepository;

    @Autowired
    private FileNameListRepository fileNameListRepository;

    public QueryListRes getList( String fileName, int page, int pageSize){
        QueryListRes queryListRes = new QueryListRes();
        List<ColumnAttr> columnAttrList = columnAttrRepository.findByFileName(fileName);
        if(columnAttrList!=null&&columnAttrList.size()>0){
            queryListRes.setTitles(columnAttrList.get(0).getColumnLIst());
            PageRequest pageRequest = PageRequest.of(page,pageSize);
            Page<FileData> fileDataPage = fileDataRepository.findByFileName(fileName,pageRequest);
            if(fileDataPage.hasContent()){
                queryListRes.setTotal((int)fileDataPage.getTotalElements());
                List<Map<String,Object>> list = new ArrayList<>();
                queryListRes.setList(list);
                fileDataPage.getContent().forEach(fileData -> {
                    Map<String,Object> datas = new HashMap<>();
                    datas.put("id",fileData.getId());
                    datas.putAll(fileData.getDatas());
                    list.add(datas);
                });

            }
        }
        log.info("fileData total:"+queryListRes.getTotal());
        return queryListRes;
    }

    public QueryFileListRes getFileList(){
        QueryFileListRes queryFileListRes = new QueryFileListRes();
        List<String> fileList = new ArrayList<>();
        queryFileListRes.setFileList(fileList);
        List<FileNameList> fileNameLists = fileNameListRepository.findAll();
        if(fileNameLists!=null&&fileNameLists.size()>0){
            fileNameLists.forEach(fileNameList -> fileList.add(fileNameList.getFileName()));
        }
        log.info("fileName list coun:"+fileNameLists.size());
        return queryFileListRes;
    }
}

