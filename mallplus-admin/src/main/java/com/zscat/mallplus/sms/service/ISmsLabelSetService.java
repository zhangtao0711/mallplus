package com.zscat.mallplus.sms.service;

import com.zscat.mallplus.sms.entity.SmsLabelSet;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author wang
* @date 2020-05-30
*/

public interface ISmsLabelSetService extends IService<SmsLabelSet> {

    Integer getCount(String labelName);
}
