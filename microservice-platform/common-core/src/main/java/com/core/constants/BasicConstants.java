package com.core.constants;

import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationContext;

import java.io.File;

/**
 * @Description 常量类
 * @Author linyf
 * @Date 2022-06-24 16:14
 */
@NoArgsConstructor
public final class BasicConstants {

    /**
     * 成功标记
     */
    public static final Integer SUCCESS = 0;

    /**
     * 失败标记
     */
    public static final Integer FAILURE = -1;

    /**
     * Y
     */
    public static final String Y = "Y";

    /**
     * N
     */
    public static final String N = "N";

    /**
     * 系统换行符
     */
    public static final String NEW_LINE = System.lineSeparator();

    /**
     * 系统路径分隔符
     */
    public static final String PATH_SEPARATOR = File.separator;

    /**
     * URL 分隔符
     */
    public static final String URL_SEPARATOR = "/";

    /**
     * 系统临时目录，容器里面的目录为：/tmp
     */
    public static final String TMP_DIR = System.getProperty("java.io.tmpdir");

    /**
     * java 代码路径
     */
    public static final String JAVA_SRC_PATH = "src" + PATH_SEPARATOR + "main" + PATH_SEPARATOR + "java";

    /**
     * 逗号分隔符
     */
    public static final String COMMA_DELIMITER = ",";

    /**
     * 行级锁标识
     */
    public static final String DB_RS_LINE_LOCK = "FOR UPDATE";

    /**
     * Spring 静态持有
     */
    public static ApplicationContext applicationContext;

    /**
     * 请求头 Authorization
     */
    public static final String FEIGN_HEADER_TOKEN_PARAM = "Authorization";

    /**
     * 系统英文
     */
    public static final String SYSTEM_EN = "system";

    /**
     * 系统中文
     */
    public static final String SYSTEM_ZH = "系统";

    /**
     * jwt 对称秘钥
     */
    public static final String SIGN_KEY = "jwt_sign_key";

    /**
     * JWT令牌黑名单的KEY
     */
    public final static String JTI_KEY_PREFIX = "oauth2:blacklist:";

    public final static String HEADER_JWT_USER = "jwt-user";

    public final static String HEADER_JWT_JTI_EXP = "jwt-jti-exp";

    public final static String USER_NAME = "user_name";

    public static final String USER_ID = "user_id";

    public static final String NAME = "name";

    public static final String MOBILE = "mobile";

    public static final String JTI = "jti";

    public static final String SUPERADMIN = "superadmin";

}
