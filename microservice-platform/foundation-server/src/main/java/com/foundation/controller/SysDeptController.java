package com.foundation.controller;

import com.core.Result;
import com.core.constants.UserConstants;
import com.core.model.SysDept;
import com.foundation.entity.vo.RoleDeptTreeVo;
import com.foundation.entity.vo.TreeSelect;
import com.foundation.service.ISysDeptService;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;


/**
 * @Description 部门管理
 * @Author linyf
 * @Date 2022-07-01 9:35
 */
@RestController
@RequestMapping("/dept")
public class SysDeptController {

    @Resource
    private ISysDeptService deptService;

    /**
     * 获取部门列表
     */
    @GetMapping("/list")
    public List<SysDept> list(SysDept dept) {
        return deptService.selectDeptList(dept);
    }

    /**
     * 查询部门列表（排除节点）
     */
    @GetMapping("/list/exclude/{deptId}")
    public List<SysDept> excludeChild(@PathVariable(value = "deptId", required = false) Long deptId) {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        Iterator<SysDept> it = depts.iterator();
        while (it.hasNext()) {
            SysDept d = it.next();
            if (d.getId().intValue() == deptId
                    || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), deptId + "")) {
                it.remove();
            }
        }
        return depts;
    }

    /**
     * 根据部门编号获取详细信息
     */
    @GetMapping(value = "/{deptId}")
    public SysDept getInfo(@PathVariable Long deptId) {
        deptService.checkDeptDataScope(deptId);
        return deptService.selectDeptById(deptId);
    }

    /**
     * 获取部门下拉树列表
     */
    @GetMapping("/treeselect")
    public List<TreeSelect> treeselect(SysDept dept) {
        List<SysDept> depts = deptService.selectDeptList(dept);
        return deptService.buildDeptTreeSelect(depts);
    }

    /**
     * 加载对应角色部门列表树
     */
    @GetMapping(value = "/roleDeptTreeselect/{roleId}")
    public RoleDeptTreeVo roleDeptTreeselect(@PathVariable("roleId") Long roleId) {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        RoleDeptTreeVo roleDeptTreeVo = new RoleDeptTreeVo();
        roleDeptTreeVo.setDepts(deptService.buildDeptTreeSelect(depts));
        roleDeptTreeVo.setCheckedKeys(deptService.selectDeptListByRoleId(roleId));
        return roleDeptTreeVo;
    }

    /**
     * 新增部门
     */
    @PostMapping
    public Result add(@Validated @RequestBody SysDept dept) {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) {
            return Result.err("新增部门'" + dept.getName() + "'失败，部门名称已存在");
        }
        return deptService.save(dept) ? Result.ok() : Result.err("新增失败！");
    }

    /**
     * 修改部门
     */
    @PutMapping
    public Result edit(@Validated @RequestBody SysDept dept) {
        Long deptId = dept.getId();
        deptService.checkDeptDataScope(deptId);
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) {
            return Result.err("修改部门'" + dept.getName() + "'失败，部门名称已存在");
        } else if (dept.getParentId().equals(deptId)) {
            return Result.err("修改部门'" + dept.getName() + "'失败，上级部门不能是自己");
        }
        return deptService.updateById(dept) ? Result.ok() : Result.err("修改失败！");
    }

    /**
     * 删除部门
     */
    @DeleteMapping("/{deptId}")
    public Result remove(@PathVariable Long deptId) {
        if (deptService.hasChildByDeptId(deptId)) {
            return Result.err("存在下级部门,不允许删除");
        }
        if (deptService.checkDeptExistUser(deptId)) {
            return Result.err("部门存在用户,不允许删除");
        }
        deptService.checkDeptDataScope(deptId);
        return deptService.deleteDeptById(deptId)>0 ? Result.ok() : Result.err("删除失败！");
    }
}
