package com.zscat.mallplus.jifen.entity;

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
 * @author wang
 * @date 2020-06-23
 * 抽奖
 */
@Data
@TableName("jifen_activity_lucky_draw")
public class JifenActivityLuckyDraw extends BaseEntity implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /**
     * 关联
     **/
    @TableField("uniacid")
    private Integer uniacid;


    /**
     * 活动名称
     **/
    @TableField("name")
    private String name;


    /**
     * 开始时间
     **/
    @TableField("begin_time")
    private Date beginTime;


    /**
     * 结束时间
     **/
    @TableField("end_time")
    private Date endTime;


    /**
     * 活动奖励有效期限
     **/
    @TableField("activity_reward_validity_period")
    private Integer activityRewardValidityPeriod;


    /**
     * 积分消耗
     **/
    @TableField("Integral_consumption")
    private Integer integralConsumption;


    /**
     * 限制次数
     **/
    @TableField("tries_limit")
    private Integer triesLimit;


    /**
     * 创建时间
     **/
    @TableField("create_time")
    private Date createTime;


    /**
     * 更新时间
     **/
    @TableField("update_time")
    private Date updateTime;

    @TableField("store_id")
    private Integer storeId;
}
