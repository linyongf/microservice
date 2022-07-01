package com.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

/**
 * @Description 网关配置
 * @Author linyf
 * @Date 2022-06-23 15:10
 * @Version 1.0
 */
@Configuration
@Slf4j
public class GatewayConfig {

    /**
     * @Description 限流策略（针对请求路径）
     * @return: org.springframework.cloud.gateway.filter.ratelimit.KeyResolver
     * @Author linyf
     * @Date 2022-06-23 14:50
     */
    @Bean
    public KeyResolver pathKeyResolver() {
        return exchange -> {
            String path = exchange.getRequest().getPath().value();
            String methodValue = exchange.getRequest().getMethodValue();
            String restFulPath = "[" + methodValue + "]" + path;
            return Mono.just(restFulPath);
        };
    }

    /**
     * @Description 消息转换器：由于 spring cloud gateway 基于 webflux, 所以 HttpMessageConverters 不会自动注入:
     *              HttpMessageConvertersAutoConfiguration - @Conditional(NotReactiveWebApplicationCondition.class)
     * @param: converters
     * @return: org.springframework.boot.autoconfigure.http.HttpMessageConverters
     * @Author linyf
     * @Date 2022-06-23 15:11
     */
    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));
    }
}
