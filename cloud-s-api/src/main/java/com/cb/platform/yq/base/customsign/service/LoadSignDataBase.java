package com.cb.platform.yq.base.customsign.service;

import com.cb.platform.yq.base.customsign.entity.PdfImageParam;
import com.cb.platform.yq.base.customsign.entity.SignFileInfo;
import com.cb.platform.yq.base.customsign.entity.Stamp;
import com.cb.platform.yq.base.customsign.entity.StampRequest;
import com.cb.platform.yq.base.filepath.constant.SystemProperties;
import com.ceba.base.exception.IDSException;
import com.ceba.base.web.response.IResult;
import org.apache.commons.fileupload.FileItem;

import java.util.List;

/**
 * 加载签章数据接口
 */
public interface LoadSignDataBase {
    static String FILE_STATUS= SystemProperties._uploadFileType;//上传文件格式限制
    static int UPLOAD_FILE_NMAE_SIZE=180;//上传文件字数大小限制
    static int SYSTEM_CHATR_LENGTH=SystemProperties._uploadFileNameSize;
    static String PDF_EXT="pdf";
    /**
     * 校验文件是否合格
     * @param fileItem
     * @return
     */
    IResult validFile(FileItem fileItem)throws IDSException;
    /**
     * 浏览器上传新文件
     * @param fileItem
     * @return
     */
    SignFileInfo uploadNewFile(FileItem fileItem)throws IDSException;

    /**
     * 获取PDF文件信息
     * @param path
     * @return
     * @throws IDSException
     */
    SignFileInfo getPdfFileInfo(String path)throws IDSException;
    /**
     * 获取签章请求
     * @param stampRequest
     * @return
     * @throws IDSException
     */
    List<Stamp> tackStamp(StampRequest stampRequest)throws IDSException;

    /**
     * 加载文件图片
     * @param signFileInfo
     * @return
     * @throws Exception
     */
    PdfImageParam loadFileImage(SignFileInfo signFileInfo)throws IDSException;
}
