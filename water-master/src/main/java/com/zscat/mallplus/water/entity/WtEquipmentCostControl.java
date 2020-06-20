package com.zscat.mallplus.water.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.io.Serializable;

/**
 * @author lyn
 * @date 2020-06-19
 * 设备成本管理
 */
@Data
@TableName("wt_equipment_cost_control")
public class WtEquipmentCostControl extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 设备id
     **/
    @TableField("eq_id")
    private String eqId;


    /**
     * 更新日期
     **/
    @TableField("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


    /**
     * 更新人
     **/
    @TableField("update_by")
    private Long updateBy;


    /**
     * 成本分类（0物业1滤芯2配件3其它）
     **/
    @TableField("cost_type")
    private String costType;


    /**
     * 成本名称
     **/
    @TableField("cost_name")
    @NotEmpty(message="名称不能为空！")
    @Length(min=1,max=50,message = "成本名称长度必须在1-50位之间！")
    private String costName;


    /**
     * 成本金额
     **/
    @TableField("cost_money")
    @DecimalMin(value="0.01")
    @DecimalMax(value="99999999.99")
    @Digits(integer=8, fraction=2,message="成本金额不能设定大于99999999.99,小数位数只支持2位。")
    private BigDecimal costMoney;


}
