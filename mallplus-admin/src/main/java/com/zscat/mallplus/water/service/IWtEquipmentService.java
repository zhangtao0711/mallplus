package com.zscat.mallplus.water.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.water.entity.WtEquipment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @author lyn
 * @date 2020-05-22
 */

public interface IWtEquipmentService extends IService<WtEquipment> {
    //获取设备关联经销商和登录者经销商是否一致
    boolean getDealerId(String eqcode, Long dealerId);
    //根据条件查询所有设备信息列表
    IPage<Map<String, Object>> selectData(Page<Map<String,Object>> mapPage, WtEquipment entity);
}
