package com.zscat.mallplus.jifen.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.jifen.entity.JifenDealerIntegrationChangeHistory;
import com.zscat.mallplus.jifen.service.IJifenDealerIntegrationChangeHistoryService;
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
import java.util.Map;

/**
 * @author wang
 * @date 2020-05-29
 * 经销商积分记录
 */
@Slf4j
@RestController
@RequestMapping("/jifen/jifenDealerIntegrationChangeHistory")
public class JifenDealerIntegrationChangeHistoryController {

    @Resource
    private IJifenDealerIntegrationChangeHistoryService IJifenDealerIntegrationChangeHistoryService;

    @SysLog(MODULE = "jifen", REMARK = "根据条件查询所有经销商积分记录列表")
    @ApiOperation("根据条件查询所有经销商积分记录列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('jifen:jifenDealerIntegrationChangeHistory:read')")
    public Object getJifenDealerIntegrationChangeHistoryByPage(JifenDealerIntegrationChangeHistory entity,
                                                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                               @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IJifenDealerIntegrationChangeHistoryService.page(new Page<JifenDealerIntegrationChangeHistory>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有经销商积分记录列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "jifen", REMARK = "保存经销商积分记录")
    @ApiOperation("保存经销商积分记录")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('jifen:jifenDealerIntegrationChangeHistory:create')")
    public Object saveJifenDealerIntegrationChangeHistory(@RequestBody JifenDealerIntegrationChangeHistory entity) {
        try {
            entity.setCreateTime(new Date());
            if (IJifenDealerIntegrationChangeHistoryService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存经销商积分记录：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "jifen", REMARK = "更新经销商积分记录")
    @ApiOperation("更新经销商积分记录")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('jifen:jifenDealerIntegrationChangeHistory:update')")
    public Object updateJifenDealerIntegrationChangeHistory(@RequestBody JifenDealerIntegrationChangeHistory entity) {
        try {
            if (IJifenDealerIntegrationChangeHistoryService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新经销商积分记录：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "jifen", REMARK = "删除经销商积分记录")
    @ApiOperation("删除经销商积分记录")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('jifen:jifenDealerIntegrationChangeHistory:delete')")
    public Object deleteJifenDealerIntegrationChangeHistory(@ApiParam("经销商积分记录id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("经销商积分记录id");
            }
            if (IJifenDealerIntegrationChangeHistoryService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除经销商积分记录：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "jifen", REMARK = "给经销商积分记录分配经销商积分记录")
    @ApiOperation("查询经销商积分记录明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('jifen:jifenDealerIntegrationChangeHistory:read')")
    public Object getJifenDealerIntegrationChangeHistoryById(@ApiParam("经销商积分记录id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("经销商积分记录id");
            }
            JifenDealerIntegrationChangeHistory coupon = IJifenDealerIntegrationChangeHistoryService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询经销商积分记录明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除经销商积分记录")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "jifen", REMARK = "批量删除经销商积分记录")
    @PreAuthorize("hasAuthority('jifen:jifenDealerIntegrationChangeHistory:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = IJifenDealerIntegrationChangeHistoryService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "jifen", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, JifenDealerIntegrationChangeHistory entity) {
        // 模拟从数据库获取需要导出的数据
        List<JifenDealerIntegrationChangeHistory> personList = IJifenDealerIntegrationChangeHistoryService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", JifenDealerIntegrationChangeHistory.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "jifen", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<JifenDealerIntegrationChangeHistory> personList = EasyPoiUtils.importExcel(file, JifenDealerIntegrationChangeHistory.class);
        IJifenDealerIntegrationChangeHistoryService.saveBatch(personList);
    }

    @SysLog(MODULE = "jifen", REMARK = "根据条件查询所有经销商积分记录列表-平台")
    @ApiOperation("根据条件查询所有经销商积分记录列表-平台")
    @GetMapping(value = "/selectBusinessRecord")
    @PreAuthorize("hasAuthority('jifen:jifenDealerIntegrationChangeHistory:selectBusinessRecord')")
    public Object selectBusinessRecord(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                       @RequestParam Date startTime,
                                       @RequestParam Date endTime,
                                       @RequestParam String dealerName,
                                       @RequestParam String realname,
                                       @RequestParam String dealerPhone,
                                       @RequestParam Integer level,
                                       @RequestParam String changeType,
                                       @RequestParam String changeCount,
                                       @RequestParam String integration,
                                       @RequestParam String sourceType,
                                       @RequestParam String integrationStatus
    ) {
        try {
            return new CommonResult().success(IJifenDealerIntegrationChangeHistoryService.selectBusinessRecord(new Page<Map<String, Object>>(pageNum, pageSize),
                    startTime,
                    endTime,
                    dealerName,
                    realname,
                    dealerPhone,
                    level,
                    changeType,
                    changeCount,
                    integration,
                    sourceType,
                    integrationStatus));
        } catch (Exception e) {
            log.error("根据条件查询所有经销商积分记录列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }
}


