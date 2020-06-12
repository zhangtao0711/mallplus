package com.zscat.mallplus.water.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.util.ConstantUtil;
import com.zscat.mallplus.water.entity.WtEquipmentWarterCard;
import com.zscat.mallplus.water.service.IWtEquipmentWarterCardService;
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
 * @date 2020-05-30
 * 设备水卡关联信息表
 */
@Slf4j
@RestController
@Api(tags = "WtEquipmentWarterCardController", description = "设备水卡关联信息表")
@RequestMapping("/water/wtEquipmentWarterCard")
public class WtEquipmentWarterCardController {

    @Resource
    private IWtEquipmentWarterCardService IWtEquipmentWarterCardService;

    @SysLog(MODULE = "water", REMARK = "根据条件查询所有设备水卡关联信息表列表")
    @ApiOperation("根据条件查询所有设备水卡关联信息表列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('water:wtEquipmentWarterCard:read')")
    public Object getWtEquipmentWarterCardByPage(WtEquipmentWarterCard entity,
                                                 @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IWtEquipmentWarterCardService.page(new Page<WtEquipmentWarterCard>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有设备水卡关联信息表列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "保存设备水卡关联信息表")
    @ApiOperation("保存设备水卡关联信息表")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('water:wtEquipmentWarterCard:create')")
    public Object saveWtEquipmentWarterCard(@RequestBody WtEquipmentWarterCard entity) {
        try {
//            entity.setDelFlag(ConstantUtil.delFlag);
            if (IWtEquipmentWarterCardService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存设备水卡关联信息表：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "更新设备水卡关联信息表")
    @ApiOperation("更新设备水卡关联信息表")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('water:wtEquipmentWarterCard:update')")
    public Object updateWtEquipmentWarterCard(@RequestBody WtEquipmentWarterCard entity) {
        try {
            if (IWtEquipmentWarterCardService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新设备水卡关联信息表：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "删除设备水卡关联信息表")
    @ApiOperation("删除设备水卡关联信息表")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('water:wtEquipmentWarterCard:delete')")
    public Object deleteWtEquipmentWarterCard(@ApiParam("设备水卡关联信息表id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("设备水卡关联信息表id");
            }
            if (IWtEquipmentWarterCardService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除设备水卡关联信息表：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "给设备水卡关联信息表分配设备水卡关联信息表")
    @ApiOperation("查询设备水卡关联信息表明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('water:wtEquipmentWarterCard:read')")
    public Object getWtEquipmentWarterCardById(@ApiParam("设备水卡关联信息表id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("设备水卡关联信息表id");
            }
            WtEquipmentWarterCard coupon = IWtEquipmentWarterCardService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询设备水卡关联信息表明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除设备水卡关联信息表")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "water", REMARK = "批量删除设备水卡关联信息表")
    @PreAuthorize("hasAuthority('water:wtEquipmentWarterCard:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = IWtEquipmentWarterCardService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "water", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, WtEquipmentWarterCard entity) {
        // 模拟从数据库获取需要导出的数据
        List<WtEquipmentWarterCard> personList = IWtEquipmentWarterCardService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", WtEquipmentWarterCard.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "water", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<WtEquipmentWarterCard> personList = EasyPoiUtils.importExcel(file, WtEquipmentWarterCard.class);
        IWtEquipmentWarterCardService.saveBatch(personList);
    }
}


