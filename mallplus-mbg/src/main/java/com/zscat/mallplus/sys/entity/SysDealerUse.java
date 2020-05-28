package com.zscat.mallplus.sys.entity;

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

import java.math.BigDecimal;
import java.io.Serializable;

/**
 * @author wang
 * @date 2020-05-25
 * 经销商的应用权限
 */
@Data
@TableName("sys_dealer_use")
public class SysDealerUse extends BaseEntity implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 经销商的id
     **/
    @TableField("dealer_id")
    private Long dealerId;


    /**
     * 权限id，逗号拼接
     **/
    @TableField("permission_ids")
    private String permissionIds;


    /**
     * 权限名称
     **/
    @TableField("permission_names")
    private String permissionNames;


    /**
     * 是否开通共享水卡
     **/
    @TableField("is_share_card")
    private String isShareCard;


    /**
     * 结算价格
     **/
    @TableField("settle_money")
    private BigDecimal settleMoney;


    /**
     * 共享方式
     **/
    @TableField("share_way")
    private String shareWay;


    /**
     * 共享商户id，逗号隔开
     **/
    @TableField("share_dealer_ids")
    private String shareDealerIds;


    /**
     * 共享商户名称
     **/
    @TableField("share_dealer_names")
    private String shareDealerNames;


    /**
     * 是否开通商户审核权限
     **/
    @TableField("is_dealer_audit")
    private String isDealerAudit;


    /**
     * 是否开通商户关联公众号，小程序
     **/
    @TableField("is_relation")
    private String isRelation;


    /**
     * 绑定经销商id
     **/
    @TableField("create_by")
    private Long createBy;


    /**
     * 创建时间
     **/
    @TableField("create_time")
    private Date createTime;


    /**
     *
     **/
    @TableField("update_by")
    private Long updateBy;


    /**
     *
     **/
    @TableField("update_time")
    private Date updateTime;

    /**
     * 所属店铺
     **/
    @TableField("store_id")
    private Integer storeId;


    /**
     *
     **/
    @TableField("store_name")
    private String storeName;
}
