package com.cb.platform.yq.api.controller;

import com.alibaba.fastjson.JSON;
import com.cb.platform.yq.api.entity.ApiUserDo;
import com.cb.platform.yq.api.sign.SignContextManager;
import com.cb.platform.yq.api.sign.SignManager;
import com.cb.platform.yq.api.sign.bean.impl.LoadFileImageDTO;
import com.cb.platform.yq.api.sign.bean.impl.SignContext;
import com.cb.platform.yq.api.sign.bean.impl.SignRequestDTO;
import com.cb.platform.yq.api.utils.ID;
import com.cb.platform.yq.api.utils.KeyUtils;
import com.cb.platform.yq.api.utils.ResultUtils;
import com.cb.platform.yq.api.utils.StampUtils;
import com.cb.platform.yq.api.vo.UploadStampVO;
import com.cb.platform.yq.base.Result;
import com.cb.platform.yq.base.customsign.entity.LoadSignDataResponse;
import com.cb.platform.yq.base.customsign.entity.StampsByRuleRequest;
import com.cb.platform.yq.base.customsign.service.SignService;
import com.cb.platform.yq.base.exception.ApiErrorEnum;
import com.cb.platform.yq.base.exception.ApiTipEnum;
import com.cb.platform.yq.base.filepath.constant.SystemProperties;
import com.cb.platform.yq.base.filepath.enums.FileVisitEnum;
import com.cb.platform.yq.base.filepath.enums.YqFilePathEnum;
import com.cb.platform.yq.base.filepath.service.YqSystemFilePathImpl;
import com.ceba.base.web.response.IResult;
import io.swagger.annotations.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author 吴海华
 * @serial config 通信接口
 *
 * @Controller（可以返回字符串或页面） 和 @RestController(只能返回字符串) 区别
 */
@Api(value = "签名相关接口",tags = "签名页面接口集合",hidden = true)
@Controller
@RequestMapping("/signPage")
public class SignPageController {
    public static Logger logger = LoggerFactory.getLogger(SignPageController.class);
    @Autowired
    private SignManager signManager;

    @Autowired
    private SignContextManager signContextManager;

    /**
     * 加载签名页面
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/loadFileImage")
    @ResponseBody
    public IResult loadFileImage(HttpServletRequest request, Model model){
        return signManager.loadFileImage(toloadFileImageRequest(request));
    }

    /**
     * 签名
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/sign",method = RequestMethod.POST)
    @ResponseBody
    public IResult sign(HttpServletRequest request)throws Exception{
        SignRequestDTO signRequest=toSignRequest(request);
        ApiUserDo apiUserDo=(ApiUserDo)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        signRequest.setUserId(apiUserDo.getId());
        String signSessionId=request.getSession().getAttribute("signSessionId").toString();
        if(StringUtils.isEmpty(signRequest.getVirtualSignFileId())){
            signRequest.setVirtualSignFileId(signSessionId);
        }
        return signManager.sign(signRequest);
    }

    /**
     * 判断key是否能使用
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/isKeyUse",method = RequestMethod.POST)
    @ResponseBody
    public Result isKeyUse(@RequestParam("keyInfo") String keyInfo){
        try {
            if(KeyUtils.isKeyUse(keyInfo)){
                return ResultUtils.success(true);
            }else{
                return ResultUtils.failMessageLog(logger,ApiTipEnum.CE_INDEX_CEBA_KEY_ERROR);
            }
        } catch (Exception e) {
            return ResultUtils.exceptionMessageLog(logger,ApiErrorEnum.CE_INDEX_CEBA_KEY_ERROR,e,"验证key异常");
        }
    }

    /**
     * 上传印章
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "上传印章图片",notes = "图片格式必须为png，上传图片和印章时集合关系")
    @ApiResponses({
            @ApiResponse(message="",code=200,response =IResult.class)
    })
    @RequestMapping(value="/uploaStamp",method = RequestMethod.POST)
    @ResponseBody
    public IResult uploadStamp(List<UploadStampVO> uploadStampVOList, HttpServletRequest request){
        String signSessionId=request.getSession().getAttribute("signSessionId").toString();
        return signManager.uploadStamp(signSessionId,uploadStampVOList);
    }

    /**
     * 上传印章图片
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "上传印章图片",notes = "图片格式必须为png")
    @ApiResponses({
            @ApiResponse(message="",code=200,response =Result.class)
    })
    @RequestMapping(value="/uploadStampImage",method = RequestMethod.POST)
    @ResponseBody
    public Result uploadStampImage(@ApiParam(value="上传的印章文件",required=true)MultipartFile multiUploadFile, HttpServletRequest request){
        String path= YqFilePathEnum.YZ.getFilePath(FileVisitEnum.ABSOLUTE, ID.getGuid())+multiUploadFile.getOriginalFilename();
        String fileExt=StampUtils.fileExt(path);
        if(fileExt != null && StringUtils.equals(fileExt.toLowerCase(),"png")){
            File file=new File(path);
            FileOutputStream fileOutputStream= null;
            try {
                fileOutputStream = new FileOutputStream(file);
                IOUtils.copy(multiUploadFile.getInputStream(),fileOutputStream);
            } catch (FileNotFoundException e) {
                return ResultUtils.exceptionMessageLog(logger, ApiErrorEnum.UPLOAD_STAMP_FAIL,e,"上传印章错误，输出文件不存在");
            } catch (IOException e) {
                return ResultUtils.exceptionMessageLog(logger, ApiErrorEnum.UPLOAD_STAMP_FAIL,e,"上传印章错误，读取图片失败");
            }
            path=YqSystemFilePathImpl.getUpfileFilePath().changeFileVisit(FileVisitEnum.ABSOLUTE,FileVisitEnum.BROWSER,path);
            return ResultUtils.successLog(logger,path,"上传印章图片成功");
        }else{
            return ResultUtils.failMessageLog(logger, ApiTipEnum.CREATE_API_USER_ERROR,"印章图片后缀名必须是png");
        }
    }

    /**
     * 取得印章信息
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "请求印章信息接口",notes = "决定印章页面是否弹出申请印章导航")
    @ApiResponses({
            @ApiResponse(message="",code=200,response =Result.class)
    })
    @RequestMapping(value="/stampInfo",method = RequestMethod.POST)
    @ResponseBody
    public Result stampInfo(HttpServletRequest request){
        String signSessionId=request.getSession().getAttribute("signSessionId").toString();
        if(StringUtils.isNotEmpty(signSessionId)){
            SignContext signContext=signContextManager.get(signSessionId);
            if(signContext != null){
                Result result=signContext.getLoadSignDataResult();
                if(result != null && result.getData() instanceof LoadSignDataResponse){
                    LoadSignDataResponse loadSignDataResponse=(LoadSignDataResponse)result.getData();
                    return ResultUtils.success(loadSignDataResponse.getApplyStampFlag());
                }
            }
        }
        return ResultUtils.success(false);
    }


    /**
     * 骑缝章切割
     * @param request
     * @return
     */
    @RequestMapping(value="/stampsByRule",method = RequestMethod.POST)
    @ResponseBody
    public IResult stampsByRule(HttpServletRequest request){
        StampsByRuleRequest stampsByRuleRequest=toStampByRuleRequest(request);
        SignService signService=new SignService();
        return signService.stampsByRule(stampsByRuleRequest);
    }

    private StampsByRuleRequest toStampByRuleRequest(HttpServletRequest request) {
        String data=request.getParameter("data");
        return JSON.parseObject(data, StampsByRuleRequest.class);
    }


    private LoadFileImageDTO toloadFileImageRequest(HttpServletRequest request) {
        String data=request.getParameter("data");
        return JSON.parseObject(data, LoadFileImageDTO.class);
    }

    private SignRequestDTO toSignRequest(HttpServletRequest request) {
        String data=request.getParameter("data");
        return JSON.parseObject(data, SignRequestDTO.class);
    }
}
