package com.zscat.mallplus.ums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.oms.mapper.OmsOrderMapper;
import com.zscat.mallplus.oms.vo.OrderStstic;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.ums.entity.UmsMemberLevel;
import com.zscat.mallplus.ums.mapper.UmsMemberMapper;
import com.zscat.mallplus.ums.service.IUmsMemberLevelService;
import com.zscat.mallplus.ums.service.IUmsMemberService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Service
public class UmsMemberServiceImpl extends ServiceImpl<UmsMemberMapper, UmsMember> implements IUmsMemberService {

    @Resource
    private IUmsMemberLevelService memberLevelService;
    @Resource
    private OmsOrderMapper omsOrderMapper;
    @Resource
    private UmsMemberMapper memberMapper;

    @Override
    public void updataMemberOrderInfo() {
        List<OrderStstic> orders = omsOrderMapper.listOrderGroupByMemberId();
        List<UmsMemberLevel> levelList = memberLevelService.list(new QueryWrapper<UmsMemberLevel>().orderByDesc("price"));
        for (OrderStstic o : orders) {
            UmsMember member = new UmsMember();
            member.setId(o.getMemberId());
            member.setBuyMoney(o.getTotalPayAmount());
            for (UmsMemberLevel level : levelList) {
                if (member.getBuyMoney().compareTo(level.getPrice()) >= 0) {
                    member.setMemberLevelId(level.getId());
                    member.setMemberLevelName(level.getName());
                    break;
                }
            }
            member.setBuyCount(o.getTotalCount());
            memberMapper.updateById(member);
        }
    }

    @Override
    public  Map memberMonthStatic(@Param("date") String date){
        return memberMapper.memberMonthStatic(date);
    }

    @Override
    public List<UmsMember> getMemberByLabelId(Long labelId) {
        return memberMapper.getMemberByLabelId(labelId);
    }

    /**
     * 授权人账号和授权人昵称是否一致
     * @param umsMemberId
     * @param umsMemberNickname
     * @param ums_status_off
     * @param storeId
     * @return
     */
    public boolean checkUmsIdNickname(Long umsMemberId, String umsMemberNickname, Integer ums_status_off, Integer storeId){
        if(memberMapper.getUmsIdNickname(umsMemberId, umsMemberNickname, ums_status_off, storeId)!=null){
            return true;
        }else{
            return false;
        }
    }

}
