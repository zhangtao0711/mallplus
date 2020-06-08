package com.zscat.mallplus.water.mapper;


import com.zscat.mallplus.water.entity.WtEquipment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author lyn
 * @date 2020-05-22
 */
public interface WtEquipmentMapper extends BaseMapper<WtEquipment> {
    //获取设备是否是当前经销商下的
    Map<String,Long> getDealerId(@Param("eqcode")String eqcode, @Param("dealerId")Long dealerId
            , @Param("delFlag")String delFlag);
}
