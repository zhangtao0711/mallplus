package com.zscat.mallplus.water.mapper;


import com.zscat.mallplus.water.entity.WtWaterCardCreate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zscat.mallplus.water.entity.WtWaterCardExcel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lyn
 * @date 2020-05-29
 */
public interface WtWaterCardCreateMapper extends BaseMapper<WtWaterCardCreate> {
    //卡号是否重复
    List<WtWaterCardCreate> getNum(@Param("sta")Long sta,@Param("end") Long end);
    //制卡信息下载
    List<WtWaterCardExcel> getExport(@Param("delFlag")String delFlag, @Param("id") Long id);
}
