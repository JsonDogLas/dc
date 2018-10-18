package com.cb.platform.yq.base.customsign.entity;

import java.util.List;

//获取pdfImage图片时返回
public class PdfImageParam {

	//获取成功
	private boolean flag ;
	
	//失败时消息
	private String message;
	
	private String inputFile;
	
	private String outPutFile;
	
	private List<PdfImage> pdfImageList ;

	
	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<PdfImage> getPdfImageList() {
		return pdfImageList;
	}

	public void setPdfImageList(List<PdfImage> pdfImageList) {
		this.pdfImageList = pdfImageList;
	}

	public String getInputFile() {
		return inputFile;
	}

	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}

	public String getOutPutFile() {
		return outPutFile;
	}

	public void setOutPutFile(String outPutFile) {
		this.outPutFile = outPutFile;
	}

	@Override
	public String toString() {
		return "PdfImageParam [flag=" + flag + ", message=" + message + ", inputFile=" + inputFile + ", outPutFile="
				+ outPutFile + ", pdfImageList=" + pdfImageList + "]";
	}
	
	
}
