package com.zscat.mallplus.water.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.water.entity.SimEntity;
import com.zscat.mallplus.water.entity.WtOpenApiInfo;
import com.zscat.mallplus.water.service.IWtOpenApiInfoService;
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
 * @date 2020-06-10
 * 硬件平台联系openApi
 */
@Slf4j
@RestController
@Api(tags = "WtOpenApiInfoController", description = "硬件平台联系openApi")
@RequestMapping("/water/wtOpenApiInfo")
public class WtOpenApiInfoController {

    @Resource
    private IWtOpenApiInfoService IWtOpenApiInfoService;

    @SysLog(MODULE = "water", REMARK = "根据条件查询所有硬件平台联系openApi列表")
    @ApiOperation("根据条件查询所有硬件平台联系openApi列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('water:wtOpenApiInfo:read')")
    public Object getWtOpenApiInfoByPage(WtOpenApiInfo entity,
                                         @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IWtOpenApiInfoService.page(new Page<WtOpenApiInfo>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有硬件平台联系openApi列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "保存硬件平台联系openApi")
    @ApiOperation("保存硬件平台联系openApi")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('water:wtOpenApiInfo:create')")
    public Object saveWtOpenApiInfo(@RequestBody WtOpenApiInfo entity) {
        try {
            entity.setCreateTime(new Date());
            if (IWtOpenApiInfoService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存硬件平台联系openApi：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "更新硬件平台联系openApi")
    @ApiOperation("更新硬件平台联系openApi")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('water:wtOpenApiInfo:update')")
    public Object updateWtOpenApiInfo(@RequestBody WtOpenApiInfo entity) {
        try {
            if (IWtOpenApiInfoService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新硬件平台联系openApi：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "删除硬件平台联系openApi")
    @ApiOperation("删除硬件平台联系openApi")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('water:wtOpenApiInfo:delete')")
    public Object deleteWtOpenApiInfo(@ApiParam("硬件平台联系openApiid") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("硬件平台联系openApiid");
            }
            if (IWtOpenApiInfoService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除硬件平台联系openApi：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "给硬件平台联系openApi分配硬件平台联系openApi")
    @ApiOperation("查询硬件平台联系openApi明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('water:wtOpenApiInfo:read')")
    public Object getWtOpenApiInfoById(@ApiParam("硬件平台联系openApiid") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("硬件平台联系openApiid");
            }
            WtOpenApiInfo coupon = IWtOpenApiInfoService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询硬件平台联系openApi明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除硬件平台联系openApi")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "water", REMARK = "批量删除硬件平台联系openApi")
    @PreAuthorize("hasAuthority('water:wtOpenApiInfo:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = IWtOpenApiInfoService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "water", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, WtOpenApiInfo entity) {
        // 模拟从数据库获取需要导出的数据
        List<WtOpenApiInfo> personList = IWtOpenApiInfoService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", WtOpenApiInfo.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "water", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<WtOpenApiInfo> personList = EasyPoiUtils.importExcel(file, WtOpenApiInfo.class);
        IWtOpenApiInfoService.saveBatch(personList);
    }


    @SysLog(MODULE = "water", REMARK = "连接硬件平台")
    @ApiOperation("连接硬件平台")
    @GetMapping(value = "/deviceInstanceQuery")
//    @PreAuthorize("hasAuthority('water:wtOpenApiInfo:read')")
    public Object deviceInstanceQuery(@ApiParam("条数")@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        try {
            Object coupon = IWtOpenApiInfoService.deviceInstanceQuery(pageSize);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("物联网卡余量查询：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }
}


