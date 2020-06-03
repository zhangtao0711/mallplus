package com.zscat.mallplus.water.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
//import com.zscat.mallplus.utils.BaseEntity;
import com.zscat.mallplus.util.BaseEntity;
import lombok.Data;
//import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 * @author lyn
 * @date 2020-05-20
 * 滤芯
 */
@Data
@TableName("wt_filter_element")
public class WtFilterElement extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 滤芯类型id
     **/
    @TableField("filter_element_type_id")
    @NotEmpty(message = "滤芯类型不能为空")
    private Long filterElementTypeId;


    /**
     * 滤芯级别
     **/
    @TableField("filter_element_level")
    @NotEmpty(message = "滤芯级别不能为空")
//    @Length(min=1, max=50, message="滤芯级别长度必须介于 1 和 50 之间!")
    private String filterElementLevel;


    /**
     * 计费模式
     **/
    @TableField("billing_mode")
    @NotEmpty(message = "计费模式不能为空")
//    @Length(min=1, max=100, message="计费模式长度必须介于 1 和 100 之间!")
    private String billingMode;


    /**
     * 滤芯更换时间
     **/
    @TableField("change_time")
    @NotEmpty(message = "滤芯更换时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date changeTime;


    /**
     * 备注
     **/
    @TableField("remarks")
//    @Length(min=1, max=255, message="计费模式长度必须介于 1 和 255 之间!")
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
     * 删除状态(0-正常,1-已删除)
     **/
    @TableField("del_flag")
    @TableLogic
    private String delFlag;


}
