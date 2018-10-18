package com.cb.platform.yq.api.sign.bean.impl;

import com.cb.platform.yq.base.Result;

import java.util.List;

/**
 * 返回签章对象
 */
public class ResultStampVO extends Result {
    private List<StampVO> stampList;

    public List<StampVO> getStampList() {
        return stampList;
    }

    public void setStampList(List<StampVO> stampList) {
        this.stampList = stampList;
    }
}
