package com.core.mybatis;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Description 实体基类
 * @Author linyf
 * @Date 2022-06-24 16:16
 */
@Data
public abstract class BasicDO implements Serializable {

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    @TableLogic(value = "0", delval = "1")
    @TableField(select = false, fill = FieldFill.INSERT)
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(fill = FieldFill.INSERT)
    private String createUserName;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

}
