package com.zscat.mallplus.water.service;

import com.zscat.mallplus.water.entity.WtOpenApiInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author lyn
 * @date 2020-06-10
 */

public interface IWtOpenApiInfoService extends IService<WtOpenApiInfo> {
    //获取设备实例
    Object deviceInstanceQuery(Integer pageSize);
}
