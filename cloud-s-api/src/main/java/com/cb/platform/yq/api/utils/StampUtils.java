package com.cb.platform.yq.api.utils;

import com.ceba.cebautils.Base64FileUtils;
import org.apache.commons.lang.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class StampUtils {

    /**
     * 透明章
     * @return
     */
    public static void transparentStamp(String path)throws Exception{
        BufferedImage image= new BufferedImage(1,1, BufferedImage.TYPE_INT_RGB);
        // 获取Graphics2D
        Graphics2D g2d = image.createGraphics();
        image = g2d.getDeviceConfiguration().createCompatibleImage(1, 1, Transparency.TRANSLUCENT);
        g2d.dispose();
        g2d = image.createGraphics();
        g2d.setColor(new Color(255,0,0));
        g2d.setStroke(new BasicStroke(1));
        g2d.dispose();
        g2d.draw(null);
        File file=new File(path);
        ImageIO.write(image,"png", file);
    }

    /**
     * base64转图片
     * @param base64
     * @param imageFilePath
     */
    public static void base64ToImage(String base64,String imageFilePath){
        Base64FileUtils.string2Image(base64, imageFilePath);
    }
    /**
     * 取得base64净化数据
     * @return
     */
    private static  String getBase64PurifyData(String base64){
        String[] base64s= StringUtils.split(base64,",");
        if(base64s.length == 2){
            return base64s[1];
        }
        return null;
    }

    /**
     * 根据文件名或者文件路径取得文件后缀
     * @param path
     * @return
     */
    public static String fileExt(String path){
        if(StringUtils.isNotEmpty(path)){
            int index=path.lastIndexOf(".");
            if(0<index && index < path.length()){
                return path.substring(index+1,path.length());
            }
        }
        return null;
    }

    /**
     * 取得文件名 没有后缀
     * @param path
     * @return
     */
    public static String fileName(String path){
        if(StringUtils.isNotEmpty(path)){
            int index=path.lastIndexOf(".");
            if(0<index && index < path.length()){
                return path.substring(0,index);
            }
        }
        return null;
    }

}
