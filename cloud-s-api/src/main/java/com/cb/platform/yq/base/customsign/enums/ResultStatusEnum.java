package com.cb.platform.yq.base.customsign.enums;
public enum ResultStatusEnum {
	SB(-1,"失败"),
	CG(0,"成功"),
	CLOSE(-2,"关闭"),
	CLOSEREQUEST(-3,"关闭请求"),
	NOTACTIVE_SYSTEM(-4,"未激活系统"),
	PERSON(-5,"请配置个人签名或企业签名"),
	FILENOTEXIST(-7,"文件不存在");
	ResultStatusEnum(int code, String name) {
		this.code=code;
		this.name=name;
	}
	private int code;
	private String name;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}