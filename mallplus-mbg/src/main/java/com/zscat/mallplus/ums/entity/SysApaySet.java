package com.zscat.mallplus.ums.entity;

import io.swagger.models.auth.In;
import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zscat.mallplus.utils.BaseEntity;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wang
 * @date 2020-06-07
 * 经销商的收款设置
 */
@Data
@TableName("sys_apay_set")
public class SysApaySet extends BaseEntity implements Serializable {


    /**
     * 经销商id
     **/
    @TableField("user_id")
    private Long userId;


    /**
     * 是否开启0否1是
     **/
    @TableField("enable")
    private Integer enable;


    /**
     * APPID
     **/
    @TableField("mch_appid")
    private String mchAppid;


    /**
     * 商户号
     **/
    @TableField("mch_id")
    private String mchId;


    /**
     * 证书.pem
     **/
    @TableField("cert_path")
    private String certPath;


    /**
     * 秘钥证书
     **/
    @TableField("key_path")
    private String keyPath;


    /**
     * 证书.p12
     **/
    @TableField("apiclient_cert")
    private String apiclientCert;

    /**
     * 上传证书的目录
     */
    @TableField("cert_catalog")
    private String certCatalog;


    /**
     * 创建时间
     **/
    @TableField("create_time")
    private Date createTime;


    /**
     * 所属店铺
     **/
    @TableField("store_id")
    private Integer storeId;


}
