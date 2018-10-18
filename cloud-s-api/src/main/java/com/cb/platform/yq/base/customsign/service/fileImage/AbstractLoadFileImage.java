package com.cb.platform.yq.base.customsign.service.fileImage;

import com.cb.platform.yq.api.utils.ID;
import com.cb.platform.yq.base.customsign.entity.LoadFileImageReqeust;
import com.cb.platform.yq.base.customsign.entity.PdfImage;
import com.cb.platform.yq.base.customsign.entity.PdfImageParam;
import com.cb.platform.yq.base.customsign.enums.IntlEnum;
import com.ceba.base.exception.IDSException;
import com.ceba.base.utils.ToPdfUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 加载文件图片实现
 * @author Administrator
 *
 */
public abstract class AbstractLoadFileImage implements LoadFileImageBase {
	private static Logger logger = LoggerFactory.getLogger(AbstractLoadFileImage.class);
	/**
	 * 加载文件图片
	 * @param loadFileImageReqeust
	 * @return
	 */
	@Override
	public PdfImageParam loadFileImage(LoadFileImageReqeust loadFileImageReqeust) {
		long startTime=System.currentTimeMillis();
		PdfImageParam pdfImageParam=new PdfImageParam();
		validParam(loadFileImageReqeust, pdfImageParam);
		if(!pdfImageParam.isFlag()){
			return pdfImageParam;
		}
		String reportPath=pdfImageAbsoluteOperatePath(loadFileImageReqeust);
		List<File> fileList=new ArrayList<>();
		boolean isCreateImage=false;//是否需要创建图片
		try {
			if(new File(reportPath).exists()){
				if(loadFileImageReqeust.isNeedNewPage()){//是否需要新的页
					//签名后  第一次 传递删除的页码
					if(loadFileImageReqeust.getDeletePageArrray() != null && loadFileImageReqeust.getDeletePageArrray().length > 0){
						deleteReprotPageFile(reportPath,loadFileImageReqeust.getDeletePageArrray());//删除report下的 指定页码
					}else{
						deleteReprotPageFile(reportPath,loadFileImageReqeust.getPageArray());//删除report下的 指定页码
					}
					if(!fileIdFileExists(reportPath,loadFileImageReqeust.getFileId())){
						deleteReprotNewFile(reportPath);//删除report下最新文件
						createNewFile(reportPath,loadFileImageReqeust.getFileId());	//创建新文件
					}
					isCreateImage=true;
				}else{
					if(!fileIdFileExists(reportPath,loadFileImageReqeust.getFileId())){
						if(loadFileImageReqeust.isDeletePage()){
							deleteReprotFile(reportPath);//删除report下所有文件
							createNewFile(reportPath,loadFileImageReqeust.getFileId());
								//创建新文件
							isCreateImage=true;
						}else{
							deleteReprotNewFile(reportPath);//删除report下最新文件
							createNewFile(reportPath,loadFileImageReqeust.getFileId());	//创建新文件
						}
					}
				}
			}else{
				createNewFile(reportPath,loadFileImageReqeust.getFileId());	//创建新文件
				isCreateImage=true;
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			pdfImageParam.setFlag(false);
			return pdfImageParam;
		}
		//取图片文件
		if(isCreateImage){
			//PDF转图片
			List<Integer> noExistPage=new ArrayList<>();
			for(int index=0;index<loadFileImageReqeust.getPageArray().length;index++){
				noExistPage.add(NumberUtils.toInt(loadFileImageReqeust.getPageArray()[index])-1);
			}
			ToPdfUtils.pdfPagesToPicWithSuffix(loadFileImageReqeust.getFilePath(), reportPath, SCALE, IMAGEFORMAT.getImageFormat(), noExistPage);
			fileList=getPageFile(reportPath,loadFileImageReqeust.getPageArray(),null);
		}else{
			List<Integer> noExistPage=new ArrayList<>();
			fileList=getPageFile(reportPath,loadFileImageReqeust.getPageArray(),noExistPage);
			if(CollectionUtils.isNotEmpty(noExistPage)){
				String[] pageArray=new String[noExistPage.size()];
				int index=0;
				for(Integer page:noExistPage){
					pageArray[index]=ObjectUtils.toString(page+1);
					index++;
				}
				//PDF转图片pdfPagesToPicWithSuffix
				ToPdfUtils.pdfPagesToPicWithSuffix(loadFileImageReqeust.getFilePath(), reportPath, SCALE, IMAGEFORMAT.getImageFormat(), noExistPage);
				fileList.addAll(getPageFile(reportPath,pageArray,noExistPage));
			}
			
		}
		
		//根据文件取图片对象
		List<PdfImage> pdfImageList=takeImagesFromFile(loadFileImageReqeust,reportPath,fileList);
		pdfImageParam.setFlag(true);
		pdfImageParam.setInputFile(loadFileImageReqeust.getFilePath());
		pdfImageParam.setOutPutFile(loadFileImageReqeust.getTempPath());
		pdfImageParam.setPdfImageList(pdfImageList);
		long endTime=System.currentTimeMillis();
		logger.info("接口参数："+loadFileImageReqeust.toString()+"\n PDF转图片成功耗时： "+(startTime-endTime));
		return pdfImageParam;
	}
	
	/**
	 * 验证参数
	 * @param loadFileImageReqeust
	 * @param pdfImageParam
	 */
	public void validParam(LoadFileImageReqeust loadFileImageReqeust, PdfImageParam pdfImageParam){
		pdfImageParam.setFlag(false);
		if(StringUtils.isEmpty(loadFileImageReqeust.getFilePath()) || !new File(loadFileImageReqeust.getFilePath()).exists() ){
			//WORKFLOW_CUSTOMSIGN_VALIDPARAM_PDF_FILE_NO=PDF文件不存在
			pdfImageParam.setMessage(IntlEnum.WORKFLOW_CUSTOMSIGN_VALIDPARAM_PDF_FILE_NO.get());
		}else if(loadFileImageReqeust.getPageArray() == null || loadFileImageReqeust.getPageArray().length <= 0){
			//WORKFLOW_CUSTOMSIGN_VALIDPARAM_PDF_FILE_NO_PAGE=没有页码需要转换
			pdfImageParam.setMessage(IntlEnum.WORKFLOW_CUSTOMSIGN_VALIDPARAM_PDF_FILE_NO_PAGE.get());
		}else{
			long startTime=System.currentTimeMillis();
			int index=loadFileImageReqeust.getFilePath().lastIndexOf(".");
			String name=loadFileImageReqeust.getFilePath().substring(index, loadFileImageReqeust.getFilePath().length());
			if (!StringUtils.equals(name.toUpperCase(), ".PDF")) {
				if( !new File(loadFileImageReqeust.getFileToPdfPath()).exists() ){
					new File(loadFileImageReqeust.getFileToPdfPath()).mkdirs();
				}
				String tempFilePath = loadFileImageReqeust.getFileToPdfPath() + ID.getGuid() + ".pdf";
				boolean isTurn = ToPdfUtils.fileToPdf(loadFileImageReqeust.getFilePath(), tempFilePath);
				long endTime=System.currentTimeMillis();
				if(isTurn){
					loadFileImageReqeust.setFilePath(tempFilePath);
					logger.info("接口参数："+loadFileImageReqeust.getFilePath()+"\n 文件转PDF成功耗时： "+(startTime-endTime));
				}else{
					//未转成功
					pdfImageParam.setFlag(false);
					pdfImageParam.setMessage(IntlEnum.FILE_TO_PDF_FAIL.get());
					logger.info("接口参数："+loadFileImageReqeust.getFilePath()+"\n 文件转PDF失败耗时： "+(startTime-endTime));
					return;
				}
			}
			pdfImageParam.setFlag(true);
		}
	}


	/**
	 * 创建新文件
	 * @param reportPath
	 * @param fileId
	 * @throws IOException
	 */
	private void createNewFile(String reportPath,String fileId) throws IOException{
		if(StringUtils.isNotBlank(reportPath) && StringUtils.isNotBlank(fileId)){
			if(!new File(reportPath).exists()){
				new File(reportPath).mkdirs();
			}
		}
		String newFileIdTxt=reportPath+ NEW_FILE_PREFIX +fileId+".txt";
		if(new File(reportPath).exists()){
			new File(newFileIdTxt).createNewFile();
		}
	}
	/**
	 * 判断 fileId 对应的 文件 是否存在
	 * @param reportPath
	 * @param fileId
	 * @return
	 */
	private boolean fileIdFileExists(String reportPath,String fileId){
		String newFileIdTxt=reportPath+ NEW_FILE_PREFIX +fileId+".txt";
		if(new File(newFileIdTxt).exists()){
			return true;
		}
		return false;
	}

	/**
	 * 删除report下最新文件
	 * @param reportPath
	 * @return
	 */
	private boolean deleteReprotNewFile(String reportPath){
		File fileDir=new File(reportPath);
		if(fileDir != null && fileDir.exists()){
			File[] fileArray=fileDir.listFiles();
			if(fileArray != null && fileArray.length > 0){
				for(int index=0;index < fileArray.length;index++){
					File file=fileArray[index];
					if(file.getName().indexOf(NEW_FILE_PREFIX) > -1){
						file.delete();
						return true;
					}
				}
			}
		}
		return true;
	}


	/**
	 * 删除report下所有文件
	 * @param reportPath
	 * @return
	 */
	private boolean deleteReprotFile(String reportPath){
		File fileDir=new File(reportPath);
		if(fileDir != null && fileDir.exists()){
			File[] fileArray=fileDir.listFiles();
			if(fileArray != null && fileArray.length > 0){
				for(int index=0;index < fileArray.length;index++){
					File file=fileArray[index];
					file.delete();
				}
			}
		}
		return true;
	}


	/**
	 * 删除report下的 指定页码
	 * @param reportPath
	 * @return
	 */
	private void deleteReprotPageFile(String reportPath,String[] pageArray){
		Map<String,File> fileMap=new HashMap<>();
		File fileDir=new File(reportPath);
		if(fileDir != null && fileDir.exists()){
			File[] fileArray=fileDir.listFiles();
			if(fileArray != null && fileArray.length > 0){
				for(int index=0;index < fileArray.length;index++){
					File file=fileArray[index];
					String fileName=file.getName();
					if(fileName.indexOf(NEW_FILE_PREFIX) > -1){
						continue;
					}
					fileName=StringUtils.split(fileName,"_")[0];
					fileMap.put(ObjectUtils.toString(NumberUtils.toInt(fileName)+1), file);
				}
			}
		}

		for(int index=0;index < pageArray.length;index++){
			String page=pageArray[index];
			File file=fileMap.get(page);
			if(file != null){
				file.delete();
			}
		}
	}

	/**
	 * 删除report下的 指定页码
	 * @param reportPath
	 * @return
	 */
	private void deleteAllPageFile(String reportPath){
		File fileDir=new File(reportPath);
		if(fileDir != null && fileDir.exists()){
			File[] fileArray=fileDir.listFiles();
			if(fileArray != null && fileArray.length > 0){
				for(int index=0;index < fileArray.length;index++){
					File file=fileArray[index];
					String fileName=file.getName();
					if(fileName.indexOf(NEW_FILE_PREFIX) > -1){
						continue;
					}else{
						file.delete();

					}
				}
			}
		}
	}
	/**
	 * 删除report下的 所有 页码图片
	 * @param reportPath
	 * @return
	 */
	private void deleteReprotPageFile(String reportPath){
		File fileDir=new File(reportPath);
		if(fileDir != null && fileDir.exists()){
			File[] fileArray=fileDir.listFiles();
			if(fileArray != null && fileArray.length > 0){
				for(int index=0;index < fileArray.length;index++){
					File file=fileArray[index];
					file.delete();
				}
			}
		}
	}

	/**
	 * 删除report下的 指定页码
	 * @param reportPath
	 * @return
	 */
	private List<File> getPageFile(String reportPath,String[] pageArray,List<Integer> noExistPage){
		Map<String,File> fileMap=new HashMap<>();
		List<File> existfileList=new ArrayList<File>();
		File fileDir=new File(reportPath);
		if(fileDir != null && fileDir.exists()){
			File[] fileArray=fileDir.listFiles();
			if(fileArray != null && fileArray.length > 0){
				for(int index=0;index < fileArray.length;index++){
					File file=fileArray[index];
					String fileName=file.getName();
					if(fileName.indexOf(NEW_FILE_PREFIX) > -1){
						continue;
					}
					fileName=StringUtils.split(fileName,"_")[0];
					fileMap.put(ObjectUtils.toString(NumberUtils.toInt(fileName)+1), file);
				}
			}
		}

		for(int index=0;index < pageArray.length;index++){
			String page=pageArray[index];
			File file=fileMap.get(page);
			if(file != null){
				existfileList.add(file);
			}else{
				if(noExistPage != null){
					noExistPage.add(NumberUtils.toInt(page)-1);
				}
			}
		}
		return existfileList;
	}

	/**
	 * 根据完整的pdf路径取得 pdfImage 数据
	 * @return
	 */
	public  List<PdfImage> takeImagesFromFile(LoadFileImageReqeust loadFileImageReqeust, String reportPath, List<File> listFile) throws IDSException {
		List<PdfImage> pdfImageList = new ArrayList<>();
		String imageFormat = IMAGEFORMAT.getImageFormat();
		for (File file:listFile) {
			logger.debug("签名--pdf文件转图片 "+(reportPath+file.getName()));
			String path = absoluteTobrowserPath(loadFileImageReqeust,reportPath+file.getName());
			logger.debug("签名--pdf浏览器路径 "+path);
			String fileInfo = file.getName().replace("." + imageFormat, "");
			//String pdfName = new File(filePath).getName();
			PdfImage pdfImage = new PdfImage();
			//pdfImage.setFileName(pdfName);
			String[] fileInfoArray=StringUtils.split(fileInfo,"_");
			pdfImage.setWidth(NumberUtils.toFloat(fileInfoArray[1]));
			String page=ObjectUtils.toString(NumberUtils.toInt(fileInfoArray[0])+1);
			pdfImage.setPage(page);
			pdfImage.setHeight(NumberUtils.toFloat(fileInfoArray[2]));
			pdfImage.setZoomRate(SCALE);
			pdfImage.setZoom(true);
			pdfImage.setImagePath(path);
			pdfImage.setId(page);
			pdfImageList.add(pdfImage);
		}
		return pdfImageList;
	}
	/**
	 * 删除报告下 的 文件页码图片
	 */
	@Override
	public PdfImageParam removeFileImage(LoadFileImageReqeust loadFileImageReqeust) {
		String tempPath=loadFileImageReqeust.getTempPath();
		String reportPath=tempPath+"/";
		deleteReprotPageFile(reportPath);
		new File(reportPath).delete();
		PdfImageParam pdfImageParam=new PdfImageParam();
		pdfImageParam.setFlag(true);
		return pdfImageParam;
	}


	/**
	 * 签名成功后需要删除的页码
	 * @param loadFileImageReqeust
	 * @objcetParam reportid,deletePageArrray 需要删除的页码
	 * @return
	 */
	@Override
	public boolean signRemovePageFileImage(LoadFileImageReqeust loadFileImageReqeust) {
		String tempPath=loadFileImageReqeust.getTempPath();
		String reportPath=tempPath+"/";
		if(new File(reportPath).exists()){
			if(loadFileImageReqeust.isDeletePage()){
				deleteAllPageFile(reportPath);
			}else{
				if(loadFileImageReqeust.getDeletePageArrray() != null && loadFileImageReqeust.getDeletePageArrray().length > 0){
					deleteReprotPageFile(reportPath,loadFileImageReqeust.getDeletePageArrray());//删除report下的 指定页码
				}
			}
		}
		return true;
	}


	/**
	 * PDF 图片 操作目录的 绝对路径
	 * @param loadFileImageReqeust
	 * @return
	 */
	public abstract String  pdfImageAbsoluteOperatePath(LoadFileImageReqeust loadFileImageReqeust);

	/**
	 * 虚拟路径转绝对路径
	 * @param loadFileImageReqeust
	 * @param filePath
	 * @return
	 */
	public abstract String absoluteTobrowserPath(LoadFileImageReqeust loadFileImageReqeust, String filePath);
}
