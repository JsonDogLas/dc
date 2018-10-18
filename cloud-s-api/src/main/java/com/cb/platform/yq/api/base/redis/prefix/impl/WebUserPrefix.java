package com.cb.platform.yq.api.base.redis.prefix.impl;


import com.cb.platform.yq.base.filepath.constant.SystemProperties;

public class WebUserPrefix extends BasePrefix {

    public WebUserPrefix(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public WebUserPrefix(String prefix) {
        super(prefix);
    }

    public static final int TOKEN_EXPIRE = 3600*2;

    public final static String USER_FLAG="DOME.USER.MANAGER";
    public final static String STAMP_FLAG="YQ.API.STAMP";
    public final static String API_CLIENT_INFO="api.client.info";
    public final static String API_KEY_INFO="api.key.info";
    public final static String API_SIGN_CONTEXT_INFO="api.sign.context.info";

    //codeToken key存储规则
    public static WebUserPrefix userPrefix = new WebUserPrefix(TOKEN_EXPIRE, USER_FLAG) ;
    //stamp存储规则
    public static WebUserPrefix stampPrefix = new WebUserPrefix(TOKEN_EXPIRE, STAMP_FLAG) ;
    //api.client.info
    public static WebUserPrefix apiClientInfo = new WebUserPrefix(TOKEN_EXPIRE, API_CLIENT_INFO) ;
    //api.key.info
    public static WebUserPrefix apiKeyInfo = new WebUserPrefix(TOKEN_EXPIRE, API_KEY_INFO) ;

    public static WebUserPrefix apiSignContextInfo=new WebUserPrefix(SystemProperties._signContextRedisTime,API_SIGN_CONTEXT_INFO);



}
