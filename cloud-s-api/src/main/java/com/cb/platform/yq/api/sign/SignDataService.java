package com.cb.platform.yq.api.sign;

import com.cb.platform.yq.api.entity.ApiGFileDo;
import com.cb.platform.yq.api.entity.ApiSignFileLogDo;
import com.cb.platform.yq.api.entity.ApiUserDo;
import com.cb.platform.yq.api.enums.FileTypeEnum;
import com.cb.platform.yq.api.sign.bean.impl.SignContext;
import com.cb.platform.yq.base.customsign.entity.SignFileInfo;
import com.ceba.base.exception.BusinessErrorInfoException;

/**
 * 签名数据服务
 * @author w
 */
public interface SignDataService {

    /**
     * 查询接口用户
     * @param clientId
     * @param keyId
     * @return
     */
    ApiUserDo sreachApiUser(String clientId,String keyId)throws BusinessErrorInfoException;


    /**
     * 创建签名用户
     * @param clientId
     * @param keyId
     * @return
     */
    ApiUserDo createApiUser(String clientId, String keyId)throws BusinessErrorInfoException;


    /**
     * 添加签名文件
     * @param signContext
     * @param signFileInfo
     * @param apiUserDo
     * @throws BusinessErrorInfoException
     */
    boolean addSignFile(SignContext signContext, SignFileInfo signFileInfo, ApiUserDo apiUserDo)throws BusinessErrorInfoException;

    /**
     * 创建文件
     * @param filePath 文件路径
     * @param apiUserId 用户id
     * @return
     */
    ApiGFileDo createApiGFileDo(String filePath, String apiUserId,FileTypeEnum fileTypeEnum)throws BusinessErrorInfoException;

    /**
     * 查询文件
     * @param filePath
     * @param fileTypeEnum
     * @return
     * @throws BusinessErrorInfoException
     */
    ApiGFileDo sreachApiGFileDo(String filePath,FileTypeEnum fileTypeEnum)throws BusinessErrorInfoException;

    /**
     * 创建签名数据
     * @param signFileLogDO
     */
    boolean insertSignFileLog(ApiSignFileLogDo signFileLogDO)throws BusinessErrorInfoException;
}
