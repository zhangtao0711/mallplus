package com.zscat.mallplus.water.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.util.ConstantUtil;
import com.zscat.mallplus.util.EasyPoiUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.water.entity.WtFilterElementType;
import com.zscat.mallplus.water.service.IWtFilterElementTypeService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * @author lyn
 * @date 2020-05-20
 * 滤芯类型
 */
@Slf4j
@Component
@RestController
@RequestMapping("/water/wtFilterElementType")
public class WtFilterElementTypeController {

    @Resource
    private IWtFilterElementTypeService IWtFilterElementTypeService;

//    @SysLog(MODULE = "water", REMARK = "根据条件查询所有滤芯类型列表")
//    @ApiOperation("根据条件查询所有滤芯类型列表")
    @GetMapping(value = "/list")
//    @PreAuthorize("hasAuthority('water:wtFilterElementType:read')")
    public Object getWtFilterElementTypeByPage(WtFilterElementType entity,
                                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                               @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IWtFilterElementTypeService.page(new Page<WtFilterElementType>(pageNum, pageSize),new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有滤芯类型列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "保存滤芯类型")
    @ApiOperation("保存滤芯类型")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('water:wtFilterElementType:create')")
    public Object saveWtFilterElementType(@RequestBody WtFilterElementType entity) {
        try {
            entity.setDelFlag(ConstantUtil.delFlag);
            entity.setCreateTime(new Date());
            if (IWtFilterElementTypeService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存滤芯类型：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "更新滤芯类型")
    @ApiOperation("更新滤芯类型")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('water:wtFilterElementType:update')")
    public Object updateWtFilterElementType(@RequestBody WtFilterElementType entity) {
        try {
            entity.setUpdateTime(new Date());
            if (IWtFilterElementTypeService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新滤芯类型：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "删除滤芯类型")
    @ApiOperation("删除滤芯类型")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('water:wtFilterElementType:delete')")
    public Object deleteWtFilterElementType(@ApiParam("滤芯类型id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("滤芯类型id");
            }
            if (IWtFilterElementTypeService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除滤芯类型：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "给滤芯类型分配滤芯类型")
    @ApiOperation("查询滤芯类型明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('water:wtFilterElementType:read')")
    public Object getWtFilterElementTypeById(@ApiParam("滤芯类型id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("滤芯类型id");
            }
            WtFilterElementType coupon = IWtFilterElementTypeService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询滤芯类型明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除滤芯类型")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "water", REMARK = "批量删除滤芯类型")
    @PreAuthorize("hasAuthority('water:wtFilterElementType:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = IWtFilterElementTypeService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "water", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, WtFilterElementType entity) {
        // 模拟从数据库获取需要导出的数据
        List<WtFilterElementType> personList = IWtFilterElementTypeService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", WtFilterElementType.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "water", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<WtFilterElementType> personList = EasyPoiUtils.importExcel(file, WtFilterElementType.class);
        IWtFilterElementTypeService.saveBatch(personList);
    }
}


