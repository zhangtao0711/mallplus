package com.zscat.mallplus.ums.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.ums.entity.UmsMemberRecommend;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @author lyn
 * @date 2020-06-06
 */

public interface IUmsMemberRecommendService extends IService<UmsMemberRecommend> {

    //根据条件查询所有会员推荐列表
    IPage<Map<String, Object>> selectMemberRecommend(Page<Map<String,Object>> page, UmsMemberRecommend entity);
}
