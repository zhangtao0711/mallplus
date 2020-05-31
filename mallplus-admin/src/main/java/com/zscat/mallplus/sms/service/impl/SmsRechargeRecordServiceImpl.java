package com.zscat.mallplus.sms.service.impl;

import com.zscat.mallplus.sms.entity.SmsRechargeRecord;
import com.zscat.mallplus.sms.mapper.SmsRechargeRecordMapper;
import com.zscat.mallplus.sms.service.ISmsRechargeRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
/**
* @author wang
* @date 2020-05-29
*/
@Service
public class SmsRechargeRecordServiceImpl extends ServiceImpl
<SmsRechargeRecordMapper, SmsRechargeRecord> implements ISmsRechargeRecordService {

@Resource
private  SmsRechargeRecordMapper smsRechargeRecordMapper;


}
