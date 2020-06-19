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
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.io.Serializable;

/**
 * @author wang
 * @date 2020-05-28
 * 代客充值
 */
@Data
@TableName("sms_replace_recharge")
public class SmsReplaceRecharge extends BaseEntity implements Serializable {


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
     * 奖励方式 0固定金额 1支付比例
     **/
    @TableField("reward_way")
    @NotNull(message = "奖励方式不能为空！")
    @Length(min = 1,max = 1,message = "奖励方式传值不正确！")
    private Integer rewardWay;


    /**
     * 奖励金额或者比例
     **/
    @TableField("reward")
    @NotNull(message = "奖励金额或者比例不能为空！")
    @Digits(integer = 10,fraction = 2,message = "奖励金额或者比例不能很大！")
    private BigDecimal reward;


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
