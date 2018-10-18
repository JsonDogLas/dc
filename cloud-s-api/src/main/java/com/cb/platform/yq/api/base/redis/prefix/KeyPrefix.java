package com.cb.platform.yq.api.base.redis.prefix;

public interface KeyPrefix {
    /**
     * 过期时间
     * @return
     */
    public int expireSeconds();

    /**
     * 缓存key的前缀
     * @return
     */
    public String getPrefix();
}
