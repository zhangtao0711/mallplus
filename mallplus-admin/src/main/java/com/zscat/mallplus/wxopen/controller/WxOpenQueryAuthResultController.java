package com.zscat.mallplus.wxopen.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.wxopen.entity.WxOpenQueryAuthResult;
import com.zscat.mallplus.wxopen.service.IWxOpenQueryAuthResultService;
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
 * @date 2020-06-17
 * 小程序创建记录
 */
@Slf4j
@RestController
@RequestMapping("/wxopen/wxOpenQueryAuthResult")
public class WxOpenQueryAuthResultController {

    @Resource
    private IWxOpenQueryAuthResultService IWxOpenQueryAuthResultService;

    @SysLog(MODULE = "wxopen", REMARK = "根据条件查询所有小程序创建记录列表")
    @ApiOperation("根据条件查询所有小程序创建记录列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('wxopen:wxOpenQueryAuthResult:read')")
    public Object getWxOpenQueryAuthResultByPage(WxOpenQueryAuthResult entity,
                                                 @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IWxOpenQueryAuthResultService.page(new Page<WxOpenQueryAuthResult>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有小程序创建记录列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "wxopen", REMARK = "保存小程序创建记录")
    @ApiOperation("保存小程序创建记录")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('wxopen:wxOpenQueryAuthResult:create')")
    public Object saveWxOpenQueryAuthResult(@RequestBody WxOpenQueryAuthResult entity) {
        try {
            entity.setCreateTime(new Date());
            if (IWxOpenQueryAuthResultService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存小程序创建记录：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "wxopen", REMARK = "更新小程序创建记录")
    @ApiOperation("更新小程序创建记录")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('wxopen:wxOpenQueryAuthResult:update')")
    public Object updateWxOpenQueryAuthResult(@RequestBody WxOpenQueryAuthResult entity) {
        try {
            if (IWxOpenQueryAuthResultService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新小程序创建记录：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "wxopen", REMARK = "删除小程序创建记录")
    @ApiOperation("删除小程序创建记录")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('wxopen:wxOpenQueryAuthResult:delete')")
    public Object deleteWxOpenQueryAuthResult(@ApiParam("小程序创建记录id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("小程序创建记录id");
            }
            if (IWxOpenQueryAuthResultService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除小程序创建记录：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "wxopen", REMARK = "给小程序创建记录分配小程序创建记录")
    @ApiOperation("查询小程序创建记录明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('wxopen:wxOpenQueryAuthResult:read')")
    public Object getWxOpenQueryAuthResultById(@ApiParam("小程序创建记录id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("小程序创建记录id");
            }
            WxOpenQueryAuthResult coupon = IWxOpenQueryAuthResultService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询小程序创建记录明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除小程序创建记录")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "wxopen", REMARK = "批量删除小程序创建记录")
    @PreAuthorize("hasAuthority('wxopen:wxOpenQueryAuthResult:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = IWxOpenQueryAuthResultService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "wxopen", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, WxOpenQueryAuthResult entity) {
        // 模拟从数据库获取需要导出的数据
        List<WxOpenQueryAuthResult> personList = IWxOpenQueryAuthResultService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", WxOpenQueryAuthResult.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "wxopen", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<WxOpenQueryAuthResult> personList = EasyPoiUtils.importExcel(file, WxOpenQueryAuthResult.class);
        IWxOpenQueryAuthResultService.saveBatch(personList);
    }
}


