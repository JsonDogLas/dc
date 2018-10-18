package com.cb.platform.yq.base.customsign.service;

import com.cb.platform.yq.api.utils.ID;
import com.cb.platform.yq.base.customsign.entity.*;
import com.cb.platform.yq.base.customsign.enums.BlueDigitalEnum;
import com.cb.platform.yq.base.customsign.enums.IntlEnum;
import com.cb.platform.yq.base.customsign.enums.ResultStatusEnum;
import com.cb.platform.yq.base.customsign.service.fileImage.LoadFileImageBase;
import com.cb.platform.yq.base.customsign.service.fileImage.impl.DefaultLoadFileImage;
import com.cb.platform.yq.base.filepath.constant.SystemProperties;
import com.cb.platform.yq.base.filepath.enums.FileVisitEnum;
import com.cb.platform.yq.base.filepath.service.YqSystemFilePathImpl;
import com.ceba.base.exception.IDSException;
import com.ceba.base.utils.IDSFileUtils;
import com.ceba.base.utils.PDFUtils;
import com.ceba.base.utils.ToPdfUtils;
import com.ceba.base.verify.VerifyUtils;
import com.ceba.base.verify.entity.VerifyResult;
import com.ceba.base.web.response.IResult;
import com.ceba.cebacer.CertificateLevelEnum;
import com.ceba.cebacer.EncodeSignType;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractLoadSignData implements LoadSignDataBase {

    public static Logger logger = LoggerFactory.getLogger(AbstractLoadSignData.class);


    private int fileMaxSize;//文件大小 （M）

    public AbstractLoadSignData(){
        this.fileMaxSize= SystemProperties._uploadFileSize;
    }
    public AbstractLoadSignData(int fileMaxSize){
        this.fileMaxSize=fileMaxSize;
    }

    /**
     * 校验文件是否合格
     * @param fileItem
     * @return
     */
    @Override
    public IResult validFile(FileItem fileItem) throws IDSException {
        String name=fileItem.getName();
        String ext=name.substring((name.lastIndexOf(".")+1), name.length());
        IResult iResult = new IResult();
        //文件类型
        if(FILE_STATUS.indexOf(ext.toLowerCase()) == -1){
            String message= IntlEnum.WORKFLOW_UPLOAD_SIGN_FILE_TYPE.get().replace("|", FILE_STATUS);
            iResult.setResultCode(ResultStatusEnum.SB.getCode());
            iResult.setMessage(message);
            return iResult;
        }

        //文件名长度
        if(name.getBytes().length > SYSTEM_CHATR_LENGTH){
            //WORKFLOW_MAIN_UPLOAD_FILE=上传的文件，文件名不能超过|个字符
            String message=IntlEnum.WORKFLOW_MAIN_UPLOAD_FILE.get().replace("|", ObjectUtils.toString(SYSTEM_CHATR_LENGTH));
            iResult.setResultCode(ResultStatusEnum.SB.getCode());
            iResult.setMessage(message);
            return iResult;
        }

        //文件大小  IDSConfig.MAX_SIZE
        if(fileItem.getSize() > (fileMaxSize * 1024 * 1024)){
            String message=IntlEnum.WORKFLOW_ACTION_UPLOAD_FILE_SIZE.get()+fileMaxSize+"M";
            iResult.setResultCode(ResultStatusEnum.SB.getCode());
            iResult.setMessage(message);
            return iResult;
        }
        iResult.setResultCode(ResultStatusEnum.CG.getCode());
        return iResult;
    }


    /**
     * 获取PDF文件信息
     * @param path
     * @return
     * @throws IDSException
     */
    @Override
    public SignFileInfo getPdfFileInfo(String path) throws IDSException {
        SignFileInfo signFileInfo=new SignFileInfo();
        //判断文件是否存在
        if(!new File(path).exists()){
            throw toIDSException(null,IntlEnum.FILE_NO_EXISTS);
        }else{
            signFileInfo.setFileName(new File(path).getName());
            signFileInfo.setOriginalPath(path);
        }
        //判断文件是否是PDF
        if(!StringUtils.equals(signFileInfo.getFileExt().toLowerCase(),PDF_EXT)){
            throw toIDSException(null,IntlEnum.FILE_NO_PDF_EXISTS);
        }
        //是否SM2签名
        try {
            boolean hasSm2Signed= PDFUtils.checkFileHasSM2Signed(path);
            signFileInfo.setHasSm2Signed(hasSm2Signed);
        } catch (Exception e) {
            //WORKFLOW_SIGN_CHECK_SM2_SIGN
            throw toIDSException(e,IntlEnum.WORKFLOW_SIGN_CHECK_SM2_SIGN);
        }
        //是否RSA签名
        try {
            boolean hasRSASigned=  PDFUtils.checkFileHasRSASigned(path);
            signFileInfo.setHasRSASigned(hasRSASigned);
        } catch (Exception e) {
            //WORKFLOW_SIGN_CHECK_RSA_SIGN
            throw toIDSException(e,IntlEnum.WORKFLOW_SIGN_CHECK_RSA_SIGN);
        }
        //判断文档是否被篡改
        try {
            VerifyResult verifyResult = VerifyUtils.packSign(signFileInfo.getOriginalPath(),signFileInfo.getHasRSASigned(),signFileInfo.getHasSm2Signed());
            if(verifyResult != null && verifyResult.isModify()){
                signFileInfo.setBlueDigital(BlueDigitalEnum.ERROR);
            }else if(verifyResult != null &&
                    verifyResult.getCertificateLevel() >= CertificateLevelEnum.YZ.getType()){
                signFileInfo.setBlueDigital(BlueDigitalEnum.BLUE);
            }
        } catch (Exception e){
            //WORKFLOW_FILE_IS_CHANGE_ERROR=判断文件是否被篡改失败
            throw toIDSException(e,IntlEnum.WORKFLOW_FILE_IS_CHANGE_ERROR);
        }
        //文件总页数
        int totalPageCount= ToPdfUtils.getPagesOfPdf(path);
        signFileInfo.setTotalPageCount(totalPageCount);
        signFileInfo.setFilePath(signFileInfo.getOriginalPath());
        signFileInfo.setVirtualPath(absolutePathToVirtualPath(signFileInfo.getOriginalPath()));
        return signFileInfo;
    }

    /**
     * 浏览器上传新文件
     * @param fileItem
     * @return
     */
    @Override
    public SignFileInfo uploadNewFile(FileItem fileItem) throws IDSException {
        //获取原文件
        SignFileInfo signFileInfo=new SignFileInfo();
        signFileInfo.setOriginalPath(originalFilePath(fileItem.getName()));
        signFileInfo.setFileName(fileItem.getName());
        logger.debug("签名——上传文件存储路径："+signFileInfo.getOriginalPath());
        try {
            IDSFileUtils.writeToFile(fileItem.getInputStream(), signFileInfo.getOriginalPath());
            //System.out.println("上传完成"+System.currentTimeMillis());
        } catch (Exception e) {
            // 请再次上传文件！WORKFLOW_AGAIN_UPLOAD_FILE
            String message = IntlEnum.WORKFLOW_AGAIN_UPLOAD_FILE.get();
            throw new IDSException(message, e);
        }
        return toPdf(signFileInfo);
    }

    /**
     * 转pdf
     * @param signFileInfo
     * @return
     */
    public SignFileInfo toPdf(SignFileInfo signFileInfo){
        //判断需不需要转pdf
        if(StringUtils.equals(signFileInfo.getFileExt().toLowerCase(),PDF_EXT)){
            SignFileInfo pdfFileInfo=getPdfFileInfo(signFileInfo.getOriginalPath());
            logger.debug("签名——pdf文件属性：");
            signFileInfo.setTotalPageCount(pdfFileInfo.getTotalPageCount());//总页数
            signFileInfo.setFilePath(signFileInfo.getOriginalPath());//原文件
            signFileInfo.setVirtualPath(absolutePathToVirtualPath(signFileInfo.getOriginalPath()));//pdf文件虚拟路径
            logger.debug("签名——上传PDF文件虚拟路径："+signFileInfo.getVirtualPath());
            signFileInfo.setHasSm2Signed(pdfFileInfo.getHasSm2Signed());
            signFileInfo.setHasRSASigned(pdfFileInfo.getHasRSASigned());
            signFileInfo.setBlueDigital(pdfFileInfo.getBlueDigital());
        }else{
            //需要转PDF
            signFileInfo.setFilePath(pdfFilePath(new File(signFileInfo.getOriginalPath()).getName()));
            logger.debug("签名——转pdf："+signFileInfo.getOriginalPath()+" 转pdf路径："+signFileInfo.getFilePath());
            boolean isTurn = ToPdfUtils.fileToPdf(signFileInfo.getOriginalPath(), signFileInfo.getFilePath());
            if(isTurn){
                signFileInfo.setVirtualPath(absolutePathToVirtualPath(signFileInfo.getFilePath()));
                logger.debug("签名——转pdf后虚拟目录："+signFileInfo.getVirtualPath());
            }else{
                //WORKFLOW_FILE_TO_PDF_FAIL=文件转PDF失败
                throw toIDSException(null,IntlEnum.WORKFLOW_FILE_TO_PDF_FAIL);
            }
            int totalPageCount= ToPdfUtils.getPagesOfPdf(signFileInfo.getFilePath());
            signFileInfo.setTotalPageCount(totalPageCount);//总页数
        }
        return signFileInfo;
    }

    /**
     * 获取签章请求
     * @param stampRequest
     * @return
     * @throws IDSException
     */
    @Override
    public List<Stamp> tackStamp(StampRequest stampRequest) throws IDSException {
        List<Stamp> stampList=new ArrayList<>();
        Stamp stamp=createStamp("upfile/stamp/1654.png",StampTypeEnum.COMPANY.getCode());
        stampList.add(stamp);
        stamp=createStamp("upfile/stamp/5387.png",StampTypeEnum.QIFENG.getCode());
        stampList.add(stamp);
        for(Stamp stampItem:stampList){
            String path= YqSystemFilePathImpl.getUpfileFilePath().changeFileVisit(FileVisitEnum.VIRTUAL, FileVisitEnum.BROWSER,stamp.getFilePath());
//                    PathUtils.changeFileVisit(PathUtils.FileVisitEnum.VIRTUAL,PathUtils.FileVisitEnum.BROWSER,stamp.getFilePath());
            stampItem.setFilePath(path);
        }
        return stampList;
    }
    /**
     * 加载文件图片
     * @param signFileInfo
     * @return
     * @throws Exception
     */
    @Override
    public PdfImageParam loadFileImage(SignFileInfo signFileInfo) throws IDSException {
        LoadFileImageReqeust loadFileImageReqeust=new LoadFileImageReqeust();
        loadFileImageReqeust.setFileId(signFileInfo.getFileId());
        loadFileImageReqeust.setFilePath(signFileInfo.getFilePath());
        firstLoadPage(signFileInfo,loadFileImageReqeust);
        loadFileImageReqeust.setIsNeedNewPage(false);
        loadFileImageReqeust.setIsDeletePage(false);
        LoadFileImageBase loadFileImageBase= new DefaultLoadFileImage();
        return loadFileImageBase.loadFileImage(loadFileImageReqeust);
    }

    private IDSException toIDSException(Exception exception, IntlEnum intlEnum){
        IDSException idsException=null;
        if(exception != null){
            idsException=new IDSException(exception.getMessage());
        }else{
            idsException=new IDSException();
        }
        idsException.setResultInfo(intlEnum.get());
        return idsException;
    }

    private Stamp createStamp(String path,String stampType){
        Stamp stamp=new Stamp();
        stamp.setStampName("测试");
        stamp.setId(ID.getGuid());
        stamp.setImgHeight(70);
        stamp.setImgWidth(70);
        stamp.setFilePath(path);
        stamp.setEncodeSignType(EncodeSignType.RSA.getCode());
        stamp.setStampType(stampType);
        return stamp;
    }

    /**
     * 首次加载页码
     * @param signFileInfo
     * @param loadFileImageReqeust
     */
    public void firstLoadPage(SignFileInfo signFileInfo,LoadFileImageReqeust loadFileImageReqeust){
        if(signFileInfo.getTotalPageCount() < 3){
            if(signFileInfo.getTotalPageCount() == 1){
                String[] pageArray={"1"};
                loadFileImageReqeust.setPageArray(pageArray);
            }else{
                String[] pageArray={"1","2"};
                loadFileImageReqeust.setPageArray(pageArray);
            }
        }else{
            String[] pageArray={"1","2","3"};
            loadFileImageReqeust.setPageArray(pageArray);
        }
    }

    /**
     * 根据文件名取得原文件路径
     * @return
     */
    public abstract String originalFilePath(String fileName);
    /**
     * 取得一个pdf 转pdf用
     * @return
     */
    public abstract String pdfFilePath(String fileName);
    /**
     * 绝对路径转虚拟路径
     * @param absolutePath
     * @return
     */
    public abstract String absolutePathToVirtualPath(String absolutePath);


    public static void main(String[] str){
        int count="上海测吧信息技术有限公司公元试下载的正确性上海测吧1111111111111111111111111111111111111111111111111111111111111111111111111111111111信1111111111111111111111111111111111111111111111111111111111息技术有限公上海测吧信息技术有限公司上海测吧信息哪个还侧测吧信息视乎有限改个半死上海测测试测试测试测试测试测测测测.docx".getBytes().length;
        System.out.println(count);
    }
}
