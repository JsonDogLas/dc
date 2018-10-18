package com.cb.platform.yq.api.sign.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cb.platform.yq.api.entity.ApiGFileDo;
import com.cb.platform.yq.api.enums.FileTypeEnum;
import com.cb.platform.yq.api.sign.SignDataService;
import com.cb.platform.yq.api.sign.StampTemplate;
import com.cb.platform.yq.api.sign.bean.StampInfo;
import com.cb.platform.yq.api.sign.bean.impl.StampInfoCollection;
import com.cb.platform.yq.api.sign.bean.impl.StampVO;
import com.cb.platform.yq.api.utils.ID;
import com.cb.platform.yq.api.utils.ManagerHttpRequestUtils;
import com.cb.platform.yq.api.utils.ResultUtils;
import com.cb.platform.yq.api.utils.StampUtils;
import com.cb.platform.yq.base.customsign.entity.Stamp;
import com.cb.platform.yq.base.customsign.entity.StampTypeEnum;
import com.cb.platform.yq.base.customsign.enums.StampFromEnum;
import com.cb.platform.yq.base.exception.ApiErrorEnum;
import com.cb.platform.yq.base.filepath.constant.SystemProperties;
import com.cb.platform.yq.base.filepath.enums.FileVisitEnum;
import com.cb.platform.yq.base.filepath.enums.YqFilePathEnum;
import com.cb.platform.yq.base.filepath.service.YqSystemFilePathImpl;
import com.ceba.base.exception.BusinessErrorInfoException;
import com.ceba.base.exception.IDSException;
import com.ceba.base.web.response.IResult;
import com.ceba.cebautils.Base64FileUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * 抽象的模板制造
 */
public abstract class  AbstractStampTemplate implements StampTemplate {
    public static Logger logger = LoggerFactory.getLogger(ThirdPartyStampTemplate.class);

    /**
     * 签章像素 宽高以75 为单位进行缩放
     */
    public static  float STAMP_FLAG=75;

    /**
     * 接口版本
     */
    public static String INTERFACE_VERSION="1";

    /**
     * stamp信息
     * @param stampList
     * @return
     */
    public StampInfo createStampInfo(List<Stamp> stampList){
        StampInfoCollection.StampInfoBuilder stampInfoBuilder =new StampInfoCollection.StampInfoBuilder();
        stampInfoBuilder.addStampList(stampList);
        return stampInfoBuilder.build();
    }

    /**
     * 获取签名资源服务
     * @return
     */
    public abstract SignDataService signDataService();



    /**
     * 创建签章
     * @param filePath 文件路径
     * @param stampZoom 签章缩放比率  以 75为单位
     * @param apiUserId
     * @return
     */
    public Stamp createStamp(String filePath, float stampZoom, String apiUserId, String stampName, StampTypeEnum stampTypeEnum)throws BusinessErrorInfoException{
        ApiGFileDo apiGFileDo=signDataService().createApiGFileDo(filePath,apiUserId, FileTypeEnum.API_STAMP);
        if(apiGFileDo == null){
            throw new BusinessErrorInfoException(ApiErrorEnum.CREATE_FILE_ERROR);
        }
        return createStamp(apiGFileDo,stampZoom,apiUserId,stampName,stampTypeEnum);
    }

    /**
     * 创建签章
     * @param filePath 文件路径
     * @param stampZoom 签章缩放比率  以 75为单位
     * @param apiUserId
     * @return
     */
    public Stamp createStamp(boolean bool,String filePath, float stampZoom, String apiUserId, String stampName, StampTypeEnum stampTypeEnum)throws BusinessErrorInfoException{
        if(bool){
            //根据路径查询出印章
            ApiGFileDo apiGFileDo=signDataService().sreachApiGFileDo(YqSystemFilePathImpl.getUpfileFilePath().
                    changeFileVisit(FileVisitEnum.ABSOLUTE,FileVisitEnum.VIRTUAL,filePath), FileTypeEnum.API_STAMP);
            return createStamp(apiGFileDo,stampZoom,apiUserId,stampName,stampTypeEnum);
        }else{
            return createStamp(filePath,stampZoom,apiUserId,stampName,stampTypeEnum);
        }
    }

    public Stamp createStamp(ApiGFileDo apiGFileDo,float stampZoom,String apiUserId,String stampName,StampTypeEnum stampTypeEnum)throws BusinessErrorInfoException{
        String absolutePath= YqSystemFilePathImpl.getUpfileFilePath().changeFileVisit(FileVisitEnum.VIRTUAL,FileVisitEnum.ABSOLUTE,apiGFileDo.getVirtualPath());
        String browserPath= YqSystemFilePathImpl.getUpfileFilePath().changeFileVisit(FileVisitEnum.VIRTUAL,FileVisitEnum.BROWSER,apiGFileDo.getVirtualPath());

        //TODO 考虑图片大小
        Stamp stamp=new Stamp();
        stamp.setId(apiGFileDo.getId());
        setStampZoom(absolutePath,stampZoom,stamp);
        stamp.setFilePath(browserPath);
        stamp.setStampType(stampTypeEnum.getCode());
        stamp.setStampName(stampName);
        return stamp;
    }

    private void setStampZoom(String absolutePath,float stampZoom,Stamp stamp)throws BusinessErrorInfoException{
        float zoom=stampZoom == 0 ? 1 : stampZoom;
        BufferedImage image= null;
        try {
            image = ImageIO.read(new File(absolutePath));
        } catch (Exception e) {
            throw new BusinessErrorInfoException(ApiErrorEnum.READER_FILE_IO_ERROR);
        }
        float height= NumberUtils.toFloat(ObjectUtils.toString(image.getHeight()));
        float width=NumberUtils.toFloat(ObjectUtils.toString(image.getWidth()));
        if(height >= width){
            width=STAMP_FLAG/height*width;
            height=STAMP_FLAG;
        }else{
            height=STAMP_FLAG/width*height;
            width=STAMP_FLAG;
        }
        stamp.setImgHeight(height * zoom);
        stamp.setImgWidth(width * zoom);
    }


    /**
     * 第三方取得印章
     * @param url
     * @param reqeustParamMap
     * @return
     */
    public StampInfo thirdPartyStamp(String url, Map<Object,Object> reqeustParamMap, String apiUserId, StampFromEnum stampFromEnum)throws BusinessErrorInfoException{
        reqeustParamMap.put("version",INTERFACE_VERSION);
        long startTime=System.currentTimeMillis();
        try {
            String resultStr= ManagerHttpRequestUtils.postJSON(url,JSON.toJSONString(reqeustParamMap));
            if(StringUtils.isNotEmpty(resultStr)){
                JSONObject jsonObject=JSON.parseObject(resultStr);
                if(jsonObject.getBoolean("result")){
                    JSONArray data=jsonObject.getJSONArray("data");
                    if(data == null || data.size() == 0){
                        SystemProperties.log(logger,"没有发现签章");
                        return null;
                    }
                    List<StampVO> stampVOList=dataToStampVO(data);
                    List<Stamp> stampList=stampVoToStamp(stampVOList,apiUserId,stampFromEnum);
                    return createStampInfo(stampList);
                }else{
                    //第三方抛出错误
                    SystemProperties.log(logger,jsonObject.getString("message"));
                    throw new BusinessErrorInfoException(ApiErrorEnum.THIRD_PARTY_STAMP_EXCEPTION);
                }
            }else{
                //调取接口出现错误
                throw new BusinessErrorInfoException(ApiErrorEnum.THIRD_PARTY_STAMP_EXCEPTION);
            }
        } catch (Exception e) {
            SystemProperties.log(logger,"请求签章是报错："+e.getMessage());
            throw new BusinessErrorInfoException(ApiErrorEnum.NO_URL_EXCEPTION);
        }finally {
            timeLog(startTime);
        }
    }

    private List<StampVO> dataToStampVO(JSONArray jsonArray){
        List<StampVO> stampVOList=new ArrayList<>();
        Iterator<Object> iterator = jsonArray.iterator();
        while (iterator.hasNext()) {
            JSONObject stampJSON = (JSONObject) iterator.next();
            stampVOList.add(JSON.toJavaObject(stampJSON,StampVO.class));
        }
        return stampVOList;
    }

    private List<Stamp> stampVoToStamp(List<StampVO> stampVOList,String apiUserId,StampFromEnum stampFromEnum){
        List<Stamp> stampList=new ArrayList<>();
        for(StampVO stampVO:stampVOList){
            stampList.add(stampVoToStamp(stampVO,apiUserId,stampFromEnum));
        }
        return stampList;
    }

    public Stamp stampVoToStamp(StampVO stampVO,String apiUserId,StampFromEnum stampFromEnum){
        if(StringUtils.isEmpty(stampVO.getStampImageBase64() )
                || StringUtils.isEmpty(stampVO.getStampName())
                || StringUtils.isEmpty(stampVO.getStampType())
                || StringUtils.isEmpty(stampVO.getStampImageFormat())){
            throw new BusinessErrorInfoException(ApiErrorEnum.THIRD_STAMP_PARAM_EXCEPTION);
        }
        if(!(StampTypeEnum.SELF.getCode().equals(stampVO.getStampType()) ||
                StampTypeEnum.COMPANY.getCode().equals(stampVO.getStampType()) ||
                StampTypeEnum.QIFENG.getCode().equals(stampVO.getStampType()))){
            throw new BusinessErrorInfoException(ApiErrorEnum.THIRD_STAMP_TYPE_EXCEPTION);
        }
        String filePath= YqFilePathEnum.YZ.getFilePath(FileVisitEnum.ABSOLUTE, ID.getGuid())+ ID.getGuid()+"."+stampVO.getStampImageFormat();
        StampUtils.base64ToImage(stampVO.getStampImageBase64(),filePath);

        //String filePath, float stampZoom, String apiUserId, String stampName, StampTypeEnum stampTypeEnum
        Stamp stamp=createStamp(filePath,stampVO.getStampZoom(),apiUserId,stampVO.getStampName(),StampTypeEnum.getCode(stampVO.getStampType()));
        stamp.setId(stampVO.getId());
        stamp.setStampFromFlag(stampFromEnum.getNumber());
        stamp.setUpdateTime(stampVO.getUpdateTime());
        return stamp;
    }

    /**
     * 第三方第二接口
     * @param url
     * @param reqeustParamMap
     * @param apiUserId
     * @param stampFromEnum
     * @param stampList
     * @return
     * @throws BusinessErrorInfoException
     */
    public StampInfo thirdPartyStamp(String url, Map<Object,Object> reqeustParamMap, String apiUserId, StampFromEnum stampFromEnum,List<Stamp> stampList)throws BusinessErrorInfoException {
        reqeustParamMap.put("version",INTERFACE_VERSION);
        if(CollectionUtils.isEmpty(stampList)){
            return thirdPartyStamp(url,reqeustParamMap,apiUserId,stampFromEnum);
        }else{
            Map<String,Stamp> stampMap=new HashMap<>();
            List<Stamp> noPaltStampList=new ArrayList<>();
            List<Map> listMap=new ArrayList<>();
            //罗列出第三方的章
            for(Stamp stamp:stampList){
                if(stampFromEnum.getNumber().equals(stamp.getStampFromFlag())){
                    Map<String,String> map=new HashMap<>();
                    map.put("id",stamp.getId());
                    map.put("updateTime",stamp.getUpdateTime());
                    listMap.add(map);
                    stampMap.put(stamp.getId(),stamp);
                }
            }
            reqeustParamMap.put("stampList",listMap);
            if(MapUtils.isEmpty(stampMap)){
                return thirdPartyStamp(url,reqeustParamMap,apiUserId,stampFromEnum);
            }
            String resultStr= ManagerHttpRequestUtils.postJSON(url,JSON.toJSONString(reqeustParamMap));
            if(StringUtils.isNotEmpty(resultStr)) {
                JSONObject jsonObject = JSON.parseObject(resultStr);
                if (jsonObject.getBoolean("result")) {
                    JSONArray data=jsonObject.getJSONArray("data");
                    if(data.size() > 0){
                        List<StampVO> stampVOList=dataToStampVO(data);
                        //罗列出更新，新增，删除
                        //1.更新只需要传更新的 部分
                        //2.删除的只需要传id 没有更新时间
                        //3.新增的需要传所有 更新时间 为新增时间
                        for(StampVO stampVO:stampVOList){
                            Stamp stamp=stampMap.get(stampVO.getId());
                            if(stamp == null){
                                //新增
                                stamp=stampVoToStamp(stampVO,apiUserId,stampFromEnum);
                                stampMap.put(stampVO.getId(),stamp);
                            }else{
                                if(StringUtils.isEmpty(stampVO.getUpdateTime())){
                                    //删除
                                    stampMap.remove(stampVO.getId());
                                }else{
                                    //更新
                                    if(StringUtils.isNotEmpty(stampVO.getStampImageBase64())){
                                        //图片更新
                                        stamp=stampVoToStamp(stampVO,apiUserId,stampFromEnum);
                                    }
                                    //签章名称
                                    if(StringUtils.isNotEmpty(stampVO.getStampName())){
                                        stamp.setStampName(stampVO.getStampName());
                                    }
                                    //签章类型
                                    if(StringUtils.isNotEmpty(stampVO.getStampType())){
                                        stamp.setStampType(stampVO.getStampType());
                                    }
                                    //签章缩放
                                    if(stampVO.getStampZoom() != 0){
                                        String absolutePath= YqSystemFilePathImpl.getUpfileFilePath().changeFileVisit(FileVisitEnum.VIRTUAL,FileVisitEnum.ABSOLUTE,stamp.getFilePath());
                                        setStampZoom(absolutePath,stampVO.getStampZoom(),stamp);
                                    }
                                    //时间戳
                                    stamp.setUpdateTime(stampVO.getUpdateTime());
                                    stampMap.put(stampVO.getId(),stamp);
                                }
                            }
                        }
                    }
                }else{
                    //第三方抛出错误
                    SystemProperties.log(logger,jsonObject.getString("message")+" 更新章时错误"+ApiErrorEnum.THIRD_PARTY_STAMP_EXCEPTION.getMessage());
                }
                noPaltStampList.addAll(stampMap.values());
            }
            return createStampInfo(noPaltStampList);
        }
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public IResult call() throws Exception {
        StampInfo stampInfo= tackStamp();
        IResult iResult=new IResult(true);
        iResult.setData(stampInfo);
        return iResult;
    }

    private void timeLog(long startTime){
        long endTime=System.currentTimeMillis();
        SystemProperties.log(logger,"加载印章开始时间："+startTime+"  结束时间："+endTime+" 耗时："+(endTime - startTime));
    }

}
