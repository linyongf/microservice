package com.auth.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * @Description 自定义用户认证失败异常
 * @Author linyf
 * @Date 2022-06-24 15:33
 */
@Getter
@JsonSerialize(using = CustomOauthExceptionSerializer.class)
public class CustomOauthException extends OAuth2Exception {

    public CustomOauthException(String msg) {
        super(msg);
    }
}
