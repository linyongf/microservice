package com.foundation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.core.model.SysRole;
import com.foundation.entity.SysUserPost;

import java.util.List;

/**
 * @Description 用户与岗位关联表 数据层
 * @Author linyf
 * @Date 2022-07-01 16:40
 */
public interface SysUserPostMapper extends BaseMapper<SysRole> {
    /**
     * 通过用户ID删除用户和岗位关联
     *
     * @param userId 用户ID
     * @return 结果
     */
    int deleteUserPostByUserId(Long userId);

    /**
     * 通过岗位ID查询岗位使用数量
     *
     * @param postId 岗位ID
     * @return 结果
     */
    int countUserPostById(Long postId);

    /**
     * 批量删除用户和岗位关联
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteUserPost(Long[] ids);

    /**
     * 批量新增用户岗位信息
     *
     * @param userPostList 用户角色列表
     * @return 结果
     */
    int batchUserPost(List<SysUserPost> userPostList);
}
