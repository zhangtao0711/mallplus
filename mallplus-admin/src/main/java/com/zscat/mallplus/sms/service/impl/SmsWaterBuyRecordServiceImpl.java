package com.zscat.mallplus.sms.service.impl;

import com.zscat.mallplus.sms.entity.SmsWaterBuyRecord;
import com.zscat.mallplus.sms.mapper.SmsWaterBuyRecordMapper;
import com.zscat.mallplus.sms.service.ISmsWaterBuyRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
/**
* @author wang
* @date 2020-05-30
*/
@Service
public class SmsWaterBuyRecordServiceImpl extends ServiceImpl
<SmsWaterBuyRecordMapper, SmsWaterBuyRecord> implements ISmsWaterBuyRecordService {

@Resource
private  SmsWaterBuyRecordMapper smsWaterBuyRecordMapper;


}
