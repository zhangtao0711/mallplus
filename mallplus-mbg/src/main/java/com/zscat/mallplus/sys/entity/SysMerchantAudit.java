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
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
    @NotNull(message = "发起商家不能为空！")
    private Long initiateId;


    /**
     * 授权商家
     **/
    @TableField("dealer_id")
    @NotNull(message = "授权商家不能为空！")
    private Long dealerId;

    @TableField("username")
    @NotEmpty(message = "用户名不能为空！")
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
    @NotEmpty(message = "状态不能为空！")
    @Length(min = 1,max = 1,message = "状态值传输错误！")
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
    @NotNull(message = "所属店铺不能为空！")
    private Integer storeId;


    /**
     *
     **/
    @TableField("store_name")
    @NotEmpty(message = "店铺名称不能为空！")
    private String storeName;


}
