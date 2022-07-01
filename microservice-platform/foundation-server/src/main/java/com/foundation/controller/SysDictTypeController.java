package com.foundation.controller;

import com.api.entity.SysDictType;
import com.core.constants.UserConstants;
import com.foundation.service.ISysDictTypeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description 数据字典信息
 * @Author linyf
 * @Date 2022-07-01 16:16
 */
@RestController
@RequestMapping("/dict/type")
public class SysDictTypeController {
    @Resource
    private ISysDictTypeService dictTypeService;

    @GetMapping("/list")
    public List<SysDictType> list(SysDictType dictType) {
        List<SysDictType> list = dictTypeService.selectDictTypeList(dictType);
        return list;
    }

    @PostMapping("/export")
    public void export(HttpServletResponse response, SysDictType dictType) {
        List<SysDictType> list = dictTypeService.selectDictTypeList(dictType);
//        ExcelUtil<SysDictType> util = new ExcelUtil<SysDictType>(SysDictType.class);
//        util.exportExcel(response, list, "字典类型");
    }

    /**
     * 查询字典类型详细
     */
    @GetMapping(value = "/{dictId}")
    public SysDictType getInfo(@PathVariable Long dictId) {
        return dictTypeService.selectDictTypeById(dictId);
    }

    /**
     * 新增字典类型
     */
    @PostMapping
    public int add(@Validated @RequestBody SysDictType dict) {
        if (UserConstants.NOT_UNIQUE.equals(dictTypeService.checkDictTypeUnique(dict))) {
            throw new RuntimeException("新增字典'" + dict.getName() + "'失败，字典类型已存在");
        }
        return dictTypeService.insertDictType(dict);
    }

    /**
     * 修改字典类型
     */
    @PutMapping
    public int edit(@Validated @RequestBody SysDictType dict) {
        if (UserConstants.NOT_UNIQUE.equals(dictTypeService.checkDictTypeUnique(dict))) {
            throw new RuntimeException("修改字典'" + dict.getName() + "'失败，字典类型已存在");
        }
        return dictTypeService.updateDictType(dict);
    }

    /**
     * 删除字典类型
     */
    @DeleteMapping("/{dictIds}")
    public void remove(@PathVariable Long[] dictIds) {
        dictTypeService.deleteDictTypeByIds(dictIds);
    }

    /**
     * 刷新字典缓存
     */
    @DeleteMapping("/refreshCache")
    public void refreshCache() {
        dictTypeService.resetDictCache();
    }

    /**
     * 获取字典选择框列表
     */
    @GetMapping("/optionselect")
    public List<SysDictType> optionselect() {
        List<SysDictType> dictTypes = dictTypeService.selectDictTypeAll();
        return dictTypes;
    }
}
