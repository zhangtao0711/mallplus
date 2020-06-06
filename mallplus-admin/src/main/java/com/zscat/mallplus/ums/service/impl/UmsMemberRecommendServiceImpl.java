package com.zscat.mallplus.ums.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.ums.entity.UmsMemberRecommend;
import com.zscat.mallplus.ums.mapper.UmsMemberRecommendMapper;
import com.zscat.mallplus.ums.service.IUmsMemberRecommendService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author lyn
 * @date 2020-06-06
 */
@Service
public class UmsMemberRecommendServiceImpl extends ServiceImpl
        <UmsMemberRecommendMapper, UmsMemberRecommend> implements IUmsMemberRecommendService {

    @Resource
    private UmsMemberRecommendMapper umsMemberRecommendMapper;

    //根据条件查询所有会员推荐列表
    @Override
    public IPage<Map<String, Object>> selectMemberRecommend(Page<Map<String,Object>> page, UmsMemberRecommend entity){
        return page.setRecords(umsMemberRecommendMapper.selectMemberRecommend(page,entity));
    }
}
