package com.core.security;

import com.core.constants.BasicConstants;
import com.core.model.SysUser;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description JWT 令牌增强，继承 JwtAccessTokenConverter，将业务所需的额外信息放入令牌中
 * @Author linyf
 * @Date 2022-06-24 16:29
 */
public class JwtAccessTokenEnhancer extends JwtAccessTokenConverter {
    /**
     * @Description 重写enhance方法，在其中扩展
     * @param: accessToken
     * @param: authentication
     * @return: org.springframework.security.oauth2.common.OAuth2AccessToken
     * @Author linyf
     * @Date 2022-06-24 16:30
     */
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Object principal = authentication.getUserAuthentication().getPrincipal();
        if (principal instanceof SysUser) {
            Map<String, Object> additionalInformation = new HashMap<>();
            //获取 userDetailService 中查询到用户信息
            SysUser user = (SysUser) principal;
            //设置用户的 id、name
            additionalInformation.put(BasicConstants.USER_ID, user.getId());
            additionalInformation.put(BasicConstants.NAME, user.getName());
            additionalInformation.put(BasicConstants.MOBILE, user.getMobile());
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
        }
        return super.enhance(accessToken, authentication);
    }
}
