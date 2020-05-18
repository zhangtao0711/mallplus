package com.zscat.mallplus.merchant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 * @author mallplus
 * @date 2020-05-14
 * 银行配置信息
 */
@Data
@TableName("merchant_bank_info")
public class MerchantBankInfo implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /**
     * 银行名
     **/
    @TableField("bank_name")
    @NotEmpty(message = "银行名称不能为空！")
    private String bankName;


    /**
     * 0数字1符号+数字
     **/
    @TableField("card_format")
    private Integer cardFormat;


    /**
     * 对私卡号长度
     **/
    @TableField("private_length")
    private String privateLength;


    /**
     * 对公账户长度
     **/
    @TableField("public_length")
    private String publicLength;


    /**
     * 创建人
     **/
    @TableField("create_by")
    private Long createBy;


    /**
     * 创建时间
     **/
    @TableField("create_time")
    private Date createTime;


    /**
     * 更新人
     **/
    @TableField("update_by")
    private Long updateBy;


    /**
     * 更新时间
     **/
    @TableField("update_time")
    private Date updateTime;


}
