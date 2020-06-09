package com.zscat.mallplus.water.mapper;


import com.zscat.mallplus.water.entity.WtWaterCardRecharge;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

import com.zscat.mallplus.set.entity.SetSalesBuy;

/**
 * @author lyn
 * @date 2020-05-31
 */
public interface WtWaterCardRechargeMapper extends BaseMapper<WtWaterCardRecharge> {
    //获取充值卡号是否是当前经销商下的
    Map<String,Long> getDealerId(@Param("sta")Long sta, @Param("end")Long end);
    //获取经销商用户标签使用次数
    Integer getSalesCount(@Param("data")WtWaterCardRecharge entity,@Param("perssionId") String perssionId);
    //更新用户标签使用次数
    boolean updateSalesCount(@Param("data")WtWaterCardRecharge entity,@Param("perssionId") String perssionId);
    //获取经销商充值套餐权限
    SetSalesBuy getSalesInfo(@Param("dealerId")Long dealerId, @Param("perssionId")String perssionId);
}
