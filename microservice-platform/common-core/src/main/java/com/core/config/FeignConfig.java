package com.core.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @Description feign 拦截器，解决 feign 调用时请求头中 token 丢失的问题
 * @Author linyf
 * @Date 2022-06-24 15:59
 */
@Configuration
@Slf4j
public class FeignConfig implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        /**
         * 使用 RequestContextHolder.getRequestAttributes() 静态方法获得Request。 (但仅限于Feign不开启Hystrix支持时。)
         * 当Feign开启Hystrix支持时，获取值为null
         * 原因在于，Hystrix的默认隔离策略是THREAD 。而 RequestContextHolder 源码中，使用了两个ThreadLocal 。
         * 解决方案一：调整隔离策略 将隔离策略设为SEMAPHORE即可
         * hystrix.command.default.execution.isolation.strategy: SEMAPHORE
         * 这样配置后，Feign可以正常工作。但该方案不是特别好。原因是Hystrix官方强烈建议使用THREAD作为隔离策略！
         *
         * 解决方案二：自定义并发策略
         * 既然Hystrix不太建议使用SEMAPHORE作为隔离策略，那么是否有其他方案呢？
         * 答案是自定义并发策略，目前，Spring Cloud Sleuth以及Spring Security都通过该方式传递 ThreadLocal 对象。
         * 编写自定义并发策略比较简单，只需编写一个类，让其继承HystrixConcurrencyStrategy ，并重写wrapCallable 方法即可。
         *
         */
//        HttpServletRequest request = WebRequestHolder.obtainRequest();
//        requestTemplate.header("Authorization", request.getHeader("Authorization"));

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null){
//                log.warn("requestAttributes为null");
            return;
        }
        //获取本地线程绑定的请求对象
        HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
        //给请求模板附加本地线程头部信息，主要是cookie信息
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String name =(String) headerNames.nextElement();
            requestTemplate.header(name,request.getHeader(name));
        }
    }
}
