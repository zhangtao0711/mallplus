package com.zscat.mallplus.weixinmp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zscat.mallplus.utils.BaseEntity;

import java.util.Date;
import java.io.Serializable;

/**
 * @author lyn
 * @date 2020-05-22
 * 微信公众号
 */
@Data
@TableName("account_wechats")
public class AccountWechats extends BaseEntity implements Serializable {


    /**
     *
     **/
    @TableId(value = "acid",type = IdType.AUTO)
    private Integer acid;


    /**
     *
     **/
    @TableField("uniacid")
    private Integer uniacid;


    /**
     * 随机生成密钥
     **/
    @TableField("token")
    private String token;


    /**
     * 与公众平台接入设置值一致，必须为英文或者数字，长度为43个字符
     **/
    @TableField("encodingaeskey")
    private String encodingaeskey;


    /**
     * 普通服务号, 2认证服务号
     **/
    @TableField("level")
    private Integer level;


    /**
     * 公众号名称
     **/
    @TableField("name")
    private String name;


    /**
     * 微信帐号
     **/
    @TableField("account")
    private String account;


    /**
     * 原始ID
     **/
    @TableField("original")
    private String original;


    /**
     * 功能介绍
     **/
    @TableField("signature")
    private String signature;


    /**
     * 国家
     **/
    @TableField("country")
    private String country;


    /**
     * 省份
     **/
    @TableField("province")
    private String province;


    /**
     * 城市
     **/
    @TableField("city")
    private String city;


    /**
     * 头像
     **/
    @TableField("logo")
    private String logo;


    /**
     * 二维码
     **/
    @TableField("qr_code")
    private String qrCode;


    /**
     * 最后触发时间
     **/
    @TableField("lastupdate")
    private Date lastupdate;


    /**
     * 微信公众平台后台的AppId
     **/
    @TableField("`key`")
    private String key;


    /**
     * 微信公众平台后台的AppSecret
     **/
    @TableField("secret")
    private String secret;


    /**
     * 风格ID
     **/
    @TableField("styleid")
    private Integer styleid;


    /**
     * 引导素材
     **/
    @TableField("subscribeurl")
    private String subscribeurl;


    /**
     * 自动更新的token码
     **/
    @TableField("auth_refresh_token")
    private String authRefreshToken;


    /**
     * 绑定经销商id
     **/
    @TableField("create_by")
    private Long createBy;


    /**
     * 创建时间
     **/
    @TableField("create_time")
    private Date createTime;

    @TableField("store_id")
    private Integer storeId;

    @TableField("appdomain")
    private String appdomain;

    /**
     * 小程序状态 0关闭 1开启 2已连接 3连接失败
     */
    @TableField("status")
    private Integer status;

}
