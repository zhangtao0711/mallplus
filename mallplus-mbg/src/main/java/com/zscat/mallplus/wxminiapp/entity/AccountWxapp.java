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
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author wang
 * @date 2020-05-23
 * 小程序
 */
@Data
@TableName("account_wxapp")
public class AccountWxapp extends BaseEntity implements Serializable {


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
     *
     **/
    @TableField("token")
//    @NotEmpty(message = "token不能为空！")
//    @Length(min = 32,max = 32,message = "长度必须是32位!")
//    @Pattern(regexp = "^[a-z0-9A-Z]+$",message = "token必须包含数字和字母！")
    private String token;


    /**
     *
     **/
    @TableField("encodingaeskey")
//    @NotEmpty(message = "encodingaeskey不能为空！")
//    @Length(min = 32,max = 32,message = "长度必须是32位!")
//    @Pattern(regexp = "^[a-z0-9A-Z]+$",message = "encodingaeskey必须包含数字和字母！")
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
    @NotEmpty(message = "原始id不能为空！")
    private String original;


    /**
     *
     **/
    @TableField("`key`")
    @NotEmpty(message = "小程序的APPID不能为空！")
    private String key;


    /**
     *
     **/
    @TableField("secret")
    @NotEmpty(message = "小程序的秘钥不能为空！")
    private String secret;


    /**
     *
     **/
    @TableField("name")
    @NotEmpty(message = "小程序的名称不能为空！")
    @Length(min = 1,max = 50,message = "小程序的名称长度必须在1-50之间!")
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
    @NotNull(message = "创建人不能为空！")
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
    @NotEmpty(message = "所属店铺名称不能为空！")
    private String storeName;

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
     * 小程序状态 0关闭 1开启 2已连接 3连接失败
     */
    @TableField("status")
    @NotNull(message = "小程序状态不能为空！")
    private Integer status;

}
