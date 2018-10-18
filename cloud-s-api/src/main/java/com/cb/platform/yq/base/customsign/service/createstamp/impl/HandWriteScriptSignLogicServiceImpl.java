package com.cb.platform.yq.base.customsign.service.createstamp.impl;

import abc.util.QRCodeUtils2;
import com.cb.platform.yq.api.entity.ApiGFileDo;
import com.cb.platform.yq.api.enums.FileTypeEnum;
import com.cb.platform.yq.api.service.ApiGFileDoService;
import com.cb.platform.yq.api.sign.SignContextManager;
import com.cb.platform.yq.api.sign.SignDataService;
import com.cb.platform.yq.api.sign.bean.impl.SignContext;
import com.cb.platform.yq.api.utils.ID;
import com.cb.platform.yq.base.customsign.cache.CreateStampCache;
import com.cb.platform.yq.base.customsign.entity.CreateStamp;
import com.cb.platform.yq.base.customsign.entity.HandWriteSignResponse;
import com.cb.platform.yq.base.customsign.entity.Stamp;
import com.cb.platform.yq.base.customsign.entity.StampTypeEnum;
import com.cb.platform.yq.base.customsign.enums.IntlEnum;
import com.cb.platform.yq.base.customsign.enums.ResultStatusEnum;
import com.cb.platform.yq.base.customsign.service.createstamp.HandWriteSignBase;
import com.cb.platform.yq.base.filepath.constant.SystemProperties;
import com.cb.platform.yq.base.filepath.enums.FileVisitEnum;
import com.cb.platform.yq.base.filepath.enums.YqFilePathEnum;
import com.cb.platform.yq.base.filepath.service.YqSystemFilePathImpl;
import com.ceba.base.exception.IDSException;
import com.ceba.base.web.response.IResult;
import com.ceba.cebautils.Base64FileUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class HandWriteScriptSignLogicServiceImpl implements HandWriteSignBase {


	@Autowired
	private SignContextManager signContextManager;

	@Autowired
	private SystemProperties systemProperties;

	@Autowired
    private SignDataService signDataService;
	static Logger logger = LoggerFactory.getLogger(HandWriteScriptSignLogicServiceImpl.class);
	public static int UPLOAD_SIGN_TIME_MINUTES=3;//三分钟没有任何上传记录自动关闭


	/**
	 * 保存base64
	 * @param base64 base64
	 * @param signPageKey key
	 * @param type 签章类型
	 * @param name 签章名称
	 * @return 不为null 代表保存出错  为null代表保存成功
	 */
	@Override
	public String saveBase64(String base64, String signPageKey,String type,String name) {
		if(StringUtils.isEmpty(base64)){
			//WORKFLOW_UPLOAD_IMAGE_EMPTY=上传图片为空
			String message= IntlEnum.WORKFLOW_UPLOAD_IMAGE_EMPTY.get();
			logger.info(message);
			return message;
		}
		if(StringUtils.isEmpty(signPageKey)){
			//WORKFLOW_SAVE_IMAGE_FLAIE=保存图片失败
			String message=IntlEnum.WORKFLOW_SAVE_IMAGE_FLAIE.get();
			logger.info(message);
			return message;
		}

		SignContext signContext=signContextManager.get(signPageKey);
		if(signContext == null){
			//WORKFLOW_SIGN_SESSION_FAIL=签名会话丢失
			String message=IntlEnum.WORKFLOW_SIGN_SESSION_FAIL.get();
			logger.info(message);
			return message;
		}
		CreateStamp createStamp= signContext.getCreateStamp();
		
		if(createStamp.getSignPageIsClose() != null && createStamp.getSignPageIsClose()){
			//WORKFLOW_SIGN_PAGE_CLOSE=签名页面已经关闭
			String message=IntlEnum.WORKFLOW_SIGN_PAGE_CLOSE.get();
			logger.info(message);
			return message;
		}
		
		if(createStamp.getUploadBaseIsClose() != null && createStamp.getUploadBaseIsClose()){
			//WORKFLOW_UPLOAD_PAGE_CLOSE=上传页面已经关闭
			String message=IntlEnum.WORKFLOW_UPLOAD_PAGE_CLOSE.get();
			logger.info(message);
			return message;
		}

		if(MapUtils.isEmpty(createStamp.getBaseMap())){
			createStamp.setBaseMap(new HashMap<>());
		}
		
		if(StringUtils.isNotEmpty(createStamp.getBaseMap().get(base64))){
			//WORKFLOW_HAND_WRITE_SIGN_IMAGE_AGAIN=请不要重复上传图片
			String message=IntlEnum.WORKFLOW_HAND_WRITE_SIGN_IMAGE_AGAIN.get();
			logger.info(message);
			return message;
		}
		//虚拟路径存储图片
		String imgFilePath= YqFilePathEnum.YZ.getFilePath(FileVisitEnum.ABSOLUTE, ID.getGuid())+ID.getGuid()+".png";
		/*int totalStamp=0;
		if(CollectionUtils.isNotEmpty(createStamp.getResultStampList()) && CollectionUtils.isNotEmpty(createStamp.getUploadStampList())){
			totalStamp=createStamp.getResultStampList().size()+createStamp.getUploadStampList().size();
		}else if(CollectionUtils.isNotEmpty(createStamp.getResultStampList())){
			totalStamp=createStamp.getResultStampList().size();
		}else if(CollectionUtils.isNotEmpty(createStamp.getUploadStampList())){
			totalStamp=createStamp.getUploadStampList().size();
		}*/
		Base64FileUtils.string2Image(getBase64PurifyData(base64), imgFilePath);
		Stamp stamp=null;
		try {
			stamp = imagePathToStamp(imgFilePath,name);
            ApiGFileDo apiGFileDo = signDataService.createApiGFileDo(imgFilePath,signContext.getNewApiGFileDo().getCreateUser(), FileTypeEnum.API_STAMP);
            stamp.setId(apiGFileDo.getId());
            stamp.setStampType(type);
		} catch (IOException e) {
			//WORKFLOW_READ_IMAGE_FLAIE=读取图片失败
			String message=IntlEnum.WORKFLOW_READ_IMAGE_FLAIE.get();
			logger.debug(message+":"+e.getMessage());
			return message;
		}
		if(CollectionUtils.isEmpty(createStamp.getResultStampList())){
			createStamp.setUploadStampList(new ArrayList<Stamp>());
		}
		createStamp.getUploadStampList().add(stamp);
		createStamp.getBaseMap().put(base64, stamp.getId());
		createStamp.setNowTime(new Date());
		stamp.setFilePath(YqSystemFilePathImpl.getUpfileFilePath().changeFileVisit(FileVisitEnum.VIRTUAL,FileVisitEnum.BROWSER,stamp.getFilePath()));
		Stamp _stamp=new Stamp();
		BeanUtils.copyProperties(stamp,_stamp);
		_stamp.setImgHeight(_stamp.getImgHeight()/3);
		_stamp.setImgWidth(_stamp.getImgWidth()/3);
		signContext.getStampList().add(_stamp);
		signContextManager.put(signPageKey,signContext);
		return null;
	}
	
	/**
	 * 将图片转换为stamp
	 * @param imgFilePath
	 * @return
	 */
	private Stamp imagePathToStamp(String imgFilePath,String imgName)throws IOException{
		BufferedImage bufferedImage=ImageIO.read(new File(imgFilePath));
		Stamp stamp=new Stamp();
		stamp.setId(ID.getGuid());
		float imgWidth = bufferedImage.getWidth();//图片的宽度
		int imgHeight = bufferedImage.getHeight();//图片的高度
		stamp.setImgHeight(imgHeight);
		stamp.setImgWidth(imgWidth);
		stamp.setFilePath(YqSystemFilePathImpl.getUpfileFilePath().changeFileVisit(FileVisitEnum.ABSOLUTE,FileVisitEnum.VIRTUAL,imgFilePath));
		stamp.setStampType(StampTypeEnum.SELF.getCode());
		stamp.setStampName(imgName);
		stamp.setStampPage("1");
		//读取图片失败
		return stamp;
	}
	/**
	 * 保存saveStamp
	 * @param saveStampList
	 * @param signPageKey
	 */
	@Override
	public String saveStamp(List<String> saveStampList, String signPageKey) {
		SignContext signContext=signContextManager.get(signPageKey);
		if(signContext == null){
			//WORKFLOW_SIGN_SESSION_FAIL=签名会话丢失
			String message=IntlEnum.WORKFLOW_SIGN_SESSION_FAIL.get();
			logger.info(message);
			return message;
		}
		CreateStamp createStamp= signContext.getCreateStamp();
		if(CollectionUtils.isEmpty(createStamp.getResultStampList())){
			return null;
		}
		if(CollectionUtils.isNotEmpty(saveStampList)){
			Map<String, Stamp> stampMap=new HashMap<>();
			for(Stamp stamp:createStamp.getResultStampList()){
				stampMap.put(stamp.getId(), stamp);
			}
		}
		return null;
	}

	@Override
	public String createQrCode(String signPageKey) throws IDSException {
		SignContext signContext=signContextManager.get(signPageKey);
		if(signContext == null){
			//WORKFLOW_SIGN_SESSION_FAIL=签名会话丢失
			String message=IntlEnum.WORKFLOW_SIGN_SESSION_FAIL.get();
			logger.info(message);
			return message;
		}
		CreateStamp createStamp= signContext.getCreateStamp();
		if(CollectionUtils.isEmpty(createStamp.getResultStampList())){
			createStamp=new CreateStamp();
			createStamp.setResultStampList(new ArrayList<>()); 
			createStamp.setUploadStampList(new ArrayList<>()); 
			createStamp.setBaseMap(new HashMap<>());
			createStamp.setSignPageKey(signPageKey);
			createStamp.setUploadTime(UPLOAD_SIGN_TIME_MINUTES);
			createStamp.setStartTime(new Date());
			String context="http://"+ systemProperties.getNettyServerId()+":"+systemProperties.getServerPort()+"/ep/do?HandWriteSignAction=&action=handWriteRequest&signPageKey="+signPageKey;
			String path=YqFilePathEnum.YZ.getFilePath(FileVisitEnum.ABSOLUTE,signPageKey)+ID.getGuid()+".png";
			QRCodeUtils2.encode(context, 240, 240, path);
			createStamp.setQrcodeUrl(path);
			signContext.setCreateStamp(createStamp);
			signContextManager.put(signPageKey,signContext);
			return path;
		}else{
			String path=createStamp.getQrcodeUrl();
			return path;
		}
	}
	/**
	 * 扫描二维码
	 */
	@Override
	public String scanQrcode(String signPageKey) {
		SignContext signContext=signContextManager.get(signPageKey);
		if(signContext == null){
			//WORKFLOW_SIGN_SESSION_FAIL=签名会话丢失
			String message=IntlEnum.WORKFLOW_SIGN_SESSION_FAIL.get();
			logger.info(message);
			return message;
		}
		CreateStamp createStamp= signContext.getCreateStamp();
		if(createStamp == null){
			createStamp=new CreateStamp();
			signContext.setCreateStamp(createStamp);
			signContextManager.put(signPageKey,signContext);
		}
		createStamp.setQrcodeIsClose(true);
		createStamp.setNowTime(new Date());
		createStamp.setStartTime(new Date());
		return null;
	}
	
	
	/**
	 * 签章请求
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public IResult stampRequest(String signPageKey) {
		SignContext signContext=signContextManager.get(signPageKey);
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
		HandWriteSignResponse handWriteSignResponse=new HandWriteSignResponse();
		if(createStamp.getQrcodeIsClose() != null && createStamp.getQrcodeIsClose()){
			handWriteSignResponse.setAction(HandWriteSignResponse.Action.COLSE_QRCODE_PAGE);//关闭二维码页面
			createStamp.setQrcodeIsClose(false);
		}else if(createStamp.getUploadBaseIsClose() != null && createStamp.getUploadBaseIsClose()){//手写签名页面已经关闭，不能在请求签章
			handWriteSignResponse.setAction(HandWriteSignResponse.Action.CLOSE_STAMP_REQUEST);//关闭签章请求
			createStamp.setUploadBaseIsClose(false);
		}else if(CollectionUtils.isNotEmpty(createStamp.getUploadStampList())){
			List<Stamp> stampList=createStamp.getUploadStampList();
			handWriteSignResponse.setStampList(stampList);
			if(CollectionUtils.isEmpty(createStamp.getResultStampList())){
				createStamp.setResultStampList(new ArrayList());
			}
			createStamp.setUploadStampList(new ArrayList<>());
			createStamp.getResultStampList().addAll(stampList);
			handWriteSignResponse.setAction(HandWriteSignResponse.Action.RETURN_SIGN);//接收签章
		}else{
			handWriteSignResponse.setAction(HandWriteSignResponse.Action.NO_OPERA);//关闭签章请求
		}
		IResult iResult=new IResult();
		iResult.setData(handWriteSignResponse);
		iResult.setResultCode(ResultStatusEnum.CG.getCode());
		return iResult;
	}
	
	/**
	 * 取得base64净化数据
	 * @return
	 */
	private String getBase64PurifyData(String base64){
		String[] base64s=StringUtils.split(base64,",");
		if(base64s.length == 2){
			return base64s[1];
		}
		return null;
	}
}
