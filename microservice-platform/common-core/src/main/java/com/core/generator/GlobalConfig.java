package com.core.generator;

import com.baomidou.mybatisplus.annotation.IdType;

/**
 * @Description mybatis-plus 自动生成器配置
 * @Author linyf
 * @Date 2022-06-24 16:15
 */
public class GlobalConfig {

    /**
     * 作者
     */
    private String author;

    /**
     * 文件是否重写
     */
    private Boolean fileOverride;

    /**
     * id类型
     */
    private IdType idType;

    /**
     * 是否swagger
     */
    private Boolean swagger2;

    /**
     * 输出根目录
     */
    private String outputDir;

    /**
     * 数据库类型，比如：mysql, oracle 等
     */
    private String dbType;

    /**
     * 数据库 URL
     */
    private String dbUrl;

    /**
     * 数据库驱动
     */
    private String dbDriverName;

    /**
     * 数据库用户名
     */
    private String dbUsername;

    /**
     * 数据库密码
     */
    private String dbPassword;

    /**
     * 生成实体类时，父类实体类中的公共列名，多个以逗号分割
     */
    private String superEntityColumns;

    /**
     * 表前缀
     */
    private String tablePrefix;

    /**
     * 代码包名
     */
    private String basicPackage;

    /**
     * 实体类的后缀，比如：UserDO 中的 DO 就是后缀名
     */
    private String entityNameSuffix;

    /**
     * 表名，多个以逗号分割
     */
    private String tables;
}
