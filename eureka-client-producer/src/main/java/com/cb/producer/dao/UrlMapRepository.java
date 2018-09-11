package com.cb.producer.dao;

import com.cb.producer.entity.UrlMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * @author jdl
 */
@Repository
public interface UrlMapRepository extends JpaRepository<UrlMap,Long> {
    /**
     * judge the file whether exists
     * @param fileName
     * @return
     */
    Boolean existsByFileName(String fileName);

    /**
     * get file name list
     * @return
     */
    @Query(value="select fileName from UrlMap ")
    List<String> findAllFileName();

    /**
     * find file info by file name
     * @param fileName
     * @return
     */
    UrlMap findByFileName(String fileName);

}
