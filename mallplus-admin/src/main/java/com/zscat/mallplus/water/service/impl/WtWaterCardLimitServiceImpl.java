package com.zscat.mallplus.water.service.impl;

import com.zscat.mallplus.water.entity.WtWaterCardLimit;
import com.zscat.mallplus.water.mapper.WtWaterCardLimitMapper;
import com.zscat.mallplus.water.service.IWtWaterCardLimitService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lyn
 * @date 2020-06-05
 */
@Service
public class WtWaterCardLimitServiceImpl extends ServiceImpl
        <WtWaterCardLimitMapper, WtWaterCardLimit> implements IWtWaterCardLimitService {

    @Resource
    private WtWaterCardLimitMapper wtWaterCardLimitMapper;


}
