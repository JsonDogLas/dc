package com.cb.platform.yq.base.filepath.enums;

import abc.util.StringUtils;
import com.cb.platform.yq.base.filepath.constant.YqPathConstant;
import com.cb.platform.yq.base.filepath.service.*;
import com.cb.platform.yq.base.filepath.service.impl.DatePathGeneratorImpl;

/**
 * 云签文件存储规则
 */
public enum YqFilePathEnum implements FilePathEnumInterface {
    LOCAL_SERVER_ROOT("temp","本地文件服务目录",new YqDatePathGeneratorImpl(YqPathConstant.LOCAL_SERVER_ROOT,"test")),
    FTP_SERVER_ROOT("fs","FTP文件服务目录",new YqDatePathGeneratorImpl(YqPathConstant.FTP_SERVER_ROOT,"test")),
    LOCAL_FILE_IMAGE("image","本地文件图片目录",new FileImageGeneratorImpl(YqPathConstant.LOCAL_SERVER_FILE_IMAGE,"test")),
    YZ("yz","印章文件存储目录",new YqDatePathTypeGeneratorImpl(YqPathConstant.SQ_ROOT,"test",YqPathConstant.YZ));

    YqFilePathEnum(String flag, String name, PathGeneratorInterface pathGenerator) {
        this.flag = flag;

        this.name = name;
        this.pathGenerator = pathGenerator;
    }

    private String flag;
    private String name;
    private PathGeneratorInterface pathGenerator;




    public String getFlag() {
        return flag;
    }
    public void setFlag(String flag) {
        this.flag = flag;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public PathGeneratorInterface getPathGenerator() {
        return pathGenerator;
    }
    public void setPathGenerator(PathGeneratorInterface pathGenerator) {
        this.pathGenerator = pathGenerator;
    }

    /**
     * 设置日期路径ID
     * @param id
     */
    public void setDatePathGeneratorImplId(String id){
        if(this.pathGenerator instanceof DatePathGeneratorImpl){
            DatePathGeneratorImpl datePathGenerator=(DatePathGeneratorImpl)this.pathGenerator;
            datePathGenerator.setId(id);
        }else if(this.pathGenerator instanceof FileImageGeneratorImpl){
            FileImageGeneratorImpl fileImageGeneratorImpl=(FileImageGeneratorImpl)this.pathGenerator;
            fileImageGeneratorImpl.setId(id);
        }
    }

    /**
     * 取得文件目录
     * @param fileVisitEnum
     * @param id
     * @return
     */
    public String getFilePath(FileVisitEnum fileVisitEnum, String id){
        String test=new String();
        synchronized (test){
            if(StringUtils.isNotEmpty(id)){
                this.setDatePathGeneratorImplId(id);
            }
            return YqSystemFilePathImpl.getUpfileFilePath().getFilePath(fileVisitEnum,this);
        }
    }

    @Override
    public String path() {
        return getPathGenerator().generatorPath();
    }
}
