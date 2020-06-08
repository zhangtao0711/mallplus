package com.zscat.mallplus.ums.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.ums.entity.SysApaySet;
import com.zscat.mallplus.ums.service.ISysApaySetService;
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
 * @date 2020-06-07
 * 经销商的收款设置
 */
@Slf4j
@RestController
@RequestMapping("/ums/sysApaySet")
public class SysApaySetController {

    @Resource
    private ISysApaySetService ISysApaySetService;

    @SysLog(MODULE = "ums", REMARK = "根据条件查询所有经销商的收款设置列表")
    @ApiOperation("根据条件查询所有经销商的收款设置列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('ums:sysApaySet:read')")
    public Object getSysApaySetByPage(SysApaySet entity,
                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                      @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(ISysApaySetService.page(new Page<SysApaySet>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有经销商的收款设置列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "ums", REMARK = "保存经销商的收款设置")
    @ApiOperation("保存经销商的收款设置")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('ums:sysApaySet:create')")
    public Object saveSysApaySet(@RequestBody SysApaySet entity) {
        try {
            entity.setCreateTime(new Date());
            if (ISysApaySetService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存经销商的收款设置：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "ums", REMARK = "更新经销商的收款设置")
    @ApiOperation("更新经销商的收款设置")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('ums:sysApaySet:update')")
    public Object updateSysApaySet(@RequestBody SysApaySet entity) {
        try {
            if (ISysApaySetService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新经销商的收款设置：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "ums", REMARK = "删除经销商的收款设置")
    @ApiOperation("删除经销商的收款设置")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('ums:sysApaySet:delete')")
    public Object deleteSysApaySet(@ApiParam("经销商的收款设置id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("经销商的收款设置id");
            }
            if (ISysApaySetService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除经销商的收款设置：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "ums", REMARK = "给经销商的收款设置分配经销商的收款设置")
    @ApiOperation("查询经销商的收款设置明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ums:sysApaySet:read')")
    public Object getSysApaySetById(@ApiParam("经销商的收款设置id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("经销商的收款设置id");
            }
            SysApaySet coupon = ISysApaySetService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询经销商的收款设置明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除经销商的收款设置")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "ums", REMARK = "批量删除经销商的收款设置")
    @PreAuthorize("hasAuthority('ums:sysApaySet:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = ISysApaySetService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "ums", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, SysApaySet entity) {
        // 模拟从数据库获取需要导出的数据
        List<SysApaySet> personList = ISysApaySetService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", SysApaySet.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "ums", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<SysApaySet> personList = EasyPoiUtils.importExcel(file, SysApaySet.class);
        ISysApaySetService.saveBatch(personList);
    }
}


