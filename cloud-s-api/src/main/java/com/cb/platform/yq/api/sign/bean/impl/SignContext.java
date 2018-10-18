package com.cb.platform.yq.api.sign.bean.impl;
import com.cb.platform.yq.api.entity.ApiGFileDo;
import com.cb.platform.yq.api.sign.bean.SignGetReadyData;
import com.cb.platform.yq.base.Result;
import com.cb.platform.yq.base.customsign.entity.CreateStamp;
import com.cb.platform.yq.base.customsign.entity.LoadSignDataResponse;
import com.cb.platform.yq.base.customsign.entity.Stamp;
import com.cb.platform.yq.base.customsign.enums.ResultStatusEnum;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 签名上下文
 */
public class SignContext  implements Serializable {
    /**
     * 签章信息
     */
    private List<Stamp> stampList;
    /**
     * 签名会话id (签名文件id, handWriteStamp id)
     */
    private String signSessionId;
    /**
     * 上传文件路径
     */
    private String uploadFilePath;
    /**
     * 第一次加载签名数据后返回的结果
     */
    private Result loadSignDataResult;
    /**
     * 签名准备数据
     * 准备完成后请置空 FileItem
     */
    private SignGetReadyData signGetReadyData;
    /**
     * 手写体
     */
    private CreateStamp createStamp;
    /**
     * 最新的文件
     */
    private ApiGFileDo newApiGFileDo;
    /**
     * 创建时间
     */
    private long updateTime;

    /**
     * 是否是电子文件平台的key
     */
    private boolean filePaltKey;

    public boolean getFilePaltKey() {
        return filePaltKey;
    }

    public void setFilePaltKey(boolean filePaltKey) {
        this.filePaltKey = filePaltKey;
    }

    public ApiGFileDo getNewApiGFileDo() {
        return newApiGFileDo;
    }

    public void setNewApiGFileDo(ApiGFileDo newApiGFileDo) {
        this.newApiGFileDo = newApiGFileDo;
    }

    public CreateStamp getCreateStamp() {
        return createStamp;
    }

    public void setCreateStamp(CreateStamp createStamp) {
        this.createStamp = createStamp;
    }

    public Result getLoadSignDataResult() {
        Result iResult=loadSignDataResult;
        if(iResult != null && iResult.getResultCode() == ResultStatusEnum.CG.getCode()){
            Object data=iResult.getData();
            if(data instanceof LoadSignDataResponse){
                LoadSignDataResponse loadSignDataResponse=(LoadSignDataResponse)data;
                loadSignDataResponse.setStampList(stampList);
                iResult.setData(loadSignDataResponse);
            }
        }
        return iResult;
    }

    public void setLoadSignDataResult(Result loadSignDataResult) {
        this.loadSignDataResult = loadSignDataResult;
    }

    public String getUploadFilePath() {
        return uploadFilePath;
    }

    public void setUploadFilePath(String uploadFilePath) {
        this.uploadFilePath = uploadFilePath;
    }

    public SignGetReadyData getSignGetReadyData() {
        return signGetReadyData;
    }

    public void setSignGetReadyData(SignGetReadyData signGetReadyData) {
        this.signGetReadyData = signGetReadyData;
    }

    public SignContext(){
        updateTime=System.currentTimeMillis();
    }

    public List<Stamp> getStampList() {
        return stampList;
    }

    public void setStampList(List<Stamp> stampList) {
        this.stampList = stampList;
    }

    public String getSignSessionId() {
        return signSessionId;
    }

    public void setSignSessionId(String signSessionId) {
        this.signSessionId = signSessionId;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime() {
        this.updateTime = System.currentTimeMillis();
    }

    /**
     * 判断是否有权限
     * @param permission
     * @return
     */
    public boolean isPermission(String permission){
        if(signGetReadyData != null){
            List<String> list=signGetReadyData.authority();
            if(CollectionUtils.isNotEmpty(list)){
                for(String str:list){
                    if(StringUtils.equals(str,permission)){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
