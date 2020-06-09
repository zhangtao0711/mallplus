package com.zscat.mallplus.water.service.impl;

import com.zscat.mallplus.water.entity.WtWaterCardWithdrawMoney;
import com.zscat.mallplus.water.mapper.WtWaterCardWithdrawMoneyMapper;
import com.zscat.mallplus.water.service.IWtWaterCardWithdrawMoneyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lyn
 * @date 2020-06-08
 */
@Service
public class WtWaterCardWithdrawMoneyServiceImpl extends ServiceImpl
        <WtWaterCardWithdrawMoneyMapper, WtWaterCardWithdrawMoney> implements IWtWaterCardWithdrawMoneyService {

    @Resource
    private WtWaterCardWithdrawMoneyMapper wtWaterCardWithdrawMoneyMapper;


}
