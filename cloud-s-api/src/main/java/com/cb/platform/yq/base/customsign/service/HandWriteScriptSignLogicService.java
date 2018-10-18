package com.cb.platform.yq.base.customsign.service;

import com.cb.platform.yq.base.customsign.entity.HandWriteSignRequest;
import com.ceba.base.web.response.IResult;

public interface HandWriteScriptSignLogicService {

    /**
     * 手写请求-扫描二维码
     * @param request
     * @return
     */
    IResult handWriteRequest(HandWriteSignRequest request);
    /**
     * 上传base64
     * @param request
     * @return
     */
    IResult handWriteUpload(HandWriteSignRequest request);
    /**
     * 关闭手写页面
     * @param request
     * @return
     */
    IResult handWriteClose(HandWriteSignRequest request);
    /**
     * 签章请求
     * @param request
     * @return
     */
    IResult stampRequest(HandWriteSignRequest request);
    /**
     * 二维码请求
     * @param request
     * @return
     */
    IResult qrcodeRequest(HandWriteSignRequest request);

    /**
     * 保存stamp
     * @param request
     * @return
     */
    IResult saveStamp(HandWriteSignRequest request);
}
