package com.zscat.mallplus.set.entity;

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
 * @date 2020-05-26
 * 购买营销功能订单
 */
@Data
@TableName("set_sales_order")
public class SetSalesOrder extends BaseEntity implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /**
     * 订单唯一编号
     **/
    @TableField("logno")
    private String logno;


    /**
     * 经销商id
     **/
    @TableField("dealer_id")
    private Long dealerId;


    /**
     * 功能id
     **/
    @TableField("perssion_id")
    private Long perssionId;


    /**
     * 价格
     **/
    @TableField("price")
    private BigDecimal price;


    /**
     * 月数
     **/
    @TableField("month")
    private Integer month;


    /**
     * 创建时间
     **/
    @TableField("create_time")
    private Date createTime;


    /**
     * 支付状态
     **/
    @TableField("paystatus")
    private Integer paystatus;


    /**
     * 支付时间
     **/
    @TableField("paytime")
    private Date paytime;


    /**
     * 支付类型
     **/
    @TableField("paytype")
    private Integer paytype;

    /**
     * 支付方式 0包年包月，1按次计费
     */
    @TableField("pay_way")
    private String payWay;


    /**
     * 所属店铺
     **/
    @TableField("store_id")
    private Integer storeId;


    /**
     * 店铺名称
     **/
    @TableField("store_name")
    private String storeName;


    /**
     * 实际到账金额
     **/
    @TableField("actual_amount")
    private BigDecimal actualAmount;


    /**
     * 微信生成的预支付会话标识，用于后续接口调用中使用，该值有效期为2小时
     **/
    @TableField("prepay_id")
    private String prepayId;


    /**
     * 此url用于生成支付二维码，然后提供给用户进行扫码支付。
     **/
    @TableField("code_url")
    private String codeUrl;

    /**
     * 积分
     */
    @TableField("integration")
    private Integer integration;

    /**
     * 错误信息
     */
    @TableField("error")
    private String error;


}
