package com.cb.platform.yq.api.dao;

import com.cb.platform.yq.api.entity.ApiSignFileLogDo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

/**
 * 签名日志数据库层
 * @author whh
 */
public interface ApiSignFileLogDao extends JpaRepository<ApiSignFileLogDo,String>,Serializable {
}
