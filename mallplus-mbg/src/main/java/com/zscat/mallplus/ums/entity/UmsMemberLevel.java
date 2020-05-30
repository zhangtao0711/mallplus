package com.zscat.mallplus.ums.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.zscat.mallplus.utils.BaseEntity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.io.Serializable;

/**
 * @author lyn
 * @date 2020-05-29
 * 会员等级
 */
@Data
@TableName("ums_member_level")
public class UmsMemberLevel extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 等级名称
     **/
    @TableField("name")
    @NotEmpty(message = "等级名称不能为空")
    @Length(min=1, max=100, message="等级名称长度必须介于 1 和 100 之间!")
    private String name;


    /**
     * 成为会员的价格
     **/
    @TableField("price")
    @DecimalMin(value="0.01")
    @Digits(integer=8, fraction=2,message="成为会员的价格不能设定大于99999999.99,小数位数只支持2位。")
    private BigDecimal price;

    /**
     * 会员折扣(0.1-10)
     **/
    @TableField("priviledge_member_price")
    @DecimalMin(value="0.1")
    @Digits(integer=2, fraction=1,message="请输入0.1-10之间的数字，值为空代表不设置折扣！")
    private BigDecimal priviledgeMemberPrice;


    /**
     * 状态（0启用1禁用）
     **/
    @TableField("state")
    @NotEmpty
    private String state;


    /**
     * 适用场景（0平台商品1平台积分商城）
     **/
    @TableField("apply_scene")
    @NotEmpty(message="适用场景必须选择一个以上，包含一个。")
    private String applyScene;


    /**
     * 等级图标
     **/
    @TableField("icon")
    private String icon;


    /**
     * 背景色区域
     **/
    @TableField("pic")
    private String pic;

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
