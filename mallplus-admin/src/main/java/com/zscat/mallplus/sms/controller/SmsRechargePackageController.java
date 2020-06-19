package com.zscat.mallplus.sms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.sms.entity.SmsRechargePackage;
import com.zscat.mallplus.sms.service.ISmsRechargePackageService;
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
 * @date 2020-05-30
 * 充值套餐
 */
@Slf4j
@RestController
@RequestMapping("/sms/smsRechargePackage")
public class SmsRechargePackageController {

    @Resource
    private ISmsRechargePackageService ISmsRechargePackageService;

    @SysLog(MODULE = "sms", REMARK = "根据条件查询所有充值套餐列表")
    @ApiOperation("根据条件查询所有充值套餐列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('sms:smsRechargePackage:read')")
    public Object getSmsRechargePackageByPage(SmsRechargePackage entity,
                                              @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                              @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(ISmsRechargePackageService.page(new Page<SmsRechargePackage>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有充值套餐列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "保存充值套餐")
    @ApiOperation("保存充值套餐")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('sms:smsRechargePackage:create')")
    public Object saveSmsRechargePackage(@RequestBody @Valid SmsRechargePackage entity) {
        try {
            entity.setCreateTime(new Date());
            if (ISmsRechargePackageService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存充值套餐：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "更新充值套餐")
    @ApiOperation("更新充值套餐")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('sms:smsRechargePackage:update')")
    public Object updateSmsRechargePackage(@RequestBody SmsRechargePackage entity) {
        try {
            if (ISmsRechargePackageService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新充值套餐：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "删除充值套餐")
    @ApiOperation("删除充值套餐")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('sms:smsRechargePackage:delete')")
    public Object deleteSmsRechargePackage(@ApiParam("充值套餐id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("充值套餐id");
            }
            if (ISmsRechargePackageService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除充值套餐：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "给充值套餐分配充值套餐")
    @ApiOperation("查询充值套餐明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('sms:smsRechargePackage:read')")
    public Object getSmsRechargePackageById(@ApiParam("充值套餐id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("充值套餐id");
            }
            SmsRechargePackage coupon = ISmsRechargePackageService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询充值套餐明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除充值套餐")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "sms", REMARK = "批量删除充值套餐")
    @PreAuthorize("hasAuthority('sms:smsRechargePackage:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = ISmsRechargePackageService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "sms", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, SmsRechargePackage entity) {
        // 模拟从数据库获取需要导出的数据
        List<SmsRechargePackage> personList = ISmsRechargePackageService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", SmsRechargePackage.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "sms", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<SmsRechargePackage> personList = EasyPoiUtils.importExcel(file, SmsRechargePackage.class);
        ISmsRechargePackageService.saveBatch(personList);
    }
}


