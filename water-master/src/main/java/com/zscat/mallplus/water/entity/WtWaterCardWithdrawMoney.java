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

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.io.Serializable;

/**
 * @author lyn
 * @date 2020-06-08
 * 水卡取现
 */
@Data
@TableName("wt_water_card_withdraw_money")
public class WtWaterCardWithdrawMoney extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 卡号
     **/
    @TableField("card_no")
    @NotEmpty(message="请选择取款卡号！")
    private String cardNo;


    /**
     * 金额取款
     **/
    @TableField("card_money")
    @DecimalMax(value="99999999.99")
    @Digits(integer=8, fraction=2,message="金额取款不能设定大于99999999.99,小数位数只支持2位。")
    private BigDecimal cardMoney;


    /**
     * 赠送取款
     **/
    @TableField("give_money")
    @DecimalMax(value="99999999.99")
    @Digits(integer=8, fraction=2,message="赠送取款不能设定大于99999999.99,小数位数只支持2位。")
    private BigDecimal giveMoney;


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


}
