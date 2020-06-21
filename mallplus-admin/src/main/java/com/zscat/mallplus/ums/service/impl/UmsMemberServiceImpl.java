package com.zscat.mallplus.ums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.oms.mapper.OmsOrderMapper;
import com.zscat.mallplus.oms.vo.OrderStstic;
import com.zscat.mallplus.sms.entity.SmsLabelMember;
import com.zscat.mallplus.sms.entity.SmsLabelSet;
import com.zscat.mallplus.sms.mapper.SmsLabelMemberMapper;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.ums.entity.UmsMemberLevel;
import com.zscat.mallplus.ums.mapper.UmsMemberMapper;
import com.zscat.mallplus.ums.service.IUmsMemberLevelService;
import com.zscat.mallplus.ums.service.IUmsMemberService;
import com.zscat.mallplus.ums.vo.UmsMemberSelect;
import com.zscat.mallplus.util.ConstantUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
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
    @Resource
    private SmsLabelMemberMapper smsLabelMemberMapper;

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
    //根据条件查询所有会员表列表
    public IPage<Map<String, Object>> selectMember(Page<Map<String,Object>> page, UmsMember entity){
        List<Map<String,Object>> umsMembers =memberMapper.selectMember(entity);
        for(int i=0;i<umsMembers.size();i++){
            Map<String,List<SmsLabelSet>> labelList= new HashMap<>();
            UmsMember data= (UmsMember) umsMembers.get(i);
            labelList.put("labelList",smsLabelMemberMapper.getLableList(data.getId()));
        }
        return page.setRecords(umsMembers);
    }
    //查询会员详情
    @Override
    public UmsMember getById(Long id){
        return super.getById(id);
    }

    //保存会员详情
    @Override
    @Transactional
    public boolean save(UmsMember umsMember){
        if(super.save(umsMember)){
            //保存会员标签信息
            for (SmsLabelSet s : umsMember.getLabelList()) {
                SmsLabelMember smsLabelMember = new SmsLabelMember();
                smsLabelMember.setLabelId(s.getId());
                smsLabelMember.setMemberId(umsMember.getId());
                smsLabelMemberMapper.insert(smsLabelMember);
            }
        }

        return true;
    }

    //更新会员详情
    @Override
    @Transactional
    public boolean updateById(UmsMember umsMember){
        if(super.updateById(umsMember)){
            //删除之前手动添加的标签
            smsLabelMemberMapper.removeByMemberId(umsMember.getId(),ConstantUtil.not);
            //保存会员标签信息
            if(umsMember.getLabelList()!=null){
                for (SmsLabelSet s : umsMember.getLabelList()) {
                    SmsLabelMember smsLabelMember = new SmsLabelMember();
                    smsLabelMember.setLabelId(s.getId());
                    smsLabelMember.setMemberId(umsMember.getId());
                    smsLabelMemberMapper.insert(smsLabelMember);
                }
            }
        }


        return true;
    }
    //删除用户标签
    public boolean removeLabel(Long id, Long umsMemberId){
        return memberMapper.removeLabel(id, umsMemberId);
    }

    //高级查询
    public IPage<Map<String, Object>> selectSenior(Page<Map<String,Object>> page, UmsMemberSelect entity){
        List<Map<String,Object>> umsMembers =memberMapper.selectSenior(page,entity);
        for(int i=0;i<umsMembers.size();i++){
            Map<String,List<SmsLabelSet>> labelList= new HashMap<>();
            UmsMember data= (UmsMember) umsMembers.get(i);
            labelList.put("labelList",smsLabelMemberMapper.getLableList(data.getId()));
        }
        return page.setRecords(umsMembers);
    }
}
