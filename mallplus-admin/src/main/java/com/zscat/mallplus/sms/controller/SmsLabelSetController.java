package com.zscat.mallplus.sms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.sms.entity.SmsLabelSet;
import com.zscat.mallplus.sms.service.ISmsLabelSendMessageService;
import com.zscat.mallplus.sms.service.ISmsLabelSetService;
import com.zscat.mallplus.ums.service.IUmsMemberService;
import com.zscat.mallplus.util.EasyPoiUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.weixinmp.entity.AccountWechats;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.tag.WxTagListUser;
import me.chanjar.weixin.mp.bean.tag.WxUserTag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 * @author wang
 * @date 2020-05-30
 * 客户标签设置
 */
@Slf4j
@RestController
@RequestMapping("/sms/smsLabelSet")
public class SmsLabelSetController {

    @Resource
    private ISmsLabelSetService ISmsLabelSetService;
    @Resource
    private IUmsMemberService umsMemberService;
    @Resource
    private ISmsLabelSendMessageService messageService;
    @Resource
    private WxMpService wxMpService;

    @SysLog(MODULE = "sms", REMARK = "根据条件查询所有客户标签设置列表-经销商看的数据")
    @ApiOperation("根据条件查询所有客户标签设置列表-经销商看的数据")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('sms:smsLabelSet:read')")
    public Object getSmsLabelSetByPage(SmsLabelSet entity,
                                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            entity.setIsSystem(1);
            QueryWrapper<SmsLabelSet> queryWrapper = new QueryWrapper<SmsLabelSet>(entity);
            queryWrapper.or().eq("dealer_id",entity.getDealerId());
            IPage<SmsLabelSet> page = ISmsLabelSetService.page(new Page<SmsLabelSet>(pageNum, pageSize),queryWrapper );
            List<SmsLabelSet> list = new ArrayList<>();
            list.forEach(item->{
                Integer count = 0;
//                if (item.getIsSystem()==1){
////                    //系统设置的检索有多少用户的数量
////                    count = ISmsLabelSetService.getCount(item.getLabelName());
////                }else {
////                    //获取经销商自己设置的那种，需要会员管理那边添加数据
////                }
                //直接从微信那边获取信息就好，比较方便
                WxTagListUser res = null;
                try {
                    AccountWechats wechats = messageService.getAppId(item.getDealerId());
                    res = this.wxMpService.switchoverTo(wechats.getKey()).getUserTagService().tagListUser(item.getTagId(), null);
                } catch (WxErrorException e) {
                    e.printStackTrace();
                }
                if (res==null){
                    count=0;
                }else {
                    count = res.getCount();
                }
                item.setCount(count);
            });
            return new CommonResult().success();
        } catch (Exception e) {
            log.error("根据条件查询所有客户标签设置列表-经销商看的数据：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "根据条件查询所有客户标签设置列表-系统设置的页面")
    @ApiOperation("根据条件查询所有客户标签设置列表-系统设置的页面")
    @GetMapping(value = "/sysList")
    @PreAuthorize("hasAuthority('sms:smsLabelSet:read')")
    public Object sysList(SmsLabelSet entity) {
        try {
            entity.setIsSystem(1);
            return new CommonResult().success(ISmsLabelSetService.list(new QueryWrapper<>(entity) ));
        } catch (Exception e) {
            log.error("根据条件查询所有客户标签设置列表-系统设置的页面：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "根据条件查询所有客户标签设置列表-系统设置的页面")
    @ApiOperation("根据条件查询所有客户标签设置列表-系统设置的页面")
    @GetMapping(value = "/memberList/{id}")
    @PreAuthorize("hasAuthority('sms:smsLabelSet:read')")
    public Object memberList(@ApiParam("客户标签设置id") @PathVariable Long id) {
        try {
            return new CommonResult().success(umsMemberService.getMemberByLabelId(id));
        } catch (Exception e) {
            log.error("根据条件查询所有客户标签设置列表-系统设置的页面：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "保存客户标签设置")
    @ApiOperation("保存客户标签设置")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('sms:smsLabelSet:create')")
    public Object saveSmsLabelSet(@RequestBody @Valid SmsLabelSet entity) throws WxErrorException {
        //把标签发送到微信备份
        String tagName = entity.getLabelName();
        AccountWechats wechats = messageService.getAppId(entity.getDealerId());
        WxUserTag res = this.wxMpService.switchoverTo(wechats.getKey()).getUserTagService().tagCreate(tagName);
        System.out.println(res);
        Long tagId = res.getId();
        if (!tagName.equals(res.getName())){
            return new CommonResult().failed("标签名称不一致！");
        }
        entity.setTagId(tagId);
        try {
            entity.setCreateTime(new Date());
            if (ISmsLabelSetService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存客户标签设置：%s", e.getMessage(), e);
            Boolean result = this.wxMpService.switchoverTo(wechats.getKey()).getUserTagService().tagDelete(tagId);
            if (!result){
                return new CommonResult().failed("标签存储失败，请手动去微信删除标签！");
            }
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "更新客户标签设置")
    @ApiOperation("更新客户标签设置")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('sms:smsLabelSet:update')")
    public Object updateSmsLabelSet(@RequestBody SmsLabelSet entity) {
        SmsLabelSet set = ISmsLabelSetService.getById(entity.getId());
        try {
            if (ISmsLabelSetService.updateById(entity)) {
                //如果更新了标签名称，则去微信更新
                if (!set.getLabelName().equals(entity.getLabelName())){
                    String tagName = entity.getLabelName();
                    AccountWechats wechats = messageService.getAppId(entity.getDealerId());
                    Boolean res = this.wxMpService.switchoverTo(wechats.getKey()).getUserTagService().tagUpdate(set.getTagId(), tagName);
                    if (!res){
                        ISmsLabelSetService.updateById(set);
                    }
                }
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新客户标签设置：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "删除客户标签设置")
    @ApiOperation("删除客户标签设置")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('sms:smsLabelSet:delete')")
    public Object deleteSmsLabelSet(@ApiParam("客户标签设置id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("客户标签设置id");
            }
            //先删微信的
            SmsLabelSet set = ISmsLabelSetService.getById(id);
            AccountWechats wechats = messageService.getAppId(set.getDealerId());
            Boolean res = this.wxMpService.switchoverTo(wechats.getKey()).getUserTagService().tagDelete(set.getTagId());
            if (!res){
                return new CommonResult().failed("无法删除系统标签或者该标签下粉丝数超过10w，不允许直接删除");
            }
            if (ISmsLabelSetService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除客户标签设置：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "给客户标签设置分配客户标签设置")
    @ApiOperation("查询客户标签设置明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('sms:smsLabelSet:read')")
    public Object getSmsLabelSetById(@ApiParam("客户标签设置id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("客户标签设置id");
            }
            SmsLabelSet coupon = ISmsLabelSetService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询客户标签设置明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除客户标签设置")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "sms", REMARK = "批量删除客户标签设置")
    @PreAuthorize("hasAuthority('sms:smsLabelSet:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = ISmsLabelSetService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "sms", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, SmsLabelSet entity) {
        // 模拟从数据库获取需要导出的数据
        List<SmsLabelSet> personList = ISmsLabelSetService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", SmsLabelSet.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "sms", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<SmsLabelSet> personList = EasyPoiUtils.importExcel(file, SmsLabelSet.class);
        ISmsLabelSetService.saveBatch(personList);
    }
}


