package com.zscat.mallplus.sms.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.sms.entity.SmsClassConfig;
import com.zscat.mallplus.sms.entity.SmsWaterPage;
import com.zscat.mallplus.sms.service.ISmsClassConfigService;
import com.zscat.mallplus.sms.service.ISmsWaterPageService;
import com.zscat.mallplus.util.EasyPoiUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.water.entity.WtEquipment;
import com.zscat.mallplus.water.service.IWtEquipmentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author wang
 * @date 2020-05-29
 * 购水设置
 */
@Slf4j
@RestController
@RequestMapping("/sms/smsClassConfig")
public class SmsClassConfigController {

    @Resource
    private ISmsClassConfigService ISmsClassConfigService;
    @Resource
    private ISmsWaterPageService smsWaterPageService;
    @Resource
    private IWtEquipmentService equipmentService;

    @SysLog(MODULE = "sms", REMARK = "根据条件查询所有购水设置列表")
    @ApiOperation("根据条件查询所有购水设置列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('sms:smsClassConfig:read')")
    public Object getSmsClassConfigByPage(SmsClassConfig entity,
                                          @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                          @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(ISmsClassConfigService.page(new Page<SmsClassConfig>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有购水设置列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "保存购水设置")
    @ApiOperation("保存购水设置")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('sms:smsClassConfig:create')")
    public Object saveSmsClassConfig(@RequestBody SmsClassConfig entity) {
        try {
            entity.setCreateTime(new Date());
            if (ISmsClassConfigService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存购水设置：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "更新购水设置")
    @ApiOperation("更新购水设置")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('sms:smsClassConfig:update')")
    public Object updateSmsClassConfig(@RequestBody SmsClassConfig entity) {
        try {
            if (ISmsClassConfigService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新购水设置：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "删除购水设置")
    @ApiOperation("删除购水设置")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('sms:smsClassConfig:delete')")
    public Object deleteSmsClassConfig(@ApiParam("购水设置id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("购水设置id");
            }
            if (ISmsClassConfigService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除购水设置：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "给购水设置分配购水设置")
    @ApiOperation("查询购水设置明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('sms:smsClassConfig:read')")
    public Object getSmsClassConfigById(@ApiParam("购水设置id") @PathVariable Long id) {
        JSONObject j = new JSONObject();
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("购水设置id");
            }
            SmsClassConfig coupon = ISmsClassConfigService.getById(id);
            j.put("coupon",coupon);
            List<String> waterIds = Arrays.asList(coupon.getWaterIds());
            List<SmsWaterPage> waterPages = new ArrayList<>();
            for (String water:waterIds){
                SmsWaterPage waterPage = smsWaterPageService.getById(water);
                waterPages.add(waterPage);
            }
            j.put("waterPages",waterPages);
            //亚楠设备还没做，设备做了现在
            Collection<WtEquipment> equipments = equipmentService.listByIds(Arrays.asList(coupon.getDevices().split(",")));
            j.put("equipments",equipments);
            return new CommonResult().success(j);
        } catch (Exception e) {
            log.error("查询购水设置明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除购水设置")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "sms", REMARK = "批量删除购水设置")
    @PreAuthorize("hasAuthority('sms:smsClassConfig:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = ISmsClassConfigService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "sms", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, SmsClassConfig entity) {
        // 模拟从数据库获取需要导出的数据
        List<SmsClassConfig> personList = ISmsClassConfigService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", SmsClassConfig.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "sms", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<SmsClassConfig> personList = EasyPoiUtils.importExcel(file, SmsClassConfig.class);
        ISmsClassConfigService.saveBatch(personList);
    }
}


