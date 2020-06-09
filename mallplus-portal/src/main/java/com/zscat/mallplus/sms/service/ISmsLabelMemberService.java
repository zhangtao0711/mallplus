package com.zscat.mallplus.sms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.sms.entity.SmsLabelMember;
import io.swagger.models.auth.In;

import java.math.BigDecimal;

/**
* @author lyn
* @date 2020-05-30
*/

public interface ISmsLabelMemberService extends IService<SmsLabelMember> {

    public void addMemberLabel(Integer uniacid,Long memberId,String openId );

    public void addCardLabel(Integer uniacid, Integer storeId, BigDecimal cardMoney);

}
