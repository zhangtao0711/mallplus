package com.zscat.mallplus.util;

import org.bouncycastle.pqc.crypto.newhope.NHOtherInfoGenerator;

import java.util.HashMap;
import java.util.Map;

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
    //微信公众号前缀
    public static final String weiXin_Subscription="http://weixin.qq.com/r/";
    //制卡状态
    public static final String card_create_state_0="0";//0未制卡
    public static final String card_create_state_1="1";//1已制卡
    //卡类型（0实体卡1虚拟卡）
    public static final String card_type="0";
    public static final String card_type_virtual="1";


    //水卡状态（0正常1复制卡2挂失卡3删除卡4非经销商卡）
    public static final String water_code_state_0="0";//正常
    public static final String water_code_state_1="1";//复制卡
    public static final String water_code_state_2="2";//挂失卡
    public static final String water_code_state_3="3";//删除卡
    public static final String water_code_state_4="4";//非经销商卡

    //水卡状态销售状态（0未售出1已售出）
    public static final String water_code_sale_state_0="0";//未售出
    public static final String water_code_sale_state_1="1";//已售出

    //充值金额类型
    public static final String recharge_money_type_0="0";//充值金额
    public static final String recharge_money_type_1="1";//充值体验金
    //充值类型
    public static final String recharge_type_0="0";//单卡
    public static final String recharge_type_1="1";//批量
    public static final String recharge_type_2="2";//筛选充值
    //筛选记号
    public static final Map<String,String> where_mark= new HashMap<>();
//    public static String water_code_sale_state_1 = "1";

    {
        where_mark.put("0",">");
        where_mark.put("1","<");
        where_mark.put("2","=");
        where_mark.put("3",">=");
        where_mark.put("4","<=");
    }
    //筛选周期
    public static final String where_period_0="0";//天
    public static final String where_period_1="1";//周
    public static final String where_period_2="2";//月
    public static final String where_period_3="3";//季
    public static final String where_period_4="4";//年
    //体验到期类型
    public static final String experience_end_type_0="0";//到期日
    public static final String experience_end_type_1="1";//有效天数

    //营销功能 客户标签id
    public static final String ums_label_perssion_id="";//功能id
    //营销功能 充值套餐id
    public static final String recharge_package_id="";//功能id

    //通用的失败成功
    public static final Integer fail = 0;
    public static final Integer succeeded = 1;
    //帐号启用状态:0->禁用；1->启用
    public static final Integer ums_status_off=0;
    public static final Integer ums_status_on=1;
    //水卡最多绑定几个用户
    public static final Integer ums_card_num=5;
    //滤芯计费类型
    public static final String billing_mode_time="0";
    public static final String billing_mode_num="1";
    //滤芯状态
    public static final String filter_element_state_0="0";
    public static final String filter_element_state_1="1";
    public static final String filter_element_state_2="2";
    //会员区分（0:日志1:备注)
    public static final String member_log_falg_0="0";
    public static final String member_log_falg_1="1";

    //短信
    public static final String forget_password_type  = "1";
    public static final String forget_password_template_code = "SMS_193016479";
    public static final String phone_update_type = "2";
    public static final String phone_update_template_code = "SMS_193016478";
    public static final String sign_name = "四两科技";

    //0备注1滤芯履历
    public static final String remarks_type_0 = "0";
    public static final String remarks_type_1 = "1";
    //小程序访问地址
    public static final String portal_url = "http://localhost:8083/";
    //水卡状态
    public static final String card_type_0 = "0";//实体卡
    public static final String card_type_1 = "1";//虚拟卡
}
