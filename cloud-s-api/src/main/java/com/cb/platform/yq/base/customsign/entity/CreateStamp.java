package com.cb.platform.yq.base.customsign.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 创建二维码
 * @author Administrator
 *
 */
public class CreateStamp implements Serializable{
	private static final long serialVersionUID = 3192851612192309346L;
	private List<Stamp> resultStampList;//返回的stamp
	private List<Stamp> uploadStampList;//上传的stamp
	private Map<String,String> baseMap;//key base64 value stamp ID
	private String signPageKey;//签名页面key
	
	// null代表未执行  false关闭已经执行  ture需要执行关闭
	private Boolean qrcodeIsClose;//二维码页面是否需要关闭
	private Boolean signPageIsClose;//签名页面是否关闭  手写页面 点击关闭后，浏览器就不需要请求 手写章
	private Boolean uploadBaseIsClose;//
	
	private String qrcodeUrl;
	
	
	//-----------------上传时间限制------在 uploadTime 内没有上传 则关闭请求，关闭上传机制------------------
	private int uploadTime;//上传时间限制   
	private Date startTime;
	private Date nowTime;
	public List<Stamp> getResultStampList() {
		return resultStampList;
	}
	public void setResultStampList(List<Stamp> resultStampList) {
		this.resultStampList = resultStampList;
	}
	public List<Stamp> getUploadStampList() {
		return uploadStampList;
	}
	public void setUploadStampList(List<Stamp> uploadStampList) {
		this.uploadStampList = uploadStampList;
	}
	public Map<String, String> getBaseMap() {
		return baseMap;
	}
	public void setBaseMap(Map<String, String> baseMap) {
		this.baseMap = baseMap;
	}
	public String getSignPageKey() {
		return signPageKey;
	}
	public void setSignPageKey(String signPageKey) {
		this.signPageKey = signPageKey;
	}
	public Boolean getQrcodeIsClose() {
		return qrcodeIsClose;
	}
	public void setQrcodeIsClose(Boolean qrcodeIsClose) {
		this.qrcodeIsClose = qrcodeIsClose;
	}
	public Boolean getSignPageIsClose() {
		return signPageIsClose;
	}
	public void setSignPageIsClose(Boolean signPageIsClose) {
		this.signPageIsClose = signPageIsClose;
	}
	public Boolean getUploadBaseIsClose() {
		return uploadBaseIsClose;
	}
	public void setUploadBaseIsClose(Boolean uploadBaseIsClose) {
		this.uploadBaseIsClose = uploadBaseIsClose;
	}
	public String getQrcodeUrl() {
		return qrcodeUrl;
	}
	public void setQrcodeUrl(String qrcodeUrl) {
		this.qrcodeUrl = qrcodeUrl;
	}
	public int getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(int uploadTime) {
		this.uploadTime = uploadTime;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getNowTime() {
		return nowTime;
	}
	public void setNowTime(Date nowTime) {
		this.nowTime = nowTime;
	}
	
	
	
	
	
	
	
}
