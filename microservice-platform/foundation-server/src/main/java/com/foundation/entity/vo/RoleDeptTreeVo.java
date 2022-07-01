package com.foundation.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * @Description 角色部门列表树
 * @Author linyf
 * @Date 2022-07-01 11:22
 */
@Data
public class RoleDeptTreeVo {
    private List<Long> checkedKeys;
    private List<TreeSelect> depts;
}
