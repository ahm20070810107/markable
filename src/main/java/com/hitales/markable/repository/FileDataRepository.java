package com.hitales.markable.repository;

import com.hitales.markable.model.FileData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileDataRepository extends MongoRepository<FileData, String>{
    List<FileData> findByFileName(String fileName);

    Page<FileData> findByFileName(String fileName, Pageable pageable);
}
