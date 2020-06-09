package com.zscat.mallplus.water.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.water.entity.WtWaterCardVituralConsume;
import com.zscat.mallplus.water.service.IWtWaterCardVituralConsumeService;
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
 * @date 2020-06-06
 * 虚拟水卡消费记录
 */
@Slf4j
@RestController
@RequestMapping("/water/wtWaterCardVituralConsume")
public class WtWaterCardVituralConsumeController {

    @Resource
    private IWtWaterCardVituralConsumeService IWtWaterCardVituralConsumeService;

    @SysLog(MODULE = "water", REMARK = "根据条件查询所有虚拟水卡消费记录列表")
    @ApiOperation("根据条件查询所有虚拟水卡消费记录列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('water:wtWaterCardVituralConsume:read')")
    public Object getWtWaterCardVituralConsumeByPage(WtWaterCardVituralConsume entity,
                                                     @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                     @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IWtWaterCardVituralConsumeService.page(new Page<WtWaterCardVituralConsume>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有虚拟水卡消费记录列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "保存虚拟水卡消费记录")
    @ApiOperation("保存虚拟水卡消费记录")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('water:wtWaterCardVituralConsume:create')")
    public Object saveWtWaterCardVituralConsume(@RequestBody WtWaterCardVituralConsume entity) {
        try {
            entity.setCreateTime(new Date());
            if (IWtWaterCardVituralConsumeService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存虚拟水卡消费记录：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "更新虚拟水卡消费记录")
    @ApiOperation("更新虚拟水卡消费记录")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('water:wtWaterCardVituralConsume:update')")
    public Object updateWtWaterCardVituralConsume(@RequestBody WtWaterCardVituralConsume entity) {
        try {
            if (IWtWaterCardVituralConsumeService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新虚拟水卡消费记录：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "删除虚拟水卡消费记录")
    @ApiOperation("删除虚拟水卡消费记录")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('water:wtWaterCardVituralConsume:delete')")
    public Object deleteWtWaterCardVituralConsume(@ApiParam("虚拟水卡消费记录id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("虚拟水卡消费记录id");
            }
            if (IWtWaterCardVituralConsumeService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除虚拟水卡消费记录：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "给虚拟水卡消费记录分配虚拟水卡消费记录")
    @ApiOperation("查询虚拟水卡消费记录明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('water:wtWaterCardVituralConsume:read')")
    public Object getWtWaterCardVituralConsumeById(@ApiParam("虚拟水卡消费记录id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("虚拟水卡消费记录id");
            }
            WtWaterCardVituralConsume coupon = IWtWaterCardVituralConsumeService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询虚拟水卡消费记录明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除虚拟水卡消费记录")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "water", REMARK = "批量删除虚拟水卡消费记录")
    @PreAuthorize("hasAuthority('water:wtWaterCardVituralConsume:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = IWtWaterCardVituralConsumeService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "water", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, WtWaterCardVituralConsume entity) {
        // 模拟从数据库获取需要导出的数据
        List<WtWaterCardVituralConsume> personList = IWtWaterCardVituralConsumeService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", WtWaterCardVituralConsume.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "water", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<WtWaterCardVituralConsume> personList = EasyPoiUtils.importExcel(file, WtWaterCardVituralConsume.class);
        IWtWaterCardVituralConsumeService.saveBatch(personList);
    }
}


