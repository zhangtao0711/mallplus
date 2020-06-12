package com.zscat.mallplus.ums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jfinal.kit.StrKit;
import com.zscat.mallplus.common.CommonConstant;
import com.zscat.mallplus.core.enums.SignType;
import com.zscat.mallplus.core.kit.IpKit;
import com.zscat.mallplus.core.kit.WxPayKit;
import com.zscat.mallplus.merchant.entity.MerchatFacilitatorConfig;
import com.zscat.mallplus.merchant.mapper.MerchatFacilitatorConfigMapper;
import com.zscat.mallplus.pay.utils.StringUtils;
import com.zscat.mallplus.sms.entity.SmsRechargeRecord;
import com.zscat.mallplus.sms.entity.SmsWaterBuyRecord;
import com.zscat.mallplus.sms.mapper.SmsRechargeRecordMapper;
import com.zscat.mallplus.sms.mapper.SmsWaterBuyRecordMapper;
import com.zscat.mallplus.sys.entity.SysUser;
import com.zscat.mallplus.sys.mapper.SysUserMapper;
import com.zscat.mallplus.ums.entity.SysApaySet;
import com.zscat.mallplus.ums.entity.SysAppletSet;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.ums.mapper.SysApaySetMapper;
import com.zscat.mallplus.ums.mapper.SysAppletSetMapper;
import com.zscat.mallplus.ums.service.ISysAppletSetService;
import com.zscat.mallplus.util.applet.StringConstantUtil;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.water.entity.WtWaterCard;
import com.zscat.mallplus.water.mapper.WtWaterCardMapper;
import com.zscat.mallplus.wxpay.WxPayApi;
import com.zscat.mallplus.wxpay.model.ProfitSharingModel;
import com.zscat.mallplus.wxpay.model.TransferModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-06-15
 */
@Service
public class SysAppletSetServiceImpl extends ServiceImpl<SysAppletSetMapper, SysAppletSet> implements ISysAppletSetService {

    @Resource
    private SysAppletSetMapper appletSetMapper;
    @Resource
    private MerchatFacilitatorConfigMapper configMapper;
    @Resource
    private SysUserMapper adminMapper;
    @Resource
    private SmsWaterBuyRecordMapper recordMapper;
    @Resource
    private SmsRechargeRecordMapper record1Mapper;
    @Resource
    private SysApaySetMapper apaySetMapper;
    @Resource
    private WtWaterCardMapper cardMapper;
    @Override
    public boolean getRout(Long dealerId, BigDecimal actualFee,String ip,String transaction_id, Long recordId,Integer type) {
        //type的类型，如果是1则是买水的分账记录，如果是2则是充值的分账记录
        SmsWaterBuyRecord record = new SmsWaterBuyRecord();
        SmsRechargeRecord record1 = new SmsRechargeRecord();
        if (type ==1) {
            record= recordMapper.selectById(recordId);
            record.setTransactionId(transaction_id);
        }else if (type==2){
            record1=record1Mapper.selectById(recordId);
            record1.setTransactionId(transaction_id);
        }else {
            return false;
        }
        //获取经销商设置的信息，确定是分账还是企业付款到零钱
        SysAppletSet set = appletSetMapper.selectById(dealerId);
        if (set.getSelfType()==1){
            //走分账
            //1.获取服务商的支付信息
            MerchatFacilitatorConfig config = configMapper.selectById(1);
            if (config==null){
                return false;
            }
            //2.获取支付信息receivers
            SysUser user = adminMapper.selectById(dealerId);
            SysUser last = adminMapper.selectById(user.getPid());
            SysUser lastLast = adminMapper.selectById(user.getGid());
            List<Map<String,String>> receivers = new ArrayList<>();
            Map<String,String> receiver1 = new HashMap<>();
            if (user.getLevel()==1){
                return false;
            }else if (user.getLevel()==2){
                receiver1.put("type","PERSONAL_WECHATID");
                receiver1.put("account",last.getWeixinOpenid());
                BigDecimal amount = set.getFirstSeparate().multiply(actualFee).setScale(2,BigDecimal.ROUND_DOWN);
                BigDecimal secondAmount = (new BigDecimal("1").subtract(set.getFirstSeparate()).multiply(actualFee).setScale(2,BigDecimal.ROUND_DOWN));
                receiver1.put("amount",amount.multiply(new BigDecimal("100")).setScale(0,BigDecimal.ROUND_DOWN).toString());
                receivers.add(receiver1);
                if (type==1) {
                    record.setFirstAmount(amount);
                    record.setSecondAmount(secondAmount);
                }else if (type==2){
                    record1.setFirstAmount(amount);
                    record1.setSecondAmount(secondAmount);
                }
            } else if (user.getLevel()==3){
                receiver1.put("type","PERSONAL_WECHATID");
                receiver1.put("account",last.getWeixinOpenid());
                BigDecimal secondAmount = set.getSecondSeparate().multiply(actualFee).setScale(2,BigDecimal.ROUND_DOWN);
                receiver1.put("amount",secondAmount.multiply(new BigDecimal("100")).setScale(0,BigDecimal.ROUND_DOWN).toString());
                receivers.add(receiver1);
                Map<String,String> receiver2 = new HashMap<>();
                receiver2.put("type","PERSONAL_WECHATID");
                receiver2.put("account",lastLast.getWeixinOpenid());
                BigDecimal amount = set.getFirstSeparate().multiply(actualFee).setScale(2,BigDecimal.ROUND_DOWN);
                BigDecimal thirdAmount = (new BigDecimal("1").subtract(set.getFirstSeparate()).subtract(set.getSecondSeparate())).multiply(actualFee).setScale(2,BigDecimal.ROUND_DOWN);
                receiver2.put("amount",amount.multiply(new BigDecimal("100")).setScale(0,BigDecimal.ROUND_DOWN).toString());
                receivers.add(receiver2);
                if (type==1) {
                    record.setFirstAmount(amount);
                    record.setSecondAmount(secondAmount);
                    record.setThirdAmount(thirdAmount);
                }else if (type==2){
                    record1.setFirstAmount(amount);
                    record1.setSecondAmount(secondAmount);
                    record1.setThirdAmount(thirdAmount);
                }
            }
            String out_trade_no = WxPayKit.generateStr();
            if (type==1){
                record.setRoutOutOrderNo(out_trade_no);
            }else if (type==2){
                record1.setOutTradeNo(out_trade_no);
            }
            Map<String, String> param = ProfitSharingModel.builder()
                    .appid(config.getAppid())
                    .mch_id(config.getMchId())
                    .sub_mch_id(set.getMchid())
                    .nonce_str(WxPayKit.generateStr())
                    .sign_type("HMAC-SHA256")
                    .transaction_id(transaction_id)
                    .out_order_no(out_trade_no)
                    .receivers(receivers.toString())
                    .build()
                    .createSign(config.getSecret(),SignType.HMACSHA256, false);
            String rout = WxPayApi.profitSharing(param, config.getCertCatalog(),config.getMchId());
            Map<String, String> map = WxPayKit.xmlToMap(rout);
            String returncode = map.get("return_code");
            String resultCode = map.get("result_code");
            if (WxPayKit.codeIsOk(returncode) && WxPayKit.codeIsOk(resultCode)) {
                // 分账成功
                if (type==1) {
                    record.setOrderId(map.get("order_id"));
                    record.setRoutStatus(StringConstantUtil.rout_success);
                    recordMapper.updateById(record);
                }else if (type==2){
                    record1.setOrderId(map.get("order_id"));
                    record1.setRoutStatus(StringConstantUtil.rout_success);
                    record1Mapper.updateById(record1);
                }
                System.out.println("订单分账成功！");
                return true;
            } else {
                // 分账失败
                if (type==1) {
                    record.setRoutStatus(StringConstantUtil.rout_failed);
                    recordMapper.updateById(record);
                }else if (type==2){
                    record1.setRoutStatus(StringConstantUtil.rout_failed);
                    record1Mapper.updateById(record1);
                }
                System.out.println("分账失败：错误代码" +map.get("err_code")+ "错误代码描述"+map.get("err_code_des"));
                return false;
            }
        }else if (set.getSelfType()==2) {
            //走企业付款到零钱，卡设置
            //这里是测试企业付款到零钱，卡设置，等设置写了上级数据
            SysApaySet apaySet = apaySetMapper.selectById(set.getParentUserId());
            String partner_trade_no = WxPayKit.generateStr();
            if (type==1){
                record.setRoutOutOrderNo(partner_trade_no);
                record.setFirstAmount(actualFee);
            }else if (type==2){
                record1.setOutTradeNo(partner_trade_no);
                record1.setFirstAmount(actualFee);
            }
            //这里需要判断代收账号的等级，若等级是总经销商则检索的是平台的，且没有比例全部分给总经销商
            if (set.getLevelId()==1){
                if (type==1){
                    record.setFirstAmount(actualFee);
                }else if (type==2){
                    record1.setFirstAmount(actualFee);
                }
                //4.拼接需要的数据
                Map<String, String> params = TransferModel
                        .builder()
                        .mch_appid(apaySet.getMchAppid())
                        .mchid(apaySet.getMchId())
                        .nonce_str(WxPayKit.generateStr())
                        .partner_trade_no(partner_trade_no)
                        .openid(set.getReceiptAccount())
                        .check_name("NO_CHECK")
                        .amount(actualFee.multiply(new BigDecimal("100")).setScale(0,BigDecimal.ROUND_DOWN).toString())
                        .desc("微信支付-企业付款到零钱")
                        .spbill_create_ip(ip)
                        .build()
                        .createSign(set.getPaySignKey(), SignType.HMACSHA256, false);
                // 提现
                String transfers = WxPayApi.transfers(params, apaySet.getCertCatalog(), apaySet.getMchId());
                System.out.println("提现结果:" + transfers);
                Map<String, String> map = WxPayKit.xmlToMap(transfers);
                String returnCode = map.get("return_code");
                String resultCode = map.get("result_code");
                if (WxPayKit.codeIsOk(returnCode) && WxPayKit.codeIsOk(resultCode)) {
                    // 提现成功
                    if (type==1){
                        record.setWithdrawStatus(StringConstantUtil.rout_success.toString()+",");
                        record.setOrderId(map.get("payment_no"));
                        recordMapper.updateById(record);
                    }else if (type==2){
                        record1.setWithdrawStatus(StringConstantUtil.rout_success.toString()+",");
                        record1.setOrderId(map.get("payment_no"));
                        record1Mapper.updateById(record1);
                    }
                    return true;
                } else {
                    // 提现失败
                    if (type==1){
                        record.setWithdrawStatus(StringConstantUtil.rout_failed.toString()+",");
                        recordMapper.updateById(record);
                    }else if (type==2){
                        record1.setWithdrawStatus(StringConstantUtil.rout_failed.toString()+",");
                        record1Mapper.updateById(record1);
                    }
                    return false;
                }
            }else if (set.getLevelId()==2){
                //等级是经销商，则有两种选择，选择代收账号是平台，平台不抽取抽成，直接按照比例分两次，分给总经销商和经销商
                BigDecimal amountLast = set.getFirstSeparate().multiply(actualFee).setScale(2,BigDecimal.ROUND_DOWN);
                if (type==1){
                    record.setFirstAmount(amountLast);
                }else if (type==2){
                    record1.setFirstAmount(amountLast);
                }
                BigDecimal amount = (new BigDecimal("1").subtract(set.getFirstSeparate())).multiply(actualFee).setScale(2,BigDecimal.ROUND_DOWN);
                if (type==1){
                    record.setSecondAmount(amount);
                }else if (type==2){
                    record1.setSecondAmount(amount);
                }
                //代收账号的微信设置
                //4.拼接需要的数据
                Map<String, String> params = TransferModel
                        .builder()
                        .mch_appid(apaySet.getMchAppid())
                        .mchid(apaySet.getMchId())
                        .nonce_str(WxPayKit.generateStr())
                        .partner_trade_no(partner_trade_no)
                        .openid(set.getReceiptAccount())
                        .check_name("NO_CHECK")
                        .amount(amount.multiply(new BigDecimal("100")).setScale(0,BigDecimal.ROUND_DOWN).toString())
                        .desc("微信支付-企业付款到零钱")
                        .spbill_create_ip(ip)
                        .build()
                        .createSign(set.getPaySignKey(), SignType.HMACSHA256, false);
                // 提现
                String transfers = WxPayApi.transfers(params, apaySet.getCertCatalog(), apaySet.getMchId());
                System.out.println("提现结果:" + transfers);
                Map<String, String> map = WxPayKit.xmlToMap(transfers);
                String returnCode = map.get("return_code");
                String resultCode = map.get("result_code");
                if (WxPayKit.codeIsOk(returnCode) && WxPayKit.codeIsOk(resultCode)) {
                    // 提现成功
                    if (type==1){
                        record.setWithdrawStatus(StringConstantUtil.rout_success.toString()+",");
                        record.setOrderId(map.get("payment_no"));
                        recordMapper.updateById(record);
                    }else if (type==2){
                        record1.setWithdrawStatus(StringConstantUtil.rout_success.toString()+",");
                        record1.setOrderId(map.get("payment_no"));
                        record1Mapper.updateById(record1);
                    }
                    return true;
                } else {
                    // 提现失败
                    if (type==1){
                        record.setWithdrawStatus(StringConstantUtil.rout_failed.toString()+",");
                        recordMapper.updateById(record);
                    }else if (type==2){
                        record1.setWithdrawStatus(StringConstantUtil.rout_failed.toString()+",");
                        record1Mapper.updateById(record1);
                    }
                }
                if (apaySet.getUserId()==1) {
                    //平台账号除了分给设置的账号之外，这个设置的账号是经销商的，还需要分给总经销商
                    //4.拼接需要的数据
                    Map<String, String> param = TransferModel
                            .builder()
                            .mch_appid(apaySet.getMchAppid())
                            .mchid(apaySet.getMchId())
                            .nonce_str(WxPayKit.generateStr())
                            .partner_trade_no(partner_trade_no)
                            .openid(set.getReceiptAccount())
                            .check_name("NO_CHECK")
                            .amount(amountLast.multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_DOWN).toString())
                            .desc("微信支付-企业付款到零钱")
                            .spbill_create_ip(ip)
                            .build()
                            .createSign(set.getPaySignKey(), SignType.HMACSHA256, false);
                    // 提现
                    String transfer = WxPayApi.transfers(param, apaySet.getCertCatalog(), apaySet.getMchId());
                    System.out.println("提现结果:" + transfer);
                    Map<String, String> map1 = WxPayKit.xmlToMap(transfer);
                    String returnCode1 = map1.get("return_code");
                    String resultCode1 = map1.get("result_code");
                    if (WxPayKit.codeIsOk(returnCode1) && WxPayKit.codeIsOk(resultCode1)) {
                        // 提现成功
                        if (type == 1) {
                            record.setWithdrawStatus(record.getWithdrawStatus()+StringConstantUtil.rout_success.toString() + ",");
                            record.setOrderId(map1.get("payment_no"));
                            recordMapper.updateById(record);
                        } else if (type == 2) {
                            record1.setWithdrawStatus(record.getWithdrawStatus()+StringConstantUtil.rout_success.toString() + ",");
                            record1.setOrderId(map1.get("payment_no"));
                            record1Mapper.updateById(record1);
                        }
                        return true;
                    } else {
                        // 提现失败
                        if (type==1){
                            record.setWithdrawStatus(record.getWithdrawStatus()+StringConstantUtil.rout_failed.toString()+",");
                            recordMapper.updateById(record);
                        }else if (type==2){
                            record1.setWithdrawStatus(record.getWithdrawStatus()+StringConstantUtil.rout_failed.toString()+",");
                            record1Mapper.updateById(record1);
                        }
                    }
                }
            }else if (set.getLevelId()==3){
               //本身等级是分销商，则有三种选择,选择代收账号是平台，平台不抽取抽成，直接按照比例分三次，分给总经销商和经销商和分销商
                BigDecimal amountLastParent = set.getFirstSeparate().multiply(actualFee).setScale(2,BigDecimal.ROUND_DOWN);
                BigDecimal amountLast = set.getSecondSeparate().multiply(actualFee).setScale(2,BigDecimal.ROUND_DOWN);
                BigDecimal amount = (new BigDecimal("1").subtract(set.getFirstSeparate()).subtract(set.getThirdSeparate())).multiply(actualFee).setScale(2,BigDecimal.ROUND_DOWN);
                if (type==1){
                    record.setFirstAmount(amountLastParent);
                    record.setSecondAmount(amountLast);
                    record.setThirdAmount(amount);
                }else if (type==2){
                    record1.setFirstAmount(amountLastParent);
                    record1.setSecondAmount(amountLast);
                    record1.setThirdAmount(amount);
                }
                //先给分销商分钱
                //4.拼接需要的数据
                Map<String, String> param = TransferModel
                        .builder()
                        .mch_appid(apaySet.getMchAppid())
                        .mchid(apaySet.getMchId())
                        .nonce_str(WxPayKit.generateStr())
                        .partner_trade_no(partner_trade_no)
                        .openid(set.getReceiptAccount())
                        .check_name("NO_CHECK")
                        .amount(amount.multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_DOWN).toString())
                        .desc("微信支付-企业付款到零钱")
                        .spbill_create_ip(ip)
                        .build()
                        .createSign(set.getPaySignKey(), SignType.HMACSHA256, false);
                // 提现
                String transfer = WxPayApi.transfers(param, apaySet.getCertCatalog(), apaySet.getMchId());
                System.out.println("提现结果:" + transfer);
                Map<String, String> map1 = WxPayKit.xmlToMap(transfer);
                String returnCode1 = map1.get("return_code");
                String resultCode1 = map1.get("result_code");
                if (WxPayKit.codeIsOk(returnCode1) && WxPayKit.codeIsOk(resultCode1)) {
                    // 提现成功
                    if (type == 1) {
                        record.setWithdrawStatus(StringConstantUtil.rout_success.toString() + ",");
                        record.setOrderId(map1.get("payment_no"));
                        recordMapper.updateById(record);
                    } else if (type == 2) {
                        record1.setWithdrawStatus(StringConstantUtil.rout_success.toString() + ",");
                        record1.setOrderId(map1.get("payment_no"));
                        record1Mapper.updateById(record1);
                    }
                    return true;
                } else {
                    // 提现失败
                    if (type==1){
                        record.setWithdrawStatus(record.getWithdrawStatus()+StringConstantUtil.rout_failed.toString()+",");
                        recordMapper.updateById(record);
                    }else if (type==2){
                        record1.setWithdrawStatus(record.getWithdrawStatus()+StringConstantUtil.rout_failed.toString()+",");
                        record1Mapper.updateById(record1);
                    }
                }
                SysUser user = adminMapper.selectById(set.getParentUserId());
                //这里选择的代收账号是不是平台或者经销商
                if (set.getParentUserId()==1||user.getLevel()==2){
                    //给总经销商
                    //4.拼接需要的数据
                    Map<String, String> param1 = TransferModel
                            .builder()
                            .mch_appid(apaySet.getMchAppid())
                            .mchid(apaySet.getMchId())
                            .nonce_str(WxPayKit.generateStr())
                            .partner_trade_no(partner_trade_no)
                            .openid(set.getReceiptAccount())
                            .check_name("NO_CHECK")
                            .amount(amountLastParent.multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_DOWN).toString())
                            .desc("微信支付-企业付款到零钱")
                            .spbill_create_ip(ip)
                            .build()
                            .createSign(set.getPaySignKey(), SignType.HMACSHA256, false);
                    // 提现
                    String transfer1 = WxPayApi.transfers(param1, apaySet.getCertCatalog(), apaySet.getMchId());
                    System.out.println("提现结果:" + transfer1);
                    Map<String, String> map2 = WxPayKit.xmlToMap(transfer1);
                    String returnCode2 = map2.get("return_code");
                    String resultCode2 = map2.get("result_code");
                    if (WxPayKit.codeIsOk(returnCode2) && WxPayKit.codeIsOk(resultCode2)) {
                        // 提现成功
                        if (type == 1) {
                            record.setWithdrawStatus(record.getWithdrawStatus()+StringConstantUtil.rout_success.toString() + ",");
                            record.setOrderId(map2.get("payment_no"));
                            recordMapper.updateById(record);
                        } else if (type == 2) {
                            record1.setWithdrawStatus(record.getWithdrawStatus()+StringConstantUtil.rout_success.toString() + ",");
                            record1.setOrderId(map2.get("payment_no"));
                            record1Mapper.updateById(record1);
                        }
                        return true;
                    } else {
                        // 提现失败
                        if (type==1){
                            record.setWithdrawStatus(record.getWithdrawStatus()+StringConstantUtil.rout_failed.toString()+",");
                            recordMapper.updateById(record);
                        }else if (type==2){
                            record1.setWithdrawStatus(record.getWithdrawStatus()+StringConstantUtil.rout_failed.toString()+",");
                            record1Mapper.updateById(record1);
                        }
                    }
                }else if (set.getParentUserId()==1||user.getLevel()==1){
                    //给经销商
                    //4.拼接需要的数据
                    Map<String, String> param2 = TransferModel
                            .builder()
                            .mch_appid(apaySet.getMchAppid())
                            .mchid(apaySet.getMchId())
                            .nonce_str(WxPayKit.generateStr())
                            .partner_trade_no(partner_trade_no)
                            .openid(set.getReceiptAccount())
                            .check_name("NO_CHECK")
                            .amount(amountLast.multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_DOWN).toString())
                            .desc("微信支付-企业付款到零钱")
                            .spbill_create_ip(ip)
                            .build()
                            .createSign(set.getPaySignKey(), SignType.HMACSHA256, false);
                    // 提现
                    String transfer2 = WxPayApi.transfers(param2, apaySet.getCertCatalog(), apaySet.getMchId());
                    System.out.println("提现结果:" + transfer2);
                    Map<String, String> map3 = WxPayKit.xmlToMap(transfer2);
                    String returnCode3 = map3.get("return_code");
                    String resultCode3 = map3.get("result_code");
                    if (WxPayKit.codeIsOk(returnCode3) && WxPayKit.codeIsOk(resultCode3)) {
                        // 提现成功
                        if (type == 1) {
                            record.setWithdrawStatus(record.getWithdrawStatus()+StringConstantUtil.rout_success.toString() + ",");
                            record.setOrderId(map3.get("payment_no"));
                            recordMapper.updateById(record);
                        } else if (type == 2) {
                            record1.setWithdrawStatus(record.getWithdrawStatus()+StringConstantUtil.rout_success.toString() + ",");
                            record1.setOrderId(map3.get("payment_no"));
                            record1Mapper.updateById(record1);
                        }
                        return true;
                    } else {
                        // 提现失败
                        if (type==1){
                            record.setWithdrawStatus(record.getWithdrawStatus()+StringConstantUtil.rout_failed.toString()+",");
                            recordMapper.updateById(record);
                        }else if (type==2){
                            record1.setWithdrawStatus(record.getWithdrawStatus()+StringConstantUtil.rout_failed.toString()+",");
                            record1Mapper.updateById(record1);
                        }
                    }
                }

            }
        }
        return false;
    }

    @Override
    public void donateVirtualCard(UmsMember member, Long dealerId) {
        WtWaterCard wtWaterCard = new WtWaterCard();
        wtWaterCard.setCardType(StringConstantUtil.card_type_1);
        wtWaterCard.setDealerId(dealerId);
        wtWaterCard.setUmsMemberId(member.getId());
        List<WtWaterCard> waterCards = cardMapper.selectList(new QueryWrapper<>(wtWaterCard));
        if (waterCards==null||waterCards.size()==0) {
            WtWaterCard waterCard = new WtWaterCard();
            waterCard.setCardType(StringConstantUtil.card_type_1);
            waterCard.setCardNo("");//生成规则不清楚
            waterCard.setAcid(member.getUniacid());
            waterCard.setUmsMemberId(member.getId());
            waterCard.setDealerId(dealerId);
            waterCard.setState("0");
            waterCard.setCardMoney(new BigDecimal("0"));
            waterCard.setRemarks("买水的时候没卡直接制卡送卡");
            waterCard.setCreateTime(new Date());
            waterCard.setStoreId(member.getStoreId());
            waterCard.setDelFlag("0");
            cardMapper.insert(waterCard);
        }
    }
}
