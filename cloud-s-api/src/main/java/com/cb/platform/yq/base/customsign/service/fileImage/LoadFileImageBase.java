package com.cb.platform.yq.base.customsign.service.fileImage;

import com.cb.platform.yq.base.customsign.entity.LoadFileImageReqeust;
import com.cb.platform.yq.base.customsign.entity.PdfImageParam;
import com.cb.platform.yq.base.customsign.enums.ImageFormatEnum;

/**
 * 加载文件图片
 * @author whh
 */
public interface LoadFileImageBase {
	public static String NEW_FILE_PREFIX="NEW_FILE_";
	public static float SCALE = 2.5f;
	public static ImageFormatEnum IMAGEFORMAT= ImageFormatEnum.JPG;
	/**
	 * 加载文件图片
	 * @param loadFileImageReqeust
	 * @return
	 */
	PdfImageParam loadFileImage(LoadFileImageReqeust loadFileImageReqeust);
	/**
	 * 删除report 文件夹
	 * @param loadFileImageReqeust
	 * @return
	 */
	PdfImageParam removeFileImage(LoadFileImageReqeust loadFileImageReqeust);
	
	/**
	 * 签名成功后需要删除的页码
	 * @param loadFileImageReqeust
	 * @objcetParam reportid,deletePageArrray 需要删除的页码
	 * @return
	 */
	boolean signRemovePageFileImage(LoadFileImageReqeust loadFileImageReqeust);
}
