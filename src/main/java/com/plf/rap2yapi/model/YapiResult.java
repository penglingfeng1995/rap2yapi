package com.plf.rap2yapi.model;

import lombok.Data;

import java.util.Map;

/**
 * yapi响应
 * @author penglf3
 */
@Data
public class YapiResult {

    private Integer errcode;

    private String errmsg;

    private Map<String,Object> data;
}
