package com.zscat.mallplus.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zscat.mallplus.sys.entity.SysUser;
import com.zscat.mallplus.sys.entity.SysUserVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 后台用户表 Mapper 接口
 * </p>
 *
 * @author zscat
 * @since 2019-04-14
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    SysUserVo selectByUserName(String username);

    public boolean updateUsernameById(String username,Long id);

    boolean updatePhoneById(String newPhone, Long id);
}
