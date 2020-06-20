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
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.weixinmp.entity.AccountWechats;
import com.zscat.mallplus.weixinmp.mapper.AccountWechatsMapper;
import com.zscat.mallplus.wxminiapp.entity.AccountWxapp;
import com.zscat.mallplus.wxminiapp.mapper.AccountWxappMapper;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private AccountWechatsMapper accountWechatsMapper;
    @Resource
    private SmsLabelSetMapper smsLabelSetMapper;
    @Resource
    private WxMpService wxMpService;

    /**
     * 注册之后自动加上社区标签和会员等级标签
     * @param uniacid
     * @param memberId
     * @param openId
     */
    @Override
    @Transactional
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
        // 微信公众号添加标签感觉好像是标签组，有问题待确定
        //首先验证该经销商有没有公众号，只有公众号才有标签
        AccountWechats accountWechats = new AccountWechats();
        accountWechats.setCreateBy(dealerId);
        AccountWechats wechats = accountWechatsMapper.selectOne(new QueryWrapper<>(accountWechats));
        if (wechats==null){
            return;
        }
        String[] openids = new String[1];
        openids[0] = openId;
        try {
            wxMpService.switchoverTo(wechats.getKey()).getUserTagService().batchTagging(CommonConstant.tag_member_level,openids);
            wxMpService.switchoverTo(wechats.getKey()).getUserTagService().batchTagging(CommonConstant.tag_member_community,openids);
        } catch (WxErrorException e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * 消费之后检验卡的余额，判断是否达到条件，达到条件加上，不达到或者以前达到过现在没达到再去掉
     * @param uniacid
     * @param storeId
     * @param cardMoney
     */
    @Override
    @Transactional
    public void addCardLabel(Integer uniacid, Integer storeId, BigDecimal cardMoney,Long memberId,String openid) throws WxErrorException {
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
            AccountWechats accountWechats = new AccountWechats();
            accountWechats.setCreateBy(dealerId);
            AccountWechats wechats = accountWechatsMapper.selectOne(new QueryWrapper<>(accountWechats));
            if (wechats==null){
                return;
            }
            //获取标签设置信息
            //需要标签id的
            SmsLabelSet set = smsLabelSetMapper.selectById(CommonConstant.label_member_card);
            //1.先校验用户有没有添加标签
            SmsLabelMember smsLabelMember = new SmsLabelMember();
            smsLabelMember.setTagId(CommonConstant.tag_member_card);
            smsLabelMember.setMemberId(memberId);
            SmsLabelMember labelMember = smsLabelMemberMapper.selectOne(new QueryWrapper<>(smsLabelMember));
            String[] openids = new String[1];
            openids[0] = openid;
            if (labelMember!=null){
                //2.如果有标签了，判断是不是符合要求
                if (cardMoney.compareTo(set.getGreater())>0
                        ||cardMoney.compareTo(set.getEqual())==0
                        ||cardMoney.compareTo(set.getLimit())<0){
                    //符合要求
                    return;
                }else {
                    //去掉标签
                    smsLabelMemberMapper.deleteById(labelMember);
                    wxMpService.switchoverTo(wechats.getKey()).getUserTagService().batchUntagging(CommonConstant.tag_member_card, openids);

                }
            }
            //没有标签
            if (cardMoney.compareTo(set.getGreater())>0
                    ||cardMoney.compareTo(set.getEqual())==0
                    ||cardMoney.compareTo(set.getLimit())<0){
                //添加标签
                SmsLabelMember member = new SmsLabelMember();
                member.setOpenId(openid);
                member.setLabelId(CommonConstant.label_member_card);
                member.setMemberId(memberId);
                member.setTagId(CommonConstant.tag_member_card);
                smsLabelMemberMapper.insert(member);
                //微信添加
                wxMpService.switchoverTo(wechats.getKey()).getUserTagService().batchTagging(CommonConstant.tag_member_card,openids);

            }
        }
    }

}
