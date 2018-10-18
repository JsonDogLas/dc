package com.cb.platform.yq.api.dao;

import com.cb.platform.yq.api.entity.ApiSignFileDo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

/**
 * 签名文件数据库层
 * @author whh
 */
public interface ApiSignFileDao extends JpaRepository<ApiSignFileDo,String>,Serializable {
}
