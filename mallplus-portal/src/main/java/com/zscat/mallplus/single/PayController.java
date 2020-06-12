package com.zscat.mallplus.single;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jfinal.kit.StrKit;
import com.zscat.mallplus.annotation.IgnoreAuth;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.core.enums.SignType;
import com.zscat.mallplus.core.enums.TradeType;
import com.zscat.mallplus.core.kit.HttpKit;
import com.zscat.mallplus.core.kit.IpKit;
import com.zscat.mallplus.core.kit.WxPayKit;
import com.zscat.mallplus.enums.AllEnum;
import com.zscat.mallplus.enums.OrderStatus;
import com.zscat.mallplus.exception.ApiMallPlusException;
import com.zscat.mallplus.merchant.entity.MerchatFacilitatorConfig;
import com.zscat.mallplus.merchant.mapper.MerchatFacilitatorConfigMapper;
import com.zscat.mallplus.oms.entity.OmsOrder;
import com.zscat.mallplus.oms.entity.OmsOrderOperateHistory;
import com.zscat.mallplus.oms.entity.OmsPayments;
import com.zscat.mallplus.oms.service.IOmsOrderItemService;
import com.zscat.mallplus.oms.service.IOmsOrderOperateHistoryService;
import com.zscat.mallplus.oms.service.IOmsOrderService;
import com.zscat.mallplus.oms.service.IOmsPaymentsService;
import com.zscat.mallplus.oms.vo.OrderParam;
import com.zscat.mallplus.oms.vo.PayParam;
import com.zscat.mallplus.sms.entity.SmsRechargeRecord;
import com.zscat.mallplus.sms.mapper.SmsGroupMapper;
import com.zscat.mallplus.sms.mapper.SmsRechargeRecordMapper;
import com.zscat.mallplus.sms.vo.SmsRechargeRecordVo;
import com.zscat.mallplus.sys.entity.SysUserStaff;
import com.zscat.mallplus.sys.mapper.SysUserStaffMapper;
import com.zscat.mallplus.ums.entity.SysAppletSet;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.ums.service.ISysAppletSetService;
import com.zscat.mallplus.ums.service.IUmsMemberBlanceLogService;
import com.zscat.mallplus.ums.service.IUmsMemberService;
import com.zscat.mallplus.ums.service.RedisService;
import com.zscat.mallplus.util.CharUtil;
import com.zscat.mallplus.util.DateUtils;
import com.zscat.mallplus.util.MapUtils;
import com.zscat.mallplus.util.XmlUtil;
import com.zscat.mallplus.util.applet.StringConstantUtil;
import com.zscat.mallplus.util.applet.WechatRefundApiResult;
import com.zscat.mallplus.util.applet.WechatUtil;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.water.entity.WtWaterCard;
import com.zscat.mallplus.water.mapper.WtWaterCardMapper;
import com.zscat.mallplus.wxminiapp.entity.AccountWxapp;
import com.zscat.mallplus.wxminiapp.mapper.AccountWxappMapper;
import com.zscat.mallplus.wxpay.WxPayApi;
import com.zscat.mallplus.wxpay.WxPayApiConfig;
import com.zscat.mallplus.wxpay.model.CloseOrderModel;
import com.zscat.mallplus.wxpay.model.OrderQueryModel;
import com.zscat.mallplus.wxpay.model.UnifiedOrderModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * 作者: @author mallplus <br>
 * 时间: 2017-08-11 08:32<br>
 * 描述: ApiIndexController <br>
 */
@Api(tags = "商户支付")
@Slf4j
@RestController
@RequestMapping("/api/pay")
public class PayController extends ApiBaseAction {
    private static Logger LOGGER = LoggerFactory.getLogger(PayController.class);
    String tradeType = "JSAPI";
    String uniformorder = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    String orderquery = "https://api.mch.weixin.qq.com/pay/orderquery";
    String refundUrl = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    String refundqueryUrl = "https://api.mch.weixin.qq.com/pay/refundquery";
    @Autowired
    private IOmsPaymentsService paymentsService;
    @Resource
    private ISysAppletSetService appletSetService;
    @Resource
    private MerchatFacilitatorConfigMapper configMapper;
    @Resource
    private SmsRechargeRecordMapper recordMapper;
    @Resource
    private IOmsOrderService orderService;
    @Resource
    private IUmsMemberService memberService;
    @Resource
    private AccountWxappMapper wxappMapper;
    @Resource
    private RedisService redisService;
    @Resource
    private WtWaterCardMapper waterCardMapper;
    @Resource
    private SysUserStaffMapper staffMapper;
    @Resource
    private IUmsMemberBlanceLogService blanceLogService;
    @Resource
    private IOmsOrderItemService orderItemService;
    @Resource
    private SmsGroupMapper groupMapper;
    @Autowired
    private IOmsOrderOperateHistoryService orderOperateHistoryService;
    private String notifyUrl = "http://java.chengguo.link:8081/api";
    private String refundNotifyUrl;

    /**
     * 订单退款请求
     */
    /*
    @SysLog(MODULE = "pay", REMARK = "订单退款请求")
    @ApiOperation(value = "订单退款请求")
    @PostMapping("refund")
    public Object refund(Integer orderId) {
        //
        OrderVo orderInfo = orderService.queryObject(orderId);

        if (null == orderInfo) {
            return toResponsObject(400, "订单已取消", "");
        }

        if (orderInfo.getOrder_status() == 401 || orderInfo.getOrder_status() == 402) {
            return toResponsObject(400, "订单已退款", "");
        }

        WechatRefundApiResult result = WechatUtil.wxRefund(orderInfo.getId().toString(),
                10.01, 10.01);
        if (result.getResult_code().equals("SUCCESS")) {
            if (orderInfo.getOrder_status() == 201) {
                orderInfo.setOrder_status(401);
            } else if (orderInfo.getOrder_status() == 300) {
                orderInfo.setOrder_status(402);
            }
            orderService.updateAll(orderInfo);
            return toResponsObject(400, "成功退款", "");
        } else {
            return toResponsObject(400, "退款失败", "");
        }
    }*/


    //返回微信服务
    public static String setXml(String return_code, String return_msg) {
        return "<xml><return_code><![CDATA[" + return_code + "]]></return_code><return_msg><![CDATA[" + return_msg + "]]></return_msg></xml>";
    }

    //模拟微信回调接口
    public static String callbakcXml(String orderNum) {
        return "<xml><appid><![CDATA[wx2421b1c4370ec43b]]></appid><attach><![CDATA[支付测试]]></attach><bank_type><![CDATA[CFT]]></bank_type><fee_type><![CDATA[CNY]]></fee_type> <is_subscribe><![CDATA[Y]]></is_subscribe><mch_id><![CDATA[10000100]]></mch_id><nonce_str><![CDATA[5d2b6c2a8db53831f7eda20af46e531c]]></nonce_str><openid><![CDATA[oUpF8uMEb4qRXf22hE3X68TekukE]]></openid> <out_trade_no><![CDATA[" + orderNum + "]]></out_trade_no>  <result_code><![CDATA[SUCCESS]]></result_code> <return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[B552ED6B279343CB493C5DD0D78AB241]]></sign><sub_mch_id><![CDATA[10000100]]></sub_mch_id> <time_end><![CDATA[20140903131540]]></time_end><total_fee>1</total_fee><trade_type><![CDATA[JSAPI]]></trade_type><transaction_id><![CDATA[1004400740201409030005092168]]></transaction_id></xml>";
    }

    @IgnoreAuth
    @ApiOperation("显示所有支付方式")
    @RequestMapping(value = "/paymentlist", method = RequestMethod.GET)
    public Object getPayments() {
        List<OmsPayments> paymentss = paymentsService.list(new QueryWrapper<OmsPayments>().eq("status", 1));
        return new CommonResult().success(paymentss);
    }

    /**
     * 余额支付
     */
    @SysLog(MODULE = "pay", REMARK = "余额支付")
    @ApiOperation(value = "余额支付")
    @PostMapping("allPay")
    public Object allPay(PayParam payParam) {
        try {
            Object order = orderService.blancePay(orderService.getById(payParam.getOrderId()));
            return new CommonResult().success(order);
        } catch (Exception e) {
            return new CommonResult().failed(e.getMessage());
        }

    }

    /**
     * 余额支付
     */
    @SysLog(MODULE = "pay", REMARK = "余额支付")
    @ApiOperation(value = "余额支付")
    @PostMapping("balancePay")
    public Object balancePay(PayParam payParam) {

        try {
            Object order = orderService.blancePay(payParam);
            return new CommonResult().success(order);
        } catch (Exception e) {
            return new CommonResult().failed(e.getMessage());
        }
    }

    /**
     * 积分兑换
     */
    @SysLog(MODULE = "pay", REMARK = "积分兑换")
    @ApiOperation(value = "积分兑换")
    @PostMapping("jifenPay")
    public Object jifenPay(OrderParam payParam) {
        try {
            return orderService.jifenPay(payParam);
        } catch (Exception e) {
            return new CommonResult().failed(e.getMessage());
        }

    }

    /**
     * 获取支付的请求参数
     */
    @SysLog(MODULE = "pay", REMARK = "获取支付的请求参数")
    @ApiOperation(value = "获取支付的请求参数")
    @PostMapping("weixinAppletPay")
    public Object payPrepay(@RequestParam(value = "orderId", required = false, defaultValue = "0") Long orderId,
                            @RequestParam(value = "appIdsource", required = false) Integer appIdsource) {
        UmsMember user = memberService.getNewCurrentMember();
        //
        OmsOrder orderInfo = orderService.getById(orderId);
        SysAppletSet appletSet = memberService.getSysAppletSet(appIdsource);
        if (null == appletSet) {
            throw new ApiMallPlusException("没有设置支付配置");
        }

        if (null == orderInfo) {
            return toResponsObject(400, "订单已取消", "");
        }

        if (orderInfo.getStatus() == OrderStatus.CLOSED.getValue()) {
            return toResponsObject(400, "订单已已关闭，请不要重复操作", "");
        }
        if (orderInfo.getStatus() != OrderStatus.INIT.getValue()) {
            return toResponsObject(400, "订单不是未支付，不能重新支付", "");
        }

        String nonceStr = CharUtil.getRandomString(32);

        //https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=7_7&index=3
        Map<Object, Object> resultObj = new TreeMap();

        try {
            Map<Object, Object> parame = new TreeMap<Object, Object>();
            parame.put("appid", appletSet.getAppid());
            // 商家账号。
            parame.put("mch_id", appletSet.getMchid());
            String randomStr = CharUtil.getRandomNum(18).toUpperCase();
            // 随机字符串
            parame.put("nonce_str", randomStr);
            // 商户订单编号
            parame.put("out_trade_no", orderInfo.getOrderSn());

            // 商品描述
            parame.put("body", "超市-支付");

            //支付金额
            parame.put("total_fee", orderInfo.getPayAmount().multiply(new BigDecimal(100)).intValue());
            // 回调地址
            parame.put("notify_url", appletSet.getNotifyurl());
            // 交易类型APP
            parame.put("trade_type", tradeType);
            parame.put("spbill_create_ip", getClientIp());
            parame.put("openid", user.getWeixinOpenid());
            String sign = WechatUtil.arraySign(parame, appletSet.getPaySignKey());
            // 数字签证
            parame.put("sign", sign);

            String xml = MapUtils.convertMap2Xml(parame);
            log.info("xml:" + xml);
            Map<String, Object> resultUn = XmlUtil.xmlStrToMap(WechatUtil.requestOnce(uniformorder, xml));
            // 响应报文
            String return_code = MapUtils.getString("return_code", resultUn);
            String return_msg = MapUtils.getString("return_msg", resultUn);
            //
            if (return_code.equalsIgnoreCase("FAIL")) {
                return new CommonResult().failed("支付失败," + return_msg);
            } else if (return_code.equalsIgnoreCase("SUCCESS")) {
                // 返回数据
                String result_code = MapUtils.getString("result_code", resultUn);
                String err_code_des = MapUtils.getString("err_code_des", resultUn);
                if (result_code.equalsIgnoreCase("FAIL")) {
                    return new CommonResult().failed("支付失败," + err_code_des);
                } else if (result_code.equalsIgnoreCase("SUCCESS")) {
                    String prepay_id = MapUtils.getString("prepay_id", resultUn);
                    // 先生成paySign 参考https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=7_7&index=5
                    resultObj.put("appId", appletSet.getAppid());
                    resultObj.put("timeStamp", DateUtils.timeToStr(System.currentTimeMillis() / 1000, DateUtils.DATE_TIME_PATTERN));
                    resultObj.put("nonceStr", nonceStr);
                    resultObj.put("package", "prepay_id=" + prepay_id);
                    resultObj.put("signType", "MD5");
                    String paySign = WechatUtil.arraySign(resultObj, appletSet.getPaySignKey());
                    resultObj.put("paySign", paySign);
                    // 业务处理
                    orderInfo.setPrepayId(prepay_id);
                    // 付款中
                    orderInfo.setStatus(OrderStatus.PayNotNotice.getValue());
                    orderInfo.setModifyTime(new Date());
                    orderService.updateById(orderInfo);

                    if (orderInfo.getPid() == null) {
                        OmsOrder neworder = new OmsOrder();
                        neworder.setStatus(OrderStatus.TO_DELIVER.getValue());
                        neworder.setPayType(AllEnum.OrderPayType.weixinAppletPay.code());
                        neworder.setPaymentTime(new Date());
                        neworder.setModifyTime(new Date());
                        orderService.update(neworder, new QueryWrapper<OmsOrder>().eq("pid", orderInfo.getId()));
                    }
                    OmsOrderOperateHistory history = new OmsOrderOperateHistory();
                    history.setOrderId(orderInfo.getId());
                    history.setCreateTime(new Date());
                    history.setOperateMan("shop");
                    history.setPreStatus(OrderStatus.INIT.getValue());
                    history.setOrderStatus(OrderStatus.TO_DELIVER.getValue());
                    history.setNote("小程序支付");
                    orderOperateHistoryService.save(history);

                    memberService.addIntegration(user.getId(), orderInfo.getPayAmount().multiply(new BigDecimal("0.1")).intValue(), 1, "小程序支付添加积分", AllEnum.ChangeSource.order.code(), user.getUsername(),orderInfo.getPayAmount());

                    return toResponsObject(200, "微信统一订单下单成功", resultObj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed("支付失败,error=" + e.getMessage());
        }
        return new CommonResult().failed("支付失败");
    }

    /**
     * 微信查询订单状态
     */
    @SysLog(MODULE = "pay", REMARK = "查询订单状态")
    @ApiOperation(value = "查询订单状态")
    @GetMapping("query")
    public Object orderQuery(@RequestParam(value = "id", required = false, defaultValue = "0") Long id,
                             @RequestParam(value = "appIdsource", required = false) Integer appIdsource) {
        UmsMember user = memberService.getNewCurrentMember();
        //
        SysAppletSet appletSet = memberService.getSysAppletSet(appIdsource);
        if (null == appletSet) {
            throw new ApiMallPlusException("没有设置支付配置");
        }
        OmsOrder orderDetail = orderService.getById(id);
        if (id == null) {
            throw new ApiMallPlusException("订单不存在");
        }
        Map<Object, Object> parame = new TreeMap<Object, Object>();
        parame.put("appid", appletSet.getAppid());
        // 商家账号。
        parame.put("mch_id", appletSet.getMchid());
        String randomStr = CharUtil.getRandomNum(18).toUpperCase();
        // 随机字符串
        parame.put("nonce_str", randomStr);
        // 商户订单编号
        parame.put("out_trade_no", orderDetail.getOrderSn());

        String sign = WechatUtil.arraySign(parame, appletSet.getPaySignKey());
        // 数字签证
        parame.put("sign", sign);

        String xml = MapUtils.convertMap2Xml(parame);
        log.info("xml:" + xml);
        Map<String, Object> resultUn = null;
        try {
            resultUn = XmlUtil.xmlStrToMap(WechatUtil.requestOnce(orderquery, xml));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiMallPlusException("查询失败,error=" + e.getMessage());
        }
        // 响应报文
        String return_code = MapUtils.getString("return_code", resultUn);
        String return_msg = MapUtils.getString("return_msg", resultUn);

        if (!"SUCCESS".equals(return_code)) {
            throw new ApiMallPlusException("查询失败,error=" + return_msg);
        }

        String trade_state = MapUtils.getString("trade_state", resultUn);
        if ("SUCCESS".equals(trade_state)) {
            // 更改订单状态
            // 业务处理
            OmsOrder orderInfo = new OmsOrder();
            orderInfo.setId(id);
            orderInfo.setStatus(2);
            orderInfo.setConfirmStatus(1);
            orderInfo.setPaymentTime(new Date());
            orderInfo.setModifyTime(new Date());
            orderService.updateById(orderInfo);
            return new CommonResult().success("支付成功");
        } else if ("USERPAYING".equals(trade_state)) {
            // 重新查询 正在支付中
           /* Integer num = (Integer) J2CacheUtils.get(J2CacheUtils.SHOP_CACHE_NAME, "queryRepeatNum" + id + "");
            if (num == null) {
                J2CacheUtils.put(J2CacheUtils.SHOP_CACHE_NAME, "queryRepeatNum" + id + "", 1);
                this.orderQuery(id);
            } else if (num <= 3) {
                J2CacheUtils.remove(J2CacheUtils.SHOP_CACHE_NAME, "queryRepeatNum" + id);
                this.orderQuery(id);
            } else {
                throw new ApiMallPlusException("查询失败,error=" + trade_state);
            }*/

        } else {
            // 失败
            throw new ApiMallPlusException("查询失败,error=" + trade_state);
        }

        throw new ApiMallPlusException("查询失败，未知错误");
    }

    /**
     * 微信订单回调接口
     *
     * @return
     */
    @SysLog(MODULE = "pay", REMARK = "微信订单回调接口")
    @ApiOperation(value = "微信订单回调接口")
    @RequestMapping(value = "/notify", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public void notify(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            InputStream in = request.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.close();
            in.close();
            //xml数据
            String reponseXml = new String(out.toByteArray(), "utf-8");

            WechatRefundApiResult result = (WechatRefundApiResult) XmlUtil.xmlStrToBean(reponseXml, WechatRefundApiResult.class);
            String result_code = result.getResult_code();
            //订单编号
            String out_trade_no = result.getOut_trade_no();
            log.error("订单" + out_trade_no + "支付成功");
            // 业务处理
            OmsOrder param = new OmsOrder();
            param.setOrderSn(out_trade_no);
            List<OmsOrder> list = orderService.list(new QueryWrapper<>(param));
            OmsOrder orderInfo = list.get(0);
            orderInfo.setStatus(2);
            orderInfo.setPaymentTime(new Date());
            if (result_code.equalsIgnoreCase("FAIL")) {

                log.error("订单" + out_trade_no + "支付失败");
                orderService.releaseStock(orderInfo);
                response.getWriter().write(setXml("SUCCESS", "OK"));
            } else if (result_code.equalsIgnoreCase("SUCCESS")) {
                orderInfo.setModifyTime(new Date());
                orderService.updateById(orderInfo);
                response.getWriter().write(setXml("SUCCESS", "OK"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    @SysLog(MODULE = "pay", REMARK = "代客充值/用户自己充值/后台充值（可能用不到)")
    @ApiOperation(value = "代客充值/用户自己充值/后台充值（可能用不到)")
    @PostMapping("rechargeWaterCard")
    public Object rechargeWaterCard(@RequestBody SmsRechargeRecordVo entity){
        //校验单笔信息，是不是可以代客充值
        if (entity.getRechargeType()==1){
            SysUserStaff userStaff = staffMapper.selectById(entity.getReplaceId());
            if (userStaff.getPayMax().compareTo(entity.getPayFee())<0){
                return new CommonResult().failed("支付金额不能超过员工单笔充值最大额度");
            }
        }
        //添加记录信息
        String outTradeNo = WxPayKit.generateStr();
        SmsRechargeRecord record = new SmsRechargeRecord();
        BeanUtils.copyProperties(entity,record);
        record.setOutTradeNo(outTradeNo);
        AccountWxapp accountWxapp = new AccountWxapp();
        accountWxapp.setUniacid(entity.getUid());
        AccountWxapp wxapp = wxappMapper.selectOne(new QueryWrapper<>(accountWxapp));
        record.setDealerId(wxapp.getCreateBy());
        //小程序支付统一下单-服务商支付
        //1.获取支付的金额
        if (record.getPayFee()==null){
            return new CommonResult().failed("订单金额不能为空！");
        }
        BigDecimal price = record.getPayFee().multiply(new BigDecimal("100")).setScale(BigDecimal.ROUND_DOWN,0);
        //2.获取IP
        String ip = IpKit.getRealIp(request);
        if (StrKit.isBlank(ip)) {
            ip = "127.0.0.1";
        }
        //3. 获取服务商和底下的特约商户的信息（平台)
        WxPayApiConfig config = this.getApiConfig(entity.getUid());
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
                .body("水卡充值-对水卡进行充值")
                .out_trade_no(outTradeNo)
                .total_fee(price.toString())
                .spbill_create_ip(ip)
                .notify_url(notifyUrl)
                .trade_type(TradeType.JSAPI.getTradeType())
                .openid(entity.getOpenId())
                .build()
                .createSign(config.getPartnerKey(), SignType.HMACSHA256);
        //5.统一下单
        String xmlResult = WxPayApi.pushOrder(false, params);
        log.info("统一下单:" + xmlResult);
        //6.将返回信息的信息转成map
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
        //7.获取return_code
        String returnCode = result.get("return_code");
        String returnMsg = result.get("return_msg");
        System.out.println(returnMsg);
        //8.如果失败则判断状态存储起来
        if (!WxPayKit.codeIsOk(returnCode)) {
            //处理自己的逻辑
            //状态 1预支付失败 2预支付成功 3支付成功-未配送 4支付失败 5已配送 6交易关闭 7交易关闭失败
            record.setStatus(StringConstantUtil.rechargeStatus_1);
            recordMapper.insert(record);
            return new CommonResult().failed("error:" + returnMsg);
        }
        //9.return_code返回的是成功，则判断result_code
        String resultCode = result.get("result_code");
        //如果失败则判断状态存储起来
        if (!WxPayKit.codeIsOk(resultCode)) {
            //处理自己的逻辑
            record.setStatus(StringConstantUtil.rechargeStatus_1);
            recordMapper.insert(record);
            return new CommonResult().failed("error:" + result.get("err_code_des"));
        }
        record.setStatus(StringConstantUtil.rechargeStatus_2);
        recordMapper.insert(record);
        return new CommonResult().success();
    }

    public WxPayApiConfig getApiConfig(Integer uid) {
        WxPayApiConfig apiConfig = null;
        //服务商的支付信息
        MerchatFacilitatorConfig config = configMapper.selectById(1);
        if (config==null){
            return apiConfig;
        }
        //拿到经销商的数据,商户号和APPID等信息
        AccountWxapp accountWxapp = new AccountWxapp();
        accountWxapp.setUniacid(uid);
        AccountWxapp wxapp = wxappMapper.selectOne(new QueryWrapper<>(accountWxapp));
        SysAppletSet set = new SysAppletSet();
        set.setStoreId(wxapp.getStoreId());
        set.setUserId(wxapp.getCreateBy());
        SysAppletSet appletSet = appletSetService.getOne(new QueryWrapper<>(set));
        apiConfig = WxPayApiConfig.builder()
                .appId(config.getAppid())
                .mchId(config.getMchId())
                .slAppId(wxapp.getKey())
                .slMchId(appletSet.getMchid())
                .partnerKey(config.getSecret())
//                .certPath(wxPayBean.getCertPath())
                .domain("https://api.mch.weixin.qq.com")
                .build();
        notifyUrl = apiConfig.getDomain().concat("/api/pay/payNotify");
        refundNotifyUrl = apiConfig.getDomain().concat("/api/pay/refundNotify");
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
        String transaction_id = params.get("transaction_id");
        SmsRechargeRecord rechargeRecord = new SmsRechargeRecord();
        rechargeRecord.setOutTradeNo(out_trade_no);
        SmsRechargeRecord record = recordMapper.selectOne(new QueryWrapper<>(rechargeRecord));
        AccountWxapp accountWxapp = new AccountWxapp();
        accountWxapp.setUniacid(record.getUniacid());
        AccountWxapp wxapp = wxappMapper.selectOne(new QueryWrapper<>(accountWxapp));
        // 注意重复通知的情况，同一订单号可能收到多次通知，请注意一定先判断订单状态
        // 4.注意此处签名方式需与统一下单的签名类型一致
        if (WxPayKit.verifyNotify(params, this.getApiConfig(wxapp.getUniacid()).getPartnerKey(), SignType.HMACSHA256)) {
            if (WxPayKit.codeIsOk(returnCode)) {
                // 5.更新订单信息
                if (record.getStatus()==StringConstantUtil.rechargeStatus_2){
                    record.setStatus(StringConstantUtil.rechargeStatus_3);
                    recordMapper.updateById(record);
                    //这里把实际到账钱数放进用户的会员卡信息里面
                    //先看看是不是实体卡,现在实体卡和虚拟卡一张表
                    WtWaterCard wtWaterCard = new WtWaterCard();
                    wtWaterCard.setCardNo(record.getCardNo());
                    WtWaterCard waterCard = waterCardMapper.selectOne(new QueryWrapper<>(wtWaterCard));
                    waterCard.setCardMoney(waterCard.getCardMoney().add(record.getPayFee()).setScale(BigDecimal.ROUND_DOWN,2));
                    waterCardMapper.updateById(waterCard);
                    //添加积分只有用户自己充值才送积分
                    if (record.getRechargeType()==2){
                        //计算积分
                        memberService.addIntegration(record.getMemberId(), record.getUniacid(), 0, "充值套餐添加积分", AllEnum.ChangeSource.order.code(), record.getMemberName(),record.getActualAccount());
                    }else if (record.getRechargeType()==1){
                        SysUserStaff userStaff = staffMapper.selectById(record.getReplaceId());
                        if (userStaff.getBalance()==null){
                            userStaff.setBalance(new BigDecimal("0"));
                        }
                        userStaff.setBalance(userStaff.getBalance().add(record.getReward()).setScale(BigDecimal.ROUND_DOWN,2));
                        staffMapper.updateById(userStaff);
                    }

                    //--- 另外还有分账，用户购买之后需要分账给经销商
                    String ip = IpKit.getRealIp(request);
                    if (StrKit.isBlank(ip)) {
                        ip = "127.0.0.1";
                    }
                    appletSetService.getRout(record.getDealerId(),record.getActualAccount(),ip,transaction_id,record.getId(),2);
                }
                // 发送通知等
                Map<String, String> xml = new HashMap<String, String>(2);
                xml.put("return_code", "SUCCESS");
                xml.put("return_msg", "OK");
                return WxPayKit.toXml(xml);
            } else {
                record.setStatus(StringConstantUtil.rechargeStatus_4);
                recordMapper.updateById(record);
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

    /**
     * 微信查询订单状态
     */
    @SysLog(MODULE = "pay", REMARK = "查询订单状态")
    @ApiOperation(value = "查询订单状态")
    @GetMapping("orderQuery")
    public Object orderQuery(@RequestParam String out_trade_no,@RequestParam Integer uid) throws Exception {
        SmsRechargeRecord rechargeRecord = new SmsRechargeRecord();
        rechargeRecord.setOutTradeNo(out_trade_no);
        SmsRechargeRecord order = recordMapper.selectOne(new QueryWrapper<>(rechargeRecord));
        if (order==null){
            return new CommonResult().failed("订单不存在");
        }
        WxPayApiConfig config = this.getApiConfig(uid);
        if (config==null){
            return new CommonResult().failed("没有设置支付配置");
        }
        Map<String, String> params = OrderQueryModel
                .builder()
                .appid(config.getAppId())
                .mch_id(config.getMchId())
                .sub_appid(config.getSlAppId())
                .sub_mch_id(config.getSlMchId())
                .out_trade_no(order.getOutTradeNo())
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
            if (!order.getStatus().equals(StringConstantUtil.rechargeStatus_3)){
                order.setBuyTime(new Date());
                order.setStatus(StringConstantUtil.rechargeStatus_3);
                recordMapper.updateById(order);
                //6.支付成功开始给会员卡加钱

            }
            return new CommonResult().success("支付成功");
        } else if ("USERPAYING".equals(trade_state)) {
            // 重新查询 正在支付中
            Long num = redisService.increment(StringConstantUtil.SHOP_CACHE_NAME + out_trade_no,1);
            if (num <= 3) {
                num = redisService.increment(StringConstantUtil.SHOP_CACHE_NAME + out_trade_no,1);
                this.orderQuery(out_trade_no,uid);
            } else {
                return new ApiMallPlusException("查询失败,error=" + trade_state);
            }

        } else {
            //如果订单查询是未支付状态，数据库里面却是支付成功，那么需要去掉授权，去掉购买明细
            if (order.getStatus().equals(StringConstantUtil.rechargeStatus_3)){
                //6.支付成功开始给会员卡加钱-这里退回来
            }
            return new ApiMallPlusException("用户支付状态:" + trade_state);
        }

        return new ApiMallPlusException("查询失败，未知错误");
    }

    @ApiOperation("关闭订单")
    @RequestMapping(value = "/orderCancel", method = RequestMethod.POST)
    public Object closeOrder(@RequestBody SmsRechargeRecord order) {
        if (order.getStatus()==StringConstantUtil.rechargeStatus_6){
            return new CommonResult().failed(400,"订单已关闭，请勿重复操作！");
        }
        if (order.getStatus()==StringConstantUtil.rechargeStatus_3){
            return new CommonResult().failed(400,"订单已支付，不允许关闭！");
        }
        //设置账号信息
        AccountWxapp accountWxapp = new AccountWxapp();
        accountWxapp.setStoreId(order.getStoreId());
        accountWxapp.setCreateBy(order.getDealerId());
        AccountWxapp wxapp = wxappMapper.selectOne(new QueryWrapper<>(accountWxapp));
        WxPayApiConfig config = this.getApiConfig(wxapp.getUniacid());
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
                .out_trade_no(order.getOutTradeNo())
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
            order.setStatus(StringConstantUtil.rechargeStatus_7);
            recordMapper.updateById(order);
            return new CommonResult().failed("error:" + returnMsg);
        }
        //9.return_code返回的是成功，则判断result_code
        String resultCode = result.get("result_code");
        //如果失败则判断状态存储起来
        if (!WxPayKit.codeIsOk(resultCode)) {
            order.setStatus(StringConstantUtil.rechargeStatus_7);
            recordMapper.updateById(order);
            return new CommonResult().failed("error:" + returnMsg);
        }
        order.setStatus(StringConstantUtil.rechargeStatus_6);
        recordMapper.updateById(order);
        return new CommonResult().success("交易已关闭");
    }
}
