package com.zscat.mallplus.water.mapper;


import com.zscat.mallplus.water.entity.WtSimCard;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author lyn
 * @date 2020-05-22
 */
public interface WtSimCardMapper extends BaseMapper<WtSimCard> {
    //根据卡号查询
    WtSimCard getByCardno(@Param("cardno")String cardno);
}
