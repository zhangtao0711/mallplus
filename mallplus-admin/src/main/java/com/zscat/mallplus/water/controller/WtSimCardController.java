package com.zscat.mallplus.water.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.jfinal.kit.StrKit;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.core.enums.SignType;
import com.zscat.mallplus.core.enums.TradeType;
import com.zscat.mallplus.core.kit.HttpKit;
import com.zscat.mallplus.core.kit.IpKit;
import com.zscat.mallplus.core.kit.QrCodeKit;
import com.zscat.mallplus.core.kit.WxPayKit;
import com.zscat.mallplus.exception.ApiMallPlusException;
import com.zscat.mallplus.merchant.entity.MerchatFacilitatorConfig;
import com.zscat.mallplus.merchant.service.IMerchatFacilitatorConfigService;
import com.zscat.mallplus.set.entity.SetSalesOrder;
import com.zscat.mallplus.set.service.ISetSalesOrderService;
import com.zscat.mallplus.ums.entity.SysAppletSet;
import com.zscat.mallplus.ums.service.ISysAppletSetService;
import com.zscat.mallplus.ums.service.RedisService;
import com.zscat.mallplus.util.ConstantUtil;
import com.zscat.mallplus.water.entity.*;
import com.zscat.mallplus.water.mapper.WtSimUrlInfoMapper;
import com.zscat.mallplus.water.service.IWtSimCardService;
import com.zscat.mallplus.util.EasyPoiUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.water.service.IWtSimSendOrderService;
import com.zscat.mallplus.water.service.IWtSimSendOrderWxService;
import com.zscat.mallplus.wxpay.WxPayApi;
import com.zscat.mallplus.wxpay.WxPayApiConfig;
import com.zscat.mallplus.wxpay.model.CloseOrderModel;
import com.zscat.mallplus.wxpay.model.OrderQueryModel;
import com.zscat.mallplus.wxpay.model.UnifiedOrderModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.util.Map;

/**
 * @author lyn
 * @date 2020-05-22
 * SIM卡列表
 */
@Slf4j
@RestController
@Api(tags = "WtSimCardController", description = "SIM卡列表")
@RequestMapping("/water/wtSimCard")
public class WtSimCardController {

    @Resource
    private IWtSimCardService IWtSimCardService;
    @Resource
    private IWtSimSendOrderWxService IWtSimSendOrderWxService;

    @Resource
    private IMerchatFacilitatorConfigService configService;
    @Resource
    private ISysAppletSetService appletSetService;
    @Resource
    private WtSimUrlInfoMapper wtSimUrlInfoMapper;
    @Resource
    private RedisService redisService;
    @Resource
    private IWtSimSendOrderService IWtSimSendOrderService;

    private String notifyUrl = "http://java.chengguo.link:8081/api";
    private String refundNotifyUrl;

    @SysLog(MODULE = "water", REMARK = "根据条件查询所有SIM卡列表列表")
    @ApiOperation("根据条件查询所有SIM卡列表列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('water:wtSimCard:read')")
    public Object getWtSimCardByPage(WtSimCard entity,
                                     @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                     @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
//            return new CommonResult().success(IWtSimCardService.page(new Page<WtSimCard>(pageNum, pageSize), new QueryWrapper<>(entity)));
            entity.setDelFlag(ConstantUtil.delFlag);
            return new CommonResult().success(IWtSimCardService.selectData(new Page<Map<String, Object>>(pageNum, pageSize),
                    entity));
        } catch (Exception e) {
            log.error("根据条件查询所有SIM卡列表列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "保存SIM卡列表")
    @ApiOperation("保存SIM卡列表")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('water:wtSimCard:create')")
    public Object saveWtSimCard(@RequestBody WtSimCard entity) {
        try {
            entity.setDelFlag(ConstantUtil.delFlag);
            entity.setCreateTime(new Date());
            if (IWtSimCardService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存SIM卡列表：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

//    @SysLog(MODULE = "water", REMARK = "SIM卡时查询")
//    @ApiOperation("添加SIM卡时查询")
//    @GetMapping(value = "/getCardInfo")
////    @PreAuthorize("hasAuthority('water:wtSimCard:read')")
//    public Object getCardInfo(@ApiParam("SIM卡号") @RequestParam String cardno) {
//        try {
//            if (ValidatorUtils.empty(cardno)) {
//                return new CommonResult().paramFailed("请输入SIM卡号！");
//            }
//            WtSimCard coupon = IWtSimCardService.getByCardno(cardno);
//            if(coupon!=null && coupon.getId()!=null){
//                return new CommonResult().paramFailed("此卡号已经添加过，不能重复添加！");
//            }
//            coupon = IWtSimCardService.getByCardnoUrl(cardno);
//            return new CommonResult().success(coupon);
//        } catch (Exception e) {
//            log.error("查询SIM卡列表明细：%s", e.getMessage(), e);
//            return new CommonResult().failed();
//        }
//    }

    @SysLog(MODULE = "water", REMARK = "物联网卡余量查询")
    @ApiOperation("物联网卡余量查询")
    @GetMapping(value = "/getChaxun/{cardno}")
    @PreAuthorize("hasAuthority('water:wtSimCard:read')")
    public Object getChaxun(@ApiParam("SIM卡号")@PathVariable String cardno) {
        try {
            JSONObject coupon = IWtSimCardService.getChaxun(cardno);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("物联网卡余量查询：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "water", REMARK = "物联网卡停机")
    @ApiOperation("物联网卡停机")
    @GetMapping(value = "/stop/{cardno}")
    @PreAuthorize("hasAuthority('water:wtSimCard:read')")
    public Object stop(@ApiParam("SIM卡号")@PathVariable String cardno) {
        try {
            SimEntity coupon = IWtSimCardService.stop(cardno);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("物联网卡余量查询：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }
    @SysLog(MODULE = "water", REMARK = "物联网卡复机")
    @ApiOperation("物联网卡复机")
    @GetMapping(value = "/start/{cardno}")
    @PreAuthorize("hasAuthority('water:wtSimCard:read')")
    public Object start(@ApiParam("SIM卡号")@PathVariable String cardno) {
        try {
            SimEntity coupon = IWtSimCardService.start(cardno);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("物联网卡余量查询：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "water", REMARK = "物联网卡充值/充值就是激活")
    @ApiOperation("物联网卡充值/充值就是激活")
    @PostMapping(value = "/sendOrder")
    public Object saveOrder(@RequestBody WtSimSendOrderWx entity , HttpServletRequest request, HttpServletResponse response) {
        String outTradeNo = WxPayKit.generateStr();
        entity.setLogno(outTradeNo);//支付订单编号
        entity.setPaytype(Integer.parseInt(ConstantUtil.wx_pay));//微信支付
        try {
            //订单金额
            WtSimUrlInfo coupon = new WtSimUrlInfo();
            coupon = wtSimUrlInfoMapper.selectOne(new QueryWrapper<>(coupon));
            entity.setPrice(coupon.getProductMoney());//套餐金额
            entity.setCreateTime(new Date());
            if (IWtSimSendOrderWxService.save(entity)) {
                if (entity.getPaytype()==Integer.parseInt(ConstantUtil.wx_pay)) {
                    //扫码支付统一下单-服务商支付
                    //1.获取支付的金额
                    if (entity.getPrice()==null){
                        return new CommonResult().failed("订单金额不能为空！");
                    }
                    BigDecimal price = entity.getPrice().multiply(new BigDecimal("100")).setScale(BigDecimal.ROUND_DOWN,0);
                    //2.获取IP
                    String ip = IpKit.getRealIp(request);
                    if (StrKit.isBlank(ip)) {
                        ip = "127.0.0.1";
                    }
                    //3. 获取服务商和底下的特约商户的信息（平台)
                    WxPayApiConfig config = this.getApiConfig();
                    if (config==null){
                        return new CommonResult().failed("没有设置支付配置");
                    }
                    //4.拼接需要的数据
                    Map<String, String> params = UnifiedOrderModel
                            .builder()
                            .appid(config.getAppId())
                            .mch_id(config.getMchId())
                            .sub_appid(config.getSlAppId())
                            .sub_mch_id(config.getSlMchId())
                            .nonce_str(WxPayKit.generateStr())
                            .body("扫码支付-SIM卡充值")
                            .out_trade_no(outTradeNo)
                            .total_fee(price.toString())
                            .spbill_create_ip(ip)
                            .notify_url(notifyUrl)
                            .trade_type(TradeType.NATIVE.getTradeType())
                            .build()
                            .createSign(config.getPartnerKey(), SignType.HMACSHA256);
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
                        //状态有这几种，1预支付成功，2预支付失败，3支付成功，4支付失败，5交易关闭
                        entity.setPaystatus(ConstantUtil.payStatus_2);
                        entity.setError(returnMsg);
                        IWtSimSendOrderWxService.updateById(entity);
                        return new CommonResult().failed("error:" + returnMsg);
                    }
                    //9.return_code返回的是成功，则判断result_code
                    String resultCode = result.get("result_code");
                    //如果失败则判断状态存储起来
                    if (!WxPayKit.codeIsOk(resultCode)) {
                        entity.setPaystatus(ConstantUtil.payStatus_2);
                        entity.setError(result.get("err_code")+": " + result.get("err_code_des"));
                        IWtSimSendOrderWxService.updateById(entity);
                        return new CommonResult().failed("error:" + returnMsg);
                    }
                    //10.生成预付订单success,保存预支付信息
                    entity.setPaystatus(ConstantUtil.payStatus_1);
                    entity.setPrepayId(result.get("prepay_id"));
                    entity.setCodeUrl(result.get("code_url"));
                    IWtSimSendOrderWxService.updateById(entity);
                    //11.拿到二维码连接，并起名字
                    String qrCodeUrl = result.get("code_url");
                    String name = "payQRCode2.png";
                    //12.生成二维码
                    Boolean encode = QrCodeKit.encode(qrCodeUrl, BarcodeFormat.QR_CODE, 3, ErrorCorrectionLevel.H, "png", 200, 200,
                            request.getSession().getServletContext().getRealPath("/") + File.separator + name);
                    if (encode) {
                        //13.在页面上显示
                        return new CommonResult().success(name);
                    }else {
                        return new CommonResult().failed("二维码生成错误，请点击'取消支付',关闭交易，并联系管理员！");
                    }
                }
//                else if (entity.getPaytype().equals(ConstantUtil.ye_pay)){
//                    //更新余额支付
//                    SysUser user = sysUserService.getById(entity.getDealerId());
//                    if (user==null){
//                        return new CommonResult().failed("经销商信息错误，请联系管理员！");
//                    }
//                    user.setBalance((user.getBalance().subtract(entity.getPrice())).setScale(BigDecimal.ROUND_DOWN,2));
//                    sysUserService.updateById(user);
//                }else if (entity.getPaytype().equals(ConstantUtil.jf_pay)){
//                    //更新积分
//                    SysUser user = sysUserService.getById(entity.getDealerId());
//                    if (user==null){
//                        return new CommonResult().failed("经销商信息错误，请联系管理员！");
//                    }
//                    user.setIntegration(user.getIntegration()-entity.getIntegration());
//                    sysUserService.updateById(user);
//                }else {
//                    return new CommonResult().failed("还在玩命开发中，请稍后！");
//                }
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("SIM卡充值：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
//        //除了微信支付，其余支付均在这里更新用户授权，购买了加上授权，每次登陆都校验到期时间，到期了就去掉授权
//        if(ISetSalesOrderService.saveBuyInfo(entity)) {
//            if(ISetSalesOrderService.addAudit(entity.getDealerId(), entity.getPerssionId(), entity.getStoreId())){
//                return new CommonResult().success(entity);
//            }else {
//                return new CommonResult().failed("授权失败！");
//            }
//        }
        return new CommonResult().failed("充值失败！");
    }
    //微信支付信息获取
    public WxPayApiConfig getApiConfig() {
        WxPayApiConfig apiConfig = null;
        //服务商的支付信息
        MerchatFacilitatorConfig config = configService.getById(1);
        if (config==null){
            return apiConfig;
        }
        //拿到平台的数据,商户号和APPID等信息
        SysAppletSet set = new SysAppletSet();
        set.setStoreId(1);
        SysAppletSet appletSet = appletSetService.getOne(new QueryWrapper<>(set));
        apiConfig = WxPayApiConfig.builder()
                .appId(config.getAppid())
                .mchId(config.getMchId())
                .slAppId(appletSet.getAppid())
                .partnerKey(config.getSecret())
//                .certPath(wxPayBean.getCertPath())
                .domain("https://api.mch.weixin.qq.com")
                .build();
        notifyUrl = apiConfig.getDomain().concat("/set/setSalesOrder/payNotify");
        refundNotifyUrl = apiConfig.getDomain().concat("/set/setSalesOrder/refundNotify");
        return apiConfig;
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
        WtSimSendOrderWx wtSimSendOrderWx = new WtSimSendOrderWx();
        wtSimSendOrderWx.setLogno(out_trade_no);
        WtSimSendOrderWx order = IWtSimSendOrderWxService.getOne(new QueryWrapper<>(wtSimSendOrderWx));
        // 注意重复通知的情况，同一订单号可能收到多次通知，请注意一定先判断订单状态
        // 4.注意此处签名方式需与统一下单的签名类型一致
        if (WxPayKit.verifyNotify(params, this.getApiConfig().getPartnerKey(), SignType.HMACSHA256)) {
            if (WxPayKit.codeIsOk(returnCode)) {
                // 5.更新订单信息
                if (order.getPaystatus().equals(ConstantUtil.payStatus_1)) {
                    order.setPaytime(new Date());
                    order.setPaystatus(ConstantUtil.payStatus_3);
                    IWtSimSendOrderWxService.updateById(order);
                    //6.支付成功调用第三方充值接口
                    SimEntity coupon = IWtSimCardService.sendOrder(order.getCardno());
                    WtSimSendOrder wtSimSendOrder = new WtSimSendOrder();
                    wtSimSendOrder.setCardno(order.getCardno());
                    wtSimSendOrder.setLogno(order.getLogno());
                    wtSimSendOrder.setPaytime(order.getPaytime());
                    if(!coupon.getData().getStatus().equals("0")){
                        log.error("SIM卡"+order.getCardno()+"充值失败");
                        order.setPaystatus(Integer.parseInt(coupon.getData().getStatus()));
                        order.setError("SIM卡"+order.getCardno()+"充值失败,原因："+coupon.getData().getMessage());
                        IWtSimSendOrderService.save(wtSimSendOrder);
                    }else{
                        log.error("SIM卡"+order.getCardno()+"充值成功");
                        order.setPaystatus(Integer.parseInt(coupon.getData().getStatus()));
                        order.setError("SIM卡"+order.getCardno()+"充值成功");
                        IWtSimSendOrderService.save(wtSimSendOrder);
                    }
                }
                // 发送通知等
                Map<String, String> xml = new HashMap<String, String>(2);
                xml.put("return_code", "SUCCESS");
                xml.put("return_msg", "OK");
                return WxPayKit.toXml(xml);
            } else {
                log.error("SIM卡"+order.getCardno()+"充值失败");
                order.setPaystatus(ConstantUtil.payStatus_4);
                order.setError("订单" + out_trade_no + "支付失败,原因："+params.get("return_msg"));
                IWtSimSendOrderWxService.updateById(order);
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

    /**
     * 微信查询订单状态
     */
    @SysLog(MODULE = "set", REMARK = "查询订单状态")
    @ApiOperation(value = "查询订单状态")
    @GetMapping("orderQuery")
    public Object orderQuery(@RequestParam String logno) throws Exception {
        String orderquery = "https://api.mch.weixin.qq.com/pay/orderquery";
        WxPayApiConfig config = this.getApiConfig();
        if (config==null){
            return new CommonResult().failed("没有设置支付配置");
        }
        WtSimSendOrderWx wtSimSendOrderWx = new WtSimSendOrderWx();
        wtSimSendOrderWx.setLogno(logno);
        WtSimSendOrderWx order = IWtSimSendOrderWxService.getOne(new QueryWrapper<>(wtSimSendOrderWx));
        if (order==null){
            return new CommonResult().failed("订单不存在");
        }
        Map<String, String> params = OrderQueryModel
                .builder()
                .appid(config.getAppId())
                .mch_id(config.getMchId())
                .sub_appid(config.getSlAppId())
                .sub_mch_id(config.getSlMchId())
                .out_trade_no(order.getLogno())
                .nonce_str(WxPayKit.generateStr())
                .build()
                .createSign(config.getPartnerKey(), SignType.HMACSHA256);
        String xmlResult = WxPayApi.orderQuery(params);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
        // 响应报文
        String return_code = result.get("return_code");
        String return_msg = result.get("return_msg");

        if (!"SUCCESS".equals(return_code)) {
            return new CommonResult().failed("查询失败,error=" + return_msg);
        }

        String trade_state = result.get("trade_state");
        if ("SUCCESS".equals(trade_state)) {
            // 更改订单状态
            // 业务处理
            if (!order.getPaystatus().equals(ConstantUtil.payStatus_3)){
                order.setPaytime(new Date());
                order.setPaystatus(ConstantUtil.payStatus_3);
                IWtSimSendOrderWxService.updateById(order);
                //6.支付成功调用第三方充值接口
                SimEntity coupon = IWtSimCardService.sendOrder(order.getCardno());
                WtSimSendOrder wtSimSendOrder = new WtSimSendOrder();
                wtSimSendOrder.setCardno(order.getCardno());
                wtSimSendOrder.setLogno(order.getLogno());
                wtSimSendOrder.setPaytime(order.getPaytime());
                if(!coupon.getData().getStatus().equals("0")){
                    log.error("SIM卡"+order.getCardno()+"充值失败");
                    order.setPaystatus(Integer.parseInt(coupon.getData().getStatus()));
                    order.setError("SIM卡"+order.getCardno()+"充值失败,原因："+coupon.getData().getMessage());
                    IWtSimSendOrderService.save(wtSimSendOrder);
                }else{
                    log.error("SIM卡"+order.getCardno()+"充值成功");
                    order.setPaystatus(Integer.parseInt(coupon.getData().getStatus()));
                    order.setError("SIM卡"+order.getCardno()+"充值成功");
                    IWtSimSendOrderService.save(wtSimSendOrder);
                }
            }
            return new CommonResult().success("支付成功");
        } else if ("USERPAYING".equals(trade_state)) {
            // 重新查询 正在支付中
            Integer num = 0;
            if (redisService.exists(ConstantUtil.SHOP_CACHE_NAME + logno)){
                num = Integer.parseInt(redisService.get(ConstantUtil.SHOP_CACHE_NAME + logno));
            }
            if (num <= 3) {
                num=num+1;
                redisService.set(ConstantUtil.SHOP_CACHE_NAME + logno,num.toString());
                this.orderQuery(logno);
            } else {
                return new ApiMallPlusException("查询失败,error=" + trade_state);
            }

        } else {
 //           //如果订单查询是未支付状态，数据库里面却是支付成功，那么需要去掉授权，去掉购买明细
//            if (order.getPaystatus().equals(ConstantUtil.payStatus_3)){
//                ISetSalesOrderService.deleteAudit(order.getDealerId(),order.getPerssionId(),order.getStoreId());
//                ISetSalesOrderService.removeBuyInfo(order);
//            }
            return new ApiMallPlusException("用户支付状态:" + trade_state);
        }

        return new ApiMallPlusException("查询失败，未知错误");
    }

    @ApiOperation("关闭订单")
    @RequestMapping(value = "/orderCancel", method = RequestMethod.POST)
    public Object closeOrder(@RequestBody WtSimSendOrderWx order) {
        if (order.getPaystatus()==ConstantUtil.payStatus_5){
            return new CommonResult().failed(400,"订单已关闭，请勿重复操作！");
        }
        if (order.getPaystatus()==ConstantUtil.payStatus_3){
            return new CommonResult().failed(400,"订单已支付，不允许关闭！");
        }
        //设置账号信息
        WxPayApiConfig config = this.getApiConfig();
        if (config==null){
            return new CommonResult().failed("没有设置支付配置");
        }
        //关闭订单
        Map<String, String> params = CloseOrderModel
                .builder()
                .appid(config.getAppId())
                .mch_id(config.getMchId())
                .sub_appid(config.getSlAppId())
                .sub_mch_id(config.getSlMchId())
                .out_trade_no(order.getLogno())
                .nonce_str(WxPayKit.generateStr())
                .build()
                .createSign(config.getPartnerKey(), SignType.HMACSHA256);
        String xmlResult = WxPayApi.closeOrder(params);
        //6.将返回信息的信息转成map
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
        //7.获取return_code
        String returnCode = result.get("return_code");
        String returnMsg = result.get("return_msg");
        //8.如果失败则判断状态存储起来
        if (!WxPayKit.codeIsOk(returnCode)) {
            //状态有这几种，1预支付成功，2预支付失败，3支付成功，4支付失败，5交易关闭
            order.setPaystatus(ConstantUtil.payStatus_6);
            order.setError(returnMsg);
            IWtSimSendOrderWxService.updateById(order);
            return new CommonResult().failed("error:" + returnMsg);
        }
        //9.return_code返回的是成功，则判断result_code
        String resultCode = result.get("result_code");
        //如果失败则判断状态存储起来
        if (!WxPayKit.codeIsOk(resultCode)) {
            order.setPaystatus(ConstantUtil.payStatus_6);
            order.setError(result.get("err_code")+": " + result.get("err_code_des"));
            IWtSimSendOrderWxService.updateById(order);
            return new CommonResult().failed("error:" + returnMsg);
        }
        order.setPaystatus(ConstantUtil.payStatus_5);
        IWtSimSendOrderWxService.updateById(order);
        return new CommonResult().success("交易已关闭");
    }
//    @SysLog(MODULE = "water", REMARK = "物联网卡充值/充值就是激活")
//    @ApiOperation("物联网卡充值/充值就是激活")
//    @GetMapping(value = "/sendOrder/{cardno}")
////    @PreAuthorize("hasAuthority('water:wtSimCard:read')")
//    public Object sendOrder(@ApiParam("SIM卡号")@PathVariable String cardno) {
//        try {
//            SimEntity coupon = IWtSimCardService.sendOrder(cardno);
//            return new CommonResult().success(coupon);
//        } catch (Exception e) {
//            log.error("物联网卡余量查询：%s", e.getMessage(), e);
//            return new CommonResult().failed();
//        }
//    }

    @SysLog(MODULE = "water", REMARK = "更新SIM卡列表")
    @ApiOperation("更新SIM卡列表")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('water:wtSimCard:update')")
    public Object updateWtSimCard(@RequestBody WtSimCard entity) {
        try {
            entity.setUpdateTime(new Date());
            if (IWtSimCardService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新SIM卡列表：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "删除SIM卡列表")
    @ApiOperation("删除SIM卡列表")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('water:wtSimCard:delete')")
    public Object deleteWtSimCard(@ApiParam("SIM卡列表id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("SIM卡列表id");
            }
            if (IWtSimCardService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除SIM卡列表：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "给SIM卡列表分配SIM卡列表")
    @ApiOperation("查询SIM卡列表明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('water:wtSimCard:read')")
    public Object getWtSimCardById(@ApiParam("SIM卡列表id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("SIM卡列表id");
            }
            WtSimCard coupon = IWtSimCardService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询SIM卡列表明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除SIM卡列表")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "water", REMARK = "批量删除SIM卡列表")
    @PreAuthorize("hasAuthority('water:wtSimCard:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = IWtSimCardService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "water", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, WtSimCard entity) {
        // 模拟从数据库获取需要导出的数据
        List<WtSimCard> personList = IWtSimCardService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", WtSimCard.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "water", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<WtSimCard> personList = EasyPoiUtils.importExcel(file, WtSimCard.class);
        IWtSimCardService.saveBatch(personList);
    }


}


