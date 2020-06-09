package com.zscat.mallplus.water.service.impl;

import com.zscat.mallplus.water.entity.WtSimSendOrder;
import com.zscat.mallplus.water.mapper.WtSimSendOrderMapper;
import com.zscat.mallplus.water.service.IWtSimSendOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lyn
 * @date 2020-06-09
 */
@Service
public class WtSimSendOrderServiceImpl extends ServiceImpl
        <WtSimSendOrderMapper, WtSimSendOrder> implements IWtSimSendOrderService {

    @Resource
    private WtSimSendOrderMapper wtSimSendOrderMapper;


}
