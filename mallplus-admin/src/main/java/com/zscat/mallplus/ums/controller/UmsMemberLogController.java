package com.zscat.mallplus.ums.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.ums.entity.UmsMemberLog;
import com.zscat.mallplus.ums.service.IUmsMemberLogService;
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
 * @date 2020-06-06
 * 会员日志/备注
 */
@Slf4j
@RestController
@RequestMapping("/ums/umsMemberLog")
public class UmsMemberLogController {

    @Resource
    private IUmsMemberLogService IUmsMemberLogService;

    @SysLog(MODULE = "ums", REMARK = "根据条件查询所有会员日志/备注列表")
    @ApiOperation("根据条件查询所有会员日志/备注列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('ums:umsMemberLog:read')")
    public Object getUmsMemberLogByPage(UmsMemberLog entity,
                                        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IUmsMemberLogService.page(new Page<UmsMemberLog>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有会员日志/备注列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "ums", REMARK = "保存会员日志/备注")
    @ApiOperation("保存会员日志/备注")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('ums:umsMemberLog:create')")
    public Object saveUmsMemberLog(@RequestBody UmsMemberLog entity) {
        try {
            entity.setCreateTime(new Date());
            if (IUmsMemberLogService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存会员日志/备注：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "ums", REMARK = "更新会员日志/备注")
    @ApiOperation("更新会员日志/备注")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('ums:umsMemberLog:update')")
    public Object updateUmsMemberLog(@RequestBody UmsMemberLog entity) {
        try {
            if (IUmsMemberLogService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新会员日志/备注：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "ums", REMARK = "删除会员日志/备注")
    @ApiOperation("删除会员日志/备注")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('ums:umsMemberLog:delete')")
    public Object deleteUmsMemberLog(@ApiParam("会员日志/备注id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("会员日志/备注id");
            }
            if (IUmsMemberLogService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除会员日志/备注：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "ums", REMARK = "给会员日志/备注分配会员日志/备注")
    @ApiOperation("查询会员日志/备注明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ums:umsMemberLog:read')")
    public Object getUmsMemberLogById(@ApiParam("会员日志/备注id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("会员日志/备注id");
            }
            UmsMemberLog coupon = IUmsMemberLogService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询会员日志/备注明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除会员日志/备注")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "ums", REMARK = "批量删除会员日志/备注")
    @PreAuthorize("hasAuthority('ums:umsMemberLog:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = IUmsMemberLogService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "ums", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, UmsMemberLog entity) {
        // 模拟从数据库获取需要导出的数据
        List<UmsMemberLog> personList = IUmsMemberLogService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", UmsMemberLog.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "ums", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<UmsMemberLog> personList = EasyPoiUtils.importExcel(file, UmsMemberLog.class);
        IUmsMemberLogService.saveBatch(personList);
    }
}


