package com.plf.rap2yapi.model;

import lombok.Data;

/**
 * 请求参数设置，Query
 * @author penglf3
 */
@Data
public class YapiReqQuery {

    /**
     * 参数备注，如 状态码
     */
    private String desc;

    /**
     * 参数示例
     */
    private String example;

    /**
     * 参数名称，如code
     */
    private String name;

    /**
     * 是否必须， "0" 非必需， "1" 必需
     */
    private String required = "0";
}
