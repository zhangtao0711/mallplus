package com.zscat.mallplus.ums.entity;

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
 * @date 2020-06-06
 * 用户推荐列表
 */
@Data
@TableName("ums_member_recommend")
public class UmsMemberRecommend extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 推荐人id
     **/
    @TableField("member_id")
    private Long memberId;


    /**
     * 被推荐人id
     **/
    @TableField("member_recommend_id")
    private Long memberRecommendId;

    /**
     * 用户名
     */
    private String username;
    /**
     * 注册时间
     */
    private Date createTime;
    /**
     * 购买时间
     */
    private Date paymentTtime;

    /**
     * 奖励
     */
    private String reward;
}
