package com.zscat.mallplus.water.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.zscat.mallplus.utils.BaseEntity;

import java.util.Date;


import java.io.Serializable;

/**
 * @author lyn
 * @date 2020-06-17
 * 售水机消费记录
 */
@Data
@TableName("wt_consume_record")
public class WtConsumeRecord extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;


    /**
     * 设备号
     **/
    @TableField("eqcode")
    private String eqcode;


    /**
     * 冷水00热水01
     **/
    @TableField("water_type")
    private String waterType;


    /**
     * 消费方式（0投币/1线上/2刷卡）
     **/
    @TableField("consume_type")
    private String consumeType;


    /**
     * 卡号(有卡有效)
     **/
    @TableField("card_no")
    private String cardNo;


    /**
     * 消费金额(分)
     **/
    @TableField("consume_money")
    private Integer consumeMoney;


    /**
     * 订单状态(线上/投币02完成 刷卡04完成）09未支付
     **/
    @TableField("order_state")
    private String orderState;


    /**
     * 订单完成日期
     **/
    @TableField("end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;


    /**
     * 创建日期
     **/
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    /**
     * 创建人(用户id)
     **/
    @TableField("create_by")
    private Long createBy;


    /**
     * 删除状态(1-正常,0-已删除)
     **/
    @TableField("del_flag")
    @TableLogic
    private String delFlag;



    /**
     * 经销商id
     **/
    @TableField("dealer_id")
    private Long dealerId;


}
