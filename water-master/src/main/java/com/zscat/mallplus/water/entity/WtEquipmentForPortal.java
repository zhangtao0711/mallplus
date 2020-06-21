package com.zscat.mallplus.water.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zscat.mallplus.util.BaseEntity;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lyn
 * @date 2020-05-22
 * 用户小程序关联售水
 */
@Data
public class WtEquipmentForPortal extends BaseEntity implements Serializable {
    /**
     * 取水0售水1
     **/
    private String getType;
    /**
     * 设备号
     **/
    private String eqcode;
    /**
     * 经销商id
     **/
    private Long dealerId;

    /**
     * 会员id
     **/
    private Long umsMemberId;

    /**
     * 设备状态【0未安装2限制使用3正常使用】
     **/
    private String stateEq;

    /**
     * 会员卡
     **/
    private String waterCardNo;
    /**
     * 卡状态 card_type（0实体卡1虚拟卡）
     **/
    private String cardType;

    /**
     * 卡状态（0正常1复制卡2挂失卡3删除卡4非经销商卡） 字典water_code_state
     **/
    private String state;
    /**
     * 体验金额
     **/
    private BigDecimal experienceMoney;
    /**
     * 体验到期日
     **/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date experienceEndData;

    /**
     * 卡内余额
     **/
    private BigDecimal cardMoney;
    /**
     * 赠送金额
     **/
    private BigDecimal giveMoney;
    /**
     * 删除状态(0-正常,1-已删除)
     **/
    private String delFlag;
}
