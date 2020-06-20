package com.zscat.mallplus.water.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.water.entity.WtEquipmentRemarks;
import com.zscat.mallplus.water.service.IWtEquipmentRemarksService;
import com.zscat.mallplus.util.EasyPoiUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import io.swagger.annotations.Api;
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
 * @author lyn
 * @date 2020-06-19
 * 设备备注/滤芯履历
 */
@Slf4j
@RestController
@Api(tags = "WtEquipmentRemarksController", description = "售水机设备备注/滤芯履历")
@RequestMapping("/water/wtEquipmentRemarks")
public class WtEquipmentRemarksController {

    @Resource
    private IWtEquipmentRemarksService IWtEquipmentRemarksService;

    @SysLog(MODULE = "water", REMARK = "根据条件查询所有设备备注/滤芯履历列表")
    @ApiOperation("根据条件查询所有设备备注/滤芯履历列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('water:wtEquipmentRemarks:read')")
    public Object getWtEquipmentRemarksByPage(WtEquipmentRemarks entity,
                                              @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                              @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IWtEquipmentRemarksService.page(new Page<WtEquipmentRemarks>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有设备备注/滤芯履历列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "保存设备备注/滤芯履历")
    @ApiOperation("保存设备备注/滤芯履历")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('water:wtEquipmentRemarks:create')")
    public Object saveWtEquipmentRemarks(@RequestBody WtEquipmentRemarks entity) {
        try {
            entity.setCreateTime(new Date());
            if (IWtEquipmentRemarksService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存设备备注/滤芯履历：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "更新设备备注/滤芯履历")
    @ApiOperation("更新设备备注/滤芯履历")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('water:wtEquipmentRemarks:update')")
    public Object updateWtEquipmentRemarks(@RequestBody WtEquipmentRemarks entity) {
        try {
            if (IWtEquipmentRemarksService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新设备备注/滤芯履历：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "删除设备备注/滤芯履历")
    @ApiOperation("删除设备备注/滤芯履历")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('water:wtEquipmentRemarks:delete')")
    public Object deleteWtEquipmentRemarks(@ApiParam("设备备注/滤芯履历id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("设备备注/滤芯履历id");
            }
            if (IWtEquipmentRemarksService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除设备备注/滤芯履历：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "给设备备注/滤芯履历分配设备备注/滤芯履历")
    @ApiOperation("查询设备备注/滤芯履历明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('water:wtEquipmentRemarks:read')")
    public Object getWtEquipmentRemarksById(@ApiParam("设备备注/滤芯履历id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("设备备注/滤芯履历id");
            }
            WtEquipmentRemarks coupon = IWtEquipmentRemarksService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询设备备注/滤芯履历明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除设备备注/滤芯履历")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "water", REMARK = "批量删除设备备注/滤芯履历")
    @PreAuthorize("hasAuthority('water:wtEquipmentRemarks:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = IWtEquipmentRemarksService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "water", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, WtEquipmentRemarks entity) {
        // 模拟从数据库获取需要导出的数据
        List<WtEquipmentRemarks> personList = IWtEquipmentRemarksService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", WtEquipmentRemarks.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "water", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<WtEquipmentRemarks> personList = EasyPoiUtils.importExcel(file, WtEquipmentRemarks.class);
        IWtEquipmentRemarksService.saveBatch(personList);
    }
}


