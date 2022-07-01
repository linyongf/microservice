package com.gateway.exception;

import com.gateway.enums.ErrorEnums;
import com.gateway.util.Utils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Description 鉴权处理器鉴权失败处理
 * @Author linyf
 * @Date 2022-06-23 16:35
 */
@Component
public class RequestAccessDeniedHandler implements ServerAccessDeniedHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        return Utils.resp(exchange.getResponse(), ErrorEnums.AUTHORIZATION_FAIL_MSG.getMsg());
    }
}
