package com.zscat.mallplus.sms.service.impl;

import com.zscat.mallplus.sms.entity.SmsWaterBuyRecord;
import com.zscat.mallplus.sms.mapper.SmsWaterBuyRecordMapper;
import com.zscat.mallplus.sms.service.ISmsWaterBuyRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author wang
 * @date 2020-05-30
 */
@Service
public class SmsWaterBuyRecordServiceImpl extends ServiceImpl
        <SmsWaterBuyRecordMapper, SmsWaterBuyRecord> implements ISmsWaterBuyRecordService {

    @Resource
    private SmsWaterBuyRecordMapper smsWaterBuyRecordMapper;


    @Override
    public Map<String, Object> statisticsWater(Long dealerId, Integer storeId) {
        return smsWaterBuyRecordMapper.statisticsWater(dealerId,storeId);
    }
}
