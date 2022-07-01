package com.core.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Description Redis 配置
 * @Author linyf
 * @Date 2022-06-24 16:01
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@ConditionalOnClass(RedisOperations.class)
@Slf4j
public class RedisTemplateConfig {

    private ObjectMapper objectMapper;
    private final Jackson2JsonRedisSerializer<Object> jsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
    private final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

    public RedisTemplateConfig() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        this.objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.objectMapper.registerModule(new JavaTimeModule());
        this.jsonRedisSerializer.setObjectMapper(this.objectMapper);
        log.info("RedisTemplate Configuration In BaseAPI Init Done!");
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        template.setValueSerializer(jsonRedisSerializer);
        template.setHashValueSerializer(jsonRedisSerializer);
        log.info("RedisTemplate<String, Object> configuration in auto-config init done");
        return template;
    }
}
