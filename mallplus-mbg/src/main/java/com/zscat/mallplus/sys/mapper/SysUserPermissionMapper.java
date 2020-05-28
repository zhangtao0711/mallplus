package com.zscat.mallplus.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zscat.mallplus.sys.entity.SysPermission;
import com.zscat.mallplus.sys.entity.SysUserPermission;

import java.util.List;

/**
 * <p>
 * 后台用户和权限关系表(除角色中定义的权限以外的加减权限) Mapper 接口
 * </p>
 *
 * @author zscat
 * @since 2019-04-14
 */
public interface SysUserPermissionMapper extends BaseMapper<SysUserPermission> {

    List<SysUserPermission> selectPerms(Long id);

    List<SysPermission> listPerms(Long id);

    List<String> selectMenuPerms(Long id);

}
