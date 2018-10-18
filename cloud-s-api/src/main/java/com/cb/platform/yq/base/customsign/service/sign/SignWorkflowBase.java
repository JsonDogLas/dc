package com.cb.platform.yq.base.customsign.service.sign;

import com.cb.platform.yq.api.utils.ID;
import com.cb.platform.yq.base.customsign.entity.SignPrintPDF;
import com.cb.platform.yq.base.customsign.entity.SignRequest;
import com.cb.platform.yq.base.customsign.entity.Stamp;
import com.cb.platform.yq.base.customsign.enums.ResultStatusEnum;
import com.cb.platform.yq.base.customsign.service.sign.impl.DigitalSignObject;
import com.cb.platform.yq.base.customsign.service.sign.impl.NormalSignObject;
import com.cb.platform.yq.base.filepath.enums.FileVisitEnum;
import com.cb.platform.yq.base.filepath.enums.YqFilePathEnum;
import com.ceba.base.enums.CertificationLevelEnum;
import com.ceba.base.enums.UserSignTypeEnum;
import com.ceba.base.sign.service.SignBase;
import com.ceba.base.utils.ToPdfUtils;
import com.ceba.base.web.response.IResult;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SignWorkflowBase extends SignBase {
	public enum SignError{
		WORKFILOW_FILE_NO_EMPORT("WORKFILOW_FILE_NO_EMPORT","文件不存在"),
		WORKFILOW_FILE_NO_SING("WORKFILOW_FILE_NO_SING","文件类型不支持签名"),
		WORKFILOW_TEMP_DIR_ERROR("WORKFILOW_TEMP_DIR_ERROR","当前文件不能转为PDF"),
		WORKFILOW_SERVIC_SIGN_ERROR("WORKFILOW_SERVIC_SIGN_ERROR","签名失败"),
		WORKFILOW_SERVIC_SIGN_SUCCESS("WORKFILOW_SERVIC_SIGN_SUCCESS","签名成功");
		private String errorCode;
		private String errorInfo;
		private SignError(String errorCode,String errorInfo){
			this.errorCode=errorCode;
			this.errorInfo=errorInfo;
		}
		public String getErrorCode() {
			return errorCode;
		}
		public void setErrorCode(String errorCode) {
			this.errorCode = errorCode;
		}
		public String getErrorInfo() {
			return errorInfo;
		}
		public void setErrorInfo(String errorInfo) {
			this.errorInfo = errorInfo;
		}
	}

	public SignWorkflowBase(UserSignTypeEnum userSignType, String newFileId, CertificationLevelEnum certificationLevelEnum) {
		if( userSignType== UserSignTypeEnum.DIGITAL_SIGN ||
				userSignType == UserSignTypeEnum.BLUE_DIGITAL_SIGN){
			this.signObject = new DigitalSignObject(newFileId);
		}else if( userSignType == UserSignTypeEnum.NORMAL_SIGN){
			this.signObject = new NormalSignObject();
		}
	}
	
	public IResult signProcess(Map<SignPrintPDF,Stamp> signPrintPdfMap, String inFilePath,
                               String outFilePath, String tempDirectory, UserSignTypeEnum signTypeEnum) throws Exception{
		IResult iResult=new IResult();
		String validResult=validFile(inFilePath);
		if(StringUtils.isNotBlank(validResult)){
			iResult.setResultCode(ResultStatusEnum.SB.getCode());
			iResult.setMessage(validResult);
			return iResult;
		}
		String inFilePathTest= ToPdfUtils.toPdfFile(inFilePath, tempDirectory);
		if(StringUtils.isBlank(inFilePathTest)){
			String message=errorShow(SignError.WORKFILOW_TEMP_DIR_ERROR);
			iResult.setResultCode(ResultStatusEnum.SB.getCode());
			iResult.setMessage(message);
			return iResult;
		}
		List<Stamp> stampList=new ArrayList<>();
		if(MapUtils.isEmpty(signPrintPdfMap)
				&& !signTypeEnum.equals(UserSignTypeEnum.BLUE_DIGITAL_SIGN )){
			return iResult;
		}  
		if(!signTypeEnum.equals(UserSignTypeEnum.BLUE_DIGITAL_SIGN )){
			SignPrintPDF[] signPrintPDFs=new SignPrintPDF[signPrintPdfMap.keySet().size()];
			int index=0;
			for(SignPrintPDF signPrintPDF:signPrintPdfMap.keySet()){
				if(signPrintPDF !=null){
					SignRequest.sgingPrintPDFtoStamp(signPrintPdfMap.get(signPrintPDF),signPrintPDF);
					signPrintPDFs[index]=signPrintPDF;
					index++;
				}
			}
			//打印层排序
			Arrays.sort(signPrintPDFs,new SignPrintPdfComparator());

			for(int i=0;i<signPrintPDFs.length;i++){
				SignPrintPDF signPrintPDF = signPrintPDFs[i];
				Stamp st = signPrintPdfMap.get(signPrintPDFs[i]);
				Stamp stamp=Stamp.copySignPrintPDF(st,signPrintPDF);
				stampList.add(stamp);
			}
		}
		JSONObject paramList = new JSONObject();
		paramList.put("stampList", new JSONArray(stampList)); 
		String tempDir = YqFilePathEnum.LOCAL_SERVER_ROOT.getFilePath(FileVisitEnum.ABSOLUTE, ID.getGuid());
		JSONObject signResult=signObject.sign(paramList,tempDir,inFilePathTest, outFilePath);
		if(signResult.getBoolean("status")){
			iResult.setResultCode(ResultStatusEnum.CG.getCode());
		}else{
			String message=signResult.getString("message");
			iResult.setResultCode(ResultStatusEnum.SB.getCode());
			iResult.setMessage(message);
		}
		return iResult;
	}
	/**
	 * 签章文件
	 * @param inFilePath
	 * @return
	 */
	public String validFile(String inFilePath){
		File file=new File(inFilePath);
		if(!file.exists()){
			return errorShow(SignError.WORKFILOW_FILE_NO_EMPORT);
		}
		String name=file.getName();
		String[] names=name.split(".");
		if(!StringUtils.equals(signFileType(), "all")){
			if(StringUtils.contains(signFileType(), names[1])){
				return errorShow(SignError.WORKFILOW_FILE_NO_EMPORT);
			}
		}
		
		return null;
	}
	
	
	/**
	 * 签名的文件类型 
	 * @return
	 */
	public String signFileType(){
		return "all";
	}
	/**
	 * 显示签名错误信息
	 * @param signError
	 * @return
	 */
	public String errorShow(SignError signError){
		return signError.getErrorInfo();
	}

}
