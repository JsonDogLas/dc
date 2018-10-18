package com.cb.platform.yq.api.base.oauth.server;
import com.cb.platform.yq.api.base.oauth.JedisTokenStore;
import com.cb.platform.yq.api.service.ApiClientInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

import javax.sql.DataSource;

/**
 * //        /oauth/authorize：授权端点。
 //        /oauth/token：令牌端点。
 //        /oauth/confirm_access：用户确认授权提交端点。
 //        /oauth/error：授权服务错误信息端点。
 //        /oauth/check_token：用于资源服务访问的令牌解析端点。
 //        /oauth/token_key：提供公有密匙的端点，如果你使用JWT令牌的话。
 //endpoints.pathMapping()
 * 授权认证服务配置
 *
 *  授权认证服务三点
 *  一.第三方用户客户端详情 client
 *  二.令牌的生成管理 Access Token
 *  三.端点介入 endpoints
 *
 *
 *
 */
//@Configuration
//@EnableAuthorizationServer
public class OAuth2AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {


    @Autowired
    private DataSource dataSource;


    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;


    private LoadClientPermissionService getLoadClientPermissionService(){
        return new LoadClientPermissionService(dataSource);
    }

    /**
     * 用来配置令牌端点（token endpoint） 的安全约束
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //设置密码加密模式
        security.passwordEncoder(new BCryptPasswordEncoder());
        //允许表单认证
        security.allowFormAuthenticationForClients();
    }


    /**
     * 第三方客户端详情 配置
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //设置客户端源头
        clients.withClientDetails(getLoadClientPermissionService());
    }

    /**
     * 来配置授权（authorization）以及令牌（token）的访问端点和令牌服务（token services）
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //设置 令牌 存储层  jedisRadis
        endpoints.tokenStore(new JedisTokenStore(jedisConnectionFactory))
                .authenticationManager(authenticationManager)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
        //设置客户端源头
        endpoints.setClientDetailsService(getLoadClientPermissionService());
        //可以设置 授权url
    }

    public static void main(String[] strs){
        String str=new BCryptPasswordEncoder().encode("111111");
        System.out.println(str);
    }
}
