package com.zscat.mallplus.sms.mapper;


import com.zscat.mallplus.sms.entity.SmsWaterBuyRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
* @author wang
* @date 2020-05-30
*/
public interface SmsWaterBuyRecordMapper extends BaseMapper<SmsWaterBuyRecord> {

    Map<String,Object> statisticsWater(@Param("dealerId") Long dealerId, @Param("storeId") Integer storeId);
}
