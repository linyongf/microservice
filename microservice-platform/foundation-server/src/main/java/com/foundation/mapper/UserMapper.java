package com.foundation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.core.model.SysUser;

import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseMapper<SysUser> {
    List<Map<String,String>> selectPermissionByUsername(String username);

    List<String> getRoleListByUrl(String url);
}
