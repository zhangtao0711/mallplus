package com.zscat.mallplus.water.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.util.StringUtils;
import com.zscat.mallplus.water.entity.WtEquipmentDetailes;
import com.zscat.mallplus.water.service.IWtEquipmentDetailesService;
import com.zscat.mallplus.util.EasyPoiUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import io.swagger.annotations.Api;
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
 * @date 2020-06-17
 * 设备详细参数
 */
@Slf4j
@RestController
@Api(tags = "WtEquipmentDetailesController", description = "设备详细参数")
@RequestMapping("/water/wtEquipmentDetailes")
public class WtEquipmentDetailesController {

    @Resource
    private IWtEquipmentDetailesService IWtEquipmentDetailesService;

    @SysLog(MODULE = "water", REMARK = "根据条件查询所有设备详细参数列表")
    @ApiOperation("根据条件查询所有设备详细参数列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('water:wtEquipmentDetailes:read')")
    public Object getWtEquipmentDetailesByPage(WtEquipmentDetailes entity,
                                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                               @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IWtEquipmentDetailesService.page(new Page<WtEquipmentDetailes>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有设备详细参数列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "保存设备详细参数")
    @ApiOperation("保存设备详细参数")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('water:wtEquipmentDetailes:create')")
    public Object saveWtEquipmentDetailes(@RequestBody WtEquipmentDetailes entity) {
        try {
            if (IWtEquipmentDetailesService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存设备详细参数：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "更新设备详细参数")
    @ApiOperation("更新设备详细参数")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('water:wtEquipmentDetailes:update')")
    public Object updateWtEquipmentDetailes(@RequestBody WtEquipmentDetailes entity) {
        try {
            //ip校验
            if(entity.getIpaddress()!=null && !StringUtils.isIP(entity.getIpaddress())){
                return new CommonResult().failed("请输入正确的ip地址！");
            }
            //端口校验
            if(entity.getPort()!=null && !StringUtils.checkPort(entity.getPort())){
                return new CommonResult().failed("请输入正确的端口号！");
            }

            entity.setUpdateTime(new Date());
            if (IWtEquipmentDetailesService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新设备详细参数：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "删除设备详细参数")
    @ApiOperation("删除设备详细参数")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('water:wtEquipmentDetailes:delete')")
    public Object deleteWtEquipmentDetailes(@ApiParam("设备详细参数id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("设备详细参数id");
            }
            if (IWtEquipmentDetailesService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除设备详细参数：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "给设备详细参数分配设备详细参数")
    @ApiOperation("查询设备详细参数明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('water:wtEquipmentDetailes:read')")
    public Object getWtEquipmentDetailesById(@ApiParam("设备详细参数id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("设备详细参数id");
            }
            WtEquipmentDetailes coupon = IWtEquipmentDetailesService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询设备详细参数明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除设备详细参数")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "water", REMARK = "批量删除设备详细参数")
    @PreAuthorize("hasAuthority('water:wtEquipmentDetailes:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = IWtEquipmentDetailesService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "water", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, WtEquipmentDetailes entity) {
        // 模拟从数据库获取需要导出的数据
        List<WtEquipmentDetailes> personList = IWtEquipmentDetailesService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", WtEquipmentDetailes.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "water", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<WtEquipmentDetailes> personList = EasyPoiUtils.importExcel(file, WtEquipmentDetailes.class);
        IWtEquipmentDetailesService.saveBatch(personList);
    }
}


