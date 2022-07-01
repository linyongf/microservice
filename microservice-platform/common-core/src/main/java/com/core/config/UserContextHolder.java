package com.core.config;

import com.core.constants.BasicConstants;
import com.core.model.SysUser;
import com.core.utils.JsonUtil;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Base64;
import java.util.Map;

/**
 * @Description 当前用户上下文持有器
 * @Author linyf
 * @Date 2022-06-24 16:12
 */
@Slf4j
@NoArgsConstructor
public final class UserContextHolder {

    /**
     * @Description 获取当前用户信息
     * @return: com.api.entity.User
     * @Author linyf
     * @Date 2022-06-24 16:13
     */
    public static SysUser obtainUserInfo() {
        //获取请求头中的用户信息
        String userInfo = WebRequestHolder.obtainRequest().getHeader(BasicConstants.HEADER_JWT_USER);
        if (StringUtils.isEmpty(userInfo)) {
            return null;
        }
        userInfo = new String(Base64.getDecoder().decode(userInfo));
        Map map = JsonUtil.deserialize(userInfo, Map.class);
        SysUser user = new SysUser();
        user.setUsername(map.get(BasicConstants.USER_NAME).toString());
        user.setId( Long.valueOf(map.get(BasicConstants.USER_ID).toString()));
        user.setName(map.get(BasicConstants.NAME).toString());
        user.setMobile(map.get(BasicConstants.MOBILE).toString());
        return user;
    }
}
