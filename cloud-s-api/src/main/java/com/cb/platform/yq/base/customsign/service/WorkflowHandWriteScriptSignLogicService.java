
package com.cb.platform.yq.base.customsign.service;

import com.cb.platform.yq.api.sign.SignContextManager;
import com.cb.platform.yq.api.sign.bean.impl.SignContext;
import com.cb.platform.yq.base.customsign.cache.CreateStampCache;
import com.cb.platform.yq.base.customsign.entity.CreateStamp;
import com.cb.platform.yq.base.customsign.entity.HandWriteSignRequest;
import com.cb.platform.yq.base.customsign.enums.IntlEnum;
import com.cb.platform.yq.base.customsign.enums.ResultStatusEnum;
import com.cb.platform.yq.base.customsign.service.createstamp.HandWriteSignBase;
import com.cb.platform.yq.base.filepath.enums.FileVisitEnum;
import com.cb.platform.yq.base.filepath.service.YqSystemFilePathImpl;
import com.ceba.base.exception.IDSException;
import com.ceba.base.web.response.IResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 手写体业务处理
 * @author Administrator
 *
 */
@Service("workflowHandWriteScriptSignLogicService")
public class WorkflowHandWriteScriptSignLogicService implements HandWriteScriptSignLogicService{
	private static Logger logger = LoggerFactory.getLogger(WorkflowHandWriteScriptSignLogicService.class);

    @Autowired
    private SignContextManager signContextManager;

	@Autowired
	private HandWriteSignBase handWriteSignBase;
	/**
	 * 手写请求-扫描二维码
	 * @param request
	 * @return
	 */
	@Override
	public IResult handWriteRequest(HandWriteSignRequest request) {
		IResult iResult = new IResult();
		String result=handWriteSignBase.scanQrcode(request.getSignPageKey());
		if(StringUtils.isNotBlank(result)){
			iResult.setMessage(result);
			iResult.setResultCode(ResultStatusEnum.SB.getCode());
		}else{
			iResult.setResultCode(ResultStatusEnum.CG.getCode());
		}
		return iResult;
	}
	/**
	 * 上传base64
	 * @param request
	 * @return
	 */
	@Override
	public IResult handWriteUpload(HandWriteSignRequest request) {
		IResult iResult=new IResult();
		String result=handWriteSignBase.saveBase64(request.getBase64(),request.getSignPageKey(),request.getType(),request.getName());
		if(StringUtils.isNotBlank(result)){
			iResult.setMessage(result);
			iResult.setResultCode(ResultStatusEnum.SB.getCode());
		}else{
			iResult.setMessage(IntlEnum.HAND_WRITE_UPLOAD_SUCESS.get());
			//HAND_WRITE_UPLOAD_SUCESS=上传成功
			iResult.setResultCode(ResultStatusEnum.CG.getCode());
		}
		return iResult;
	}
	/**
	 * 关闭手写页面
	 * @param request
	 * @return
	 */
	@Override
	public IResult handWriteClose(HandWriteSignRequest request) {
        SignContext signContext=signContextManager.get(request.getSignPageKey());
        if(signContext == null){
            //WORKFLOW_SIGN_SESSION_FAIL=签名会话丢失
            String message=IntlEnum.WORKFLOW_SIGN_SESSION_FAIL.get();
            logger.info(message);
            IResult iResult=new IResult();
            iResult.setResultCode(ResultStatusEnum.SB.getCode());
            iResult.setMessage(IntlEnum.WORKFLOW_SIGN_SESSION_FAIL.get());
            return iResult;
        }
		CreateStamp createStamp= signContext.getCreateStamp();
		createStamp.setSignPageIsClose(true);
		IResult iResult = new IResult();
		iResult.setResultCode(ResultStatusEnum.CG.getCode());
		return iResult;
	}
	/**
	 * 签章请求
	 * @param request
	 * @return
	 */
	@Override
	public IResult stampRequest(HandWriteSignRequest request) {
		IResult iResult=handWriteSignBase.stampRequest(request.getSignPageKey());
		return iResult;
	}

	/**
	 * 二维码请求
	 * @param request
	 * @return
	 */
	@Override
	public IResult qrcodeRequest(HandWriteSignRequest request)throws IDSException {
		IResult iResult = new IResult();
		try{
			String result=handWriteSignBase.createQrCode(request.getSignPageKey());
			iResult.setData(YqSystemFilePathImpl.getUpfileFilePath().changeFileVisit(FileVisitEnum.ABSOLUTE, FileVisitEnum.BROWSER,result));
			iResult.setResultCode(ResultStatusEnum.CG.getCode());
		}catch(IDSException exception){
			logger.debug(exception.getMessage());
			iResult.setMessage(exception.getResultInfo());
			iResult.setResultCode(ResultStatusEnum.SB.getCode());
			return iResult;
		}

		return iResult;
	}

	/**
	 * 保存stamp
	 * @param request
	 * @return
	 */
	@Override
	public IResult saveStamp(HandWriteSignRequest request) {
		IResult iResult=new IResult();
		String result=handWriteSignBase.saveStamp(request.getSaveStampList(),request.getSignPageKey());
		if(StringUtils.isNotBlank(result)){
			iResult.setMessage(result);
			iResult.setResultCode(ResultStatusEnum.SB.getCode());
		}else{
			iResult.setMessage(IntlEnum.HAND_WRITE_UPLOAD_SUCESS.get());
			//HAND_WRITE_UPLOAD_SUCESS=上传成功
			iResult.setResultCode(ResultStatusEnum.CG.getCode());
		}
		return iResult;
	}

	public HandWriteSignBase getHandWriteSignBase() {
		return handWriteSignBase;
	}

	public void setHandWriteSignBase(HandWriteSignBase handWriteSignBase) {
		this.handWriteSignBase = handWriteSignBase;
	}
}
