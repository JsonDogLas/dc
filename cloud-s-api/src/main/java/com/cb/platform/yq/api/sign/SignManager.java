package com.cb.platform.yq.api.sign;


import com.cb.platform.yq.api.sign.bean.impl.SignRequestDTO;
import com.cb.platform.yq.api.vo.UploadStampVO;
import com.cb.platform.yq.base.customsign.entity.LoadFileImageReqeust;
import com.ceba.base.web.response.IResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 签名管理
 * @author whh
 */
public interface SignManager extends SignApi{
    /**
     * 签名
     * @param signRequestDTO
     * @return
     */
    IResult sign(SignRequestDTO signRequestDTO);


    /**
     * 加载签名数据
     * @param signSessionId
     * @return
     */
    IResult loadSignData(String signSessionId);


    /**
     * 加载签名文件图片
     * @param loadFileImageReqeust
     * @return
     */
    IResult loadFileImage(LoadFileImageReqeust loadFileImageReqeust);

    /**
     * 上传印章
     * @param signSessionId
     * @param uploadStampVOList
     * @return
     */
    IResult uploadStamp(String signSessionId,List<UploadStampVO> uploadStampVOList);
}
