package com.zscat.mallplus.common;


public class CommonConstant {




    // 外部来源和内部来源的cookie名
    public static final String OUTER_SOURCE_COOKIE = "RJFT_OUTER_SOURCE";
    public static final String INNER_SOURCE_COOKIE = "RJFT_INNER_SOURCE";

    public static final String OUTER_SOURCE_COOKIE_CLUE = "RJFT_OUTER_SOURCE_CLUE";
    // 来源cookie过期时间
    public static final int OUTER_SOURCE_COOKIE_TIMEOUT = 24 * 60 * 60;

    public static final String WEIXIN_QRPAY_COOKIE = "_wxpay";

    // 用户信息在session中的key
    public static final String USER_SESSION_KEY = "USER_SESSION";
    //订单确认信息在session中的key
    public static final String ORDER_SESSION_KEY = "ORDER_SESSION";

    public static final String FITTIME_CART_COOKIE = "FITTIME_CART_COOKIE";
    //心愿单cookie
    public static final String FITTIME_WISH_COOKIE = "FITTIME_WISH_COOKIE";

    //操作成功（用于controller返回code）
    public static final int OPERATE_SUCCESS = 200;
    //参数错误
    public static final int PARAM_ERROR_CODE = -1;
    //操作失败
    public static final int OPERATE_FAIL = -2;
    // 没有登录
    public static final int RET_NOT_LOGIN = -100;
    // 优惠券领取次已达到最大次数（包含当前这次）
    public static final int COUPON_OBTAIN_LAST_TIME = 201;

    public static final String AJAX_REQUEST_HEADER_KEY = "x-rjft-request";
    public static final String AJAX_REQUEST_HEADER_VALUE = "ajax";

    public static final String AJAX_REQUEST_TOKEN_KEY = "Authorization";

    //app原生页请求
    public static final String AJAX_NATIVE_HEADER_KEY = "x-rjft-native";
    public static final String AJAX_NATIVE_HEADER_VALUE = "native";

    public static final String CART_SOURCE_COOKIE = "cookie";
    public static final String CART_SOURCE_MODIFY = "modify";

    public static final Object NEED_ID_CARD_NO = "needIdCard";

    public static final int TITLE_LENGTH = 11;

    public static final int VOTE_BANNED = 300;

    public static final int VOTE_LIMITED = 301;

    public static final String CUSTOM_COURSE_SALT = "FITCAMP_CUSTOMCOURSE_";

    //商品类型. 1->配件商品 2->积分商品 3->普通商品；其中只有3经销商可以添加，其余的均是
    public static final Integer productType_1 = 1;
    public static final Integer productType_2 = 2;
    public static final Integer productType_3 = 3;

    //促销类型：0->没有促销使用原价;1->使用促销价；2->使用会员价；3->使用阶梯价格；4->使用满减价格；5->限时购; 6->积分抵扣
    public static final Integer promotion_type_0 = 0;
    public static final Integer promotion_type_1 = 1;
    public static final Integer promotion_type_2 = 2;
    public static final Integer promotion_type_3 = 3;
    public static final Integer promotion_type_4 = 4;
    public static final Integer promotion_type_5 = 5;
    public static final Integer promotion_type_6 = 6;

    //营销功能的id问题
    public static final Long register_integral = 593L;
    public static final Long integral_store = 595L;
    public static final Long group_purchase = 92L;
    public static final Long recharge_package = 596L;
    public static final Long recommend_card = 597L;
    public static final Long member_label = 598L;
    public static final Long store_decorate = 599L;

    //系统标签问题
    public static final Long label_member_card=0L;
    public static final Long label_use_water=0L;
    public static final Long label_recommend_time=0L;
    public static final Long label_member_community=0L;
    public static final Long label_member_level=0L;

    public static final Long tag_member_card=0L;
    public static final Long tag_use_water=0L;
    public static final Long tag_recommend_time=0L;
    public static final Long tag_member_community=0L;
    public static final Long tag_member_level=0L;

}
