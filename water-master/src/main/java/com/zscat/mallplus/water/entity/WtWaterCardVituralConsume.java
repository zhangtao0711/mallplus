package com.zscat.mallplus.water.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zscat.mallplus.util.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author wang
 * @date 2020-06-06
 * 虚拟水卡消费记录
 */
@Data
@TableName("wt_water_card_vitural_consume")
public class WtWaterCardVituralConsume extends BaseEntity implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 虚拟卡id
     **/
    @TableField("virtual_id")
    private Long virtualId;


    /**
     * 用户id
     **/
    @TableField("member_id")
    private Long memberId;


    /**
     * 小程序或者公众号id
     **/
    @TableField("uniacid")
    private Integer uniacid;


    /**
     * 卡号
     **/
    @TableField("card_no")
    private String cardNo;


    /**
     * 消费金额
     **/
    @TableField("consume_fee")
    private BigDecimal consumeFee;


    /**
     * 卡内余额
     **/
    @TableField("card_money")
    private BigDecimal cardMoney;


    /**
     * 创建日期
     **/
    @TableField("create_time")
    private Date createTime;


    /**
     * 水量
     **/
    @TableField("water_yield")
    private BigDecimal waterYield;

    @TableField(exist = false)
    private Integer type;

    private Integer storeId;

}
