/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.gateway.swagger;

import com.base.constants.BasicConstants;
import com.base.constants.SystemServerConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/***
 * @Description swagger 资源配置，根据网关路由获取所有服务实例名
 * @Author linyf
 * @Date 2022-06-23 14:33
 */
@Slf4j
@Component
@Primary
public class SwaggerResourceConfig implements SwaggerResourcesProvider {

    @Resource
    private RouteLocator routeLocator;

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public List<SwaggerResource> get() {
        //服务名称列表
        List<String> routeHosts = new ArrayList<>();
        // 获取所有可用的应用名称
        routeLocator.getRoutes()
                .filter(route -> route.getUri().getHost() != null
                        && !applicationName.equals(route.getUri().getHost())
                        && !route.getUri().getHost().contains(SystemServerConstants.AUTH)) // 过滤掉认证服务
                .subscribe(route -> routeHosts.add(route.getUri().getHost()));

        //接口资源列表
        List<SwaggerResource> resources = new ArrayList<>();
        // 去重，多负载服务只添加一次
        Set<String> existsServer = new HashSet<>();
        routeHosts.forEach(host -> {
            // 拼接url
            String url = BasicConstants.URL_SEPARATOR + host.split("-")[0] + "/v2/api-docs?group=" + host;
            //不存在则添加
            if (!existsServer.contains(url)) {
                existsServer.add(url);
                SwaggerResource swaggerResource = new SwaggerResource();
                swaggerResource.setUrl(url);
                swaggerResource.setName(host);
                resources.add(swaggerResource);
            }
        });
        return resources;
    }
}
