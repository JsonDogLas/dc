package com.cb.platform.yq.base.customsign.entity;

import com.alibaba.druid.util.StringUtils;

public enum StampTypeEnum {
	SELF("0","签名"),
	COMPANY("1","印章"),
	DATE("2","时间")
	,WATERMARK("3","水印"),
	QIFENG("4","骑缝印章"),
	QRCODE("5","二维码"),
	TRANSPARENT("6","透明图片章")
	;
	StampTypeEnum(String code, String name) {
		this.code=code;
		this.name=name;
	}
	private String code;
	private String name;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public static StampTypeEnum getCode(String code){
		for(StampTypeEnum stampTypeEnum:StampTypeEnum.values()){
			if(StringUtils.equals(code,stampTypeEnum.code)){
				return stampTypeEnum;
			}
		}
		return null;
	}

}
