package com.zscat.mallplus.water.service.impl;

import com.zscat.mallplus.water.entity.WtWaterCard;
import com.zscat.mallplus.water.mapper.WtWaterCardMapper;
import com.zscat.mallplus.water.service.IWtWaterCardService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lyn
 * @date 2020-05-30
 */
@Service
public class WtWaterCardServiceImpl extends ServiceImpl
        <WtWaterCardMapper, WtWaterCard> implements IWtWaterCardService {

    @Resource
    private WtWaterCardMapper wtWaterCardMapper;

    //根据卡号获取数据库中卡号信息
    public WtWaterCard getOneBy(WtWaterCard entity){
        return wtWaterCardMapper.getOneBy(entity);
    }
}
