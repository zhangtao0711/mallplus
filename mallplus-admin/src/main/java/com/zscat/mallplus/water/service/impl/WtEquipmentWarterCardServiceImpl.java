package com.zscat.mallplus.water.service.impl;

import com.zscat.mallplus.water.entity.WtEquipmentWarterCard;
import com.zscat.mallplus.water.mapper.WtEquipmentWarterCardMapper;
import com.zscat.mallplus.water.service.IWtEquipmentWarterCardService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lyn
 * @date 2020-05-30
 */
@Service
public class WtEquipmentWarterCardServiceImpl extends ServiceImpl
        <WtEquipmentWarterCardMapper, WtEquipmentWarterCard> implements IWtEquipmentWarterCardService {

    @Resource
    private WtEquipmentWarterCardMapper wtEquipmentWarterCardMapper;


}
