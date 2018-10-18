package com.cb.platform.yq.api.dao;

import com.cb.platform.yq.api.entity.ApiGFileDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;

/**
 *文件数据库
 * @author whh
 */
public interface ApiGFileDao  extends JpaRepository<ApiGFileDo,String>,Serializable {

    /**
     * 查询文件
     * @param filePath
     * @param flag
     * @return
     */
    @Query(value="select * from api_g_file where virtual_path=:virtual_path and type=:type limit 1", nativeQuery=true)
    ApiGFileDo sreachApiGFileDo(@Param("virtual_path") String filePath, @Param("type")String flag);
}
