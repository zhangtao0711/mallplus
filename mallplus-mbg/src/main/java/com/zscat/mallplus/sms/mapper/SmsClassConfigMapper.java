package com.zscat.mallplus.sms.mapper;


import com.zscat.mallplus.sms.entity.SmsClassConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author wang
* @date 2020-05-29
*/
public interface SmsClassConfigMapper extends BaseMapper<SmsClassConfig> {

    SmsClassConfig selectByDeviceId(String deviceId);
}
