package com.cb.platform.yq;

import abc.Config;
import com.cb.platform.yq.api.base.redis.RedisService;
import com.cb.platform.yq.api.sign.SignContextManager;
import com.cb.platform.yq.api.sign.SignDataService;
import com.cb.platform.yq.api.sign.SignManager;
import com.cb.platform.yq.api.sign.StampManager;
import com.cb.platform.yq.api.sign.impl.BaseStampManager;
import com.cb.platform.yq.api.sign.impl.SessionSignProxyManager;
import com.cb.platform.yq.base.filepath.constant.SystemProperties;
import com.ceba.base.web.response.IResult;
import com.ceba.netty.server.config.NettyTCPServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import redis.clients.jedis.Jedis;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.concurrent.*;

@SpringBootApplication
public class SpringbootOauthApplication{
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(SpringbootOauthApplication.class);
		app.run(args);
	}

	@Autowired
	private SignContextManager signContextManager;



	/**
	 * 签名数据服务
	 */
	@Autowired
	private SignDataService signDataService;

	@Bean
	@ConfigurationProperties(prefix = "com.cb.platform.yq.api")
	public SystemProperties connectionSettings(){
		return new SystemProperties();
	}

	@Bean
	public NettyTCPServer nettyTCPServer(){
		try{
			String applicationPath=Config.APPLICATION_PATH;
			String applicationPath1=Config.APPLICATION_PATH;
		}catch (Exception e){

		}
		return new NettyTCPServer();
	}

	/**
	 * 线程池
	 * @return
	 */
	@Bean
	public CompletionService<IResult> completionService(){
		SystemProperties systemProperties=connectionSettings();
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(systemProperties.getCorePoolSize(),
				systemProperties.getMaximumPoolSize(), systemProperties.getKeepAliveTime(), TimeUnit.SECONDS,
				new LinkedBlockingDeque<>(systemProperties.getCapacity()));
		CompletionService<IResult> completionService=new ExecutorCompletionService<IResult>(threadPoolExecutor);
		return completionService;
	}



}
