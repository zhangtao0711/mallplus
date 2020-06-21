package com.zscat.mallplus.water.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 会员虚拟卡
 */
@Data
public class WtWaterCardExportVirtual {
    @Excel(name = "会员等级", width = 15)
    private String memberLevelName;
    /**
     * 绑定用户名称
     **/
    private transient String umsMemberName;

    /**
     * 卡号
     */
    @Excel(name = "会员卡号", width = 15)
    private transient String cardNo;
    /**
     * 设备id
     */
    private transient String eqcode;
    /**
     * 设备地址
     */
    private transient String eqAddress;
    /**
     * 余额
     */
    @Excel(name = "余额", width = 15)
    private transient BigDecimal cardMoney;

    /**
     * 赠送金额
     **/
    @Excel(name = "赠送金额", width = 15)
    private transient BigDecimal giveMoney;

    /**
     * 推荐人
     **/
    @Excel(name = "推荐人", width = 15)
    private String umsMemberReferrerId;
    /**
     * 公众号名称
     */
    @Excel(name = "公众号名称", width = 15)
    private transient String uniacName;

}
