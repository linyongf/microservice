package com.core.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * @Description mybatis-plus 代码生成器
 * @Author linyf
 * @Date 2022-06-24 16:15
 */
public class MyBatisPlusGenerator {

    public static void generateCode(){
        FastAutoGenerator.create("jdbc:mysql://192.168.211.1:3306/cloud_test?characterEncoding=utf-8&serverTimezone=UTC&useSSL=false",
                "root", "123456")
                .globalConfig(builder -> {
                    builder.author("lyf") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("C:\\workspace\\lyf-cloud-platform\\base-cloud-platform\\src\\main\\java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder
                            .parent("com.base") // 设置父包名
//                            .moduleName("system") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml,
                                    "C:\\workspace\\lyf-cloud-platform\\base-cloud-platform\\src\\main\\resources\\mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("user"); // 设置需要生成的表名
//                            .addTablePrefix("t_", "c_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }

}

