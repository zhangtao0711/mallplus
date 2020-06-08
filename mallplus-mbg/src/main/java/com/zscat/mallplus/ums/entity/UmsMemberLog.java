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
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author lyn
 * @date 2020-06-06
 * 会员日志/备注
 */
@Data
@TableName("ums_member_log")
public class UmsMemberLog extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 用户id
     **/
    @TableField("member_id")
    private Long memberId;


    /**
     * 区分（0:日志1:备注)
     **/
    @TableField("falg")
    private String falg;


    /**
     * 内容
     **/
    @TableField("content")
    @NotEmpty(message = "内容不能为空！")
    @Length(min=1, max=500, message="内容长度必须在1-500以内！")
    private String content;


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
     * 所属店铺
     **/
    @TableField("store_id")
    private Integer storeId;


    /**
     * 经销商id
     **/
    @TableField("dealer_id")
    private Long dealerId;


}
