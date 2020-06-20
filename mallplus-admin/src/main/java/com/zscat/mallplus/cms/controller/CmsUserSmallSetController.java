package com.zscat.mallplus.cms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.cms.entity.CmsUserSmallSet;
import com.zscat.mallplus.cms.service.ICmsUserSmallSetService;
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
 * @date 2020-06-18
 * 用户小程序基础设置
 */
@Slf4j
@RestController
@Api(tags = "CmsUserSmallSetController", description = "用户小程序基础设置")
@RequestMapping("/cms/cmsUserSmallSet")
public class CmsUserSmallSetController {

    @Resource
    private ICmsUserSmallSetService ICmsUserSmallSetService;

    @SysLog(MODULE = "cms", REMARK = "根据条件查询所有用户小程序基础设置列表")
    @ApiOperation("根据条件查询所有用户小程序基础设置列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('cms:cmsUserSmallSet:read')")
    public Object getCmsUserSmallSetByPage(CmsUserSmallSet entity,
                                           @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                           @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(ICmsUserSmallSetService.page(new Page<CmsUserSmallSet>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有用户小程序基础设置列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "cms", REMARK = "保存用户小程序基础设置")
    @ApiOperation("保存用户小程序基础设置")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('cms:cmsUserSmallSet:create')")
    public Object saveCmsUserSmallSet(@RequestBody CmsUserSmallSet entity) {
        try {
            entity.setCreateTime(new Date());
            if (ICmsUserSmallSetService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存用户小程序基础设置：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "cms", REMARK = "更新用户小程序基础设置")
    @ApiOperation("更新用户小程序基础设置")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('cms:cmsUserSmallSet:update')")
    public Object updateCmsUserSmallSet(@RequestBody CmsUserSmallSet entity) {
        try {
            entity.setUpdateTime(new Date());
            if (ICmsUserSmallSetService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新用户小程序基础设置：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "cms", REMARK = "删除用户小程序基础设置")
    @ApiOperation("删除用户小程序基础设置")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('cms:cmsUserSmallSet:delete')")
    public Object deleteCmsUserSmallSet(@ApiParam("用户小程序基础设置id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("用户小程序基础设置id");
            }
            if (ICmsUserSmallSetService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除用户小程序基础设置：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "cms", REMARK = "给用户小程序基础设置分配用户小程序基础设置")
    @ApiOperation("查询用户小程序基础设置明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('cms:cmsUserSmallSet:read')")
    public Object getCmsUserSmallSetById(@ApiParam("用户小程序基础设置id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("用户小程序基础设置id");
            }
            CmsUserSmallSet coupon = ICmsUserSmallSetService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询用户小程序基础设置明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除用户小程序基础设置")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "cms", REMARK = "批量删除用户小程序基础设置")
    @PreAuthorize("hasAuthority('cms:cmsUserSmallSet:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = ICmsUserSmallSetService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "cms", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, CmsUserSmallSet entity) {
        // 模拟从数据库获取需要导出的数据
        List<CmsUserSmallSet> personList = ICmsUserSmallSetService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", CmsUserSmallSet.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "cms", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<CmsUserSmallSet> personList = EasyPoiUtils.importExcel(file, CmsUserSmallSet.class);
        ICmsUserSmallSetService.saveBatch(personList);
    }
}


