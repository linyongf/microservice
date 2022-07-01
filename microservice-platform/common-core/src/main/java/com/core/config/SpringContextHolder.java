package com.core.config;

import com.core.constants.BasicConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * @Description Spring容器上下文持有器
 * @Author linyf
 * @Date 2022-06-24 16:11
 */
@Slf4j
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Assert.notNull(applicationContext, "applicationContext 注入失败");

        if(Objects.nonNull(BasicConstants.applicationContext)){
            log.warn("SpringContextHolder 中的 applicationContext 被覆盖，原有 context 为：{}", BasicConstants.applicationContext);
        }

        BasicConstants.applicationContext = applicationContext;
    }

    public static <T> T getBean(String name){
        return (T) BasicConstants.applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> requiredType){
        return (T) BasicConstants.applicationContext.getBean(requiredType);
    }

    public static <T> T getBean(String name, Class<T> requiredType){
        return (T) BasicConstants.applicationContext.getBean(name, requiredType);
    }

    @Override
    public void destroy() throws Exception {
        BasicConstants.applicationContext = null;
    }
}
