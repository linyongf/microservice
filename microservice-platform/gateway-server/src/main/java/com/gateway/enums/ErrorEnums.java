package com.gateway.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description 错误枚举
 * @Author linyf
 * @Date 2022-06-23 17:10
 */
@Getter
@AllArgsConstructor
public enum ErrorEnums {
    TOKEN_INVALID_MSG("无效的token!"),
    TOKEN_EXPIRED_MSG("token已过期!"),
    FALLBACK_TIMEOUT_MSG("接口调用超时"),
    FALLBACK_404_MSG("服务未注册到注册中心"),
    AUTHORIZATION_FAIL_MSG("没有访问该资源的权限！"),
    RATE_LIMIT_MSG("访问已受限制，请稍候重试")
    ;


    private String msg;
}
