package com.zscat.mallplus.sms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.sms.entity.SmsWaterBuyRecord;
import com.zscat.mallplus.sms.service.ISmsWaterBuyRecordService;
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
 * @date 2020-05-30
 * 扫码购水记录
 */
@Slf4j
@RestController
@RequestMapping("/sms/smsWaterBuyRecord")
public class SmsWaterBuyRecordController {

    @Resource
    private ISmsWaterBuyRecordService ISmsWaterBuyRecordService;

    @SysLog(MODULE = "sms", REMARK = "根据条件查询所有扫码购水记录列表")
    @ApiOperation("根据条件查询所有扫码购水记录列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('sms:smsWaterBuyRecord:read')")
    public Object getSmsWaterBuyRecordByPage(SmsWaterBuyRecord entity,
                                             @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                             @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(ISmsWaterBuyRecordService.page(new Page<SmsWaterBuyRecord>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有扫码购水记录列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "保存扫码购水记录")
    @ApiOperation("保存扫码购水记录")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('sms:smsWaterBuyRecord:create')")
    public Object saveSmsWaterBuyRecord(@RequestBody SmsWaterBuyRecord entity) {
        try {
            entity.setCreateTime(new Date());
            if (ISmsWaterBuyRecordService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存扫码购水记录：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "更新扫码购水记录")
    @ApiOperation("更新扫码购水记录")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('sms:smsWaterBuyRecord:update')")
    public Object updateSmsWaterBuyRecord(@RequestBody SmsWaterBuyRecord entity) {
        try {
            if (ISmsWaterBuyRecordService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新扫码购水记录：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "删除扫码购水记录")
    @ApiOperation("删除扫码购水记录")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('sms:smsWaterBuyRecord:delete')")
    public Object deleteSmsWaterBuyRecord(@ApiParam("扫码购水记录id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("扫码购水记录id");
            }
            if (ISmsWaterBuyRecordService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除扫码购水记录：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "给扫码购水记录分配扫码购水记录")
    @ApiOperation("查询扫码购水记录明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('sms:smsWaterBuyRecord:read')")
    public Object getSmsWaterBuyRecordById(@ApiParam("扫码购水记录id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("扫码购水记录id");
            }
            SmsWaterBuyRecord coupon = ISmsWaterBuyRecordService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询扫码购水记录明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除扫码购水记录")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "sms", REMARK = "批量删除扫码购水记录")
    @PreAuthorize("hasAuthority('sms:smsWaterBuyRecord:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = ISmsWaterBuyRecordService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "sms", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, SmsWaterBuyRecord entity) {
        // 模拟从数据库获取需要导出的数据
        List<SmsWaterBuyRecord> personList = ISmsWaterBuyRecordService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", SmsWaterBuyRecord.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "sms", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<SmsWaterBuyRecord> personList = EasyPoiUtils.importExcel(file, SmsWaterBuyRecord.class);
        ISmsWaterBuyRecordService.saveBatch(personList);
    }
}


