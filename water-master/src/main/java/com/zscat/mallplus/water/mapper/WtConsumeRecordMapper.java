package com.zscat.mallplus.water.mapper;


import com.zscat.mallplus.water.entity.WtConsumeRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author lyn
 * @date 2020-06-17
 */
public interface WtConsumeRecordMapper extends BaseMapper<WtConsumeRecord> {
    //根据条件查询所有会员表列表
    List<Map<String,Object>> getListSum(@Param("entity")WtConsumeRecord entity);
}
