package com.zscat.mallplus.sms.mapper;


import com.zscat.mallplus.sms.entity.SmsLabelMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zscat.mallplus.sms.entity.SmsLabelSet;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author lyn
* @date 2020-05-30
*/
public interface SmsLabelMemberMapper extends BaseMapper<SmsLabelMember> {
    //根据用户id删除之前手动添加的标签
    void removeByMemberId(@Param("id") Long id,@Param("isSystem") String isSystem);
    //根据用户id获取标签信息
    List<SmsLabelSet> getLableList(@Param("memberId")Long memberId);
}
