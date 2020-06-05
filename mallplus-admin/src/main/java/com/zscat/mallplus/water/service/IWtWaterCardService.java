package com.zscat.mallplus.water.service;

import com.zscat.mallplus.water.entity.WtWaterCard;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author lyn
 * @date 2020-05-30
 */

public interface IWtWaterCardService extends IService<WtWaterCard> {
    //根据卡号获取数据库中卡号信息
    WtWaterCard getOneBy(WtWaterCard entity);
    //根据开卡id获取售出卡信息
    List<WtWaterCard> getAllSaleStateOn(Long id,String saleStateOn);
    //会员卡和授权人是否关联
    boolean checkCardUms(String cardNo, Long umsMemberId, String delFlag, String water_code_state_0);
}
