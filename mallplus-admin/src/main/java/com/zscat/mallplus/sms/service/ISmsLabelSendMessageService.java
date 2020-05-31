package com.zscat.mallplus.sms.service;

import com.zscat.mallplus.sms.entity.SmsLabelSendMessage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.weixinmp.entity.AccountWechats;
import me.chanjar.weixin.common.error.WxErrorException;

/**
* @author wang
* @date 2020-05-30
*/

public interface ISmsLabelSendMessageService extends IService<SmsLabelSendMessage> {

    String sendWxMessage(SmsLabelSendMessage message) throws WxErrorException;

    public AccountWechats getAppId(Long dealerId);
}
