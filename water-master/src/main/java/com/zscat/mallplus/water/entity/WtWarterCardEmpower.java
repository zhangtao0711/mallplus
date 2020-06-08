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

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author lyn
 * @date 2020-06-05
 * 水卡授权用户
 */
@Data
@TableName("wt_warter_card_empower")
public class WtWarterCardEmpower extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 卡号
     **/
    @TableField("card_no")
    @NotEmpty(message="必须设定会员卡号！")
    private String cardNo;


    /**
     * 授权人账号
     **/
    @TableField("ums_member_id")
    @NotEmpty(message="必须设定授权人账号！")
    private Long umsMemberId;


    /**
     * 授权人昵称
     **/
    @TableField("ums_member_nickname")
    @NotEmpty(message="必须设定授权人昵称！")
    private String umsMemberNickname;


    /**
     * 被授权人账号
     **/
    @TableField("ums_member_to_id")
    @NotEmpty(message="必须设定被授权人账号！")
    private Long umsMemberToId;


    /**
     * 被授权人昵称
     **/
    @TableField("ums_member_to_nickname")
    @NotEmpty(message="必须设定被授权人昵称！")
    private String umsMemberToNickname;


    /**
     * 与被授权人关系(字典empower_relation)
     **/
    @TableField("empower_relation")
    @NotEmpty(message="必须设定与被授权人关系！")
    private String empowerRelation;


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


    /**
     * 经销商id
     **/
    @TableField("dealer_id")
    private Long dealerId;


}
