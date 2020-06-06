package com.zscat.mallplus.water.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.util.ConstantUtil;
import com.zscat.mallplus.util.StringUtils;
import com.zscat.mallplus.water.entity.WtEquipmentWarterCard;
import com.zscat.mallplus.water.entity.WtWaterCardLimit;
import com.zscat.mallplus.water.service.IWtEquipmentService;
import com.zscat.mallplus.water.service.IWtEquipmentWarterCardService;
import com.zscat.mallplus.water.service.IWtWaterCardLimitService;
import com.zscat.mallplus.util.EasyPoiUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.water.service.IWtWaterCardRechargeService;
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
 * @date 2020-06-05
 * 水卡限制消费
 */
@Slf4j
@RestController
@RequestMapping("/water/wtWaterCardLimit")
public class WtWaterCardLimitController {

    @Resource
    private IWtWaterCardLimitService IWtWaterCardLimitService;
    @Resource
    private IWtWaterCardRechargeService IWtWaterCardRechargeService;
    @Resource
    private IWtEquipmentService IWtEquipmentService;
    @Resource
    private IWtEquipmentWarterCardService IWtEquipmentWarterCardService;

    @SysLog(MODULE = "water", REMARK = "根据条件查询所有水卡限制消费列表")
    @ApiOperation("根据条件查询所有水卡限制消费列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('water:wtWaterCardLimit:read')")
    public Object getWtWaterCardLimitByPage(WtWaterCardLimit entity,
                                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IWtWaterCardLimitService.page(new Page<WtWaterCardLimit>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有水卡限制消费列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "保存限制消费单卡")
    @ApiOperation("保存限制消费单卡")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('water:wtWaterCardLimit:create')")
    public Object saveWtWaterCardLimit(@RequestBody WtWaterCardLimit entity) {
        try {
            //左补位到10位
            entity.setCardNo(StringUtils.padRight(entity.getCardNo(),10,'0'));
            //获取卡号关联经销商和登录者经销商是否一致
            if (!IWtWaterCardRechargeService.getDealerId(Long.valueOf(entity.getCardNo()),Long.valueOf(entity.getCardNo()),entity.getDealerId())) {
                return new CommonResult().failed("此卡没有绑定在您的账号下！");
            }
            //获取设备关联经销商和登录者经销商是否一致
            if (!IWtEquipmentService.getDealerId(entity.getEqcode(),entity.getDealerId())) {
                return new CommonResult().failed("此设备没有绑定在您的账号下！");
            }
            WtEquipmentWarterCard wtEquipmentWarterCard = new WtEquipmentWarterCard();
            wtEquipmentWarterCard.setEqcode(entity.getEqcode());//设备号
            wtEquipmentWarterCard.setCardNo(entity.getCardNo());//卡号
            IWtEquipmentWarterCardService.save(wtEquipmentWarterCard);

//            entity.setDelFlag(ConstantUtil.delFlag);
            entity.setCreateTime(new Date());
            if (IWtWaterCardLimitService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存水卡限制消费：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "保存限制消费批量")
    @ApiOperation("保存限制消费批量")
    @PostMapping(value = "/createList")
    @PreAuthorize("hasAuthority('water:wtWaterCardLimit:create')")
    @Transactional
    public Object saveWtWaterCardLimitList(@RequestBody WtWaterCardLimit entity) {
        try {
            //判断起始卡号和终止卡号合理性
            if(Long.valueOf(entity.getStartNo())>Long.valueOf(entity.getEndNo())){
                return new CommonResult().failed("终止卡号必须大于等于起始卡号！");
            }
            //单次不能超过200张
            if(Long.valueOf(entity.getEndNo())-Long.valueOf(entity.getStartNo())+1 <= 200){
                return new CommonResult().failed("单次不能超过200张！");
            }
            //获取卡号关联经销商和登录者经销商是否一致
            if (!IWtWaterCardRechargeService.getDealerId(Long.valueOf(entity.getStartNo()),Long.valueOf(entity.getEndNo()),entity.getDealerId())) {
                return new CommonResult().failed("此区段内有卡没有绑定在您的账号下！");
            }
            //获取设备关联经销商和登录者经销商是否一致
            if (!IWtEquipmentService.getDealerId(entity.getEqcode(),entity.getDealerId())) {
                return new CommonResult().failed("此设备没有绑定在您的账号下！");
            }
            //添加水卡和设备关联信息表
            for(Long i =Long.valueOf(entity.getStartNo());i<=Long.valueOf(entity.getEndNo());i++){
                //左补位到10位
                String num = StringUtils.padRight(i.toString(),10,'0');
                WtEquipmentWarterCard wtEquipmentWarterCard = new WtEquipmentWarterCard();
                wtEquipmentWarterCard.setEqcode(entity.getEqcode());//设备号
                wtEquipmentWarterCard.setCardNo(num);//卡号
                IWtEquipmentWarterCardService.save(wtEquipmentWarterCard);
            }
//            entity.setDelFlag(ConstantUtil.delFlag);
            entity.setCreateTime(new Date());
            if (IWtWaterCardLimitService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存水卡限制消费：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "更新水卡限制消费")
    @ApiOperation("更新水卡限制消费")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('water:wtWaterCardLimit:update')")
    public Object updateWtWaterCardLimit(@RequestBody WtWaterCardLimit entity) {
        try {
            if (IWtWaterCardLimitService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新水卡限制消费：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "删除水卡限制消费")
    @ApiOperation("删除水卡限制消费")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('water:wtWaterCardLimit:delete')")
    public Object deleteWtWaterCardLimit(@ApiParam("水卡限制消费id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("水卡限制消费id");
            }
            if (IWtWaterCardLimitService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除水卡限制消费：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "给水卡限制消费分配水卡限制消费")
    @ApiOperation("查询水卡限制消费明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('water:wtWaterCardLimit:read')")
    public Object getWtWaterCardLimitById(@ApiParam("水卡限制消费id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("水卡限制消费id");
            }
            WtWaterCardLimit coupon = IWtWaterCardLimitService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询水卡限制消费明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除水卡限制消费")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "water", REMARK = "批量删除水卡限制消费")
    @PreAuthorize("hasAuthority('water:wtWaterCardLimit:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = IWtWaterCardLimitService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "water", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, WtWaterCardLimit entity) {
        // 模拟从数据库获取需要导出的数据
        List<WtWaterCardLimit> personList = IWtWaterCardLimitService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", WtWaterCardLimit.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "water", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<WtWaterCardLimit> personList = EasyPoiUtils.importExcel(file, WtWaterCardLimit.class);
        IWtWaterCardLimitService.saveBatch(personList);
    }
}


