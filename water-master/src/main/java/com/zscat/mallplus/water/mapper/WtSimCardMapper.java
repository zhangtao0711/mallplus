package com.zscat.mallplus.water.mapper;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.water.entity.WtSimCard;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author lyn
 * @date 2020-05-22
 */
public interface WtSimCardMapper extends BaseMapper<WtSimCard> {
    //根据卡号查询
    WtSimCard getByCardno(@Param("cardno")String cardno);
    //根据条件查询所有SIM卡信息
    List<Map<String,Object>> selectData(Page<Map<String,Object>> page,  @Param("entity")WtSimCard entity);
}
