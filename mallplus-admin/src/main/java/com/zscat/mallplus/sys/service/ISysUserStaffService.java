package com.zscat.mallplus.sys.service;

import com.zscat.mallplus.sys.entity.SysUserStaff;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
* @author mallplus
* @date 2020-05-20
*/

public interface ISysUserStaffService extends IService<SysUserStaff> {

    Object resetPwd(SysUserStaff staff);

    List<Map<String,Object>> bindWeChant(String value,String username);
}
