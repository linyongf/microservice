package com.core.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.core.config.UserContextHolder;
import com.core.model.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.core.constants.BasicConstants.SYSTEM_EN;
import static com.core.constants.BasicConstants.SYSTEM_ZH;

/**
 * @Description 插入/修改 自动填充处理器
 * @Author linyf
 * @Date 2022-06-24 16:28
 */
@Slf4j
public class MybatisAutoFillHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
      if(!(metaObject.getOriginalObject() instanceof BasicDO)){
        log.warn("insertFill 慎用非 POJO 对象传参");
        return;
      }

        SysUser user = UserContextHolder.obtainUserInfo();
        String currentUsername = SYSTEM_EN;
        String currentName = SYSTEM_ZH;
        if(Objects.nonNull(user)){
            currentUsername = user.getUsername();
            currentName = user.getName();
        }

        LocalDateTime now = LocalDateTime.now();

        this.strictInsertFill(metaObject, "deleted", Integer.class, 0);
        this.strictInsertFill(metaObject, "createUser", String.class, currentUsername);
        this.strictInsertFill(metaObject, "createUserName", String.class, currentName);
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, now);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if(!(metaObject.getOriginalObject() instanceof BasicDO)){
            log.warn("insertFill 慎用非 POJO 对象传参");
            return;
        }

        SysUser user = UserContextHolder.obtainUserInfo();
        String currentUsername = SYSTEM_EN;
        if(Objects.nonNull(user)){
            currentUsername = user.getUsername();
        }

        this.strictInsertFill(metaObject, "updateUser", String.class, currentUsername);
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
}
