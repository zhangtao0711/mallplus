package com.zscat.mallplus.water.entity;

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

import java.io.Serializable;

/**
 * @author lyn
 * @date 2020-06-09
 * SIM卡第三方充值记录
 */
@Data
@TableName("wt_sim_send_order")
public class WtSimSendOrder extends BaseEntity implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /**
     * 订单唯一编号（微信支付）
     **/
    @TableField("logno")
    private String logno;


    /**
     * SIM卡号
     **/
    @TableField("cardno")
    private String cardno;


    /**
     * 错误原因
     **/
    @TableField("error")
    private String error;


    /**
     * 创建时间
     **/
    @TableField("create_time")
    private Date createTime;


    /**
     * 支付状态(0成功，0以外失败）
     **/
    @TableField("paystatus")
    private Integer paystatus;


    /**
     * 支付时间
     **/
    @TableField("paytime")
    private Date paytime;


    /**
     * 经销商id
     **/
    @TableField("dealer_id")
    private Long dealerId;


}
