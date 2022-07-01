package com.auth.handler;

import com.base.Result;
import com.base.utils.JsonUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * @Description 退出成功处理器
 * @Author linyf
 * @Date 2022-06-24 15:38
 */
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        PrintWriter writer = response.getWriter();
        writer.write(JsonUtil.serialize(Result.ok()));
        writer.flush();
    }
}
