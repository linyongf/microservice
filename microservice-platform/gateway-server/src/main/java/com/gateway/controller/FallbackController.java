package com.gateway.controller;

import com.base.Result;
import com.gateway.enums.ErrorEnums;
import com.netflix.hystrix.exception.HystrixTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;

/**
 * @Description 熔断、降级回调
 * @Author linyf
 * @Date 2022-06-23 16:14
 */
@RestController
@Slf4j
public class FallbackController {

    @RequestMapping("/fallback")
    public Result fallback(ServerWebExchange exchange){
        Exception exception = exchange.getAttribute(ServerWebExchangeUtils.HYSTRIX_EXECUTION_EXCEPTION_ATTR);
        ServerWebExchange delegate = ((ServerWebExchangeDecorator) exchange).getDelegate();
        log.error("接口调用失败，URL={}, msg={}", delegate.getRequest().getURI(), exception);
        if (exception instanceof HystrixTimeoutException) {
            return Result.err(ErrorEnums.FALLBACK_TIMEOUT_MSG.getMsg());
        } else if (exception instanceof NotFoundException) {
            return Result.err(ErrorEnums.FALLBACK_404_MSG.getMsg());
        } else {
            return Result.err("接口调用失败: " + exception.getLocalizedMessage());
        }
    }
}
