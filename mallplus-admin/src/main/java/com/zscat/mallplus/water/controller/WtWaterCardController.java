package com.zscat.mallplus.water.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.sys.entity.SysUser;
import com.zscat.mallplus.util.ConstantUtil;
import com.zscat.mallplus.util.UserUtils;
import com.zscat.mallplus.water.entity.WtWaterCard;
import com.zscat.mallplus.water.service.IWtWaterCardService;
import com.zscat.mallplus.util.EasyPoiUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.water.vo.WtWaterCardExcelBind;
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
 * @date 2020-05-30
 * 水卡
 */
@Slf4j
@RestController
@RequestMapping("/water/wtWaterCard")
public class WtWaterCardController {

    @Resource
    private IWtWaterCardService IWtWaterCardService;

    @SysLog(MODULE = "water", REMARK = "根据条件查询所有水卡列表")
    @ApiOperation("根据条件查询所有水卡列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('water:wtWaterCard:read')")
    public Object getWtWaterCardByPage(WtWaterCard entity,
                                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IWtWaterCardService.page(new Page<WtWaterCard>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有水卡列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "保存水卡")
    @ApiOperation("保存水卡")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('water:wtWaterCard:create')")
    public Object saveWtWaterCard(@RequestBody WtWaterCard entity) {
        try {
            entity.setDelFlag(ConstantUtil.delFlag);
            entity.setCreateTime(new Date());
            if (IWtWaterCardService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存水卡：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "更新水卡")
    @ApiOperation("更新水卡")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('water:wtWaterCard:update')")
    public Object updateWtWaterCard(@RequestBody WtWaterCard entity) {
        try {
            entity.setUpdateTime(new Date());
            if (IWtWaterCardService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新水卡：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "删除水卡")
    @ApiOperation("删除水卡")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('water:wtWaterCard:delete')")
    public Object deleteWtWaterCard(@ApiParam("水卡id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("水卡id");
            }
            if (IWtWaterCardService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除水卡：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "给水卡分配水卡")
    @ApiOperation("查询水卡明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('water:wtWaterCard:read')")
    public Object getWtWaterCardById(@ApiParam("水卡id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("水卡id不存在");
            }
            WtWaterCard coupon = IWtWaterCardService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询水卡明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除水卡")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "water", REMARK = "批量删除水卡")
    @PreAuthorize("hasAuthority('water:wtWaterCard:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = IWtWaterCardService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "water", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, WtWaterCard entity) {
        // 模拟从数据库获取需要导出的数据
        List<WtWaterCard> personList = IWtWaterCardService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", WtWaterCard.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "water", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<WtWaterCard> personList = EasyPoiUtils.importExcel(file, WtWaterCard.class);
        IWtWaterCardService.saveBatch(personList);
    }

    @SysLog(MODULE = "water", REMARK = "批量绑定")
    @PostMapping("/importExcelBind")
    @PreAuthorize("hasAuthority('water:wtWaterCard:update')")
    public void importExcelBind(@RequestParam MultipartFile file) {
        List<WtWaterCardExcelBind> personList = EasyPoiUtils.importExcel(file, WtWaterCardExcelBind.class);
        for (WtWaterCardExcelBind wtWaterCardExcelBind : personList){
            WtWaterCard entity = new WtWaterCard();
            entity.setCardNo(wtWaterCardExcelBind.getCardNo());
            entity = IWtWaterCardService.getOneBy(entity);
            entity.setUmsMemberId(wtWaterCardExcelBind.getUmsMemberId());
            entity.setUpdateTime(new Date());
            SysUser user = UserUtils.getCurrentMember();
            entity.setUpdateBy(user.getId());
            IWtWaterCardService.updateById(entity);
        }
    }

    @SysLog(MODULE = "water", REMARK = "会员卡导出")
    @GetMapping("/exportExcelCard")
    public void exportCard(HttpServletResponse response, WtWaterCard entity) {
        // 模拟从数据库获取需要导出的数据
        List<WtWaterCard> personList = IWtWaterCardService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", WtWaterCard.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "water", REMARK = "添加问题卡")
    @ApiOperation("添加问题卡")
    @PostMapping(value = "/updateState/{id}")
    @PreAuthorize("hasAuthority('water:wtWaterCard:update')")
    public Object updateState(@RequestBody WtWaterCard entity) {
        try {
            //获取数据库中卡号信息
            entity.setDelFlag(ConstantUtil.delFlag);
            WtWaterCard wtWaterCardDb = IWtWaterCardService.getOneBy(entity);
            if(wtWaterCardDb != null){
                wtWaterCardDb.setUpdateTime(new Date());
                wtWaterCardDb.setUpdateBy(entity.getUpdateBy());
                wtWaterCardDb.setState(entity.getState());//状态
                wtWaterCardDb.setRemarks(entity.getRemarks());//备注
                if (IWtWaterCardService.updateById(wtWaterCardDb)) {
                    return new CommonResult().success();
                }
            }else{
                return new CommonResult().failed("卡号不存在!");
            }
        } catch (Exception e) {
            log.error("添加问题卡：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "删除问题卡")
    @ApiOperation("删除问题卡")
    @PostMapping(value = "/updateStateRemove/{id}")
    @PreAuthorize("hasAuthority('water:wtWaterCard:update')")
    public Object updateStateRemove(@ApiParam("水卡id") @PathVariable Long id) {
        try {
            //获取数据库中卡号信息
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("水卡id不存在");
            }
            WtWaterCard coupon = IWtWaterCardService.getById(id);

            if(coupon != null){
                coupon.setUpdateTime(new Date());
                coupon.setUpdateBy(UserUtils.getCurrentMember().getId());//当前登录者
                coupon.setState(ConstantUtil.water_code_state_0);//状态正常
                coupon.setRemarks("");//备注
                if (IWtWaterCardService.updateById(coupon)) {
                    return new CommonResult().success();
                }
            }else{
                return new CommonResult().failed("卡号不存在!");
            }
        } catch (Exception e) {
            log.error("添加问题卡：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }
}


