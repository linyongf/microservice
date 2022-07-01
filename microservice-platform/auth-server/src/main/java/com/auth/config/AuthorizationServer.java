package com.auth.config;

import com.auth.exception.CustomWebResponseExceptionTranslator;
import com.auth.filter.CustomClientCredentialsTokenEndpointFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Arrays;

/**
 * @Description 授权服务配置
 *  1.客户端详情配置：configure(ClientDetailsServiceConfigurer clients)
 *  2.授权服务端点配置：configure(AuthorizationServerEndpointsConfigurer endpoints)
 *
 * @Author linyf
 * @Date 2022-06-24 14:33
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Resource
    private TokenStore tokenStore;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private DataSource dataSource;

    @Resource
    private AuthenticationEntryPoint customAuthenticationEntryPoint;

    /**
     * @Description 客户端详情
     * @return: org.springframework.security.oauth2.provider.ClientDetailsService
     * @Author linyf
     * @Date 2022-06-24 15:21
     */
    @Bean
    public ClientDetailsService clientDetailsService() {
        JdbcClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        clientDetailsService.setPasswordEncoder(passwordEncoder);
        return clientDetailsService;
    }

    /**
     * @Description 1.客户端详情配置
     * @param: clients
     * @Author linyf
     * @Date 2022-06-24 15:22
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService());
    }

    /**
     * @Description 令牌配置
     * @return: org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices
     * @Author linyf
     * @Date 2022-06-24 15:22
     */
    @Bean
    public AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setClientDetailsService(clientDetailsService());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setTokenStore(tokenStore);

        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        enhancerChain.setTokenEnhancers(Arrays.asList(jwtAccessTokenConverter));
        tokenServices.setTokenEnhancer(enhancerChain);

        tokenServices.setAccessTokenValiditySeconds(2 * 60 * 60);//令牌默认有效期2小时
        tokenServices.setRefreshTokenValiditySeconds(3 * 24 * 60 * 60);//刷新令牌默认有效期3天
        return tokenServices;
    }

    /**
     * @Description 授权码
     * @return: org.springframework.security.oauth2.provider.code.AuthorizationCodeServices
     * @Author linyf
     * @Date 2022-06-24 15:23
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        // 设置授权码模式的授权码如何存取
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    /**
     * @Description 2.授权服务令牌端点配置
     * @param: endpoints
     * @Author linyf
     * @Date 2022-06-24 15:25
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .authorizationCodeServices(authorizationCodeServices())
                .tokenServices(tokenServices())
                .reuseRefreshTokens(false)
                .allowedTokenEndpointRequestMethods(HttpMethod.POST)
                .exceptionTranslator(new CustomWebResponseExceptionTranslator())
        ;
    }

    /**
     * @Description 3.授权服务令牌端点安全约束配置
     * @param: security
     * @Author linyf
     * @Date 2022-06-24 15:25
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception{
        CustomClientCredentialsTokenEndpointFilter endpointFilter = new CustomClientCredentialsTokenEndpointFilter(security);
        endpointFilter.afterPropertiesSet();
        endpointFilter.setAuthenticationEntryPoint(customAuthenticationEntryPoint);

        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()")
                // .allowFormAuthenticationForClients()
                // 作用：让 /oauth/token 支持 client_id 以及 client_secret 作登录认证，在 BasicAuthenticationFilter 之前
                // 添加 ClientCredentialsTokenEndpointFilter，从而使用 ClientDetailUserDetailsService 进行 client 登录的验证。
                // 同时，即使自定义了 ClientCredentialsTokenEndpointFilter，oauth2 仍然会使用默认的 ClientCredentialsTokenEndpointFilter
                .addTokenEndpointAuthenticationFilter(endpointFilter)
        ;
    }
}
