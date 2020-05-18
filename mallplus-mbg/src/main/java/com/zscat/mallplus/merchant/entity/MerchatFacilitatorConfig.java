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
 * 服务商配置
 */
@Data
@TableName("merchat_facilitator_config")
public class MerchatFacilitatorConfig implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /**
     * 用于证书解密的密钥
     **/
    @TableField("apiV3key")
    @NotEmpty(message = "用于证书解密的密钥不能为空！")
    private String apiv3key;


    /**
     * 商户号
     **/
    @TableField("mch_id")
    @NotEmpty(message = "商户号不能为空！")
    private String mchId;


    /**
     * 商户证书路径
     **/
    @TableField("apiclient_cert")
    @NotEmpty(message = "商户证书路径不能为空！")
    private String apiclientCert;


    /**
     * 商户私钥路径
     **/
    @TableField("private_key_path")
    @NotEmpty(message = "商户私钥路径不能为空！")
    private String privateKeyPath;


    /**
     * 商户证书路径
     **/
    @TableField("apiclient_cert_p12")
    @NotEmpty(message = "商户证书路径不能为空！")
    private String apiclientCertP12;


    /**
     * 微信支付平台证书路径
     **/
    @TableField("public_key_path")
    @NotEmpty(message = "微信支付平台证书路径不能为空！")
    private String publicKeyPath;


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
