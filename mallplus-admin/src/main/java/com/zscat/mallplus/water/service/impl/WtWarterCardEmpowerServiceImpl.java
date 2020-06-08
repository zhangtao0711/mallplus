package com.zscat.mallplus.water.service.impl;

import com.zscat.mallplus.water.entity.WtWarterCardEmpower;
import com.zscat.mallplus.water.mapper.WtWarterCardEmpowerMapper;
import com.zscat.mallplus.water.service.IWtWarterCardEmpowerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lyn
 * @date 2020-06-05
 */
@Service
public class WtWarterCardEmpowerServiceImpl extends ServiceImpl
        <WtWarterCardEmpowerMapper, WtWarterCardEmpower> implements IWtWarterCardEmpowerService {

    @Resource
    private WtWarterCardEmpowerMapper wtWarterCardEmpowerMapper;

    //水卡下绑定用户数
    public Integer getUmsNum(String cardNo, String delFlag){
        return wtWarterCardEmpowerMapper.getUmsNum(cardNo, delFlag);
    }
}
