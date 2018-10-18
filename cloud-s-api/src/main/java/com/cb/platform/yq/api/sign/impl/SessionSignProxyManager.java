package com.cb.platform.yq.api.sign.impl;

import com.alibaba.fastjson.JSON;
import com.cb.platform.yq.api.entity.ApiGFileDo;
import com.cb.platform.yq.api.entity.ApiSignFileLogDo;
import com.cb.platform.yq.api.enums.FileTypeEnum;
import com.cb.platform.yq.api.enums.SignTypeEnum;
import com.cb.platform.yq.api.sign.*;
import com.cb.platform.yq.api.sign.bean.SignGetReadyData;
import com.cb.platform.yq.api.sign.bean.StampInfo;
import com.cb.platform.yq.api.sign.bean.impl.*;
import com.cb.platform.yq.api.utils.ID;
import com.cb.platform.yq.api.utils.KeyUtils;
import com.cb.platform.yq.api.utils.ResultUtils;
import com.cb.platform.yq.api.vo.UploadStampVO;
import com.cb.platform.yq.base.Result;
import com.cb.platform.yq.base.customsign.entity.*;
import com.cb.platform.yq.base.customsign.enums.ResultStatusEnum;
import com.cb.platform.yq.base.customsign.enums.StampFromEnum;
import com.cb.platform.yq.base.customsign.service.SignService;
import com.cb.platform.yq.base.customsign.service.fileImage.LoadFileImageBase;
import com.cb.platform.yq.base.exception.ApiErrorEnum;
import com.cb.platform.yq.base.exception.ApiTipEnum;
import com.cb.platform.yq.base.filepath.constant.SystemProperties;
import com.cb.platform.yq.base.filepath.enums.FileVisitEnum;
import com.cb.platform.yq.base.filepath.enums.YqFilePathEnum;
import com.cb.platform.yq.base.filepath.service.YqSystemFilePathImpl;
import com.ceba.base.enums.CertificationLevelEnum;
import com.ceba.base.enums.UserSignTypeEnum;
import com.ceba.base.exception.IDSException;
import com.ceba.base.utils.IDSDateUtils;
import com.ceba.base.utils.IDSFileUtils;
import com.ceba.base.web.response.IResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletionService;
import java.util.concurrent.Future;

/**
 * 签名会话管理
 *
 * 签名页面需要调用的接口
 *
 * 上传文件
 *
 * 转pdf  和  pdf转图片  取章  异步线程解决
 *
 * 打开页面的时候
 *
 * 签名的时候
 * @author whh
 */
@Service
public class SessionSignProxyManager implements SignManager {
    public static Logger logger = LoggerFactory.getLogger(SessionSignProxyManager.class);
    /**
     * 线程池
     */
    @Autowired
    private CompletionService<IResult> completionService;
    /**
     * 签名向下文管理
     */
    @Autowired
    private SignContextManager signContextManager;
    /**
     * 签名数据服务层  负责与 DAO 交互
     */
    @Autowired
    private SignDataService signDataService;

    /**
     * 线程内存
     */
    @Autowired
    private MemoryFutureManager memoryFutureManager;


    @Autowired
    private SignService signService;

    /**
     * 签名
     * @return
     */
    @Override
    public IResult sign(SignRequestDTO signRequestDTO) {
        //验证key的是不是ceba的key
        try{
            if(StringUtils.isNotEmpty(signRequestDTO.getKeyInfo())){
                if(!KeyUtils.isKeyUse(signRequestDTO.getKeyInfo())){
                    Result result= ResultUtils.failMessageLog(logger,ApiTipEnum.CE_INDEX_CEBA_KEY_ERROR);
                    return ResultUtils.iResult(result);
                }else{
                    SystemProperties.log(logger,"文件签名，验证key成功");
                }
            }else{
                SystemProperties.log(logger,"文件签名，没有验证key");
            }
        }catch (Exception e){
            Result result=  ResultUtils.exceptionMessageLog(logger,ApiErrorEnum.CE_INDEX_CEBA_KEY_EXCEPTION,e);
            return ResultUtils.iResult(result);
        }
        ApiLoadSignData loadSignDataBase=new ApiLoadSignData();
        IResult iResult=signService.sign(signRequestDTO,loadSignDataBase);
        if(ResultStatusEnum.SB.getCode()==iResult.getResultCode()){
            return iResult;
        }
        //更新签章缩放比例
        Map<String,String> map=new HashMap<>();
        for(SignPrintPDF signPrintPDF:signRequestDTO.getSignPrintPdfList()){
            if(signPrintPDF != null && StringUtils.isNotEmpty(signPrintPDF.getId()) && map.get(signPrintPDF.getId()) == null){
                map.put(signPrintPDF.getId(),signPrintPDF.getId());
                SignContext signContext=signContextManager.get(signRequestDTO.getVirtualSignFileId());
                List<Stamp> list=signContext.getStampList();
                for(Stamp stamp:list){
                    if(StringUtils.equals(stamp.getId(),signPrintPDF.getId())){
                        //zoom 是代表页面 以 75 来放大
                        float zoom=NumberUtils.toFloat(signPrintPDF.getZoomRate());
                        stamp.setImgWidth(signPrintPDF.getImageWidth()*zoom);
                        stamp.setImgHeight(signPrintPDF.getImageHeight()*zoom);
                    }
                }
            }
        }
        SystemProperties.log(logger,"签名——签名结果："+ JSON.toJSONString(iResult));
        //插入签名后的ID 文件
        SignFileInfo signFileInfo=(SignFileInfo)iResult.getData();
        String absolutePath= YqSystemFilePathImpl.getUpfileFilePath().changeFileVisit(FileVisitEnum.VIRTUAL,FileVisitEnum.ABSOLUTE,signFileInfo.getVirtualPath());
        ApiGFileDo apiGFileDo=signDataService.createApiGFileDo(absolutePath,signRequestDTO.getUserId(), FileTypeEnum.API_TEMP);
        SignContext signContext=signContextManager.get(signRequestDTO.getVirtualSignFileId());
        signContext.setNewApiGFileDo(apiGFileDo);
        signFileInfo.setFileId(apiGFileDo.getId());
        //更新签名文件信息
        Result loadSignDataResult = signContext.getLoadSignDataResult();
        if (loadSignDataResult != null && loadSignDataResult.getResultCode() == ResultStatusEnum.CG.getCode()) {
            LoadSignDataResponse loadSignDataResponse=(LoadSignDataResponse)loadSignDataResult.getData();
            loadSignDataResponse.setSignFileInfo(signFileInfo);
        }
        signContextManager.put(signRequestDTO.getVirtualSignFileId(),signContext);
        //插入签名日志
        try {
            insertSignFileLog(signRequestDTO,apiGFileDo.getId());
        } catch (Exception e) {
            Result result=ResultUtils.exceptionMessageLog(logger,ApiErrorEnum.CST_INSERT_SIGN_FILE_LOG_FAIL,e);
            return ResultUtils.iResult(result);
        }
        return iResult;
    }

    /**
     * 插入签名日志
     * @param signRequestDTO
     * @param afterSignFileId
     */
    private void insertSignFileLog(SignRequestDTO signRequestDTO,String afterSignFileId)throws Exception{
        ApiSignFileLogDo signFileLogDO=new ApiSignFileLogDo();
        signFileLogDO.setId(ID.getGuid());
        String signFileId=signRequestDTO.getVirtualSignFileId();
        if(StringUtils.isNotEmpty(signRequestDTO.getSignFileId())){
            signFileId=signRequestDTO.getSignFileId();
        }
        signFileLogDO.setSignFileId(signFileId);
        signFileLogDO.setOriginalFileId(signRequestDTO.getFileId());
        signFileLogDO.setAfterSignFileId(afterSignFileId);
        if(signRequestDTO.getUserSignTypeEnum() == UserSignTypeEnum.NORMAL_SIGN){
            signFileLogDO.setSignType(SignTypeEnum.ORDINARY_SIGN.getFlag());
        }else if(signRequestDTO.getCertificationLevelEnum() == CertificationLevelEnum.XD){
            signFileLogDO.setSignType(SignTypeEnum.REVISE_SIGN.getFlag());
        }else if(signRequestDTO.getCertificationLevelEnum() == CertificationLevelEnum.YZ){
            signFileLogDO.setSignType(SignTypeEnum.VALID_SIGN.getFlag());
        }
        signFileLogDO.setKeyId(signRequestDTO.getKeyId());
        Set<String> stampSet=new HashSet<>();
        StringBuffer stampIdBuffer=new StringBuffer();//签章ID集合 (sz_g_stamp) 去重，多个用逗号隔开
        for(SignPrintPDF signPrintPDF:signRequestDTO.getSignPrintPdfList()){
            /*GStampDO gStampDO=stampService.getById(signPrintPDF.getId());
            if(gStampDO == null){
                String absolutePath= YqSystemFilePathImpl.getUpfileFilePath().changeFileVisit(FileVisitEnum.BROWSER,FileVisitEnum.ABSOLUTE,signPrintPDF.getImagePath());
                String virtualPath= YqSystemFilePathImpl.getUpfileFilePath().changeFileVisit(FileVisitEnum.BROWSER,FileVisitEnum.VIRTUAL,signPrintPDF.getImagePath());
                String extName=virtualPath.substring(virtualPath.lastIndexOf(".")+1,virtualPath.length());
                File file=new File(absolutePath);
                GFileDO gFileDO=new GFileDO(ID.getGuid(),file.getName(),extName,
                        virtualPath,file.length(),"", GFileTypeEnum.SZ_YZ.getFlag(),signRequestDTO.getUserId(),IDSDateUtils.getNowTime(null));
                gStampDO=new GStampDO(signPrintPDF.getId(),
                        null,
                        gFileDO.getId(),
                        gFileDO.getFileName(),
                        StampTypeEnum.SELF.getCode(),
                        "");
                gStampDO.setCreateTime(IDSDateUtils.getNowTime(null));
                gStampDO.setCreateUser(signRequestDTO.getUserId());
                gStampDO.setUpdateUser(signRequestDTO.getUserId());
                gStampDO.setUpdateTime(IDSDateUtils.getNowTime(null));
                gFileService.insertGFile(gFileDO);
                stampService.insertGstamp(gStampDO);
            }*/
            stampSet.add(signPrintPDF.getId());
        }
        Iterator<String> stampIterator = stampSet.iterator();
        while(stampIterator.hasNext()){
            stampIdBuffer.append(stampIterator.next());
        }
        signFileLogDO.setCreateUser(signRequestDTO.getUserId());
        signFileLogDO.setCreateTime(IDSDateUtils.getNowTime(null));
        signFileLogDO.setStampFileIds(stampIdBuffer.toString());
        signFileLogDO.setKeyId(signRequestDTO.getKeyId());
        signDataService.insertSignFileLog(signFileLogDO);
    }

    /**
     * 加载签名数据
     *
     * @param signSessionId
     * @return
     */
    @Override
    public IResult loadSignData(String signSessionId) {
        SignContext signContext=signContextManager.get(signSessionId);
        if(signContext == null){
            Result result=ResultUtils.failMessageLog(logger,ApiTipEnum.OPEN_SIGN_PAGE_OUT_TIME);
            return ResultUtils.iResult(result);
        }
        if(signContext.getLoadSignDataResult() != null){
            SystemProperties.log(logger,"签名会话【"+signContext.getSignSessionId()+"】的 重复 取出线程返回数据");
            //重新转图片
            Object object =signContext.getLoadSignDataResult().getData();
            if(object instanceof LoadSignDataResponse && signContext.getLoadSignDataResult().getResultCode() == ResultStatusEnum.CG.getCode()) {
                //更新平台印章
                SignDataLoadCallable signDataLoadCallable=new SignDataLoadCallable(signContext,this);
                signDataLoadCallable.platTackStamp(signContext.getNewApiGFileDo().getCreateUser(),signContext.getFilePaltKey());

                //更新签名页面图片
                LoadSignDataResponse loadSignDataResponse = (LoadSignDataResponse) object;
                try{
                    SignFileInfoDTO signFileInfoDTO=new SignFileInfoDTO();
                    //获取原文件
                    String path=YqSystemFilePathImpl.getUpfileFilePath().changeFileVisit(FileVisitEnum.VIRTUAL,
                            FileVisitEnum.ABSOLUTE,signContext.getNewApiGFileDo().getVirtualPath());
                    signFileInfoDTO.setOriginalPath(path);
                    signFileInfoDTO.setFileName(signContext.getNewApiGFileDo().getFileName());
                    signContext.setUploadFilePath(path);
                    signFileInfoDTO.setSignFileInfo(loadSignDataResponse.getSignFileInfo());
                    signFileInfoDTO.setFileId(signContext.getSignSessionId());
                    signFileInfoDTO.setVirtualSignFileId(signContext.getSignSessionId());
                    signFileInfoDTO.setSignFileId(signContext.getSignSessionId());
                    ApiLoadSignData loadSignDataBase=new ApiLoadSignData();
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

                //取出手写体签章的id集合
                if(signContext.getCreateStamp() != null && (CollectionUtils.isNotEmpty(signContext.getCreateStamp().getUploadStampList()) ||
                        CollectionUtils.isNotEmpty(signContext.getCreateStamp().getResultStampList()))){
                    List<Stamp> list=signContext.getCreateStamp().getUploadStampList();
                    List<Stamp> lists=signContext.getCreateStamp().getResultStampList();
                    loadSignDataResponse.setHandWriteStamp(new ArrayList<>());
                    for(Stamp stamp:list){
                        loadSignDataResponse.getHandWriteStamp().add(stamp.getId());
                    }
                    for(Stamp stamp:lists){
                        loadSignDataResponse.getHandWriteStamp().add(stamp.getId());
                    }
                }

                //取出更新平台的印章
                List<Stamp> stampList=signDataLoadCallable.getStampManager().tackStamp().tackStamp();

                //后台的写法是 无论如何 都会返回章  出错 就返回以前的章  未错误 就返回更新的章
                if(CollectionUtils.isNotEmpty(signContext.getStampList())){
                    for(int index=(signContext.getStampList().size()-1);index > -1;index--){
                        Stamp stamp=signContext.getStampList().get(index);
                        if(StampFromEnum.FILE_PLAT.getNumber().equals(stamp.getStampFromFlag())
                                || StampFromEnum.THREE_PLAT.getNumber().equals(stamp.getStampFromFlag())){
                            signContext.getStampList().remove(index);
                        }
                    }
                }

                if(CollectionUtils.isNotEmpty(stampList)){
                    signContext.getStampList().addAll(stampList);
                }
                loadSignDataResponse.setStampList(signContext.getStampList());
            }
            //放入到内存中
            if(signContextManager instanceof MemorySignContextManager){
                MemorySignContextManager memorySignContextManager=(MemorySignContextManager)signContextManager;
                memorySignContextManager.memoryPut(signSessionId,signContext);
                signContext.setUpdateTime();
            }
            signContextManager.put(signSessionId,signContext);
            return ResultUtils.iResult(signContext.getLoadSignDataResult());
        }
        Future<IResult> future=memoryFutureManager.get(signSessionId);
        try {
            IResult iResult =null;
            if(future != null){
                iResult=future.get();
                memoryFutureManager.remove(signContext.getSignSessionId());
            }
            if(iResult == null){
                Result result=ResultUtils.failMessageLog(logger,ApiTipEnum.LOAD_SIGN_DATA_ERROR);
                return ResultUtils.iResult(result);
            }else{
                SystemProperties.log(logger,"签名会话【"+signContext.getSignSessionId()+"】的取出线程返回数据");
                signContext.setLoadSignDataResult(ResultUtils.result(iResult));
                signContext.setUpdateTime();
                //放入到内存中
                if(signContextManager instanceof MemorySignContextManager){
                    MemorySignContextManager memorySignContextManager=(MemorySignContextManager)signContextManager;
                    memorySignContextManager.memoryPut(signSessionId,signContext);
                    signContext.setUpdateTime();
                }
                signContextManager.put(signSessionId,signContext);
            }
            return iResult;
        } catch (Exception e) {
            Result result=ResultUtils.exceptionMessageLog(logger,ApiErrorEnum.SIGN_LOAD_DATA_THREAD_EXCEPTION,e);
            return ResultUtils.iResult(result);
        }
    }

    /**
     * 加载签名文件图片
     * @param loadFileImageReqeust
     * @return
     */
    @Override
    public IResult loadFileImage(LoadFileImageReqeust loadFileImageReqeust) {
        LoadFileImageBase loadFileImageBase= new ApiLoadFileImage();
        IResult iResult=new IResult();
        iResult.setResultCode(ResultStatusEnum.CG.getCode());
        PdfImageParam pdfImageParam = loadFileImageBase.loadFileImage(loadFileImageReqeust);
        if(!pdfImageParam.isFlag()){
            iResult.setResultCode(ResultStatusEnum.SB.getCode());
            iResult.setMessage(pdfImageParam.getMessage());
        }else{
            //改变缓存中的SignContext
            iResult.setData(pdfImageParam);
        }
        return iResult;
    }

    /**
     * 上传签名文件信息
     * @param signGetReadyData
     * @return
     */
    @Override
    public Result uploadFile(SignGetReadyData signGetReadyData) {
        //异步开启线程 创建key用户 ，转pdf,转图片等耗时操作
        SignContext signContext=new SignContext();
        signContext.setUploadFilePath(signGetReadyData.uploadFilePath());
        signContext.setSignSessionId(ID.getGuid());
        signContext.setSignGetReadyData(signGetReadyData);
        SignDataLoadCallable signDataLoadCallable=new SignDataLoadCallable(signContext,this);
        Future<IResult> future=completionService.submit(signDataLoadCallable);
        memoryFutureManager.put(signContext.getSignSessionId(),future);
        if(signContextManager instanceof MemorySignContextManager){
            MemorySignContextManager memorySignContextManager=(MemorySignContextManager)signContextManager;
            signContext.setUpdateTime();
            memorySignContextManager.memoryPut(signContext.getSignSessionId(),signContext);
        }
        return ResultUtils.success(signContext.getSignSessionId());
    }

    /**
     * 根据签名会话返回签名文件
     * @param signSessionId
     * @return
     */
    @Override
    public Result signData(String signSessionId) {
        SignContext signContext=signContextManager.get(signSessionId);
        if(signContext == null){
            //WORKFLOW_SIGN_SESSION_FAIL=签名会话丢失
            return ResultUtils.failMessageLog(logger,ApiTipEnum.WORKFLOW_SIGN_SESSION_FAIL);
        }
        String virtualPath=signContext.getNewApiGFileDo().getVirtualPath();
        String browserPath=YqSystemFilePathImpl.getUpfileFilePath().changeFileVisit(FileVisitEnum.VIRTUAL,FileVisitEnum.BROWSER,virtualPath);
        SignCompleteData signCompleteData=new SignCompleteData();
        signCompleteData.setDownloadFileUrl(browserPath);
        if(signContext.isPermission("ROLE_API_OPERATE_INFO")){
            List<Stamp> stampList=signContext.getStampList();
            List<StampDTO> stampDTOList=new ArrayList<>();
            for(Stamp stamp:stampList){
                StampDTO stampDTO=StampDTO.stampToStampDTO(stamp);
                stampDTOList.add(stampDTO);
            }
            signCompleteData.setStampList(stampDTOList);
        }
        //清除内存中的数据
        if(signContextManager instanceof MemorySignContextManager){
            MemorySignContextManager memorySignContextManager=(MemorySignContextManager)signContextManager;
            memorySignContextManager.memoryRemove(signSessionId);
        }
        return ResultUtils.successLog(logger,signCompleteData,"签名获取返回数据成功");
    }

    /**
     * 上传印章
     * @param signSessionId 签名会话id
     * @param uploadStampVOList
     * @return
     */
    @Override
    public IResult uploadStamp(String signSessionId, List<UploadStampVO> uploadStampVOList) {
        if(CollectionUtils.isEmpty(uploadStampVOList)){
            Result result=ResultUtils.failMessageLog(logger,ApiTipEnum.UPLOAD_STAMP_NULL);
            return ResultUtils.iResult(result);
        }
        SignContext signContext=signContextManager.get(signSessionId);
        if(signContext == null){
            Result result=ResultUtils.failMessageLog(logger,ApiTipEnum.OPEN_SIGN_PAGE_OUT_TIME);
            return ResultUtils.iResult(result);
        }
        if(!signContext.isPermission("ROLE_API_OPERATE_UPLOAD")) {
            Result result=ResultUtils.failMessageLog(logger,ApiTipEnum.ME_UPLOAD_STAMP_FAIL);
            return ResultUtils.iResult(result);
        }
        UploadStampTemplate uploadStampTemplate=new UploadStampTemplate(signDataService,uploadStampVOList,signContext.getNewApiGFileDo().getCreateUser());
        StampInfo stampInfo=uploadStampTemplate.tackStamp();
        signContext.getStampList().addAll(stampInfo.tackStamp());
        signContextManager.put(signSessionId,signContext);
        SystemProperties.log(logger,"上传印章成功");
        IResult iResult=new IResult(true);
        iResult.setMessage(ApiTipEnum.UPLOAD_STAMP_SUCCESS.getMessage());
        return iResult;
    }

    public CompletionService<IResult> getCompletionService() {
        return completionService;
    }

    public void setCompletionService(CompletionService<IResult> completionService) {
        this.completionService = completionService;
    }

    public SignContextManager getSignContextManager() {
        return signContextManager;
    }

    public void setSignContextManager(SignContextManager signContextManager) {
        this.signContextManager = signContextManager;
    }

    public SignDataService getSignDataService() {
        return signDataService;
    }

    public void setSignDataService(SignDataService signDataService) {
        this.signDataService = signDataService;
    }
}
