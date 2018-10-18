package com.cb.platform.yq.base.customsign.service.sign.impl;

import com.cb.platform.yq.base.customsign.enums.IntlEnum;
import com.cb.platform.yq.base.customsign.service.sign.SignInterface;
import com.ceba.base.sign.ClientSign;
import com.ceba.base.sign.enums.ErrSign;
import com.ceba.cebacer.EncodeSignType;
import com.ceba.netty.clientSign.IdsClientSign;
import org.json.JSONObject;

public class DigitalSignObject extends SignInterface {

	private String fileId;
	
	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public DigitalSignObject(String fileId) {
		this.fileId = fileId; 
	}
	
	
	private void checkSystemCanSign(ClientSign sign, JSONObject object ) throws Exception{
		checkSystemCanSign(object);
		if(!(ErrSign.ERR_0.getCode()+"").equals(object.get("status"))){
			object.put("status", false);
			return ;
		}
		object.put("status",true );
		return ;
	}
	
	/**
	 * 数字签名
	 * paramList={"stampList":,"gCertificateServer":} 
	 * @throws Exception 
	 */
	public JSONObject sign(JSONObject paramList,String tempDir, String inFilePath, String outFilePath) throws Exception {
		JSONObject object  = new JSONObject();
		checkSystemCanSign(null,object);
		if(!object.getBoolean("status")){
			return object;
		}
		//文档签名前是否有sm2签名以及RSA签名
		if(IdsClientSign.getFileHasSm2ByFileId(fileId) && IdsClientSign.getFileHasRSAByFileId(fileId)){
			//当前文档签名类型
			if(IdsClientSign.getEncodeSignTypeByFileId(fileId) == EncodeSignType.SM2.getCode()){
				object.put("status", false);
				object.put("message", IntlEnum.SIGN_FILE_HAS_RSA_SM2_SIGNED.get());
				return object;
			}
		}
		if(IdsClientSign.getSignPdf(fileId, outFilePath)){
			object.put("status",true );
		}else{
			object.put("status", false);
			object.put("message", IntlEnum.SIGN_FILE_FAIL.get());
		}
		return object;
	}

}
