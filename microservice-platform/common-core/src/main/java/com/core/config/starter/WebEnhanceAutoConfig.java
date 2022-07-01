package com.core.config.starter;

import com.core.config.SpringContextHolder;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Servlet;

@Configuration(proxyBeanMethods = false)
@ComponentScan(WebEnhanceAutoConfig.SCAN_PACKAGE)
@ConditionalOnWebApplication(type=ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass({Servlet.class, DispatcherServlet.class, WebMvcConfigurer.class})
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
public class WebEnhanceAutoConfig {
    public static final String SCAN_PACKAGE = "com.autoconfig.web";

    @Bean
    public ServletListenerRegistrationBean<RequestContextListener> servletListenerRegistrationBean(){
        ServletListenerRegistrationBean<RequestContextListener> registrationBean = new ServletListenerRegistrationBean<>();
        registrationBean.setListener(new RequestContextListener());
        return registrationBean;
    }

    @Bean
    public SpringContextHolder springContextHolder(){
        return new SpringContextHolder();
    }
}
