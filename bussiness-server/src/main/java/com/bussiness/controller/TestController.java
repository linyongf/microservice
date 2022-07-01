package com.bussiness.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RequestMapping("/r")
@Api(tags = "业务模块")
@RestController
public class TestController {

    @GetMapping("/r1")
    @ApiOperation(value = "访问资源1", notes = "访问资源111")
    public String r1() {
        return "访问资源1";
    }

    @GetMapping("/r2")
    @ApiIgnore
    public String r2() {
        return "访问资源2";
    }
}
