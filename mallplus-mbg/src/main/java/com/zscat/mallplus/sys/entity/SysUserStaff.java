package com.zscat.mallplus.sys.entity;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zscat.mallplus.utils.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author mallplus
 * @date 2020-05-20
 * 员工账号
 */
@Data
@TableName("sys_user_staff")
public class SysUserStaff extends BaseEntity implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("uniacid")
    private Integer uniacid;
    @TableField("uid")
    private Integer uid;
    @TableField("groupid")
    private String groupid;
    @TableField("pid")
    private Long pid;
    @TableField("gid")
    private Long gid;

    /**
     *
     **/
    @TableField("username")
    private String username;


    /**
     *
     **/
    @TableField("password")
    private String password;

    /**
     * 昵称
     */
    @TableField("nick_name")
    private String nickname;


    /**
     * 电话
     **/
    @TableField("phone")
    private String phone;


    /**
     * 设备管理组id
     **/
    @TableField("device_group_id")
    private Long deviceGroupId;


    /**
     * 用户名
     **/
    @TableField("staff_name")
    private String staffName;


    /**
     * 省
     **/
    @TableField("province")
    private String province;


    /**
     * 市
     **/
    @TableField("city")
    private String city;


    /**
     * 县
     **/
    @TableField("county")
    private String county;


    /**
     * 创建时间
     **/
    @TableField("create_time")
    private Date createTime;


    /**
     * 最后登录时间
     **/
    @TableField("login_time")
    private Date loginTime;


    /**
     * 帐号启用状态：0->禁用；1->启用
     **/
    @TableField("status")
    private Integer status;


    /**
     * 供应商
     **/
    @TableField("supply_id")
    private Integer supplyId;


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

    @TableField("weixin_openid")
    private String weixinOpenid;
    @TableField("avatar")
    private String avatar;
    @TableField("apply_status")
    private Integer applyStatus;

    //充值额度
    @TableField("pay_max")
    private BigDecimal payMax;

    @TableField("weixin")
    private String weixin;
    @TableField("icon")
    private String icon;
}
