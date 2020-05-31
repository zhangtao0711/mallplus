package com.zscat.mallplus.sms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.sms.entity.SmsRechargeRecord;
import com.zscat.mallplus.sms.service.ISmsRechargeRecordService;
import com.zscat.mallplus.util.EasyPoiUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Date;

/**
 * @author wang
 * @date 2020-05-29
 * 充值记录
 */
@Slf4j
@RestController
@RequestMapping("/sms/smsRechargeRecord")
public class SmsRechargeRecordController {

    @Resource
    private ISmsRechargeRecordService ISmsRechargeRecordService;

    @SysLog(MODULE = "sms", REMARK = "根据条件查询所有充值记录列表")
    @ApiOperation("根据条件查询所有充值记录列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('sms:smsRechargeRecord:read')")
    public Object getSmsRechargeRecordByPage(SmsRechargeRecord entity,
                                             @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                             @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(ISmsRechargeRecordService.page(new Page<SmsRechargeRecord>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有充值记录列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "保存充值记录")
    @ApiOperation("保存充值记录")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('sms:smsRechargeRecord:create')")
    public Object saveSmsRechargeRecord(@RequestBody SmsRechargeRecord entity) {
        try {
            entity.setCreateTime(new Date());
            if (ISmsRechargeRecordService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存充值记录：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "更新充值记录")
    @ApiOperation("更新充值记录")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('sms:smsRechargeRecord:update')")
    public Object updateSmsRechargeRecord(@RequestBody SmsRechargeRecord entity) {
        try {
            if (ISmsRechargeRecordService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新充值记录：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "删除充值记录")
    @ApiOperation("删除充值记录")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('sms:smsRechargeRecord:delete')")
    public Object deleteSmsRechargeRecord(@ApiParam("充值记录id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("充值记录id");
            }
            if (ISmsRechargeRecordService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除充值记录：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "给充值记录分配充值记录")
    @ApiOperation("查询充值记录明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('sms:smsRechargeRecord:read')")
    public Object getSmsRechargeRecordById(@ApiParam("充值记录id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("充值记录id");
            }
            SmsRechargeRecord coupon = ISmsRechargeRecordService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询充值记录明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除充值记录")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "sms", REMARK = "批量删除充值记录")
    @PreAuthorize("hasAuthority('sms:smsRechargeRecord:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = ISmsRechargeRecordService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "sms", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, SmsRechargeRecord entity) {
        // 模拟从数据库获取需要导出的数据
        List<SmsRechargeRecord> personList = ISmsRechargeRecordService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", SmsRechargeRecord.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "sms", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<SmsRechargeRecord> personList = EasyPoiUtils.importExcel(file, SmsRechargeRecord.class);
        ISmsRechargeRecordService.saveBatch(personList);
    }
}


