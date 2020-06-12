package com.zscat.mallplus.water.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
//import com.zscat.mallplus.utils.BaseEntity;
import com.zscat.mallplus.util.BaseEntity;
import lombok.Data;
import org.apache.poi.hpsf.Decimal;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
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
     * 滤芯名称
     **/
    @TableField("name")
    @NotEmpty(message = "滤芯名称不能为空")
    @Length(min=1, max=200, message="滤芯名称长度必须介于 1 和 200 之间!")
    private String name;
    /**
     * 设备号
     **/
    @TableField("eqcode")
    @NotEmpty(message = "选择设备后添加滤芯！")
    private Long eqcode;

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
    @Range(min=0,max = 999,message = "滤芯级别只能输入1-999的正整数！")
    private int filterElementLevel;

    /**
     * 计费模式
     **/
    @TableField("billing_mode")
    @NotEmpty(message = "计费模式不能为空")
    private String billingMode;

    /**
     * 过滤水量
     **/
    @TableField("purifier_num")
    @Length(min=0, max=4, message="过滤水量(吨)不能设置大于9999!")
    private Integer purifierNum;

    /**
     * 已使用百分比
     **/
    @TableField("use_percent")
    private Decimal usePercent;

    /**
     * 滤芯更换时间
     **/
    @TableField("change_time")
    @NotEmpty(message = "滤芯更换时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date changeTime;

    /**
     * 滤芯到期时间
     **/
    @TableField("end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    /**
     * 滤芯状态0正常使用1准备更换2等待更换
     **/
    @TableField("state")
    private String state;

    /**
     * 有效周期天数
     **/
    @TableField("change_cycle")
    private Integer changeCycle;
    /**
     * 水量标准(吨)
     **/
    @TableField("purifier_total")
    private Integer purifierTotal;

    /**
     * 更换滤芯提前提醒日期
     **/
    @TableField("remind_time")
    private Date remindTime;

    /**
     * 备注
     **/
    @TableField("remarks")
    @Length(min=0, max=255, message="计费模式长度必须介于 1 和 255 之间!")
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

    /**
     * 经销商id
     **/
    @TableField("dealer_id")
    private Long dealerId;
}
