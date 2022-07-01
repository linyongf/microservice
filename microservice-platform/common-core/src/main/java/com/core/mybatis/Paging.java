package com.core.mybatis;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 分页对象
 * @Author linyf
 * @Date 2022-06-24 16:28
 */
@Data
public class Paging implements Serializable {
    /**
     * 当前页
     */
    private Integer current;

    /**
     * 页大小
     */
    private Integer size;

    /**
     * 总记录条数
     */
    private Integer total;

    /**
     * 总页数
     */
    private Integer pages;

    /**
     * 分页数据记录
     */
    private List<? extends Serializable> records;

    /**
     * @Description 构建分页结果对象
     * @param: page {@link Page}
     * @return: 分页结果 {@link Paging}
     * @Author linyf
     * @Date 2022-06-24 16:28
     */
    public static Paging of(Page<?> page){
        Paging paging = new Paging();
        paging.setCurrent((int)page.getCurrent());
        paging.setSize((int)page.getSize());
        paging.setTotal((int)page.getTotal());
        paging.setPages((int)page.getPages());
        paging.setRecords((List<? extends Serializable>)page.getRecords());
        return paging;
    }
}
