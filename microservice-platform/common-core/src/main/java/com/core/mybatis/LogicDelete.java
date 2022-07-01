package com.core.mybatis;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description 逻辑删除
 * @Author linyf
 * @Date 2022-06-24 16:27
 */
public class LogicDelete extends AbstractMethod {

    public LogicDelete(){
        super("logicDelete");
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo){
        String sql;
        SqlMethod sqlMethod = SqlMethod.LOGIC_DELETE;
        if(tableInfo.isWithLogicDelete()){
            List<TableFieldInfo> fieldInfos = tableInfo.getFieldList().stream()
                    .filter(TableFieldInfo::isWithUpdateFill)
                    .collect(Collectors.toList());

            if(!CollectionUtils.isEmpty(fieldInfos)){
                String sqlSet = "SET" + fieldInfos.stream().map(i -> i.getSqlSet(Constants.ENTITY)).collect(Collectors.joining(EMPTY))
                        + tableInfo.getLogicDeleteSql(false, false);
                sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), sqlSet,
                        sqlWhereEntityWrapper(true, tableInfo), sqlComment());
            }else {
                sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), sqlLogicSet(tableInfo),
                        sqlWhereEntityWrapper(true, tableInfo), sqlComment());
            }
        }else {
            sqlMethod = SqlMethod.DELETE;
            sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(),
                    sqlWhereEntityWrapper(true, tableInfo), sqlComment());
        }
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return addUpdateMappedStatement(mapperClass, modelClass, super.methodName, sqlSource);
    }

}
