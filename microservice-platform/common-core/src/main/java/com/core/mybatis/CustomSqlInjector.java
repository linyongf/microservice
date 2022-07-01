package com.core.mybatis;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.injector.methods.DeleteById;
import com.baomidou.mybatisplus.core.metadata.TableInfo;

import java.util.List;

/**
 * @Description SQL 注入器，逻辑删除数据时更新字段自动填充
 * @Author linyf
 * @Date 2022-06-24 15:57
 */
public class CustomSqlInjector extends DefaultSqlInjector {
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
        methodList.add(new DeleteById());
        methodList.add(new LogicDelete());
        return methodList;
    }
}
