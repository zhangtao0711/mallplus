package com.zscat.mallplus.water.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.util.ConstantUtil;
import com.zscat.mallplus.water.entity.WtSimCard;
import com.zscat.mallplus.water.service.IWtSimCardService;
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
 * SIM卡列表
 */
@Slf4j
@RestController
@RequestMapping("/water/wtSimCard")
public class WtSimCardController {

    @Resource
    private IWtSimCardService IWtSimCardService;

    @SysLog(MODULE = "water", REMARK = "根据条件查询所有SIM卡列表列表")
    @ApiOperation("根据条件查询所有SIM卡列表列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('water:wtSimCard:read')")
    public Object getWtSimCardByPage(WtSimCard entity,
                                     @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                     @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IWtSimCardService.page(new Page<WtSimCard>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有SIM卡列表列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "保存SIM卡列表")
    @ApiOperation("保存SIM卡列表")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('water:wtSimCard:create')")
    public Object saveWtSimCard(@RequestBody WtSimCard entity) {
        try {
            entity.setDelFlag(ConstantUtil.delFlag);
            entity.setCreateTime(new Date());
            if (IWtSimCardService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存SIM卡列表：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "更新SIM卡列表")
    @ApiOperation("更新SIM卡列表")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('water:wtSimCard:update')")
    public Object updateWtSimCard(@RequestBody WtSimCard entity) {
        try {
            entity.setUpdateTime(new Date());
            if (IWtSimCardService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新SIM卡列表：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "删除SIM卡列表")
    @ApiOperation("删除SIM卡列表")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('water:wtSimCard:delete')")
    public Object deleteWtSimCard(@ApiParam("SIM卡列表id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("SIM卡列表id");
            }
            if (IWtSimCardService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除SIM卡列表：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "给SIM卡列表分配SIM卡列表")
    @ApiOperation("查询SIM卡列表明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('water:wtSimCard:read')")
    public Object getWtSimCardById(@ApiParam("SIM卡列表id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("SIM卡列表id");
            }
            WtSimCard coupon = IWtSimCardService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询SIM卡列表明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除SIM卡列表")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "water", REMARK = "批量删除SIM卡列表")
    @PreAuthorize("hasAuthority('water:wtSimCard:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = IWtSimCardService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "water", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, WtSimCard entity) {
        // 模拟从数据库获取需要导出的数据
        List<WtSimCard> personList = IWtSimCardService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", WtSimCard.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "water", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<WtSimCard> personList = EasyPoiUtils.importExcel(file, WtSimCard.class);
        IWtSimCardService.saveBatch(personList);
    }


}


