package com.cb.platform.yq.base.interfaces.bean;


/**
 * 输入参数
 */
public class RequestParam extends ResponseParam {
    private String accessToken;//接口口令


    //分页
    private String page;//起始页
    private String pageSize;//每页查询的行数

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }


    public String getPageSize () {
        return pageSize;
    }

    public void setPageSize ( String pageSize ) {
        this.pageSize = pageSize;
    }

    public RequestParam(){
        //TODO 通过公共的方法 取 TOKEN
        this.accessToken=null;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
