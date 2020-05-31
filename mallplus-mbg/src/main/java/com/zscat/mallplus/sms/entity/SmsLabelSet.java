package com.zscat.mallplus.sms.entity;

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
 * @date 2020-05-30
 * 客户标签设置
 */
@Data
@TableName("sms_label_set")
public class SmsLabelSet extends BaseEntity implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 标签名称
     **/
    @TableField("label_name")
    private String labelName;


    /**
     * 标签描述
     **/
    @TableField("label_breif")
    private String labelBreif;


    /**
     * 创建时间
     **/
    @TableField("create_time")
    private Date createTime;


    /**
     * 更新时间
     **/
    @TableField("update_time")
    private Date updateTime;


    /**
     * 经销商id
     **/
    @TableField("dealer_id")
    private Long dealerId;


    /**
     * 所属门店
     **/
    @TableField("store_id")
    private Integer storeId;


    /**
     * 0否1是
     **/
    @TableField("is_system")
    private Integer isSystem;


    /**
     * 0数量1天2周3月4季5年
     **/
    @TableField("frequency")
    private Integer frequency;


    /**
     * 大于
     **/
    @TableField("greater")
    private BigDecimal greater;


    /**
     * 等于
     **/
    @TableField("equal")
    private BigDecimal equal;


    /**
     * 小于
     **/
    @TableField("limit")
    private BigDecimal limit;


    /**
     * 消息
     **/
    @TableField("message")
    private String message;

    @TableField(exist = false)
    private Integer count;

    @TableField("tag_id")
    private Long tagId;

}
