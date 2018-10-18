package com.cb.platform.yq.api.sign.impl;

import com.cb.platform.yq.api.sign.StampManager;
import com.cb.platform.yq.api.sign.StampTemplate;
import com.cb.platform.yq.api.sign.bean.StampInfo;
import com.cb.platform.yq.api.sign.bean.impl.StampInfoCollection;
import com.cb.platform.yq.api.utils.ResultUtils;
import com.cb.platform.yq.base.customsign.entity.Stamp;
import com.cb.platform.yq.base.exception.ApiErrorEnum;
import com.cb.platform.yq.base.exception.ApiTipEnum;
import com.cb.platform.yq.base.filepath.constant.SystemProperties;
import com.ceba.base.exception.BusinessErrorInfoException;
import com.ceba.base.web.response.IResult;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 基础的签章管理类
 */
public class BaseStampManager implements StampManager {
    public static Logger logger = LoggerFactory.getLogger(BaseStampManager.class);

    private Map<StampTemplate,Future<IResult>> futureMap;
    private StampInfoCollection.StampInfoBuilder stampInfoBuilder;
    /**
     * 线程池
     */
    private CompletionService<IResult> completionService;
    public BaseStampManager(CompletionService<IResult> completionService){
        this.stampInfoBuilder=new StampInfoCollection.StampInfoBuilder();
        this.futureMap=new HashMap<>();
        this.completionService=completionService;
    }

    /**
     * 全局取得印章
     * @return
     */
    @Override
    public StampInfo tackStamp() throws BusinessErrorInfoException {
        if(MapUtils.isEmpty(futureMap)){
            return stampInfoBuilder.build();
        }
        for(StampTemplate stampTemplate:futureMap.keySet()){
            try {
                Future<IResult> future=futureMap.get(stampTemplate);
                IResult iResult=future.get();
                if(iResult.getResult()){
                    Object object = iResult.getData();
                    if(object != null && object instanceof StampInfo){
                        stampInfoBuilder.addStampInfo((StampInfo)object);
                    }
                }else{
                    ResultUtils.failMessageLog(logger, ApiTipEnum.CREATE_RUN_STAMP_ERROR,stampTemplate.stampSourceInfo());
                }
            } catch (Exception e) {
                //线程出错
                ResultUtils.exceptionMessageLog(logger, ApiErrorEnum.CREATE_RUN_STAMP_ERROR,e,stampTemplate.stampSourceInfo());
            }
        }
        return stampInfoBuilder.build();
    }

    /**
     * 加签章模板
     *
     * @param stampTemplate
     */
    @Override
    public void addStampTemplate(StampTemplate stampTemplate) throws BusinessErrorInfoException {
        Future<IResult> future=this.completionService.submit(stampTemplate);
        this.futureMap.put(stampTemplate,future);
    }
}
