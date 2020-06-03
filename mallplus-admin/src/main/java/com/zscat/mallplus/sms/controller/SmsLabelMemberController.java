package com.zscat.mallplus.sms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.sms.entity.SmsLabelMember;
import com.zscat.mallplus.sms.entity.SmsLabelSet;
import com.zscat.mallplus.sms.service.ISmsLabelMemberService;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.ums.service.IUmsMemberService;
import com.zscat.mallplus.util.EasyPoiUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.weixinmp.entity.AccountWechats;
import com.zscat.mallplus.weixinmp.service.IAccountWechatsService;
import com.zscat.mallplus.wxminiapp.service.IAccountWxappService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Date;

/**
 * @author wang
 * @date 2020-05-30
 * 批量设置标签用户
 */
@Slf4j
@RestController
@RequestMapping("/sms/smsLabelMember")
public class SmsLabelMemberController {

    @Resource
    private ISmsLabelMemberService ISmsLabelMemberService;
    @Resource
    private WxMpService wxMpService;
    @Resource
    private IUmsMemberService memberService;
    @Resource
    private IAccountWechatsService wechatsService;

    @SysLog(MODULE = "sms", REMARK = "根据条件查询所有批量设置标签用户列表")
    @ApiOperation("根据条件查询所有批量设置标签用户列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('sms:smsLabelMember:read')")
    public Object getSmsLabelMemberByPage(SmsLabelMember entity,
                                          @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                          @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(ISmsLabelMemberService.page(new Page<SmsLabelMember>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有批量设置标签用户列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "保存批量设置标签用户")
    @ApiOperation("保存批量设置标签用户")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('sms:smsLabelMember:create')")
    public Object saveBathSmsLabelMember(@RequestBody List<SmsLabelMember> entity) {
        try {
//            entity.setCreateTime(new Date());
            if (ISmsLabelMemberService.saveBatch(entity)) {
                //给微信那边数据备份
                UmsMember umsMember = new UmsMember();
                umsMember.setWeixinOpenid(entity.get(0).getOpenId());
                UmsMember members = memberService.getOne(new QueryWrapper<>(umsMember));
                //获取微信公众号信息
                AccountWechats accountWechats = new AccountWechats();
                accountWechats.setUniacid(members.getUniacid());
                AccountWechats wxapp = wechatsService.getOne(new QueryWrapper<>(accountWechats));
                String[] openids = this.getStrings(entity);
                boolean res = this.wxMpService.switchoverTo(wxapp.getKey()).getUserTagService().batchTagging(entity.get(0).getTagId(), openids);
                if (!res){
                    for (SmsLabelMember smsLabelMember : entity){
                        SmsLabelMember labelMember = new SmsLabelMember();
                        labelMember.setLabelId(smsLabelMember.getLabelId());
                        labelMember.setMemberId(smsLabelMember.getMemberId());
                        labelMember.setLabelId(smsLabelMember.getTagId());
                        labelMember.setOpenId(smsLabelMember.getOpenId());
                        SmsLabelMember member = ISmsLabelMemberService.getOne(new QueryWrapper<>(labelMember));
                        ISmsLabelMemberService.removeById(member);
                    }
                }
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存批量设置标签：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    private String[] getStrings(List<SmsLabelMember> list){
        String[] orgId = new String[list.size()];
        for(int i=0;i<list.size();i++){
            orgId[i] = list.get(i).getOpenId();
        }
        return orgId;
    }

    @SysLog(MODULE = "sms", REMARK = "更新批量设置标签用户")
    @ApiOperation("更新批量设置标签用户")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('sms:smsLabelMember:update')")
    public Object updateBatchSmsLabelMember(@RequestBody List<SmsLabelMember> entity) {
        try {
            List<SmsLabelMember> labelMembers = (List<SmsLabelMember>) ISmsLabelMemberService.listByIds(entity);
            if (ISmsLabelMemberService.updateBatchById(entity)) {
                //批量对用户取消标签
                UmsMember umsMember = new UmsMember();
                umsMember.setWeixinOpenid(entity.get(0).getOpenId());
                UmsMember members = memberService.getOne(new QueryWrapper<>(umsMember));
                //获取微信公众号信息
                AccountWechats accountWechats = new AccountWechats();
                accountWechats.setUniacid(members.getUniacid());
                AccountWechats wxapp = wechatsService.getOne(new QueryWrapper<>(accountWechats));
                String[] openids = getStrings(entity);
                boolean res = this.wxMpService.switchoverTo(wxapp.getKey()).getUserTagService().batchUntagging(entity.get(0).getTagId(), openids);
                if (!res){
                    ISmsLabelMemberService.updateBatchById(labelMembers);
                }
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新批量设置标签：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "删除批量设置标签用户")
    @ApiOperation("删除批量设置标签用户")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('sms:smsLabelMember:delete')")
    public Object deleteSmsLabelMember(@ApiParam("批量设置标签用户id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("批量设置标签用户id");
            }
            if (ISmsLabelMemberService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除批量设置标签：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "给批量设置标签用户分配批量设置标签用户")
    @ApiOperation("查询批量设置标签用户明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('sms:smsLabelMember:read')")
    public Object getSmsLabelMemberById(@ApiParam("批量设置标签用户id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("批量设置标签用户id");
            }
            SmsLabelMember coupon = ISmsLabelMemberService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询批量设置标签明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除批量设置标签用户")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "sms", REMARK = "批量删除批量设置标签用户")
    @PreAuthorize("hasAuthority('sms:smsLabelMember:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = ISmsLabelMemberService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "sms", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, SmsLabelMember entity) {
        // 模拟从数据库获取需要导出的数据
        List<SmsLabelMember> personList = ISmsLabelMemberService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", SmsLabelMember.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "sms", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<SmsLabelMember> personList = EasyPoiUtils.importExcel(file, SmsLabelMember.class);
        ISmsLabelMemberService.saveBatch(personList);
    }
}


