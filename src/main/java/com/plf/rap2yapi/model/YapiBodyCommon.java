package com.plf.rap2yapi.model;

import lombok.Data;

/**
 * @author penglf3
 */
@Data
public class YapiBodyCommon extends YapiBody{

    /**
     * 商品的名称和备注
     */
    private String description;
}
