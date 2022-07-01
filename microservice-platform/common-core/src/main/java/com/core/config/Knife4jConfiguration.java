package com.core.config;

import com.core.properties.Knife4jProperties;
import com.google.common.base.Predicate;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.annotation.Resource;

@Configuration
public class Knife4jConfiguration {

    Predicate<RequestHandler> predicate = input -> {
        Class<?> declaringClass = input.declaringClass();
        if (declaringClass == BasicErrorController.class)// 排除
            return false;
        if (declaringClass.isAnnotationPresent(RestController.class)) // 被注解的类
            return true;
        return false;
    };

    @Resource
    private Knife4jProperties knife4jProperties;

    @Bean
    public Docket defaultDocket() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                //分组名称
                .groupName(knife4jProperties.getGroupName())
                .select()
                //这里指定Controller扫描包路径
                .apis(predicate)
                .build();
        return docket;
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title(knife4jProperties.getTitle())
                .description(knife4jProperties.getDesc())
                .build();
    }
}

