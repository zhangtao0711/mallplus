package com.zscat.mallplus.water.mapper;


import com.zscat.mallplus.water.entity.WtWarterCardEmpower;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author lyn
 * @date 2020-06-05
 */
public interface WtWarterCardEmpowerMapper extends BaseMapper<WtWarterCardEmpower> {
    //水卡下绑定用户数
    Integer getUmsNum(@Param("cardNo")String cardNo, @Param("delFlag")String delFlag);
}
