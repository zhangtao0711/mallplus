package com.zscat.mallplus.water.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.sms.entity.SmsRechargePackage;
import com.zscat.mallplus.sms.service.ISmsRechargePackageService;
import com.zscat.mallplus.sys.entity.SysUser;
import com.zscat.mallplus.util.ConstantUtil;
import com.zscat.mallplus.util.StringUtils;
import com.zscat.mallplus.util.UserUtils;
import com.zscat.mallplus.water.entity.WtWaterCardRecharge;
import com.zscat.mallplus.water.service.IWtWaterCardRechargeService;
import com.zscat.mallplus.util.EasyPoiUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.water.vo.WtWaterCardRechargeExcelCreate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author lyn
 * @date 2020-05-31
 * 充值
 */
@Slf4j
@RestController
@Api(tags = "WtWaterCardRechargeController", description = "充值")
@RequestMapping("/water/wtWaterCardRecharge")
public class WtWaterCardRechargeController {

    @Resource
    private IWtWaterCardRechargeService IWtWaterCardRechargeService;
    @Resource
    private ISmsRechargePackageService ISmsRechargePackageService;

    @SysLog(MODULE = "water", REMARK = "根据条件查询所有充值列表")
    @ApiOperation("根据条件查询所有充值列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('water:wtWaterCardRecharge:read')")
    public Object getWtWaterCardRechargeByPage(WtWaterCardRecharge entity,
                                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                               @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IWtWaterCardRechargeService.page(new Page<WtWaterCardRecharge>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有充值列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "批量保存充值")
    @ApiOperation("批量保存充值")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('water:wtWaterCardRecharge:create')")
    public Object saveWtWaterCardRecharge(@RequestBody WtWaterCardRecharge entity) {
        try {
            //选择充值金额
            if(entity.getRechargeMoneyType().equals(ConstantUtil.recharge_money_type_0)){
                //必须输入校验
                if(entity.getStartNo().isEmpty()||entity.getEndNo().isEmpty()
                        ||entity.getRechargeMoney()==null|| entity.getReceivedMoney()==null){
                    return new CommonResult().failed("选择充值金额时，起始卡号、终止卡号、充值金额、实收金额必须输入！");
                }
                //判断起始卡号和终止卡号合理性
                if(Long.valueOf(entity.getStartNo())>Long.valueOf(entity.getEndNo())){
                    return new CommonResult().failed("终止卡号必须大于等于起始卡号！");
                }
                if(Long.valueOf(entity.getEndNo())> ConstantUtil.max_card_no){
                    return new CommonResult().failed("制卡卡号最大值是"+ ConstantUtil.max_card_no +"！");
                }
                //单次不能超过200张
                if(Long.valueOf(entity.getEndNo())-Long.valueOf(entity.getStartNo())+1 > 200){
                    return new CommonResult().failed("单次不能超过200张！");
                }
                //获取卡号关联经销商和登录者经销商是否一致
                if (!IWtWaterCardRechargeService.getDealerId(Long.valueOf(entity.getStartNo()),Long.valueOf(entity.getEndNo()),entity.getDealerId())) {
                    return new CommonResult().failed("此区段内有卡没有绑定在您的账号下！");
                }

                entity.setStartNo(StringUtils.padRight(entity.getStartNo(),9,'0'));
                entity.setEndNo(StringUtils.padRight(entity.getEndNo(),9,'0'));
            //选择充值体验金额
            }else{
                if(entity.getCardType().equals(ConstantUtil.card_type_virtual)){
                    return new CommonResult().failed("体验金额只能充值到实体卡中！");
                }
                //按卡号充值
                if(entity.getRechargeType().equals(ConstantUtil.recharge_type_1)){
                    //必须输入校验
                    if(entity.getStartNo().isEmpty()||entity.getEndNo().isEmpty()
                            ||entity.getExperienceMoney()==null|| entity.getReceivedMoney()==null){
                        return new CommonResult().failed("选择充值金额时，起始卡号、终止卡号、体验金、实收金额、体验到期必须输入！");
                    }
                    //判断起始卡号和终止卡号合理性
                    if(Long.valueOf(entity.getStartNo())>Long.valueOf(entity.getEndNo())){
                        return new CommonResult().failed("终止卡号必须大于等于起始卡号！");
                    }
                    if(Long.valueOf(entity.getEndNo())> ConstantUtil.max_card_no){
                        return new CommonResult().failed("卡号最大值是"+ ConstantUtil.max_card_no +"！");
                    }
                    //单次不能超过200张
                    if(Long.valueOf(entity.getEndNo())-Long.valueOf(entity.getStartNo())+1 <= 200){
                        return new CommonResult().failed("单次不能超过200张！");
                    }
                    //获取卡号关联经销商和登录者经销商是否一致
                    if (!IWtWaterCardRechargeService.getDealerId(Long.valueOf(entity.getStartNo()),Long.valueOf(entity.getEndNo()),entity.getDealerId())) {
                        return new CommonResult().failed("此区段内有卡没有绑定在您的账号下！");
                    }
                    //到期日和有效天数
                    if(entity.getExperienceEndData()==null && entity.getExperienceEndDay()==null){
                        return new CommonResult().failed("到期日或有效天数必须设定一个！");
                    }

                    entity.setStartNo(StringUtils.padRight(entity.getStartNo(),9,'0'));
                    entity.setEndNo(StringUtils.padRight(entity.getEndNo(),9,'0'));
                //筛选充值
                }else{
                    //判断有没有购买用户标签功能
                    Integer count =IWtWaterCardRechargeService.getSalesCount(entity,ConstantUtil.ums_label_perssion_id);
                    if(count == null || count<1){
                        return new CommonResult().failed("您尚未购买此功能，或使用次数已到上线。请开通此功能后再次使用！");
                    }
                    //筛选条件不能没有
                    if(entity.getUmsBalanceMark().isEmpty() && entity.getUmsUseMark().isEmpty()
                            && entity.getUmsRecommendMark().isEmpty() && entity.getUmsCommunity().isEmpty()
                            && entity.getUmsMemberLevel()==null){
                        return new CommonResult().failed("筛选条件至少设定一个！");
                    }
                    //会员卡余额
                    if (!(entity.getUmsBalanceMark().isEmpty() && entity.getUmsBalanceWhere()==null )
                            && !(!entity.getUmsBalanceMark().isEmpty() && entity.getUmsBalanceWhere()!=null)) {
                         return new CommonResult().failed("筛选条件会员卡余额大小和余额数需要同时设定！");
                    }

                    //用水频次
                    if (!(entity.getUmsUseMark().isEmpty() && entity.getUmsUseWhere()==null && entity.getUmsUsePeriod().isEmpty())
                            && !(!entity.getUmsUseMark().isEmpty() && entity.getUmsUseWhere()!=null && !entity.getUmsUsePeriod().isEmpty())) {
                        return new CommonResult().failed("筛选条件用水频次大小、频次数和周期需要同时设定！");
                    }
                    //用户推荐频次
                    if (!(entity.getUmsRecommendMark().isEmpty() && entity.getUmsRecommendWhere()==null && entity.getUmsRecommendPeriod().isEmpty())
                            && !(!entity.getUmsRecommendMark().isEmpty() && entity.getUmsRecommendWhere()!=null && !entity.getUmsRecommendPeriod().isEmpty())) {
                        return new CommonResult().failed("筛选条件用户推荐频次大小、频次数和周期需要同时设定！");
                    }
                }
            }
            entity.setCreateTime(new Date());
            if (IWtWaterCardRechargeService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存充值：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "后台普通充值")
    @ApiOperation("后台普通充值")
    @PostMapping(value = "/createSingle")
    @PreAuthorize("hasAuthority('water:wtWaterCardRecharge:create')")
    public Object saveWtWaterCardRechargeSingle(@RequestBody WtWaterCardRecharge entity) {
        try {
            //普通充值
            entity.setRechargeType(ConstantUtil.recharge_type_0);
            entity.setRechargeMoneyType(ConstantUtil.recharge_money_type_0);//充值金额
            //必须输入校验
            if(entity.getCardNo().isEmpty()
                    ||entity.getRechargeMoney()==null|| entity.getReceivedMoney()==null){
                return new CommonResult().failed("卡号、充值金额、实收金额必须输入！");
            }
            if(Long.valueOf(entity.getCardNo())> ConstantUtil.max_card_no){
                return new CommonResult().failed("制卡卡号最大值是"+ ConstantUtil.max_card_no +"！");
            }

            entity.setCardNo(StringUtils.padRight(entity.getCardNo(),9,'0'));
            //获取卡号关联经销商和登录者经销商是否一致
            if (!IWtWaterCardRechargeService.getDealerId(Long.valueOf(entity.getCardNo()),Long.valueOf(entity.getCardNo()),entity.getDealerId())) {
                return new CommonResult().failed("此卡没有绑定在您的账号下！");
            }
//            entity.setDelFlag(ConstantUtil.delFlag);
            entity.setCreateTime(new Date());
            if (IWtWaterCardRechargeService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存充值：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "后台套餐充值")
    @ApiOperation("后台套餐充值")
    @PostMapping(value = "/createSinglePackage")
    @PreAuthorize("hasAuthority('water:wtWaterCardRecharge:create')")
    public Object saveSinglePackage(@RequestBody WtWaterCardRecharge entity) {
        try {
            if(entity.getRechargePackage()==null || entity.getRechargePackage().isEmpty()){
                return new CommonResult().failed("请选择充值套餐！");
            }
            //判断有没有购买充值套餐功能
            if(!IWtWaterCardRechargeService.getSalesInfo(entity.getDealerId(),ConstantUtil.recharge_package_id)){
                return new CommonResult().failed("您尚未购买此功能，或已到期。请开通此功能后再次使用！");
            }

            //套餐充值
            entity.setRechargeType(ConstantUtil.recharge_type_0);
            entity.setRechargeMoneyType(ConstantUtil.recharge_money_type_0);//充值金额
            //必须输入校验
            if(entity.getCardNo().isEmpty()
                    ||entity.getReceivedMoney()==null){
                return new CommonResult().failed("卡号、实收金额必须输入！");
            }
            if(Long.valueOf(entity.getCardNo())> ConstantUtil.max_card_no){
                return new CommonResult().failed("制卡卡号最大值是"+ ConstantUtil.max_card_no +"！");
            }

            entity.setCardNo(StringUtils.padRight(entity.getCardNo(),9,'0'));
            //获取卡号关联经销商和登录者经销商是否一致
            if (!IWtWaterCardRechargeService.getDealerId(Long.valueOf(entity.getCardNo()),Long.valueOf(entity.getCardNo()),entity.getDealerId())) {
                return new CommonResult().failed("此卡没有绑定在您的账号下！");
            }

            //计算套餐下的金额
            List<String> rechargePackageIds = Arrays.asList(entity.getRechargePackage().split(","));
            for(String id:rechargePackageIds){
                SmsRechargePackage coupon = ISmsRechargePackageService.getById(id);
                if(coupon !=null && coupon.getActualFee() !=null && coupon.getDonateFee() !=null){
                    entity.setRechargeMoney(entity.getRechargeMoney().add(coupon.getActualFee().subtract(coupon.getDonateFee())));//充值金额=实际到账金额-赠送金额
                }
            }

//            entity.setDelFlag(ConstantUtil.delFlag);
            entity.setCreateTime(new Date());
            if (IWtWaterCardRechargeService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存充值：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "更新充值")
    @ApiOperation("更新充值")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('water:wtWaterCardRecharge:update')")
    public Object updateWtWaterCardRecharge(@RequestBody WtWaterCardRecharge entity) {
        try {
            if (IWtWaterCardRechargeService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新充值：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "删除充值")
    @ApiOperation("删除充值")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('water:wtWaterCardRecharge:delete')")
    public Object deleteWtWaterCardRecharge(@ApiParam("充值id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("充值id");
            }
            if (IWtWaterCardRechargeService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除充值：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "给充值分配充值")
    @ApiOperation("查询充值明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('water:wtWaterCardRecharge:read')")
    public Object getWtWaterCardRechargeById(@ApiParam("充值id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("充值id");
            }
            WtWaterCardRecharge coupon = IWtWaterCardRechargeService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询充值明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除充值")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "water", REMARK = "批量删除充值")
    @PreAuthorize("hasAuthority('water:wtWaterCardRecharge:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = IWtWaterCardRechargeService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "water", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, WtWaterCardRecharge entity) {
        // 模拟从数据库获取需要导出的数据
        List<WtWaterCardRecharge> personList = IWtWaterCardRechargeService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", WtWaterCardRecharge.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "water", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<WtWaterCardRecharge> personList = EasyPoiUtils.importExcel(file, WtWaterCardRecharge.class);
        IWtWaterCardRechargeService.saveBatch(personList);
    }

    @SysLog(MODULE = "water", REMARK = "导入充值")
    @PostMapping("/importExcelCreate")
    public Object importExcelCreate(@RequestParam MultipartFile file) {
        List<WtWaterCardRechargeExcelCreate> personList = EasyPoiUtils.importExcel(file, WtWaterCardRechargeExcelCreate.class);
        for(WtWaterCardRechargeExcelCreate data :personList){
            try {
                WtWaterCardRecharge entity = new WtWaterCardRecharge();
                entity.setRechargeType(ConstantUtil.recharge_type_0);//充值金额//普通充值
                entity.setRechargeMoneyType(ConstantUtil.recharge_money_type_0);//充值金额
                //必须输入校验
                if(data.getCardNo()==null
                        ||data.getRechargeMoney()==null|| data.getReceivedMoney()==null){
                    log.error("导入失败充值金额实收金额必须输入！");
                    return new CommonResult().failed("导入失败充值金额实收金额必须输入！");
                }
                if(Long.valueOf(data.getCardNo())> ConstantUtil.max_card_no){
                    log.error("导入失败超出卡号最大值！");
                    return new CommonResult().failed("导入失败超出卡号最大值！");
                }
                //获取卡号关联经销商和登录者经销商是否一致
                SysUser user = UserUtils.getCurrentMember();
                if (user==null || !IWtWaterCardRechargeService.getDealerId(Long.valueOf(data.getCardNo())
                        ,Long.valueOf(data.getCardNo()),user.getStoreId().longValue())) {

                    log.error("导入失败此卡没有绑定在您的账号下！");
                    return new CommonResult().failed("导入失败此卡没有绑定在您的账号下！");
                }
                entity.setCreateBy(user.getId());
                entity.setCreateTime(new Date());
                IWtWaterCardRechargeService.save(entity);

            } catch (Exception e) {
                log.error("保存充值：%s", e.getMessage(), e);
                return new CommonResult().failed(e.getMessage());
            }
        }
        return new CommonResult().success("导入成功！");
    }


}


