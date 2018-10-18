package com.cb.platform.yq.base.filepath.service;

import com.cb.platform.yq.base.filepath.constant.SystemPathConstant;
import com.cb.platform.yq.base.filepath.constant.SystemProperties;
import com.cb.platform.yq.base.filepath.constant.YqPathConstant;
import com.cb.platform.yq.base.filepath.enums.FileVisitEnum;
import com.cb.platform.yq.base.filepath.enums.SystemFilePathEnum;

import java.io.File;

/**
 * 云签文件路径接口
 */
public class YqSystemFilePathImpl extends AbstractSystemFilePath {
    private static YqSystemFilePathImpl tempFilePath=new YqSystemFilePathImpl(SystemFilePathEnum.SYSTEM_TEMP);//缓存文件路径
    private static YqSystemFilePathImpl upfileFilePath=new YqSystemFilePathImpl();//非缓存文件 目录
    public YqSystemFilePathImpl(SystemFilePathEnum SYSTEM_UPFILE) {
        super(SYSTEM_UPFILE);
    }
    public YqSystemFilePathImpl() {
        super();
    }

    /**
     * 切换文件访问方式
     * @param originalFileVisitEnum
     * @param goalFileVisitEnum
     * @param path
     * @return
     */
    @Override
    public String changeFileVisit(FileVisitEnum originalFileVisitEnum, FileVisitEnum goalFileVisitEnum, String path) {
        if(originalFileVisitEnum == goalFileVisitEnum){
            return path;
        }
        if((FileVisitEnum.FTP_BROWSER == originalFileVisitEnum || FileVisitEnum.FTP_VIRTUAL == originalFileVisitEnum) ||
                (FileVisitEnum.FTP_BROWSER == goalFileVisitEnum ||  FileVisitEnum.FTP_VIRTUAL == goalFileVisitEnum)){
            String originalPath=pathPrefix(originalFileVisitEnum);//原文件访问目录
            String virtualPath=path.substring(originalPath.length(),path.length());//去掉前缀
            virtualPath=virtualPath.substring(this.getSYSTEM_UPFILE().getPath().length(),virtualPath.length());//去掉 系统默认前缀
            if(FileVisitEnum.FTP_BROWSER == originalFileVisitEnum || FileVisitEnum.FTP_VIRTUAL == originalFileVisitEnum) {
                //源目录是 FTP 去掉 yq/fs/
                virtualPath = virtualPath.substring(YqPathConstant.FTP_SERVER_ROOT.length(), virtualPath.length());
            }else{
                //源目录不是 FTP 去掉 yq/temp/
                virtualPath = virtualPath.substring(YqPathConstant.LOCAL_SERVER_ROOT.length(), virtualPath.length());
            }
            String pathStr = pathPrefix(goalFileVisitEnum) + SystemPathConstant.SYSTEM_UPFILE;//访问前缀 + 系统前缀
            //根据 访问 方式而定
            if(FileVisitEnum.FTP_BROWSER == goalFileVisitEnum || FileVisitEnum.FTP_VIRTUAL == goalFileVisitEnum){
                pathStr= pathStr+YqPathConstant.FTP_SERVER_ROOT;
            }else{
                pathStr= pathStr+YqPathConstant.LOCAL_SERVER_ROOT;
            }
            return pathStr+virtualPath;
        }
        return super.changeFileVisit(originalFileVisitEnum, goalFileVisitEnum, path);
    }


    /**
     * 路径前缀特殊处理
     *
     * @param fileVisitEnum
     * @return
     */
    @Override
    public String pathPrefix(FileVisitEnum fileVisitEnum) {
        if(fileVisitEnum == FileVisitEnum.BROWSER){
            return SystemProperties.contextPath+SystemPathConstant.FILE_FILTER_PATH;
        }
        return fileVisitEnum.getPath();
    }

    /**
     * 取得缓存操作目录  缓存文件 不能存到数据库中
     * @return
     */
    public static YqSystemFilePathImpl getTempFilePath(){
        return tempFilePath;
    }

    /**
     * 正常操作目录
     * @return
     */
    public static YqSystemFilePathImpl getUpfileFilePath(){
        return upfileFilePath;
    }

    /**
     * 取得FTP虚拟路径
     * @param virtualPath
     * @return
     */
    public static String getFtpVirtualPath(String virtualPath){
        String absolutePath=upfileFilePath.changeFileVisit(FileVisitEnum.FTP_VIRTUAL,FileVisitEnum.ABSOLUTE,virtualPath);
        File file =new File(absolutePath);
        int length=file.getName().length();
        return virtualPath.substring(0,(virtualPath.length()-length));
    }

    /**
     * 取文件名
     * @param virtualPath
     * @return
     */
    public static String fileName(String virtualPath){
        String absolutePath=upfileFilePath.changeFileVisit(FileVisitEnum.VIRTUAL,FileVisitEnum.ABSOLUTE,virtualPath);
        File file =new File(absolutePath);
        return file.getName();
    }


    /**
     * 取文件名
     * @param virtualPath
     * @return
     */
    public static String fileName(String virtualPath,String fileName){
        String virtualExt=virtualPath.substring(virtualPath.indexOf("."),virtualPath.length());
        fileName=fileName.substring(0,fileName.indexOf("."));
        return fileName+virtualExt;
    }


    public static String toLocalFilePath(String absolutePath){
        File file =new File(absolutePath);
        int length=file.getName().length();
        return absolutePath.substring(0,(absolutePath.length()-length));
    }

    public static void main(String[] stras){
        String path=YqSystemFilePathImpl.getUpfileFilePath().changeFileVisit(FileVisitEnum.VIRTUAL,FileVisitEnum.FTP_VIRTUAL,"upfile/yq/fs/2018/5/21/f2fdf204602a4761aa611f3da5475360/123.pdf");
        System.out.print(path);
    }
}
