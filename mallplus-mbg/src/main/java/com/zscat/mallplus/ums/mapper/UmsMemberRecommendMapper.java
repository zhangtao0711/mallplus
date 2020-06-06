package com.zscat.mallplus.ums.mapper;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.ums.entity.UmsMemberRecommend;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author lyn
 * @date 2020-06-06
 */
public interface UmsMemberRecommendMapper extends BaseMapper<UmsMemberRecommend> {
    //根据条件查询所有会员推荐列表
    List<Map<String,Object>> selectMemberRecommend(Page<Map<String,Object>> page, @Param("entity")UmsMemberRecommend entity);
}
