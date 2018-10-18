package com.cb.platform.yq.api.base.druid;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;


/**
 * druid 配置
 *  @Configuration 注解是用来配置SpringBoot项目的配置注解，
 *  如果将该注解配置在实体类上，该类内的所有bean以及配置都会
 *  应用的全局
 *
 *  在 druidConfiguration 内配置的用户名，密码登录监控平台
 *
 *  druid 模块
 *  数据源
 *      可以看到项目中管理的所有数据源配置的详细情况，处理密码没有显示外其他都在
 *  SQL监控
 *      可以查看所有的执行sql语句
 *  SQL防火墙
 *      druid提供了黑白名单的访问，可以清除的看到slq的防护情况
 *  Web应用
 *      可以看到目前运行的web程序的详细信息
 *  URL监控
 *       可以简单空到所有的请求路径的请求次数，请求时间等其他参数
 *  Session监控
 *       可以看到当前的session状况，创建时间，最后活跃时间请求次数，请求时间等详细数据
 *  JSONAPI监控
 *       通过api的形式访问Druid的监控接口，api接口返回json形式数据
 */
public class DruidConfiguration {

    /**
     * servlet注册
     * @return
     */
    @Bean
    public ServletRegistrationBean druidServlet(){
        //创建servlet实体类
        //192.168.0.131:6060/druid/login.html
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
        servletRegistrationBean.setServlet(new StatViewServlet());
        servletRegistrationBean.addUrlMappings("/druid/*");

        //设置ip白名单
        servletRegistrationBean.addInitParameter("allow","192.168.0.131,192.168.0.121");
        //设置ip黑名单，如果allow与derry共同存在是，derry优先于allow
        servletRegistrationBean.addInitParameter("derry","192.168.0.19");
        //设置控制台管理用户
        servletRegistrationBean.addInitParameter("loginUsername","ceba");
        servletRegistrationBean.addInitParameter("loginPassword","111111");
        //是否可以重置数据
        servletRegistrationBean.addInitParameter("resetEnable","false");
        return servletRegistrationBean;
    }

    /**
     * 创建过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean statFilter(){
        //创建过滤器
        FilterRegistrationBean filterRegistrationBean= new FilterRegistrationBean(new WebStatFilter());
        //设置过滤器过滤路径
        filterRegistrationBean.addUrlPatterns("/*");
        //忽略过滤的形式
        filterRegistrationBean.addInitParameter("exclusions","*.js,*.gif,*.jsp,*.png,*.css,*.ico,/druid/*");
        return  filterRegistrationBean;
    }

}
