package com.cb.platform.yq.base;

import com.cb.platform.yq.base.filepath.constant.SystemProperties;
import com.ceba.base.web.response.IResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * 返回结果定义
 */
@ApiModel(value="com.cb.platform.yq.base.Result",description = "返回结果")
public class Result implements Serializable{
    public static Logger logger = LoggerFactory.getLogger(Result.class);
    @ApiModelProperty(value = "返回结果码")
    private int resultCode;
    @ApiModelProperty(value = "返回成功，失败")
    private boolean result;
    @ApiModelProperty(value = "返回数据对象")
    private Object data;
    @ApiModelProperty(value = "返回消息")
    private String message;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public Result(){}

    /**
     * 返回
     * @param iResult
     */
    public Result(IResult iResult,boolean bool){
        if(iResult != null){
            this.setData(iResult.getData());
            this.setResultCode(iResult.getResultCode());
            this.setMessage(iResult.getMessage());
            this.setResult(iResult.getResult());
            this.setResult(bool);
        }else{
            if(SystemProperties.loggerFalg){
                logger.debug("iResult为空");
            }
        }
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
