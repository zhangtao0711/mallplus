package com.zscat.mallplus.cms.service.impl;

import com.zscat.mallplus.cms.entity.CmsUserSmallSet;
import com.zscat.mallplus.cms.mapper.CmsUserSmallSetMapper;
import com.zscat.mallplus.cms.service.ICmsUserSmallSetService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lyn
 * @date 2020-06-18
 */
@Service
public class CmsUserSmallSetServiceImpl extends ServiceImpl
        <CmsUserSmallSetMapper, CmsUserSmallSet> implements ICmsUserSmallSetService {

    @Resource
    private CmsUserSmallSetMapper cmsUserSmallSetMapper;


}
