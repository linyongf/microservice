package com.foundation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class})
@EnableFeignClients
@EnableDiscoveryClient
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableSwagger2WebMvc
public class FoundationServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoundationServerApplication.class, args);
    }

}
