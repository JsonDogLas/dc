package com.cb.platform.yq.api.controller;

import com.alibaba.fastjson.JSON;
import com.cb.platform.yq.api.constants.URLConstants;
import com.cb.platform.yq.base.customsign.entity.HandWriteSignRequest;
import com.cb.platform.yq.base.customsign.enums.IntlEnum;
import com.cb.platform.yq.base.customsign.enums.ResultStatusEnum;
import com.cb.platform.yq.base.customsign.service.HandWriteScriptSignLogicService;
import com.ceba.base.web.response.IResult;
import io.swagger.annotations.Api;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 手写体签名
 * @author Administrator
 * 
 * 手写签名步骤
 * 2.手机扫描二位码
 * 3.重定向到手写页面
 * 		手写页面，在三分钟之内没有上传任何照片 自动关闭请求 关闭页面
 * 5.手机书写签名，点击上传
 * 1.签名页面 打开二维码
 * 4.签名页面 关闭二维码
 * 6.签名页面 请求手写签章
 * 7.签名页面 保存手写签章
 * 8.签名页面 关闭请求
 * 9.签名页面 激活请求
 * 打开一个签名页面 生成一个 key
 */

/**
 * 手写体
 */
@Api(value = "手写体接口",hidden = true)
@Controller
@RequestMapping("/signPage/handWriteScriptSign")
public class HandWriteScriptSignController {
	@Autowired
	public HandWriteScriptSignLogicService handWriteScriptSignLogicService;

	/**
	 * 查询签名文件列表
	 * @param model
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/to_hand",method = RequestMethod.GET)
	public String tohand(Model model,HttpServletResponse response){
		response.setHeader("X-Frame-Options", "SAMEORIGIN");// 解决IFrame拒绝的问题
		return URLConstants.CLOUDSIGN_CUSTOM_HAND;
	}

	/**
	 * 手写请求
	 * @return
	 */
	@RequestMapping(value="/handWriteRequest",method = RequestMethod.POST)
	@ResponseBody
	public IResult handWriteRequest(HttpServletRequest httpServletRequest) {
		IResult iResult = new IResult();
		HandWriteSignRequest request=toRequest(httpServletRequest);
		if(request == null){
			iResult.setResultCode(ResultStatusEnum.SB.getCode());
			//WORKFLOW_HANDWRITESIGNACTION_FAILE=请求失败
			iResult.setMessage(IntlEnum.WORKFLOW_HANDWRITESIGNACTION_FAILE.get());
			return iResult;
		}else{
			iResult=handWriteScriptSignLogicService.handWriteRequest(request);
			/*if(iResult.getResultCode() == ResultStatusEnum.CG.getCode()){
				HttpServletResponse httpServletResponse=httpContext.getResponse();
				HttpServletRequest httpServletRequest=httpContext.getRequest();
				WebUtils.redirect(httpServletResponse, httpServletRequest.getContextPath()+HAND_WRITE_PAGE_URL);
			}*/
			return iResult;
		}
	}


	/**
	 * base64上传
	 * @return
	 */
	@RequestMapping(value="/handWriteUpload",method = RequestMethod.POST)
	@ResponseBody
	public IResult handWriteUpload(HttpServletRequest httpServletRequest) {
		IResult iResult = new IResult();
		HandWriteSignRequest request=toRequest(httpServletRequest);
		if(request == null){
			iResult.setResultCode(ResultStatusEnum.SB.getCode());
			//WORKFLOW_HANDWRITESIGNACTION_FAILE=请求失败
			iResult.setMessage(IntlEnum.WORKFLOW_HANDWRITESIGNACTION_FAILE.get());
			return iResult;
		}else{
			return handWriteScriptSignLogicService.handWriteUpload(request);
		}		
	}
	/**
	 * 关闭手写页面
	 * @return
	 */
	@RequestMapping(value="/handWriteClose",method = RequestMethod.POST)
	@ResponseBody
	public IResult handWriteClose(HttpServletRequest httpServletRequest) {
		IResult iResult = new IResult();
		HandWriteSignRequest request=toRequest(httpServletRequest);
		if(request == null){
			iResult.setResultCode(ResultStatusEnum.SB.getCode());
			//WORKFLOW_HANDWRITESIGNACTION_FAILE=请求失败
			iResult.setMessage(IntlEnum.WORKFLOW_HANDWRITESIGNACTION_FAILE.get());
			return iResult;
		}else{
			return handWriteScriptSignLogicService.handWriteClose(request);
		}	
	}
	
	
	
	/**
	 * 签章请求
	 * @return
	 */
	@RequestMapping(value="/stampRequest",method = RequestMethod.POST)
	@ResponseBody
	public IResult stampRequest(HttpServletRequest httpServletRequest) {
		IResult iResult = new IResult();
		HandWriteSignRequest request=toRequest(httpServletRequest);
		if(request == null){
			iResult.setResultCode(ResultStatusEnum.SB.getCode());
			//WORKFLOW_HANDWRITESIGNACTION_FAILE=请求失败
			iResult.setMessage(IntlEnum.WORKFLOW_HANDWRITESIGNACTION_FAILE.get());
			return iResult;
		}else{
			return handWriteScriptSignLogicService.stampRequest(request);
		}	
	}
	
	/**
	 * 二维码请求
	 * @return
	 */
	@RequestMapping(value="/qrcodeRequest",method = RequestMethod.POST)
	@ResponseBody
	public IResult qrcodeRequest(HttpServletRequest httpServletRequest) {
		IResult iResult = new IResult();
		HandWriteSignRequest request=toRequest(httpServletRequest);
		if(request == null){
			iResult.setResultCode(ResultStatusEnum.SB.getCode());
			//WORKFLOW_HANDWRITESIGNACTION_FAILE=请求失败
			iResult.setMessage(IntlEnum.WORKFLOW_HANDWRITESIGNACTION_FAILE.get());
			return iResult;
		}else{
			return handWriteScriptSignLogicService.qrcodeRequest(request);
		}	
	}
	
	/**
	 * 保存stamp
	 * @return
	 */
	@RequestMapping(value="/saveStamp",method = RequestMethod.POST)
	@ResponseBody
	public IResult saveStamp(HttpServletRequest httpServletRequest) {
		IResult iResult = new IResult();
		HandWriteSignRequest request=toRequest(httpServletRequest);
		if(request == null){
			iResult.setResultCode(ResultStatusEnum.SB.getCode());
			//WORKFLOW_HANDWRITESIGNACTION_FAILE=请求失败
			iResult.setMessage(IntlEnum.WORKFLOW_HANDWRITESIGNACTION_FAILE.get());
			return iResult;
		}else{
			return handWriteScriptSignLogicService.saveStamp(request);
		}
	}

	private HandWriteSignRequest toRequest(HttpServletRequest request){
		String data=request.getParameter("data");
		if(StringUtils.isNotEmpty(data)){
			return JSON.parseObject(data, HandWriteSignRequest.class);
		}
		return null;
	}
	
	
	@SuppressWarnings("unused")
	private HandWriteSignRequest toRequest(String data){
		if(StringUtils.isNotEmpty(data)){
			return JSON.parseObject(data, HandWriteSignRequest.class);
		}
		return null;
	}


	
}
