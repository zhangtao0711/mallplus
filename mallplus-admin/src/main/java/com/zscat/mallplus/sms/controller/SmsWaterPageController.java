package com.zscat.mallplus.sms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.sms.entity.SmsWaterPage;
import com.zscat.mallplus.sms.service.ISmsWaterPageService;
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
 * 购水页面
 */
@Slf4j
@RestController
@RequestMapping("/sms/smsWaterPage")
public class SmsWaterPageController {

    @Resource
    private ISmsWaterPageService ISmsWaterPageService;

    @SysLog(MODULE = "sms", REMARK = "根据条件查询所有购水页面列表")
    @ApiOperation("根据条件查询所有购水页面列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('sms:smsWaterPage:read')")
    public Object getSmsWaterPageByPage(SmsWaterPage entity,
                                        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(ISmsWaterPageService.page(new Page<SmsWaterPage>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有购水页面列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "保存购水页面")
    @ApiOperation("保存购水页面")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('sms:smsWaterPage:create')")
    public Object saveSmsWaterPage(@RequestBody SmsWaterPage entity) {
        try {
            entity.setCreateTime(new Date());
            if (ISmsWaterPageService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存购水页面：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "更新购水页面")
    @ApiOperation("更新购水页面")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('sms:smsWaterPage:update')")
    public Object updateSmsWaterPage(@RequestBody SmsWaterPage entity) {
        try {
            if (ISmsWaterPageService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新购水页面：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "删除购水页面")
    @ApiOperation("删除购水页面")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('sms:smsWaterPage:delete')")
    public Object deleteSmsWaterPage(@ApiParam("购水页面id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("购水页面id");
            }
            if (ISmsWaterPageService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除购水页面：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "给购水页面分配购水页面")
    @ApiOperation("查询购水页面明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('sms:smsWaterPage:read')")
    public Object getSmsWaterPageById(@ApiParam("购水页面id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("购水页面id");
            }
            SmsWaterPage coupon = ISmsWaterPageService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询购水页面明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除购水页面")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "sms", REMARK = "批量删除购水页面")
    @PreAuthorize("hasAuthority('sms:smsWaterPage:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = ISmsWaterPageService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "sms", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, SmsWaterPage entity) {
        // 模拟从数据库获取需要导出的数据
        List<SmsWaterPage> personList = ISmsWaterPageService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", SmsWaterPage.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "sms", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<SmsWaterPage> personList = EasyPoiUtils.importExcel(file, SmsWaterPage.class);
        ISmsWaterPageService.saveBatch(personList);
    }
}


