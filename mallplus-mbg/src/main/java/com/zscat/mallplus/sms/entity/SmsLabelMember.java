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

import java.io.Serializable;

/**
 * @author lyn
 * @date 2020-05-30
 * 批量开卡
 */
@Data
@TableName("sms_label_member")
public class SmsLabelMember extends BaseEntity implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 标签id
     **/
    @TableField("label_id")
    private Long labelId;


    /**
     * 会员id
     **/
    @TableField("member_id")
    private Long memberId;
    @TableField("tag_id")
    private Long tagId;

    @TableField("open_id")
    private String openId;
}
