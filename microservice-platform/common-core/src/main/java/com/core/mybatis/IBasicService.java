package com.core.mybatis;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import static com.baomidou.mybatisplus.core.toolkit.Constants.ENTITY;
import static com.baomidou.mybatisplus.core.toolkit.Constants.WRAPPER;

/**
 * @Description 服务接口基类接口
 * @param <T>
 * @Author linyf
 * @Date 2022-06-24 16:20
 */
public interface IBasicService<T> extends IService<T> {
    /**
     * @Description  逻辑删除（自动填充）
     * @param: entity 实体对象
     * @return: true-删除成功；false-删除失败
     * @Author linyf
     * @Date 2022-06-24 16:21
     */
    boolean logicRemoveById(T entity);

    /**
     * 根据 entity 条件，逻辑删除
     * @param entity 实体对象
     * @param updateWrapper 实体对象封装操作类（@link com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper）
     * @return true-删除成功；false-删除失败
     */
    boolean logicRemove(@Param(ENTITY) T entity, @Param(WRAPPER) Wrapper<T> updateWrapper);
}
