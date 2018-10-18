package com.cb.platform.yq.api.utils;

import abc.util.StringUtils;
import com.cb.platform.yq.base.Result;
import com.cb.platform.yq.base.customsign.enums.ResultStatusEnum;
import com.cb.platform.yq.base.exception.ApiErrorEnum;
import com.cb.platform.yq.base.exception.ApiTipEnum;
import com.cb.platform.yq.base.filepath.constant.SystemProperties;
import com.ceba.base.web.response.IResult;
import org.slf4j.Logger;

/**
 * 返回结果处理
 * @author whh
 */
public class ResultUtils {

    /**
     * 成功
     * @param data
     * @return
     */
    public static Result success(Object data){
        Result result=new Result();
        result.setResult(true);
        result.setData(data);
        return result;
    }

    /**
     * 成功的日志
     * @param logger
     * @param data
     * @param message
     * @return
     */
    public static Result successLog(Logger logger,Object data,String message){
        SystemProperties.log(logger,message);
        return success(data);
    }

    /**
     * 带错误信息的返回
     * @param message
     * @return
     */
    public static Result errorMessage(String message){
        Result result=new Result();
        result.setResult(false);
        result.setMessage(message);
        return result;
    }


    /**
     * 带错误信息的返回
     * @param message
     * @return
     */
    public static Result errorMessage(String message,Object o){
        Result result=new Result();
        result.setResult(false);
        result.setMessage(message);
        result.setData(o);
        return result;
    }



    /**
     * 带错误信息的返回
     * @param message
     * @return
     */
    public static Result errorMessage(Logger logger,String message){
        SystemProperties.log(logger,message);
        Result result=new Result();
        result.setResult(false);
        result.setMessage(message);
        return result;
    }


    /**
     * 异常返回
     * @param logger
     * @param apiErrorEnum
     * @param e
     * @param loggerMessage
     * @return
     */
    public static Result exceptionMessageLog(Logger logger, ApiErrorEnum apiErrorEnum, Exception e, String loggerMessage){
        if(SystemProperties.loggerFalg){
            if(StringUtils.isNotEmpty(loggerMessage)){
                loggerMessage=apiErrorEnum.getMessage();
            }
            SystemProperties.log(logger,loggerMessage+":"+e.getMessage());
        }
        return ResultUtils.errorMessage(apiErrorEnum.getMessage());
    }


    /**
     * 异常返回
     * @param logger
     * @param apiErrorEnum
     * @param e
     * @return
     */
    public static Result exceptionMessageLog(Logger logger,  ApiErrorEnum apiErrorEnum,Exception e){
        return exceptionMessageLog(logger,apiErrorEnum,e,apiErrorEnum.getMessage());
    }


    /**
     * 错误返回
     * @param logger
     * @param apiTipEnum
     * @return
     */
    public static Result failMessageLog(Logger logger, ApiTipEnum apiTipEnum){
        return failMessageLog(logger,apiTipEnum,apiTipEnum.getMessage());
    }

    /**
     * 错误返回
     * @param logger
     * @param apiTipEnum 失败信息
     * @param loggerMessage 失败的日志
     * @return
     */
    public static Result failMessageLog(Logger logger, ApiTipEnum apiTipEnum,String loggerMessage){
        if(SystemProperties.loggerFalg){
            if(StringUtils.isNotEmpty(loggerMessage)){
                loggerMessage=apiTipEnum.getMessage();
            }
            SystemProperties.log(logger,loggerMessage);
        }
        return ResultUtils.errorMessage(apiTipEnum.getMessage());
    }


    /**
     * 转返回
     * @param result
     * @return
     */
    public static IResult iResult(Result result){
        IResult iResult=new IResult();
        iResult.setData(result.getData());
        if(result.isResult()){
            iResult.setResultCode(ResultStatusEnum.CG.getCode());
        }else{
            iResult.setResultCode(ResultStatusEnum.SB.getCode());
        }
        iResult.setMessage(result.getMessage());
        iResult.setResult(result.isResult());
        return iResult;
    }

    /**
     * 返回
     * @param result
     * @return
     */
    public static Result result(IResult iresult){
        Result result=new Result();
        result.setData(iresult.getData());
        if(iresult.getResultCode() == ResultStatusEnum.CG.getCode()){
            result.setResult(true);
        }else{
            result.setResult(false);
        }
        result.setMessage(iresult.getMessage());
        result.setResult(iresult.getResult());
        return result;
    }

}
