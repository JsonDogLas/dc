package com.cb.platform.yq.api.controller.api;

import com.alibaba.fastjson.JSON;
import com.cb.platform.yq.api.base.redis.RedisService;
import com.cb.platform.yq.api.base.redis.prefix.impl.WebUserPrefix;
import com.cb.platform.yq.api.constants.URLConstants;
import com.cb.platform.yq.api.entity.ApiClientInfoDo;
import com.cb.platform.yq.api.sign.SignManager;
import com.cb.platform.yq.api.sign.bean.impl.FileItemAdapter;
import com.cb.platform.yq.api.sign.bean.impl.SignCompleteData;
import com.cb.platform.yq.api.sign.bean.impl.SignGetReadyDataDTO;
import com.cb.platform.yq.api.sign.bean.impl.SignGetReadyDataVO;
import com.cb.platform.yq.api.utils.ID;
import com.cb.platform.yq.api.utils.KeyUtils;
import com.cb.platform.yq.api.utils.ResultUtils;
import com.cb.platform.yq.api.vo.SignDataVO;
import com.cb.platform.yq.base.Result;
import com.cb.platform.yq.base.customsign.enums.ResultStatusEnum;
import com.cb.platform.yq.base.customsign.service.DefaultLoadSignData;
import com.cb.platform.yq.base.customsign.service.LoadSignDataBase;
import com.cb.platform.yq.base.exception.ApiErrorEnum;
import com.cb.platform.yq.base.exception.ApiTipEnum;
import com.cb.platform.yq.base.filepath.constant.SystemProperties;
import com.cb.platform.yq.base.filepath.enums.FileVisitEnum;
import com.cb.platform.yq.base.filepath.enums.YqFilePathEnum;
import com.ceba.base.utils.IDSFileUtils;
import com.ceba.base.web.response.IResult;
import io.swagger.annotations.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 吴海华
 * @serial config 通信接口
 *
 * @Controller（可以返回字符串或页面） 和 @RestController(只能返回字符串) 区别
 */
@Api(value = "签名相关接口",tags = "2.签名接口集合",position = 1)
@Controller
@RequestMapping("/sign")
public class SignController {
    public static Logger logger = LoggerFactory.getLogger(SignController.class);
    @Autowired
    private RedisService redisService;

    @Autowired
    private SignManager signManager;

    @ApiOperation(value = "1.上传文件接口",notes = "访问需要带access_token")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "keyId", value = "keyId", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "keyValidInfo", value = "key身份验证信息", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "authority", value = "签名页面权限，多个用 英文逗号 隔开 不传默认全部开启", required = false, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "pageTitle", value = "页面标题 默认为文件名（没有文件后缀）", required = false, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "dateStampZoom", value = "日期章缩放比率 默认为1 缩放比率 1=75px", required = false, dataType = "Float"),
            @ApiImplicitParam(paramType="query", name = "qrcodeStampZoom", value = "二维码缩放比率 默认为1 缩放比率 1=75px", required = false, dataType = "Float"),
    })
    @ApiResponses({
            @ApiResponse(message="",code=200,response =Result.class)
    })
    @RequestMapping(value = "/uploadFile",method = RequestMethod.POST)
    @ResponseBody
    public Result uploadFile(String keyId,String keyValidInfo,String authority,String pageTitle,Float dateStampZoom ,Float qrcodeStampZoom,
                             @ApiParam(value="上传的文件",required=true) MultipartFile multiUploadFile,
                             Model model, HttpServletRequest httpServletRequest){
        //参数校验
        SignGetReadyDataVO signGetReadyDataVO=new SignGetReadyDataVO(multiUploadFile,keyId,keyValidInfo,authority,pageTitle,dateStampZoom,qrcodeStampZoom);
        Result result=valid(signGetReadyDataVO);
        if(!result.isResult()){
            return result;
        }
        OAuth2Authentication oauth2Authentication=(OAuth2Authentication)SecurityContextHolder.getContext().getAuthentication();
        String apiClientInfoStr=redisService.get(WebUserPrefix.apiClientInfo,ObjectUtils.toString(oauth2Authentication.getPrincipal()),String.class);
        ApiClientInfoDo apiClientInfoDo= JSON.parseObject(apiClientInfoStr,ApiClientInfoDo.class);
        OAuth2AuthenticationDetails oAuth2AuthenticationDetails=(OAuth2AuthenticationDetails)oauth2Authentication.getDetails();
        SignGetReadyDataDTO signGetReadyDataDTO=new SignGetReadyDataDTO(signGetReadyDataVO,oAuth2AuthenticationDetails.getTokenValue(),apiClientInfoDo);
        //取出客户端权限
        Collection<GrantedAuthority> authorityCollection = oauth2Authentication.getAuthorities();
        Map<String,String> map=new HashMap<>();
        StringBuilder builder=new StringBuilder();
        for(GrantedAuthority grantedAuthority : authorityCollection){
            String permission=grantedAuthority.getAuthority();
            builder.append(permission);
            builder.append(",");
            map.put(permission,permission);
        }
        //遍历自定义权限
        if(StringUtils.isNotEmpty(signGetReadyDataVO.getAuthority())){
            builder=new StringBuilder();
            String[] array=signGetReadyDataVO.getAuthority().split(",");
            for(String str : array){
                if(map.get(str) != null){
                    builder.append(str);
                    builder.append(",");
                }
            }
        }
        signGetReadyDataDTO.setAuthority(builder.toString());
        //验证文件的有效性
        LoadSignDataBase loadSignDataBase=new DefaultLoadSignData();
        FileItem fileItem=new FileItemAdapter(signGetReadyDataVO.getMultiUploadFile());
        IResult iResult=loadSignDataBase.validFile(fileItem);
        if(iResult.getResultCode() == ResultStatusEnum.SB.getCode()){
            SystemProperties.log(logger,"上传文件签名，验证文件失败，原因："+iResult.getMessage());
            return new Result(iResult,false);
        }else{
            SystemProperties.log(logger,"上传文件签名，验证文件成功");
        }
        //验证key的是不是ceba的key
        try{
            if(StringUtils.isNotEmpty(signGetReadyDataDTO.keyValidInfo())){
                if(!KeyUtils.isKeyUse(signGetReadyDataDTO.keyValidInfo())){
                    return ResultUtils.failMessageLog(logger, ApiTipEnum.CE_INDEX_CEBA_KEY_ERROR);
                }else{
                    signGetReadyDataDTO.setKeyValidInfo(null);
                    SystemProperties.log(logger,"上传文件签名，验证key成功");
                }
            }else{
                SystemProperties.log(logger,"上传文件签名，没有验证key");
            }
        }catch (Exception e){
            return ResultUtils.exceptionMessageLog(logger,ApiErrorEnum.CE_INDEX_CEBA_KEY_EXCEPTION,e);
        }
        //上传文件
        String filePath= YqFilePathEnum.LOCAL_SERVER_ROOT.getFilePath(FileVisitEnum.ABSOLUTE, ID.getGuid())
                +fileItem.getName();
        try{
            boolean bool= IDSFileUtils.writeToFile(fileItem.getInputStream(), filePath);
            if(bool){
                SystemProperties.log(logger,"上传文件签名，文件读取成功");
            }else{
                return ResultUtils.failMessageLog(logger,ApiTipEnum.CE_INDEX_CEBA_KEY_ERROR);
            }
        } catch (FileNotFoundException e) {
            return ResultUtils.exceptionMessageLog(logger,ApiErrorEnum.SYSTEM_FILE_PATH_NO_FILE_EXCEPTION,e);
        } catch (IOException e) {
            return ResultUtils.exceptionMessageLog(logger,ApiErrorEnum.SYSTEM_FILE_IO_EXCEPTION,e);
        } catch (Exception e) {
            return ResultUtils.exceptionMessageLog(logger,ApiErrorEnum.SYSTEM_FILE_IO_EXCEPTION,e);
        }
        signGetReadyDataDTO.setUploadFilePath(filePath);
        result=signManager.uploadFile(signGetReadyDataDTO);
        if(result.isResult()){
            String signSessionId=ObjectUtils.toString("/sign/page?signSessionId="+result.getData());
            result.setData(signSessionId);
        }
        return result;
    }

    /**
     * 打开签名页面
     * @param signSessionId
     * @param model
     * @return
     */
    @ApiOperation(value = "2.打开签名页面",notes = "访问需要带access_token" )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "signSessionId", value = "签名会话ID", required = true, dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(message="返回签名页面",code=200,response =String.class)
    })
    @RequestMapping(value = "/page",method = RequestMethod.GET)
    public String page(@Param("signSessionId") String signSessionId,Model model,HttpServletRequest request){
        IResult iResult=signManager.loadSignData(signSessionId);
        try {
            model.addAttribute("iResult",URLEncoder.encode(JSON.toJSONString(iResult),"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            ResultUtils.exceptionMessageLog(logger, ApiErrorEnum.THIRD_LOAD_JSON_EXCEPTION,e);
            Map<String,String> map=new HashMap<>();
            map.put("result","false");
            map.put("message",ApiErrorEnum.THIRD_LOAD_JSON_EXCEPTION.getMessage());
            model.addAttribute("iResult",JSON.toJSONString(map));
        }
        HttpSession session = request.getSession();
        session.setAttribute("signSessionId",signSessionId);
        return URLConstants.CLOUDSIGN_CUSTOM_SING_PAGE;
    }

    /**
     * 获取签名后的数据
     * @param signSessionId
     * @return
     */
    @ApiOperation(value = "3.获取签名后的数据",notes = "访问需要带access_token")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "signSessionId", value = "签名会话ID", required = true, dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(message="",code=200,response =SignDataVO.class)
    })
    @RequestMapping(value = "/signData",method = RequestMethod.GET)
    @ResponseBody
    public Result signData(@Param("signSessionId") String signSessionId){
        return signManager.signData(signSessionId);
    }



    private Result valid(SignGetReadyDataVO signGetReadyData){
        if(signGetReadyData.getMultiUploadFile() == null || signGetReadyData.getMultiUploadFile().getSize() <= 0){
            return ResultUtils.failMessageLog(logger,ApiTipEnum.SIGN_FILD_NO_FIND,"上传文件签名，没有发现文件");
        }
        if(org.apache.commons.lang3.StringUtils.isEmpty(signGetReadyData.getKeyValidInfo())){
            return ResultUtils.failMessageLog(logger,ApiTipEnum.KEY_INFO_NO_FIND,"上传文件签名，没有key身份信息");
        }
        if(org.apache.commons.lang3.StringUtils.isEmpty(signGetReadyData.getKeyId())){
            return ResultUtils.failMessageLog(logger,ApiTipEnum.KEY_ID_NO_FIND,"上传文件签名，没有keyId");
        }
        return ResultUtils.success(null);
    }
}
