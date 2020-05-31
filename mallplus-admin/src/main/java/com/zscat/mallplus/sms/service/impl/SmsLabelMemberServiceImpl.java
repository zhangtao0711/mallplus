package com.zscat.mallplus.sms.service.impl;

import com.zscat.mallplus.sms.entity.SmsLabelMember;
import com.zscat.mallplus.sms.mapper.SmsLabelMemberMapper;
import com.zscat.mallplus.sms.service.ISmsLabelMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
/**
* @author lyn
* @date 2020-05-30
*/
@Service
public class SmsLabelMemberServiceImpl extends ServiceImpl
<SmsLabelMemberMapper, SmsLabelMember> implements ISmsLabelMemberService {

@Resource
private  SmsLabelMemberMapper smsLabelMemberMapper;


}
