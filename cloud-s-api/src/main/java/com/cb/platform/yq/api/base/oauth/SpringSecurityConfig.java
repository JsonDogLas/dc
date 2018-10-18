package com.cb.platform.yq.api.base.oauth;

import com.cb.platform.yq.api.service.ApiUserDoService;
import com.cb.platform.yq.api.sign.SignContextManager;
import com.cb.platform.yq.base.filter.FileVisitFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

/**
 * SpringSecurity 认证和授权矿建
 *      认证可以访问系统的用户
 *      授权则是用户可以访问的资源
 *
 * SpringSecurity 配置
 */
@Configuration
@EnableWebSecurity
@ComponentScan(basePackages={"com.ceba.netty.server","com.ceba.netty.pdf"})
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter{
    @Autowired
    private SpringSecurityDruidRequestMatcher springSecurityDruidRequestMatcher;

    @Autowired
    private ApiUserDoService apiUserDoService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //不过滤/druid/
        //http.csrf().disable();//requireCsrfProtectionMatcher(springSecurityDruidRequestMatcher);
        //允许指定将在其上调用的HttpServletRequest实例 HttpSecurity。
        /**/http.requestMatchers().antMatchers("/", "/login","/oauth/authorize","/swagger-ui")
        //确保对我们的应用程序的任何请求都要求用户进行身份验证
            //我们指定了任何用户都可以访问的多种URL模式。具体来说，如果URL以“/login，则任何用户都可以访问请求
            //任何尚未匹配的URL只需要对用户进行身份验证
        .and().authorizeRequests()
                .anyRequest().permitAll()
        //允许用户使用基于表单的登录进行身份验证
        .and().formLogin().loginPage("/login")
        //允许用户使用HTTP基本身份验证进行身份验证
        //.and().exceptionHandling().accessDeniedPage("/login?authorization_error=true")
        .and().csrf().disable().headers().frameOptions().disable().and().httpBasic().disable();
        //.and().httpBasic().disable();
        //headers().disable()解决不能用户IFAME 打开页面


        /*http.
                requestMatchers()
                // /oauth/authorize link org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint
                // 必须登录过的用户才可以进行 oauth2 的授权码申请
                .antMatchers("/", "/home","/login","/oauth/authorize")
                .and()
                .authorizeRequests()
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .and()
                .httpBasic()
                .disable()
                .exceptionHandling()
                .accessDeniedPage("/login?authorization_error=true")
                .and()
                // TODO: put CSRF protection back into this endpoint
                .csrf()
                .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth/authorize"))
                .disable();*/

        /*http
                .requestMatchers()
                .antMatchers("/oauth/**","/login/**","/logout/**")
                .and()
                .authorizeRequests()
                .antMatchers("/oauth/**").authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll();*/
        // /oauth/authorize link org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint
        // 必须登录过的用户才可以进行 oauth2 的授权码申请
        /**/

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //UserDetailsServiceImpl选择用户数据来源  BCryptPasswordEncoder选择一种加密方式
        PasswordEncoder bCryptPasswordEncoder=PasswordEncoderFactories.createDelegatingPasswordEncoder();
        auth.userDetailsService(apiUserDoService).passwordEncoder(bCryptPasswordEncoder);
        //加一种验证方式
        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        auth.authenticationProvider(daoAuthenticationProvider);

    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
