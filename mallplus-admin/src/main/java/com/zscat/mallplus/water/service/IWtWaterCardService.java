package com.zscat.mallplus.water.service;

import com.zscat.mallplus.water.entity.WtWaterCard;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author lyn
 * @date 2020-05-30
 */

public interface IWtWaterCardService extends IService<WtWaterCard> {
    //根据卡号获取数据库中卡号信息
    WtWaterCard getOneBy(WtWaterCard entity);
}
