package com.cb.platform.yq.api.controller;


import com.cb.platform.yq.api.base.redis.RedisService;
 import com.cb.platform.yq.api.base.redis.prefix.impl.WebUserPrefix;
 import com.cb.platform.yq.base.customsign.enums.IntlEnum;
 import com.cb.platform.yq.base.customsign.enums.ResultStatusEnum;
 import com.cb.platform.yq.base.filepath.constant.SystemProperties;
 import com.cb.platform.yq.base.filepath.enums.FileVisitEnum;
 import com.cb.platform.yq.base.filepath.service.YqSystemFilePathImpl;
 import com.ceba.base.helper.LoggerHelper;
 import com.ceba.base.sign.ClientFile;
 import com.ceba.base.sign.ClientSign;
 import com.ceba.base.web.response.IResult;
 import com.ceba.cebacer.CertificateLevelEnum;
 import com.ceba.cebacer.KeyLoaderType;
 import com.ceba.cebautils.StringUtil;
 import com.ceba.cpdf.bean.PDFParam;
 import com.ceba.netty.clientSign.IdsClientSign;
import io.swagger.annotations.Api;
import org.apache.commons.lang.StringUtils;
 import org.apache.commons.lang.math.NumberUtils;
 import org.json.JSONArray;
 import org.json.JSONObject;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Controller;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.RequestMethod;
 import org.springframework.web.bind.annotation.ResponseBody;

 import javax.servlet.http.HttpServletRequest;
 import java.net.URLDecoder;
 @Api(value = "socket通信接口",hidden = true)
 @Controller
 @RequestMapping("/signPage/signSocket")
 public class SignSocketController {
     private static Logger logger = LoggerFactory.getLogger(SignSocketController.class);
     @Autowired
     private SystemProperties systemProperties;
     @Autowired
     private RedisService redisService;

     /**
      * 保存文件签名需要的数据进签名文件队列
      * @return
      */
     @RequestMapping(value="/saveToSignList",method = RequestMethod.POST)
     @ResponseBody
     public IResult saveToSignList(HttpServletRequest request) {
         //var sendData = {'fileData':responseData,'stampData':stampData,'cerBase64Str':cer,'isSm2':isSm2,'certificationLevel':'0'}
         IResult result = new IResult(true);
         String isSm2 =request.getParameter("isSm2");
         int digestHashType = NumberUtils.toInt(request.getParameter("digestMethod"));
         String stampData = request.getParameter("stampData");
         String fileData = request.getParameter("fileData");

         String certificationLevel = CertificateLevelEnum.XD.getType()+"";
         try {
             String cerInfo = URLDecoder.decode(request.getParameter("cerBase64Str"),"utf-8");
             JSONObject stampDataObject = new JSONObject(stampData);
             JSONObject fileDataObject = new JSONObject(fileData);
             JSONArray g_StampList = fileDataObject.getJSONArray("stampList");

             ClientSign sign = new ClientSign();
             ClientFile file = new ClientFile();
             if(fileDataObject.has("fileId")){
                 file.setId(fileDataObject.getString("fileId"));
                 if(fileDataObject.has("filePath")){
                     file.setFilePath(fileDataObject.getString("filePath"));
                 }
                 if(fileDataObject.has("certificationLevel")){
                     //签名等级
                     certificationLevel = fileDataObject.getString("certificationLevel");
                 }
                 file.setCerInfo(cerInfo);
             }
             if(stampDataObject.has("signatureArrays")){
                 JSONArray stampArray = stampDataObject.getJSONArray("signatureArrays");
                 if(stampArray !=null && stampArray.length() >0){
                     JSONObject lastStamp = null;
                     if(stampArray.length()>=1){
                         Object s = stampArray.get(stampArray.length()-1);
                         if(s == null){
                             lastStamp = stampArray.getJSONObject(stampArray.length()-2);
                         }else{
                             lastStamp = stampArray.getJSONObject(stampArray.length()-1);
                         }
                     }
                     if(lastStamp.has("id")){
                         sign.setId(lastStamp.getString("id"));
                         if(g_StampList !=null && g_StampList.length() >0){
                             for(int i = 0 ; i<g_StampList.length() ; i++){
                                 JSONObject oo =g_StampList.getJSONObject(i);
                                 if(oo.has("id")){
                                     if(sign.getId().equals(oo.getString("id"))){
                                         if(lastStamp.has("imagePath") && !StringUtils.equals(lastStamp.getString("imagePath"), "null")){
                                             String path= YqSystemFilePathImpl.getUpfileFilePath().changeFileVisit(
                                                     FileVisitEnum.BROWSER,FileVisitEnum.ABSOLUTE,lastStamp.getString("imagePath"));
                                             sign.setStampPath(path);
                                         }
                                         if(lastStamp.has("floatImgHeight")){
                                             sign.setImgHeight(lastStamp.getDouble("floatImgHeight")+"");
                                         }
                                         if(lastStamp.has("floatImgWidth")){
                                             sign.setImgWidth(lastStamp.getDouble("floatImgWidth")+"");
                                         }
                                         if(lastStamp.has("pdf_x")){
                                             sign.setStampX(lastStamp.getDouble("pdf_x")+"");
                                         }
                                         if(lastStamp.has("pdf_y")){
                                             sign.setStampY(lastStamp.getDouble("pdf_y")+"");
                                         }
                                         if(lastStamp.has("page")){
                                             sign.setStampPage(lastStamp.getInt("page")+"");
                                             sign.setRectPageNo(lastStamp.getInt("page"));
                                         }
                                         if(lastStamp.has("cerNo")){
                                             sign.setCerNo(lastStamp.getString("cerNo"));
                                         }
                                     }
                                 }
                             }
                         }
                     }

                 }
             }
             PDFParam pdfParam = new PDFParam();
             sign.setEncodeSignType(Integer.valueOf(isSm2));
             if(StringUtil.isEmptyString(certificationLevel) ||
                     (!StringUtil.isEmptyString(certificationLevel) &&
                             CertificateLevelEnum.XD.getType() == Integer.valueOf(certificationLevel))){
                 sign.setCertificationLevel(CertificateLevelEnum.XD.getType());
             }else{
                 //sign.setCertificationLevel(CertificateLevelEnum.YZ3.getType());
                 //获取系统设置的蓝丝带签名等级
                 sign.setCertificationLevel(systemProperties.getCertificateLevel());
             }
             sign.setKeyType(KeyLoaderType.TYPE_CLIENT);
             sign.setHashType(digestHashType);

             ClientSign.packISignToPDFParam(pdfParam, sign,file.getFilePath());
             //存入队列
             IdsClientSign.createCbPdfFrontSignParam(pdfParam,new JSONObject(file));
             result.setData(file.getId());
         } catch (Exception e) {
             result.setResult(false);
             LoggerHelper.error(logger, e, "");
         }
         return result;
     }


     /**
      * 获取服务器端socket通信IP和PORT
      * @return
      */
     @RequestMapping(value="/getSocketIpAndPort",method = RequestMethod.GET)
     @ResponseBody
     public String getSocketIpAndPort() {
         IResult result = new IResult(true);
         try {
             result.set("SERVER_IP", systemProperties.getNettyServerId());
             result.set("SERVER_PORT", systemProperties.getNettyServerPort());
         } catch (Exception e) {
             return idsExceptionToResult(IntlEnum.SIGN_GET_SOCKET_IP_PORT_FALI.get()).toString();
         }
         String resultStr=result.toString();
         return resultStr;
     }


     /**
      * 获取用户是否输入过KEY密码
      * @return
      */
     @RequestMapping(value="/checkUserKeyHasPutIn",method = RequestMethod.GET)
     @ResponseBody
     public IResult checkUserKeyHasPutIn(HttpServletRequest request) {
         IResult result = new IResult(false);
         try {
             String keyId = request.getParameter("keyId");
             /*String userId = request.getSession().getId();
             SystemProperties.log(logger,"获取用户是否输入过key密码 keyId["+keyId+"]  userId["+userId+"]");
             String keyId_=redisService.get(WebUserPrefix.apiKeyInfo,userId,String.class);
             if(keyId_ !=null){
                 if(keyId.equals(keyId_)){
                     result.setResult(true);
                     return result;
                 }
             }*/
             String keyId_=(String)request.getSession().getAttribute("keyId");
             if(StringUtils.equals(keyId,keyId_)){
                 result.setResult(true);
                 return result;
             }
         } catch (Exception e) {
             LoggerHelper.error(logger, e, "");
         }
         return result;
     }

     /**
      * 保存用户key登录
      * @return
      */
     @RequestMapping(value="/saveUserKeyInfo",method = RequestMethod.GET)
     @ResponseBody
     public IResult saveUserKeyInfo(HttpServletRequest request) {
         IResult result = new IResult(false);
         try {
             /*String keyId = request.getParameter("keyId");
             String userId = request.getSession().getId();
             SystemProperties.log(logger,"保存用户key登录 keyId["+keyId+"]  userId["+userId+"]");
             redisService.set(WebUserPrefix.apiKeyInfo,userId,keyId);*/
             request.getSession().setAttribute("keyId",request.getParameter("keyId"));
         } catch (Exception e) {
             LoggerHelper.error(logger, e, "");
         }
         return result;
     }

     /**
      * 清除用户key登录
      * @return
      */
     @RequestMapping(value="/clearUserKeyInfo",method = RequestMethod.GET)
     @ResponseBody
     public IResult clearUserKeyInfo(HttpServletRequest request) {
         IResult result = new IResult(false);
         try {
             String userId = request.getSession().getId();
             redisService.del(WebUserPrefix.apiKeyInfo,userId);
         } catch (Exception e) {
             LoggerHelper.error(logger, e, "");
         }
         return result;
     }

     private IResult idsExceptionToResult(String message){
         IResult iResult=new IResult();
         iResult.setResultCode(ResultStatusEnum.SB.getCode());
         iResult.setMessage(message);
         return iResult;
     }
 }