package com.cb.platform.yq.base.customsign.entity;

import java.util.List;

/**
 *
 */
public class HandWriteSignResponse {
	public enum Action{
		COLSE_QRCODE_PAGE("关闭二维码页面","1","WORKFLOW_COLSE_QRCODE_PAGE"),
		RETURN_QRCODE_PAGE("返回二维码","2","WORKFLOW_COLSE_QRCODE_PAGE"),
		CLOSE_STAMP_REQUEST("关闭签章请求","3","WORKFLOW_CLOSE_STAMP_REQUEST"),
		OPEN_STAMP_REQUEST("打开签章请求","4","WORKFLOW_COLOR_RECODE_PAGE"),
		RETURN_SIGN("接收签章","5","WORKFLOW_COLOR_RECODE_PAGE"),
		NO_OPERA("不做任何操作","6","WORKFLOW_NO_OPERA");
		private Action(String exption,String code,String intl){
			this.exption=exption;
			this.code=code;
			this.intl=intl;
		}
		private String exption;//说明
		private String code;//标识
		private String intl;//国际化
		public String getExption() {
			return exption;
		}
		public void setExption(String exption) {
			this.exption = exption;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getIntl() {
			return intl;
		}
		public void setIntl(String intl) {
			this.intl = intl;
		}
	}
	
	
	private Action action;//动作
	private List<Stamp> stampList;//stampList
	private String qrcodeUrl;//二维码url
	private String signPageKey;//签名页面key
	
	
	public String getSignPageKey() {
		return signPageKey;
	}
	public void setSignPageKey(String signPageKey) {
		this.signPageKey = signPageKey;
	}
	public Action getAction() {
		return action;
	}
	public void setAction(Action action) {
		this.action = action;
	}
	public List<Stamp> getStampList() {
		return stampList;
	}
	public void setStampList(List<Stamp> stampList) {
		this.stampList = stampList;
	}
	public String getQrcodeUrl() {
		return qrcodeUrl;
	}
	public void setQrcodeUrl(String qrcodeUrl) {
		this.qrcodeUrl = qrcodeUrl;
	}
	
	public String getActionStr(){
		if(this.action != null){
			return this.action.getCode();
		}
		return null;
	}
	
}
