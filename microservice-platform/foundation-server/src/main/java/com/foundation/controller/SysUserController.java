package com.foundation.controller;

import com.core.config.UserContextHolder;
import com.core.constants.UserConstants;
import com.core.model.SysUser;
import com.foundation.service.ISysPostService;
import com.foundation.service.ISysRoleService;
import com.foundation.service.ISysUserService;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @Description 用户信息
 * @Author linyf
 * @Date 2022-07-01 16:15
 */
@RestController
@RequestMapping("/user")
public class SysUserController {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysPostService postService;

//    @Autowired
//    private ISysPermissionService permissionService;

//    @Autowired
//    private ISysConfigService configService;

    /**
     * 获取用户列表
     */
    @GetMapping("/list")
    public List<SysUser> list(SysUser user) {
        List<SysUser> list = userService.selectUserList(user);
        return list;
    }

    @PostMapping("/export")
    public void export(HttpServletResponse response, SysUser user) {
        List<SysUser> list = userService.selectUserList(user);
    }

//    @PostMapping("/importData")
//    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
//        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
//        List<SysUser> userList = util.importExcel(file.getInputStream());
//        String operName = SecurityUtils.getUsername();
//        String message = userService.importUser(userList, updateSupport, operName);
//        return AjaxResult.success(message);
//    }

    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) throws IOException {
//        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
//        util.importTemplateExcel(response, "用户数据");
    }

    /**
     * 获取当前用户信息
     */
//    @GetMapping("/info/{username}")
//    public R<LoginUser> info(@PathVariable("username") String username) {
//        SysUser sysUser = userService.selectUserByUserName(username);
//        if (Objects.isNull(sysUser)) {
//            return R.fail("用户名或密码错误");
//        }
//        // 角色集合
//        Set<String> roles = permissionService.getRolePermission(sysUser.getUserId());
//        // 权限集合
//        Set<String> permissions = permissionService.getMenuPermission(sysUser.getUserId());
//        LoginUser sysUserVo = new LoginUser();
//        sysUserVo.setSysUser(sysUser);
//        sysUserVo.setRoles(roles);
//        sysUserVo.setPermissions(permissions);
//        return R.ok(sysUserVo);
//    }

    /**
     * 注册用户信息
     */
//    @InnerAuth
//    @PostMapping("/register")
//    public R<Boolean> register(@RequestBody SysUser sysUser) {
//        String username = sysUser.getUserName();
//        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser")))) {
//            return R.fail("当前系统没有开启注册功能！");
//        }
//        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(username))) {
//            return R.fail("保存用户'" + username + "'失败，注册账号已存在");
//        }
//        return R.ok(userService.registerUser(sysUser));
//    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
//    @GetMapping("getInfo")
//    public AjaxResult getInfo() {
//        Long userId = SecurityUtils.getUserId();
//        // 角色集合
//        Set<String> roles = permissionService.getRolePermission(userId);
//        // 权限集合
//        Set<String> permissions = permissionService.getMenuPermission(userId);
//        AjaxResult ajax = AjaxResult.success();
//        ajax.put("user", userService.selectUserById(userId));
//        ajax.put("roles", roles);
//        ajax.put("permissions", permissions);
//        return ajax;
//    }

    /**
     * 根据用户编号获取详细信息
     */
//    @RequiresPermissions("system:user:query")
//    @GetMapping(value = {"/", "/{userId}"})
//    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) Long userId) {
//        userService.checkUserDataScope(userId);
//        AjaxResult ajax = AjaxResult.success();
//        List<SysRole> roles = roleService.selectRoleAll();
//        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
//        ajax.put("posts", postService.selectPostAll());
//        if (StringUtils.isNotNull(userId)) {
//            SysUser sysUser = userService.selectUserById(userId);
//            ajax.put(AjaxResult.DATA_TAG, sysUser);
//            ajax.put("postIds", postService.selectPostListByUserId(userId));
//            ajax.put("roleIds", sysUser.getRoles().stream().map(SysRole::getRoleId).collect(Collectors.toList()));
//        }
//        return ajax;
//    }

    /**
     * 新增用户
     */
    @PostMapping
    public int add(@Validated @RequestBody SysUser user) {
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user.getUsername()))) {
            throw new RuntimeException("新增用户'" + user.getUsername() + "'失败，登录账号已存在");
        } else if (!StringUtils.isEmpty(user.getMobile())
                && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            throw new RuntimeException("新增用户'" + user.getUsername() + "'失败，手机号码已存在");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        return userService.insertUser(user);
    }

    /**
     * 修改用户
     */
    @PutMapping
    public int edit(@Validated @RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getId());
        if (!StringUtils.isEmpty(user.getMobile())
                && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            throw new RuntimeException("修改用户'" + user.getUsername() + "'失败，手机号码已存在");
        }
        return userService.updateUser(user);
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{userIds}")
    public int remove(@PathVariable Long[] userIds) {
        if (ArrayUtils.contains(userIds, UserContextHolder.obtainUserInfo().getId())) {
            throw new RuntimeException("当前用户不能删除");
        }
        return userService.deleteUserByIds(userIds);
    }

    /**
     * 重置密码
     */
    @PutMapping("/resetPwd")
    public int resetPwd(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getId());
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return userService.resetPwd(user);
    }

    /**
     * 状态修改
     */
    @PutMapping("/changeStatus")
    public int changeStatus(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getId());
        return userService.updateUserStatus(user);
    }

    /**
     * 根据用户编号获取授权角色
     */
//    @GetMapping("/authRole/{userId}")
//    public AjaxResult authRole(@PathVariable("userId") Long userId) {
//        AjaxResult ajax = AjaxResult.success();
//        SysUser user = userService.selectUserById(userId);
//        List<SysRole> roles = roleService.selectRolesByUserId(userId);
//        ajax.put("user", user);
//        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
//        return ajax;
//    }

    /**
     * 用户授权角色
     */
//    @RequiresPermissions("system:user:edit")
//    @Log(title = "用户管理", businessType = BusinessType.GRANT)
//    @PutMapping("/authRole")
//    public AjaxResult insertAuthRole(Long userId, Long[] roleIds) {
//        userService.checkUserDataScope(userId);
//        userService.insertUserAuth(userId, roleIds);
//        return success();
//    }

//    @GetMapping("/{username}")
//    @ApiOperation("查询用户")
//    public User getUserByUsername(@PathVariable String username) {
//        User user = userService.getUserByUsername(username);
//        return user;
//    }

//    @GetMapping("/role-list")
//    List<String> getRoleListByUrl(@RequestParam(name = "url") String url) {
//        return userService.getRoleListByUrl(url);
//    }
//
//    @GetMapping("/curUser")
//    public User getUser(HttpServletRequest request) {
//        return UserContextHolder.obtainUserInfo();
//    }
}
