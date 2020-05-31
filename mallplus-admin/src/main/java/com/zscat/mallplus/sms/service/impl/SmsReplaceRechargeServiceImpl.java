package com.zscat.mallplus.sms.service.impl;

import com.zscat.mallplus.sms.entity.SmsReplaceRecharge;
import com.zscat.mallplus.sms.mapper.SmsReplaceRechargeMapper;
import com.zscat.mallplus.sms.service.ISmsReplaceRechargeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
/**
* @author wang
* @date 2020-05-28
*/
@Service
public class SmsReplaceRechargeServiceImpl extends ServiceImpl
<SmsReplaceRechargeMapper, SmsReplaceRecharge> implements ISmsReplaceRechargeService {

@Resource
private  SmsReplaceRechargeMapper smsReplaceRechargeMapper;


}
