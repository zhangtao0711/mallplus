package com.zscat.mallplus.single;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jfinal.kit.StrKit;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.core.enums.SignType;
import com.zscat.mallplus.core.enums.TradeType;
import com.zscat.mallplus.core.kit.HttpKit;
import com.zscat.mallplus.core.kit.IpKit;
import com.zscat.mallplus.core.kit.WxPayKit;
import com.zscat.mallplus.enums.AllEnum;
import com.zscat.mallplus.exception.ApiMallPlusException;
import com.zscat.mallplus.merchant.entity.MerchatFacilitatorConfig;
import com.zscat.mallplus.merchant.mapper.MerchatFacilitatorConfigMapper;
import com.zscat.mallplus.sms.entity.SmsClassConfig;
import com.zscat.mallplus.sms.entity.SmsRechargePackage;
import com.zscat.mallplus.sms.entity.SmsWaterBuyRecord;
import com.zscat.mallplus.sms.entity.SmsWaterPage;
import com.zscat.mallplus.sms.mapper.SmsClassConfigMapper;
import com.zscat.mallplus.sms.mapper.SmsRechargePackageMapper;
import com.zscat.mallplus.sms.mapper.SmsWaterBuyRecordMapper;
import com.zscat.mallplus.sms.mapper.SmsWaterPageMapper;
import com.zscat.mallplus.sms.entity.*;
import com.zscat.mallplus.sms.mapper.*;
import com.zscat.mallplus.sms.service.ISmsLabelMemberService;
import com.zscat.mallplus.sms.service.IWtEquipmentService;
import com.zscat.mallplus.ums.entity.SysAppletSet;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.ums.service.ISysAppletSetService;
import com.zscat.mallplus.ums.service.IUmsMemberService;
import com.zscat.mallplus.ums.service.RedisService;
import com.zscat.mallplus.util.CharUtil;
import com.zscat.mallplus.util.applet.StringConstantUtil;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.water.entity.*;
import com.zscat.mallplus.water.entity.WtConsumeRecord;
import com.zscat.mallplus.water.entity.WtWaterCard;
import com.zscat.mallplus.water.entity.WtWaterCardVituralConsume;
import com.zscat.mallplus.water.mapper.WtConsumeRecordMapper;
import com.zscat.mallplus.water.mapper.WtWaterCardMapper;
import com.zscat.mallplus.water.mapper.WtWaterCardVituralConsumeMapper;
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
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

@Api(tags = "购水页面显示问题")
@Slf4j
@RestController
@RequestMapping("/api/water")
public class SingleWaterPageController extends ApiBaseAction {

    @Resource
    private SmsClassConfigMapper smsClassConfigMapper;
    @Resource
    private SmsWaterPageMapper smsWaterPageMapper;
    @Resource
    private SmsRechargePackageMapper smsRechargePackageMapper;
    @Resource
    private MerchatFacilitatorConfigMapper configMapper;
    @Resource
    private AccountWxappMapper wxappMapper;
    @Resource
    private ISysAppletSetService appletSetService;
    @Resource
    private SmsWaterBuyRecordMapper recordMapper;
    @Resource
    private RedisService redisService;
    @Resource
    private IUmsMemberService memberService;
    @Resource
    private WtWaterCardMapper waterCardMapper;
    @Resource
    private WtWaterCardVituralConsumeMapper consumeMapper;
    @Resource
    private WtConsumeRecordMapper consumeRecordMapper;
    @Resource
    private ISmsLabelMemberService labelMemberService;

    @Resource
    private IWtEquipmentService iWtEquipmentService;
    @Resource
    private WtWaterCardMapper wtWaterCardMapper;

    private String notifyUrl = "http://siliang.zlkjhb.cn/api";
    private String refundNotifyUrl;

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
    /*********************************lyn*****************************************/
    @SysLog(MODULE = "single", REMARK = "小程序取水")
    @ApiOperation("小程序取水售水扫码后再次查询")
    @RequestMapping(value = "/getWaterCardByEq", method = RequestMethod.GET)
    @ResponseBody
    public Object getWaterCardByEq(WtEquipmentForPortal entity) {
        try {
            if(ValidatorUtils.empty(entity.getEqcode()) || ValidatorUtils.empty(entity.getDealerId())
                    || ValidatorUtils.empty(entity.getGetType())|| ValidatorUtils.empty(entity.getUmsMemberId())){
                log.error("小程序取水售水扫码后再次查询参数不全",null,null);
                return new CommonResult().paramFailed("数据获取失败！");
            }
            entity.setDelFlag(StringConstantUtil.delFlag);
            entity.setState(StringConstantUtil.water_code_state_0);//水卡正常
            entity.setStateEq(StringConstantUtil.state_eq_3);//设备正常
            entity.setCardType(StringConstantUtil.card_type_virtual);//虚拟卡不需要和设备关联
            //取水
            if(entity.getGetType().equals(StringConstantUtil.not)){
                return new CommonResult().success(iWtEquipmentService.getWaterCardByEq(entity));
            }else{//售水
                //通过deviceNo找到数据
                SmsClassConfig config = smsClassConfigMapper.selectByDeviceId(String.valueOf(entity.getDealerId()));
                JSONObject j = new JSONObject();
                List<String> waters = Arrays.asList(config.getWaterIds());
                List<SmsWaterPage> waterPages = new ArrayList<>();
                for (String water:waters){
                    SmsWaterPage waterPage = smsWaterPageMapper.selectById(water);
                    waterPages.add(waterPage);
                }
                j.put("waterList",waterPages);
                //获取套餐
                SmsRechargePackage rechargePackage = new SmsRechargePackage();
                rechargePackage.setDealerId(config.getDealerId());
                rechargePackage.setStoreId(config.getStoreId());
                List<SmsRechargePackage> rechargePackages = smsRechargePackageMapper.selectList(new QueryWrapper<>(rechargePackage));
                j.put("rechargePackages",rechargePackages);
                return new CommonResult().success(j);
            }

        } catch (Exception e) {
            log.error("小程序取水售水扫码后再次查询失败：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }
    @SysLog(MODULE = "single", REMARK = "取水参数获取")
    @ApiOperation("取水参数获取")
    @PostMapping(value = "/getWaterList")
    public Object getWaterList(@RequestParam String deviceId) {
        if(ValidatorUtils.empty(deviceId)){
            log.error("取水参数获取参数不全",null,null);
            return new CommonResult().paramFailed("数据获取失败！");
        }
        //通过deviceNo找到数据
        SmsClassConfig config = smsClassConfigMapper.selectByDeviceId(deviceId);
        JSONObject j = new JSONObject();
        List<String> waters = Arrays.asList(config.getWaterIds());
        List<SmsWaterPage> waterPages = new ArrayList<>();
        for (String water:waters){
            SmsWaterPage waterPage = smsWaterPageMapper.selectById(water);
            waterPages.add(waterPage);
        }
        j.put("waterList",waterPages);
        return new CommonResult().success(j);
    }

    @SysLog(MODULE = "single", REMARK = "开始取水")
    @ApiOperation("开始取水")
    @PostMapping(value = "/getWater")
    @ResponseBody
    public Object getWater(WtConsumeRecord consume) throws WxErrorException{
        if(ValidatorUtils.empty(consume.getEqcode()) || ValidatorUtils.empty(consume.getDealerId())
                || ValidatorUtils.empty(consume.getConsumeMoney())|| ValidatorUtils.empty(consume.getCreateBy())){
            log.error("开始取水参数不全",null,null);
            return new CommonResult().paramFailed("取水失败！");
        }
        if(!CharUtil.isInteger(String.valueOf(consume.getConsumeMoney()))){
            return new CommonResult().paramFailed("消费金额错误！");
        }
        //判断设备是否空闲
        if(iWtEquipmentService.checkEquipmentState(consume.getEqcode(),StringConstantUtil.is)){
            return new CommonResult().paramFailed("设备忙线中，请稍后再试！");
        }
        WtConsumeRecord wtConsumeRecord = new WtConsumeRecord();
        wtConsumeRecord.setId(getRecordId());//订单号生成
        //设备号
        wtConsumeRecord.setWaterType("00");//冷水00
        wtConsumeRecord.setConsumeType(StringConstantUtil.consumeType_1);//消费方式 线上
        //水卡号
        //消费金额单位转分
        wtConsumeRecord.setOrderState("1");//订单状态
        wtConsumeRecord.setCreateTime(new Date());//创建日期
        wtConsumeRecord.setDelFlag(StringConstantUtil.delFlag);

        //保存订单信息
        consumeRecordMapper.insert(consume);
        //有卡号时更新水卡金额
        if(!ValidatorUtils.empty(consume.getCardNo())){
            WtWaterCard wtWaterCard = new WtWaterCard();
            wtWaterCard.setCardNo(consume.getCardNo());
            WtWaterCard waterCard = waterCardMapper.selectOne(new QueryWrapper<>(wtWaterCard));
            waterCard.setUpdateBy(consume.getCreateBy());//更新人
            waterCard.setUpdateTime(consume.getCreateTime());//更新时间
            waterCard.setCardMoney(waterCard.getCardMoney().subtract(
                    new BigDecimal(consume.getConsumeMoney()/100)).setScale(2,BigDecimal.ROUND_DOWN));
            waterCardMapper.updateById(waterCard);
            //标签
            UmsMember member = memberService.getById(waterCard.getUmsMemberId());
            labelMemberService.addCardLabel(member.getUniacid(),consume.getStoreId(),waterCard.getCardMoney(),member.getId(),member.getWeixinOpenid());
        }
        //TODO 调用硬件出水
        return new CommonResult().success("取水中");
    }

    @SysLog(MODULE = "single", REMARK = "中断取水")
    @ApiOperation("中断取水")
    @PostMapping(value = "/stopWater")
    @ResponseBody
    public Object stopWater(WtConsumeRecord consume) throws WxErrorException{
        if(ValidatorUtils.empty(consume.getEqcode()) || ValidatorUtils.empty(consume.getDealerId())
                || ValidatorUtils.empty(consume.getConsumeMoney())|| ValidatorUtils.empty(consume.getCreateBy())){
            log.error("中断取水参数不全",null,null);
            return new CommonResult().paramFailed("中断取水失败！");
        }

        WtConsumeRecord wtConsumeRecord = consumeRecordMapper.selectById(consume.getId());
        wtConsumeRecord.setOrderState("2");//订单状态
        wtConsumeRecord.setEndTime(new Date());//结束日期
        //更新订单信息
        consumeRecordMapper.updateById(wtConsumeRecord);

        //TODO 调用硬件停止出水
        return new CommonResult().success("停止出水");
    }

    //生成售水机取水订单id
    private String getRecordId(){
        DateFormat formater = new SimpleDateFormat("yyyyMMddHHmmss");
        String date=formater.format(new Date());
        //获取最大订单号
        String maxNo = consumeRecordMapper.getMaxIdByDate(date);
        if(ValidatorUtils.empty(maxNo)){
            maxNo="1";
        }
        return date+CharUtil.padRight(maxNo,4,'0');
    }

    @SysLog(MODULE = "single", REMARK = "充值套餐获取")
    @ApiOperation("充值套餐获取")
    @PostMapping(value = "/getRechargePackages")
    public Object getRechargePackages(@RequestParam String deviceId) {
        if(ValidatorUtils.empty(deviceId)){
            log.error("充值套餐获取参数不全",null,null);
            return new CommonResult().paramFailed("数据获取失败！");
        }
        //通过deviceNo找到数据
        SmsClassConfig config = smsClassConfigMapper.selectByDeviceId(String.valueOf(deviceId));
        JSONObject j = new JSONObject();
        //获取套餐
        SmsRechargePackage rechargePackage = new SmsRechargePackage();
        rechargePackage.setDealerId(config.getDealerId());
        rechargePackage.setStoreId(config.getStoreId());
        List<SmsRechargePackage> rechargePackages = smsRechargePackageMapper.selectList(new QueryWrapper<>(rechargePackage));
        j.put("rechargePackages",rechargePackages);
        return new CommonResult().success(j);
    }

    @SysLog(MODULE = "single", REMARK = "套餐充值")
    @ApiOperation("套餐充值")
    @PostMapping(value = "/createSinglePackage")
    public Object saveSinglePackage(@RequestBody SmsWaterBuyRecord entity) {
        //添加记录信息
        String outTradeNo = WxPayKit.generateStr();
        //小程序支付统一下单-服务商支付
        //1.获取支付的金额
        if (entity.getPayFee()==null||entity.getActualFee()==null){
            return new CommonResult().failed("订单金额不能为空！");
        }
        BigDecimal price = entity.getActualFee().multiply(new BigDecimal("100")).setScale(BigDecimal.ROUND_DOWN,0);
        BigDecimal actual = entity.getActualFee().multiply(new BigDecimal("994")).setScale(BigDecimal.ROUND_DOWN,2);
        entity.setActualAccount(actual);
        //2.获取IP
        String ip = IpKit.getRealIp(request);
        if (StrKit.isBlank(ip)) {
            ip = "127.0.0.1";
        }
        //3. 获取服务商和底下的特约商户的信息（平台)
        WxPayApiConfig config = this.getApiConfig(entity.getUniacid());
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
                .body("套餐充值")
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
            entity.setStatus(StringConstantUtil.rechargeStatus_1);
            recordMapper.insert(entity);
            return new CommonResult().failed("error:" + returnMsg);
        }
        //9.return_code返回的是成功，则判断result_code
        String resultCode = result.get("result_code");
        //如果失败则判断状态存储起来
        if (!WxPayKit.codeIsOk(resultCode)) {
            //处理自己的逻辑
            entity.setStatus(StringConstantUtil.rechargeStatus_1);
            recordMapper.insert(entity);
            return new CommonResult().failed("error:" + result.get("err_code_des"));
        }
        entity.setStatus(StringConstantUtil.rechargeStatus_2);
        entity.setPrepayId(result.get("prepay_id"));
        recordMapper.insert(entity);
        return new CommonResult().success();
    }
    /**
     * 套餐充值异步通知
     */
    @RequestMapping(value = "/payNotifyPackage", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String payNotifyPackage(HttpServletRequest request) {
        //1.获取微信异步通知结果准换成map
        String xmlMsg = HttpKit.readData(request);
        Map<String, String> params = WxPayKit.xmlToMap(xmlMsg);
        log.info("微信支付通知=" + params);
        //2.获取返回的code
        String returnCode = params.get("return_code");
        //3.获取订单编号，根据订单编号获取唯一的订单
        String out_trade_no = params.get("out_trade_no");
        SmsWaterBuyRecord waterBuyRecord = new SmsWaterBuyRecord();
        waterBuyRecord.setOutTradeNo(out_trade_no);
        SmsWaterBuyRecord record = recordMapper.selectOne(new QueryWrapper<>(waterBuyRecord));
        AccountWxapp accountWxapp = new AccountWxapp();
        accountWxapp.setUniacid(record.getUniacid());
        AccountWxapp wxapp = wxappMapper.selectOne(new QueryWrapper<>(accountWxapp));
        // 注意重复通知的情况，同一订单号可能收到多次通知，请注意一定先判断订单状态
        // 4.注意此处签名方式需与统一下单的签名类型一致
        if (WxPayKit.verifyNotify(params, this.getApiConfig(wxapp.getUniacid()).getPartnerKey(), SignType.HMACSHA256)) {
            if (WxPayKit.codeIsOk(returnCode)) {
                // 5.更新订单信息
                if (record.getStatus().equals(StringConstantUtil.rechargeStatus_2)){
                    record.setStatus(StringConstantUtil.rechargeStatus_3);
                    recordMapper.updateById(record);
                    //更新卡内余额
                    wtWaterCardMapper.updateRechargeForUser(record,StringConstantUtil.water_code_state_0,StringConstantUtil.delFlag);
                    //用户买水分账或者企业付款到零钱
                    String ip = IpKit.getRealIp(request);
                    if (StrKit.isBlank(ip)) {
                        ip = "127.0.0.1";
                    }
                    String transaction_id = params.get("transaction_id");
                    appletSetService.getRout(record.getDealerId(),record.getActualAccount(),ip,transaction_id,record.getId(),1);
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
    /*********************************lyn*****************************************/
    /**
     * 扫码之后的购水页面
     */
    @SysLog(MODULE = "single", REMARK = "扫码之后的购水页面")
    @ApiOperation(value = "扫码之后的购水页面")
    @PostMapping("buyWaterPage")
    public Object buyWaterPage(@RequestParam String openid,@RequestParam String deviceId){
        //通过deviceNo找到数据
        SmsClassConfig config = smsClassConfigMapper.selectByDeviceId(deviceId);
        JSONObject j = new JSONObject();
        List<String> waters = Arrays.asList(config.getWaterIds());
        List<SmsWaterPage> waterPages = new ArrayList<>();
        for (String water:waters){
            SmsWaterPage waterPage = smsWaterPageMapper.selectById(water);
            waterPages.add(waterPage);
        }
        j.put("waterList",waterPages);
        //获取套餐
        SmsRechargePackage rechargePackage = new SmsRechargePackage();
        rechargePackage.setDealerId(config.getDealerId());
        rechargePackage.setStoreId(config.getStoreId());
        List<SmsRechargePackage> rechargePackages = smsRechargePackageMapper.selectList(new QueryWrapper<>(rechargePackage));
        j.put("rechargePackages",rechargePackages);
        return new CommonResult().success(j);
    }

    @SysLog(MODULE = "single", REMARK = "扫码之后的购水页面-绑定实体卡")
    @ApiOperation(value = "扫码之后的购水页面-绑定实体卡")
    @PostMapping("bindEntityCard")
    public Object bindEntityCard(@RequestParam String openid,@RequestParam Integer uniacid){
        //通过openid找到用户， 有卡则把水卡信息返给前台
        UmsMember umsMember = new UmsMember();
        umsMember.setWeixinOpenid(openid);
        UmsMember member = memberService.getOne(new QueryWrapper<>(umsMember));
        Long dealerId = wxappMapper.getDealerIdByUniacid(uniacid);
        if (member!=null){
            //实卡，水卡信息
            WtWaterCard wtWaterCard = new WtWaterCard();
            wtWaterCard.setCardType(StringConstantUtil.card_type_0);
            wtWaterCard.setDealerId(dealerId);
            wtWaterCard.setUmsMemberId(member.getId());
            List<WtWaterCard> waterCard = waterCardMapper.selectList(new QueryWrapper<>(wtWaterCard));
            if (waterCard!=null&&waterCard.size()!=0 ) {
                return new CommonResult().success(waterCard);
            }
        }
        return new CommonResult().failed();
    }

    /**
     * 买水
     */
    @SysLog(MODULE = "single", REMARK = "买水")
    @ApiOperation(value = "买水")
    @PostMapping("waterBuy")
    public Object waterBuy(@RequestBody SmsWaterBuyRecord entity){
        //添加记录信息
        String outTradeNo = WxPayKit.generateStr();
        //小程序支付统一下单-服务商支付
        //1.获取支付的金额
        if (entity.getPayFee()==null||entity.getActualFee()==null){
            return new CommonResult().failed("订单金额不能为空！");
        }
        BigDecimal price = entity.getActualFee().multiply(new BigDecimal("100")).setScale(BigDecimal.ROUND_DOWN,0);
        BigDecimal actual = entity.getActualFee().multiply(new BigDecimal("0.994")).setScale(BigDecimal.ROUND_DOWN,2);
        entity.setActualAccount(actual);
        //2.获取IP
        String ip = IpKit.getRealIp(request);
        if (StrKit.isBlank(ip)) {
            ip = "127.0.0.1";
        }
        //3. 获取服务商和底下的特约商户的信息（平台)
        WxPayApiConfig config = this.getApiConfig(entity.getUniacid());
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
                .body("扫码售水-买水")
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
            entity.setStatus(StringConstantUtil.rechargeStatus_1);
            recordMapper.insert(entity);
            return new CommonResult().failed("error:" + returnMsg);
        }
        //9.return_code返回的是成功，则判断result_code
        String resultCode = result.get("result_code");
        //如果失败则判断状态存储起来
        if (!WxPayKit.codeIsOk(resultCode)) {
            //处理自己的逻辑
            entity.setStatus(StringConstantUtil.rechargeStatus_1);
            recordMapper.insert(entity);
            return new CommonResult().failed("error:" + result.get("err_code_des"));
        }
        entity.setStatus(StringConstantUtil.rechargeStatus_2);
        entity.setPrepayId(result.get("prepay_id"));
        recordMapper.insert(entity);
        return new CommonResult().success();
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
        SmsWaterBuyRecord waterBuyRecord = new SmsWaterBuyRecord();
        waterBuyRecord.setOutTradeNo(out_trade_no);
        SmsWaterBuyRecord record = recordMapper.selectOne(new QueryWrapper<>(waterBuyRecord));
        AccountWxapp accountWxapp = new AccountWxapp();
        accountWxapp.setUniacid(record.getUniacid());
        AccountWxapp wxapp = wxappMapper.selectOne(new QueryWrapper<>(accountWxapp));
        // 注意重复通知的情况，同一订单号可能收到多次通知，请注意一定先判断订单状态
        // 4.注意此处签名方式需与统一下单的签名类型一致
        if (WxPayKit.verifyNotify(params, this.getApiConfig(wxapp.getUniacid()).getPartnerKey(), SignType.HMACSHA256)) {
            if (WxPayKit.codeIsOk(returnCode)) {
                // 5.更新订单信息
                if (record.getStatus().equals(StringConstantUtil.rechargeStatus_2)){
                    record.setStatus(StringConstantUtil.rechargeStatus_3);
                    recordMapper.updateById(record);
                    //获取用户，添加积分
                    UmsMember umsMember = new UmsMember();
                    umsMember.setUniacid(record.getUniacid());
                    umsMember.setWeixinOpenid(record.getOpenId());
                    UmsMember member = memberService.getOne(new QueryWrapper<>(umsMember));
                    //添加积分之前，先判定一下有没有卡，没卡送卡
                    appletSetService.donateVirtualCard(member,record.getDealerId());
                    memberService.addIntegration(member.getId(),member.getUniacid(),0,"扫码买水赠积分", AllEnum.ChangeSource.order.code(),member.getUsername(),record.getActualAccount());
                    //用户买水分账或者企业付款到零钱
                    String ip = IpKit.getRealIp(request);
                    if (StrKit.isBlank(ip)) {
                        ip = "127.0.0.1";
                    }
                    String transaction_id = params.get("transaction_id");
                    appletSetService.getRout(record.getDealerId(),record.getActualAccount(),ip,transaction_id,record.getId(),1);
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
    @SysLog(MODULE = "single", REMARK = "查询订单状态")
    @ApiOperation(value = "查询订单状态")
    @GetMapping("orderQuery")
    public Object orderQuery(@RequestParam String out_trade_no,@RequestParam Integer uid) throws Exception {
        SmsWaterBuyRecord waterBuyRecord = new SmsWaterBuyRecord();
        waterBuyRecord.setOutTradeNo(out_trade_no);
        SmsWaterBuyRecord order = recordMapper.selectOne(new QueryWrapper<>(waterBuyRecord));
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
                .out_trade_no(out_trade_no)
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
            if (!order.getStatus().equals(StringConstantUtil.rechargeStatus_3)){
                order.setBuyTime(new Date());
                order.setStatus(StringConstantUtil.rechargeStatus_3);
                recordMapper.updateById(order);
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
            if (order.getStatus().equals(StringConstantUtil.rechargeStatus_3)){
                //6.修改状态
                order.setStatus(StringConstantUtil.rechargeStatus_4);
                recordMapper.updateById(order);
            }
            return new ApiMallPlusException("用户支付状态:" + trade_state);
        }

        return new ApiMallPlusException("查询失败，未知错误");
    }

    @ApiOperation("关闭订单")
    @RequestMapping(value = "/orderCancel", method = RequestMethod.POST)
    public Object closeOrder(@RequestBody SmsWaterBuyRecord order) {
        if (order.getStatus().equals(StringConstantUtil.rechargeStatus_6)){
            return new CommonResult().failed(400,"订单已关闭，请勿重复操作！");
        }
        if (order.getStatus().equals(StringConstantUtil.rechargeStatus_3)){
            return new CommonResult().failed(400,"订单已支付，不允许关闭！");
        }
        //设置账号信息
        WxPayApiConfig config = this.getApiConfig(order.getUniacid());
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

    @SysLog(MODULE = "single", REMARK = "会员卡购水-虚卡")
    @ApiOperation(value = "会员卡购水-虚卡")
    @PostMapping("buyWaterByVirtualCard")
    public Object buyWaterByVirtualCard(WtWaterCardVituralConsume consume) throws WxErrorException {
        //虚拟卡
        WtWaterCard waterCard = waterCardMapper.selectById(consume.getVirtualId());
        waterCard.setCardMoney(waterCard.getCardMoney().subtract(consume.getConsumeFee()).setScale(2,BigDecimal.ROUND_DOWN));
        consume.setCardMoney(waterCard.getCardMoney());
        consume.setCreateTime(new Date());
        consumeMapper.insert(consume);
        waterCardMapper.updateById(waterCard);
        //标签
        UmsMember member = memberService.getById(consume.getMemberId());
        labelMemberService.addCardLabel(consume.getUniacid(),consume.getStoreId(),waterCard.getCardMoney(),consume.getMemberId(),member.getWeixinOpenid());
        return new CommonResult().success();
    }

    @SysLog(MODULE = "single", REMARK = "会员卡购水-实体卡")
    @ApiOperation(value = "会员卡购水-实体卡")
    @PostMapping("buyWaterByCard")
    public Object buyWaterByCard(WtConsumeRecord consume) throws WxErrorException {
        consume.setId("");//订单编号
        consume.setCreateTime(new Date());
        //TODO 李亚楠 判定一下卡号是否有效

        consumeRecordMapper.insert(consume);
        WtWaterCard wtWaterCard = new WtWaterCard();
        wtWaterCard.setCardNo(consume.getCardNo());
        WtWaterCard waterCard = waterCardMapper.selectOne(new QueryWrapper<>(wtWaterCard));
        waterCard.setCardMoney(waterCard.getCardMoney().subtract(new BigDecimal(consume.getConsumeMoney()*100)).setScale(2,BigDecimal.ROUND_DOWN));
        waterCardMapper.updateById(waterCard);
        //标签
        UmsMember member = memberService.getById(waterCard.getUmsMemberId());
        labelMemberService.addCardLabel(member.getUniacid(),consume.getStoreId(),waterCard.getCardMoney(),member.getId(),member.getWeixinOpenid());
        return new CommonResult().success();
    }
}
