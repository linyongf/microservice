package com.foundation.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 用户和岗位关联
 * @Author linyf
 * @Date 2022-07-01 16:27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user_post")
public class SysUserPost {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 岗位ID
     */
    private Long postId;
}
