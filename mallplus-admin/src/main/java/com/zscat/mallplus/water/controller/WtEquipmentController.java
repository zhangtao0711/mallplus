package com.zscat.mallplus.water.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.util.ConstantUtil;
import com.zscat.mallplus.water.entity.WtEquipment;
import com.zscat.mallplus.water.service.IWtEquipmentService;
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
 * @date 2020-05-22
 * 设备信息
 */
@Slf4j
@RestController
@Api(tags = "WtEquipmentController", description = "设备信息")
@RequestMapping("/water/wtEquipment")
public class WtEquipmentController {

    @Resource
    private IWtEquipmentService IWtEquipmentService;

    @SysLog(MODULE = "water", REMARK = "根据条件查询所有设备信息列表")
    @ApiOperation("根据条件查询所有设备信息列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('water:wtEquipment:read')")
    public Object getWtEquipmentByPage(WtEquipment entity,
                                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IWtEquipmentService.page(new Page<WtEquipment>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有设备信息列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "保存设备信息")
    @ApiOperation("保存设备信息")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('water:wtEquipment:create')")
    public Object saveWtEquipment(@RequestBody WtEquipment entity) {
        try {
            entity.setDelFlag(ConstantUtil.delFlag);
            entity.setCreateTime(new Date());
            if (IWtEquipmentService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存设备信息：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "更新设备信息")
    @ApiOperation("更新设备信息")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('water:wtEquipment:update')")
    public Object updateWtEquipment(@RequestBody WtEquipment entity) {
        try {
            if (IWtEquipmentService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新设备信息：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "删除设备信息")
    @ApiOperation("删除设备信息")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('water:wtEquipment:delete')")
    public Object deleteWtEquipment(@ApiParam("设备信息id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("设备信息id");
            }
            if (IWtEquipmentService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除设备信息：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "给设备信息分配设备信息")
    @ApiOperation("查询设备信息明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('water:wtEquipment:read')")
    public Object getWtEquipmentById(@ApiParam("设备信息id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("设备信息id");
            }
            WtEquipment coupon = IWtEquipmentService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询设备信息明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除设备信息")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "water", REMARK = "批量删除设备信息")
    @PreAuthorize("hasAuthority('water:wtEquipment:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = IWtEquipmentService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "water", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, WtEquipment entity) {
        // 模拟从数据库获取需要导出的数据
        List<WtEquipment> personList = IWtEquipmentService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", WtEquipment.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "water", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<WtEquipment> personList = EasyPoiUtils.importExcel(file, WtEquipment.class);
        IWtEquipmentService.saveBatch(personList);
    }
}


