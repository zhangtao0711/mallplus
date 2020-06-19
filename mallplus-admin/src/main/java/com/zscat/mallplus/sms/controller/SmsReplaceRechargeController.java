package com.zscat.mallplus.sms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.sms.entity.SmsReplaceRecharge;
import com.zscat.mallplus.sms.service.ISmsReplaceRechargeService;
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
import javax.validation.Valid;
import java.util.List;
import java.util.Date;

/**
 * @author wang
 * @date 2020-05-28
 * 代客充值
 */
@Slf4j
@RestController
@RequestMapping("/sms/smsReplaceRecharge")
public class SmsReplaceRechargeController {

    @Resource
    private ISmsReplaceRechargeService ISmsReplaceRechargeService;

    @SysLog(MODULE = "sms", REMARK = "根据条件查询所有代客充值列表")
    @ApiOperation("根据条件查询所有代客充值列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('sms:smsReplaceRecharge:read')")
    public Object getSmsReplaceRechargeByPage(SmsReplaceRecharge entity,
                                              @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                              @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(ISmsReplaceRechargeService.page(new Page<SmsReplaceRecharge>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有代客充值列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "保存代客充值")
    @ApiOperation("保存代客充值")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('sms:smsReplaceRecharge:create')")
    public Object saveSmsReplaceRecharge(@RequestBody @Valid SmsReplaceRecharge entity) {
        try {
            entity.setCreateTime(new Date());
            if (ISmsReplaceRechargeService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存代客充值：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "更新代客充值")
    @ApiOperation("更新代客充值")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('sms:smsReplaceRecharge:update')")
    public Object updateSmsReplaceRecharge(@RequestBody SmsReplaceRecharge entity) {
        try {
            if (ISmsReplaceRechargeService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新代客充值：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "删除代客充值")
    @ApiOperation("删除代客充值")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('sms:smsReplaceRecharge:delete')")
    public Object deleteSmsReplaceRecharge(@ApiParam("代客充值id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("代客充值id");
            }
            if (ISmsReplaceRechargeService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除代客充值：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "给代客充值分配代客充值")
    @ApiOperation("查询代客充值明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('sms:smsReplaceRecharge:read')")
    public Object getSmsReplaceRechargeById(@ApiParam("代客充值id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("代客充值id");
            }
            SmsReplaceRecharge coupon = ISmsReplaceRechargeService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询代客充值明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除代客充值")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "sms", REMARK = "批量删除代客充值")
    @PreAuthorize("hasAuthority('sms:smsReplaceRecharge:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = ISmsReplaceRechargeService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "sms", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, SmsReplaceRecharge entity) {
        // 模拟从数据库获取需要导出的数据
        List<SmsReplaceRecharge> personList = ISmsReplaceRechargeService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", SmsReplaceRecharge.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "sms", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<SmsReplaceRecharge> personList = EasyPoiUtils.importExcel(file, SmsReplaceRecharge.class);
        ISmsReplaceRechargeService.saveBatch(personList);
    }
}


