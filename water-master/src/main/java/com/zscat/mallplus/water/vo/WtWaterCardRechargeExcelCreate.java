package com.zscat.mallplus.water.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zscat.mallplus.util.BaseEntity;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lyn
 * @date 2020-05-31
 * 充值
 */
@Data
public class WtWaterCardRechargeExcelCreate {


    /**
     * 卡号
     **/
    @TableField("card_no")
    @Pattern(regexp = "^[0-9]*$", message = "卡号只能输入[0-9]的数字!")
    @Length(min=1, max=10, message="卡号长度必须介于 1 和 10 之间")

    @Excel(name = "卡号", width = 15)
    private String cardNo;


    /**
     * 充值金额
     **/
    @Excel(name = "充值金额", width = 15)
    private BigDecimal rechargeMoney;


    /**
     * 实收金额
     **/
    @Excel(name = "实收金额", width = 15)
    private BigDecimal receivedMoney;

}
