package com.cb.platform.yq.api.sign.impl;


import com.cb.platform.yq.api.sign.MemorySignContextManager;
import com.cb.platform.yq.api.sign.SignContextManager;
import com.cb.platform.yq.api.sign.bean.impl.SignContext;
import com.cb.platform.yq.api.utils.ResultUtils;
import com.cb.platform.yq.base.customsign.entity.LoadSignDataResponse;
import com.cb.platform.yq.base.customsign.enums.ResultStatusEnum;
import com.cb.platform.yq.base.exception.ApiErrorEnum;
import com.cb.platform.yq.base.filepath.constant.SystemProperties;
import com.ceba.base.web.response.IResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.Future;

/**
 * 定时任务
 */
@Component
@EnableScheduling
public class QuartzService {
    public static Logger logger = LoggerFactory.getLogger(QuartzService.class);
    @Autowired
    private SignContextManager signContextManager;
    @Autowired
    private SystemProperties systemProperties;
    @Autowired
    private MemoryFutureManager memoryFutureManager;
    /**
     * 每分钟启动
     * 缓解内存的资源
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void timerToNow(){
        MemorySignContextManager memorySignContextManager;
        if(signContextManager instanceof MemorySignContextManager){
            memorySignContextManager=(MemorySignContextManager)signContextManager;
        }else{
            SystemProperties.log(logger,"缓解内存的资源定时器未执行");
            return;
        }
        //检测每个数据是否在线程中执行完成
        Collection<Future<IResult>> futureCollection=memoryFutureManager.all();
        if(CollectionUtils.isNotEmpty(futureCollection)){
            for(Future<IResult> future:futureCollection){
                if(future != null && future.isDone()){
                    //线程执行完毕 但是 有没有取出数据
                    try {
                        IResult iResult=future.get();
                        String signSessionId=null;
                        if((!iResult.getResult() || iResult.getResultCode() == ResultStatusEnum.SB.getCode())){
                            signSessionId= ObjectUtils.toString(iResult.getData());
                        }else{
                            Object o=iResult.getData();
                            if(o instanceof LoadSignDataResponse){
                                LoadSignDataResponse loadSignDataResponse=(LoadSignDataResponse)o;
                                signSessionId=loadSignDataResponse.getHandSignKey();
                            }
                        }
                        SignContext signContext=signContextManager.get(signSessionId);
                        signContext.setLoadSignDataResult(ResultUtils.result(iResult));
                        signContextManager.put(signContext.getSignSessionId(),signContext);
                        memoryFutureManager.remove(signContext.getSignSessionId());
                        SystemProperties.log(logger,"签名会话【"+signContext.getSignSessionId()+"】定时器取出数据");
                    } catch (Exception e) {
                        ResultUtils.exceptionMessageLog(logger, ApiErrorEnum.CREATE_QUARTZ_ERROR,e,"定时器报错");
                    }
                }
            }
        }

        //检测每个数据是否超时
        Collection<SignContext> signContextCollection=memorySignContextManager.memoryAllData();
        if(signContextCollection != null && CollectionUtils.isNotEmpty(signContextCollection)){
            for(SignContext signContext:signContextCollection){
                boolean bool=isOutTime(signContext.getUpdateTime());
                if(bool){
                    outTime(memorySignContextManager,signContext);
                }
            }
        }
    }

    /**
     * 超时操作
     * @param memorySignContextManager
     * @param signContext
     */
    public void outTime(MemorySignContextManager memorySignContextManager,SignContext signContext){
        //超过时间 删除内存中的数据
        memorySignContextManager.memoryRemove(signContext.getSignSessionId());
        SystemProperties.log(logger,"签名会话【"+signContext.getSignSessionId()+"】超过活跃期时间，已经从内存中删除");
    }

    /**
     * 判断是否超时
     * @param time
     * @return
     */
    public boolean isOutTime(long time){
        long now=System.currentTimeMillis();
        if((now - time) > systemProperties.getSignContextMemoryMillisecond()){
            return true;
        }else{
            return false;
        }
    }
}
