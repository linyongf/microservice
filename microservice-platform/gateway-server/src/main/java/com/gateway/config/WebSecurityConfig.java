package com.gateway.config;

import com.base.constants.BasicConstants;
import com.base.security.JwtAccessTokenEnhancer;
import com.gateway.exception.RequestAccessDeniedHandler;
import com.gateway.exception.RequestAuthenticationEntryPoint;
import com.gateway.filter.CorsFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.server.resource.web.server.ServerBearerTokenAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authorization.AuthorizationContext;

import javax.annotation.Resource;

/**
 * @Description 网关的OAuth2.0资源的配置类
 * @Author linyf
 * @Date 2022-06-23 15:01
 */
@AllArgsConstructor
@EnableWebFluxSecurity
@Configuration
public class WebSecurityConfig {

    @Resource
    private ReactiveAuthenticationManager authenticationManager;

    @Resource
    private ReactiveAuthorizationManager<AuthorizationContext> authorizationManager;

    @Resource
    private RequestAccessDeniedHandler requestAccessDeniedHandler;

    @Resource
    private RequestAuthenticationEntryPoint requestAuthenticationEntryPoint;

    @Resource
    private SecurityProperties securityProperties;

    @Resource
    private CorsFilter corsFilter;

    /**
     * @Description 令牌存储策略
     * @return: org.springframework.security.oauth2.provider.token.TokenStore
     * @Author linyf
     * @Date 2022-06-23 15:12
     */
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    /**
     * @Description TokenEnhancer 的子类，在 JWT 编码的令牌值和 OAuth 身份验证信息之间进行转换
     * @return: org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
     * @Author linyf
     * @Date 2022-06-23 15:15
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenEnhancer();
        converter.setSigningKey(BasicConstants.SIGN_KEY);
        return converter;
    }

    /**
     * @Description 安全过滤器链
     * @param: http
     * @return: org.springframework.security.web.server.SecurityWebFilterChain
     * @Author linyf
     * @Date 2022-06-23 15:18
     */
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
            .httpBasic().disable()
            .csrf().disable()
            .authorizeExchange()
            // 白名单直接放行
            .pathMatchers(securityProperties.getWhiteList().toArray(new String[0])).permitAll()
            // 其他的请求使用鉴权管理器鉴权
            .anyExchange().access(authorizationManager)
            //鉴权的异常处理：权限不足、token失效
            .and().exceptionHandling()
            .accessDeniedHandler(requestAccessDeniedHandler)//处理未授权
            .authenticationEntryPoint(requestAuthenticationEntryPoint)//处理未认证
            .and()
            // 跨域过滤器
            .addFilterAt(corsFilter, SecurityWebFiltersOrder.CORS)
        ;

        //认证过滤器，放入认证管理器 tokenAuthenticationManager
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(authenticationManager);
        authenticationWebFilter.setServerAuthenticationConverter(new ServerBearerTokenAuthenticationConverter());
        //token的认证过滤器，用于校验token和认证
        http.addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION);
        return http.build();
    }
}
