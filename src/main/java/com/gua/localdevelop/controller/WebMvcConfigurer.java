package com.gua.localdevelop.controller;

import com.gua.localdevelop.controller.interceptor.LoginInterceptor;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import javax.annotation.Resource;

@SpringBootConfiguration
public class WebMvcConfigurer extends WebMvcConfigurationSupport {


    @Resource
    private LoginInterceptor loginInterceptor;


    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        String[] resourcePatterns = {"/static/**","/index.html","/favicon.ico","/assets/**","/error","/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**"};
        registry.addInterceptor(loginInterceptor).order(2).addPathPatterns("/**").excludePathPatterns(resourcePatterns);
    }

}
