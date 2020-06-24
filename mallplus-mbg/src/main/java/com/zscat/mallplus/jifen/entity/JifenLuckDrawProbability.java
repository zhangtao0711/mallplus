package com.zscat.mallplus.jifen.entity;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zscat.mallplus.utils.BaseEntity;

import java.math.BigDecimal;
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
@TableName("jifen_luck_draw_probability")
public class JifenLuckDrawProbability extends BaseEntity implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 奖品ID
     **/
    @TableField("points_prize_id")
    private Long pointsPrizeId;


    /**
     * 抽奖活动主键
     **/
    @TableField("activity_id")
    private Integer activityId;


    /**
     * 概率
     **/
    @TableField("probability")
    private BigDecimal probability;


    /**
     * 商品抽中后的冷冻次数
     **/
    @TableField("frozen")
    private Integer frozen;


    /**
     * 该商品平台每天最多抽中的次数
     **/
    @TableField("prize_day_max_times")
    private Integer prizeDayMaxTimes;


    /**
     * 每位用户每月最多抽中该商品的次数
     **/
    @TableField("user_prize_month_max_times")
    private Integer userPrizeMonthMaxTimes;


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

    @TableField("store_id")
    private Integer storeId;
}
