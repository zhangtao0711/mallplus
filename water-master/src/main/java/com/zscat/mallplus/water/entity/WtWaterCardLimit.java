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
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * @author lyn
 * @date 2020-06-05
 * 水卡限制消费
 */
@Data
@TableName("wt_water_card_limit")
public class WtWaterCardLimit extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 起始卡号
     **/
    @TableField("start_no")
    private String startNo;


    /**
     * 终止卡号
     **/
    @TableField("end_no")
    private String endNo;


    /**
     * 卡号
     **/
    @TableField("card_no")
    private String cardNo;

    /**
     * 设备号
     **/
    @TableField("eqcode")
    private String eqcode;

    /**
     * 备注
     **/
    @TableField("remarks")
    @Length(min=0, max=255, message="备注长度必须介于 1 和 255 之间")
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
     * 经销商id
     **/
    @TableField("dealer_id")
    private Long dealerId;


}
