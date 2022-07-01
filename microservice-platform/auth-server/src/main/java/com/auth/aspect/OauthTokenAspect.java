package com.auth.aspect;

import com.base.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;

import java.security.Principal;

/**
 * @Description 封装 认证通过 的响应结果
 * @Author linyf
 * @Date 2022-06-24 14:17
 */
@Slf4j
@Component
@Aspect
public class OauthTokenAspect {
    @Around("execution(* org.springframework.security.oauth2.provider.endpoint.TokenEndpoint.postAccessToken(..))")
    public Object handleControllerMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Principal principal = (Principal) args[0];
        if (!(principal instanceof Authentication)) {
            throw new InsufficientAuthenticationException("There is no client authentication. Try adding an appropriate authentication filter.");
        }
        Object proceed = joinPoint.proceed();
        ResponseEntity<OAuth2AccessToken> responseEntity = (ResponseEntity<OAuth2AccessToken>) proceed;
        OAuth2AccessToken body = responseEntity.getBody();
        return ResponseEntity.status(HttpStatus.OK).body(Result.ok(body.getValue()));
    }
}
