package com.zscat.mallplus.water.mapper;


import com.zscat.mallplus.water.entity.WtEquipment;
import com.zscat.mallplus.water.entity.WtProductGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author lyn
* @date 2020-05-22
*/
public interface WtProductGroupMapper extends BaseMapper<WtProductGroup> {
    //获取分组下面是绑定的设备
    List<WtEquipment> getProduct(@Param("eqGroupId")Long id,@Param("delFlag")String delFlag);
}
