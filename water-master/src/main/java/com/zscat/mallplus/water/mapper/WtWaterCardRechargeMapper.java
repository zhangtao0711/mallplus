package com.zscat.mallplus.water.mapper;


import com.zscat.mallplus.water.entity.WtWaterCardRecharge;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Map;

/**
 * @author lyn
 * @date 2020-05-31
 */
public interface WtWaterCardRechargeMapper extends BaseMapper<WtWaterCardRecharge> {
    //获取充值卡号是否是当前经销商下的
    Map<String,Integer> getStoreId(Long sta, Long end);
}
