package com.zscat.mallplus.water.mapper;


import com.zscat.mallplus.water.entity.WtWarterCardEmpower;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author lyn
 * @date 2020-06-05
 */
public interface WtWarterCardEmpowerMapper extends BaseMapper<WtWarterCardEmpower> {
    //水卡下绑定用户数
    Integer getUmsNum(String cardNo, String delFlag);
}
