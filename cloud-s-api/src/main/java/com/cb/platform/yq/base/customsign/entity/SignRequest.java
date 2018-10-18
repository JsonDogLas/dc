package com.cb.platform.yq.base.customsign.entity;

import com.ceba.base.enums.CertificationLevelEnum;
import com.ceba.base.enums.UserSignTypeEnum;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.util.List;

/**
 * 签名请求
 * @author Administrator
 */
public class SignRequest {
	private List<SignPrintPDF> signPrintPdfList;
	private String virtualPath;//签名文件 - 相对路径--pdf
	private String signPageKey;//签名key
	private String certificationLevel;//修订/验证 CertificationLevelEnum
	private UserSignTypeEnum userSignTypeEnum;
	private String fileId;//文件ID


	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getCertificationLevel() {
		return certificationLevel;
	}
	public void setCertificationLevel(String certificationLevel) {
		this.certificationLevel = certificationLevel;
	}
	
	public CertificationLevelEnum getCertificationLevelEnum(){
		return CertificationLevelEnum.getFlag(this.certificationLevel);
	}
	
	public UserSignTypeEnum getUserSignTypeEnum() {
		return userSignTypeEnum;
	}
	public void setUserSignTypeEnum(UserSignTypeEnum userSignTypeEnum) {
		this.userSignTypeEnum = userSignTypeEnum;
	}
	public void setUserSignType(String code){
		UserSignTypeEnum signTypeEnum= UserSignTypeEnum.getUserSignTypeEnum(NumberUtils.toInt(code));
		this.userSignTypeEnum=signTypeEnum;
	}
	
	public static void sgingPrintPDFtoStamp(Stamp stamp, SignPrintPDF signPrintPDF){
		stamp.setId(signPrintPDF.getId());
		stamp.setStampX(signPrintPDF.getPdf_x());
		stamp.setStampPage(signPrintPDF.getPage());	
		stamp.setStampY(signPrintPDF.getPdf_y());
		float imgWidth=0,imgHeight=0;
		if(!signPrintPDF.getIsZoomRate()){
			imgWidth = signPrintPDF.getImageWidth()*NumberUtils.toFloat(signPrintPDF.getZoomRate(),1);
			imgHeight = signPrintPDF.getImageHeight()*NumberUtils.toFloat(signPrintPDF.getZoomRate(),1);
		}else{
			imgWidth = stamp.getImgWidth();
			imgHeight = stamp.getImgHeight();
		}
		stamp.setImgHeight(imgHeight);
		stamp.setImgWidth(imgWidth);
		if(StringUtils.isNotBlank(signPrintPDF.getImagePath()) && StringUtils.isBlank(stamp.getFilePath())){
			stamp.setFilePath(signPrintPDF.getImagePath());
		}
	}
	public String getSignPageKey() {
		return signPageKey;
	}
	public void setSignPageKey(String signPageKey) {
		this.signPageKey = signPageKey;
	}

	public String getVirtualPath() {
		return virtualPath;
	}

	public void setVirtualPath(String virtualPath) {
		this.virtualPath = virtualPath;
	}

	public List<SignPrintPDF> getSignPrintPdfList() {
		return signPrintPdfList;
	}

	public void setSignPrintPdfList(List<SignPrintPDF> signPrintPdfList) {
		this.signPrintPdfList = signPrintPdfList;
	}
}
