package com.cb.platform.yq.api.base.oauth;

import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

/**
 * Spring security 和 Druid 融合
 *
 * 原因分析：如果是使用的是 Java 代码配置 Spring Security，那么 CSRF 保护默认是开启的，
 * 那么在 POST 方式提交表单的时候就必须验证 Token，如果没有，那么自然也就是 403 没权限了。
 *      csrf 默认放行 get 所以 get的可以请求
 *
 * 解决办法放行
 *      /druid/*
 */
@Component
public class SpringSecurityDruidRequestMatcher implements RequestMatcher {
    private Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");
    private RegexRequestMatcher unprotectedMatcher = new RegexRequestMatcher("^/druid/.*", null);

    @Override
    public boolean matches(HttpServletRequest request) {
        if(allowedMethods.matcher(request.getMethod()).matches()){
            return false;
        }
        boolean bool= !unprotectedMatcher.matches(request);
        return bool;
    }
    /*static Pattern pattern = Pattern.compile("/druid/.*");

    public static void main(String[] stra){
        boolean true1=pattern.matcher("/druid/").matches();
        boolean true2=pattern.matcher("/druid/submitLogin").matches();
        System.out.println(true1+"  "+true2);
    }*/
}
