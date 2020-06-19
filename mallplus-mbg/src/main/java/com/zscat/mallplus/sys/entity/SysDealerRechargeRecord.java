package com.zscat.mallplus.sys.entity;

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
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.io.Serializable;

/**
 * @author wang
 * @date 2020-05-25
 * 经销商的余额充值记录
 */
@Data
@TableName("sys_dealer_recharge_record")
public class SysDealerRechargeRecord extends BaseEntity implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 经销商id
     **/
    @TableField("dealer_id")
    @NotNull(message = "经销商不能为空！")
    private Long dealerId;


    /**
     * 余额
     **/
    @TableField("balance")
    @NotNull(message = "余额不能为空！")
    @Digits(integer = 10, fraction = 2,message = "充值数目过大！")
    private BigDecimal balance;

    /**
     * 新的余额
     **/
    @TableField("new_balance")
    @NotNull(message = "新的余额不能为空！")
    @Digits(integer = 10, fraction = 2,message = "充值数目过大！")
    private BigDecimal newBalance;


    /**
     * 充值数目
     **/
    @TableField("recharge")
    @NotNull(message = "充值数目不能为空！")
    @Digits(integer = 10, fraction = 2,message = "充值数目过大！")
    private BigDecimal recharge;


    /**
     * 备注
     **/
    @TableField("note")
    private String note;


    /**
     *
     **/
    @TableField("create_by")
    @NotNull(message = "创建人不能为空！")
    private Long createBy;


    /**
     * 创建时间
     **/
    @TableField("create_time")
    private Date createTime;


    /**
     *
     **/
    @TableField("update_by")
    private Long updateBy;


    /**
     *
     **/
    @TableField("update_time")
    private Date updateTime;


    /**
     * 所属店铺
     **/
    @TableField("store_id")
    @NotNull(message = "门店不能为空！")
    private Integer storeId;


    /**
     *
     **/
    @TableField("store_name")
    @NotNull(message = "门店不能为空！")
    private String storeName;


}
