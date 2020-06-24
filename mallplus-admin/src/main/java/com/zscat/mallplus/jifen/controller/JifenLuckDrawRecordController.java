package com.zscat.mallplus.jifen.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.jifen.entity.JifenLuckDrawRecord;
import com.zscat.mallplus.jifen.service.IJifenLuckDrawRecordService;
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
 * @date 2020-06-23
 * 抽奖
 */
@Slf4j
@RestController
@RequestMapping("/jifen/jifenLuckDrawRecord")
public class JifenLuckDrawRecordController {

    @Resource
    private IJifenLuckDrawRecordService IJifenLuckDrawRecordService;

    @SysLog(MODULE = "jifen", REMARK = "根据条件查询所有抽奖列表")
    @ApiOperation("根据条件查询所有抽奖列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('jifen:jifenLuckDrawRecord:read')")
    public Object getJifenLuckDrawRecordByPage(JifenLuckDrawRecord entity,
                                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                               @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IJifenLuckDrawRecordService.page(new Page<JifenLuckDrawRecord>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有抽奖列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "jifen", REMARK = "保存抽奖")
    @ApiOperation("保存抽奖")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('jifen:jifenLuckDrawRecord:create')")
    public Object saveJifenLuckDrawRecord(@RequestBody JifenLuckDrawRecord entity) {
        try {
            entity.setCreateTime(new Date());
            if (IJifenLuckDrawRecordService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存抽奖：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "jifen", REMARK = "更新抽奖")
    @ApiOperation("更新抽奖")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('jifen:jifenLuckDrawRecord:update')")
    public Object updateJifenLuckDrawRecord(@RequestBody JifenLuckDrawRecord entity) {
        try {
            if (IJifenLuckDrawRecordService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新抽奖：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "jifen", REMARK = "删除抽奖")
    @ApiOperation("删除抽奖")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('jifen:jifenLuckDrawRecord:delete')")
    public Object deleteJifenLuckDrawRecord(@ApiParam("抽奖id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("抽奖id");
            }
            if (IJifenLuckDrawRecordService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除抽奖：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "jifen", REMARK = "给抽奖分配抽奖")
    @ApiOperation("查询抽奖明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('jifen:jifenLuckDrawRecord:read')")
    public Object getJifenLuckDrawRecordById(@ApiParam("抽奖id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("抽奖id");
            }
            JifenLuckDrawRecord coupon = IJifenLuckDrawRecordService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询抽奖明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除抽奖")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "jifen", REMARK = "批量删除抽奖")
    @PreAuthorize("hasAuthority('jifen:jifenLuckDrawRecord:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = IJifenLuckDrawRecordService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "jifen", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, JifenLuckDrawRecord entity) {
        // 模拟从数据库获取需要导出的数据
        List<JifenLuckDrawRecord> personList = IJifenLuckDrawRecordService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", JifenLuckDrawRecord.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "jifen", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<JifenLuckDrawRecord> personList = EasyPoiUtils.importExcel(file, JifenLuckDrawRecord.class);
        IJifenLuckDrawRecordService.saveBatch(personList);
    }
}


