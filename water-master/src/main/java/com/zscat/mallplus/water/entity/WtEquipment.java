package com.zscat.mallplus.water.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zscat.mallplus.util.BaseEntity;
import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author lyn
 * @date 2020-05-22
 * 设备信息
 */
@Data
@TableName("wt_equipment")
public class WtEquipment extends BaseEntity implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /**
     * 设备号
     **/
    @TableField("eqcode")
    private String eqcode;


    /**
     * 设备归属者id
     **/
    @TableField("eq_belongs_to")
    private String eqBelongsTo;


    /**
     * 设备归属分组id
     **/
    @TableField("eq_group_id")
    private Long eqGroupId;


    /**
     * 设备地址
     **/
    @TableField("eq_address")
    private String eqAddress;


    /**
     * 设备地址经度
     **/
    @TableField("eq_address_longitude")
    private String eqAddressLongitude;


    /**
     * 设备地纬度
     **/
    @TableField("eq_address_latitude")
    private String eqAddressLatitude;


    /**
     * 设备使用SIM卡
     **/
    @TableField("eq_simcode")
    private String eqSimcode;


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
     * 删除状态(0-正常,1-已删除)
     **/
    @TableField("del_flag")
    @TableLogic
    private String delFlag;


}
