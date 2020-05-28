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

import java.io.Serializable;

/**
 * @author wang
 * @date 2020-05-26
 * 经销商交叉授权
 */
@Data
@TableName("sys_merchant_audit")
public class SysMerchantAudit extends BaseEntity implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 发起商家
     **/
    @TableField("initiate_id")
    private Long initiateId;


    /**
     * 授权商家
     **/
    @TableField("dealer_id")
    private Long dealerId;

    @TableField("username")
    private String username;


    /**
     * 消费授权
     **/
    @TableField("sale")
    private String sale;


    /**
     * 组内消费
     **/
    @TableField("group_sale")
    private String groupSale;


    /**
     * 状态
     **/
    @TableField("status")
    private String status;


    /**
     * 更新时间
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
