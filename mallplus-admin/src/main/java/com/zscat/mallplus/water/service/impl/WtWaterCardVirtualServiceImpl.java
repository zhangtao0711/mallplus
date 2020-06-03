package com.zscat.mallplus.water.service.impl;

import com.zscat.mallplus.water.entity.WtWaterCardVirtual;
import com.zscat.mallplus.water.mapper.WtWaterCardVirtualMapper;
import com.zscat.mallplus.water.service.IWtWaterCardVirtualService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lyn
 * @date 2020-06-03
 */
@Service
public class WtWaterCardVirtualServiceImpl extends ServiceImpl
        <WtWaterCardVirtualMapper, WtWaterCardVirtual> implements IWtWaterCardVirtualService {

    @Resource
    private WtWaterCardVirtualMapper wtWaterCardVirtualMapper;


}
