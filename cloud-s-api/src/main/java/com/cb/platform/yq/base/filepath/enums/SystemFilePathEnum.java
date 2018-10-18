package com.cb.platform.yq.base.filepath.enums;

import com.cb.platform.yq.base.filepath.constant.SystemPathConstant;
import com.cb.platform.yq.base.filepath.service.FilePathEnumInterface;

/**
 * 文件存储类型
 */
public enum SystemFilePathEnum implements FilePathEnumInterface {
    SYSTEM_UPFILE("upfile","系统中所有文件根目录", SystemPathConstant.SYSTEM_UPFILE),
    SYSTEM_TEMP("temp","系统中所有缓存文件目录", SystemPathConstant.SYSTEM_TEMP);
    private String flag;
    private String name;
    private String path;

    SystemFilePathEnum(String flag, String name, String path) {
        this.flag = flag;
        this.name = name;
        this.path = path;
    }

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String path() {
        return path;
    }
}
