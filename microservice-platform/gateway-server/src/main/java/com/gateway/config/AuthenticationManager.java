package com.gateway.config;

import com.gateway.enums.ErrorEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * @Description JWT 认证管理器：对客户端请求携带过来的 token 进行校验
 * @Author linyf
 * @Date 2022-06-23 14:35
 */
@Component
@Slf4j
public class AuthenticationManager implements ReactiveAuthenticationManager {

    @Resource
    private TokenStore tokenStore;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.justOrEmpty(authentication)
            .filter(a -> a instanceof BearerTokenAuthenticationToken)
            .cast(BearerTokenAuthenticationToken.class)
            .map(BearerTokenAuthenticationToken::getToken)
            .flatMap(accessToken -> {
                OAuth2AccessToken oAuth2AccessToken = this.tokenStore.readAccessToken(accessToken);
                if (oAuth2AccessToken == null) {
                    return Mono.error(new InvalidTokenException(ErrorEnums.TOKEN_INVALID_MSG.getMsg()));
                } else if (oAuth2AccessToken.isExpired()) {
                    oAuth2AccessToken.getExpiresIn();
                    return Mono.error(new InvalidTokenException(ErrorEnums.TOKEN_EXPIRED_MSG.getMsg()));
                }

                OAuth2Authentication oAuth2Authentication = this.tokenStore.readAuthentication(accessToken);
                if (oAuth2Authentication == null) {
                    return Mono.error(new InvalidTokenException(ErrorEnums.TOKEN_INVALID_MSG.getMsg()));
                } else {
                    return Mono.just(oAuth2Authentication);
                }
            }).cast(Authentication.class);
    }
}
