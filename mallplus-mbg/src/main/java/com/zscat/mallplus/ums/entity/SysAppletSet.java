package com.zscat.mallplus.ums.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zscat.mallplus.ums.vo.Receivers;
import com.zscat.mallplus.utils.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author zscat
 * @since 2019-06-15
 */
@Data
@TableName("sys_applet_set")
public class SysAppletSet extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String appid;

    private String appsecret;

    private String mchid;

    @TableField("pay_sign_key")
    private String paySignKey;

    private String certname;

    private String notifyurl;

    /**
     * 下单通知模版
     */
    private String templateid1;

    /**
     * 支付成功模版
     */
    private String templateid2;

    private String templateid3;

    private String templateid4;

    private String templateid5;

    private String templateid6;

    private Integer storeId;
    @TableId(value = "user_id")
    private Long userId;

    private Integer levelId;
    @NotNull(message = "商户号类型不能为空！")
    private Integer selfType;
    @NotNull(message = "上级经销商不能为空！")
    private Long parentUserId;
    private String receiptAccount;
    private BigDecimal firstSeparate;
    private BigDecimal secondSeparate;
    private BigDecimal thirdSeparate;

    @TableField(exist = false)
    private List<Receivers> receiversList;

    @TableField(exist = false)
    private String receivers;
}
