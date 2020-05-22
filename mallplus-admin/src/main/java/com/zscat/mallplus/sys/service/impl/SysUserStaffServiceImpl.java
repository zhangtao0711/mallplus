package com.zscat.mallplus.sys.service.impl;

import com.zscat.mallplus.sys.entity.SysUser;
import com.zscat.mallplus.sys.entity.SysUserStaff;
import com.zscat.mallplus.sys.entity.SysUserVo;
import com.zscat.mallplus.sys.mapper.SysUserStaffMapper;
import com.zscat.mallplus.sys.service.ISysUserStaffService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.sys.util.JsonUtils;
import com.zscat.mallplus.ums.service.RedisService;
import com.zscat.mallplus.util.JsonUtil;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.vo.Rediskey;
import io.swagger.models.auth.In;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author mallplus
 * @date 2020-05-20
 */
@Service
public class SysUserStaffServiceImpl extends ServiceImpl<SysUserStaffMapper, SysUserStaff> implements ISysUserStaffService {

    @Resource
    private SysUserStaffMapper sysUserStaffMapper;
    @Resource
    private BCryptPasswordEncoder passwordEncoder;
    @Resource
    private RedisService redisService;

    @Override
    public Object resetPwd(SysUserStaff staff) {
        if (ValidatorUtils.empty(staff.getPassword())) {
            return new CommonResult().paramFailed("请输入密码");
        }
        staff.setPassword(passwordEncoder.encode(staff.getPassword()));
        return new CommonResult().success(sysUserStaffMapper.updatePasswordById(staff.getPassword(),staff.getId()));
    }

    @Override
    public List<Map<String, Object>> bindWeChant(String value,String username) {
        //获取当前登录用户的小程序的uniacid和storeid
        SysUserVo admin = new SysUserVo();
        String str = redisService.get(String.format(Rediskey.user, username));
        if (str != null){
             admin= JsonUtils.jsonToPojo(str,SysUserVo.class);
        }else {
            return null;
        }
        //TODO 这里须得把公众号哪里关联上才可以使用
        String uniacid = "";
        Integer storeId = admin.getStoreId();
        return sysUserStaffMapper.bindWeChant(uniacid,storeId,value);
    }
}
