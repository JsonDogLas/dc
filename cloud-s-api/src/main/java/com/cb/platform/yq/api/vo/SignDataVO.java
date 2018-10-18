package com.cb.platform.yq.api.vo;

import com.cb.platform.yq.api.sign.bean.impl.SignCompleteData;
import com.cb.platform.yq.base.Result;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * 返回结果定义
 */
@ApiModel(value="获取签名数据返回结果",description = "返回结果")
public class SignDataVO extends Result {
    @Override
    public SignCompleteData getData() {
        if(super.getData() instanceof SignCompleteData){
            return (SignCompleteData)super.getData();
        }else{
            return null;
        }
    }
}
