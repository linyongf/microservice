package com.foundation.controller;

import com.core.config.UserContextHolder;
import com.core.constants.UserConstants;
import com.foundation.entity.SysMenu;
import com.foundation.entity.vo.RoleDeptTreeVo;
import com.foundation.entity.vo.RouterVo;
import com.foundation.entity.vo.TreeSelect;
import com.foundation.service.ISysMenuService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description 菜单信息
 * @Author linyf
 * @Date 2022-07-01 16:15
 */
@RestController
@RequestMapping("/menu")
public class SysMenuController {

    @Resource
    private ISysMenuService menuService;

    /**
     * 获取菜单列表
     */
    @GetMapping("/list")
    public List<SysMenu> list(SysMenu menu) {
        Long userId = UserContextHolder.obtainUserInfo().getId();
        List<SysMenu> menus = menuService.selectMenuList(menu, userId);
        return menus;
    }

    /**
     * 根据菜单编号获取详细信息
     */
    @GetMapping(value = "/{menuId}")
    public SysMenu getInfo(@PathVariable Long menuId) {
        return menuService.selectMenuById(menuId);
    }

    /**
     * 获取菜单下拉树列表
     */
    @GetMapping("/treeselect")
    public List<TreeSelect> treeselect(SysMenu menu) {
        Long userId = UserContextHolder.obtainUserInfo().getId();
        List<SysMenu> menus = menuService.selectMenuList(menu, userId);
        return menuService.buildMenuTreeSelect(menus);
    }

    /**
     * 加载对应角色菜单列表树
     */
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public RoleDeptTreeVo roleMenuTreeselect(@PathVariable("roleId") Long roleId) {
        Long userId = UserContextHolder.obtainUserInfo().getId();
        List<SysMenu> menus = menuService.selectMenuList(userId);
        RoleDeptTreeVo roleDeptTreeVo = new RoleDeptTreeVo();
        roleDeptTreeVo.setCheckedKeys(menuService.selectMenuListByRoleId(roleId));
        roleDeptTreeVo.setDepts(menuService.buildMenuTreeSelect(menus));
        return roleDeptTreeVo;
    }

    /**
     * 新增菜单
     */
    @PostMapping
    public int add(@Validated @RequestBody SysMenu menu) {
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu))) {
            throw new RuntimeException("新增菜单'" + menu.getName() + "'失败，菜单名称已存在");
        } else if (UserConstants.YES_FRAME.equals(menu.getIsFrame()) && !(menu.getPath().startsWith("http") || menu.getPath().startsWith("https"))) {
            throw new RuntimeException("新增菜单'" + menu.getName() + "'失败，地址必须以http(s)://开头");
        }
        return menuService.insertMenu(menu);
    }

    /**
     * 修改菜单
     */
    @PutMapping
    public int edit(@Validated @RequestBody SysMenu menu) {
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu))) {
            throw new RuntimeException("修改菜单'" + menu.getName() + "'失败，菜单名称已存在");
        } else if (UserConstants.YES_FRAME.equals(menu.getIsFrame()) && !(menu.getPath().startsWith("http") || menu.getPath().startsWith("https"))) {
            throw new RuntimeException("修改菜单'" + menu.getName() + "'失败，地址必须以http(s)://开头");
        } else if (menu.getId().equals(menu.getParentId())) {
            throw new RuntimeException("修改菜单'" + menu.getName() + "'失败，上级菜单不能选择自己");
        }
        return menuService.updateMenu(menu);
    }

    /**
     * 删除菜单
     */
    @DeleteMapping("/{menuId}")
    public int remove(@PathVariable("menuId") Long menuId) {
        if (menuService.hasChildByMenuId(menuId)) {
            throw new RuntimeException("存在子菜单,不允许删除");
        }
        if (menuService.checkMenuExistRole(menuId)) {
            throw new RuntimeException("菜单已分配,不允许删除");
        }
        return menuService.deleteMenuById(menuId);
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public List<RouterVo> getRouters() {
        Long userId = UserContextHolder.obtainUserInfo().getId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return menuService.buildMenus(menus);
    }
}