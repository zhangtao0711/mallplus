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

import java.io.Serializable;

/**
 * @author lyn
 * @date 2020-06-19
 * 设备备注/滤芯履历
 */
@Data
@TableName("wt_equipment_remarks")
public class WtEquipmentRemarks extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 设备号
     **/
    @TableField("eqcode")
    private Long eqcode;


    /**
     * 备注
     **/
    @TableField("remarks")
    private String remarks;


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
     * 0备注1滤芯履历
     **/
    @TableField("remarks_type")
    private String remarksType;


}
