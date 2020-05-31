package com.zscat.mallplus.sms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.set.service.ISetSalesBuyService;
import com.zscat.mallplus.sms.entity.SmsLabelSendMessage;
import com.zscat.mallplus.sms.service.ISmsLabelSendMessageService;
import com.zscat.mallplus.util.ConstantUtil;
import com.zscat.mallplus.util.EasyPoiUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpMassNews;
import me.chanjar.weixin.mp.bean.result.WxMpMassUploadResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;
import java.util.Date;

/**
 * @author wang
 * @date 2020-05-30
 * 手动发送记录
 */
@Slf4j
@RestController
@RequestMapping("/sms/smsLabelSendMessage")
public class SmsLabelSendMessageController {

    @Resource
    private ISmsLabelSendMessageService ISmsLabelSendMessageService;

    @SysLog(MODULE = "sms", REMARK = "根据条件查询所有手动发送记录列表")
    @ApiOperation("根据条件查询所有手动发送记录列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('sms:smsLabelSendMessage:read')")
    public Object getSmsLabelSendMessageByPage(SmsLabelSendMessage entity,
                                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                               @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(ISmsLabelSendMessageService.page(new Page<SmsLabelSendMessage>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有手动发送记录列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "保存手动发送记录")
    @ApiOperation("保存手动发送记录")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('sms:smsLabelSendMessage:create')")
    public Object saveSmsLabelSendMessage(@RequestBody SmsLabelSendMessage entity) {
        try {
            entity.setCreateTime(new Date());
            //发送消息到公众号或者小程序
            String msgId = ISmsLabelSendMessageService.sendWxMessage(entity);
            if (msgId==null){
                entity.setMsgId(null);
                entity.setStatus(ConstantUtil.fail);
            }else {
                entity.setMsgId(msgId);
                entity.setStatus(ConstantUtil.succeeded);
            }
            if (ISmsLabelSendMessageService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存手动发送记录：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "更新手动发送记录")
    @ApiOperation("更新手动发送记录")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('sms:smsLabelSendMessage:update')")
    public Object updateSmsLabelSendMessage(@RequestBody SmsLabelSendMessage entity) {
        try {
            if (ISmsLabelSendMessageService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新手动发送记录：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "删除手动发送记录")
    @ApiOperation("删除手动发送记录")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('sms:smsLabelSendMessage:delete')")
    public Object deleteSmsLabelSendMessage(@ApiParam("手动发送记录id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("手动发送记录id");
            }
            if (ISmsLabelSendMessageService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除手动发送记录：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "给手动发送记录分配手动发送记录")
    @ApiOperation("查询手动发送记录明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('sms:smsLabelSendMessage:read')")
    public Object getSmsLabelSendMessageById(@ApiParam("手动发送记录id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("手动发送记录id");
            }
            SmsLabelSendMessage coupon = ISmsLabelSendMessageService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询手动发送记录明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除手动发送记录")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "sms", REMARK = "批量删除手动发送记录")
    @PreAuthorize("hasAuthority('sms:smsLabelSendMessage:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = ISmsLabelSendMessageService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "sms", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, SmsLabelSendMessage entity) {
        // 模拟从数据库获取需要导出的数据
        List<SmsLabelSendMessage> personList = ISmsLabelSendMessageService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", SmsLabelSendMessage.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "sms", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<SmsLabelSendMessage> personList = EasyPoiUtils.importExcel(file, SmsLabelSendMessage.class);
        ISmsLabelSendMessageService.saveBatch(personList);
    }
}


