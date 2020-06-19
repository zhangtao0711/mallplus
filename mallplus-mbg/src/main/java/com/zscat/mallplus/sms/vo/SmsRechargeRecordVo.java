package com.zscat.mallplus.sms.vo;

import com.zscat.mallplus.utils.BaseEntity;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
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
    @NotNull(message = "会员不能为空！")
    private Long memberId;


    /**
     * 会员名称
     **/
    @NotEmpty(message = "会员名称不能为空!")
    private String memberName;


    /**
     * 套餐id
     **/
    @NotNull(message = "套餐不能为空！")
    private Long packageId;


    /**
     * 代充值员工id
     **/
    @NotNull(message = "代充值员工不能为空！")
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
     * 实体卡或者虚拟卡的id
     */
    @NotNull(message = "实体卡或者虚拟卡id")
    private Long cardId;


    /**
     * 卡号
     **/
    @NotEmpty(message = "卡号不能为空！")
    @Length(max = 9,message = "卡号最大为9位")
    private String cardNo;


    /**
     * 状态
     **/
    @NotNull(message = "状态不能为空！")
    private Integer status;


    /**
     * 赠品名称
     **/
    private String gift;


    /**
     * 充值金额
     **/
    @NotNull(message = "充值金额不能为空！")
    @Digits(integer = 10,fraction = 2,message = "充值金额不能超过最大限额！")
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
    @NotNull(message = "充值类型不能为空！")
    @Min(value = 1,message = "充值类型值不正确！")
    @Max(value = 3,message = "充值类型值不正确！")
    private Integer rechargeType;

    /**
     * 小程序id
     */
    @NotNull(message = "小程序的id不能为空！")
    @Min(value = 1,message = "小程序id值不正确！")
    private Integer uid;

    /**
     * 获取小程序的openID
     */
    @NotEmpty(message = "用户的openID不能为空！")
    private String openId;

    /**
     * 唯一订单号
     */
    private String outTradeNo;

    /**
     * 经销商id
     **/
    @NotNull(message = "经销商不能为空！")
    private Long dealerId;


    /**
     * 所属店铺
     **/
    @NotNull(message = "门店信息不能为空！")
    private Integer storeId;
}
