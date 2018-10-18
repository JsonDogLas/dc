
package com.cb.platform.yq.api.sign.impl;

import com.cb.platform.yq.base.customsign.entity.HandWriteSignRequest;
import com.cb.platform.yq.base.customsign.service.WorkflowHandWriteScriptSignLogicService;
import com.ceba.base.exception.IDSException;
import com.ceba.base.web.response.IResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 云签手写体业务处理
 * @author Administrator
 *
 */
@Service
public class CloudSignHandWriteScriptSignLogicService {
	private static Logger logger = LoggerFactory.getLogger(CloudSignHandWriteScriptSignLogicService.class);
	@Autowired
	public WorkflowHandWriteScriptSignLogicService workflowHandWriteScriptSignLogicService;


	/**
	 * 手写请求-扫描二维码
	 * @param request
	 * @return
	 */
	public IResult handWriteRequest(HandWriteSignRequest request) {
		return workflowHandWriteScriptSignLogicService.handWriteRequest(request);
	}
	/**
	 * 上传base64
	 * @param request
	 * @return
	 */
	public IResult handWriteUpload(HandWriteSignRequest request) {
		return workflowHandWriteScriptSignLogicService.handWriteUpload(request);
	}
	/**
	 * 关闭手写页面
	 * @param request
	 * @return
	 */
	public IResult handWriteClose(HandWriteSignRequest request) {
		return workflowHandWriteScriptSignLogicService.handWriteClose(request);
	}
	/**
	 * 签章请求
	 * @param request
	 * @return
	 */
	public IResult stampRequest(HandWriteSignRequest request) {
		return workflowHandWriteScriptSignLogicService.stampRequest(request);
	}
	
	/**
	 * 二维码请求
	 * @param request
	 * @return
	 */
	public IResult qrcodeRequest(HandWriteSignRequest request)throws IDSException {
		return workflowHandWriteScriptSignLogicService.qrcodeRequest(request);
	}
	
	/**
	 * 保存stamp
	 * @param request
	 * @return
	 */
	public IResult saveStamp(HandWriteSignRequest request) {
		return workflowHandWriteScriptSignLogicService.saveStamp(request);
	}

}
