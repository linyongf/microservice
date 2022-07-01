package com.foundation.controller;

import com.core.config.UserContextHolder;
import com.core.model.SysUser;
import com.foundation.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.token.TokenService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 个人信息 业务处理
 * @Author linyf
 * @Date 2022-07-01 16:15
 */
@RestController
@RequestMapping("/user/profile")
public class SysProfileController {

    @Autowired
    private ISysUserService userService;

    @Autowired
    private TokenService tokenService;

//    @Autowired
//    private RemoteFileService remoteFileService;

    /**
     * 个人信息
     */
    @GetMapping
    public SysUser profile() {
        String username = UserContextHolder.obtainUserInfo().getUsername();
        SysUser user = userService.selectUserByUserName(username);
//        ajax.put("roleGroup", userService.selectUserRoleGroup(username));
//        ajax.put("postGroup", userService.selectUserPostGroup(username));
        return user;
    }

    /**
     * 修改用户
     */
//    @PutMapping
//    public AjaxResult updateProfile(@RequestBody SysUser user) {
//        LoginUser loginUser = SecurityUtils.getLoginUser();
//        SysUser sysUser = loginUser.getSysUser();
//        user.setUserName(sysUser.getUserName());
//        if (StringUtils.isNotEmpty(user.getPhonenumber())
//                && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
//            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
//        } else if (StringUtils.isNotEmpty(user.getEmail())
//                && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
//            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
//        }
//        user.setUserId(sysUser.getUserId());
//        user.setPassword(null);
//        if (userService.updateUserProfile(user) > 0) {
//            // 更新缓存用户信息
//            loginUser.getSysUser().setNickName(user.getNickName());
//            loginUser.getSysUser().setPhonenumber(user.getPhonenumber());
//            loginUser.getSysUser().setEmail(user.getEmail());
//            loginUser.getSysUser().setSex(user.getSex());
//            tokenService.setLoginUser(loginUser);
//            return AjaxResult.success();
//        }
//        return AjaxResult.error("修改个人信息异常，请联系管理员");
//    }

    /**
     * 重置密码
     */
//    @PutMapping("/updatePwd")
//    public AjaxResult updatePwd(String oldPassword, String newPassword) {
//        String username = SecurityUtils.getUsername();
//        SysUser user = userService.selectUserByUserName(username);
//        String password = user.getPassword();
//        if (!SecurityUtils.matchesPassword(oldPassword, password)) {
//            return AjaxResult.error("修改密码失败，旧密码错误");
//        }
//        if (SecurityUtils.matchesPassword(newPassword, password)) {
//            return AjaxResult.error("新密码不能与旧密码相同");
//        }
//        if (userService.resetUserPwd(username, SecurityUtils.encryptPassword(newPassword)) > 0) {
//            // 更新缓存用户密码
//            LoginUser loginUser = SecurityUtils.getLoginUser();
//            loginUser.getSysUser().setPassword(SecurityUtils.encryptPassword(newPassword));
//            tokenService.setLoginUser(loginUser);
//            return AjaxResult.success();
//        }
//        return AjaxResult.error("修改密码异常，请联系管理员");
//    }

    /**
     * 头像上传
     */
//    @Log(title = "用户头像", businessType = BusinessType.UPDATE)
//    @PostMapping("/avatar")
//    public AjaxResult avatar(@RequestParam("avatarfile") MultipartFile file) {
//        if (!file.isEmpty()) {
//            LoginUser loginUser = SecurityUtils.getLoginUser();
//            String extension = FileTypeUtils.getExtension(file);
//            if (!StringUtils.equalsAnyIgnoreCase(extension, MimeTypeUtils.IMAGE_EXTENSION)) {
//                return AjaxResult.error("文件格式不正确，请上传" + Arrays.toString(MimeTypeUtils.IMAGE_EXTENSION) + "格式");
//            }
//            R<SysFile> fileResult = remoteFileService.upload(file);
//            if (StringUtils.isNull(fileResult) || StringUtils.isNull(fileResult.getData())) {
//                return AjaxResult.error("文件服务异常，请联系管理员");
//            }
//            String url = fileResult.getData().getUrl();
//            if (userService.updateUserAvatar(loginUser.getUsername(), url)) {
//                AjaxResult ajax = AjaxResult.success();
//                ajax.put("imgUrl", url);
//                // 更新缓存用户头像
//                loginUser.getSysUser().setAvatar(url);
//                tokenService.setLoginUser(loginUser);
//                return ajax;
//            }
//        }
//        return AjaxResult.error("上传图片异常，请联系管理员");
//    }
}
