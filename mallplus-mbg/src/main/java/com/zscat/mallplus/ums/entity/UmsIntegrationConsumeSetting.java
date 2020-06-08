package com.zscat.mallplus.ums.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zscat.mallplus.utils.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 积分消费设置
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Setter
@Getter
@TableName("ums_integration_consume_setting")
public class UmsIntegrationConsumeSetting extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 每一元需要抵扣的积分数量
     */
    @TableField("deduction_per_amount")
    private Integer deductionPerAmount;

    /**
     * 每笔订单最高抵用百分比
     */
    @TableField("max_percent_per_order")
    private Integer maxPercentPerOrder;

    /**
     * 充值的时候，1元兑换的积分
     */
    @TableField("use_unit")
    private Integer useUnit;

    /**
     * 是否可以和优惠券同用；0->不可以；1->可以
     */
    @TableField("coupon_status")
    private Integer couponStatus;

    /**
     * 登录送积分
     */
    @TableField("login")
    private Integer login;

    /**
     * 注册送积分
     */
    @TableField("register")
    private Integer register;
    /**
     * 签到送积分
     */
    @TableField("sign")
    private Integer sign;

    /**
     * 下单 送积分比例
     */
    @TableField("orders")
    private Integer orders;

    /**
     * 是否开启下单送积分比例 0否1是
     */
    @TableField("orders_status")
    private Integer ordersStatus;

    /**
     * 注册赠送的水费
     */
    @TableField("water_fee")
    private BigDecimal waterFee;

    /**
     * 经销商id
     */
    @TableField("dealer_id")
    private Long dealerId;

    /**
     * 激活设备送积分
     */
    @TableField("device_activate")
    private Integer deviceActivate;
}
