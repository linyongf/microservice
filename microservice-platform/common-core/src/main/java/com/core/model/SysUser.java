package com.core.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.core.mybatis.BasicDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;

/**
 * @Description 用户对象
 * @Author linyf
 * @Date 2022-07-01 10:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user")
public class SysUser extends BasicDO implements UserDetails {
    private static final long serialVersionUID = 1L;

    /**
     * 部门ID
     */
    private Long deptId;

    //    @Xss(message = "用户账号不能包含脚本字符")
    @NotBlank(message = "用户账号不能为空")
    @Size(max = 30, message = "用户账号长度不能超过30个字符")
    private String username;

    /**
     * 用户昵称
     */
//    @Xss(message = "用户昵称不能包含脚本字符")
    @Size(max = 30, message = "用户昵称长度不能超过30个字符")
    private String nickName;

    /**
     * 手机号码
     */
    @Size(max = 11, message = "手机号码长度不能超过11个字符")
    private String mobile;

    /**
     * 用户性别
     */
    private String sex;

    /**
     * 密码
     */
    private String password;

    /**
     * 帐号状态（0正常 1停用）
     */
    private String status;

    private SysDept dept;

    /**
     * 角色对象
     */
    private List<SysRole> roles;

    /**
     * 角色组
     */
    private Long[] roleIds;

    /**
     * 岗位组
     */
    private Long[] postIds;

    /**
     * 角色ID
     */
    private Long roleId;

    private String name;

    private String idCard;

    //权限+角色集合
    @TableField(exist = false)
    private Collection<UserAuthority> authorities;

    @Override
    public Collection<UserAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    // 账户是否未过期
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 账户是否未被锁
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @NoArgsConstructor
    @Data
    public static class UserAuthority implements GrantedAuthority {
        private String authority;
        private Map<String, Object> attributes;

        public UserAuthority(Map<String, Object> attributes) {
            this("ROLE_USER", attributes);
        }

        public UserAuthority(String authority, Map<String, Object> attributes) {
            Assert.hasText(authority, "authority cannot be empty");
            Assert.notEmpty(attributes, "attributes cannot be empty");
            this.authority = authority;
            this.attributes = Collections.unmodifiableMap(new LinkedHashMap(attributes));
        }

        public String getAuthority() {
            List<String> roles = new ArrayList<>();
            for (Map.Entry<String, Object> entry : attributes.entrySet()) {
                if (entry.getKey().equals("role")) {
                    roles.add(entry.getValue().toString());
                }
            }
            return StringUtils.arrayToCommaDelimitedString(roles.toArray());
        }
    }
}
