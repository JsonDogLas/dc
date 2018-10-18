package com.cb.platform.yq.base.customsign.entity;

import java.util.List;

public class StampsByRuleRequest {
	private String virtualPath;//切割虚拟文件ID
	private List<Stamp> stampList;//需要图片高度，宽度，路径
	public List<Stamp> getStampList() {
		return stampList;
	}
	public void setStampList(List<Stamp> stampList) {
		this.stampList = stampList;
	}

	public String getVirtualPath() {
		return virtualPath;
	}

	public void setVirtualPath(String virtualPath) {
		this.virtualPath = virtualPath;
	}
}
