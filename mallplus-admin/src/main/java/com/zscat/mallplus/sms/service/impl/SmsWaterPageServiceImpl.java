package com.zscat.mallplus.sms.service.impl;

import com.zscat.mallplus.sms.entity.SmsWaterPage;
import com.zscat.mallplus.sms.mapper.SmsWaterPageMapper;
import com.zscat.mallplus.sms.service.ISmsWaterPageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
/**
* @author wang
* @date 2020-05-29
*/
@Service
public class SmsWaterPageServiceImpl extends ServiceImpl
<SmsWaterPageMapper, SmsWaterPage> implements ISmsWaterPageService {

@Resource
private  SmsWaterPageMapper smsWaterPageMapper;


}
