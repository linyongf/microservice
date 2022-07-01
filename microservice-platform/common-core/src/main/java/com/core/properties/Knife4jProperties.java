package com.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description xxx
 * @Author linyf
 * @Date 2022-06-29 9:48
 */
@ConfigurationProperties(prefix = "knife4j")
@Data
@Component
public class Knife4jProperties {
    private String title;
    private String desc;
    private String groupName;
}
