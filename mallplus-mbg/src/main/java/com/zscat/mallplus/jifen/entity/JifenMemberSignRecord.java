package com.zscat.mallplus.jifen.entity;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zscat.mallplus.utils.BaseEntity;

import java.time.Year;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wang
 * @date 2020-06-22
 * 签到记录
 */
@Data
@TableName("jifen_member_sign_record")
public class JifenMemberSignRecord extends BaseEntity implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 会员id
     **/
    @TableField("member_id")
    private Long memberId;


    /**
     * 签到信息（每个月的）
     **/
    @TableField("mark")
    private String mark;


    /**
     * 总签到数
     **/
    @TableField("count")
    private Integer count;


    /**
     * 连续签到
     **/
    @TableField("continue_sign")
    private Integer continueSign;


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


    /**
     * 小程序或者公众号唯一标识
     **/
    @TableField("uniacid")
    private Integer uniacid;


    /**
     * 月份
     **/
    @TableField("month")
    private Integer month;


    /**
     * 年份
     **/
    @TableField("year")
    private Year year;


}
