package com.foundation.entity.vo;

import com.core.model.SysDept;
import lombok.Data;
import sun.reflect.generics.tree.Tree;

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
