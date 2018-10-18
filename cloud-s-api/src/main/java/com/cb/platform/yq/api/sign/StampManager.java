package com.cb.platform.yq.api.sign;


import com.cb.platform.yq.api.sign.bean.StampInfo;
import com.ceba.base.exception.BusinessErrorInfoException;

/**
 * 印章管理
 */
public interface StampManager {
     /**
      * 取出签章
      * @return
      */
     StampInfo tackStamp()throws BusinessErrorInfoException;

     /**
      * 加签章模板
      * @param stampTemplate
      */
     void addStampTemplate(StampTemplate stampTemplate)throws BusinessErrorInfoException;

}
