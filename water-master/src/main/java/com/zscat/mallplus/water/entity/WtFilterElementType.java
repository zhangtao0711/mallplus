package com.zscat.mallplus.water.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author lyn
 * @date 2020-05-20
 * 滤芯类型
 */
@Data
@TableName("wt_filter_element_type")
public class WtFilterElementType implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 滤芯类型名称
     **/
    @TableField("filter_element_type_name")
    @NotEmpty
    @Length(min=1, max=50, message="滤芯类型名称长度必须介于 1 和 50 之间!")
    private String filterElementTypeName;


    /**
     * 更换周期天数
     **/
    @TableField("change_cycle")
    @NotEmpty
    @Length(min=1, max=4, message="更换周期天数不能设置大于9999!")
    private Integer changeCycle;


    /**
     * 更换滤芯提前提醒天数
     **/
    @TableField("remind_day")
    @NotEmpty
//    @Length(min=1, max=2, message="更换滤芯提前提醒天数不能设置大于99!")
    @Min(1)
    @Max(99)
    private Integer remindDay;


    /**
     * 净水总量吨
     **/
    @TableField("purifier_total")
    @NotEmpty
    @Length(min=1, max=4, message="净水总量(吨)不能设置大于9999!")
    private Integer purifierTotal;


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
