package com.zscat.mallplus.water.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 会员实体卡
 */
@Data
public class WtWaterCardExport {
    @Excel(name = "会员等级", width = 15)
    private String memberLevelName;
    /**
     * 绑定用户名称
     **/
    private transient String umsMemberName;

    /**
     * 实体卡号
     */
    @Excel(name = "会员卡号", width = 15)
    private transient String cardNo;
    /**
     * 余额
     */
    @Excel(name = "余额", width = 15)
    private transient BigDecimal cardMoney;
    /**
     * 体验金额
     **/
    @Excel(name = "体验金额", width = 15)
    private transient BigDecimal experienceMoney;
    /**
     * 体验到期日
     **/
    @Excel(name = "体验到期日", width = 15)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private transient Date experienceEndData;

    /**
     * 赠送金额
     **/
    @Excel(name = "赠送金额", width = 15)
    private transient BigDecimal giveMoney;
    /**
     * 公众号名称
     */
    @Excel(name = "公众号名称", width = 15)
    private transient String uniacName;

    /**
     * 售卡人名
     **/
    private transient Long saleByName;
    /**
     * 设备id
     */
    private transient String eqcode;
    /**
     * 设备地址
     */
    private transient String eqAddress;
}
