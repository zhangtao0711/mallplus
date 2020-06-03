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
    private Long dealerId;


    /**
     * 所属店铺
     **/
    @TableField("store_id")
    private Integer storeId;


    /**
     *
     **/
    @TableField("store_name")
    private String storeName;


    /**
     * 充值条件
     **/
    @TableField("term_fee")
    private BigDecimal termFee;


    /**
     * 赠送金额
     **/
    @TableField("donate_fee")
    private BigDecimal donateFee;


    /**
     * 实际到账金额
     **/
    @TableField("actual_fee")
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
