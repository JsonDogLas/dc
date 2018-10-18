package com.cb.platform.yq.base.filepath.service;

import com.cb.platform.yq.base.filepath.enums.FileVisitEnum;
import com.cb.platform.yq.base.filepath.enums.SystemFilePathEnum;
import org.apache.commons.lang3.StringUtils;

import java.io.File;


/**
 * 系统文件路径接口
 */
public abstract class AbstractSystemFilePath implements SystemFilePathInterface {
    private SystemFilePathEnum SYSTEM_UPFILE;

    public AbstractSystemFilePath(SystemFilePathEnum SYSTEM_UPFILE) {
        this.SYSTEM_UPFILE = SYSTEM_UPFILE;
    }

    public AbstractSystemFilePath() {
        this(SystemFilePathEnum.SYSTEM_UPFILE);
    }
    /**
     * 取得系统访问路径
     *
     * @param fileVisitEnum
     * @param fileStoragePathInterface
     * @return
     */
    @Override
    public String getFilePath(FileVisitEnum fileVisitEnum, FilePathEnumInterface fileStoragePathInterface) {
        StringBuffer buffer=new StringBuffer();
        //加访问方式
        buffer.append(pathPrefix(fileVisitEnum));
        //虚拟目录
        buffer.append(SYSTEM_UPFILE.path());
        buffer.append(fileStoragePathInterface.path());

        //如果访问方式为 绝对访问 判断目录有没有创建
        if(fileVisitEnum == FileVisitEnum.ABSOLUTE){
            File file=new File(buffer.toString());
            if(!file.exists()){
                file.mkdirs();
            }
        }

        return buffer.toString();
    }

    /**
     * 路径前缀特殊处理
     * @param fileVisitEnum
     * @return
     */
    public abstract String pathPrefix(FileVisitEnum fileVisitEnum);

    /**
     * 切换文件访问方式
     *
     * @param originalFileVisitEnum
     * @param goalFileVisitEnum
     * @param path
     * @return
     */
    @Override
    public String changeFileVisit(FileVisitEnum originalFileVisitEnum, FileVisitEnum goalFileVisitEnum, String path) {
        // 将文件转换为 虚拟方式
        String originalPath=pathPrefix(originalFileVisitEnum);
        if(StringUtils.isEmpty(originalPath)){
            return pathPrefix(goalFileVisitEnum)+path;
        }else{
            if(pathPrefix(goalFileVisitEnum) != null ){
                return pathPrefix(goalFileVisitEnum) + path.substring(originalPath.length(),path.length());
            }else{
                return path.substring(originalPath.length(),path.length());
            }
        }
    }

    public SystemFilePathEnum getSYSTEM_UPFILE() {
        return SYSTEM_UPFILE;
    }

    public void setSYSTEM_UPFILE(SystemFilePathEnum SYSTEM_UPFILE) {
        this.SYSTEM_UPFILE = SYSTEM_UPFILE;
    }
}
