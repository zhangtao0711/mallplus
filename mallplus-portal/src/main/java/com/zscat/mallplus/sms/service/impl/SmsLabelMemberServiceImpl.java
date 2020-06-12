package com.zscat.mallplus.sms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.common.CommonConstant;
import com.zscat.mallplus.set.entity.SetSalesBuy;
import com.zscat.mallplus.set.mapper.SetSalesBuyMapper;
import com.zscat.mallplus.sms.entity.SmsLabelMember;
import com.zscat.mallplus.sms.entity.SmsLabelSet;
import com.zscat.mallplus.sms.mapper.SmsLabelMemberMapper;
import com.zscat.mallplus.sms.mapper.SmsLabelSetMapper;
import com.zscat.mallplus.sms.service.ISmsLabelMemberService;
import com.zscat.mallplus.wxminiapp.entity.AccountWxapp;
import com.zscat.mallplus.wxminiapp.mapper.AccountWxappMapper;
import com.zscat.mallplus.wxminiapp.service.WxMaServiceDemo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lyn
 * @date 2020-05-30
 */
@Service
public class SmsLabelMemberServiceImpl extends ServiceImpl
        <SmsLabelMemberMapper, SmsLabelMember> implements ISmsLabelMemberService {

    @Resource
    private SmsLabelMemberMapper smsLabelMemberMapper;
    @Resource
    private SetSalesBuyMapper buyMapper;
    @Resource
    private AccountWxappMapper accountWxappMapper;
    @Resource
    private WxMaServiceDemo maServiceDemo;
    @Resource
    private SmsLabelSetMapper smsLabelSetMapper;

    @Override
    public void addMemberLabel(Integer uniacid, Long memberId, String openId) {
        //首先验证小程序的经销商买没买客户标签功能
        AccountWxapp accountWxapp = new AccountWxapp();
        accountWxapp.setUniacid(uniacid);
        AccountWxapp wxapp = accountWxappMapper.selectOne(new QueryWrapper<>(accountWxapp));
        Long dealerId = wxapp.getCreateBy();
        SetSalesBuy salesBuy = new SetSalesBuy();
        salesBuy.setDealerId(dealerId);
        salesBuy.setPerssionId(CommonConstant.member_label);
        SetSalesBuy buy = buyMapper.selectOne(new QueryWrapper<>(salesBuy));
        if (buy.getEndTime().before(new Date())) {
            return;
        }
        SmsLabelMember labelMember = new SmsLabelMember();
        labelMember.setOpenId(openId);
        labelMember.setLabelId(CommonConstant.label_member_community);
        labelMember.setMemberId(memberId);
        labelMember.setTagId(CommonConstant.tag_member_community);
        smsLabelMemberMapper.insert(labelMember);
        SmsLabelMember member = new SmsLabelMember();
        member.setOpenId(openId);
        member.setLabelId(CommonConstant.label_member_level);
        member.setMemberId(memberId);
        member.setTagId(CommonConstant.tag_member_level);
        smsLabelMemberMapper.insert(member);
        //TODO 微信公众号添加标签感觉好像是标签组，有问题待确定
    }

    @Override
    public void addCardLabel(Integer uniacid, Integer storeId, BigDecimal cardMoney) {
        //获取标签
        Long dealerId = accountWxappMapper.getDealerIdByUniacid(uniacid);
        //查看有没有购买客户标签权限，购买了是否已过期等等
        SetSalesBuy salesBuy  = new SetSalesBuy();
        salesBuy.setPerssionId(CommonConstant.member_label);
        salesBuy.setDealerId(dealerId);
        salesBuy.setStoreId(storeId);
        SetSalesBuy buy = buyMapper.selectOne(new QueryWrapper<>(salesBuy));
        //如果没过期
        if (buy.getEndTime().after(new Date())){
            //获取标签设置信息
            //需要标签id的
            SmsLabelSet set = smsLabelSetMapper.selectById(CommonConstant.label_member_level);
            if (cardMoney.compareTo(set.getGreater())>0
                    ||cardMoney.compareTo(set.getEqual())==0
                    ||cardMoney.compareTo(set.getLimit())<0){
                //添加标签
            }
        }
    }

}
