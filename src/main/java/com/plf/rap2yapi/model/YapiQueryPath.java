package com.plf.rap2yapi.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author penglf3
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class YapiQueryPath {

    /**
     * 和接口中的path一致
     */
    @NonNull
    private String path;

    /**
     * 路径上自带的参数
     */
    private List<Object> params = new ArrayList<>();
}
