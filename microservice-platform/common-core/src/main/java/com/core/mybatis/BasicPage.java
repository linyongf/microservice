package com.core.mybatis;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description 分页基类
 * @Author linyf
 * @Date 2022-06-24 16:18
 */
@Data
public abstract class BasicPage implements Serializable {
    /**
     * 当前第几页
     */
    private Integer current = 1;
    /**
     * 页大小
     */
    private Integer size = 10;
}
