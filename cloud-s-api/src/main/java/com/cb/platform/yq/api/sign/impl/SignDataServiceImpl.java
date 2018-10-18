package com.cb.platform.yq.api.sign.impl;

import abc.util.StringUtils;
import com.cb.platform.yq.api.entity.ApiGFileDo;
import com.cb.platform.yq.api.entity.ApiSignFileDo;
import com.cb.platform.yq.api.entity.ApiSignFileLogDo;
import com.cb.platform.yq.api.entity.ApiUserDo;
import com.cb.platform.yq.api.enums.FileStatusEnum;
import com.cb.platform.yq.api.enums.FileTypeEnum;
import com.cb.platform.yq.api.service.ApiGFileDoService;
import com.cb.platform.yq.api.service.ApiSignFileLogService;
import com.cb.platform.yq.api.service.ApiSignFileService;
import com.cb.platform.yq.api.service.ApiUserDoService;
import com.cb.platform.yq.api.sign.SignDataService;
import com.cb.platform.yq.api.sign.bean.impl.SignContext;
import com.cb.platform.yq.api.utils.ID;
import com.cb.platform.yq.base.customsign.entity.SignFileInfo;
import com.cb.platform.yq.base.filepath.enums.FileVisitEnum;
import com.cb.platform.yq.base.filepath.service.YqSystemFilePathImpl;
import com.ceba.base.exception.BusinessErrorInfoException;
import com.ceba.base.utils.IDSDateUtils;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * 签名数据服务
 * @author whh
 */
@Service
public class SignDataServiceImpl implements SignDataService {

    @Autowired
    private ApiUserDoService apiUserDoService;

    @Autowired
    private ApiGFileDoService apiGFileDoService;

    @Autowired
    private ApiSignFileService apiSignFileService;

    @Autowired
    private ApiSignFileLogService apiSignFileLogService;
    /**
     * 查询接口用户
     *
     * @param clientId
     * @param keyId
     * @return
     */
    @Override
    public ApiUserDo sreachApiUser(String clientId, String keyId) throws BusinessErrorInfoException {
        return apiUserDoService.sreachApiUser(clientId,keyId);
    }

    /**
     * 创建签名用户
     *
     * @param clientId
     * @param keyId
     * @return
     */
    @Override
    public ApiUserDo createApiUser(String clientId, String keyId) throws BusinessErrorInfoException {
        ApiUserDo apiUserDo=apiUserDo(clientId,keyId);
        apiUserDoService.createApiUser(apiUserDo);
        return apiUserDo;
    }

    /**
     * 添加签名文件
     *
     * @param signContext
     * @param signFileInfo
     * @param apiUserDo
     * @throws BusinessErrorInfoException
     */
    @Override
    @Transient
    public boolean addSignFile(SignContext signContext, SignFileInfo signFileInfo, ApiUserDo apiUserDo) throws BusinessErrorInfoException {
        ApiSignFileDo apiSignFileDo=new ApiSignFileDo();
        apiSignFileDo.setId(signContext.getSignSessionId());
        apiSignFileDo.setTitle(signContext.getSignGetReadyData().pageTitle());
        apiSignFileDo.setStatus(FileStatusEnum.SHOW.getFlag());
        apiSignFileDo.setSignFileId(ID.getGuid());
        apiSignFileDo.setCreateUser(apiUserDo.getId());
        apiSignFileDo.setCreateTime(IDSDateUtils.getNowTime(null));
        ApiGFileDo apiGFileDo=apiGFileDo(signContext.getUploadFilePath(),apiUserDo.getId(),signFileInfo.getFileId());
        apiGFileDo.setId(signFileInfo.getFileId());
        apiGFileDo.setType(FileTypeEnum.API_TEMP.getFlag());
        apiSignFileService.insertApiSignFileDo(apiSignFileDo);
        apiGFileDoService.createApiGFileDo(apiGFileDo);
        signContext.setNewApiGFileDo(apiGFileDo);
        return true;
    }

    private ApiGFileDo apiGFileDo(String filePath,String apiUserId,String fileId){
        ApiGFileDo apiGFileDo=new ApiGFileDo();
        if(StringUtils.isEmpty(fileId)){
            fileId=ID.getGuid();
        }
        apiGFileDo.setId(fileId);
        File file=new File(filePath);
        apiGFileDo.setFileName(file.getName());
        int position=file.getName().lastIndexOf(".")+1;
        apiGFileDo.setExtName(file.getName().substring(position,file.getName().length()));
        apiGFileDo.setSize(ObjectUtils.toString(file.length()));
        apiGFileDo.setCreateTime(IDSDateUtils.getNowTime(null));
        apiGFileDo.setCreateUser(apiUserId);
        apiGFileDo.setVirtualPath(YqSystemFilePathImpl.getUpfileFilePath().changeFileVisit(FileVisitEnum.ABSOLUTE,FileVisitEnum.VIRTUAL,filePath));
        return apiGFileDo;
    }


    private ApiUserDo apiUserDo(String clientId, String keyId){
        ApiUserDo apiUserDo=new ApiUserDo();
        apiUserDo.setAppId(clientId);
        apiUserDo.setKeyId(keyId);
        apiUserDo.setId(ID.getGuid());
        apiUserDo.setCreateTime(IDSDateUtils.getNowTime(null));
        return apiUserDo;
    }
    /**
     * 创建文件
     *
     * @param filePath  文件路径
     * @param apiUserId 用户id
     * @return
     */
    @Override
    public ApiGFileDo createApiGFileDo(String filePath, String apiUserId,FileTypeEnum fileTypeEnum) throws BusinessErrorInfoException {
        ApiGFileDo apiGFileDo=apiGFileDo(filePath,apiUserId,ID.getGuid());
        apiGFileDo.setType(fileTypeEnum.getFlag());
        apiGFileDoService.createApiGFileDo(apiGFileDo);
        return apiGFileDo;
    }

    /**
     * 创建签名数据
     *
     * @param signFileLogDO
     */
    @Override
    public boolean insertSignFileLog(ApiSignFileLogDo signFileLogDO)  throws BusinessErrorInfoException{
        return apiSignFileLogService.insertSignFileLog(signFileLogDO);
    }

    /**
     * 查询文件
     *
     * @param filePath
     * @param fileTypeEnum
     * @return
     * @throws BusinessErrorInfoException
     */
    @Override
    public ApiGFileDo sreachApiGFileDo(String filePath, FileTypeEnum fileTypeEnum) throws BusinessErrorInfoException {
        return apiGFileDoService.sreachApiGFileDo(filePath,fileTypeEnum.getFlag());
    }
}
