package com.zscat.mallplus.sms.mapper;


import com.zscat.mallplus.sms.entity.SmsRechargeRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
* @author wang
* @date 2020-05-29
*/
public interface SmsRechargeRecordMapper extends BaseMapper<SmsRechargeRecord> {

    Map<String,Object> statisticsPackage(@Param("dealerId") Long dealerId, @Param("storeId") Integer storeId);
}
