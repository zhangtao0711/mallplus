package com.zscat.mallplus.util.applet;

public class StringConstantUtil {

    //用户或者代客微信充值的状态 1预支付失败 2预支付成功 3支付成功-未配送 4支付失败 5已配送 6交易关闭 7交易关闭失败
    public static final Integer rechargeStatus_1 = 1;
    public static final Integer rechargeStatus_2 = 2;
    public static final Integer rechargeStatus_3 = 3;
    public static final Integer rechargeStatus_4 = 4;
    public static final Integer rechargeStatus_5 = 5;
    public static final Integer rechargeStatus_6 = 6;
    public static final Integer rechargeStatus_7 = 7;
    public static final String SHOP_CACHE_NAME = "queryRepeatNum";

    //积分状态 0->冻结 1->正常
    public static final Integer integrationStatus_0 = 0;
    public static final Integer integrationStatus_1 = 0;

    //通用了 1分账成功，0分账失败
    public static final Integer rout_success = 1;
    public static final Integer rout_failed = 0;
}
