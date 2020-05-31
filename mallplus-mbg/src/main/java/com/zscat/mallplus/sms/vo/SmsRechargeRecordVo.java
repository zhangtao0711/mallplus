package com.zscat.mallplus.sms.vo;

import com.zscat.mallplus.utils.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author wang
 * @date 2020-05-29
 * 充值记录
 */
@Data
public class SmsRechargeRecordVo extends BaseEntity implements Serializable {


    private Long id;


    /**
     * 会员id
     **/
    private Long memberId;


    /**
     * 会员名称
     **/
    private String memberName;


    /**
     * 套餐id
     **/
    private Long packageId;


    /**
     * 代充值员工id
     **/
    private Long replaceId;


    /**
     * 创建时间
     **/
    private Date createTime;


    /**
     * 购买时间
     **/
    private Date buyTime;


    /**
     * 支付方式
     **/
    private Integer payWay;


    /**
     * 卡号
     **/
    private String cardNo;


    /**
     * 状态
     **/
    private Integer status;


    /**
     * 赠品名称
     **/
    private String gift;


    /**
     * 充值金额
     **/
    private BigDecimal payFee;


    /**
     * 实收金额
     **/
    private BigDecimal actualFee;


    /**
     * 实际到账金额
     **/
    private BigDecimal actualAccount;


    /**
     * 奖励金额
     **/
    private BigDecimal reward;


    /**
     * 充值类型 1代客2用户3后台
     **/
    private Integer rechargeType;

    /**
     * 小程序id
     */
    private Integer uid;

    /**
     * 获取小程序的openID
     */
    private String openId;

    /**
     * 唯一订单号
     */
    private String outTradeNo;

    /**
     * 经销商id
     **/
    private Long dealerId;


    /**
     * 所属店铺
     **/
    private Integer storeId;
}
