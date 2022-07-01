package com.foundation.controller;

import com.core.constants.UserConstants;
import com.core.model.SysRole;
import com.core.model.SysUser;
import com.foundation.entity.SysUserRole;
import com.foundation.service.ISysRoleService;
import com.foundation.service.ISysUserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description 角色信息
 * @Author linyf
 * @Date 2022-07-01 16:15
 */
@RestController
@RequestMapping("/role")
public class SysRoleController {
    @Resource
    private ISysRoleService roleService;

    @Resource
    private ISysUserService userService;

    @GetMapping("/list")
    public List<SysRole> list(SysRole role) {
        List<SysRole> list = roleService.selectRoleList(role);
        return list;
    }

    @PostMapping("/export")
    public void export(HttpServletResponse response, SysRole role) {
        List<SysRole> list = roleService.selectRoleList(role);
//        ExcelUtil<SysRole> util = new ExcelUtil<SysRole>(SysRole.class);
//        util.exportExcel(response, list, "角色数据");
    }

    /**
     * 根据角色编号获取详细信息
     */
    @GetMapping(value = "/{roleId}")
    public SysRole getInfo(@PathVariable Long roleId) {
        roleService.checkRoleDataScope(roleId);
        return roleService.selectRoleById(roleId);
    }

    /**
     * 新增角色
     */
    @PostMapping
    public int add(@Validated @RequestBody SysRole role) {
        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) {
            throw new RuntimeException("新增角色'" + role.getName() + "'失败，角色名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role))) {
            throw new RuntimeException("新增角色'" + role.getName() + "'失败，角色权限已存在");
        }
        return roleService.insertRole(role);

    }

    /**
     * 修改保存角色
     */
    @PutMapping
    public int edit(@Validated @RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getId());
        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) {
            throw new RuntimeException("修改角色'" + role.getName() + "'失败，角色名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role))) {
            throw new RuntimeException("修改角色'" + role.getName() + "'失败，角色权限已存在");
        }
        return roleService.updateRole(role);
    }

    /**
     * 修改保存数据权限
     */
    @PutMapping("/dataScope")
    public int dataScope(@RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getId());
        return roleService.authDataScope(role);
    }

    /**
     * 状态修改
     */
    @PutMapping("/changeStatus")
    public int changeStatus(@RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getId());
        return roleService.updateRoleStatus(role);
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{roleIds}")
    public int remove(@PathVariable Long[] roleIds) {
        return roleService.deleteRoleByIds(roleIds);
    }

    /**
     * 获取角色选择框列表
     */
    @GetMapping("/optionselect")
    public List<SysRole> optionselect() {
        return roleService.selectRoleAll();
    }

    /**
     * 查询已分配用户角色列表
     */
    @GetMapping("/authUser/allocatedList")
    public List<SysUser> allocatedList(SysUser user) {
        List<SysUser> list = userService.selectAllocatedList(user);
        return list;
    }

    /**
     * 查询未分配用户角色列表
     */
    @GetMapping("/authUser/unallocatedList")
    public List<SysUser> unallocatedList(SysUser user) {
        List<SysUser> list = userService.selectUnallocatedList(user);
        return list;
    }

    /**
     * 取消授权用户
     */
    @PutMapping("/authUser/cancel")
    public int cancelAuthUser(@RequestBody SysUserRole userRole) {
        return roleService.deleteAuthUser(userRole);
    }

    /**
     * 批量取消授权用户
     */
    @PutMapping("/authUser/cancelAll")
    public int cancelAuthUserAll(Long roleId, Long[] userIds) {
        return roleService.deleteAuthUsers(roleId, userIds);
    }

    /**
     * 批量选择用户授权
     */
    @PutMapping("/authUser/selectAll")
    public int selectAuthUserAll(Long roleId, Long[] userIds) {
        roleService.checkRoleDataScope(roleId);
        return roleService.insertAuthUsers(roleId, userIds);
    }
}