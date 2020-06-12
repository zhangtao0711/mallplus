package com.zscat.mallplus.util.applet;

public class StringConstantUtil {

    //平台用户小程序-积分商城，使用手机号和经销商用户小程序实现数据共通
    public static final Integer applet_platform_user = 1;//平台公众号
    public static final Integer applet_platform = 3;//平台小程序
    //经销商小程序-设备管理
    public static final Integer applet_dealer = 2;
    //经销商用户小程序-卖水 和公众号用unionid打通数据共通 若没有配置这个则使用手机号
//    public static final String applet_dealer_user = "3";

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

    public static final String card_type_0 = "0";//实体卡
    public static final String card_type_1 = "1";//虚拟卡
}
