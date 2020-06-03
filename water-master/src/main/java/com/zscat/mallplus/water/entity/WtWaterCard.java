package com.zscat.mallplus.water.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zscat.mallplus.util.BaseEntity;
import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.io.Serializable;

/**
 * @author lyn
 * @date 2020-05-30
 * 水卡
 */
@Data
@TableName("wt_water_card")
public class WtWaterCard extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 生成id
     **/
    @TableField("card_create_id")
    private Long cardCreateId;


    /**
     * 卡号
     **/
    @TableField("card_no")
    private String cardNo;


    /**
     * 二维码
     **/
    @TableField("qr_code")
    private String qrCode;

    /**
     * 绑定用户id
     **/
    @TableField("ums_member_id")
    private Long umsMemberId;

    /**
     * 卡状态 字典water_code_state
     **/
    @TableField("state")
    private String state;
    /**
     * 体验金额
     **/
    @TableField("experience_money")
    @DecimalMin(value="0.01")
    @DecimalMax(value="99999999.99")
    @Digits(integer=8, fraction=2,message="体验金额不能设定大于99999999.99,小数位数只支持2位。")
    private BigDecimal experienceMoney;
    /**
     * 体验到期日
     **/
    @TableField("experience_end_data")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date experienceEndData;

    /**
     * 卡内余额
     **/
    @TableField("card_money")
    @DecimalMin(value="0.01")
    @DecimalMax(value="99999999.99")
    @Digits(integer=8, fraction=2,message="卡内余额不能设定大于99999999.99,小数位数只支持2位。")
    private BigDecimal cardMoney;
    /**
     * 赠送金额
     **/
    @TableField("give_money")
    @DecimalMin(value="0.01")
    @DecimalMax(value="99999999.99")
    @Digits(integer=8, fraction=2,message="卡内余额不能设定大于99999999.99,小数位数只支持2位。")
    private BigDecimal giveMoney;

    /**
     * 备注
     **/
    @TableField("remarks")
    @Length(min=0, max=255, message="备注长度必须在255以内!")
    private String remarks;

    /**
     * 创建日期
     **/
    @TableField("create_time")
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
    private String delFlag;

    /**
     * 经销商
     **/
    @TableField("dealer_id")
    private Long dealerId;
}
