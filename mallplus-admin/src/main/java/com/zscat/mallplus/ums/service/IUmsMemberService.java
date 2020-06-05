package com.zscat.mallplus.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.ums.entity.UmsMember;
import org.apache.ibatis.annotations.Param;

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
}
