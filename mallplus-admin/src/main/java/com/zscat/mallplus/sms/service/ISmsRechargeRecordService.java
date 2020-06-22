package com.zscat.mallplus.sms.service;

import com.zscat.mallplus.sms.entity.SmsRechargeRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
* @author wang
* @date 2020-05-29
*/

public interface ISmsRechargeRecordService extends IService<SmsRechargeRecord> {
    Map<String,Object> statisticsPackage(Long dealerId,Integer storeId);
}
