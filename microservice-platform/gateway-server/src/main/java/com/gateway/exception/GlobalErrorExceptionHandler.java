package com.gateway.exception;

import com.gateway.enums.ErrorEnums;
import com.gateway.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Description 认证管理器认证失败异常处理，@Order(-1)：优先级一定要比ResponseStatusExceptionHandler低
 * @Author linyf
 * @Date 2022-06-23 16:26
 */
@Slf4j
@Order(-1)
@Component
@RequiredArgsConstructor
public class GlobalErrorExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        if (response.isCommitted()) {
            return Mono.error(ex);
        }

        String msg = ErrorEnums.AUTHORIZATION_FAIL_MSG.getMsg();
        if (ex instanceof ResponseStatusException) {
            msg = ((ResponseStatusException) ex).getStatus().toString();
        }
        if (ex instanceof InvalidTokenException) {
            msg = ErrorEnums.TOKEN_INVALID_MSG.getMsg();
        }

        return Utils.resp(response, msg);
    }
}