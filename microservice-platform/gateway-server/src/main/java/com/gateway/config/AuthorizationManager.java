package com.gateway.config;

import com.base.constants.BasicConstants;
import com.base.Result;
import com.gateway.feign.UserService;
import com.gateway.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description 鉴权管理器：用于认证成功后对用户访问某资源进行鉴权
 * @Author linyf
 * @Date 2022-06-23 14:39
 */
@Slf4j
@Component
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    @Resource
    private UserService userService;

    @Resource
    private SecurityProperties securityProperties;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
        // 获取 restful 访问路径：请求路径和请求方式进行组合
        String restfulPath = Utils.getRestfulPath(authorizationContext.getExchange().getRequest());

        // restfulPath 是否认证即可访问
        if (Utils.checkUrls(securityProperties.getAuthList(), restfulPath)) {
            return mono
                    .map(a -> new AuthorizationDecision(a.isAuthenticated()))
                    .defaultIfEmpty(new AuthorizationDecision(false));
        }

        // 当前登录用户是否有权限访问该路径（超级管理员直接放行）
        Result<List<String>> roleListRes = userService.getRoleListByUrl(restfulPath);
        return mono
                .filter(Authentication::isAuthenticated)
                .flatMapIterable(Authentication::getAuthorities)
                .map(GrantedAuthority::getAuthority)
                .any(authority -> {
                    if (BasicConstants.SUPERADMIN.equals(authority))
                        return true;
                    return roleListRes.getData().contains(authority);
                })
                .map(AuthorizationDecision::new)
                .defaultIfEmpty(new AuthorizationDecision(false));
    }
}