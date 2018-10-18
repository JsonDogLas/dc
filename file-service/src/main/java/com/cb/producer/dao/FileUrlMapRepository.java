package com.cb.producer.dao;

import com.cb.producer.entity.FileUrlMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * @author jdl
 */
@Repository
public interface FileUrlMapRepository extends JpaRepository<FileUrlMap,String> {

    /**
     * get file name list
     * @return
     */
    @Query(value="select fileName from FileUrlMap ")
    List<String> findAllFileName();

    /**
     * find file info by file name
     * @param fileName
     * @return
     */
    FileUrlMap findByFileName(String fileName);

}
