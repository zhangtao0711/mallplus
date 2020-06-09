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

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author lyn
 * @date 2020-05-22
 * SIM卡第三方appkey
 */
@Data
@TableName("wt_sim_url_info")
public class WtSimUrlInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * appid
     **/
    @TableField("appid")
    @NotEmpty
    @Length(min=1, max=100, message="appid长度必须介于 1 和 100 之间!")
    private String appid;


    /**
     * appkey
     **/
    @TableField("appkey")
    @NotEmpty
    @Length(min=1, max=255, message="appkey长度必须介于 1 和 255 之间!")
    private String appkey;


    /**
     * 接口地址
     **/
    @TableField("url")
    @NotEmpty
    @Length(min=1, max=500, message="接口地址长度必须介于 1 和 500 之间!")
    private String url;

    /**
     * 运营商名称
     **/
    @TableField("operators")
    @NotEmpty
    @Length(min=1, max=100, message="运营商名称长度必须介于 1 和 100 之间!")
    private String operators;
    /**
     * 套餐编码
     **/
    @TableField("product_id")
    @NotEmpty
    @Length(min=1, max=100, message="套餐编码长度必须介于 1 和 100 之间!")
    private String productId;

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
