package com.hitales.markable.repository;

import com.hitales.markable.model.FileNameList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileNameListRepository extends MongoRepository<FileNameList,String> {
    List<FileNameList> findDistinctByFileName(String fileName);
}
