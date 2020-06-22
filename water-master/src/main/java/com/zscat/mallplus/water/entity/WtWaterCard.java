package com.zscat.mallplus.water.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zscat.mallplus.util.BaseEntity;
import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.io.Serializable;

/**
 * @author lyn
 * @date 2020-05-30
 * 水卡
 */
@Data
@TableName("wt_water_card")
public class WtWaterCard extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 生成id
     **/
    @TableField("card_create_id")
    private Long cardCreateId;


    /**
     * 卡号
     **/
    @TableField("card_no")
    private String cardNo;


    /**
     * 二维码
     **/
    @TableField("qr_code")
    private String qrCode;

    /**
     * 绑定用户id
     **/
    @TableField("ums_member_id")
    private Long umsMemberId;
    /**
     * 卡状态 card_type（0实体卡1虚拟卡）
     **/
    @TableField("card_type")
    private String cardType;

    /**
     * 卡状态（0正常1复制卡2挂失卡3删除卡4非经销商卡） 字典water_code_state
     **/
    @TableField("state")
    private String state;
    /**
     * 体验金额
     **/
    @TableField("experience_money")
    @DecimalMin(value="0.01")
    @DecimalMax(value="99999999.99")
    @Digits(integer=8, fraction=2,message="体验金额不能设定大于99999999.99,小数位数只支持2位。")
    private BigDecimal experienceMoney;
    /**
     * 体验到期日
     **/
    @TableField("experience_end_data")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date experienceEndData;

    /**
     * 卡内余额
     **/
    @TableField("card_money")
    @DecimalMin(value="0.01")
    @DecimalMax(value="99999999.99")
    @Digits(integer=8, fraction=2,message="卡内余额不能设定大于99999999.99,小数位数只支持2位。")
    private BigDecimal cardMoney;
    /**
     * 赠送金额
     **/
    @TableField("give_money")
    @DecimalMin(value="0.01")
    @DecimalMax(value="99999999.99")
    @Digits(integer=8, fraction=2,message="卡内余额不能设定大于99999999.99,小数位数只支持2位。")
    private BigDecimal giveMoney;

    /**
     * 备注
     **/
    @TableField("remarks")
    @Length(min=0, max=255, message="备注长度必须在255以内!")
    private String remarks;

    /**
     * 创建日期
     **/
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    /**
     * 创建人
     **/
    @TableField("create_by")
    private Long createBy;

    /**
     * 更新日期
     **/
    @TableField("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


    /**
     * 更新人
     **/
    @TableField("update_by")
    private Long updateBy;


    /**
     * 删除状态(1-正常,0-已删除)
     **/
    @TableField("del_flag")
    @TableLogic
    private String delFlag;

    /**
     * 经销商
     **/
    @TableField("dealer_id")
    private Long dealerId;

    /**
     * 水卡状态销售状态（0未售出1已售出）
     **/
    @TableField("sale_state")
    private String saleState;
    /**
     * 水卡状态激活状态(0-未激活,1-激活)
     **/
    @TableField("activation_state")
    private String activationState;
    /**
     * 售卡日期
     **/
    @TableField("sale_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date saleTime;
    /**
     * 售卡人
     **/
    @TableField("sale_by")
    private Long saleBy;

    /**
     * 推荐人
     **/
    @TableField("ums_member_referrer_id")
    private String umsMemberReferrerId;


    /**
     * 关联公众号id
     **/
    @TableField("acid")
    private Integer acid;
    /**
     * 单次消费限额
     **/
    @TableField("xfxe_limit")
    @DecimalMin(value="0.01")
    @DecimalMax(value="99.99")
    @Digits(integer=8, fraction=2,message="单次消费限额不能设定大于99.99,小数位数只支持2位。")
    private BigDecimal xfxeLimit;

    /**
     * 消费次数限制（天）
     **/
    @TableField("xf_num_limit")
    @Range(min=1,max = 99,message = "单次消费限额（分）只能输入1-99的正整数！")
    private Integer xfNumLimit;

    /**
     * 绑定用户名称
     **/
    private transient String umsMemberName;
    /**
     * 登录名称
     */
    private transient String weixinOpenid;
    /**
     * 公众号名称
     */
    private transient String uniacName;
    /**
     * 会员等级
     */
    private transient String memberLevelName;
    /**
     * 设备id
     */
    private transient String eqcode;
    /**
     * 设备地址
     */
    private transient String eqAddress;
    /**
     * 售卡人名
     **/
    private transient Long saleByName;
    /**
     * 推荐人名
     **/
    private transient String umsMemberReferrerName;

    /**
     * 是否绑定（0未绑定1已绑定）
     **/
    private transient String binding;
    /**
     * 卡余额最低
     **/
    @DecimalMin(value="0.01")
    @DecimalMax(value="99999999.99")
    @Digits(integer=8, fraction=2,message="卡内余额不能设定大于99999999.99,小数位数只支持2位。")
    private transient BigDecimal cardMoneyMin;
    /**
     * 卡余额最高
     **/
    @DecimalMin(value="0.01")
    @DecimalMax(value="99999999.99")
    @Digits(integer=8, fraction=2,message="卡内余额不能设定大于99999999.99,小数位数只支持2位。")
    private transient BigDecimal cardMoneyMax;

}
