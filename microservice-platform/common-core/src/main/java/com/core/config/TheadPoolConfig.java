package com.core.config;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @Description 线程池配置
 * @Author linyf
 * @Date 2022-06-24 16:11
 */
@Configuration
@EnableAsync
@Data
public class TheadPoolConfig {

    private static volatile ScheduledThreadPoolExecutor SCHEDULED_THREAD_POOL;

    @Bean
    public AsyncTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setQueueCapacity(50);
        taskExecutor.setMaxPoolSize(20);
        taskExecutor.setThreadNamePrefix("async-task-thread-");
        taskExecutor.setKeepAliveSeconds(300);
        taskExecutor.initialize();
        return taskExecutor;
    }

    /**
     * @return 创建一个ScheduledThreadPoolExecutor
     */
    public static ScheduledThreadPoolExecutor getScheduledThreadPool() {
        if (SCHEDULED_THREAD_POOL == null) {
            synchronized (TheadPoolConfig.class) {
                if (SCHEDULED_THREAD_POOL == null) {
                    SCHEDULED_THREAD_POOL = new ScheduledThreadPoolExecutor(10,
                            new CustomizableThreadFactory("schedule-task-pool-"));
                }
            }
        }
        return SCHEDULED_THREAD_POOL;
    }
}
