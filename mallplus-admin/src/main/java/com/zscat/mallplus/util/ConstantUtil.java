package com.zscat.mallplus.util;

public class ConstantUtil {

    //平台用户小程序-积分商城，使用手机号和经销商用户小程序实现数据共通
    public static final String applet_platform_user = "1";
    //经销商小程序-设备管理
    public static final String applet_dealer = "2";
    //经销商用户小程序-卖水 和公众号用unionid打通数据共通 若没有配置这个则使用手机号
    public static final String applet_dealer_user = "3";

    public static final String delFlag = "1";   // 删除状态(1-正常,0-已删除)
    public static final String deleteLogic = "0";// 删除状态(1-正常,0-已删除)

    //适用于所有的0否1是
    public static final String is = "1";
    public static final String not = "0";

    //经销商支付方式
    public static final String wx_pay = "0";
    public static final String ye_pay = "1";
    public static final String jf_pay = "2";

    //购买营销功能的微信支付的状态,状态有这几种，1预支付成功，2预支付失败，3支付成功，4支付失败，5交易关闭,6交易关闭失败
    public static final Integer payStatus_1 = 1;
    public static final Integer payStatus_2 = 2;
    public static final Integer payStatus_3 = 3;
    public static final Integer payStatus_4 = 4;
    public static final Integer payStatus_5 = 5;
    public static final Integer payStatus_6 = 6;
    public static final String SHOP_CACHE_NAME = "queryRepeatNum";

    //制卡最大值
    public static final Long max_card_no = 4294967295L;


}
