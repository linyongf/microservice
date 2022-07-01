package com.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 文件信息
 * @Author linyf
 * @Date 2022-07-01 10:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysFile {
    /**
     * 文件名称
     */
    private String name;

    /**
     * 文件地址
     */
    private String url;
}
