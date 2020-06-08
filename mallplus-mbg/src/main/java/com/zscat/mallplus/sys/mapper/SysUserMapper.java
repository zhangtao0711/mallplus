package com.zscat.mallplus.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zscat.mallplus.sys.entity.SysUser;
import com.zscat.mallplus.sys.entity.SysUserVo;
import com.zscat.mallplus.ums.vo.SysDealerVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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

    List<Map<String,Object>> listDealer(@Param("level")Integer level, @Param("value") String value,
                                        @Param("storeId")Integer storeId);
}
