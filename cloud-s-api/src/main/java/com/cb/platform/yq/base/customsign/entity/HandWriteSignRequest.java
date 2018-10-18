package com.cb.platform.yq.base.customsign.entity;

import java.util.List;


public class HandWriteSignRequest {
	private String signPageKey;//签名页面key
	private String base64;//base64
	private String name;//名称
	private String type;//类型
	private String isCloseSignRequest;//是否关闭签章请求 1 关闭 0 不关闭    手写页面关闭请求
	
	private List<String> saveStampList;//需要保存的手写签名
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getSaveStampList() {
		return saveStampList;
	}

	public void setSaveStampList(List<String> saveStampList) {
		this.saveStampList = saveStampList;
	}

	public String getIsCloseSignRequest() {
		return isCloseSignRequest;
	}

	public void setIsCloseSignRequest(String isCloseSignRequest) {
		this.isCloseSignRequest = isCloseSignRequest;
	}

	public String getBase64() {
		return base64;
	}

	public void setBase64(String base64) {
		this.base64 = base64;
	}

	public String getSignPageKey() {
		return signPageKey;
	}

	public void setSignPageKey(String signPageKey) {
		this.signPageKey = signPageKey;
	}
	
	
}
