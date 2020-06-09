package com.zscat.mallplus.sms.entity;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zscat.mallplus.utils.BaseEntity;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.io.Serializable;

/**
 * @author wang
 * @date 2020-05-30
 * 扫码购水记录
 */
@Data
@TableName("sms_water_buy_record")
public class SmsWaterBuyRecord extends BaseEntity implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 公众号id
     **/
    @TableField("uniacid")
    private Integer uniacid;


    /**
     * 用户的openid
     **/
    @TableField("open_id")
    private String openId;


    /**
     * 创建时间
     **/
    @TableField("create_time")
    private Date createTime;


    /**
     * 购买时间
     **/
    @TableField("buy_time")
    private Date buyTime;


    /**
     * 支付方式
     **/
    @TableField("pay_way")
    private Integer payWay;


    /**
     * 状态
     **/
    @TableField("status")
    private Integer status;


    /**
     * 充值金额
     **/
    @TableField("pay_fee")
    private BigDecimal payFee;


    /**
     * 实收金额
     **/
    @TableField("actual_fee")
    private BigDecimal actualFee;


    /**
     * 实际到账金额
     **/
    @TableField("actual_account")
    private BigDecimal actualAccount;


    /**
     * 唯一订单号
     **/
    @TableField("out_trade_no")
    private String outTradeNo;


    /**
     * 所属店铺
     **/
    @TableField("store_id")
    private Integer storeId;


    /**
     * 经销商id
     **/
    @TableField("dealer_id")
    private Long dealerId;


    /**
     * 返回的唯一id，微信的
     **/
    @TableField("prepay_id")
    private String prepayId;

    /**
     * 微信支付订单号
     */
    @TableField("transaction_id")
    private String transactionId;

    /**
     * 分账唯一订单号
     */
    @TableField("rout_out_order_no")
    private String routOutOrderNo;

    /**
     * 微信分账单号，微信系统返回的唯一标识
     */
    @TableField("order_id")
    private String orderId;

    @TableField("first_amount")
    private BigDecimal firstAmount;

    @TableField("second_amount")
    private BigDecimal secondAmount;

    @TableField("third_amount")
    private BigDecimal thirdAmount;

    @TableField("rout_status")
    private Integer routStatus;
    @TableField("withdraw_status")
    private String withdrawStatus;


}
