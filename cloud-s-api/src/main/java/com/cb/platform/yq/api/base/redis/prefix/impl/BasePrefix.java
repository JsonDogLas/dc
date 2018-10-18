package com.cb.platform.yq.api.base.redis.prefix.impl;


import com.cb.platform.yq.api.base.redis.prefix.KeyPrefix;

public abstract class BasePrefix implements KeyPrefix {

    /**
     * 0 永不过期
     * @param prefix
     */
    public BasePrefix(String prefix) {
      this(0,prefix) ;
    }

    public BasePrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    /*
        0--代表永不过期
         */
    private int expireSeconds;

    private String prefix;

    /***
     * @return
     */
    @Override
    public int expireSeconds() {
        return expireSeconds;
    }

    @Override
    public String getPrefix() {
        String className = getClass().getName();
        return className+":"+prefix+":";
    }
}
