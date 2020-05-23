package com.zscat.mallplus.sys.mapper;


import com.zscat.mallplus.sys.entity.SysUserStaff;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zscat.mallplus.sys.entity.SysUserVo;
import com.zscat.mallplus.ums.entity.UmsMember;

import java.util.List;
import java.util.Map;

/**
* @author mallplus
* @date 2020-05-20
*/
public interface SysUserStaffMapper extends BaseMapper<SysUserStaff> {

    public SysUserVo selectByUserName(String username);

    int updatePasswordById(String password, Long id);

    List<Map<String,Object>> bindWeChant(String uniacid, Integer storeId,String value);
}
