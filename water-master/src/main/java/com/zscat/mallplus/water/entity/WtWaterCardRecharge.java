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

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.io.Serializable;

/**
 * @author lyn
 * @date 2020-05-31
 * 充值
 */
@Data
@TableName("wt_water_card_recharge")
public class WtWaterCardRecharge extends BaseEntity implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 充值金额类型（0充值金额1充值体验金）字典recharge_money_type
     **/
    @TableField("recharge_money_type")
    @NotEmpty(message="请选择充值金额或充值体验金额！")
    private String rechargeMoneyType;


    /**
     * 充值类型（0单卡 1批量 2筛选充值）字典	recharge_type
     **/
    @TableField("recharge_type")
    @NotEmpty(message="请选择批量或筛选充值！")
    private String rechargeType;

    /**
     * 起始卡号
     **/
    @TableField("start_no")
//    @NotEmpty(message="起始卡号必须输入")
    @Pattern(regexp = "^[0-9]*$", message = "起始卡号只能输入[0-9]的数字!")
    @Length(min=1, max=10, message="起始卡号长度必须介于 1 和 10 之间！")
    private String startNo;


    /**
     * 终止卡号
     **/
    @TableField("end_no")
//    @NotEmpty(message="终止卡号必须输入")
    @Pattern(regexp = "^[0-9]*$", message = "终止卡号只能输入[0-9]的数字!")
    @Length(min=1, max=10, message="终止卡号长度必须介于 1 和 10 之间")
    private String endNo;



    /**
     * 卡号
     **/
    @TableField("card_no")
    @Pattern(regexp = "^[0-9]*$", message = "卡号只能输入[0-9]的数字!")
    @Length(min=1, max=10, message="卡号长度必须介于 1 和 10 之间")
    private String cardNo;


    /**
     * 充值金额
     **/
    @TableField("recharge_money")
    @DecimalMin(value="0.01")
    @DecimalMax(value="99999999.99")
    @Digits(integer=8, fraction=2,message="充值金额不能设定大于99999999.99,小数位数只支持2位。")
    private BigDecimal rechargeMoney;


    /**
     * 实收金额
     **/
    @TableField("received_money")
    @DecimalMin(value="0.01")
    @DecimalMax(value="99999999.99")
    @Digits(integer=8, fraction=2,message="实收金额不能设定大于99999999.99,小数位数只支持2位。")
    private BigDecimal receivedMoney;


    /**
     * 体验金额
     **/
    @TableField("experience_money")
    @DecimalMin(value="0.01")
    @DecimalMax(value="99999999.99")
    @Digits(integer=8, fraction=2,message="体验金额不能设定大于99999999.99,小数位数只支持2位。")
    private BigDecimal experienceMoney;

    /**
     * 体验到期类型（0到期日1有效天数）字典 experience_end_type
     **/
    @TableField("experience_end_type")
    private String experienceEndType;

    /**
     * 体验到期日
     **/
    @TableField("experience_end_data")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date experienceEndData;


    /**
     * 体验金有效天数
     **/
    @TableField("experience_end_day")
    @Min(1)
    @Digits(integer=3, fraction=0,message="体验金额不能设定大于999正整数。")
    private Integer experienceEndDay;


    /**
     * 会员卡余额-筛选记号(字典where_mark)
     **/
    @TableField("ums_balance_mark")
    private String umsBalanceMark;


    /**
     * 会员卡余额-筛选条件
     **/
    @TableField("ums_balance_where")
    private BigDecimal umsBalanceWhere;


    /**
     * 用水频次-筛选记号(字典where_mark)
     **/
    @TableField("ums_use_mark")
    private String umsUseMark;


    /**
     * 用水频次-筛选条件
     **/
    @TableField("ums_use_where")
    private Integer umsUseWhere;


    /**
     * 用水频次-筛选周期(字典where_period)
     **/
    @TableField("ums_use_period")
    private String umsUsePeriod;


    /**
     * 推荐频次-筛选记号(字典where_mark)
     **/
    @TableField("ums_recommend_mark")
    private String umsRecommendMark;


    /**
     * 推荐频次-筛选条件
     **/
    @TableField("ums_recommend_where")
    private Integer umsRecommendWhere;


    /**
     * 推荐频次-筛选周期(字典where_period)
     **/
    @TableField("ums_recommend_period")
    private String umsRecommendPeriod;


    /**
     * 用户社区
     **/
    @TableField("ums_community")
    private String umsCommunity;


    /**
     * 会员等级
     **/
    @TableField("ums_member_level")
    private Long umsMemberLevel;


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
     * 经销商
     **/
    @TableField("dealer_id")
    private Long dealerId;
}
