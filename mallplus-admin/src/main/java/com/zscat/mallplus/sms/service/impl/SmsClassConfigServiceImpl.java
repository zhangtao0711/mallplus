package com.zscat.mallplus.sms.service.impl;

import com.zscat.mallplus.sms.entity.SmsClassConfig;
import com.zscat.mallplus.sms.mapper.SmsClassConfigMapper;
import com.zscat.mallplus.sms.service.ISmsClassConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
/**
* @author wang
* @date 2020-05-29
*/
@Service
public class SmsClassConfigServiceImpl extends ServiceImpl
<SmsClassConfigMapper, SmsClassConfig> implements ISmsClassConfigService {

@Resource
private  SmsClassConfigMapper smsClassConfigMapper;


}
