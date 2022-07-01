package com.core.config.starter;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.core.mybatis.CustomSqlInjector;
import com.core.mybatis.MybatisAutoFillHandler;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@MapperScan(MybatisPlusConfig.MAPPER_SCAN_PACKAGE)
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class, MybatisPlusAutoConfiguration.class})
@AutoConfigureBefore(MybatisPlusAutoConfiguration.class)
public class MybatisPlusConfig {
    /**
     * Mybatis 扫描路径
     */
    static final String MAPPER_SCAN_PACKAGE = "com.**.mapper";

    /**
     * @Description MybatisPlus 拦截器链
     * @return: com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor
     * @Author linyf
     * @Date 2022-06-24 15:50
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }

    @Bean
    public ConfigurationCustomizer configurationCustomizer(){
        return configuration -> configuration.setUseGeneratedShortKey(false);
    }

    /**
     * @Description 自动填充处理器
     * @return: com.baseapi.mybatis.MybatisAutoFillHandler
     * @Author linyf
     * @Date 2022-06-24 15:55
     */
    @Bean
    public MybatisAutoFillHandler mybatisAutoFillHandler(){
        return new MybatisAutoFillHandler();
    }

    /**
     * @Description sql 注入器
     * @return: com.baseapi.mybatis.CustomSqlInjector
     * @Author linyf
     * @Date 2022-06-24 15:55
     */
    @Bean
    public CustomSqlInjector sqlInjector(){
        return new CustomSqlInjector();
    }
}
