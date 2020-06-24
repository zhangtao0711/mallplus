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
@TableName("jifen_luck_draw_record")
public class JifenLuckDrawRecord extends BaseEntity implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 用户ID
     **/
    @TableField("member_id")
    private Long memberId;


    /**
     * 中奖用户手机号
     **/
    @TableField("member_mobile")
    private String memberMobile;


    /**
     * 消耗积分
     **/
    @TableField("points")
    private Integer points;


    /**
     * 奖品ID
     **/
    @TableField("prize_id")
    private Long prizeId;


    /**
     * 1:中奖 2:未中奖
     **/
    @TableField("result")
    private Integer result;


    /**
     * 中奖月份
     **/
    @TableField("month")
    private String month;


    /**
     * 中奖日期（不包括时间）
     **/
    @TableField("daily")
    private Date daily;


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
