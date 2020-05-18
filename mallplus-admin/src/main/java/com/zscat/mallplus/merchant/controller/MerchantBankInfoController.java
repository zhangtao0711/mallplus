package com.zscat.mallplus.merchant.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.merchant.entity.MerchantBankInfo;
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
import java.util.Date;
import java.util.List;

/**
 * @author mallplus
 * @date 2020-05-14
 * 银行配置信息
 */
@Slf4j
@RestController
@RequestMapping("/bank/merchantBankInfo")
public class MerchantBankInfoController {

    @Resource
    private com.zscat.mallplus.merchant.service.IMerchantBankInfoService IMerchantBankInfoService;

    @SysLog(MODULE = "bank", REMARK = "根据条件查询所有银行配置信息列表")
    @ApiOperation("根据条件查询所有银行配置信息列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('bank:merchantBankInfo:read')")
    public Object getMerchantBankInfoByPage(MerchantBankInfo entity,
                                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IMerchantBankInfoService.page(new Page<MerchantBankInfo>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有银行配置信息列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "bank", REMARK = "保存银行配置信息")
    @ApiOperation("保存银行配置信息")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('bank:merchantBankInfo:create')")
    public Object saveMerchantBankInfo(@RequestBody MerchantBankInfo entity) {
        try {
            entity.setCreateTime(new Date());
            if (IMerchantBankInfoService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存银行配置信息：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "bank", REMARK = "更新银行配置信息")
    @ApiOperation("更新银行配置信息")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('bank:merchantBankInfo:update')")
    public Object updateMerchantBankInfo(@RequestBody MerchantBankInfo entity) {
        try {
            if (IMerchantBankInfoService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新银行配置信息：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "bank", REMARK = "删除银行配置信息")
    @ApiOperation("删除银行配置信息")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('bank:merchantBankInfo:delete')")
    public Object deleteMerchantBankInfo(@ApiParam("银行配置信息id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("银行配置信息id");
            }
            if (IMerchantBankInfoService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除银行配置信息：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "bank", REMARK = "给银行配置信息分配银行配置信息")
    @ApiOperation("查询银行配置信息明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('bank:merchantBankInfo:read')")
    public Object getMerchantBankInfoById(@ApiParam("银行配置信息id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("银行配置信息id");
            }
            MerchantBankInfo coupon = IMerchantBankInfoService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询银行配置信息明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除银行配置信息")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "bank", REMARK = "批量删除银行配置信息")
    @PreAuthorize("hasAuthority('bank:merchantBankInfo:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = IMerchantBankInfoService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "bank", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, MerchantBankInfo entity) {
        // 模拟从数据库获取需要导出的数据
        List<MerchantBankInfo> personList = IMerchantBankInfoService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", MerchantBankInfo.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "bank", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<MerchantBankInfo> personList = EasyPoiUtils.importExcel(file, MerchantBankInfo.class);
        IMerchantBankInfoService.saveBatch(personList);
    }
}


