package com.zscat.mallplus.sys.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.zscat.mallplus.utils.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 后台用户角色表
 * </p>
 *
 * @author zscat
 * @since 2019-04-14
 */
@Data
@TableName("sys_role")
public class SysRole extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "role_id", type = IdType.AUTO)
    private Long roleId;

    /**
     * 名称
     */
    private String roleName;

    @TableField(exist = false)
    private boolean checked = false;

    /**
     * 角色权限字符串
     */
    private String roleKey;

    /**
     * 数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）
     */
    @TableField("data_scope")
    private String dataScope;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    @TableLogic
    private String delFlag;
    private String createBy;
    private String updateBy;
    private Date updateTime;
    private String remark;


    /**
     * 启用状态：0->禁用；1->启用
     */
    private Integer status;

    private Integer roleSort;
    @TableField(exist = false)
    private String menuIds;

    @TableField("store_id")
    private Integer storeId;
    @TableField("store_name")
    private String storeName;

}
