package com.api.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.core.mybatis.BasicDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @Description 字典类型表
 * @Author linyf
 * @Date 2022-07-01 10:50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_dict_type")
public class SysDictType extends BasicDO {
    private static final long serialVersionUID = 1L;

    /**
     * 字典名称
     */
    @NotBlank(message = "字典名称不能为空")
    @Size(min = 0, max = 100, message = "字典类型名称长度不能超过100个字符")
    private String name;

    /**
     * 字典类型
     */
    @NotBlank(message = "字典类型不能为空")
    @Size(min = 0, max = 100, message = "字典类型类型长度不能超过100个字符")
    @Pattern(regexp = "^[a-z][a-z0-9_]*$", message = "字典类型必须以字母开头，且只能为（小写字母，数字，下滑线）")
    private String type;

    /**
     * 状态（0正常 1停用）
     */
    private String status;
}
