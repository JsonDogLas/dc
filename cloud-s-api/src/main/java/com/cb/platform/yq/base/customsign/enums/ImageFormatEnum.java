package com.cb.platform.yq.base.customsign.enums;

public enum ImageFormatEnum {
	PNG("png"),JPG("jpg");
	private String imageFormat;
	ImageFormatEnum(String imageFormat){
		this.imageFormat=imageFormat;
	}
	public String getImageFormat() {
		return imageFormat;
	}

	public void setImageFormat(String imageFormat) {
		this.imageFormat = imageFormat;
	}
	
}
