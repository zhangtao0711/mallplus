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
    boolean getStoreId(Long aLong, Long aLong1, Integer storeId);
    //保存
    boolean save(WtWaterCardRecharge entity);
}
