package com.zscat.mallplus.single;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zscat.mallplus.jifen.entity.JifenDealerIntegrationChangeHistory;
import com.zscat.mallplus.jifen.entity.JifenMemberSignRecord;
import com.zscat.mallplus.jifen.entity.JifenSignRule;
import com.zscat.mallplus.jifen.mapper.JifenDealerIntegrationChangeHistoryMapper;
import com.zscat.mallplus.jifen.mapper.JifenMemberSignRecordMapper;
import com.zscat.mallplus.jifen.mapper.JifenSignRuleMapper;
import com.zscat.mallplus.sys.entity.SysUser;
import com.zscat.mallplus.sys.mapper.SysUserMapper;
import com.zscat.mallplus.ums.entity.UmsIntegrationChangeHistory;
import com.zscat.mallplus.ums.entity.UmsIntegrationConsumeSetting;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.ums.service.IUmsIntegrationChangeHistoryService;
import com.zscat.mallplus.ums.service.IUmsIntegrationConsumeSettingService;
import com.zscat.mallplus.ums.service.IUmsMemberService;
import com.zscat.mallplus.util.JsonUtils;
import com.zscat.mallplus.util.TimeUtil;
import com.zscat.mallplus.util.applet.StringConstantUtil;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.vo.jifen.JifenMemberSignRecordVo;
import com.zscat.mallplus.wxminiapp.mapper.AccountWxappMapper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wang
 * @date 2020-06-22
 * 签到记录
 */
@Slf4j
@RestController
@RequestMapping("/api/jifen")
public class SingleJifenMemberSignRecordController {

    @Resource
    private JifenMemberSignRecordMapper jifenMemberSignRecordMapper;
    @Resource
    private AccountWxappMapper wxappMapper;
    @Resource
    private JifenSignRuleMapper jifenSignRuleMapper;
    @Resource
    private IUmsIntegrationConsumeSettingService consumeSettingService;
    @Resource
    private IUmsIntegrationChangeHistoryService umsIntegrationChangeHistoryService;
    @Resource
    private JifenDealerIntegrationChangeHistoryMapper jifenDealerIntegrationChangeHistoryMapper;
    @Resource
    private IUmsMemberService memberService;
    @Resource
    private SysUserMapper adminMapper;

    @ApiOperation("根据条件查询所有签到记录列表-查询日期数据所有")
    @GetMapping(value = "/queryInfo")
    public Object queryInfo(JifenMemberSignRecord entity) {
        JSONObject j = new JSONObject();
        if (ValidatorUtils.empty(entity.getMemberId())||ValidatorUtils.empty(entity.getMonth())||ValidatorUtils.empty(entity.getYear())||ValidatorUtils.empty(entity.getUniacid())){
            return new CommonResult().validateFailed("uniacid或者会员id或者月份以及年份不能为空！");
        }
        //获取了日期上半部分的数据
        JifenMemberSignRecord record = jifenMemberSignRecordMapper.selectOne(new QueryWrapper<>(entity));
        if (record==null||ValidatorUtils.empty(record.getMark())){
            j.put("center",null);
        }else {
            j.put("center", net.sf.json.JSONObject.fromObject(record.getMark()));
        }
        //获取顶部数据
        if (record==null||ValidatorUtils.empty(record.getCount())){
            j.put("top",0);
        }else {
            j.put("top",record.getCount());
        }
        //获取底部数据
        Long dealerId = wxappMapper.getDealerIdByUniacid(entity.getUniacid());
        JifenSignRule jifenSignRule = new JifenSignRule();
        jifenSignRule.setDealerId(dealerId);
        List<JifenSignRule> signRules = jifenSignRuleMapper.selectList(new QueryWrapper<>(jifenSignRule));
        List<JifenSignRule> jifenSignRules = new ArrayList<>();
        //注意每天签到加积分的数据是单加的
        UmsIntegrationConsumeSetting setting = new UmsIntegrationConsumeSetting();
        setting.setDealerId(dealerId);
        UmsIntegrationConsumeSetting coupon = consumeSettingService.getOne(new QueryWrapper<UmsIntegrationConsumeSetting>(setting));
        JifenSignRule jifenSignRule1 = new JifenSignRule();
        jifenSignRule1.setContineuCount(1);
        jifenSignRule1.setDealerId(dealerId);
        jifenSignRule1.setStatus(1);
        if (coupon==null||ValidatorUtils.empty(coupon.getSign())){
            jifenSignRule1.setDonateIntegrtion(0);
        }else {
            jifenSignRule1.setDonateIntegrtion(coupon.getSign());
        }
        jifenSignRules.add(jifenSignRule1);
        //这些是经销商自己设置的数据
        for (JifenSignRule rule:signRules){
            if (record==null||ValidatorUtils.empty(record.getContinueSign())){
                rule.setStatus(0);//未达标
            }else if (record.getContinueSign()<rule.getContineuCount()){
                rule.setStatus(0);//未达标
            }else if (record.getContinueSign().equals(rule.getContineuCount())){
                rule.setStatus(1);//领取
            }else if (record.getContinueSign()>rule.getContineuCount()){
                rule.setStatus(2);//已领取
            }
            jifenSignRules.add(rule);
        }
        j.put("down",jifenSignRules);
        return new CommonResult().success(j);
    }

    @ApiOperation("保存签到记录-外加更改-还得更新用户积分和经销商积分")
    @PostMapping(value = "/update")
    @Transactional
    public Object saveJifenMemberSignRecord(@RequestBody JifenMemberSignRecordVo vo) {
        //获取各个实体数据，并校验其不为空！
        JifenMemberSignRecord entity = vo.getRecord();
        Long dealerId = wxappMapper.getDealerIdByUniacid(entity.getUniacid());
        JifenDealerIntegrationChangeHistory dealerIntegrationChangeHistory = vo.getDealerIntegrationChangeHistory();
        if (dealerIntegrationChangeHistory==null){
            return new CommonResult().validateFailed("经销商的历史修改记录不能为空！");
        }
        dealerIntegrationChangeHistory.setDealerId(dealerId);
        UmsIntegrationChangeHistory umsIntegrationChangeHistory = vo.getUmsIntegrationChangeHistory();
        if (umsIntegrationChangeHistory==null){
            return new CommonResult().validateFailed("会员的历史修改记录不能为空！");
        }
        if (!umsIntegrationChangeHistory.getChangeCount().equals(dealerIntegrationChangeHistory.getChangeCount())){
            return new CommonResult().validateFailed("经销商更改的积分不等于会员更改的积分!");
        }
        JSONObject j = new JSONObject();
        if (ValidatorUtils.empty(entity.getMemberId())||ValidatorUtils.empty(entity.getMonth())||ValidatorUtils.empty(entity.getYear())||ValidatorUtils.empty(entity.getUniacid())||ValidatorUtils.empty(entity.getMark())){
            return new CommonResult().validateFailed("uniacid或者会员id或者签到详情或者月份以及年份不能为空！");
        }
        UmsMember umsMember = memberService.getById(entity.getMemberId());
        if (umsMember.getIntegrationStatus().equals(StringConstantUtil.integrationStatus_0)){
            return new CommonResult().validateFailed("用户的积分已冻结，无法进行签到活动！");
        }
        SysUser user = adminMapper.selectById(dealerId);
        if (user.getIntegrationStatus().equals(StringConstantUtil.integrationStatus_0)){
            return new CommonResult().validateFailed("经销商的积分已冻结，无法进行签到活动！");
        }
        if (user.getIntegration()<umsIntegrationChangeHistory.getChangeCount()){
            user.setIntegrationStatus(StringConstantUtil.integrationStatus_1);
            adminMapper.updateById(user);
            return new CommonResult().validateFailed("经销商的积分不足，无法进行签到活动,已冻结！");
        }
        JifenMemberSignRecord jifenMemberSignRecord = new JifenMemberSignRecord();
        jifenMemberSignRecord.setUniacid(entity.getUniacid());
        jifenMemberSignRecord.setMemberId(entity.getMemberId());
        jifenMemberSignRecord.setMonth(entity.getMonth());
        jifenMemberSignRecord.setYear(entity.getYear());
        JifenMemberSignRecord record = jifenMemberSignRecordMapper.selectOne(new QueryWrapper<>(jifenMemberSignRecord));
        if (record==null){
            //如果是这种情况，则直接新建
            entity.setContinueSign(1);
            entity.setCount(1);
            entity.setCreateTime(new Date());
            if(jifenMemberSignRecordMapper.insert(entity)>0){
                j.put("top",1);
                umsIntegrationChangeHistoryService.save(umsIntegrationChangeHistory);
                jifenDealerIntegrationChangeHistoryMapper.insert(dealerIntegrationChangeHistory);
                user.setIntegration(user.getIntegration()-dealerIntegrationChangeHistory.getChangeCount());
                umsMember.setIntegration(umsMember.getIntegration()+umsIntegrationChangeHistory.getChangeCount());
                adminMapper.updateById(user);
                memberService.updateById(umsMember);
                return new CommonResult().success(j);
            }
        }else {
            //修改
            record.setMark(entity.getMark());
            net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(record.getMark());
            //获取昨天的时间字符串
            String yesterdayStr = TimeUtil.getYesterdayStr("yyyy-MM-dd");
            //查看昨天有没有签到,如果昨天的key是空的，那么就是漏签的，所以连续签到清零，重新计时
            if (jsonObject.getString(yesterdayStr)==null){
                record.setContinueSign(0);
            }else {
                record.setContinueSign(record.getContinueSign() + 1);
            }
            record.setCount(record.getCount() + 1);
            record.setUpdateTime(new Date());
            if (jifenMemberSignRecordMapper.updateById(record)>0){
                j.put("top",record.getCount());
                umsIntegrationChangeHistoryService.save(umsIntegrationChangeHistory);
                jifenDealerIntegrationChangeHistoryMapper.insert(dealerIntegrationChangeHistory);
                user.setIntegration(user.getIntegration()-dealerIntegrationChangeHistory.getChangeCount());
                umsMember.setIntegration(umsMember.getIntegration()+umsIntegrationChangeHistory.getChangeCount());
                adminMapper.updateById(user);
                memberService.updateById(umsMember);
                return new CommonResult().success(j);
            }
        }
        return new CommonResult().failed();
    }

}


