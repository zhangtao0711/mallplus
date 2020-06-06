package com.zscat.mallplus.water.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zscat.mallplus.water.entity.WtFilterElement;
import org.apache.ibatis.annotations.Param;

/**
* @author lyn
* @date 2020-05-20
*/
public interface WtFilterElementMapper extends BaseMapper<WtFilterElement> {
    //自动更新滤芯计时类型状态区分
    void updateState(@Param("delFlag")String delFlag, @Param("billing_mode_time")String billing_mode_time
                    ,@Param("normal")String normal,@Param("prepare")String prepare,@Param("wait")String wait);
}
