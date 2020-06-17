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

import java.beans.Transient;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
//import javax.persistence.*;

import java.beans.Transient;

/**
 * @author lyn
 * @date 2020-05-30
 * 批量开卡
 */
@Data
@TableName("wt_water_card_activate")
public class WtWaterCardActivate extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 起始卡号
     **/
    @TableField("start_no")
    @NotEmpty(message="起始卡号必须输入")
    @Pattern(regexp = "^[0-9]*$", message = "起始卡号只能输入[0-9]的数字!")
    @Length(min=1, max=10, message="起始卡号长度必须介于 1 和 9 之间！")
    private String startNo;


    /**
     * 终止卡号
     **/
    @TableField("end_no")
    @NotEmpty(message="终止卡号必须输入")
    @Pattern(regexp = "^[0-9]*$", message = "终止卡号只能输入[0-9]的数字!")
    @Length(min=1, max=10, message="终止卡号长度必须介于 1 和 9 之间")
    private String endNo;


    /**
     * 设备号
     **/
    @TableField("eqcode")
    @NotEmpty(message="设备号必须输入")
    @Length(min=1, max=16, message="设备号长度必须介于 1 和 16 之间")
    private String eqcode;

    /**
     * 经销商名称
     **/
//    @Transient
    private transient String storeName;

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
