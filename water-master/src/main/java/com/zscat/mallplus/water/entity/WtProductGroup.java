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
 * 设备分组
 */
@Data
@TableName("wt_product_group")
public class WtProductGroup extends BaseEntity implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 设备分组名称
     **/
    @TableField("product_group_name")
    private String productGroupName;


    /**
     * 客服电话
     **/
    @TableField("customer_service_tp")
    private String customerServiceTp;


    /**
     * 售后人员联系方式
     **/
    @TableField("after_sales_tp")
    private String afterSalesTp;


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
     * 删除状态(1-正常,0-已删除)
     **/
    @TableField("del_flag")
    @TableLogic
    private String delFlag;


}
