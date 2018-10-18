package com.cb.platform.yq.base.customsign.entity;
/**
 * PDF图片
 * @author Administrator
 *
 */
public class PdfImage {
	//pdf id
	private String id;
	//pdf文件名
	private String fileName;
	//pdf图片路径
	private String imagePath;
	//pdf图片缩放倍率
	private float zoomRate;
	//pdf图片是否缩放过
	private boolean zoom;
	//pdf图片所在页码
	private String page;
	
	private float width;//x轴
	private float height;//y轴
	
	
	public float getWidth() {
		return width;
	}
	public void setWidth(float width) {
		this.width = width;
	}
	public float getHeight() {
		return height;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	public float getZoomRate() {
		return zoomRate;
	}
	public void setZoomRate(float zoomRate) {
		this.zoomRate = zoomRate;
	}
	public boolean isZoom() {
		return zoom;
	}
	public void setZoom(boolean zoom) {
		this.zoom = zoom;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	
	
}
