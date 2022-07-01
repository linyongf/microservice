package com.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description 安全配置属性
 * @Author linyf
 * @Date 2022-06-23 14:55
 */
@Data
@Component
@ConfigurationProperties(prefix = "secure")
public class SecurityProperties {
    /**
     * 白名单
     */
    private List<String> whiteList;

    /**
     * 认证即可访问
     */
    private List<String> authList;
}
