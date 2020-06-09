package com.zscat.mallplus.ums.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.ums.vo.UmsMemberSelect;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
public interface IUmsMemberService extends IService<UmsMember> {

    void updataMemberOrderInfo();

    Map memberMonthStatic( String date);

    List<UmsMember> getMemberByLabelId(Long labelId);

    //授权人账号和授权人昵称是否一致
    boolean checkUmsIdNickname(Long umsMemberId, String umsMemberNickname, Integer ums_status_off, Integer storeId);
    //根据条件查询所有会员表列表
    IPage<Map<String, Object>> selectMember(Page<Map<String,Object>> page, UmsMember entity);

    UmsMember getById(Long id);
    //保存信息
    boolean save(UmsMember umsMember);
    //更新信息
    boolean updateById(UmsMember umsMember);
    //删除用户标签
    boolean removeLabel(Long id, Long umsMemberId);
    //高级查询
    IPage<Map<String, Object>> selectSenior(Page<Map<String,Object>> mapPage, UmsMemberSelect entity);
}
