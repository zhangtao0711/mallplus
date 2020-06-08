package com.zscat.mallplus.water.service;

import com.zscat.mallplus.water.entity.WtEquipment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author lyn
 * @date 2020-05-22
 */

public interface IWtEquipmentService extends IService<WtEquipment> {
    //获取设备关联经销商和登录者经销商是否一致
    boolean getDealerId(String eqcode, Long dealerId);
}
