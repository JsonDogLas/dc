package com.cb.platform.yq.base.customsign.service.sign;


import com.cb.platform.yq.base.customsign.entity.StampTypeEnum;
import com.ceba.base.sign.enums.ErrSign;
import com.ceba.base.sign.service.SignFatherInterface;
import com.ceba.base.utils.PDFUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;


public abstract class SignInterface extends SignFatherInterface {
	public static String QFSPLITWEIGHT="6";
	/**
	 * 判断系统是否可用
	 * 1、激活
	 * 2、过期
	 * @return
	 */
	protected void checkSystemCanSign( JSONObject json){
		//TODO
		json.put("status", ErrSign.ERR_0.getCode()+"");
		return ;
	}
	
	/**
	 * 获取可以数字签名的章列表
	 * @param stampList
	 * @return
	 */
	protected JSONArray getCanDigitSignStampList(JSONArray stampList,String inFilePath, String tempFilePath){
		JSONArray stampListUserArray=new JSONArray();
		for(int i=0;i<stampList.length();i++){
			JSONObject jsonObject=(JSONObject) stampList.get(i);
			if(StringUtils.equals(jsonObject.getString("stampType"), StampTypeEnum.SELF.getCode())){
				stampListUserArray.put(stampList.get(i));
			}else if(StringUtils.equals(jsonObject.getString("stampType"), StampTypeEnum.COMPANY.getCode())){
				stampListUserArray.put(stampList.get(i));
			}else if(StringUtils.equals(jsonObject.getString("stampType"), StampTypeEnum.DATE.getCode())){
				stampListUserArray.put(stampList.get(i));
			}else if(StringUtils.equals(jsonObject.getString("stampType"), StampTypeEnum.QIFENG.getCode())){
				jsonObject.put("splitWeight", QFSPLITWEIGHT);
				JSONArray list = PDFUtils.getQFStampsByRule(inFilePath,tempFilePath,jsonObject.getString("filePath"),jsonObject);
				if(list !=null){
					for( int j = 0 ; j < list.length() ; j ++){
						JSONObject oo = list.getJSONObject(j);
						stampListUserArray.put(oo);
					}
				}
			}
		}
		return stampListUserArray;
	}
	

}
 