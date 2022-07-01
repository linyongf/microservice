package com.gateway.util;

import com.base.Result;
import com.base.constants.BasicConstants;
import com.base.utils.JsonUtil;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @Description 网关服务工具类
 * @Author linyf
 * @Date 2022-06-23 14:47
 * @Version 1.0
 */
public class Utils {

    private static final String REQUEST_METHOD_PREFIX = "[";
    private static final String REQUEST_METHOD_SUFFIX = "]";

    /***
     * @Description 对url进行校验匹配
     * @param: urls
     * @param: path
     * @return: boolean
     * @Author linyf
     * @Date 2022-06-23 14:47
     */
    public static boolean checkUrls(List<String> urls, String path) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        for (String url : urls) {
            if (pathMatcher.match(url, path))
                return true;
        }
        return false;
    }

    /**
     * @Description 获取 restful 请求路径
     * @param: request
     * @return: java.lang.String
     * @Author linyf
     * @Date 2022-06-23 16:09
     */
    public static String getRestfulPath(ServerHttpRequest request){
        String targetURI = request.getURI().getPath();
        String requestMethod = request.getMethodValue();
        return REQUEST_METHOD_PREFIX + requestMethod + REQUEST_METHOD_SUFFIX + targetURI;
    }

    /**
     * @Description 响应认证或鉴权失败消息
     * @param: response
     * @return: reactor.core.publisher.Mono<java.lang.Void>
     * @Author linyf
     * @Date 2022-06-23 16:44
     */
    public static Mono<Void> resp(ServerHttpResponse response, String msg){
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        String body = JsonUtil.serialize(Result.err(msg));
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    /**
     * @Description 获取请求头中的token
     * @param: request
     * @return: java.lang.String
     * @Author linyf
     * @Date 2022-06-28 15:09
     */
    public static String getToken(ServerHttpRequest request){
        String tokenStr = request.getHeaders().getFirst(BasicConstants.FEIGN_HEADER_TOKEN_PARAM);
        if (StringUtils.isEmpty(tokenStr)) {
            return null;
        }
        String token = tokenStr.split(" ")[1];
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        return token;
    }
}
