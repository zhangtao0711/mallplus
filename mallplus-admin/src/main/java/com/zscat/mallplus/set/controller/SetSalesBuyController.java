package com.zscat.mallplus.set.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.set.entity.SetSalesBuy;
import com.zscat.mallplus.set.service.ISetSalesBuyService;
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
 * 营销功能购买
 */
@Slf4j
@RestController
@RequestMapping("/set/setSalesBuy")
public class SetSalesBuyController {

    @Resource
    private ISetSalesBuyService ISetSalesBuyService;

    @SysLog(MODULE = "set", REMARK = "根据条件查询所有营销功能购买列表")
    @ApiOperation("根据条件查询所有营销功能购买列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('set:setSalesBuy:read')")
    public Object getSetSalesBuyByPage(SetSalesBuy entity,
                                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(ISetSalesBuyService.page(new Page<SetSalesBuy>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有营销功能购买列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "set", REMARK = "保存营销功能购买")
    @ApiOperation("保存营销功能购买")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('set:setSalesBuy:create')")
    public Object saveSetSalesBuy(@RequestBody SetSalesBuy entity) {
        try {
            entity.setCreateTime(new Date());
            if (ISetSalesBuyService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存营销功能购买：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "set", REMARK = "更新营销功能购买")
    @ApiOperation("更新营销功能购买")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('set:setSalesBuy:update')")
    public Object updateSetSalesBuy(@RequestBody SetSalesBuy entity) {
        try {
            if (ISetSalesBuyService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新营销功能购买：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "set", REMARK = "删除营销功能购买")
    @ApiOperation("删除营销功能购买")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('set:setSalesBuy:delete')")
    public Object deleteSetSalesBuy(@ApiParam("营销功能购买id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("营销功能购买id");
            }
            if (ISetSalesBuyService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除营销功能购买：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "set", REMARK = "给营销功能购买分配营销功能购买")
    @ApiOperation("查询营销功能购买明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('set:setSalesBuy:read')")
    public Object getSetSalesBuyById(@ApiParam("营销功能购买id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("营销功能购买id");
            }
            SetSalesBuy coupon = ISetSalesBuyService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询营销功能购买明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除营销功能购买")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "set", REMARK = "批量删除营销功能购买")
    @PreAuthorize("hasAuthority('set:setSalesBuy:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = ISetSalesBuyService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "set", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, SetSalesBuy entity) {
        // 模拟从数据库获取需要导出的数据
        List<SetSalesBuy> personList = ISetSalesBuyService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", SetSalesBuy.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "set", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<SetSalesBuy> personList = EasyPoiUtils.importExcel(file, SetSalesBuy.class);
        ISetSalesBuyService.saveBatch(personList);
    }
}


