package com.foundation.controller;

import com.core.constants.UserConstants;
import com.foundation.entity.SysPost;
import com.foundation.service.ISysPostService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description 岗位信息操作处理
 * @Author linyf
 * @Date 2022-07-01 16:15
 */
@RestController
@RequestMapping("/post")
public class SysPostController {

    @Resource
    private ISysPostService postService;

    /**
     * 获取岗位列表
     */
    @GetMapping("/list")
    public List<SysPost> list(SysPost post) {
        List<SysPost> list = postService.selectPostList(post);
        return list;
    }

    @PostMapping("/export")
    public void export(HttpServletResponse response, SysPost post) {
        List<SysPost> list = postService.selectPostList(post);
//        ExcelUtil<SysPost> util = new ExcelUtil<SysPost>(SysPost.class);
//        util.exportExcel(response, list, "岗位数据");
    }

    /**
     * 根据岗位编号获取详细信息
     */
    @GetMapping(value = "/{postId}")
    public SysPost getInfo(@PathVariable Long postId) {
        return postService.selectPostById(postId);
    }

    /**
     * 新增岗位
     */
    @PostMapping
    public int add(@Validated @RequestBody SysPost post) {
        if (UserConstants.NOT_UNIQUE.equals(postService.checkPostNameUnique(post))) {
            throw new RuntimeException("新增岗位'" + post.getName() + "'失败，岗位名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(postService.checkPostCodeUnique(post))) {
            throw new RuntimeException("新增岗位'" + post.getName() + "'失败，岗位编码已存在");
        }
        return postService.insertPost(post);
    }

    /**
     * 修改岗位
     */
    @PutMapping
    public int edit(@Validated @RequestBody SysPost post) {
        if (UserConstants.NOT_UNIQUE.equals(postService.checkPostNameUnique(post))) {
            throw new RuntimeException("修改岗位'" + post.getName() + "'失败，岗位名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(postService.checkPostCodeUnique(post))) {
            throw new RuntimeException("修改岗位'" + post.getName() + "'失败，岗位编码已存在");
        }
        return postService.updatePost(post);
    }

    /**
     * 删除岗位
     */
    @DeleteMapping("/{postIds}")
    public int remove(@PathVariable Long[] postIds) {
        return postService.deletePostByIds(postIds);
    }

    /**
     * 获取岗位选择框列表
     */
    @GetMapping("/optionselect")
    public List<SysPost> optionselect() {
        return postService.selectPostAll();
    }
}
