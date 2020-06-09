package com.zscat.mallplus.water.service.impl;

import com.zscat.mallplus.water.entity.WtWaterCardVituralConsume;
import com.zscat.mallplus.water.mapper.WtWaterCardVituralConsumeMapper;
import com.zscat.mallplus.water.service.IWtWaterCardVituralConsumeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wang
 * @date 2020-06-06
 */
@Service
public class WtWaterCardVituralConsumeServiceImpl extends ServiceImpl
        <WtWaterCardVituralConsumeMapper, WtWaterCardVituralConsume> implements IWtWaterCardVituralConsumeService {

    @Resource
    private WtWaterCardVituralConsumeMapper wtWaterCardVituralConsumeMapper;


}
