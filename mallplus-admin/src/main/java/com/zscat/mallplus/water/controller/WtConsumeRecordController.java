package com.zscat.mallplus.water.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.util.ConstantUtil;
import com.zscat.mallplus.water.entity.WtConsumeRecord;
import com.zscat.mallplus.water.service.IWtConsumeRecordService;
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
 * 售水机消费记录
 */
@Slf4j
@RestController
@Api(tags = "WtConsumeRecordController", description = "售水机消费记录")
@RequestMapping("/water/wtConsumeRecord")
public class WtConsumeRecordController {

    @Resource
    private IWtConsumeRecordService IWtConsumeRecordService;

    @SysLog(MODULE = "water", REMARK = "根据条件查询所有售水机消费记录列表")
    @ApiOperation("根据条件查询所有售水机消费记录列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('water:wtConsumeRecord:read')")
    public Object getWtConsumeRecordByPage(WtConsumeRecord entity,
                                           @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                           @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IWtConsumeRecordService.page(new Page<WtConsumeRecord>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有售水机消费记录列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "根据设备查询销售信息")
    @ApiOperation("根据设备查询销售信息")
    @GetMapping(value = "/listSum")
    @PreAuthorize("hasAuthority('water:wtConsumeRecord:read')")
    public Object listSum(WtConsumeRecord entity) {
        try {
            entity.setDelFlag(ConstantUtil.delFlag);
            return new CommonResult().success(IWtConsumeRecordService.getListSum(entity));
        } catch (Exception e) {
            log.error("根据设备查询销售信息：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "保存售水机消费记录")
    @ApiOperation("保存售水机消费记录")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('water:wtConsumeRecord:create')")
    public Object saveWtConsumeRecord(@RequestBody WtConsumeRecord entity) {
        try {
            entity.setCreateTime(new Date());
            if (IWtConsumeRecordService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存售水机消费记录：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "更新售水机消费记录")
    @ApiOperation("更新售水机消费记录")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('water:wtConsumeRecord:update')")
    public Object updateWtConsumeRecord(@RequestBody WtConsumeRecord entity) {
        try {

            if (IWtConsumeRecordService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新售水机消费记录：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "删除售水机消费记录")
    @ApiOperation("删除售水机消费记录")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('water:wtConsumeRecord:delete')")
    public Object deleteWtConsumeRecord(@ApiParam("售水机消费记录id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("售水机消费记录id");
            }
            if (IWtConsumeRecordService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除售水机消费记录：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "给售水机消费记录分配售水机消费记录")
    @ApiOperation("查询售水机消费记录明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('water:wtConsumeRecord:read')")
    public Object getWtConsumeRecordById(@ApiParam("售水机消费记录id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("售水机消费记录id");
            }
            WtConsumeRecord coupon = IWtConsumeRecordService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询售水机消费记录明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除售水机消费记录")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "water", REMARK = "批量删除售水机消费记录")
    @PreAuthorize("hasAuthority('water:wtConsumeRecord:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = IWtConsumeRecordService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "water", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, WtConsumeRecord entity) {
        // 模拟从数据库获取需要导出的数据
        List<WtConsumeRecord> personList = IWtConsumeRecordService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", WtConsumeRecord.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "water", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<WtConsumeRecord> personList = EasyPoiUtils.importExcel(file, WtConsumeRecord.class);
        IWtConsumeRecordService.saveBatch(personList);
    }
}


