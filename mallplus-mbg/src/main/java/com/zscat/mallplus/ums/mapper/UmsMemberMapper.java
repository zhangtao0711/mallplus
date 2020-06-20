package com.zscat.mallplus.ums.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.ums.vo.UmsMemberSelect;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
public interface UmsMemberMapper extends BaseMapper<UmsMember> {

    List<UmsMember> listByDate(@Param("date") String date, @Param("type") Integer type);

    Map memberMonthStatic(@Param("date") String date);

    UmsMember selectByUsernameLeader(String username);

    UmsMember selectByUsernameStaff(String username);

    List<UmsMember> getMemberByLabelId(Long labelId);
    //根据账号和昵称获取用户信息
    UmsMember getUmsIdNickname(@Param("umsMemberId")Long umsMemberId
            , @Param("umsMemberNickname")String umsMemberNickname,@Param("status_off") Integer ums_status_off, @Param("storeId")Integer storeId);
    //根据条件查询所有会员表列表
    List<Map<String,Object>> selectMember(Page<Map<String,Object>> page, @Param("entity")UmsMember entity);
//    //查询会员详情
//    UmsMember getById(Long id);
    //删除用户标签
    boolean removeLabel(@Param("id")Long id, @Param("umsMemberId")Long umsMemberId);
    //高级查询
    List<Map<String,Object>> selectSenior(Page<Map<String,Object>> page, @Param("entity")UmsMemberSelect entity);

    Long getByOpenid(@Param("openId") String openId);

    List<String> queryIdList(String phone);
}
