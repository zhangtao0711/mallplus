package com.zscat.mallplus.sms.mapper;


import com.zscat.mallplus.sms.entity.SmsLabelSet;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Map;

/**
* @author wang
* @date 2020-05-30
*/
public interface SmsLabelSetMapper extends BaseMapper<SmsLabelSet> {

    Map<String,Object> selectBalanceCount(String labelName);
}
