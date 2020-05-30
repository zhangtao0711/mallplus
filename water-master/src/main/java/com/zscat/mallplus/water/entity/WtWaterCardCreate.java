package com.zscat.mallplus.water.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zscat.mallplus.util.BaseEntity;
import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;


//import javax.persistence.Transient;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author lyn
 * @date 2020-05-29
 * 制卡
 */
@Data
@TableName("wt_water_card_create")
public class WtWaterCardCreate extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 代号（英文字符大写）
     **/
    @TableField("code")
    @NotEmpty(message="代号必须输入")
    @Length(min=2, max=2, message="代号必须是两位英文字符！")
    private String code;


    /**
     * 起始卡号
     **/
    @TableField("start_no")
    @NotEmpty(message="起始卡号必须输入")
    @Length(min=1, max=10, message="起始卡号必须设定！")
    private String startNo;


    /**
     * 终止卡号
     **/
    @TableField("end_no")
    @NotEmpty(message="终止卡号必须输入")
    @Length(min=1, max=10, message="终止卡号必须设定！")
    private String endNo;


    /**
     * 数量
     **/
    @TableField("number")
    private String number;


    /**
     * 关联公众号id
     **/
    @TableField("acid")
    @NotEmpty(message="请选择公众号名称！")
    private Integer acid;

//    /**
//     * 关联公众号key
//     **/
//    @NotEmpty(message="此公众号没有对应的key，请选择信息完整的公众号！")
//    private String ackey;

    /**
     * 识别码（自动生成）
     **/
    @TableField("distinguish_num")
    private String distinguishNum;

    /**
     * 制卡状态
     **/
    @TableField("state")
    private String state;

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
}
