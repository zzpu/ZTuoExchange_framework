package cn.ztuo.bitrade.config;

import cn.ztuo.bitrade.ext.SmartHttpSessionStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.*;

/**
 * 一个小时过期
 */
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds =3600)
public class HttpSessionConfig {

	 @Bean
	 public HttpSessionIdResolver httpSessionStrategy() {
		 HeaderHttpSessionIdResolver headerSession = new HeaderHttpSessionIdResolver("x-auth-token");
		 CookieHttpSessionIdResolver cookieSession = new CookieHttpSessionIdResolver();
		 return new SmartHttpSessionStrategy(cookieSession, headerSession);
	 }

}
