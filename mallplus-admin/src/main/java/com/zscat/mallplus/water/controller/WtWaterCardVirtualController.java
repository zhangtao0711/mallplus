package com.zscat.mallplus.water.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.util.ConstantUtil;
import com.zscat.mallplus.water.entity.WtWaterCardVirtual;
import com.zscat.mallplus.water.service.IWtWaterCardVirtualService;
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
 * @author lyn
 * @date 2020-06-03
 * 虚拟水卡
 */
@Slf4j
@RestController
@RequestMapping("/water/wtWaterCardVirtual")
public class WtWaterCardVirtualController {

    @Resource
    private IWtWaterCardVirtualService IWtWaterCardVirtualService;

    @SysLog(MODULE = "water", REMARK = "根据条件查询所有虚拟水卡列表")
    @ApiOperation("根据条件查询所有虚拟水卡列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('water:wtWaterCardVirtual:read')")
    public Object getWtWaterCardVirtualByPage(WtWaterCardVirtual entity,
                                              @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                              @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IWtWaterCardVirtualService.page(new Page<WtWaterCardVirtual>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有虚拟水卡列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "保存虚拟水卡")
    @ApiOperation("保存虚拟水卡")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('water:wtWaterCardVirtual:create')")
    public Object saveWtWaterCardVirtual(@RequestBody WtWaterCardVirtual entity) {
        try {
            entity.setDelFlag(ConstantUtil.delFlag);
            entity.setCreateTime(new Date());
            if (IWtWaterCardVirtualService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存虚拟水卡：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "更新虚拟水卡")
    @ApiOperation("更新虚拟水卡")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('water:wtWaterCardVirtual:update')")
    public Object updateWtWaterCardVirtual(@RequestBody WtWaterCardVirtual entity) {
        try {
            entity.setUpdateTime(new Date());
            if (IWtWaterCardVirtualService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新虚拟水卡：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "删除虚拟水卡")
    @ApiOperation("删除虚拟水卡")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('water:wtWaterCardVirtual:delete')")
    public Object deleteWtWaterCardVirtual(@ApiParam("虚拟水卡id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("虚拟水卡id");
            }
            if (IWtWaterCardVirtualService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除虚拟水卡：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "给虚拟水卡分配虚拟水卡")
    @ApiOperation("查询虚拟水卡明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('water:wtWaterCardVirtual:read')")
    public Object getWtWaterCardVirtualById(@ApiParam("虚拟水卡id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("虚拟水卡id");
            }
            WtWaterCardVirtual coupon = IWtWaterCardVirtualService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询虚拟水卡明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除虚拟水卡")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "water", REMARK = "批量删除虚拟水卡")
    @PreAuthorize("hasAuthority('water:wtWaterCardVirtual:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = IWtWaterCardVirtualService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "water", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, WtWaterCardVirtual entity) {
        // 模拟从数据库获取需要导出的数据
        List<WtWaterCardVirtual> personList = IWtWaterCardVirtualService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", WtWaterCardVirtual.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "water", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<WtWaterCardVirtual> personList = EasyPoiUtils.importExcel(file, WtWaterCardVirtual.class);
        IWtWaterCardVirtualService.saveBatch(personList);
    }
}


