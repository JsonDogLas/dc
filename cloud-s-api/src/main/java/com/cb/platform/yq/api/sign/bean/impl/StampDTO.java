package com.cb.platform.yq.api.sign.bean.impl;

import com.cb.platform.yq.base.customsign.entity.Stamp;
import com.cb.platform.yq.base.filepath.enums.FileVisitEnum;
import com.cb.platform.yq.base.filepath.service.YqSystemFilePathImpl;
import com.ceba.base.utils.Base64FileUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.File;

/**
 * 印章返回数据
 */
@ApiModel(value="签章",description = "签名页面所有的签章数据")
public class StampDTO {

    /**
     * 印章ID
     */
    @ApiModelProperty(value = "印章ID")
    private String id;

    /**
     * 图片宽度
     */
    @ApiModelProperty(value = "图片的宽度")
    private float imgWidth;

    /**
     * 图片的高度
     */
    @ApiModelProperty(value = "图片的高度")
    private float imgHeight;

    /**
     * 图片印章路径
     */
    @ApiModelProperty(value = "图片印章路径")
    private String base64Context;


    /**
     * 印章类型
     */
    @ApiModelProperty(value = "印章类型")
    private String stampType;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getImgWidth() {
        return imgWidth;
    }

    public void setImgWidth(float imgWidth) {
        this.imgWidth = imgWidth;
    }

    public float getImgHeight() {
        return imgHeight;
    }

    public void setImgHeight(float imgHeight) {
        this.imgHeight = imgHeight;
    }

    public String getBase64Context() {
        return base64Context;
    }

    public void setBase64Context(String base64Context) {
        this.base64Context = base64Context;
    }

    public String getStampType() {
        return stampType;
    }

    public void setStampType(String stampType) {
        this.stampType = stampType;
    }

    /**
     * stamp 转换为
     * @param stamp
     * @return
     */
    public static StampDTO stampToStampDTO(Stamp stamp){
        StampDTO stampDTO=new StampDTO();
        stampDTO.setId(stamp.getId());
        stampDTO.setImgHeight(stamp.getImgHeight());
        stampDTO.setImgWidth(stamp.getImgWidth());
        stampDTO.setStampType(stamp.getStampType());
        String absolutePath= YqSystemFilePathImpl.getUpfileFilePath().changeFileVisit(FileVisitEnum.BROWSER,FileVisitEnum.ABSOLUTE,stamp.getFilePath());
        String base64=Base64FileUtils.encodeBase64File(absolutePath);
        stampDTO.setBase64Context(base64);
        return stampDTO;
    }
}
