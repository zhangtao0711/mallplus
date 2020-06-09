package com.zscat.mallplus.water.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.water.entity.WtSimSendOrderWx;
import com.zscat.mallplus.water.service.IWtSimSendOrderWxService;
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
 * @date 2020-06-09
 * SIM卡充值记录
 */
@Slf4j
@RestController
@RequestMapping("/water/wtSimSendOrder")
public class WtSimSendOrderWxController {

    @Resource
    private IWtSimSendOrderWxService IWtSimSendOrderService;

    @SysLog(MODULE = "water", REMARK = "根据条件查询所有SIM卡充值记录列表")
    @ApiOperation("根据条件查询所有SIM卡充值记录列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('water:wtSimSendOrder:read')")
    public Object getWtSimSendOrderByPage(WtSimSendOrderWx entity,
                                          @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                          @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IWtSimSendOrderService.page(new Page<WtSimSendOrderWx>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有SIM卡充值记录列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "保存SIM卡充值记录")
    @ApiOperation("保存SIM卡充值记录")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('water:wtSimSendOrder:create')")
    public Object saveWtSimSendOrder(@RequestBody WtSimSendOrderWx entity) {
        try {
            entity.setCreateTime(new Date());
            if (IWtSimSendOrderService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存SIM卡充值记录：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "更新SIM卡充值记录")
    @ApiOperation("更新SIM卡充值记录")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('water:wtSimSendOrder:update')")
    public Object updateWtSimSendOrder(@RequestBody WtSimSendOrderWx entity) {
        try {
            if (IWtSimSendOrderService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新SIM卡充值记录：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "删除SIM卡充值记录")
    @ApiOperation("删除SIM卡充值记录")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('water:wtSimSendOrder:delete')")
    public Object deleteWtSimSendOrder(@ApiParam("SIM卡充值记录id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("SIM卡充值记录id");
            }
            if (IWtSimSendOrderService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除SIM卡充值记录：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "给SIM卡充值记录分配SIM卡充值记录")
    @ApiOperation("查询SIM卡充值记录明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('water:wtSimSendOrder:read')")
    public Object getWtSimSendOrderById(@ApiParam("SIM卡充值记录id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("SIM卡充值记录id");
            }
            WtSimSendOrderWx coupon = IWtSimSendOrderService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询SIM卡充值记录明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除SIM卡充值记录")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "water", REMARK = "批量删除SIM卡充值记录")
    @PreAuthorize("hasAuthority('water:wtSimSendOrder:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = IWtSimSendOrderService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "water", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, WtSimSendOrderWx entity) {
        // 模拟从数据库获取需要导出的数据
        List<WtSimSendOrderWx> personList = IWtSimSendOrderService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", WtSimSendOrderWx.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "water", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<WtSimSendOrderWx> personList = EasyPoiUtils.importExcel(file, WtSimSendOrderWx.class);
        IWtSimSendOrderService.saveBatch(personList);
    }
}


