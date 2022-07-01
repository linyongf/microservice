package com.auth.exception;

import com.base.Result;
import com.base.utils.JsonUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * @Description 处理客户端认证失败
 * @Author linyf
 * @Date 2022-06-24 15:29
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        Result result = Result.err(authException.getMessage());
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        PrintWriter writer = response.getWriter();
        writer.write(JsonUtil.serialize(result));
        writer.flush();
    }
}
