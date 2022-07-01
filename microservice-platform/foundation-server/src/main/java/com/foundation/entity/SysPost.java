package com.foundation.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.core.mybatis.BasicDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @Description 岗位表
 * @Author linyf
 * @Date 2022-07-01 16:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_post")
public class SysPost extends BasicDO {
    private static final long serialVersionUID = 1L;

    /**
     * 岗位编码
     */
    @NotBlank(message = "岗位编码不能为空")
    @Size(max = 64, message = "岗位编码长度不能超过64个字符")
    private String code;

    /**
     * 岗位名称
     */
    @NotBlank(message = "岗位名称不能为空")
    @Size(max = 50, message = "岗位名称长度不能超过50个字符")
    private String name;

    /**
     * 岗位排序
     */
    private String sort;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 用户是否存在此岗位标识 默认不存在
     */
    private boolean flag = false;

}
