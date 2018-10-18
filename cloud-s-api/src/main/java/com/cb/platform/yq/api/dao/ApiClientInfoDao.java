package com.cb.platform.yq.api.dao;

import com.cb.platform.yq.api.entity.ApiClientInfoDo;
import com.cb.platform.yq.api.entity.ApiUserDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;

/**
 * 客户端信息接口
 * @author whh
 */
public interface ApiClientInfoDao extends JpaRepository<ApiClientInfoDo,String>,Serializable {

    /**
     * 根据客户单id取的客户端信息
     * @param appId
     * @return
     */
    @Query(value="select * from api_client_info where app_id=:app_id limit 1", nativeQuery=true)
    ApiClientInfoDo getByAppId(@Param("app_id") String appId);
}
