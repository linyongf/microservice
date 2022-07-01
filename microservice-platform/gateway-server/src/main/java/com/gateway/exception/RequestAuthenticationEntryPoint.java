package com.gateway.exception;

import com.gateway.enums.ErrorEnums;
import com.gateway.util.Utils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Description 用于处理鉴权管理器鉴权过程中没有登录或token过期时的自定义返回结果
 * @Author linyf
 * @Date 2022-06-23 16:38
 */
@Component
public class RequestAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
        return Utils.resp(exchange.getResponse(), ErrorEnums.TOKEN_INVALID_MSG.getMsg());
    }
}
