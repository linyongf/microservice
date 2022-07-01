package com.gateway.filter;

import com.base.constants.BasicConstants;
import com.base.utils.DateUtil;
import com.base.utils.JsonUtil;
import com.gateway.config.SecurityProperties;
import com.gateway.enums.ErrorEnums;
import com.gateway.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

/**
 * @Description 全局过滤器：对token的拦截，解析token放入header中，便于下游微服务获取用户信息
 * @Author linyf
 * @Date 2022-06-23 16:59
 */
@Component
@Slf4j
@Order(0)
public class GlobalAuthenticationFilter implements GlobalFilter {
    /**
     * JWT令牌的服务
     */
    @Resource
    private TokenStore tokenStore;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 系统参数配置
     */
    @Resource
    private SecurityProperties securityProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestUrl = exchange.getRequest().getPath().value();
        //1、白名单放行，比如授权服务、静态资源.....
        if (Utils.checkUrls(securityProperties.getWhiteList(), requestUrl)) {
            return chain.filter(exchange);
        }

        //2、 检查token是否存在
        String token = Utils.getToken(exchange.getRequest());
        if (StringUtils.isEmpty(token)) {
            return Utils.resp(exchange.getResponse(), ErrorEnums.TOKEN_INVALID_MSG.getMsg());
        }

        //3 判断是否是有效的token
        try {
            OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(token);
            Map<String, Object> additionalInformation = oAuth2AccessToken.getAdditionalInformation();
            String jti = additionalInformation.get(BasicConstants.JTI).toString();
            Boolean hasKey = redisTemplate.hasKey(BasicConstants.JTI_KEY_PREFIX + jti);
            if (hasKey)
                return Utils.resp(exchange.getResponse(), ErrorEnums.TOKEN_INVALID_MSG.getMsg());
            // 认证通过，将令牌中的额外信息加密后直接放入请求头中，方便下游微服务解析获取用户信息；
            // 同时将 jti 放入请求头中，以便于退出时放入 redis 黑名单中
            String encodeInfo = Base64.getEncoder().encodeToString(JsonUtil.serialize(additionalInformation).getBytes(StandardCharsets.UTF_8));
            ServerHttpRequest tokenRequest = exchange
                    .getRequest()
                    .mutate()
                    .header(BasicConstants.HEADER_JWT_USER, encodeInfo)
                    .header(BasicConstants.HEADER_JWT_JTI_EXP,
                            jti,
                            DateUtil.date2LocalDateTime(oAuth2AccessToken.getExpiration()).toString())
                    .build();
            ServerWebExchange build = exchange.mutate().request(tokenRequest).build();

            return chain.filter(build);
        } catch (InvalidTokenException e) {
            //解析token异常，直接返回token无效
            return Utils.resp(exchange.getResponse(), ErrorEnums.TOKEN_INVALID_MSG.getMsg());
        }
    }
}
