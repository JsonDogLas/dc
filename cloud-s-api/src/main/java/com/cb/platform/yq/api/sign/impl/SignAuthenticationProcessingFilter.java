package com.cb.platform.yq.api.sign.impl;

import com.cb.platform.yq.api.dto.ApiUserDto;
import com.cb.platform.yq.api.entity.ApiUserDo;
import com.cb.platform.yq.api.service.ApiUserDoService;
import com.cb.platform.yq.api.sign.SignContextManager;
import com.cb.platform.yq.api.sign.bean.SignGetReadyData;
import com.cb.platform.yq.api.sign.bean.impl.SignContext;
import com.cb.platform.yq.base.filepath.constant.SystemProperties;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 签名页面请求时  更换 认证方式
 * @author whh
 */
public class SignAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    public static Logger logger = LoggerFactory.getLogger(SignAuthenticationProcessingFilter.class);
    private SignContextManager signContextManager;

    private ApiUserDoService apiUserDoService;

    private AuthenticationManager authenticationManager;

    public SignAuthenticationProcessingFilter(String defaultFilterProcessesUrl,SignContextManager signContextManager
            ,ApiUserDoService apiUserDoService,AuthenticationManager authenticationManager) {
        super(defaultFilterProcessesUrl);
        this.signContextManager=signContextManager;
        this.apiUserDoService=apiUserDoService;
        this.authenticationManager=authenticationManager;
    }

    protected SignAuthenticationProcessingFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        HttpSession httpSession=httpServletRequest.getSession();
        if(httpSession != null){
            String signSessionId= (String) httpSession.getAttribute("signSessionId");
            SystemProperties.log(logger,"signSessionId["+signSessionId+"] sessionId["+httpSession.getId()+"]");
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=null;
            if(StringUtils.isNotEmpty(signSessionId)){
                SystemProperties.log(logger,"发现签名会话Id【"+signSessionId+"】");
                SignContext signContext=signContextManager.get(signSessionId);
                if(signContext != null){
                    SystemProperties.log(logger,"添加认证方式【"+signSessionId+"】");
                    SignGetReadyData signGetReadyData=signContext.getSignGetReadyData();
                    ApiUserDo apiUserDo=apiUserDoService.sreachApiUser(signGetReadyData.clientId(),signGetReadyData.keyId());
                    String keyId = signGetReadyData.keyId();//PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(signGetReadyData.keyId());
                    usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(apiUserDo.getId(),keyId);
                    usernamePasswordAuthenticationToken.setDetails(new ApiUserDto(apiUserDo));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }else{
                usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken("","");
            }
            return usernamePasswordAuthenticationToken;// this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        }else{
            return null;
        }
    }
}
