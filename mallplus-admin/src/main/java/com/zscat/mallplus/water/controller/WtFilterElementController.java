package com.zscat.mallplus.water.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.util.ConstantUtil;
import com.zscat.mallplus.util.DateUtils;
import com.zscat.mallplus.water.entity.WtEquipmentRemarks;
import com.zscat.mallplus.water.entity.WtFilterElement;
import com.zscat.mallplus.water.entity.WtFilterElementType;
import com.zscat.mallplus.water.service.IWtFilterElementService;
import com.zscat.mallplus.util.EasyPoiUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.water.service.IWtFilterElementTypeService;
import io.swagger.annotations.Api;
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
 * @date 2020-05-20
 * 滤芯
 */
@Slf4j
@RestController
@Api(tags = "WtFilterElementController", description = "滤芯")
@RequestMapping("/water/wtFilterElement")
public class WtFilterElementController {

    @Resource
    private IWtFilterElementService IWtFilterElementService;

    @Resource
    private IWtFilterElementTypeService IWtFilterElementTypeService;

    @SysLog(MODULE = "water", REMARK = "根据条件查询所有滤芯列表")
    @ApiOperation("根据条件查询所有滤芯列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('water:wtFilterElement:read')")
    public Object getWtFilterElementByPage(WtFilterElement entity,
                                           @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                           @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IWtFilterElementService.page(new Page<WtFilterElement>(pageNum, pageSize), new QueryWrapper<>(entity).orderByDesc("create_time")));
        } catch (Exception e) {
            log.error("根据条件查询所有滤芯列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "保存滤芯")
    @ApiOperation("保存滤芯")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('water:wtFilterElement:create')")
    @Transactional
    public Object saveWtFilterElement(@RequestBody WtFilterElement entity) {
        try {
            //滤芯更换时间必须小于当前时间
            if(entity.getChangeTime().after(new Date())){
                return new CommonResult().failed("滤芯更换时间必须小于当前时间");
            }
            WtFilterElementType coupon = IWtFilterElementTypeService.getById(entity.getFilterElementTypeId());
            entity.setChangeCycle(coupon.getChangeCycle());//更换周期天数
            entity.setPurifierTotal(coupon.getPurifierTotal());//水量标准

            //选择及时时设定使用到期时间
            if(entity.getBillingMode().equals(ConstantUtil.billing_mode_time)){
                entity.setEndTime(DateUtils.addDay2(entity.getChangeTime(),coupon.getChangeCycle()));
                entity.setRemindTime(DateUtils.addDay2(entity.getChangeTime(),coupon.getRemindDay()));
            }
            entity.setState(ConstantUtil.billing_mode_time);//状态正常
            entity.setDelFlag(ConstantUtil.delFlag);
            entity.setCreateTime(new Date());
            //更新设备滤芯日志表
            if (!ValidatorUtils.empty(entity.getEqcode())) {
                WtEquipmentRemarks remarks= new WtEquipmentRemarks();
                remarks.setEqcode(entity.getEqcode());//设备号
                remarks.setRemarksType(ConstantUtil.remarks_type_1);//履历
                remarks.setCreateBy(entity.getCreateBy());//创建人
                remarks.setCreateTime(entity.getCreateTime());//创建时间
                remarks.setRemarks("设备"+entity.getEqcode()+"新增了第"
                        +entity.getFilterElementLevel()+"级别的"+entity.getName()+"滤芯");//履历内容
            }
            if (IWtFilterElementService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存滤芯：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "更换滤芯")
    @ApiOperation("更换滤芯")
    @PostMapping(value = "/change")
    @PreAuthorize("hasAuthority('water:wtFilterElement:create')")
    @Transactional
    public Object changeWtFilterElement(@RequestBody WtFilterElement entity) {
        try {
            //滤芯更换时间必须小于当前时间
            if(entity.getChangeTime().after(new Date())){
                return new CommonResult().failed("滤芯更换时间必须小于当前时间");
            }
            WtFilterElementType coupon = IWtFilterElementTypeService.getById(entity.getFilterElementTypeId());
            entity.setChangeCycle(coupon.getChangeCycle());//更换周期天数
            entity.setPurifierTotal(coupon.getPurifierTotal());//水量标准

            //选择计时时设定使用到期时间
            if(entity.getBillingMode().equals(ConstantUtil.billing_mode_time)){
                entity.setEndTime(DateUtils.addDay2(entity.getChangeTime(),coupon.getChangeCycle()));
                entity.setRemindTime(DateUtils.addDay2(entity.getChangeTime(),coupon.getRemindDay()));
            }
            entity.setState(ConstantUtil.billing_mode_time);//状态正常
            entity.setDelFlag(ConstantUtil.delFlag);
            entity.setCreateTime(new Date());
            //更新设备滤芯日志表
            if (!ValidatorUtils.empty(entity.getEqcode())) {
                WtEquipmentRemarks remarks= new WtEquipmentRemarks();
                remarks.setEqcode(entity.getEqcode());//设备号
                remarks.setRemarksType(ConstantUtil.remarks_type_1);//履历
                remarks.setCreateBy(entity.getCreateBy());//创建人
                remarks.setCreateTime(entity.getCreateTime());//创建时间
                remarks.setRemarks("设备"+entity.getEqcode()+"跟换了第"
                        +entity.getFilterElementLevel()+"级别的"+entity.getName()+"滤芯");//履历内容
            }
            if (IWtFilterElementService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存滤芯：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "更新滤芯")
    @ApiOperation("更新滤芯")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('water:wtFilterElement:update')")
    @Transactional
    public Object updateWtFilterElement(@RequestBody WtFilterElement entity) {
        try {
           entity.setChangeTime(new Date());//跟换时间当前时间
            WtFilterElementType coupon = IWtFilterElementTypeService.getById(entity.getFilterElementTypeId());
            entity.setChangeCycle(coupon.getChangeCycle());//更换周期天数
            entity.setPurifierTotal(coupon.getPurifierTotal());//水量标准

            //选择及时时设定使用到期时间
            if(entity.getBillingMode().equals(ConstantUtil.billing_mode_time)){
                entity.setEndTime(DateUtils.addDay2(entity.getChangeTime(),coupon.getChangeCycle()));
                entity.setRemindTime(DateUtils.addDay2(entity.getChangeTime(),coupon.getRemindDay()));
            }
            entity.setUpdateTime(new Date());
            //更新设备滤芯日志表
            if (!ValidatorUtils.empty(entity.getEqcode())) {
                WtEquipmentRemarks remarks= new WtEquipmentRemarks();
                remarks.setEqcode(entity.getEqcode());//设备号
                remarks.setRemarksType(ConstantUtil.remarks_type_1);//履历
                remarks.setCreateBy(entity.getCreateBy());//创建人
                remarks.setCreateTime(entity.getCreateTime());//创建时间
                remarks.setRemarks("设备"+entity.getEqcode()+"更换了第"
                        +entity.getFilterElementLevel()+"级别的"+entity.getName()+"滤芯");//履历内容
            }
            if (IWtFilterElementService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新滤芯：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "删除滤芯")
    @ApiOperation("删除滤芯")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('water:wtFilterElement:delete')")
    @Transactional
    public Object deleteWtFilterElement(@ApiParam("滤芯id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("滤芯id");
            }
            WtFilterElement entity = IWtFilterElementService.getById(id);
            //更新设备滤芯日志表
            if (!ValidatorUtils.empty(entity.getEqcode())) {
                WtEquipmentRemarks remarks= new WtEquipmentRemarks();
                remarks.setEqcode(entity.getEqcode());//设备号
                remarks.setRemarksType(ConstantUtil.remarks_type_1);//履历
                remarks.setCreateBy(entity.getCreateBy());//创建人
                remarks.setCreateTime(entity.getCreateTime());//创建时间
                remarks.setRemarks("设备"+entity.getEqcode()+"删除了第"
                        +entity.getFilterElementLevel()+"级别的"+entity.getName()+"滤芯");//履历内容
            }
            if (IWtFilterElementService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除滤芯：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "给滤芯分配滤芯")
    @ApiOperation("查询滤芯明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('water:wtFilterElement:read')")
    public Object getWtFilterElementById(@ApiParam("滤芯id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("滤芯id");
            }
            WtFilterElement coupon = IWtFilterElementService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询滤芯明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除滤芯")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "water", REMARK = "批量删除滤芯")
    @PreAuthorize("hasAuthority('water:wtFilterElement:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = IWtFilterElementService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "water", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, WtFilterElement entity) {
        // 模拟从数据库获取需要导出的数据
        List<WtFilterElement> personList = IWtFilterElementService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", WtFilterElement.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "water", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<WtFilterElement> personList = EasyPoiUtils.importExcel(file, WtFilterElement.class);
        IWtFilterElementService.saveBatch(personList);
    }
}


