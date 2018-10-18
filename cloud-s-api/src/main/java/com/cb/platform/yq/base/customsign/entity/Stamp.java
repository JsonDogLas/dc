package com.cb.platform.yq.base.customsign.entity;

import com.cb.platform.yq.base.customsign.enums.StampFromEnum;
import com.ceba.cebacer.EncodeSignType;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.json.JSONObject;

import java.util.Date;

/**
 * 签章
 * @author Administrator
 *
 */
public class Stamp {
	private String id;//id
	private String matchName;//匹配关键字
	private float imgWidth = 70;//图片的宽度
	private float imgHeight = 70;//图片的高度
	private String filePath;//章(虚拟路径)
	private String stampType;//印章  1公司章，0私人章
	private String stampName;//章名称
	private String stampX;//定位的x坐标
	private String stampY;//定位的y坐标
	private String stampPage;//盖章页码
	private String cerNo;//证书
	//1--rsa 2--sm2
	private int encodeSignType = EncodeSignType.RSA.getCode();//加密方式
	private String createUser;//创建用户
	private Date createTime;//创建时间
	private String updateUser;//更新用户
	private String updateTime;//更新时间
	private String storageAddressId;//文件存储ID

	//以下字段非数据库中字段
	private float positionRate;//骑缝章的位置比例   靠右边
	private String auditoryFlay;//印章授权  0—待审核  1—已通过  2—未通过 AudtoryFlayEnums

	private String filePathUrl;
	//一下非表中字段
	private String stampFromFlag= StampFromEnum.API_PLAT.getNumber();//印章来源 1云签API 2第三方 3电子文件平台 StampFromEnum

	public String getStampFromFlag() {
		return stampFromFlag;
	}

	public void setStampFromFlag(String stampFromFlag) {
		this.stampFromFlag = stampFromFlag;
	}

	public String getStorageAddressId() {
		return storageAddressId;
	}
	public void setStorageAddressId(String storageAddressId) {
		this.storageAddressId = storageAddressId;
	}
	public float getPositionRate() {
		return positionRate;
	}
	public void setPositionRate(float positionRate) {
		this.positionRate = positionRate;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMatchName() {
		return matchName;
	}
	public void setMatchName(String matchName) {
		this.matchName = matchName;
	}
	public float getImgWidth() {
		return imgWidth;
	}
	public void setImgWidth(float imgWidth) {
		this.imgWidth = imgWidth;
	}
	public float getImgHeight() {
		return imgHeight;
	}
	public void setImgHeight(float imgHeight) {
		this.imgHeight = imgHeight;
	}
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getStampType() {
		return stampType;
	}
	public void setStampType(String stampType) {
		this.stampType = stampType;
	}
	public String getStampName() {
		return stampName;
	}
	public void setStampName(String stampName) {
		this.stampName = stampName;
	}
	public String getStampX() {
		return stampX;
	}
	public void setStampX(String stampX) {
		this.stampX = stampX;
	}
	public String getStampY() {
		return stampY;
	}
	public void setStampY(String stampY) {
		this.stampY = stampY;
	}
	public String getStampPage() {
		return stampPage;
	}
	public void setStampPage(String stampPage) {
		this.stampPage = stampPage;
	}
	public String getCerNo() {
		return cerNo;
	}
	public void createUser(String cerNo) {
		this.cerNo = cerNo;
	}
	public int getEncodeSignType() {
		return encodeSignType;
	}
	public void setEncodeSignType(int encodeSignType) {
		this.encodeSignType = encodeSignType;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public Float getFloatImgWidth() {
		return imgWidth;
	}

	public Float getFloatImgHeight() {
		return imgHeight;
	}


	public int getCorFlag(){
		return Integer.valueOf(stampType);
	}


	public void setCerNo(String cerNo) {
		this.cerNo = cerNo;
	}

	public String getAuditoryFlay() {
		return auditoryFlay;
	}
	public void setAuditoryFlay(String auditoryFlay) {
		this.auditoryFlay = auditoryFlay;
	}

	public String getFilePathUrl() {
		return filePathUrl;
	}

	public void setFilePathUrl(String filePathUrl) {
		this.filePathUrl = filePathUrl;
	}
	public static Stamp createStamp(String id, String matchName, float imgWidth, float imgHeight, String filePath, String stampType,
                                    String stampName, String stampX, String stampY, String stampPage, String cerNo, int encodeSignType) {

		Stamp stamp = new Stamp();
		stamp.setId(id);
		stamp.setMatchName(matchName);
		stamp.setImgWidth(imgWidth);
		stamp.setImgHeight(imgHeight);
		stamp.setFilePath(filePath);
		stamp.setStampType(stampType);
		stamp.setStampName(stampName);
		stamp.setStampX(stampX);
		stamp.setStampY(stampY);
		stamp.setStampPage(stampPage);
		stamp.setCerNo(cerNo);
		stamp.setEncodeSignType(encodeSignType);

		return stamp;
	}



	public static Stamp createStamp(String id, String matchName, float imgWidth, float imgHeight,
                                    String filePath, String stampType,
                                    String stampName, String storageAddressId) {
		Stamp stamp = new Stamp();
		stamp.setId(id);
		stamp.setMatchName(matchName);
		stamp.setImgWidth(imgWidth);
		stamp.setImgHeight(imgHeight);
		stamp.setFilePath(filePath);
		stamp.setStampType(stampType);
		stamp.setStampName(stampName);
		stamp.setStorageAddressId(storageAddressId);
		return stamp;
	}
	/**
	 * CorStampName 字符串处理
	 * @return
	 */
	public String getStampNameStr(){
		String result=null;
		if(StringUtils.isNotBlank(stampName) &&  stampName.length()>4){
			result=stampName.substring(0, 4)+"...";
		}
		return result;
	}

	//克隆属性
	public static void parseStampToJson(Stamp gsFront, JSONObject oo){
		oo.put("id", gsFront.getId());
		oo.put("imgWidth",gsFront.getImgWidth());
		oo.put("imgHeight",gsFront.getImgHeight());
		oo.put("matchName",gsFront.getMatchName());
		oo.put("stampPath",gsFront.getFilePath());
		oo.put("stampX",gsFront.getStampX());
		oo.put("stampY",gsFront.getStampY());
		oo.put("stampPage",gsFront.getStampPage());
		oo.put("stampType",gsFront.getStampType());
		oo.put("stampName",gsFront.getStampName());
		oo.put("cerNo",gsFront.getCerNo());
		oo.put("encodeSignType", gsFront.getEncodeSignType());
		oo.put("storageAddressId", gsFront.getStorageAddressId());
	}

	public static void copyStamp(Stamp stamp, Stamp orig){
		stamp.setId(orig.getId());
		stamp.setCerNo(orig.getCerNo());
		stamp.setMatchName(orig.getMatchName());
		stamp.setImgHeight(orig.getImgHeight());
		stamp.setImgWidth(orig.getImgWidth());
		stamp.setFilePath(orig.getFilePath());
		stamp.setStampType(orig.getStampType());
		stamp.setStampName(orig.getStampName());
		stamp.setStampX(orig.getStampX());
		stamp.setStampY(orig.getStampY());
		stamp.setStampPage(orig.getStampPage());
		stamp.setEncodeSignType(orig.getEncodeSignType());
		stamp.setStorageAddressId(orig.getStorageAddressId());
		stamp.setCreateUser(orig.getCreateUser());
		stamp.setCreateTime(orig.getCreateTime());
		stamp.setUpdateTime(orig.getUpdateTime());
		stamp.setUpdateUser(orig.getUpdateUser());
	}

	public static Stamp copySignPrintPDF(Stamp st, SignPrintPDF signPrintPDF) {
		Stamp stamp =new Stamp();
		copyStamp(stamp, st);
		float imgWidth=0,imgHeight=0;
		if(!signPrintPDF.getIsZoomRate()){
			imgWidth = signPrintPDF.getImageWidth()* NumberUtils.toFloat(signPrintPDF.getZoomRate(),1);
			imgHeight = signPrintPDF.getImageHeight()*NumberUtils.toFloat(signPrintPDF.getZoomRate(),1);
		}else{
			imgHeight=signPrintPDF.getImageHeight();
			imgWidth=signPrintPDF.getImageWidth();
		}
		stamp.setImgHeight(imgHeight);
		stamp.setImgWidth(imgWidth);
		stamp.setStampX(signPrintPDF.getPdf_x());
		stamp.setStampY(signPrintPDF.getPdf_y());
		stamp.setStampPage(signPrintPDF.getPage());
		stamp.setPositionRate(signPrintPDF.getPositionRate());
		return stamp;
	}
}
