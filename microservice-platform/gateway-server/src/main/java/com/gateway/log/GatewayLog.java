package com.gateway.log;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * @Description 日志实体
 * @Author linyf
 * @Date 2022-06-23 17:23
 */
@Data
@Document
public class GatewayLog {
    @Id
    private String id;
    /**访问实例*/
    private String targetServer;
    /**请求路径*/
    private String requestPath;
    /**请求方法*/
    private String requestMethod;
    /**请求用户*/
    private String requestUser;
    /**协议 */
    private String schema;
    /**请求体*/
    private String requestBody;
    /**响应体*/
    private String responseData;
    /**请求ip*/
    private String ip;
    /**请求时间*/
    private LocalDateTime requestTime;
    /**响应时间*/
    private LocalDateTime responseTime;
    /**执行时间*/
    private float executeTime;
}
