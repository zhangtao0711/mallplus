package com.zscat.mallplus.water.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zscat.mallplus.util.BaseEntity;
import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * @author lyn
 * @date 2020-05-22
 * SIM卡列表
 */
@Data
@TableName("wt_sim_card")
public class WtSimCard extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 物联网卡号
     **/
    @TableField("cardno")
    @Length(min=1, max=32, message="物联网卡号长度必须介于 1 和 32 之间!")
    private String cardno;


//    /**
//     * 产品编码
//     **/
//    @TableField("product_id")
//    @Length(min=1, max=100, message="物联网卡号长度必须介于 1 和 100 之间!")
//    private String productId;


    /**
     * 主版设备id
     **/
    @TableField("eqcode")
//    @Length(min=1, max=20, message="主版设备长度必须介于 1 和 16 之间!")
    private Long eqcode;


    /**
     * 使用流量(-1表示未知，单位KB，时间当月)
     **/
    @TableField("used")
    @Length(min=1, max=32, message="使用流量长度必须介于 1 和 32 之间!")
    private String used;


    /**
     * 卡状态 (字典类型：sim_state)
     **/
    @TableField("state")
    @Length(min=1, max=4, message="卡状态长度必须介于 1 和 4 之间!")
    private String state;


    /**
     * 卡有效期
     **/
    @TableField("expired_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expiredAt;


    /**
     * 是否预充值 （字典类型：sim_precharge_type0无、1有）
     **/
    @TableField("precharge_type")
    @Length(min=1, max=1, message="是否预充值长度必须是 1 !")
    private String prechargeType;


    /**
     * 预充值生效日期
     **/
    @TableField("precharge_offsets_time")
    private String prechargeOffsetsTime;


    /**
     * 创建日期
     **/
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    /**
     * 创建人
     **/
    @TableField("create_by")
    private Long createBy;


    /**
     * 更新日期
     **/
    @TableField("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


    /**
     * 更新人
     **/
    @TableField("update_by")
    private Long updateBy;


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
    /**
     * 地址
     **/
    private transient String eqAddress;
}
