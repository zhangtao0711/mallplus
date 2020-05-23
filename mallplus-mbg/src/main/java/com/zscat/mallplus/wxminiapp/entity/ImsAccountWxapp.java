package com.zscat.mallplus.wxminiapp.entity;

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
 * @date 2020-05-23
 * 小程序
 */
@Data
@TableName("ims_account_wxapp")
public class ImsAccountWxapp extends BaseEntity implements Serializable {


    /**
     *
     **/
    @TableField("acid")
    private Integer acid;


    /**
     *
     **/
    @TableField("uniacid")
    private Integer uniacid;


    /**
     *
     **/
    @TableField("token")
    private String token;


    /**
     *
     **/
    @TableField("encodingaeskey")
    private String encodingaeskey;


    /**
     *
     **/
    @TableField("level")
    private Integer level;


    /**
     *
     **/
    @TableField("account")
    private String account;


    /**
     *
     **/
    @TableField("original")
    private String original;


    /**
     *
     **/
    @TableField("key")
    private String key;


    /**
     *
     **/
    @TableField("secret")
    private String secret;


    /**
     *
     **/
    @TableField("name")
    private String name;


    /**
     *
     **/
    @TableField("appdomain")
    private String appdomain;


    /**
     *
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


    /**
     * 所属店铺
     **/
    @TableField("store_id")
    private Integer storeId;


    /**
     *
     **/
    @TableField("store_name")
    private String storeName;


}
