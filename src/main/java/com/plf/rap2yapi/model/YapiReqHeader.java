package com.plf.rap2yapi.model;

import lombok.Data;

/**
 * 请求参数设置，Headers
 * @author penglf3
 */
@Data
public class YapiReqHeader extends YapiReqQuery{

    /**
     * 参数值
     */
    private String value;
}
