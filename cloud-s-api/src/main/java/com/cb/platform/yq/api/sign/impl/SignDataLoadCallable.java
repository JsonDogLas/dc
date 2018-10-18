package com.cb.platform.yq.api.sign.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cb.platform.yq.api.entity.ApiUserDo;
import com.cb.platform.yq.api.sign.SignDataService;
import com.cb.platform.yq.api.sign.StampManager;
import com.cb.platform.yq.api.sign.bean.SignGetReadyData;
import com.cb.platform.yq.api.sign.bean.impl.SignContext;
import com.cb.platform.yq.api.sign.bean.impl.SignFileInfoDTO;
import com.cb.platform.yq.api.utils.ManagerHttpRequestUtils;
import com.cb.platform.yq.api.utils.ResultUtils;
import com.cb.platform.yq.base.Result;
import com.cb.platform.yq.base.customsign.entity.LoadSignDataResponse;
import com.cb.platform.yq.base.customsign.entity.PdfImageParam;
import com.cb.platform.yq.base.customsign.entity.SignFileInfo;
import com.cb.platform.yq.base.customsign.entity.Stamp;
import com.cb.platform.yq.base.exception.ApiErrorEnum;
import com.cb.platform.yq.base.exception.ApiTipEnum;
import com.cb.platform.yq.base.filepath.constant.SystemProperties;
import com.ceba.base.exception.BusinessErrorInfoException;
import com.ceba.base.exception.IDSException;
import com.ceba.base.web.response.IResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 签名加载数据异步线程
 * @author whh
 */
public class SignDataLoadCallable implements Callable<IResult> {
    private SignContext signContext;
    private SessionSignProxyManager sessionSignProxyManager;
    public static Logger logger = LoggerFactory.getLogger(SignDataLoadCallable.class);
    private StampManager stampManager;


    public SignDataLoadCallable(SignContext signContext,SessionSignProxyManager sessionSignProxyManager){
        this.signContext=signContext;
        this.sessionSignProxyManager=sessionSignProxyManager;
        this.stampManager=new BaseStampManager(sessionSignProxyManager.getCompletionService());
    }
    /**
     * 处理 创建用户 转pdf 转图片 等耗时操作
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public IResult call() throws Exception {
        try{
            return _call();
        }catch (BusinessErrorInfoException e){
            ApiErrorEnum apiErrorEnum=(ApiErrorEnum) e.getErrorInfo();
            Result result = ResultUtils.exceptionMessageLog(logger,apiErrorEnum,e);
            result.setData(signContext.getSignSessionId());
            return ResultUtils.iResult(result);
        }
    }


    private IResult _call()throws BusinessErrorInfoException{
        //创建签名用户=客户端id+keyId
        SystemProperties.log(logger,"签名会话【"+signContext.getSignSessionId()+"】的加载签名数据线程已经启动");
        SignDataService signDataService=sessionSignProxyManager.getSignDataService();
        ApiUserDo apiUserDo=signDataService.sreachApiUser(signContext.getSignGetReadyData().clientId(),
                signContext.getSignGetReadyData().keyId());
        if(apiUserDo == null){
            //创建apiUser用户
            apiUserDo=signDataService.createApiUser(signContext.getSignGetReadyData().clientId(),
                    signContext.getSignGetReadyData().keyId());
            if(apiUserDo == null){
                Result result = ResultUtils.failMessageLog(logger, ApiTipEnum.CREATE_API_USER_ERROR,"签名会话【"+signContext.getSignSessionId()+"】的加载签名数据线程不能创建用户");
                result.setData(signContext.getSignSessionId());
                return ResultUtils.iResult(result);
            }
        }
        //异步加载印章
        boolean isFilePaltKey=keyInfo(signContext.getSignGetReadyData().keyId());
        signContext.setFilePaltKey(isFilePaltKey);
        loadStampFuture(apiUserDo,isFilePaltKey);
        //加载文件信息
        ApiLoadSignData loadSignDataBase=new ApiLoadSignData();
        LoadSignDataResponse loadSignDataResponse=new LoadSignDataResponse();
        SignFileInfoDTO signFileInfoDTO=new SignFileInfoDTO();
        try{
            //获取原文件
            File file=new File(signContext.getUploadFilePath());
            signFileInfoDTO.setOriginalPath(signContext.getUploadFilePath());
            signFileInfoDTO.setFileName(file.getName());
            loadSignDataBase.toPdf(signFileInfoDTO);
            signContext.setUploadFilePath(signFileInfoDTO.getFilePath());
            SignFileInfo signFileInfo_=loadSignDataBase.getPdfFileInfo(signContext.getUploadFilePath());
            signFileInfoDTO.setSignFileInfo(signFileInfo_);
            signFileInfoDTO.setFileId(signContext.getSignSessionId());
            signFileInfoDTO.setVirtualSignFileId(signContext.getSignSessionId());
            signFileInfoDTO.setSignFileId(signContext.getSignSessionId());
            if(signContext.isPermission("ROLE_API_OPERATE_TITLE")){
                signFileInfoDTO.setTitle(signContext.getSignGetReadyData().pageTitle());
            }
        }catch (IDSException idsException){
            Result result = ResultUtils.errorMessage(logger,idsException.getResultInfo());
            result.setData(signContext.getSignSessionId());
            return ResultUtils.iResult(result);
        }
        SystemProperties.log(logger,"签名会话【"+signContext.getSignSessionId()+"】的加载签名数据线程加载文件信息完成");
        //加载页面图片
        try{
            PdfImageParam pdfImageParam=loadSignDataBase.loadFileImage(signFileInfoDTO);
            if(pdfImageParam.isFlag()){
                loadSignDataResponse.setPdfImageList(pdfImageParam.getPdfImageList());
            }else{
                Result result = ResultUtils.errorMessage(logger,pdfImageParam.getMessage());
                result.setData(signContext.getSignSessionId());
                return ResultUtils.iResult(result);
            }
        }catch(IDSException idsException){
            Result result = ResultUtils.errorMessage(logger,idsException.getResultInfo());
            result.setData(signContext.getSignSessionId());
            return ResultUtils.iResult(result);
        }
        SystemProperties.log(logger,"签名会话【"+signContext.getSignSessionId()+"】的加载签名数据线程加载页面图片完成");
        //加载签章
        signContext.setStampList(stampManager.tackStamp().tackStamp());
        loadSignDataResponse.setHandSignKey(signContext.getSignSessionId());
        loadSignDataResponse.setApplyStampFlag(false);
        loadSignDataResponse.setSignFileInfo(signFileInfoDTO);
        loadSignDataResponse.setStampList(signContext.getStampList());
        loadSignDataResponse.setPermissionList(signContext.getSignGetReadyData().authority());
        loadSignDataResponse.setApplyStampFlag(isFilePaltKey);
        SystemProperties.log(logger,"签名会话【"+signContext.getSignSessionId()+"】的加载签名数据线程加载签章完成");
        //业务处理
        boolean bool=signDataService.addSignFile(signContext,signFileInfoDTO,apiUserDo);
        if(!bool){
            Result result = ResultUtils.failMessageLog(logger,ApiTipEnum.ADD_SIGN_FILE_ERROR);
            result.setData(signContext.getSignSessionId());
            return ResultUtils.iResult(result);
        }
        sessionSignProxyManager.getSignContextManager().put(signContext.getSignSessionId(),signContext);
        Result result =  ResultUtils.successLog(logger,loadSignDataResponse,"签名会话【"+signContext.getSignSessionId()+"】的加载签名数据线程业务处理完成");
        return ResultUtils.iResult(result);
    }


    /**
     * 异步加载签章签章
     * @param apiUserDo
     */
    public void loadStampFuture(ApiUserDo apiUserDo,boolean isFilePaltKey){
        SignDataService signDataService=sessionSignProxyManager.getSignDataService();
        SignGetReadyData signGetReadyData = signContext.getSignGetReadyData();
        //自定义签章
        CustomStampTemplate customStampTemplate=new CustomStampTemplate(
                signDataService,
                apiUserDo.getId(),
                signGetReadyData.qrcodeStampZoom(),
                signGetReadyData.dateStampZoom(),
                signContext
                );
        stampManager.addStampTemplate(customStampTemplate);
        //平台获取印章
        platTackStamp(apiUserDo.getId(),isFilePaltKey);
    }

    /**
     *  平台获取印章
     * @param userId
     * @param isFilePaltKey
     * @return
     */
    public void platTackStamp(String userId,boolean isFilePaltKey){
        SignDataService signDataService=sessionSignProxyManager.getSignDataService();
        //第三方加载签章
        SignGetReadyData signGetReadyData = signContext.getSignGetReadyData();
        ThirdPartyStampTemplate thirdPartyStampTemplate=new ThirdPartyStampTemplate(
                signDataService,
                signGetReadyData.apiClientInfoDo(),
                signGetReadyData.token(),
                signGetReadyData.keyId(),
                userId,
                signContext.getStampList()
        );
        stampManager.addStampTemplate(thirdPartyStampTemplate);
        //电子文件平台签章
        if(isFilePaltKey){
            FilePlatformStampTemplate filePlatformStampTemplate=new FilePlatformStampTemplate(
                    signGetReadyData.keyId(),
                    signDataService,
                    userId,
                    signGetReadyData.apiClientInfoDo(),
                    signContext.getStampList()
            );
            stampManager.addStampTemplate(filePlatformStampTemplate);
        }else{
            SystemProperties.log(logger,"["+signContext.getSignGetReadyData().keyId()+"]不属于电子文件平台");
        }
    }

    public boolean keyInfo(String keyId){
        Map<Object, Object> dataMap=new HashMap<>();
        dataMap.put("keyId",keyId);
        try {
            String resultStr= ManagerHttpRequestUtils.get(SystemProperties._filePlatKeyInfoUrl,dataMap);
            JSONObject jsonObject= JSON.parseObject(resultStr);
            return jsonObject.getBoolean("result");
        } catch (Exception e) {
            ResultUtils.exceptionMessageLog(logger,ApiErrorEnum.CREATE_API_USER_ERROR,e,"取得子文件平台key信息异常");
            return false;
        }
    }

    public StampManager getStampManager() {
        return stampManager;
    }

    public void setStampManager(StampManager stampManager) {
        this.stampManager = stampManager;
    }
}
