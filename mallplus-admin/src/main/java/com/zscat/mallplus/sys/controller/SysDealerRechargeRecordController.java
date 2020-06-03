package com.zscat.mallplus.sys.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.sys.entity.SysDealerRechargeRecord;
import com.zscat.mallplus.sys.entity.SysUser;
import com.zscat.mallplus.sys.service.ISysDealerRechargeRecordService;
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
 * @date 2020-05-25
 * 经销商的余额充值记录
 */
@Slf4j
@RestController
@RequestMapping("/sys/sysDealerRechargeRecord")
public class SysDealerRechargeRecordController {

    @Resource
    private ISysDealerRechargeRecordService ISysDealerRechargeRecordService;
    @Resource
    private ISysUserService sysUserService;

    @SysLog(MODULE = "sys", REMARK = "根据条件查询所有经销商的余额充值记录列表")
    @ApiOperation("根据条件查询所有经销商的余额充值记录列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('sys:sysDealerRechargeRecord:read')")
    public Object getSysDealerRechargeRecordByPage(SysDealerRechargeRecord entity,
                                                   @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                   @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(ISysDealerRechargeRecordService.page(new Page<SysDealerRechargeRecord>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有经销商的余额充值记录列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "查询所有的消费记录")
    @ApiOperation("查询所有的消费记录")
    @GetMapping(value = "/getPurchaseHistory")
    @PreAuthorize("hasAuthority('sys:sysDealerRechargeRecord:read')")
    public Object getPurchaseHistory(SysDealerRechargeRecord entity) {
        return new CommonResult().success(ISysDealerRechargeRecordService.getSaleList(entity.getDealerId()));
    }

    @SysLog(MODULE = "sys", REMARK = "保存经销商的余额充值记录")
    @ApiOperation("保存经销商的余额充值记录")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('sys:sysDealerRechargeRecord:create')")
    public Object saveSysDealerRechargeRecord(@RequestBody SysDealerRechargeRecord entity) {
        try {
            entity.setCreateTime(new Date());
            if (ISysDealerRechargeRecordService.save(entity)) {
                //充值的钱更新
                SysUser user = sysUserService.getById(entity.getDealerId());
                user.setBalance(entity.getNewBalance());
                sysUserService.updateById(user);
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存经销商的余额充值记录：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "更新经销商的余额充值记录")
    @ApiOperation("更新经销商的余额充值记录")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('sys:sysDealerRechargeRecord:update')")
    public Object updateSysDealerRechargeRecord(@RequestBody SysDealerRechargeRecord entity) {
        try {
            if (ISysDealerRechargeRecordService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新经销商的余额充值记录：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "删除经销商的余额充值记录")
    @ApiOperation("删除经销商的余额充值记录")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('sys:sysDealerRechargeRecord:delete')")
    public Object deleteSysDealerRechargeRecord(@ApiParam("经销商的余额充值记录id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("经销商的余额充值记录id");
            }
            if (ISysDealerRechargeRecordService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除经销商的余额充值记录：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "给经销商的余额充值记录分配经销商的余额充值记录")
    @ApiOperation("查询经销商的余额充值记录明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('sys:sysDealerRechargeRecord:read')")
    public Object getSysDealerRechargeRecordById(@ApiParam("经销商的余额充值记录id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("经销商的余额充值记录id");
            }
            SysDealerRechargeRecord coupon = ISysDealerRechargeRecordService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询经销商的余额充值记录明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除经销商的余额充值记录")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "sys", REMARK = "批量删除经销商的余额充值记录")
    @PreAuthorize("hasAuthority('sys:sysDealerRechargeRecord:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = ISysDealerRechargeRecordService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "sys", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, SysDealerRechargeRecord entity) {
        // 模拟从数据库获取需要导出的数据
        List<SysDealerRechargeRecord> personList = ISysDealerRechargeRecordService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", SysDealerRechargeRecord.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "sys", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<SysDealerRechargeRecord> personList = EasyPoiUtils.importExcel(file, SysDealerRechargeRecord.class);
        ISysDealerRechargeRecordService.saveBatch(personList);
    }
}


