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
 * @date 2020-05-29
 * 充值记录
 */
@Data
@TableName("sms_recharge_record")
public class SmsRechargeRecord extends BaseEntity implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 会员id
     **/
    @TableField("member_id")
    private Long memberId;


    /**
     * 会员名称
     **/
    @TableField("member_name")
    private String memberName;


    /**
     * 套餐id
     **/
    @TableField("package_id")
    private Long packageId;


    /**
     * 代充值员工id
     **/
    @TableField("replace_id")
    private Long replaceId;


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
     * 卡号
     **/
    @TableField("card_no")
    private String cardNo;


    /**
     * 状态
     **/
    @TableField("status")
    private Integer status;


    /**
     * 赠品名称
     **/
    @TableField("gift")
    private String gift;


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
     * 奖励金额
     **/
    @TableField("reward")
    private BigDecimal reward;


    /**
     * 充值类型 1代客2用户3后台
     **/
    @TableField("recharge_type")
    private Integer rechargeType;

    /**
     * 唯一订单号
     */
    @TableField("out_trade_no")
    private String outTradeNo;
    /**
     * 经销商id
     **/
    @TableField("dealer_id")
    private Long dealerId;


    /**
     * 所属店铺
     **/
    @TableField("store_id")
    private Integer storeId;

    @TableField("uniacid")
    private Integer uniacid;

}
