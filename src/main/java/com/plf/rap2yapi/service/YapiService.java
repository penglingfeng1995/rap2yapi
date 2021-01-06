package com.plf.rap2yapi.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.plf.rap2yapi.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

/**
 * 调用yapi接口服务
 *
 * @author penglf3
 */
@Slf4j
@Service
public class YapiService {

    public void transJsonFile(File sourceRapJsonFile, File targetYapiJsonFile) {
        long now = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

        Set<String> requestTypeSet = new HashSet<>();
        Set<String> dataTypeSet = new HashSet<>();

        List<YapiModule> yapiModuleList = new ArrayList<>();

        try (InputStream is = new FileInputStream(sourceRapJsonFile);
             OutputStream os = new FileOutputStream(targetYapiJsonFile)) {
            JSONObject ex = JSON.parseObject(is, JSONObject.class);
            String modelJSONStr = ex.getString("modelJSON");
            JSONObject modelJSON = JSON.parseObject(modelJSONStr, JSONObject.class);
            JSONArray moduleList = modelJSON.getJSONArray("moduleList");

            for (int i = 0; i < moduleList.size(); i++) {
                JSONObject module = moduleList.getJSONObject(i);
                // 顶部的分类
                String moduleName = module.getString("name");
                // 左侧的子分类
                JSONArray pageList = module.getJSONArray("pageList");

                YapiModule yapiModule = new YapiModule();
                yapiModule.setName(moduleName);
                yapiModule.setAdd_time(now);
                yapiModule.setUp_time(now);

                List<YapiInterface> yapiInterfaceList = new ArrayList<>();
                for (int j = 0; j < pageList.size(); j++) {
                    JSONObject page = pageList.getJSONObject(j);
                    // 子分类的名称
                    String pageName = page.getString("name");
                    JSONArray actionList = page.getJSONArray("actionList");
                    for (int k = 0; k < actionList.size(); k++) {
                        JSONObject action = actionList.getJSONObject(k);
                        // 接口名称
                        String actionName = action.getString("name");
                        // 接口路径
                        String requestUrl = action.getString("requestUrl");
                        // 请求类型，数字 1,2.. 依次为get,post,put,delete
                        String requestType = action.getString("requestType");
                        requestTypeSet.add(requestType);

                        YapiInterface yapiInterface = new YapiInterface();
                        yapiInterface.setTitle(pageName + "-" + actionName);
                        yapiInterface.setMethod(transMethod(requestType));
                        String fullPath = fullPath(requestUrl);
                        yapiInterface.setPath(fullPath);
                        yapiInterface.setQuery_path(new YapiQueryPath(fullPath));
                        yapiInterface.setAdd_time(now);
                        yapiInterface.setUp_time(now);

                        log.info("****模块{}-{},接口:{}-{}-{}", moduleName, pageName, actionName, requestUrl, requestType);

                        // 参数
                        JSONArray requestParameterList = action.getJSONArray("requestParameterList");
                        // 如果是get请求，则只取第一级的参数
                        if ("1".equals(requestType)) {
                            List<YapiReqQuery> yapiReqQueryList = transGetParam(requestParameterList);
                            yapiInterface.setReq_query(yapiReqQueryList);
                        } else {
                            // post请求会去递归参数
                            YapiBodyObject yapiBodyObject = transPostBody(requestParameterList);
                            yapiInterface.setReq_body_other(JSON.toJSONString(yapiBodyObject));
                        }
                        // 响应
                        JSONArray responseParameterList = action.getJSONArray("responseParameterList");
                        YapiBodyObject yapiBodyObject = transPostBody(responseParameterList);
                        yapiInterface.setRes_body(JSON.toJSONString(yapiBodyObject));

                        yapiInterfaceList.add(yapiInterface);
                    }
                }
                yapiModule.setList(yapiInterfaceList);
                yapiModuleList.add(yapiModule);
            }
            //requestType:[1, 2],dataType[, number, boolean, array<object>, string, array<string>, array<number>, object]
            log.info("requestType:{},dataType{}", requestTypeSet, dataTypeSet);

            JSON.writeJSONString(os, yapiModuleList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info("转换完成");
    }

    /**
     * 补全 请求路径，如果 开头不是 / ，则带上
     *
     * @param requestUrl
     * @return
     */
    private String fullPath(String requestUrl) {
        if (StringUtils.isNotEmpty(requestUrl) && !StringUtils.startsWith(requestUrl, "/")) {
            log.info("进行补全,{}", requestUrl);
            return "/" + requestUrl;
        }
        return requestUrl;
    }

    /**
     * 转换为yapi的get请求参数，只取一级
     *
     * @param requestParameterList
     * @return
     */
    private List<YapiReqQuery> transGetParam(JSONArray requestParameterList) {
        List<YapiReqQuery> yapiReqQueryList = new ArrayList<>();
        for (int m = 0; m < requestParameterList.size(); m++) {
            JSONObject requestParameter = requestParameterList.getJSONObject(m);
            // 变量名
            String identifier = requestParameter.getString("identifier");
            // 类型，如 object,array<object>,number...
            String dataType = requestParameter.getString("dataType");
            // 含义
            String name = requestParameter.getString("name");
            // 备注
            String remark = requestParameter.getString("remark");

            YapiReqQuery yapiReqQuery = new YapiReqQuery();
            yapiReqQuery.setName(identifier);
            yapiReqQuery.setDesc(complexDesc(name, remark));
            yapiReqQuery.setExample(dataType);

            yapiReqQueryList.add(yapiReqQuery);
            log.info("----请求参数:{}-{}-{}-{}", identifier, dataType, name, remark);
        }
        return yapiReqQueryList;
    }

    /**
     * 复合接口名称和备注，作为接口描述
     *
     * @param name
     * @param remark
     * @return
     */
    private String complexDesc(String name, String remark) {
        if (StringUtils.isEmpty(remark) || StringUtils.contains(remark, "@mock")) {
            return name;
        }
        return name + "," + remark;
    }

    /**
     * 转换post请求参数，或响应体，递归遍历多级参数
     *
     * @param requestParameterList
     * @return
     */
    //dataType[, number, boolean, array<object>, string, array<string>, array<number>, object]
    //array,number,string,object,boolean,integer
    private YapiBodyObject transPostBody(JSONArray requestParameterList) {
        YapiBodyObject root = new YapiBodyObject();
        Map<String, YapiBody> properties = new HashMap<>();
        for (int m = 0; m < requestParameterList.size(); m++) {
            JSONObject requestParameter = requestParameterList.getJSONObject(m);
            // 变量名
            String identifier = requestParameter.getString("identifier");
            // 类型
            String dataType = requestParameter.getString("dataType");
            // 含义
            String name = requestParameter.getString("name");
            // 备注
            String remark = requestParameter.getString("remark");
            // 层级不断
            JSONArray parameterList = requestParameter.getJSONArray("parameterList");

            switch (dataType) {
                case "object":
                    // 递归调用当前方法
                    YapiBodyObject yapiBodyObject = transPostBody(parameterList);
                    properties.put(identifier, yapiBodyObject);
                    break;
                case "array<object>":
                    YapiBodyObject yapiBodyObjectItem = transPostBody(parameterList);
                    YapiBodyArray yapiBodyArray = new YapiBodyArray();
                    yapiBodyArray.setItems(yapiBodyObjectItem);
                    properties.put(identifier, yapiBodyArray);
                    break;
                case "array<string>":
                case "array":
                    wrapperCommonArrayBody(properties, identifier, name, remark, "string");
                    break;
                case "array<boolean>":
                    wrapperCommonArrayBody(properties, identifier, name, remark, "boolean");
                    break;
                case "array<number>":
                    wrapperCommonArrayBody(properties, identifier, name, remark, "number");
                    break;
                default:
                    // 没有指定类型，或类型为 number,boolean,string
                    wrapperCommonBody(properties, identifier, dataType, name, remark);
                    break;
            }
            log.info("----请求参数:{}-{}-{}-{}", identifier, dataType, name, remark);
        }
        root.setProperties(properties);
        return root;
    }

    private void wrapperCommonArrayBody(Map<String, YapiBody> properties, String identifier, String name, String remark, String type) {
        YapiBodyArray yapiBodyArray = new YapiBodyArray();
        YapiBodyCommon yapiBodyItem = new YapiBodyCommon();
        yapiBodyItem.setType(type);
        yapiBodyItem.setDescription(complexDesc(name, remark));
        yapiBodyArray.setItems(yapiBodyItem);
        properties.put(identifier, yapiBodyArray);
    }

    private void wrapperCommonBody(Map<String, YapiBody> properties, String identifier, String dataType, String name, String remark) {
        YapiBodyCommon yapiBodyCommon = new YapiBodyCommon();
        yapiBodyCommon.setType(dataType);
        yapiBodyCommon.setDescription(complexDesc(name, remark));
        properties.put(identifier, yapiBodyCommon);
    }

    private String transMethod(String requestType) {
        String method = "POST";
        switch (requestType) {
            case "1":
                method = "GET";
                break;
            case "2":
                method = "POST";
                break;
            case "3":
                method = "PUT";
                break;
            case "4":
                method = "DELETE";
                break;
            default:
                method = "POST";
                break;
        }
        return method;
    }
}
