package com.plf.rap2yapi.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * yapi请求体，对象类型
 * @author penglf3
 */
@Data
public class YapiBodyObject extends YapiBody{

    public YapiBodyObject(){
        super("object");
    }

    /**
     * 对象的属性,key为参数名称,如 code
     */
    private Map<String,YapiBody> properties;

    /**
     * 必选字段
     */
    private List<String> required = new ArrayList<>();
}
