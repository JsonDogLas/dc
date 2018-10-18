package com.cb.platform.yq.api.dao;

import com.cb.platform.yq.api.entity.ApiUserDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;

/**
 * api调用者
 * 云签接口用户 根据客户端 id + keyId  生成的用户
 */
public interface ApiUserDao extends JpaRepository<ApiUserDo,String>,Serializable{

    /**
     * 查询 云签API用户
     * @param clientId
     * @param keyId
     * @return
     */
    @Query(value="select * from api_user where app_id=:app_id and key_id=:key_id limit 1", nativeQuery=true)
    ApiUserDo sreachApiUser(@Param("app_id") String clientId, @Param("key_id") String keyId);
}
