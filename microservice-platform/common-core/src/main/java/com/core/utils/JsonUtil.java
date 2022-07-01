package com.core.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @Description JSON 工具类
 * @Author linyf
 * @Date 2022-06-24 16:37
 */
@Slf4j
public class JsonUtil {
    private final static ObjectMapper mapper;

    static {
        mapper = Jackson2ObjectMapperBuilder.json().createXmlMapper(false).build()
                // 忽略为空的字段
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                // 忽略在json串中存在但Bean对象中没有的属性
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                // 允许键、值是非标准化（双引号）的json字符串
                .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
                // 忽略空对象校验异常
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                // 禁止将日期类型格式化为timestamps类型
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                // 允许出现特殊字符和转义符
                .configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true)
                .configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature(), true)
                // 允许出现单引号
                .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    }

    /**
     * 将一个对象obj转换成json字符串
     *
     * @param object 对象
     * @return json字符串
     */
    public synchronized static String serialize(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception exception) {
            log.error("Failed to serialization object because of an error has occurred: {}.", exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    /**
     * 将一个对象转化为另外一种对象
     *
     * @param object       对象
     * @param tClass       转化类型
     * @param elementClass 子类型
     * @param <T>          转化类型泛型参数
     * @return object
     */
    public synchronized static <T> T serialize(Object object, Class<T> tClass, Class<?>... elementClass) {
        try {
            String json = mapper.writeValueAsString(object);
            return deserialize(json, tClass, elementClass);
        } catch (Exception exception) {
            log.error("Failed to serialization object because of an error has occurred: {}.", exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    /**
     * Description: 将一个json字符串转换成指定类型
     *
     * @param jsonString json字符串
     * @param clazz      泛型
     * @return 泛型对象
     */
    public synchronized static <T> T deserialize(String jsonString, Class<T> clazz) {
        try {
            return mapper.readValue(jsonString, clazz);
        } catch (IOException exception) {
            log.error("Failed to deserialization object because of an error has occurred: {}.", exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    /**
     * Description: 将一个字节数组转换成指定类型
     *
     * @param bytes 数组
     * @param clazz 泛型
     * @return 泛型对象
     */
    public synchronized static <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try {
            return mapper.readValue(bytes, clazz);
        } catch (IOException exception) {
            log.error("Failed to deserialization object because of an error has occurred: {}.", exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    /**
     * Description: 将一个json字符串转换成指定类型
     *
     * @param jsonString   json字符串
     * @param clazz        泛型
     * @param elementClass 元素泛型
     */
    public synchronized static <T> T deserialize(String jsonString, Class<T> clazz, Class<?>... elementClass) {
        try {
            JavaType javaType = mapper.getTypeFactory().constructParametricType(clazz, elementClass);
            return mapper.readValue(jsonString, javaType);
        } catch (IOException exception) {
            log.error("Failed to deserialization object because of an error has occurred: {}.", exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    /**
     * Description: 将一个json字符串转换成指定类型
     *
     * @param bytes        数组
     * @param clazz        泛型
     * @param elementClass 元素泛型
     */
    public synchronized static <T> T deserialize(byte[] bytes, Class<T> clazz, Class<?>... elementClass) {
        try {
            JavaType javaType = mapper.getTypeFactory().constructParametricType(clazz, elementClass);
            return mapper.readValue(bytes, javaType);
        } catch (IOException exception) {
            log.error("Failed to deserialization object because of an error has occurred: {}.", exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    /**
     * Description: 将一个json字符串转换成指定类型
     *
     * @param jsonString    json字符串
     * @param typeReference TypeReference
     */
    public synchronized static <T> T deserialize(String jsonString, TypeReference<T> typeReference) {
        try {
            return mapper.readValue(jsonString, typeReference);
        } catch (IOException exception) {
            log.error("Failed to deserialization object because of an error has occurred: {}.", exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    /**
     * Description: 将一个字节数组转换成指定类型
     *
     * @param bytes         数组
     * @param typeReference TypeReference
     */
    public synchronized static <T> T deserialize(byte[] bytes, TypeReference<T> typeReference) {
        try {
            return mapper.readValue(bytes, typeReference);
        } catch (IOException exception) {
            log.error("Failed to deserialization object because of an error has occurred: {}.", exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    /**
     * Description: 获取Json字符串中指定的节点
     *
     * @param jsonString json字符串
     * @param nodeName   节点名称
     */
    public synchronized static JsonNode getJsonNode(String jsonString, String nodeName) {
        JsonNode node = toJsonNode(jsonString);
        return node == null ? null : node.get(nodeName);
    }

    /**
     * Description: 将对象转化为JsonNode
     *
     * @param jsonString json字符串
     * @return JsonNode
     */
    public synchronized static JsonNode toJsonNode(String jsonString) {
        try {
            return mapper.readTree(jsonString);
        } catch (IOException exception) {
            log.error("Failed to convert object because of an error has occurred: {}.", exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    public synchronized static JsonNode toJsonNode(byte[] bytes) {
        return toJsonNode(new String(bytes, 0, bytes.length, Charset.defaultCharset()));
    }

    /**
     * Description: 将对象转化为JsonNode
     *
     * @param object object
     * @return JsonNode
     */
    public synchronized static JsonNode toJsonNode(Object object) {
        return mapper.convertValue(object, JsonNode.class);
    }
}
