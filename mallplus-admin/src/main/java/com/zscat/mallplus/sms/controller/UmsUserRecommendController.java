package com.zscat.mallplus.sms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.sms.entity.SmsRechargePackage;
import com.zscat.mallplus.sms.entity.UmsUserRecommend;
import com.zscat.mallplus.sms.service.IUmsUserRecommendService;
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
 * @author zhangtao
 * @date 2020-06-26
 * 客户推荐
 */
@Slf4j
@RestController
@RequestMapping("/sms/umsUserRecommend")
public class UmsUserRecommendController {

    @Resource
    private IUmsUserRecommendService IUmsUserRecommendService;

    @SysLog(MODULE = "sms", REMARK = "根据条件查询所有客户推荐列表")
    @ApiOperation("根据条件查询所有客户推荐列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('sms:umsUserRecommend:read')")
    public Object getUmsUserRecommendByPage(UmsUserRecommend entity,
                                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IUmsUserRecommendService.page(new Page<UmsUserRecommend>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有客户推荐列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "保存客户推荐")
    @ApiOperation("保存客户推荐")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('sms:umsUserRecommend:create')")
    public Object saveUmsUserRecommend(@RequestBody UmsUserRecommend entity) {
        try {
            entity.setCreateTime(new Date());
            if (IUmsUserRecommendService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存客户推荐：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "更新客户推荐")
    @ApiOperation("更新客户推荐")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('sms:umsUserRecommend:update')")
    public Object updateUmsUserRecommend(@RequestBody UmsUserRecommend entity) {
        try {
            if (IUmsUserRecommendService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新客户推荐：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "删除客户推荐")
    @ApiOperation("删除客户推荐")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('sms:umsUserRecommend:delete')")
    public Object deleteUmsUserRecommend(@ApiParam("客户推荐id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("客户推荐id");
            }
            if (IUmsUserRecommendService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除客户推荐：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "给客户推荐分配客户推荐")
    @ApiOperation("查询客户推荐明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('sms:umsUserRecommend:read')")
    public Object getUmsUserRecommendById(@ApiParam("客户推荐id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("客户推荐id");
            }
            UmsUserRecommend coupon = IUmsUserRecommendService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询客户推荐明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除客户推荐")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "sms", REMARK = "批量删除客户推荐")
    @PreAuthorize("hasAuthority('sms:umsUserRecommend:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = IUmsUserRecommendService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "sms", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, UmsUserRecommend entity) {
        // 模拟从数据库获取需要导出的数据
        List<UmsUserRecommend> personList = IUmsUserRecommendService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", UmsUserRecommend.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "sms", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<UmsUserRecommend> personList = EasyPoiUtils.importExcel(file, UmsUserRecommend.class);
        IUmsUserRecommendService.saveBatch(personList);
    }
}


