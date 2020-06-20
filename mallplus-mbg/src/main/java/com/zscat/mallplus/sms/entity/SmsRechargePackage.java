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

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.io.Serializable;

/**
 * @author wang
 * @date 2020-05-30
 * 充值套餐
 */
@Data
@TableName("sms_recharge_package")
public class SmsRechargePackage extends BaseEntity implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 经销商id
     **/
    @TableField("dealer_id")
    @NotNull(message = "经销商不能为空！")
    private Long dealerId;


    /**
     * 所属店铺
     **/
    @TableField("store_id")
    @NotNull(message = "所属店铺不能为空！")
    private Integer storeId;


    /**
     *
     **/
    @TableField("store_name")
    @NotEmpty(message = "所属店铺名称不能为空！")
    private String storeName;


    /**
     * 充值条件
     **/
    @TableField("term_fee")
    @NotNull(message = "充值条件不能为空！")
    @Digits(integer = 10,fraction = 2,message = "充值金额不能很大！")
    private BigDecimal termFee;


    /**
     * 赠送金额
     **/
    @TableField("donate_fee")
    @NotNull(message = "赠送金额不能为空！")
    @Digits(integer = 10,fraction = 2,message = "赠送金额不能很大！")
    private BigDecimal donateFee;


    /**
     * 实际到账金额
     **/
    @TableField("actual_fee")
    @NotNull(message = "实际到账金额不能为空！")
    @Digits(integer = 10,fraction = 2,message = "实际到账金额不能很大！")
    private BigDecimal actualFee;


    /**
     * 赠品名称
     **/
    @TableField("gift_one")
    private String giftOne;


    /**
     * 赠品名称路径
     **/
    @TableField("gift_one_url")
    private String giftOneUrl;


    /**
     * 赠品名称
     **/
    @TableField("gift_two")
    private String giftTwo;


    /**
     * 赠品名称路径
     **/
    @TableField("gift_two_url")
    private String giftTwoUrl;


    /**
     * 赠品名称
     **/
    @TableField("gift_three")
    private String giftThree;


    /**
     * 赠品名称路径
     **/
    @TableField("gift_three_url")
    private String giftThreeUrl;


    /**
     *
     **/
    @TableField("create_time")
    private Date createTime;


    /**
     *
     **/
    @TableField("update_time")
    private Date updateTime;


}
