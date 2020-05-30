package com.zscat.mallplus.water.mapper;


import com.zscat.mallplus.water.entity.WtWaterCardCreate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lyn
 * @date 2020-05-29
 */
public interface WtWaterCardCreateMapper extends BaseMapper<WtWaterCardCreate> {
    //卡号是否重复
    List<WtWaterCardCreate> getNum(@Param("sta")Long sta,@Param("end") Long end);
}
