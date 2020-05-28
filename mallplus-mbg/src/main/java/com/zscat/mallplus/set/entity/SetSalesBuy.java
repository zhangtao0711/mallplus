package com.zscat.mallplus.set.entity;

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

import java.math.BigDecimal;
import java.io.Serializable;

/**
 * @author wang
 * @date 2020-05-26
 * 营销功能购买
 */
@Data
@TableName("set_sales_buy")
public class SetSalesBuy extends BaseEntity implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 经销商id
     **/
    @TableField("dealer_id")
    private Long dealerId;


    /**
     * 菜单id
     **/
    @TableField("perssion_id")
    private Long perssionId;


    /**
     * 菜单名称
     **/
    @TableField("perssion_name")
    private String perssionName;


    /**
     * 购买时间
     **/
    @TableField("buy_time")
    private Date buyTime;


    /**
     * 到期时间
     **/
    @TableField("end_time")
    private Date endTime;


    /**
     * 上次有效时间
     **/
    @TableField("last_end_time")
    private Date lastEndTime;


    /**
     * 累计购买时间单位月
     **/
    @TableField("total_buy_month")
    private Integer totalBuyMonth;

    /**
     * 是否购买0否1是
     */
    @TableField("is_buy")
    private String isBuy;


    /**
     * 累计购买金额
     **/
    @TableField("total_money")
    private BigDecimal totalMoney;


    /**
     * 累计购买积分
     **/
    @TableField("total_credit")
    private BigDecimal totalCredit;


    /**
     * 生成时间
     **/
    @TableField("create_time")
    private Date createTime;


    /**
     * 更新时间
     **/
    @TableField("update_time")
    private Date updateTime;

    /**
     * 所属店铺
     **/
    @TableField("store_id")
    private Integer storeId;


    /**
     *
     **/
    @TableField("store_name")
    private String storeName;

}
