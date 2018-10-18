package com.cb.platform.yq.base.customsign.entity;
/**
 * 签章打印PDF对象数据模型
 * @author Administrator
 *
 */
public class SignPrintPDF {
	/******必传项******/
	//页数
	private String page;
    //签章缩放倍率
    private String zoomRate;
    //PDF上的 X 坐标位置
    private String pdf_x;
    //PDF上的 Y 坐标位置
    private String pdf_y;
    //打印层次
    private int z_index;
    
    private String userid;
    
    private float positionRate;//骑缝章位置占比
    
    /******第一种通过ID取得******/
    //签章id
    private String id;
    /******第二种传递数据******/
    
    //PDF上的签章图片路径
    private String imagePath;
    //PDF上的签章图片height
    private float imageHeight;
    //PDF上的签章图片width
    private float imageWidth;
    //签章类型
    private String stampType;
    //缩放是否算过
    private boolean isZoomRate;
    
	
	public float getPositionRate() {
		return positionRate;
	}


	public void setPositionRate(float positionRate) {
		this.positionRate = positionRate;
	}


	public void setStampType(String stampType) {
		this.stampType = stampType;
	}
	
	
	public String getStampType() {
		return stampType;
	}


	public int getZ_index() {
		return z_index;
	}
	public void setZ_index(int z_index) {
		this.z_index = z_index;
	}
	public boolean getIsZoomRate() {
		return isZoomRate;
	}
	public void setIsZoomRate(boolean isZoomRate) {
		this.isZoomRate = isZoomRate;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public float getImageHeight() {
		return imageHeight;
	}
	public void setImageHeight(float imageHeight) {
		this.imageHeight = imageHeight;
	}
	public float getImageWidth() {
		return imageWidth;
	}
	public void setImageWidth(float imageWidth) {
		this.imageWidth = imageWidth;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getZoomRate() {
		return zoomRate;
	}
	public void setZoomRate(String zoomRate) {
		this.zoomRate = zoomRate;
	}
	public String getPdf_x() {
		return pdf_x;
	}
	public void setPdf_x(String pdf_x) {
		this.pdf_x = pdf_x;
	}
	public String getPdf_y() {
		return pdf_y;
	}
	public void setPdf_y(String pdf_y) {
		this.pdf_y = pdf_y;
	}
	
    
	
	
}
