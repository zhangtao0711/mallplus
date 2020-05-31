package com.zscat.mallplus.water.mapper;


import com.zscat.mallplus.water.entity.WtWaterCardActivate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author lyn
 * @date 2020-05-30
 */
public interface WtWaterCardActivateMapper extends BaseMapper<WtWaterCardActivate> {
    //卡号是否重复
    List<WtWaterCardActivate> getNum(@Param("sta")Long sta, @Param("end") Long end);
    //获取开卡开号存在数 卡号关联公众号
    Map<String,Integer> getNumInfo(@Param("sta")Long sta, @Param("end") Long end, @Param("userId") String userId);
}
