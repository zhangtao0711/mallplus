package com.zscat.mallplus.water.controller;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.sys.entity.SysUser;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.ums.entity.UmsMemberLevel;
import com.zscat.mallplus.ums.service.IUmsMemberService;
import com.zscat.mallplus.util.ConstantUtil;
import com.zscat.mallplus.util.UserUtils;
import com.zscat.mallplus.water.entity.WtWaterCard;
import com.zscat.mallplus.water.service.IWtWaterCardService;
import com.zscat.mallplus.util.EasyPoiUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.water.vo.WtWaterCardExcelBind;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.List;
import java.util.Date;
import java.util.Map;

/**
 * @author lyn
 * @date 2020-05-30
 * 水卡
 */
@Slf4j
@RestController
@Api(tags = "WtWaterCardController", description = "水卡")
@RequestMapping("/water/wtWaterCard")
public class WtWaterCardController {

    @Resource
    private IWtWaterCardService IWtWaterCardService;
    @Resource
    private IUmsMemberService IUmsMemberService;

    @SysLog(MODULE = "water", REMARK = "根据条件查询所有水卡列表")
    @ApiOperation("根据条件查询所有水卡列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('water:wtWaterCard:read')")
    public Object getWtWaterCardByPage(WtWaterCard entity,
                                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
//            return new CommonResult().success(IWtWaterCardService.page(new Page<WtWaterCard>(pageNum, pageSize), new QueryWrapper<>(entity)));
            return new CommonResult().success(IWtWaterCardService.selectData(new Page<Map<String, Object>>(pageNum, pageSize),
                    entity));
        } catch (Exception e) {
            log.error("根据条件查询所有水卡列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "根据条件查询所有问题卡列表")
    @ApiOperation("根据条件查询所有问题卡列表")
    @GetMapping(value = "/listProblem")
    @PreAuthorize("hasAuthority('water:wtWaterCard:read')")
    public Object listProblem(WtWaterCard entity,
                                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {

            entity.setDelFlag(ConstantUtil.delFlag);
//            return new CommonResult().success(IWtWaterCardService.page(new Page<WtWaterCard>(pageNum, pageSize), new QueryWrapper<>(entity)));
            return new CommonResult().success(IWtWaterCardService.selectProblemData(new Page<Map<String, Object>>(pageNum, pageSize),
                    entity,ConstantUtil.water_code_state_0));
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
    @PostMapping(value = "/updateSale/{id}")
    @PreAuthorize("hasAuthority('water:wtWaterCard:update')")
    public Object updateWtWaterCardSale(@RequestBody WtWaterCard entity) {
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

    @SysLog(MODULE = "water", REMARK = "水卡分配")
    @ApiOperation("水卡分配")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('water:wtWaterCard:update')")
    public Object updateWtWaterCard(@RequestBody WtWaterCard entity) {
        try {
            WtWaterCard coupon = IWtWaterCardService.getById(entity.getId());
            coupon.setSaleBy(entity.getSaleBy());//售卡人
            entity.setUpdateTime(new Date());
            if (IWtWaterCardService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("水卡分配：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }
    @SysLog(MODULE = "water", REMARK = "限额消费金额")
    @ApiOperation("限额消费金额")
    @PostMapping(value = "/updateLimit")
//    @PreAuthorize("hasAuthority('water:wtWaterCard:update')")
    public Object updateLimit(@RequestBody WtWaterCard entity) {
        try {

            WtWaterCard coupon = IWtWaterCardService.getOneBy(entity);
            if(coupon !=null){
                coupon.setXfxeLimit(entity.getXfxeLimit());//单次消费限额
                coupon.setXfNumLimit(entity.getXfNumLimit());//消费次数限制（天）
                entity.setUpdateTime(new Date());
                if (IWtWaterCardService.updateById(entity)) {
                    return new CommonResult().success();
                }
            }else{
                return new CommonResult().failed("水卡不存在！");
            }

        } catch (Exception e) {
            log.error("限额消费金额：%s", e.getMessage(), e);
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

    @SysLog(MODULE = "water", REMARK = "查询水卡明细")
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
    public Object importExcelBind(@RequestParam MultipartFile file) {
        List<WtWaterCardExcelBind> personList = EasyPoiUtils.importExcel(file, WtWaterCardExcelBind.class);
        for (WtWaterCardExcelBind wtWaterCardExcelBind : personList){
            WtWaterCard entity = new WtWaterCard();
            entity.setDelFlag(ConstantUtil.delFlag);
            entity.setCardNo(wtWaterCardExcelBind.getCardNo());
            entity = IWtWaterCardService.getOneBy(entity);
            if(entity==null || entity.getId()==null){
                return new CommonResult().success("没有找到对应的实体卡，卡号【"+wtWaterCardExcelBind.getCardNo()+"】！");
            }
            UmsMember coupon = IUmsMemberService.getById(wtWaterCardExcelBind.getUmsMemberId());
            if(coupon==null || coupon.getId()==null){
                return new CommonResult().success("没有找到对应的用户id，id【"+wtWaterCardExcelBind.getUmsMemberId()+"】！");
            }
            entity.setUmsMemberId(wtWaterCardExcelBind.getUmsMemberId());
            entity.setUpdateTime(new Date());
            SysUser user = UserUtils.getCurrentMember();
            entity.setUpdateBy(user.getId());
            IWtWaterCardService.updateById(entity);
        }
        return new CommonResult().success("批量绑定成功！");
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
    @PostMapping(value = "/updateState/{cardNo}")
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


