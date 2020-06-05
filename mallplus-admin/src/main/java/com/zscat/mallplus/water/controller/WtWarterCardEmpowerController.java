package com.zscat.mallplus.water.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.ums.service.IUmsMemberService;
import com.zscat.mallplus.util.ConstantUtil;
import com.zscat.mallplus.util.StringUtils;
import com.zscat.mallplus.water.entity.WtWarterCardEmpower;
import com.zscat.mallplus.water.service.IWtWarterCardEmpowerService;
import com.zscat.mallplus.util.EasyPoiUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.water.service.IWtWaterCardRechargeService;
import com.zscat.mallplus.water.service.IWtWaterCardService;
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
 * @date 2020-06-05
 * 水卡授权用户
 */
@Slf4j
@RestController
@RequestMapping("/water/wtWarterCardEmpower")
public class WtWarterCardEmpowerController {

    @Resource
    private IWtWarterCardEmpowerService IWtWarterCardEmpowerService;
    @Resource
    private IWtWaterCardService IWtWaterCardService;
    @Resource
    private IUmsMemberService IUmsMemberService;
    @Resource
    private IWtWaterCardRechargeService IWtWaterCardRechargeService;

    @SysLog(MODULE = "water", REMARK = "根据条件查询所有水卡授权用户列表")
    @ApiOperation("根据条件查询所有水卡授权用户列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('water:wtWarterCardEmpower:read')")
    public Object getWtWarterCardEmpowerByPage(WtWarterCardEmpower entity,
                                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                               @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IWtWarterCardEmpowerService.page(new Page<WtWarterCardEmpower>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有水卡授权用户列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "保存水卡授权用户")
    @ApiOperation("保存水卡授权用户")
    @PostMapping(value = "/create")
//    @PreAuthorize("hasAuthority('water:wtWarterCardEmpower:create')")
    public Object saveWtWarterCardEmpower(@RequestBody WtWarterCardEmpower entity) {
        try {
            //左补位到10位
            entity.setCardNo(StringUtils.padRight(entity.getCardNo(),10,'0'));
            //获取卡号关联经销商和登录者经销商是否一致
            if (!IWtWaterCardRechargeService.getDealerId(Long.valueOf(entity.getCardNo()),Long.valueOf(entity.getCardNo()),entity.getDealerId())) {
                return new CommonResult().failed("此卡没有绑定在您的账号下！");
            }
            //会员卡和授权人是否关联
            if(!IWtWaterCardService.checkCardUms(entity.getCardNo(),entity.getUmsMemberId()
                ,ConstantUtil.delFlag,ConstantUtil.water_code_state_0)){
                return new CommonResult().failed("填写的会员卡和授权人账号不一致!");
            }
            //授权人账号和授权人昵称是否一致
            if(!IUmsMemberService.checkUmsIdNickname(entity.getUmsMemberId(),entity.getUmsMemberNickname()
                    ,ConstantUtil.ums_status_on,entity.getStoreId())){
                return new CommonResult().failed("授权人账号和授权人昵称不一致!");
            }
            //被授权人账号和被授权人昵称是否一致
            if(!IUmsMemberService.checkUmsIdNickname(entity.getUmsMemberToId(),entity.getUmsMemberToNickname()
                    ,ConstantUtil.ums_status_on,entity.getStoreId())){
                return new CommonResult().failed("被授权人账号和被授权人昵称不一致!");
            }
            //水卡下最多绑定用户数
            if(IWtWarterCardEmpowerService.getUmsNum(entity.getCardNo(),ConstantUtil.delFlag)>=ConstantUtil.ums_card_num){
                return new CommonResult().failed("一张水卡最多绑定"+ConstantUtil.ums_card_num+"用户！");
            }
            entity.setDelFlag(ConstantUtil.delFlag);
            entity.setCreateTime(new Date());
            if (IWtWarterCardEmpowerService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存水卡授权用户：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "更新水卡授权用户")
    @ApiOperation("更新水卡授权用户")
    @PostMapping(value = "/update/{id}")
//    @PreAuthorize("hasAuthority('water:wtWarterCardEmpower:update')")
    public Object updateWtWarterCardEmpower(@RequestBody WtWarterCardEmpower entity) {
        try {
            entity.setUpdateTime(new Date());
            if (IWtWarterCardEmpowerService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新水卡授权用户：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "取消水卡授权用户")
    @ApiOperation("取消水卡授权用户")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('water:wtWarterCardEmpower:delete')")
    public Object deleteWtWarterCardEmpower(@ApiParam("水卡授权用户id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("水卡授权数据不存在或已被删除！");
            }
            if (IWtWarterCardEmpowerService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除水卡授权用户：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "给水卡授权用户分配水卡授权用户")
    @ApiOperation("查询水卡授权用户明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('water:wtWarterCardEmpower:read')")
    public Object getWtWarterCardEmpowerById(@ApiParam("水卡授权用户id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("水卡授权用户id");
            }
            WtWarterCardEmpower coupon = IWtWarterCardEmpowerService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询水卡授权用户明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除水卡授权用户")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "water", REMARK = "批量删除水卡授权用户")
    @PreAuthorize("hasAuthority('water:wtWarterCardEmpower:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = IWtWarterCardEmpowerService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "water", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, WtWarterCardEmpower entity) {
        // 模拟从数据库获取需要导出的数据
        List<WtWarterCardEmpower> personList = IWtWarterCardEmpowerService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", WtWarterCardEmpower.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "water", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<WtWarterCardEmpower> personList = EasyPoiUtils.importExcel(file, WtWarterCardEmpower.class);
        IWtWarterCardEmpowerService.saveBatch(personList);
    }
}


