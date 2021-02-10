package cn.ztuo.bitrade.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity //使得SpringMVC集成了Spring Security的web安全支持
@Slf4j
public class ActuatorSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	Environment env;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		String contextPath = env.getProperty("management.server.servlet.context-path");
		if (StringUtils.isEmpty(contextPath)) {
			contextPath = "";
		}
		log.info("contextPath={}",contextPath);
		http.csrf().disable();
		//
		http.authorizeRequests()
				.antMatchers("/**" + contextPath + "/**")
				.authenticated()                    //允许基于使用HttpServletRequest限制访问
				.anyRequest().permitAll()

				.and()				 //连接以上策略的连接器，用来组合安全策略。实际上就是”而且”的意思
				.httpBasic()		  //配置 Http Basic 验证
				;

	}

}