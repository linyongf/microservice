package com.core.mybatis;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import static com.baomidou.mybatisplus.core.toolkit.Constants.*;

/**
 * @Description mapper 基类接口
 * @Author linyf
 * @Date 2022-06-24 16:17
 */
public interface BasicMapper<T> extends BaseMapper<T> {
    /**
     * @Description 逻辑删除（自动填充）
     * @param: entity 实体对象
     * @return: int 删除记录条数
     * @Author linyf
     * @Date 2022-06-24 16:18
     */
    int deleteByIdWithFill(T entity);

    /**
     * @Description 根据 entity 条件，逻辑删除
     * @param: entity 实体对象
     * @param: wrapper 条件包装器
     * @return: int 删除记录条数
     * @Author linyf
     * @Date 2022-06-24 16:19
     */
    int logicDelete(@Param(ENTITY) T entity, @Param(WRAPPER)Wrapper<T> wrapper);
}
