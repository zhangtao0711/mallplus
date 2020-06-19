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

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author wang
 * @date 2020-05-25
 * 小程序版本
 */
@Data
@TableName("account_wxapp_version")
public class AccountWxappVersion extends BaseEntity implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     *微信小程序id
     **/
    @TableField("uniacid")
    @NotNull(message = "微信小程序id不能为空！")
    private Integer uniacid;

    /**
     * 经销商id
     */
    @TableField("dealer_id")
    @NotNull(message = "经销商不能为空！")
    private Long dealerId;


    /**
     *微信小程序名称 这是那个上传到草稿箱的模板id，写死的
     **/
    @TableField("name")
    private String name;


    /**
     * 版本描述
     **/
    @TableField("version_described")
    private String versionDescribed;


    /**
     * 版本号
     **/
    @TableField("version")
    @NotEmpty(message = "版本号不能为空！")
    @Pattern(regexp = "d+(.\\d+){0,2}",message = "请输入正确的版本号格式！")
    private String version;


    /**
     * 应用插件逗号隔开
     **/
    @TableField("plugs")
    private String plugs;


    /**
     * 绑定经销商id
     **/
    @TableField("create_by")
    @NotNull(message = "创建者不能为空！")
    private Long createBy;


    /**
     * 创建时间
     **/
    @TableField("create_time")
    private Date createTime;


    /**
     *
     **/
    @TableField("update_by")
    private Long updateBy;


    /**
     *
     **/
    @TableField("update_time")
    private Date updateTime;


    /**
     * 所属店铺
     **/
    @TableField("store_id")
    @NotNull(message = "门店不能为空！")
    private Integer storeId;


    /**
     *
     **/
    @TableField("store_name")
    @NotEmpty(message = "门店名称不能为空！")
    private String storeName;


}
