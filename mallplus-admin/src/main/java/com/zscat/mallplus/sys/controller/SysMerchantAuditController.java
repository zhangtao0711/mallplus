package com.zscat.mallplus.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.sys.entity.SysMerchantAudit;
import com.zscat.mallplus.sys.entity.SysUser;
import com.zscat.mallplus.sys.service.ISysMerchantAuditService;
import com.zscat.mallplus.sys.service.ISysUserService;
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
 * @date 2020-05-26
 * 经销商交叉授权
 */
@Slf4j
@RestController
@RequestMapping("/sys/sysMerchantAudit")
public class SysMerchantAuditController {

    @Resource
    private ISysMerchantAuditService ISysMerchantAuditService;
    @Resource
    private ISysUserService sysUserService;

    @SysLog(MODULE = "sys", REMARK = "根据条件查询所有经销商交叉授权列表")
    @ApiOperation("根据条件查询所有经销商交叉授权列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('sys:sysMerchantAudit:read')")
    public Object getSysMerchantAuditByPage(SysMerchantAudit entity,
                                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(ISysMerchantAuditService.page(new Page<SysMerchantAudit>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有经销商交叉授权列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "保存经销商交叉授权")
    @ApiOperation("保存经销商交叉授权")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('sys:sysMerchantAudit:create')")
    public Object saveSysMerchantAudit(@RequestBody SysMerchantAudit entity) {
        SysUser user = new SysUser();
        user.setUsername(entity.getUsername());
        SysUser sysUser = sysUserService.getOne(new QueryWrapper<>(user));
        if (sysUser==null){
            return new CommonResult().failed("授权商家不存在，请检查填写是否正确！");
        }
        try {
            entity.setUpdateTime(new Date());
            if (ISysMerchantAuditService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存经销商交叉授权：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "更新经销商交叉授权")
    @ApiOperation("更新经销商交叉授权")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('sys:sysMerchantAudit:update')")
    public Object updateSysMerchantAudit(@RequestBody SysMerchantAudit entity) {
        try {
            if (ISysMerchantAuditService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新经销商交叉授权：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "删除经销商交叉授权")
    @ApiOperation("删除经销商交叉授权")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('sys:sysMerchantAudit:delete')")
    public Object deleteSysMerchantAudit(@ApiParam("经销商交叉授权id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("经销商交叉授权id");
            }
            if (ISysMerchantAuditService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除经销商交叉授权：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "给经销商交叉授权分配经销商交叉授权")
    @ApiOperation("查询经销商交叉授权明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('sys:sysMerchantAudit:read')")
    public Object getSysMerchantAuditById(@ApiParam("经销商交叉授权id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("经销商交叉授权id");
            }
            SysMerchantAudit coupon = ISysMerchantAuditService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询经销商交叉授权明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除经销商交叉授权")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "sys", REMARK = "批量删除经销商交叉授权")
    @PreAuthorize("hasAuthority('sys:sysMerchantAudit:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = ISysMerchantAuditService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "sys", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, SysMerchantAudit entity) {
        // 模拟从数据库获取需要导出的数据
        List<SysMerchantAudit> personList = ISysMerchantAuditService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", SysMerchantAudit.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "sys", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<SysMerchantAudit> personList = EasyPoiUtils.importExcel(file, SysMerchantAudit.class);
        ISysMerchantAuditService.saveBatch(personList);
    }
}


