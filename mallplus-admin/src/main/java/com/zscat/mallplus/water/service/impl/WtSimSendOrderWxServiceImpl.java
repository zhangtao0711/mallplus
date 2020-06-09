package com.zscat.mallplus.water.service.impl;

import com.zscat.mallplus.water.entity.WtSimSendOrderWx;
import com.zscat.mallplus.water.mapper.WtSimSendOrderWxMapper;
import com.zscat.mallplus.water.service.IWtSimSendOrderWxService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lyn
 * @date 2020-06-09
 */
@Service
public class WtSimSendOrderWxServiceImpl extends ServiceImpl
        <WtSimSendOrderWxMapper, WtSimSendOrderWx> implements IWtSimSendOrderWxService {

    @Resource
    private WtSimSendOrderWxMapper wtSimSendOrderMapper;


}
