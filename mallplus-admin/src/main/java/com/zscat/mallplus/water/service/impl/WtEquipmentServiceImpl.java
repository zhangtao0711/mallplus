package com.zscat.mallplus.water.service.impl;

import com.zscat.mallplus.water.entity.WtEquipment;
import com.zscat.mallplus.water.mapper.WtEquipmentMapper;
import com.zscat.mallplus.water.service.IWtEquipmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lyn
 * @date 2020-05-22
 */
@Service
public class WtEquipmentServiceImpl extends ServiceImpl
        <WtEquipmentMapper, WtEquipment> implements IWtEquipmentService {

    @Resource
    private WtEquipmentMapper wtEquipmentMapper;


}
