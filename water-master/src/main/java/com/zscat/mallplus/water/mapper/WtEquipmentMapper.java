package com.zscat.mallplus.water.mapper;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.water.entity.WtEquipment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author lyn
 * @date 2020-05-22
 */
public interface WtEquipmentMapper extends BaseMapper<WtEquipment> {
    //获取设备是否是当前经销商下的
    Map<String,Long> getDealerId(@Param("eqcode")String eqcode, @Param("dealerId")Long dealerId
            , @Param("delFlag")String delFlag);
    //根据条件查询所有会员表列表
    List<Map<String,Object>> selectData(Page<Map<String,Object>> page, @Param("entity")WtEquipment entity);
}
