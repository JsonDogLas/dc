package com.cb.platform.yq.api.sign;


import com.cb.platform.yq.api.sign.bean.StampInfo;
import com.ceba.base.exception.BusinessErrorInfoException;
import com.ceba.base.utils.IDSDateUtils;
import com.ceba.base.web.response.IResult;

import java.util.concurrent.Callable;

/**
 * 印章模板
 * @author whh
 */
public interface StampTemplate extends Callable<IResult> {
    String DATE_FILE_TYPE="png";
    String DATE_TYPE= IDSDateUtils.YYYYMMDD_SLASH;

    /**
     * 获取签章
     * @return
     */
    StampInfo tackStamp() throws BusinessErrorInfoException;

    /**
     * 印章来源信息
     * @return
     */
    String stampSourceInfo();
}
