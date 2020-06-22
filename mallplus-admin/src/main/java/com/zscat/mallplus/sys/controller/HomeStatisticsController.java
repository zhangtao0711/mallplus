package com.zscat.mallplus.sys.controller;

import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.sdk.Validator;
import com.zscat.mallplus.sms.service.ISmsRechargeRecordService;
import com.zscat.mallplus.sms.service.ISmsWaterBuyRecordService;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@RestController
@Api(tags = "HomeStatisticsController", description = "首页统计图表")
@RequestMapping("/sys/homeStatistics")
public class HomeStatisticsController {
    @Resource
    private ISmsWaterBuyRecordService smsWaterBuyRecordService;
    @Resource
    private ISmsRechargeRecordService smsRechargeRecordService;

    @ApiOperation("首页统计售水商品")
    @GetMapping(value = "/statisticsWater")
    public Object statisticsWater(@RequestParam Long dealerId,@RequestParam Integer storeId) {
        if (ValidatorUtils.empty(dealerId)||ValidatorUtils.empty(storeId)){
            return new CommonResult().failed("参数不能为空！");
        }
        Map<String,Object> map = smsWaterBuyRecordService.statisticsWater(dealerId,storeId);
        return new CommonResult().success(map);
    }

    @ApiOperation("首页统计售水套餐")
    @GetMapping(value = "/statisticsPackage")
    public Object statisticsPackage(@RequestParam Long dealerId,@RequestParam Integer storeId) {
        if (ValidatorUtils.empty(dealerId)||ValidatorUtils.empty(storeId)){
            return new CommonResult().failed("参数不能为空！");
        }
        Map<String,Object> map = smsRechargeRecordService.statisticsPackage(dealerId,storeId);
        return new CommonResult().success(map);
    }
}
