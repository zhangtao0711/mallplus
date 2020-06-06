package com.zscat.mallplus.water.service;

import com.zscat.mallplus.water.entity.WtWaterCardRecharge;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @author lyn
 * @date 2020-05-31
 */

public interface IWtWaterCardRechargeService extends IService<WtWaterCardRecharge> {

    //获取充值卡号是否是当前经销商下的
    boolean getDealerId(Long aLong, Long aLong1, Long dalerId);
    //保存
    boolean save(WtWaterCardRecharge entity);
    //获取经销商用户标签使用次数
    Integer getSalesCount(WtWaterCardRecharge entity,String perssionId);
    //获取经销商充值套餐权限
    boolean getSalesInfo(Long dealerId, String perssionId);
}
