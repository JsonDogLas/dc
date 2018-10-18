package com.cb.platform.yq.api.service;

import com.cb.platform.yq.api.entity.ApiUserDo;
import com.ceba.base.exception.BusinessErrorInfoException;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 用户服务接口
 * @author whh
 */
public interface ApiUserDoService extends UserDetailsService{
    /**
     * 查询接口用户
     *
     * @param clientId
     * @param keyId
     * @return
     */
    ApiUserDo sreachApiUser(String clientId, String keyId) throws BusinessErrorInfoException;

    /**
     * 创建签名用户
     *
     * @param apiUserDo
     * @return
     */
    boolean createApiUser(ApiUserDo apiUserDo) throws BusinessErrorInfoException;

}
