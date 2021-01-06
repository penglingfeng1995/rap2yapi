package com.plf.rap2yapi.model;

import lombok.Data;

import java.util.List;

/**
 * yapi的接口分类
 * @author penglf3
 */
@Data
public class YapiModule {

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类备注，可以为null
     */
    private String desc;

    /**
     * 创建时间，10位时间戳
     */
    private Long add_time;

    /**
     * 修改时间
     */
    private Long up_time;

    /**
     * 排序，默认为0
     */
    private Integer index = 0;

    /**
     * 接口列表
     */
    private List<YapiInterface> list;
}
