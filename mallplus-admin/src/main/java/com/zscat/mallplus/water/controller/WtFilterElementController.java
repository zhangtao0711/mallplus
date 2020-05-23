package com.zscat.mallplus.water.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.water.entity.WtFilterElement;
import com.zscat.mallplus.water.service.IWtFilterElementService;
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
 * @date 2020-05-20
 * 滤芯
 */
@Slf4j
@RestController
@RequestMapping("/water/wtFilterElement")
public class WtFilterElementController {

    @Resource
    private IWtFilterElementService IWtFilterElementService;

    @SysLog(MODULE = "water", REMARK = "根据条件查询所有滤芯列表")
    @ApiOperation("根据条件查询所有滤芯列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('water:wtFilterElement:read')")
    public Object getWtFilterElementByPage(WtFilterElement entity,
                                           @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                           @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IWtFilterElementService.page(new Page<WtFilterElement>(pageNum, pageSize), new QueryWrapper<>(entity).orderByDesc("create_time")));
        } catch (Exception e) {
            log.error("根据条件查询所有滤芯列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "保存滤芯")
    @ApiOperation("保存滤芯")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('water:wtFilterElement:create')")
    public Object saveWtFilterElement(@RequestBody WtFilterElement entity) {
        try {
            entity.setCreateTime(new Date());
            if (IWtFilterElementService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存滤芯：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "更新滤芯")
    @ApiOperation("更新滤芯")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('water:wtFilterElement:update')")
    public Object updateWtFilterElement(@RequestBody WtFilterElement entity) {
        try {

            entity.setUpdateTime(new Date());
            if (IWtFilterElementService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新滤芯：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "删除滤芯")
    @ApiOperation("删除滤芯")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('water:wtFilterElement:delete')")
    public Object deleteWtFilterElement(@ApiParam("滤芯id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("滤芯id");
            }
            if (IWtFilterElementService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除滤芯：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "给滤芯分配滤芯")
    @ApiOperation("查询滤芯明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('water:wtFilterElement:read')")
    public Object getWtFilterElementById(@ApiParam("滤芯id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("滤芯id");
            }
            WtFilterElement coupon = IWtFilterElementService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询滤芯明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除滤芯")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "water", REMARK = "批量删除滤芯")
    @PreAuthorize("hasAuthority('water:wtFilterElement:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = IWtFilterElementService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "water", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, WtFilterElement entity) {
        // 模拟从数据库获取需要导出的数据
        List<WtFilterElement> personList = IWtFilterElementService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", WtFilterElement.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "water", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<WtFilterElement> personList = EasyPoiUtils.importExcel(file, WtFilterElement.class);
        IWtFilterElementService.saveBatch(personList);
    }
}

