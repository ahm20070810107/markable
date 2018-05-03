package com.hitales.markable.repository;

import com.hitales.markable.model.ColumnAttr;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColumnAttrRepository extends MongoRepository<ColumnAttr, String> {
    ColumnAttr findByFileName(String fileName);
}
