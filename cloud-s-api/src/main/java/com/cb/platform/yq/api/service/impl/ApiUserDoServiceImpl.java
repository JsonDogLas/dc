package com.cb.platform.yq.api.service.impl;

import com.cb.platform.yq.api.dao.ApiUserDao;
import com.cb.platform.yq.api.dto.ApiUserDto;
import com.cb.platform.yq.api.entity.ApiUserDo;
import com.cb.platform.yq.api.service.ApiUserDoService;
import com.cb.platform.yq.base.exception.ApiErrorEnum;
import com.ceba.base.exception.BusinessErrorInfoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 用户服务
 * @author whh
 */
@Service
public class ApiUserDoServiceImpl implements ApiUserDoService {

    @Autowired
    private ApiUserDao apiUserDao;
    /**
     * 查询接口用户
     * @param clientId
     * @param keyId
     * @return
     */
    @Override
    public ApiUserDo sreachApiUser(String clientId, String keyId)throws BusinessErrorInfoException {
        try{
            return apiUserDao.sreachApiUser(clientId,keyId);
        }catch (Exception e){
            throw new BusinessErrorInfoException(ApiErrorEnum.QUERY_SIGN_USER_ERROR);
        }
    }

    /**
     * 创建签名用户
     *
     * @param apiUserDo
     * @return
     */
    @Override
    public boolean createApiUser(ApiUserDo apiUserDo) throws BusinessErrorInfoException{
        try{
            ApiUserDo apiUserDo_ = apiUserDao.save(apiUserDo);
            if(apiUserDo_ != null){
                return true;
            }
            return false;
        }catch (Exception e){
            throw new BusinessErrorInfoException(ApiErrorEnum.CE_SIGN_USER_ERROR);
        }

    }

    /**
     * 查询用户信息
     * @param s
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        ApiUserDo apiUserDo= apiUserDao.findById(s).get();
        return new ApiUserDto(apiUserDo);
    }
}
