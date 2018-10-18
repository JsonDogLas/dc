package com.cb.platform.yq.base.customsign.service;

import com.cb.platform.yq.api.utils.ID;
import com.cb.platform.yq.base.customsign.entity.*;
import com.cb.platform.yq.base.customsign.enums.IntlEnum;
import com.cb.platform.yq.base.customsign.enums.ResultStatusEnum;
import com.cb.platform.yq.base.customsign.service.fileImage.LoadFileImageBase;
import com.cb.platform.yq.base.customsign.service.sign.SignWorkflowBase;
import com.cb.platform.yq.base.filepath.enums.FileVisitEnum;
import com.cb.platform.yq.base.filepath.enums.YqFilePathEnum;
import com.cb.platform.yq.base.filepath.service.YqSystemFilePathImpl;
import com.ceba.base.exception.IDSException;
import com.ceba.base.utils.PDFUtils;
import com.ceba.base.utils.ToPdfUtils;
import com.ceba.base.web.response.IResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SignService {
    static Logger logger = LoggerFactory.getLogger(SignService.class);

    /**
     * 加载pdf图片
     * @param loadFileImage
     * @return
     */
    public IResult loadFileImage(LoadFileImageReqeust loadFileImage,LoadFileImageBase loadFileImageBase) {
        IResult iResult=new IResult();
        iResult.setResultCode(ResultStatusEnum.CG.getCode());
        PdfImageParam pdfImageParam = loadFileImageBase.loadFileImage(loadFileImage);
        if(!pdfImageParam.isFlag()){
            iResult.setResultCode(ResultStatusEnum.SB.getCode());
            iResult.setMessage(pdfImageParam.getMessage());
            return iResult;
        }else{
            iResult.setData(pdfImageParam);
        }

        return iResult;
    }
    /**
     * 签名
     * @param signRequest
     * @return
     */
    public IResult sign(SignRequest signRequest,LoadSignDataBase loadSignDataBase) {
        IResult iResult=new IResult();
        //准备签章
        Map<SignPrintPDF,Stamp> signPrintPdfMap=new HashMap<>();
        if(signRequest.getSignPrintPdfList() !=null &&
                signRequest.getSignPrintPdfList().size() >0) {
            for (SignPrintPDF pdf : signRequest.getSignPrintPdfList()) {
                if (pdf != null) {
                    Stamp stamp = new Stamp();
                    stamp.setId(pdf.getId());
                    stamp.setImgWidth(pdf.getImageWidth());
                    stamp.setImgHeight(pdf.getImageHeight());
                    if (StringUtils.isNotEmpty(pdf.getImagePath())) {
                        String path = YqSystemFilePathImpl.getUpfileFilePath().changeFileVisit(FileVisitEnum.BROWSER,FileVisitEnum.ABSOLUTE,pdf.getImagePath());
                        stamp.setFilePath(path);
                    }
                    stamp.setStampType(pdf.getStampType());
                    stamp.setMatchName("");
                    signPrintPdfMap.put(pdf, stamp);
                }
            }
        }

        //签名
        SignWorkflowBase signWorkflowBase = new SignWorkflowBase(signRequest.getUserSignTypeEnum(),signRequest.getFileId(),signRequest.getCertificationLevelEnum());
        String inFilePath=YqSystemFilePathImpl.getUpfileFilePath().changeFileVisit(FileVisitEnum.VIRTUAL,FileVisitEnum.ABSOLUTE,signRequest.getVirtualPath());
        String outFilePath= YqFilePathEnum.LOCAL_SERVER_ROOT.getFilePath(FileVisitEnum.ABSOLUTE,ID.getGuid())+new File(inFilePath).getName();
        logger.debug("签名——文件输入路径："+inFilePath);
        logger.debug("签名——文件输出路径："+outFilePath);
        try {
            iResult=signWorkflowBase.signProcess(signPrintPdfMap, inFilePath, outFilePath,
                    YqFilePathEnum.LOCAL_SERVER_ROOT.getFilePath(FileVisitEnum.ABSOLUTE, ID.getGuid()),signRequest.getUserSignTypeEnum());
        } catch (Exception e1) {
            return iResult;
        }

        if(iResult.getResultCode() == ResultStatusEnum.SB.getCode()){
            return iResult;
        }

        //获取签名文件信息
        SignFileInfo signFileInfo = loadSignDataBase.getPdfFileInfo(outFilePath);
        signFileInfo.setFileId(signRequest.getFileId());
        iResult.setData(signFileInfo);
        iResult.setResultCode(ResultStatusEnum.CG.getCode());
        return iResult;
    }
    /**
     * 签章分割
     * @param stampsByRuleRequest
     * @return
     */
    public IResult stampsByRule(StampsByRuleRequest stampsByRuleRequest) {
        IResult iResult=new IResult();
        String inFilePath=YqSystemFilePathImpl.getUpfileFilePath().changeFileVisit(FileVisitEnum.VIRTUAL,FileVisitEnum.ABSOLUTE,stampsByRuleRequest.getVirtualPath());
        //文件管理修改--签章分割 （属于一次性文件）
        String tempFilePath=YqFilePathEnum.LOCAL_SERVER_ROOT.getFilePath(FileVisitEnum.ABSOLUTE,ID.getGuid())+ ID.getGuid()+"."+LoadSignDataBase.PDF_EXT;
        //文件管理修改--签章分割 （属于一次性文件）
        inFilePath= ToPdfUtils.toPdfFile(inFilePath,YqFilePathEnum.LOCAL_SERVER_ROOT.getFilePath(FileVisitEnum.ABSOLUTE,ID.getGuid()));
        if(CollectionUtils.isNotEmpty(stampsByRuleRequest.getStampList())){
            List<Stamp> stampList=new ArrayList<>();
            for(Stamp stamp:stampsByRuleRequest.getStampList()){
                JSONObject object=new JSONObject();
                object.put("imgWidth", stamp.getImgWidth());
                object.put("imgHeight", stamp.getImgHeight());
                object.put("positionRate",stamp.getPositionRate());
                object.put("splitWeight", QF_SPLIT_WEIHT);
                String imgPath=YqSystemFilePathImpl.getUpfileFilePath().changeFileVisit(FileVisitEnum.BROWSER,FileVisitEnum.ABSOLUTE,stamp.getFilePath());
                String tempPath=YqFilePathEnum.YZ.getFilePath(FileVisitEnum.ABSOLUTE,ID.getGuid())+new File(imgPath).getName();
                try {
                    org.apache.commons.io.FileUtils.copyFile(new File(imgPath), new File(tempPath));
                } catch (IOException e) {
                    logger.debug(e.getMessage());
                }
                JSONArray list = PDFUtils.getQFStampsByRule(inFilePath,tempFilePath,tempPath,object);
                List<Stamp> _stampList=new ArrayList<>();
                for(int index=0;index<list.length();index++){
                    Stamp _stamp=new Stamp();
                    object=list.getJSONObject(index);
                    _stamp.setImgWidth(object.getLong("imgWidth"));
                    _stamp.setImgHeight(object.getLong("imgHeight"));
                    String filePath=YqSystemFilePathImpl.getUpfileFilePath().changeFileVisit(FileVisitEnum.ABSOLUTE,FileVisitEnum.BROWSER,object.getString("filePath"));
                    _stamp.setFilePath(filePath);
                    _stamp.setStampX(object.getString("stampX"));
                    _stamp.setStampY(object.getString("stampY"));
                    _stamp.setStampPage(object.getString("stampPage"));
                    _stamp.setId(ID.getGuid());
                    _stamp.setStampType(StampTypeEnum.SELF.getCode());
                    _stampList.add(_stamp);
                }
                stampList.addAll(_stampList);
            }
            iResult.setData(stampList);
            iResult.setResultCode(ResultStatusEnum.CG.getCode());
        }else{
            //WORKFLOW_SREACH_FILE_NO_DATA=没有数据!
            iResult.setResultCode(ResultStatusEnum.SB.getCode());
            iResult.setMessage(IntlEnum.WORKFLOW_SREACH_FILE_NO_DATA.get());
            return iResult;
        }
        return iResult;
    }


    private IResult idsExceptionToResult(IDSException idsException){
        IResult iResult=new IResult();
        iResult.setResultCode(ResultStatusEnum.SB.getCode());
        iResult.setMessage(idsException.getResultInfo());
        return iResult;
    }

    private IResult idsExceptionToResult(String message){
        IResult iResult=new IResult();
        iResult.setResultCode(ResultStatusEnum.SB.getCode());
        iResult.setMessage(message);
        return iResult;
    }


    public static String QF_SPLIT_WEIHT="6";
}
