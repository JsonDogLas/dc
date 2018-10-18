package com.cb.platform.yq.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 上传印章VO
 * @author whh
 */
@ApiModel(value="上传印章",description = "返回结果")
public class UploadStampVO {
    /**
     * 上传路径
     */
    @ApiModelProperty(value = "上传路径")
    private String imagePath;

    /**
     * 印章类型
     */
    @ApiModelProperty(value = "印章类型 1印章 4骑缝印章")
    private String stampType;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getStampType() {
        return stampType;
    }

    public void setStampType(String stampType) {
        this.stampType = stampType;
    }
}
