package com.auth.handler;

import com.base.config.WebRequestHolder;
import com.base.constants.BasicConstants;
import com.base.utils.DateUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.concurrent.TimeUnit;

/**
 * @Description 退出处理器
 * @Author linyf
 * @Date 2022-06-24 15:37
 */
@Component
public class CustomLogoutHandler extends SecurityContextLogoutHandler {

    @Resource
    private RedisTemplate redisTemplate;

    public void logout(HttpServletRequest request, HttpServletResponse response,
                       Authentication authentication) {
        super.logout(request, response, authentication);
        Enumeration<String> headers = WebRequestHolder.obtainRequest().getHeaders(BasicConstants.HEADER_JWT_JTI_EXP);
        String jti = headers.nextElement();
        String expStr = headers.nextElement();
        LocalDateTime expDate = LocalDateTime.parse(expStr, DateTimeFormatter.ISO_DATE_TIME);
        redisTemplate.opsForValue().set(BasicConstants.JTI_KEY_PREFIX + jti, null, DateUtil.between(LocalDateTime.now(), expDate), TimeUnit.SECONDS);
    }
}
