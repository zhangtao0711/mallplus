package com.zscat.mallplus.jifen.entity;

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
 * @date 2020-05-29
 * 经销商积分记录
 */
@Data
@TableName("jifen_dealer_integration_change_history")
public class JifenDealerIntegrationChangeHistory extends BaseEntity implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     *
     **/
    @TableField("dealer_id")
    private Long dealerId;


    /**
     *
     **/
    @TableField("create_time")
    private Date createTime;


    /**
     * 改变类型：0->增加；1->减少
     **/
    @TableField("change_type")
    private Integer changeType;


    /**
     * 积分改变数量
     **/
    @TableField("change_count")
    private Integer changeCount;


    /**
     * 水费改变量
     **/
    @TableField("change_balance")
    private BigDecimal changeBalance;


    /**
     * 操作人员
     **/
    @TableField("operate_man")
    private String operateMan;


    /**
     * 操作备注
     **/
    @TableField("operate_note")
    private String operateNote;


    /**
     * 积分来源：0->购物；1->管理员修改
     **/
    @TableField("source_type")
    private Integer sourceType;


    /**
     * 所属店铺
     **/
    @TableField("store_id")
    private Integer storeId;


    /**
     * 积分余额
     **/
    @TableField("integration")
    private Integer integration;

    /**
     * 当前剩余余额
     */
    @TableField("balance")
    private BigDecimal balance;


}
