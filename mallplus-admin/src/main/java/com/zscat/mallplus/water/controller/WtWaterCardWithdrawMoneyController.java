package com.zscat.mallplus.water.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.water.entity.WtWaterCard;
import com.zscat.mallplus.water.entity.WtWaterCardWithdrawMoney;
import com.zscat.mallplus.water.service.IWtWaterCardService;
import com.zscat.mallplus.water.service.IWtWaterCardWithdrawMoneyService;
import com.zscat.mallplus.util.EasyPoiUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Date;

/**
 * @author lyn
 * @date 2020-06-08
 * 水卡取现
 */
@Slf4j
@RestController
@RequestMapping("/water/wtWaterCardWithdrawMoney")
public class WtWaterCardWithdrawMoneyController {

    @Resource
    private IWtWaterCardWithdrawMoneyService IWtWaterCardWithdrawMoneyService;
    @Resource
    private IWtWaterCardService IWtWaterCardService;

    @SysLog(MODULE = "water", REMARK = "根据条件查询所有水卡取现列表")
    @ApiOperation("根据条件查询所有水卡取现列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('water:wtWaterCardWithdrawMoney:read')")
    public Object getWtWaterCardWithdrawMoneyByPage(WtWaterCardWithdrawMoney entity,
                                                    @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                    @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IWtWaterCardWithdrawMoneyService.page(new Page<WtWaterCardWithdrawMoney>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有水卡取现列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "保存水卡取现")
    @ApiOperation("保存水卡取现")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('water:wtWaterCardWithdrawMoney:create')")
    @Transactional
    public Object saveWtWaterCardWithdrawMoney(@RequestBody WtWaterCardWithdrawMoney entity) {
        try {
            //卡号存在判定
            WtWaterCard coupon = IWtWaterCardService.getById(entity.getCardNo());
            if(coupon==null || coupon.getId()==null){
                return new CommonResult().failed("取卡卡号不正确！");
            }
            //取现金额不能大于卡内金额
            if(coupon.getCardMoney().compareTo(entity.getCardMoney())==-1){
                return new CommonResult().failed("金额取现大于卡内余额，取现失败！");
            }
            if(coupon.getGiveMoney().compareTo(entity.getGiveMoney())==-1){
                return new CommonResult().failed("赠送取现大于卡内赠送余额，取现失败！");
            }

            entity.setCreateTime(new Date());
            if (IWtWaterCardWithdrawMoneyService.save(entity)) {

                //更新卡内余额
                coupon.setCardMoney(coupon.getCardMoney().subtract(entity.getCardMoney()));
                coupon.setGiveMoney(coupon.getGiveMoney().subtract(entity.getGiveMoney()));
                coupon.setUpdateBy(entity.getCreateBy());
                coupon.setUpdateTime(new Date());
                if (IWtWaterCardService.updateById(coupon)) {
                    return new CommonResult().success();
                }
                return new CommonResult().success();
            }

        } catch (Exception e) {
            log.error("保存水卡取现：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "更新水卡取现")
    @ApiOperation("更新水卡取现")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('water:wtWaterCardWithdrawMoney:update')")
    public Object updateWtWaterCardWithdrawMoney(@RequestBody WtWaterCardWithdrawMoney entity) {
        try {
            if (IWtWaterCardWithdrawMoneyService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新水卡取现：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "删除水卡取现")
    @ApiOperation("删除水卡取现")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('water:wtWaterCardWithdrawMoney:delete')")
    public Object deleteWtWaterCardWithdrawMoney(@ApiParam("水卡取现id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("水卡取现id");
            }
            if (IWtWaterCardWithdrawMoneyService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除水卡取现：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "给水卡取现分配水卡取现")
    @ApiOperation("查询水卡取现明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('water:wtWaterCardWithdrawMoney:read')")
    public Object getWtWaterCardWithdrawMoneyById(@ApiParam("水卡取现id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("水卡取现id");
            }
            WtWaterCardWithdrawMoney coupon = IWtWaterCardWithdrawMoneyService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询水卡取现明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除水卡取现")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "water", REMARK = "批量删除水卡取现")
    @PreAuthorize("hasAuthority('water:wtWaterCardWithdrawMoney:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = IWtWaterCardWithdrawMoneyService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "water", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, WtWaterCardWithdrawMoney entity) {
        // 模拟从数据库获取需要导出的数据
        List<WtWaterCardWithdrawMoney> personList = IWtWaterCardWithdrawMoneyService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", WtWaterCardWithdrawMoney.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "water", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<WtWaterCardWithdrawMoney> personList = EasyPoiUtils.importExcel(file, WtWaterCardWithdrawMoney.class);
        IWtWaterCardWithdrawMoneyService.saveBatch(personList);
    }
}


