package com.core.config.starter;

import com.core.config.*;
import com.core.properties.Knife4jProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({FeignConfig.class, RedisTemplateConfig.class, RestTemplateConfig.class, TheadPoolConfig.class,
        ResponseWrapperControllerAdvice.class, UnifiedExceptionControllerAdvice.class,
        Knife4jConfiguration.class, Knife4jProperties.class
})
public class AutoConfig {
}
