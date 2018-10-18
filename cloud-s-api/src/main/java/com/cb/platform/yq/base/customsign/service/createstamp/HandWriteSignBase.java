package com.cb.platform.yq.base.customsign.service.createstamp;

import com.ceba.base.exception.IDSException;
import com.ceba.base.web.response.IResult;

import java.util.List;


/**
 * 手写签名接口
 * @author Administrator
 *
 */
public interface HandWriteSignBase {
	/**
	 * 保存base64
	 * @param base64 base64
	 * @param signPageKey key
	 * @param type 签章类型
	 * @param name 签章名称
	 * @return 不为null 代表保存出错  为null代表保存成功
	 */
	String saveBase64(String base64, String signPageKey, String type, String name);
	
	/**
	 * 保存saveStamp
	 * @param saveStampList
	 * @param signPageKey
	 * @return
	 */
	String saveStamp(List<String> saveStampList, String signPageKey);
	
	/**
	 * 创建二维码
	 * @param signPageKey
	 * @return 返回二维码路径
	 */
	String createQrCode(String signPageKey)throws IDSException;
	
	/**
	 * 扫描二维码
	 * @param signPageKey
	 * @return
	 */
	String scanQrcode(String signPageKey);

	/**
	 * 签章请求
	 * @param signPageKey
	 * @return
	 */
	IResult stampRequest(String signPageKey);
	
	
}
