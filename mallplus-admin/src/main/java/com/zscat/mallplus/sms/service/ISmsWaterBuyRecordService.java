package com.zscat.mallplus.sms.service;

import com.zscat.mallplus.sms.entity.SmsWaterBuyRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
* @author wang
* @date 2020-05-30
*/

public interface ISmsWaterBuyRecordService extends IService<SmsWaterBuyRecord> {
    Map<String,Object> statisticsWater(Long dealerId, Integer storeId);
}
