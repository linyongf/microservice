package com.auth.config;

import com.auth.handler.CustomLogoutHandler;
import com.auth.handler.CustomLogoutSuccessHandler;
import com.base.constants.BasicConstants;
import com.base.security.JwtAccessTokenEnhancer;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.annotation.Resource;

/**
 * @Description web 安全配置
 * @Author linyf
 * @Date 2022-06-24 15:18
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private CustomLogoutHandler logoutHandler;

    /**
     * @Description 密码加密器
     * @return: org.springframework.security.crypto.password.PasswordEncoder
     * @Author linyf
     * @Date 2022-06-24 15:25
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * @Description 令牌的存储策略
     * @return: org.springframework.security.oauth2.provider.token.TokenStore
     * @Author linyf
     * @Date 2022-06-24 15:25
     */
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    /**
     * @Description TokenEnhancer的子类，在JWT编码的令牌值和OAuth身份验证信息之间进行转换。
     * @return: org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
     * @Author linyf
     * @Date 2022-06-24 15:26
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenEnhancer();
        //对称秘钥，资源服务器使用该秘钥来验证
        converter.setSigningKey(BasicConstants.SIGN_KEY);
        return converter;
    }

    /**
     * @Description 认证管理器
     * @return: org.springframework.security.authentication.AuthenticationManager
     * @Author linyf
     * @Date 2022-06-24 15:26
     */
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * @Description 安全配置
     * @param: hs
     * @Author linyf
     * @Date 2022-06-24 15:27
     */
    @Override
    protected void configure(HttpSecurity hs) throws Exception {
        hs.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // actuator中的所有健康检查端点都放行, 此处包含了上述几处oauth端点,直接放行
                .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().permitAll()
                .and()
                .logout().addLogoutHandler(logoutHandler)
                .logoutSuccessHandler(new CustomLogoutSuccessHandler())
        ;
    }
}
