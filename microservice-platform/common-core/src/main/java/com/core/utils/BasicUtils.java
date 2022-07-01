package com.core.utils;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description 工具基类
 * @Author linyf
 * @Date 2022-06-24 16:31
 */
public interface BasicUtils {
    /**
     * @Description 获取所有的controller请求
     * @param: requestMappingHandlerMapping
     * @return: java.util.Set<java.lang.String>
     * @Author linyf
     * @Date 2022-06-24 16:36
     */
    static Set<String> getAllUrisInCurrSpringBoot(RequestMappingHandlerMapping requestMappingHandlerMapping){
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        Set<RequestMappingInfo> list = handlerMethods.keySet();
        Set<String> set =  new HashSet<>();
        for (RequestMappingInfo it : list) {
            Set<String> patterns = it.getPatternsCondition().getPatterns();
            Set<RequestMethod> methods = it.getMethodsCondition().getMethods();
            List<String> ms = methods.stream().map(Enum::name).collect(Collectors.toList());
            for (String pattern : patterns) {
                set.add(String.join(",", ms) + "\t" + pattern);
            }
        }
        return set;
    }
}
