package com.zscat.mallplus.ums.service.impl;

import com.zscat.mallplus.ums.entity.UmsMemberLevel;
import com.zscat.mallplus.ums.mapper.UmsMemberLevelMapper;
import com.zscat.mallplus.ums.service.IUmsMemberLevelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 会员等级表 服务实现类
 *
 * @date 2020-05-29
 */
@Service
public class UmsMemberLevelServiceImpl extends ServiceImpl
        <UmsMemberLevelMapper, UmsMemberLevel> implements IUmsMemberLevelService {

    @Resource
    private UmsMemberLevelMapper umsMemberLevelMapper;


}
