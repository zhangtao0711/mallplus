package com.zscat.mallplus.water.mapper;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.water.entity.WtSimUrlInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zscat.mallplus.water.entity.WtWaterCard;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author lyn
 * @date 2020-05-22
 */
public interface WtSimUrlInfoMapper extends BaseMapper<WtSimUrlInfo> {
    //检索数据
    List<Map<String,Object>> selectData(Page<Map<String,Object>> page,@Param("entity") WtWaterCard entity);
}
