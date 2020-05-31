package com.zscat.mallplus.sms.service.impl;

import com.zscat.mallplus.sms.entity.SmsRechargePackage;
import com.zscat.mallplus.sms.mapper.SmsRechargePackageMapper;
import com.zscat.mallplus.sms.service.ISmsRechargePackageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
/**
* @author wang
* @date 2020-05-30
*/
@Service
public class SmsRechargePackageServiceImpl extends ServiceImpl
<SmsRechargePackageMapper, SmsRechargePackage> implements ISmsRechargePackageService {

@Resource
private  SmsRechargePackageMapper smsRechargePackageMapper;


}
