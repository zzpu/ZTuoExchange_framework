package cn.ztuo.bitrade.config;

import cn.ztuo.bitrade.ext.OrdinalToEnumConverterFactory;
import cn.ztuo.bitrade.interceptor.MemberInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author GuoShuai
 * @date 2018年02月06日
 */
@Configuration
public class ApplicationConfig implements WebMvcConfigurer {


    /**
     * 国际化
     *
     * @return
     */
    @Bean(name = "messageSource")
    public ResourceBundleMessageSource getMessageSource() {
        ResourceBundleMessageSource resourceBundleMessageSource = new ResourceBundleMessageSource();
        resourceBundleMessageSource.setDefaultEncoding("UTF-8");
        resourceBundleMessageSource.setCacheSeconds(3601);
        resourceBundleMessageSource.setBasenames("i18n/messages", "i18n/ValidationMessages");
        return resourceBundleMessageSource;
    }

    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(getMessageSource());
        return validator;
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/asset/**").addResourceLocations("classpath:/asset/");

    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new OrdinalToEnumConverterFactory());

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MemberInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/register/**", "/mobile/code","/login","/check/login","/start/captcha","/support/country",
                        "/ancillary/**","/announcement/**","/mobile/reset/code","/reset/email/code",
                        "/reset/login/password","/vote/info","/coin/supported","/get/user","/ieo/all");

    }

    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.setAllowCredentials(true);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addExposedHeader("x-auth-token");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }

}
