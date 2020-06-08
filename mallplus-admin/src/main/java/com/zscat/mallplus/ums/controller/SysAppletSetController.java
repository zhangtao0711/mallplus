package com.zscat.mallplus.ums.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jfinal.kit.StrKit;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.core.enums.SignType;
import com.zscat.mallplus.core.kit.HttpKit;
import com.zscat.mallplus.core.kit.IpKit;
import com.zscat.mallplus.core.kit.WxPayKit;
import com.zscat.mallplus.exception.ApiMallPlusException;
import com.zscat.mallplus.merchant.entity.MerchatFacilitatorConfig;
import com.zscat.mallplus.merchant.service.IMerchatFacilitatorConfigService;
import com.zscat.mallplus.sys.util.JsonUtils;
import com.zscat.mallplus.ums.entity.SysApaySet;
import com.zscat.mallplus.ums.entity.SysAppletSet;
import com.zscat.mallplus.ums.service.ISysApaySetService;
import com.zscat.mallplus.ums.service.ISysAppletSetService;
import com.zscat.mallplus.ums.service.RedisService;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.wxpay.WxPayApi;
import com.zscat.mallplus.wxpay.model.OrderQueryModel;
import com.zscat.mallplus.wxpay.model.ProfitSharingModel;
import com.zscat.mallplus.wxpay.model.TransferModel;
import com.zscat.mallplus.wxpay.model.UnifiedOrderModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * <p>
 * </p>
 *
 * @author zscat
 * @since 2019-06-15
 */
@Slf4j
@RestController
@Api(tags = "SysAppletSetController", description = "管理")
@RequestMapping("/ums/SysAppletSet")
public class SysAppletSetController {
    @Resource
    private ISysAppletSetService ISysAppletSetService;
    @Resource
    private IMerchatFacilitatorConfigService configService;
    @Resource
    private RedisService redisService;
    @Resource
    private ISysApaySetService sysApaySetService;


    @SysLog(MODULE = "ums", REMARK = "根据条件查询所有列表")
    @ApiOperation("根据条件查询所有列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('ums:SysAppletSet:read')")
    public Object getSysAppletSetByPage(SysAppletSet entity,
                                        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(ISysAppletSetService.page(new Page<SysAppletSet>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "ums", REMARK = "保存")
    @ApiOperation("保存")
    @PostMapping(value = "/create")
    public Object saveSysAppletSet(@RequestBody SysAppletSet entity) {
        try {
            if (ISysAppletSetService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "ums", REMARK = "更新")
    @ApiOperation("更新")
    @PostMapping(value = "/update/{id}")
    public Object updateSysAppletSet(@RequestBody SysAppletSet entity) {
        try {

            if (ISysAppletSetService.update(entity, new QueryWrapper<>())) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "ums", REMARK = "删除")
    @ApiOperation("删除")
    @GetMapping(value = "/delete/{id}")
    public Object deleteSysAppletSet(@ApiParam("id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("id");
            }
            if (ISysAppletSetService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "ums", REMARK = "给分配")
    @ApiOperation("查询明细")
    @GetMapping(value = "/{id}")
    public Object getSysAppletSetById(@ApiParam("id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("id");
            }
            SysAppletSet coupon = ISysAppletSetService.getOne(new QueryWrapper<>());
            if (coupon == null) {
                coupon = new SysAppletSet();
                ISysAppletSetService.save(coupon);
            }
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "批量删除")
    public Object deleteBatch(@RequestParam("ids") List<Long> ids) {
        boolean count = ISysAppletSetService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "ums", REMARK = "获取企业微信付款到零钱账号信息(不等于)level是当前经销商的等级，不是你要查的")
    @ApiOperation("获取企业微信付款到零钱账号信息(不等于)level是当前经销商的等级，不是你要查的")
    @GetMapping(value = "/getMonitorFirm")
    public Object getMonitorFirm(@RequestParam Integer level,
                                 @RequestParam Integer storeId,
                                 @RequestParam String value) {
        //这里获取账号信息，level是当前经销商的等级，不是你要查的
        List<Map<String,Object>> list = ISysAppletSetService.
                getMonitorFirm(level,value,storeId);
        return new CommonResult().success(list);
    }

    @SysLog(MODULE = "ums", REMARK = "监测账号有没有设置付款功能")
    @ApiOperation("监测账号有没有设置付款功能")
    @PostMapping(value = "/monitorFirmPay")
    public Object monitorFirmPay(@RequestBody SysAppletSet entity, HttpServletRequest request) {
        //这里是测试企业付款到零钱，卡设置，等设置写了
        SysApaySet apaySet = sysApaySetService.getById(entity.getParentUserId());
        if (apaySet==null){
            return new CommonResult().failed("该账号没有开通企业付款功能！");
        }
        return new CommonResult().success(apaySet);
    }

    @SysLog(MODULE = "ums", REMARK = "监测上级账号的企业付款到零钱功能")
    @ApiOperation("监测上级账号的企业付款到零钱功能")
    @PostMapping(value = "/monitorWeChantPay")
    public Object monitorWeChantPay(@RequestBody SysAppletSet entity, HttpServletRequest request) {
        Long userId = entity.getUserId();
        String receiptAccount = entity.getReceiptAccount();
        if (ValidatorUtils.empty(userId)||ValidatorUtils.empty(receiptAccount)){
            return new CommonResult().failed("参数不能为空！");
        }
        //2.获取IP
        String ip = IpKit.getRealIp(request);
        if (StrKit.isBlank(ip)) {
            ip = "127.0.0.1";
        }
        //其实这里应该获取的是企业付款到零钱的付款设置信息
        entity = ISysAppletSetService.getById(userId);
        if (entity==null){
            return new CommonResult().failed("数据不存在，请检查参数！");
        }
        //这里是测试企业付款到零钱，卡设置，等设置写了上级数据
        SysApaySet apaySet = sysApaySetService.getById(entity.getParentUserId());
        //4.拼接需要的数据
        Map<String, String> params = TransferModel
                .builder()
                .mch_appid(apaySet.getMchAppid())
                .mchid(apaySet.getMchId())
                .nonce_str(WxPayKit.generateStr())
                .partner_trade_no(WxPayKit.generateStr())
                .openid(receiptAccount)
                .check_name("NO_CHECK")
                .amount("100")
                .desc("微信支付-企业付款到零钱")
                .spbill_create_ip(ip)
                .build()
                .createSign(entity.getPaySignKey(), SignType.HMACSHA256, false);
        // 提现
        String transfers = WxPayApi.transfers(params, apaySet.getCertCatalog(), apaySet.getMchId());
        log.info("提现结果:" + transfers);
        Map<String, String> map = WxPayKit.xmlToMap(transfers);
        String returnCode = map.get("return_code");
        String resultCode = map.get("result_code");
        if (WxPayKit.codeIsOk(returnCode) && WxPayKit.codeIsOk(resultCode)) {
            // 提现成功
            return new CommonResult().success();
        } else {
            // 提现失败
            return new CommonResult().failed("错误码：" +map.get("err_code") + "错误原因："+map.get("err_code_des"));
        }
    }




    @SysLog(MODULE = "ums", REMARK = "监测分账功能")
    @ApiOperation("监测分账功能")
    @PostMapping(value = "/monitorProfitShare")
    public Object monitorProfitShare(@RequestBody SysAppletSet entity, HttpServletRequest request) {
        String ip = IpKit.getRealIp(request);
        if (StrKit.isBlank(ip)) {
            ip = "127.0.0.1";
        }
        if (entity.getSelfType()==2){
            //有代收账号，走的是微信企业付款到零钱
        }
        //这里走的是分账
        //1.服务商的支付信息
        MerchatFacilitatorConfig config = configService.getById(1);
        if (config==null){
            return new CommonResult().failed("服务商的支付数据不存在，请检查参数！");
        }
        //2.统一下单到微信
        String out_trade_no =WxPayKit.generateStr();
        Map<String, String> params = UnifiedOrderModel.builder()
                .appid(config.getAppid())
                .mch_id(config.getMchId())
                .sub_appid(entity.getAppid())
                .sub_mch_id(entity.getMchid())
                .nonce_str(WxPayKit.generateStr())
                .body("测试分账")
                .out_trade_no(out_trade_no)
                .total_fee("100")
                .spbill_create_ip(ip)
                .notify_url(entity.getNotifyurl()+"//ums/SysAppletSet/payNotify")
                .trade_type("NATIVE")
                .build()
                .createSign(config.getSecret(),SignType.HMACSHA256);
        //5.统一下单
        String xmlResult = WxPayApi.pushOrder(params);
        log.info("统一下单:" + xmlResult);
        //6.将返回信息的信息转成map
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
        //7.获取return_code
        String returnCode = result.get("return_code");
        String returnMsg = result.get("return_msg");
        System.out.println(returnMsg);
        //8.如果失败则判断状态存储起来
        if (!WxPayKit.codeIsOk(returnCode)) {
            return new CommonResult().failed("error:" + returnMsg);
        }
        //9.return_code返回的是成功，则判断result_code
        String resultCode = result.get("result_code");
        //如果失败则判断状态存储起来
        if (!WxPayKit.codeIsOk(resultCode)) {
            return new CommonResult().failed("error:" + result.get("err_code_des"));
        }
        redisService.set(out_trade_no,JSONObject.toJSONString(entity));
        return new CommonResult().success(out_trade_no);
    }

    /**
     * 异步通知
     */
    @RequestMapping(value = "/payNotify", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String payNotify(HttpServletRequest request) {
        //1.获取微信异步通知结果准换成map
        String xmlMsg = HttpKit.readData(request);
        Map<String, String> params = WxPayKit.xmlToMap(xmlMsg);
        log.info("微信支付通知=" + params);
        //2.获取返回的code
        String returnCode = params.get("return_code");
        //3.获取订单编号，根据订单编号获取唯一的订单
        String out_trade_no = params.get("out_trade_no");
        String transaction_id = params.get("transaction_id");
        MerchatFacilitatorConfig config = configService.getById(1);
        if (config==null){
            return "服务商的支付数据不存在，请检查参数！";
        }
        if (WxPayKit.verifyNotify(params, config.getSecret(), SignType.HMACSHA256)) {
            if (WxPayKit.codeIsOk(returnCode)) {
                // 发送通知等
                Map<String, String> xml = new HashMap<String, String>(2);
                xml.put("return_code", "SUCCESS");
                xml.put("return_msg", "OK");
                //分账
                SysAppletSet set = JsonUtils.jsonToPojo(redisService.get(out_trade_no),SysAppletSet.class);
                String fenzhang = WxPayKit.generateStr();
                JSONObject info = new JSONObject();
                info.put("fenzhang",fenzhang);
                info.put("transaction_id",transaction_id);
                info.put("mch_id",params.get("mch_id"));
                info.put("sub_mch_id",params.get("sub_mch_id"));
                info.put("secret",config.getSecret());
                redisService.set(out_trade_no,info.toJSONString());
                Map<String, String> param = ProfitSharingModel.builder()
                        .appid(config.getAppid())
                        .mch_id(config.getMchId())
                        .sub_mch_id(params.get("sub_mch_id"))
                        .nonce_str(WxPayKit.generateStr())
                        .sign_type("HMAC-SHA256")
                        .transaction_id(transaction_id)
                        .out_order_no(fenzhang)
                        .receivers(set.getReceivers())
                        .build()
                        .createSign(config.getSecret(),SignType.HMACSHA256, false);
                String rout = WxPayApi.profitSharing(param, config.getApiclientCertP12(),config.getMchId());
                Map<String, String> map = WxPayKit.xmlToMap(rout);
                String returncode = map.get("return_code");
                String resultCode = map.get("result_code");
                if (WxPayKit.codeIsOk(returncode) && WxPayKit.codeIsOk(resultCode)) {
                    // 分账成功
                    redisService.remove(out_trade_no);
                    log.info("订单分账成功！");
                    return WxPayKit.toXml(xml);
                } else {
                    // 分账失败
                    log.error("分账失败：错误代码" +map.get("err_code")+ "错误代码描述"+map.get("err_code_des"));
                    return WxPayKit.toXml(xml);
                }
            } else {
                log.error("订单" + out_trade_no + "支付失败");
            }
        }else {
            // 发送通知等
            Map<String, String> xml = new HashMap<String, String>(2);
            xml.put("return_code", "FAIL");
            xml.put("return_msg", "签名失败");
            return WxPayKit.toXml(xml);
        }
        return null;
    }

    @SysLog(MODULE = "ums", REMARK = "查询分账结果")
    @ApiOperation(value = "查询分账结果")
    @GetMapping("orderQuery")
    public Object orderQuery(@RequestParam String out_trade_no) {
        JSONObject map = JSONObject.parseObject(redisService.get(out_trade_no));
        redisService.remove(out_trade_no);
        Map<String, String> params = OrderQueryModel
                .builder()
                .mch_id(map.getString("mch_id"))
                .sub_mch_id(map.getString("sub_mch_id"))
                .out_trade_no(map.getString("fenzhang"))
                .transaction_id(map.getString("transaction_id"))
                .nonce_str(WxPayKit.generateStr())
                .build()
                .createSign(map.getString("secret"), SignType.HMACSHA256);
        String xmlResult = WxPayApi.profitSharingQuery(params);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
        // 响应报文
        String return_code = result.get("return_code");
        String return_msg = result.get("return_msg");
        if (!"SUCCESS".equals(return_code)) {
            return new CommonResult().failed("查询失败,error=" + return_msg);
        }
        String resultCode = result.get("result_code");
        if ("SUCCESS".equals(resultCode)) {
            return new CommonResult().success("分账成功");
        } else {
            return new ApiMallPlusException("分账失败:" + result.get("err_code") + result.get("err_code_des"));
        }
    }
}
