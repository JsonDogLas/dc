package com.cb.platform.yq.base.customsign.entity;

import com.cb.platform.yq.base.filepath.enums.FileVisitEnum;
import com.cb.platform.yq.base.filepath.enums.YqFilePathEnum;
import com.cb.platform.yq.base.filepath.service.YqSystemFilePathImpl;

import java.util.Arrays;

/**
 * 加载文件图片请求
 * 主要用于PDF转图片
 * @author wuhaihu
 */
public class LoadFileImageReqeust {
	private String fileId;//文件ID-转图片需要的文件ID
	private String filePath;//文件路径 -转图片需要的文件路径   绝对路径必传
	private String[] pageArray;//需要转换的页码
	private String[] deletePageArrray;//需要删除的页码
	private boolean isNeedNewPage;//是否需要新的页面  【true 删除原来的页 在转换】【false 如果页存在则 直接取】
	private boolean isDeletePage;//文件ID 与 NEW_FILE_ID 中的ID  不一样是否删除所有页
	private String tempPath=YqSystemFilePathImpl.getUpfileFilePath().getFilePath(FileVisitEnum.ABSOLUTE, YqFilePathEnum.LOCAL_FILE_IMAGE);
	private String fileToPdfPath= YqSystemFilePathImpl.getUpfileFilePath().getFilePath(FileVisitEnum.ABSOLUTE, YqFilePathEnum.LOCAL_SERVER_ROOT);//文件转pdf路径  ;

	//-----非必传项-----------
	private String pdfPath;//



	public String[] getDeletePageArrray() {
		return deletePageArrray;
	}
	public void setDeletePageArrray(String[] deletePageArrray) {
		this.deletePageArrray = deletePageArrray;
	}
	public String getPdfPath() {
		return pdfPath;
	}
	public void setPdfPath(String pdfPath) {
		this.pdfPath = pdfPath;
	}
	public String getFileToPdfPath() {
		return fileToPdfPath;
	}
	public void setFileToPdfPath(String fileToPdfPath) {
		this.fileToPdfPath = fileToPdfPath;
	}
	public String getTempPath() {
		YqFilePathEnum.LOCAL_FILE_IMAGE.setDatePathGeneratorImplId(this.fileId);
		return YqSystemFilePathImpl.getUpfileFilePath().getFilePath(FileVisitEnum.ABSOLUTE, YqFilePathEnum.LOCAL_FILE_IMAGE);
	}
	public void setTempPath(String tempPath) {
		this.tempPath = tempPath;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public boolean isNeedNewPage() {
		return isNeedNewPage;
	}
	public void setIsNeedNewPage(boolean isNeedNewPage) {
		this.isNeedNewPage = isNeedNewPage;
	}
	public boolean isDeletePage() {
		return isDeletePage;
	}
	public void setIsDeletePage(boolean isDeletePage) {
		this.isDeletePage = isDeletePage;
	}
	public String[] getPageArray() {
		return pageArray;
	}
	public void setPageArray(String[] pageArray) {
		this.pageArray = pageArray;
	}
	@Override
	public String toString() {
		return "LoadFileImageReqeust [ fileId=" + fileId + ", filePath=" + filePath
				+ ", pageArray=" + Arrays.toString(pageArray) + ", isNeedNewPage=" + isNeedNewPage + ", isDeletePage="
				+ isDeletePage + ", tempPath=" + tempPath + "]";
	}


}
