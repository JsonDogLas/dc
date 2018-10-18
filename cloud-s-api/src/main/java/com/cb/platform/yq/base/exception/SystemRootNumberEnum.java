package com.cb.platform.yq.base.exception;

import com.cb.platform.yq.base.log.ModulesNumberInterface;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 系统编号枚举
 */
public enum SystemRootNumberEnum implements ModulesNumberInterface {
    YQ_ROOT("6","云签API");//此节点无实际作用
    private String number;
    private String name;
    SystemRootNumberEnum(String number, String name) {
        this.number = number;
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 取得前端点击编号
     *
     * @return
     */
    @Override
    public String getWebNumber() {
        return number+WEB_LOG_NUMBER_ROOT+INTERVAL;
    }

    /**
     * 取得service编号
     *
     * @return
     */
    @Override
    public String getServiceNumber() {
        return number+SERVICE_LOG_NUMBER_ROOT+INTERVAL;
    }

    /**
     * 取得异常编号
     *
     * @return
     */
    @Override
    public String getExceptionNumber() {
        return number+EXCEPTION_NUMBER_ROOT+INTERVAL;
    }

    /**
     * 取得提示的编号
     *
     * @return
     */
    @Override
    public String getTipNumber() {
        return number+TIP_NUMBER_ROOT+INTERVAL;
    }

    /**
     * 取得根节点
     *
     * @return
     */
    @Override
    public ModulesNumberInterface getRoot() {
        return null;
    }

    /**
     * 日志类型
     * @return
     */
    public static Map<String,String> logType(){
        Map<String,String> map=new HashMap<>();
        map.put("serviceNumber",SERVICE_LOG_NUMBER_ROOT);
        map.put("exceptionNumber",EXCEPTION_NUMBER_ROOT);
        map.put("tipNumber",TIP_NUMBER_ROOT);
        map.put("webNumber",WEB_LOG_NUMBER_ROOT);
        return map;
    }
    public static String getSystemType(SystemRootNumberEnum systemRootNumberEnum,String LOG_TYPE){
        if(StringUtils.equals(LOG_TYPE,ModulesNumberInterface.TIP_NUMBER_ROOT)){//提示编号
            return systemRootNumberEnum.getTipNumber();
        }else if(StringUtils.equals(LOG_TYPE,ModulesNumberInterface.EXCEPTION_NUMBER_ROOT)){//异常编号
            return systemRootNumberEnum.getExceptionNumber();
        }else if(StringUtils.equals(LOG_TYPE,ModulesNumberInterface.WEB_LOG_NUMBER_ROOT)){//前端点击
            return systemRootNumberEnum.getWebNumber();
        }else if(StringUtils.equals(LOG_TYPE,ModulesNumberInterface.SERVICE_LOG_NUMBER_ROOT)){//服务日端日
            return systemRootNumberEnum.getServiceNumber();
        }
        return null;
    }
    /**
     * 系统分类
     * @return
     */
    public static Map<String,Map<String,String>> getDataMap(){
        SystemRootNumberEnum[] list=SystemRootNumberEnum.values();
        Map<String,Map<String,String>> userMap=new LinkedHashMap<>();
        Map<String,String> webNumberMap=new LinkedHashMap<>();
        Map<String,String> serviceNumberMap=new LinkedHashMap<>();
        Map<String,String> exceptionNumberMap=new LinkedHashMap<>();
        Map<String,String> tipNumberMap=new LinkedHashMap<>();
        for(SystemRootNumberEnum systemRootNumberEnum:list){
            String webNumber=systemRootNumberEnum.getWebNumber();
            String serviceNumber=systemRootNumberEnum.getServiceNumber();
            String exceptionNumber=systemRootNumberEnum.getExceptionNumber();
            String tipNumber=systemRootNumberEnum.getTipNumber();

            tipNumberMap.put(tipNumber,systemRootNumberEnum.getName());
            exceptionNumberMap.put(exceptionNumber,systemRootNumberEnum.getName());
            webNumberMap.put(webNumber,systemRootNumberEnum.getName());
            serviceNumberMap.put(serviceNumber,systemRootNumberEnum.getName());
        }

        userMap.put("tipNumber",tipNumberMap);
        userMap.put("exceptionNumber",exceptionNumberMap);
        userMap.put("webNumber",webNumberMap);
        userMap.put("serviceNumber",serviceNumberMap);
        return userMap;
    }
    //类型  系统 模块
}
