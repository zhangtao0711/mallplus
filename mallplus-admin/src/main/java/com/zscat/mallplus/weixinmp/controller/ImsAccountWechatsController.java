package com.zscat.mallplus.weixinmp.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.weixinmp.entity.ImsAccountWechats;
import com.zscat.mallplus.weixinmp.service.IImsAccountWechatsService;
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
 * 微信公众号
 */
@Slf4j
@RestController
@RequestMapping("/weixin/imsAccountWechats")
public class ImsAccountWechatsController {

    @Resource
    private IImsAccountWechatsService IImsAccountWechatsService;

    @SysLog(MODULE = "weixin", REMARK = "根据条件查询所有微信公众号列表")
    @ApiOperation("根据条件查询所有微信公众号列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('weixin:imsAccountWechats:read')")
    public Object getImsAccountWechatsByPage(ImsAccountWechats entity,
                                             @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                             @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IImsAccountWechatsService.page(new Page<ImsAccountWechats>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有微信公众号列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "weixin", REMARK = "保存微信公众号")
    @ApiOperation("保存微信公众号")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('weixin:imsAccountWechats:create')")
    public Object saveImsAccountWechats(@RequestBody ImsAccountWechats entity) {
//        WxPayApi.entrustWeb()
        try {
            entity.setCreateTime(new Date());

            if (IImsAccountWechatsService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存微信公众号：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "weixin", REMARK = "更新微信公众号")
    @ApiOperation("更新微信公众号")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('weixin:imsAccountWechats:update')")
    public Object updateImsAccountWechats(@RequestBody ImsAccountWechats entity) {
        try {
            if (IImsAccountWechatsService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新微信公众号：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "weixin", REMARK = "删除微信公众号")
    @ApiOperation("删除微信公众号")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('weixin:imsAccountWechats:delete')")
    public Object deleteImsAccountWechats(@ApiParam("微信公众号id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("微信公众号id");
            }
            if (IImsAccountWechatsService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除微信公众号：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "weixin", REMARK = "给微信公众号分配微信公众号")
    @ApiOperation("查询微信公众号明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('weixin:imsAccountWechats:read')")
    public Object getImsAccountWechatsById(@ApiParam("微信公众号id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("微信公众号id");
            }
            ImsAccountWechats coupon = IImsAccountWechatsService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询微信公众号明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除微信公众号")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "weixin", REMARK = "批量删除微信公众号")
    @PreAuthorize("hasAuthority('weixin:imsAccountWechats:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = IImsAccountWechatsService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "weixin", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, ImsAccountWechats entity) {
        // 模拟从数据库获取需要导出的数据
        List<ImsAccountWechats> personList = IImsAccountWechatsService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", ImsAccountWechats.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "weixin", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<ImsAccountWechats> personList = EasyPoiUtils.importExcel(file, ImsAccountWechats.class);
        IImsAccountWechatsService.saveBatch(personList);
    }
}


