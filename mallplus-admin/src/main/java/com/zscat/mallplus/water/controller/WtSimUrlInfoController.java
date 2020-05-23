package com.zscat.mallplus.water.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.water.entity.SimEntity;
import com.zscat.mallplus.water.entity.WtSimCard;
import com.zscat.mallplus.water.entity.WtSimUrlInfo;
import com.zscat.mallplus.water.service.IWtSimUrlInfoService;
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
 * SIM卡第三方appkey
 */
@Slf4j
@RestController
@RequestMapping("/water/wtSimUrlInfo")
public class WtSimUrlInfoController {

    @Resource
    private IWtSimUrlInfoService IWtSimUrlInfoService;

    @SysLog(MODULE = "water", REMARK = "根据条件查询所有SIM卡第三方appkey列表")
    @ApiOperation("根据条件查询所有SIM卡第三方appkey列表")
    @GetMapping(value = "/list")
//    @PreAuthorize("hasAuthority('water:wtSimUrlInfo:read')")
    public Object getWtSimUrlInfoByPage(WtSimUrlInfo entity,
                                        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IWtSimUrlInfoService.page(new Page<WtSimUrlInfo>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有SIM卡第三方appkey列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "保存SIM卡第三方appkey")
    @ApiOperation("保存SIM卡第三方appkey")
    @PostMapping(value = "/create")
//    @PreAuthorize("hasAuthority('water:wtSimUrlInfo:create')")
    public Object saveWtSimUrlInfo(@RequestBody WtSimUrlInfo entity) {
        try {
            entity.setCreateTime(new Date());
            if (IWtSimUrlInfoService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存SIM卡第三方appkey：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "更新SIM卡第三方appkey")
    @ApiOperation("更新SIM卡第三方appkey")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('water:wtSimUrlInfo:update')")
    public Object updateWtSimUrlInfo(@RequestBody WtSimUrlInfo entity) {
        try {
            entity.setUpdateTime(new Date());
            if (IWtSimUrlInfoService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新SIM卡第三方appkey：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "删除SIM卡第三方appkey")
    @ApiOperation("删除SIM卡第三方appkey")
    @GetMapping(value = "/delete/{id}")
//    @PreAuthorize("hasAuthority('water:wtSimUrlInfo:delete')")
    public Object deleteWtSimUrlInfo(@ApiParam("SIM卡第三方appkeyid") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("SIM卡第三方appkeyid");
            }
            if (IWtSimUrlInfoService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除SIM卡第三方appkey：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "给SIM卡第三方appkey分配SIM卡第三方appkey")
    @ApiOperation("查询SIM卡第三方appkey明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('water:wtSimUrlInfo:read')")
    public Object getWtSimUrlInfoById(@ApiParam("SIM卡第三方appkeyid") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("SIM卡第三方appkeyid");
            }
            WtSimUrlInfo coupon = IWtSimUrlInfoService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询SIM卡第三方appkey明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除SIM卡第三方appkey")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "water", REMARK = "批量删除SIM卡第三方appkey")
    @PreAuthorize("hasAuthority('water:wtSimUrlInfo:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = IWtSimUrlInfoService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "water", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, WtSimUrlInfo entity) {
        // 模拟从数据库获取需要导出的数据
        List<WtSimUrlInfo> personList = IWtSimUrlInfoService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", WtSimUrlInfo.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "water", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<WtSimUrlInfo> personList = EasyPoiUtils.importExcel(file, WtSimUrlInfo.class);
        IWtSimUrlInfoService.saveBatch(personList);
    }

    @SysLog(MODULE = "water", REMARK = "查询账号余额")
    @ApiOperation("查询账号余额")
    @GetMapping(value = "/gerAmount/{id}")
//    @PreAuthorize("hasAuthority('water:wtSimCard:read')")
    public Object gerAmount(@ApiParam("SIM卡第三方appkeyid")@PathVariable Long id) {
        try {
            SimEntity coupon = IWtSimUrlInfoService.gerAmount(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询账号余额：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }
}


