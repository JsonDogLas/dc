package com.cb.platform.yq.base.filter;

import abc.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.cb.platform.yq.api.sign.impl.SessionSignProxyManager;
import com.cb.platform.yq.api.utils.ResultUtils;
import com.cb.platform.yq.base.Result;
import com.cb.platform.yq.base.exception.ApiTipEnum;
import com.cb.platform.yq.base.filepath.constant.SystemPathConstant;
import com.cb.platform.yq.base.filepath.constant.SystemProperties;
import com.cb.platform.yq.base.filepath.enums.FileVisitEnum;
import com.cb.platform.yq.base.filepath.service.YqSystemFilePathImpl;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 文件过滤器 支持下载文件
 * @author whh
 */
public class FileVisitFilter extends GenericFilterBean {
    public static Logger logger = LoggerFactory.getLogger(FileVisitFilter.class);
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest=(HttpServletRequest)servletRequest;
        HttpServletResponse httpServletResponse=(HttpServletResponse)servletResponse;
        String url=httpServletRequest.getRequestURI();
        //有签名会话ID可以访问文件
        String signSessionId=null;
        if(httpServletRequest.getSession() != null){
            Object object= httpServletRequest.getSession().getAttribute("signSessionId");
            if(object != null){
                signSessionId=object.toString();
            }
        }
        //有token 也能访问文件
        SecurityContext securityContext=SecurityContextHolder.getContext();
        Authentication authentication=null;
        if(securityContext != null || signSessionId != null){
            authentication=securityContext.getAuthentication();
            if(authentication instanceof UsernamePasswordAuthenticationToken || authentication instanceof OAuth2Authentication || signSessionId != null){
                if(url.startsWith(SystemProperties.contextPath+ SystemPathConstant.FILE_FILTER_PATH) &&
                        (StringUtils.isNotEmpty(signSessionId) || (authentication != null && authentication.isAuthenticated()))){
                    url=URLDecoder.decode(url,"utf-8");
                    String path=YqSystemFilePathImpl.getUpfileFilePath().changeFileVisit(FileVisitEnum.BROWSER, FileVisitEnum.ABSOLUTE,url);
                    File file = new File(path);
                    if(!file.exists()){
                        String message="访问的文件不存在:"+url;
                        if(StringUtils.isNotEmpty(signSessionId)){
                            message="[签名会话"+signSessionId+"]"+message;
                        }
                        Result result=ResultUtils.failMessageLog(logger, ApiTipEnum.WORKFLOW_NO_FILE_FAIL,message);
                        httpServletResponse.getWriter().print(JSON.toJSONString(result));
                    }else{
                        ServletOutputStream outputStream=httpServletResponse.getOutputStream();
                        FileInputStream fileInputStream=new FileInputStream(file);
                        Path pathType = Paths.get(path);
                        httpServletResponse.addHeader("Content-Length", "" + file.length());
                        httpServletResponse.addHeader("content-type", "" +  Files.probeContentType(pathType));
                        IOUtils.copy(fileInputStream, outputStream);
                    }
                }else{
                    filterChain.doFilter(servletRequest,servletResponse);
                }
            }else{
                filterChain.doFilter(servletRequest,servletResponse);
            }
        }

    }
}
