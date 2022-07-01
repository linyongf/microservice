package com.core.config;

import com.core.Result;
import com.core.utils.JsonUtil;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import springfox.documentation.spring.web.json.Json;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

/**
 * @Description 响应增强
 * @Author linyf
 * @Date 2022-06-24 16:10
 */
@RestControllerAdvice
public class ResponseWrapperControllerAdvice implements ResponseBodyAdvice<Object> {
    private static final List EXCLUDE_METHODS = Arrays.asList("healthCheck", "swaggerResources", "uiConfiguration");

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        if(excludeMethod(methodParameter.getMethod().getName())){
            return false;
        }

        Annotation[] classAnnotations = methodParameter.getContainingClass().getAnnotations();
        for (Annotation a : classAnnotations) {
            if(a instanceof RestController){
                return true;
            }
        }

        Annotation[] methodAnnotations = methodParameter.getMethod().getAnnotations();
        for (Annotation a : methodAnnotations) {
            if(a instanceof ResponseBody){
                return true;
            }
        }

        return false;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if(filterRtnVal(body)){
            return body;
        }

        return body instanceof String
                ? JsonUtil.serialize(Result.ok(body))
                : Result.ok(body);
    }

    /**
     * 排除不需要包装的方法
     * @param methodName 当前方法名
     * @return true: 排除；false:不排除
     */
    private boolean excludeMethod(String methodName){
        if(EXCLUDE_METHODS.contains(methodName)){
            return true;
        }
        return false;
    }

    private boolean filterRtnVal(Object body){
        return body instanceof byte[] || body instanceof ResponseEntity || body instanceof Resource || body instanceof Result || body instanceof Json;
    }
}
