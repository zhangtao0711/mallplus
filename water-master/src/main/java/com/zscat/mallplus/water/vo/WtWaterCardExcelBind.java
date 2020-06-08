package com.zscat.mallplus.water.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zscat.mallplus.util.BaseEntity;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lyn
 * @date 2020-05-30
 * 水卡
 */
@Data
public class WtWaterCardExcelBind {

    /**
     * 卡号
     **/
    @TableField("card_no")
    private String cardNo;

    /**
     * 绑定用户id
     **/
    @TableField("ums_member_id")
    private Long umsMemberId;


}
