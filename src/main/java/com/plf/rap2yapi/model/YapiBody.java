package com.plf.rap2yapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 请求体响应体
 * @author penglf3
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class YapiBody {

    /**
     * 类型，array,number,string,object,boolean,integer
     */
    private String type;
}
