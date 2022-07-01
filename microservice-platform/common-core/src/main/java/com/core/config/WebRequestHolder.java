package com.core.config;

import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description Http 请求响应持有器
 * @Author linyf
 * @Date 2022-06-24 16:12
 */
public final class WebRequestHolder {
    private WebRequestHolder() {
    }

    public static ServletRequestAttributes obtainServletRequestAttr() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    /**
     * 获取 Http 请求对象
     * @return {@link HttpServletRequest}
     */
    public static HttpServletRequest obtainRequest() {
        ServletRequestAttributes servletRequestAttributes = obtainServletRequestAttr();
        Assert.notNull(servletRequestAttributes, "servletRequestAttributes is null!");

        HttpServletRequest request = servletRequestAttributes.getRequest();
        Assert.notNull(request, "HttpServletRequest is null!");
        return request;
    }

    /**
     * 获取 Http 响应对象
     * @return {@link HttpServletResponse}
     */
    public static HttpServletResponse obtainResponse() {
        ServletRequestAttributes servletRequestAttributes = obtainServletRequestAttr();
        Assert.notNull(servletRequestAttributes, "servletRequestAttributes is null!");

        HttpServletResponse response = servletRequestAttributes.getResponse();
        Assert.notNull(response, "HttpServletResponse is null!");
        return response;
    }
}
