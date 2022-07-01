package com.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;

/**
 * @Description 用户认证失败处理
 * @Author linyf
 * @Date 2022-06-24 15:35
 */
public class CustomWebResponseExceptionTranslator extends DefaultWebResponseExceptionTranslator {

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
        ResponseEntity<OAuth2Exception> translate = super.translate(e);
        HttpStatus httpStatus = translate.getStatusCode();
        OAuth2Exception body = translate.getBody();
        CustomOauthException customOauthException = new CustomOauthException(HttpStatus.BAD_REQUEST.equals(httpStatus) ? body.getMessage() : "用户名或密码错误");
        return new ResponseEntity<>(customOauthException, translate.getHeaders(), translate.getStatusCode());
    }
}
