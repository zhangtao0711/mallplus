package com.zscat.mallplus.sms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.water.entity.WtEquipment;
import com.zscat.mallplus.water.entity.WtEquipmentForPortal;

import java.util.List;
import java.util.Map;

/**
 * @author lyn
 * @date 2020-05-22
 */

public interface IWtEquipmentService extends IService<WtEquipment> {
    //获取设备关联经销商和登录者经销商是否一致
    boolean getDealerId(String eqcode, Long dealerId);
    //根据条件查询所有设备信息列表
    IPage<Map<String, Object>> selectData(Page<Map<String, Object>> mapPage, WtEquipment entity);
    //根据设备用户查询水卡信息
    List<WtEquipmentForPortal> getWaterCardByEq(WtEquipmentForPortal entity);
    //判断设备是否空闲
    boolean checkEquipmentState(String eqcode,String on);
}
