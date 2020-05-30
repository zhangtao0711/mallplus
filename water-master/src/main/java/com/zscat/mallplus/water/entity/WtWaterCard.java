package com.zscat.mallplus.water.entity;

import com.zscat.mallplus.util.BaseEntity;
import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * @author lyn
 * @date 2020-05-30
 * 水卡
 */
@Data
@TableName("wt_water_card")
public class WtWaterCard extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 生成id
     **/
    @TableField("card_create_id")
    private Long cardCreateId;


    /**
     * 卡号
     **/
    @TableField("card_no")
    private String cardNo;


    /**
     * 二维码
     **/
    @TableField("qr_code")
    private String qrCode;

    /**
     * 卡状态 字典water_code_state
     **/
    @TableField("state")
    private String state;

    /**
     * 备注
     **/
    @TableField("remarks")
    @Length(min=0, max=255, message="备注长度必须在255以内!")
    private String remarks;

    /**
     * 创建日期
     **/
    @TableField("create_time")
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
    private String delFlag;


}
