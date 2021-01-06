package com.plf.rap2yapi.model;

import lombok.Data;

/**
 *
 * @author penglf3
 */
@Data
public class YapiBodyArray extends YapiBody{

    public YapiBodyArray(){
        super("array");
    }
    /**
     * 这个数组的每一个元素
     */
    private YapiBody items;
}
