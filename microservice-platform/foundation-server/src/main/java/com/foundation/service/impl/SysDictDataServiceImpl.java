package com.foundation.service.impl;

import com.api.entity.SysDictData;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.foundation.mapper.SysDictDataMapper;
import com.foundation.service.ISysDictDataService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description 字典 业务层处理
 * @Author linyf
 * @Date 2022-07-01 16:47
 */
@Service
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataMapper, SysDictData> implements ISysDictDataService {
    /**
     * 根据条件分页查询字典数据
     *
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */
    @Override
    public List<SysDictData> selectDictDataList(SysDictData dictData) {
        return this.baseMapper.selectDictDataList(dictData);
    }

    /**
     * 根据字典类型和字典键值查询字典数据信息
     *
     * @param dictType  字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    @Override
    public String selectDictLabel(String dictType, String dictValue) {
        return this.baseMapper.selectDictLabel(dictType, dictValue);
    }

    /**
     * 根据字典数据ID查询信息
     *
     * @param dictCode 字典数据ID
     * @return 字典数据
     */
    @Override
    public SysDictData selectDictDataById(Long dictCode) {
        return this.baseMapper.selectDictDataById(dictCode);
    }

    /**
     * 批量删除字典数据信息
     *
     * @param dictCodes 需要删除的字典数据ID
     */
    @Override
    public void deleteDictDataByIds(Long[] dictCodes) {
        for (Long dictCode : dictCodes) {
            SysDictData data = selectDictDataById(dictCode);
            this.baseMapper.deleteDictDataById(dictCode);
            List<SysDictData> dictDatas = this.baseMapper.selectDictDataByType(data.getDictType());
//            DictUtils.setDictCache(data.getDictType(), dictDatas);
        }
    }

    /**
     * 新增保存字典数据信息
     *
     * @param data 字典数据信息
     * @return 结果
     */
    @Override
    public int insertDictData(SysDictData data) {
        int row = this.baseMapper.insertDictData(data);
        if (row > 0) {
            List<SysDictData> dictDatas = this.baseMapper.selectDictDataByType(data.getDictType());
//            DictUtils.setDictCache(data.getDictType(), dictDatas);
        }
        return row;
    }

    /**
     * 修改保存字典数据信息
     *
     * @param data 字典数据信息
     * @return 结果
     */
    @Override
    public int updateDictData(SysDictData data) {
        int row = this.baseMapper.updateDictData(data);
        if (row > 0) {
            List<SysDictData> dictDatas = this.baseMapper.selectDictDataByType(data.getDictType());
//            DictUtils.setDictCache(data.getDictType(), dictDatas);
        }
        return row;
    }
}
