package com.foundation.controller;

import com.api.entity.SysDictData;
import com.foundation.service.ISysDictDataService;
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
@RequestMapping("/dict/data")
public class SysDictDataController {

    @Resource
    private ISysDictDataService dictDataService;

    @Resource
    private ISysDictTypeService dictTypeService;

    @GetMapping("/list")
    public List<SysDictData> list(SysDictData dictData) {
//        startPage();
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
//        return getDataTable(list);
        return list;
    }

    @PostMapping("/export")
    public void export(HttpServletResponse response, SysDictData dictData) {
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
//        ExcelUtil<SysDictData> util = new ExcelUtil<SysDictData>(SysDictData.class);
//        util.exportExcel(response, list, "字典数据");
    }

    /**
     * 查询字典数据详细
     */
    @GetMapping(value = "/{dictCode}")
    public SysDictData getInfo(@PathVariable Long dictCode) {
        return dictDataService.selectDictDataById(dictCode);
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping(value = "/type/{dictType}")
    public List<SysDictData> dictType(@PathVariable String dictType) {
        return dictTypeService.selectDictDataByType(dictType);
    }

    /**
     * 新增字典类型
     */
    @PostMapping
    public int add(@Validated @RequestBody SysDictData dict) {
        return dictDataService.insertDictData(dict);
    }

    /**
     * 修改保存字典类型
     */
    @PutMapping
    public int edit(@Validated @RequestBody SysDictData dict) {
        return dictDataService.updateDictData(dict);
    }

    /**
     * 删除字典类型
     */
    @DeleteMapping("/{dictCodes}")
    public void remove(@PathVariable Long[] dictCodes) {
        dictDataService.deleteDictDataByIds(dictCodes);
    }
}
