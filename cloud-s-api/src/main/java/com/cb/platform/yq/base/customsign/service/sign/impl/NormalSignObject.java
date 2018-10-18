package com.cb.platform.yq.base.customsign.service.sign.impl;

import com.cb.platform.yq.api.utils.ID;
import com.cb.platform.yq.base.customsign.entity.StampTypeEnum;
import com.cb.platform.yq.base.customsign.enums.IntlEnum;
import com.cb.platform.yq.base.customsign.service.sign.SignInterface;
import com.ceba.base.enums.SealTypeEnum;
import com.ceba.base.sign.enums.ErrSign;
import com.ceba.base.utils.PDFUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class NormalSignObject extends SignInterface {
	private void checkSystemCanSign(Object sign,JSONObject object ) throws Exception{
		checkSystemCanSign(object);
		if(!(ErrSign.ERR_0.getCode()+"").equals(object.get("status"))){
			object.put("status", false);
			return ;
		}
		object.put("status",true );
		return ;
	}
	/**
	 * 普通签名
	 * paramList={"stampList":,"gCertificateServer":}
	 */
	public JSONObject sign(JSONObject paramList,String tempDir, String inFilePath, String outFilePath)  throws Exception{
		JSONObject object  = new JSONObject();
		checkSystemCanSign(null, object);
		if(!object.getBoolean("status")){
			return object;
		}
		JSONArray stampList = paramList.getJSONArray("stampList");
		if(!PDFUtils.checkFileHasRSASigned(inFilePath)){
			if(!PDFUtils.checkFileHasSM2Signed(inFilePath)){
				JSONArray stampListUserArray=new JSONArray();
				JSONObject stampListWater=null;
				JSONArray stampListQiFengArray=new JSONArray();
				for(int i=0;i<stampList.length();i++){
					JSONObject jsonObject=(JSONObject) stampList.get(i);
					if(StringUtils.equals(jsonObject.getString("stampType"), StampTypeEnum.SELF.getCode())){
						stampListUserArray.put(stampList.get(i));
					}else if(StringUtils.equals(jsonObject.getString("stampType"), StampTypeEnum.COMPANY.getCode())){
						stampListUserArray.put(stampList.get(i));
					}else if(StringUtils.equals(jsonObject.getString("stampType"), StampTypeEnum.DATE.getCode())){
						stampListUserArray.put(stampList.get(i));
					}else if(StringUtils.equals(jsonObject.getString("stampType"), StampTypeEnum.WATERMARK.getCode())){
						stampListWater=jsonObject;
					}else if(StringUtils.equals(jsonObject.getString("stampType"), StampTypeEnum.QIFENG.getCode())){
						stampListQiFengArray.put(stampList.get(i));
					}
				}
				//文件管理修改 中间的 临时文件可以随意放置
				//String tempPath=LogicPathUtil.getPhysicalTempDirPath(null)+ID.getGuid()+".pdf";
				String tempPath = tempDir + ID.getGuid()+".pdf";
				//个人签名,公章,资质章
				if(stampListUserArray.length() > 0){
					object.put("status",  insertNormalImages(inFilePath, tempPath, stampListUserArray));
					if(!object.getBoolean("status")){
						return object;
					}
					inFilePath =tempPath;
					tempPath = tempDir + ID.getGuid()+".pdf";
				}
				//水印
				if(stampListWater != null){
					JSONObject result=insertQianWater(stampListWater,inFilePath,tempPath);
					if(!result.getBoolean("status")){
						return result;
					}
					inFilePath =tempPath;
					tempPath = tempDir + ID.getGuid()+".pdf";
					object.put("status",result.getBoolean("status"));
				}
				//骑缝章
				if(stampListQiFengArray.length() > 0){
					JSONObject result=insertQFStampForFacade(stampListQiFengArray,tempDir,inFilePath,tempPath);
					if(!result.getBoolean("status")){
						return result;
					}
					inFilePath =tempPath;
					object.put("status",result.getBoolean("status"));
				}
				FileUtils.copyFile(new File(inFilePath), new File(outFilePath));
				return object;
			}else{
				object.put("status", false);
				object.put("message", IntlEnum.SIGN_FILE_HAS_SIGNED.get());
				return object;
			}
		}else{
			object.put("status", false);
			object.put("message", IntlEnum.SIGN_FILE_HAS_SIGNED.get());
			return object;
		}
	}

	/**
	 * 批量插入骑缝章
	 * @param stampList
	 * @param inFilePath
	 * @param outFilePath
	 * @return
	 */
	private JSONObject insertQFStampForFacade(JSONArray stampList,String tempDir, String inFilePath, String outFilePath){
		JSONObject jsonObjectResult=new JSONObject();
		String path= tempDir+ID.getGuid()+".pdf";
		for(int i=0;i<stampList.length();i++){
			JSONObject jsonObject=(JSONObject) stampList.get(i);
			boolean bool=true;
			try {
				jsonObject.put("insertFlag", "1");
				jsonObject.put("saveSplit", "2");
				jsonObject.put("hPercent",Float.valueOf(jsonObject.get("positionRate").toString())+0.017+"");
				jsonObject.put("splitWeight", QFSPLITWEIGHT);
				PDFUtils.insertQFStampByRule(inFilePath,path,jsonObject.getString("filePath"),jsonObject);
				if(i== stampList.length()-1){//没有下一个
					inFilePath=path;
				}else{
					inFilePath = path;
					path=tempDir+ID.getGuid()+".pdf";
				}
			}catch (Exception e) {
				jsonObjectResult.put("status", false);
				jsonObjectResult.put("message", IntlEnum.SIGN_FILE_HAS_SIGNED.get());
				return jsonObjectResult;
			}
			if(!bool){
				jsonObjectResult.put("status", false);
				jsonObjectResult.put("message", IntlEnum.SIGN_FILE_HAS_SIGNED.get());
				return jsonObjectResult;
			}
		}
		try {
			FileUtils.copyFile(new File(path), new File(outFilePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		jsonObjectResult.put("status", true);
		return jsonObjectResult;
	}


	/**
	 * 插入水印
	 * @param jsonObjectStamp
	 * @param inFilePath 输入文件路径
	 * @param outFilePath 输出文件路径
	 */
	public static JSONObject insertQianWater(JSONObject jsonObjectStamp, String inFilePath, String outFilePath) {
		JSONObject jsonObjectResult=new JSONObject();
		boolean bool=false;
		try {
			bool = PDFUtils.insertQianWater(inFilePath,outFilePath,jsonObjectStamp.getString("filePath"));
		} catch (Exception e) {
			jsonObjectResult.put("status", false);
			jsonObjectResult.put("message", IntlEnum.INSERT_WATERMARK_FAIL.get());
			return jsonObjectResult;
		}
		if(!bool){
			jsonObjectResult.put("status", false);
			jsonObjectResult.put("message", IntlEnum.INSERT_WATERMARK_FAIL.get());
			return jsonObjectResult;
		}
		jsonObjectResult.put("status", true);
		return jsonObjectResult;
	}

	/**
	 * 插入多个非签名章时调用
	 * @param inFilePath
	 * @param outPath
	 */
	public static boolean insertNormalImages(String inFilePath,String outPath,JSONArray stamps){
		boolean isFlag = true;
		try{
			if(stamps !=null && stamps.length() >0){
				for( int i=0 ; i < stamps.length() ; i++){
					if(stamps.get(i) !=null){
						JSONObject gs = stamps.getJSONObject(i);
						gs.put("stamp_x",gs.getString("stampX"));
						gs.put("stamp_y",gs.getString("stampY"));
						gs.put("imgWidth",gs.getDouble("imgWidth")+"");
						gs.put("imgHeight",gs.getDouble("imgHeight")+"");
						gs.put("sealType", SealTypeEnum.DRAFTING_POSTION_STAMP_FLAG.getType());
					}
				}
				return PDFUtils.insertImagesByXY(outPath,inFilePath,stamps);
			}

		}catch (Exception e){
			return false;
		}
		return isFlag ;
	}

}
