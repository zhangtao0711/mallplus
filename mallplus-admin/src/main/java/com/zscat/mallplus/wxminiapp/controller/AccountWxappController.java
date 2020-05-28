package com.zscat.mallplus.wxminiapp.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.wxminiapp.entity.AccountWxapp;
import com.zscat.mallplus.wxminiapp.service.IAccountWxappService;
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
 * @date 2020-05-23
 * 小程序
 */
@Slf4j
@RestController
@RequestMapping("/wxminiapp/accountWxapp")
public class AccountWxappController {

    @Resource
    private IAccountWxappService IAccountWxappService;

    @SysLog(MODULE = "wxminiapp", REMARK = "根据条件查询所有小程序列表")
    @ApiOperation("根据条件查询所有小程序列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('wxminiapp:imsAccountWxapp:read')")
    public Object getImsAccountWxappByPage(AccountWxapp entity,
                                           @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                           @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IAccountWxappService.page(new Page<AccountWxapp>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有小程序列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "wxminiapp", REMARK = "保存小程序")
    @ApiOperation("保存小程序")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('wxminiapp:imsAccountWxapp:create')")
    public Object saveImsAccountWxapp(@RequestBody AccountWxapp entity) {
        AccountWxapp accountWxapp = new AccountWxapp();
        accountWxapp.setCreateBy(entity.getCreateBy());
        AccountWxapp wxapp = IAccountWxappService.getOne(new QueryWrapper<>(accountWxapp));
        if (wxapp!=null){
            return new CommonResult().failed("该经销商已经关联过小程序，请勿重复关联！");
        }
        try {
            entity.setCreateTime(new Date());
            if (IAccountWxappService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存小程序：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "wxminiapp", REMARK = "更新小程序")
    @ApiOperation("更新小程序")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('wxminiapp:imsAccountWxapp:update')")
    public Object updateImsAccountWxapp(@RequestBody AccountWxapp entity) {
        try {
            if (IAccountWxappService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新小程序：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "wxminiapp", REMARK = "删除小程序")
    @ApiOperation("删除小程序")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('wxminiapp:imsAccountWxapp:delete')")
    public Object deleteImsAccountWxapp(@ApiParam("小程序id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("小程序id");
            }
            if (IAccountWxappService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除小程序：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "wxminiapp", REMARK = "给小程序分配小程序")
    @ApiOperation("查询小程序明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('wxminiapp:imsAccountWxapp:read')")
    public Object getImsAccountWxappById(@ApiParam("小程序id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("小程序id");
            }
            AccountWxapp wxapp = new AccountWxapp();
            wxapp.setCreateBy(id);
            AccountWxapp coupon = IAccountWxappService.getOne(new QueryWrapper<>(wxapp));
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询小程序明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除小程序")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "wxminiapp", REMARK = "批量删除小程序")
    @PreAuthorize("hasAuthority('wxminiapp:imsAccountWxapp:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = IAccountWxappService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "wxminiapp", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, AccountWxapp entity) {
        // 模拟从数据库获取需要导出的数据
        List<AccountWxapp> personList = IAccountWxappService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", AccountWxapp.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "wxminiapp", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<AccountWxapp> personList = EasyPoiUtils.importExcel(file, AccountWxapp.class);
        IAccountWxappService.saveBatch(personList);
    }
}


