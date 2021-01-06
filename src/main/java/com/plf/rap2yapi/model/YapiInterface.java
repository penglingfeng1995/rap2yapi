package com.plf.rap2yapi.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * yapi的一个接口对象
 * @author penglf3
 */
@Data
public class YapiInterface {

    /**
     * 请求方式，字符串，GET,POST,PUT,DELETE
     */
    private String method;

    /**
     * 接口标题名称，如 获取商品
     */
    private String title;

    /**
     * 接口路径，如 /getProduct
     */
    private String path;

    /**
     * 排序，默认0
     */
    private Integer index = 0;

    /**
     * 类型，static
     */
    private String type = "static";

    /**
     * 状态,done,undone
     */
    private String status = "done";

    /**
     * 底部的选项是否为公共接口,默认false
     */
    private Boolean api_opened =false;


    private Long add_time;

    private Long up_time;

    /**
     * 接口的备注，md形式
     */
    private String markdown;

    /**
     * 接口备注，html形式
     */
    private String desc;

    /**
     * 请求地址
     */
    private YapiQueryPath query_path;

    /**
     * 请求体是否开启jsonSchema
     */
    private Boolean req_body_is_json_schema = true;

    /**
     * 请求体类型，raw,json
     */
    private String req_body_type = "json";

    /**
     * post请求体
     */
    private String req_body_other ;

    /**
     * 空的
     */
    private List<Map<String,Object>> req_body_form;

    /**
     * 空的
     */
    private List<Map<String,Object>> req_params;

    /**
     * 请求参数设置，Headers
     */
    private List<YapiReqHeader> req_headers;

    /**
     * 请求参数设置，Query的列表
     */
    private List<YapiReqQuery> req_query;

    /**
     * 响应体是否开启jsonSchema
     */
    private Boolean res_body_is_json_schema = true;

    /**
     * 响应体类型，raw,json
     */
    private String res_body_type = "json";

    /**
     * 响应体，以json5字符串的形式存储，
     */
    private String res_body;

}
