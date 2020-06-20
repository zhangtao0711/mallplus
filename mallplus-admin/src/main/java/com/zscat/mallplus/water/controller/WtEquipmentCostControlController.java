package com.zscat.mallplus.water.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.water.entity.WtEquipmentCostControl;
import com.zscat.mallplus.water.service.IWtEquipmentCostControlService;
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
 * 设备成本管理
 */
@Slf4j
@RestController
@Api(tags = "WtEquipmentCostControlController", description = "售水机设备成本管理")
@RequestMapping("/water/wtEquipmentCostControl")
public class WtEquipmentCostControlController {

    @Resource
    private IWtEquipmentCostControlService IWtEquipmentCostControlService;

    @SysLog(MODULE = "water", REMARK = "根据条件查询所有设备成本管理列表")
    @ApiOperation("根据条件查询所有设备成本管理列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('water:wtEquipmentCostControl:read')")
    public Object getWtEquipmentCostControlByPage(WtEquipmentCostControl entity,
                                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                  @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IWtEquipmentCostControlService.page(new Page<WtEquipmentCostControl>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有设备成本管理列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "保存设备成本管理")
    @ApiOperation("保存设备成本管理")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('water:wtEquipmentCostControl:create')")
    public Object saveWtEquipmentCostControl(@RequestBody WtEquipmentCostControl entity) {
        try {
            entity.setUpdateTime(new Date());
            if (IWtEquipmentCostControlService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存设备成本管理：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "更新设备成本管理")
    @ApiOperation("更新设备成本管理")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('water:wtEquipmentCostControl:update')")
    public Object updateWtEquipmentCostControl(@RequestBody WtEquipmentCostControl entity) {
        try {
            entity.setUpdateTime(new Date());
            if (IWtEquipmentCostControlService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新设备成本管理：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "删除设备成本管理")
    @ApiOperation("删除设备成本管理")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('water:wtEquipmentCostControl:delete')")
    public Object deleteWtEquipmentCostControl(@ApiParam("设备成本管理id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("设备成本管理id");
            }
            if (IWtEquipmentCostControlService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除设备成本管理：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "给设备成本管理分配设备成本管理")
    @ApiOperation("查询设备成本管理明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('water:wtEquipmentCostControl:read')")
    public Object getWtEquipmentCostControlById(@ApiParam("设备成本管理id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("设备成本管理id");
            }
            WtEquipmentCostControl coupon = IWtEquipmentCostControlService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询设备成本管理明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除设备成本管理")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "water", REMARK = "批量删除设备成本管理")
    @PreAuthorize("hasAuthority('water:wtEquipmentCostControl:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = IWtEquipmentCostControlService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "water", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, WtEquipmentCostControl entity) {
        // 模拟从数据库获取需要导出的数据
        List<WtEquipmentCostControl> personList = IWtEquipmentCostControlService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", WtEquipmentCostControl.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "water", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<WtEquipmentCostControl> personList = EasyPoiUtils.importExcel(file, WtEquipmentCostControl.class);
        IWtEquipmentCostControlService.saveBatch(personList);
    }
}


