package com.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @Description RestTemplate 配置
 * @Author linyf
 * @Date 2022-06-24 16:11
 */
@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(10_000);
        clientHttpRequestFactory.setReadTimeout(10_000);

        restTemplate.setRequestFactory(clientHttpRequestFactory);

        return restTemplate;
    }
}
