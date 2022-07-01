package com.core.mybatis;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import static com.baomidou.mybatisplus.core.toolkit.Constants.*;

/**
 * @Description 服务实现基类
 * @Author linyf
 * @Date 2022-06-24 16:18
 */
public abstract class BasicServiceImpl<M extends BasicMapper<T>, T> extends ServiceImpl<M, T> implements IBasicService<T> {
    @Autowired
    protected M basicMapper;

    /**
     * @Description 逻辑删除（自动填充）
     * @param: entity 实体对象
     * @return: true-删除成功；false-删除失败
     * @Author linyf
     * @Date 2022-06-24 16:19
     */
    public boolean logicRemoveById(T entity){
        return SqlHelper.retBool(baseMapper.deleteByIdWithFill(entity));
    }

    /**
     * @Description  根据 entity 条件，逻辑删除
     * @param: entity 实体对象
     * @param: updateWrapper 实体对象封装操作类（@link com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper）
     * @return:  true-删除成功；false-删除失败
     * @Author linyf
     * @Date 2022-06-24 16:20
     */
    public boolean logicRemove(@Param(ENTITY) T entity, @Param(WRAPPER) Wrapper<T> updateWrapper){
        return SqlHelper.retBool(baseMapper.logicDelete(entity, updateWrapper));
    }
}
