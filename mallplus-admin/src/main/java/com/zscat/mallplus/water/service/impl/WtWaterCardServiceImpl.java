package com.zscat.mallplus.water.service.impl;

import com.zscat.mallplus.water.entity.WtWaterCard;
import com.zscat.mallplus.water.mapper.WtWaterCardMapper;
import com.zscat.mallplus.water.service.IWtWaterCardService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
    //根据开卡id获取售出卡信息
    public List<WtWaterCard> getAllSaleStateOn(Long id, String saleStateOn){
        return wtWaterCardMapper.getAllSaleStateOn(id, saleStateOn);
    }

    /**
     * 会员卡和授权人是否关联
     * @param cardNo
     * @param umsMemberId
     * @param delFlag
     * @param state 正常卡
     * @return
     */
    public boolean checkCardUms(String cardNo, Long umsMemberId, String delFlag, String state){
        if(wtWaterCardMapper.getWaterCard(cardNo, umsMemberId, delFlag, state)!=null){
            return true;
        }else{
            return false;
        }
    }
}
