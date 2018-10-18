package com.cb.platform.yq.api.sign.bean.impl;

import com.cb.platform.yq.api.sign.bean.StampInfo;
import com.cb.platform.yq.base.customsign.entity.Stamp;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 签章信息构造
 */
public class StampInfoCollection implements StampInfo {
    private List<Stamp> stampList;
    public static class StampInfoBuilder{
        private List<Stamp> stampList;

        /**
         * 添加印章信息
         * @param stampInfo
         * @return
         */
        public StampInfoBuilder addStampInfo(StampInfo stampInfo){
            if(CollectionUtils.isEmpty(stampList)){
                this.stampList=new ArrayList<>();
            }
            if(stampInfo != null){
                stampList.addAll(stampInfo.tackStamp());
            }
            return this;
        }

        /**
         * 添加签章集合
         * @param stampList
         * @return
         */
        public StampInfoBuilder addStampList(List<Stamp> stampList){
            if(CollectionUtils.isEmpty(this.stampList)){
                this.stampList=new ArrayList<>();
            }
            this.stampList.addAll(stampList);
            return this;
        }

        /**
         * 判断是否为空
         * @return
         */
        public boolean isEmpty(){
            return CollectionUtils.isEmpty(stampList);
        }

        public StampInfoCollection build(){
            return new StampInfoCollection(this.stampList);
        }
    }

    private StampInfoCollection(List<Stamp> stampList){
        this.stampList=stampList;
    }

    @Override
    public List<Stamp> tackStamp() {
        if(!CollectionUtils.isEmpty(this.stampList)){
            Map<String,Stamp> stampMap=new HashMap<>();
            for(Stamp stamp:this.stampList){
                stampMap.put(stamp.getId(),stamp);
            }
            List<Stamp> newStampList=new ArrayList<>();
            for(Stamp stamp:stampMap.values()){
                newStampList.add(stamp);
            }
            return newStampList;
        }
        return null;
    }

}
