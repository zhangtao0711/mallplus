package com.zscat.mallplus.water.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.util.ConstantUtil;
import com.zscat.mallplus.water.entity.WtProductGroup;
import com.zscat.mallplus.water.service.IWtProductGroupService;
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
 * @date 2020-05-22
 * 设备分组
 */
@Slf4j
@RestController
@RequestMapping("/water/wtProductGroup")
public class WtProductGroupController {

    @Resource
    private IWtProductGroupService IWtProductGroupService;

    @SysLog(MODULE = "water", REMARK = "根据条件查询所有设备分组列表")
    @ApiOperation("根据条件查询所有设备分组列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('water:wtProductGroup:read')")
    public Object getWtProductGroupByPage(WtProductGroup entity,
                                          @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                          @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IWtProductGroupService.page(new Page<WtProductGroup>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有设备分组列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "保存设备分组")
    @ApiOperation("保存设备分组")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('water:wtProductGroup:create')")
    public Object saveWtProductGroup(@RequestBody WtProductGroup entity) {
        try {
            entity.setCreateTime(new Date());
            if (IWtProductGroupService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存设备分组：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "更新设备分组")
    @ApiOperation("更新设备分组")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('water:wtProductGroup:update')")
    public Object updateWtProductGroup(@RequestBody WtProductGroup entity) {
        try {
            entity.setUpdateTime(new Date());
            if (IWtProductGroupService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新设备分组：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "删除设备分组")
    @ApiOperation("删除设备分组")
    @GetMapping(value = "/delete/{id}")
//    @PreAuthorize("hasAuthority('water:wtProductGroup:delete')")
    public Object deleteWtProductGroup(@ApiParam("设备分组id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("设备分组id");
            }

            //判断分组下面是否绑定了设备
            if(IWtProductGroupService.checkProductExists(id,ConstantUtil.delFlag)){
                return new CommonResult().failed("组内有设备不允许删除！");
            }
            if (IWtProductGroupService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除设备分组：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "给设备分组分配设备分组")
    @ApiOperation("查询设备分组明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('water:wtProductGroup:read')")
    public Object getWtProductGroupById(@ApiParam("设备分组id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("设备分组id");
            }
            WtProductGroup coupon = IWtProductGroupService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询设备分组明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

//    @ApiOperation(value = "批量删除设备分组")
//    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
//    @SysLog(MODULE = "water", REMARK = "批量删除设备分组")
//    @PreAuthorize("hasAuthority('water:wtProductGroup:delete')")
//    public Object deleteBatch(@RequestParam("ids") List
//            <Long> ids) {
//        boolean count = IWtProductGroupService.removeByIds(ids);
//        if (count) {
//            return new CommonResult().success(count);
//        } else {
//            return new CommonResult().failed();
//        }
//    }


    @SysLog(MODULE = "water", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, WtProductGroup entity) {
        // 模拟从数据库获取需要导出的数据
        List<WtProductGroup> personList = IWtProductGroupService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", WtProductGroup.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "water", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<WtProductGroup> personList = EasyPoiUtils.importExcel(file, WtProductGroup.class);
        IWtProductGroupService.saveBatch(personList);
    }
}


