/**
 * <p>mallplus Pay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 * <p>
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 * <p>
 * <p>mallplus Pay 交流群: 320860169</p>
 * <p>
 * <p>Node.js 版: https://gitee.com/javen205/TNW</p>
 * <p>
 * <p>商户二维码支付接口 Model</p>
 *
 * @author Javen
 */
package com.zscat.mallplus.jdpay.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CustomerPayModel extends JdBaseModel {
    private String version;
    private String sign;
    private String merchant;
    private String payMerchant;
    private String device;
    private String tradeNum;
    private String tradeName;
    private String tradeDesc;
    private String tradeTime;
    private String amount;
    private String orderType;
    private String industryCategoryCode;
    private String currency;
    private String note;
    private String callbackUrl;
    private String notifyUrl;
    private String ip;
    private String expireTime;
    private String riskInfo;
    private String goodsInfo;
    private String bizTp;
}
