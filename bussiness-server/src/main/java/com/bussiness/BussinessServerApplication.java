package com.bussiness;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
@EnableSwagger2WebMvc
public class BussinessServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BussinessServerApplication.class, args);
    }

}
