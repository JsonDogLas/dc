package com.cb.platform.yq.api.base.oauth.client;

import com.cb.platform.yq.api.base.oauth.JedisTokenStore;
import com.cb.platform.yq.api.service.ApiUserDoService;
import com.cb.platform.yq.api.sign.SignContextManager;
import com.cb.platform.yq.api.sign.impl.SignAuthenticationProcessingFilter;
import com.cb.platform.yq.base.filter.FileVisitFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

/**
 * 资源服务器 配置
 */
@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Autowired
    private SignContextManager signContextManager;

    @Autowired
    private ApiUserDoService apiUserDoService;

    /**
     * 来配置授权（resource）以及令牌（token）的访问端点和令牌服务（token services）
     * @throws Exception
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("api");//资源id
        //设置 令牌 存储层  jedisRadis
        resources.tokenStore(new JedisTokenStore(jedisConnectionFactory));
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.requestMatchers().antMatchers("/sign/**")
                .antMatchers("/fileVisit/**")
                .antMatchers("/signPage/**");
        //要求访问应用的所有用户都要验证
        http.authorizeRequests().antMatchers("/sign/**").authenticated();
        http.authorizeRequests().antMatchers("/signPage/**").authenticated();
        http.authorizeRequests().antMatchers("/fileVisit/**").permitAll();
        //加载文件过滤器
        //http.addFilterAfter(new FileVisitFilter(), SecurityContextPersistenceFilter.class);
        http.addFilterAfter(new FileVisitFilter(), FilterSecurityInterceptor.class);
        http.addFilterBefore(signCodeAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
        http.csrf().disable().headers().frameOptions().disable().and().httpBasic().disable();

    }

    /**
     * 签名时根据httpSession 更换验证方式  更换为用户密码验证
     * @return
     */
    public SignAuthenticationProcessingFilter signCodeAuthenticationProcessingFilter(){
        SignAuthenticationProcessingFilter signAuthenticationProcessingFilter = new SignAuthenticationProcessingFilter("/signPage/**",signContextManager,apiUserDoService,authenticationManager);
        signAuthenticationProcessingFilter.setContinueChainBeforeSuccessfulAuthentication(true);
        return signAuthenticationProcessingFilter;
    }
}
