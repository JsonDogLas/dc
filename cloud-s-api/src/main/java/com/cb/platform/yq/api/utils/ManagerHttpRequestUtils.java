package com.cb.platform.yq.api.utils;

import com.cb.platform.yq.base.exception.ApiTipEnum;
import com.ceba.base.helper.LoggerHelper;
import com.ceba.base.web.response.IResult;
import org.apache.commons.lang.ObjectUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 模拟http请求
 */
public class ManagerHttpRequestUtils {
    public static Logger logger = LoggerFactory.getLogger(ManagerHttpRequestUtils.class);

    private static PoolingHttpClientConnectionManager cm;
    private static RequestConfig requestConfig;

    static {
        //初始化配置
        ConnectionSocketFactory plainsf = (ConnectionSocketFactory) PlainConnectionSocketFactory.getSocketFactory();
        Registry registry = RegistryBuilder.create().register("http", plainsf).build();
        ManagerHttpRequestUtils.requestConfig = RequestConfig.custom().setConnectTimeout(60000).setConnectionRequestTimeout(20000)
                .setSocketTimeout(120000).build();
        (ManagerHttpRequestUtils.cm = new PoolingHttpClientConnectionManager((Registry) registry)).setMaxTotal(50);
        ManagerHttpRequestUtils.cm.setDefaultMaxPerRoute(50);
    }

    private static CloseableHttpClient createHttpClient() {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager((HttpClientConnectionManager) ManagerHttpRequestUtils.cm)
                .setDefaultRequestConfig(ManagerHttpRequestUtils.requestConfig).build();
        return httpClient;
    }

    /**
     * post请求
     *
     * @return
     */
    public static String post(String url, Map<Object,Object> map) {
        List<NameValuePair> nvps = new LinkedList<NameValuePair>();
        ResponseHandler<String> responseHandler = (ResponseHandler<String>) new BasicResponseHandler();
        for (Object key : map.keySet()) {
            if(map.get(key) != null){
                nvps.add((NameValuePair) new BasicNameValuePair(ObjectUtils.toString(key), map.get(key).toString()));
            }
        }
        return post(url,nvps,responseHandler);
    }

    public static String get(String url, Map<Object, Object> data) throws Exception {
        ResponseHandler<String> responseHandler = (ResponseHandler<String>) new BasicResponseHandler();
        return get(url,data,responseHandler);
    }


    /**
     * post请求
     *
     * @return
     */
    public static String post(String url, List<NameValuePair> nvps,ResponseHandler<String> responseHandler) {
        try {
            CloseableHttpClient httpClient = createHttpClient();
            HttpPost httpost = new HttpPost(url);
            httpost.setEntity((HttpEntity) new UrlEncodedFormEntity((List) nvps, "UTF-8"));
            // 打印成功信息
            return (String) httpClient.execute((HttpUriRequest) httpost, (ResponseHandler) responseHandler);
        } catch (Exception e) {
            LoggerHelper.error(e, "");
            IResult iResult = new IResult(false);
            iResult.setMessage(ApiTipEnum.ME_THIRD_GET_FAIL.getMessage());
            logger.debug("调用第三方接口异常:"+e.getMessage());
            return iResult.toString();
        }
    }


    /**
     * postJSON请求
     *
     * @return
     */
    public static String postJSON(String url, String json) {
        try {
            CloseableHttpClient httpClient = createHttpClient();
            HttpPost httpost = new HttpPost(url);
            HttpEntity httpEntity=new StringEntity(json,ContentType.APPLICATION_JSON);
            httpost.setEntity(httpEntity);
            ResponseHandler<String> responseHandler = (ResponseHandler<String>) new BasicResponseHandler();
            // 打印成功信息
            return (String) httpClient.execute((HttpUriRequest) httpost, responseHandler);
        } catch (Exception e) {
            LoggerHelper.error(e, "");
            IResult iResult = new IResult(false);
            iResult.setMessage(ApiTipEnum.ME_THIRD_GET_FAIL.getMessage());
            logger.debug("调用第三方接口异常:"+e.getMessage());
            return iResult.toString();
        }
    }

    public static String get(String url, Map<Object, Object> data,ResponseHandler<String> responseHandler) throws Exception {
        try {
            // 创建一个get请求
            StringBuffer urlBuffer = new StringBuffer();
            urlBuffer.append(url);
            urlBuffer.append("?");
            for (Object key : data.keySet()) {
                urlBuffer.append(key.toString());
                urlBuffer.append("=");
                if(data.get(key) != null){
                    urlBuffer.append(data.get(key).toString());
                    urlBuffer.append("&");
                }
            }
            CloseableHttpClient httpClient = createHttpClient();
            HttpGet httpGet = new HttpGet(urlBuffer.toString());
            // 打印成功信息
            return (String) httpClient.execute((HttpUriRequest) httpGet, (ResponseHandler) responseHandler);
        }catch (Exception e) {
            LoggerHelper.error(e, "");
            IResult iResult = new IResult(false);
            iResult.setMessage(ApiTipEnum.ME_THIRD_GET_FAIL.getMessage());
            logger.debug("调用第三方接口异常:"+e.getMessage());
            return iResult.toString();
        }
    }


}
