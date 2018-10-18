package com.cb.producer.util;

/**
 * @author jdl
 * @date 2018/9/18 15:14
 */
public class StringUtil {
    /**
     * 获取文件后缀名
     * @param fileName 文件名
     * @return 后缀名
     */
    public static String getSuffix(String fileName) {
        String suffix = "";
        int indexSuffix = fileName.lastIndexOf(".");
        if(indexSuffix != -1) {
            suffix = fileName.substring(indexSuffix + 1);
        }
        return suffix;
    }
}
