package com.zscat.mallplus.ums.service.impl;

import com.zscat.mallplus.ums.entity.UmsMemberLog;
import com.zscat.mallplus.ums.mapper.UmsMemberLogMapper;
import com.zscat.mallplus.ums.service.IUmsMemberLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lyn
 * @date 2020-06-06
 */
@Service
public class UmsMemberLogServiceImpl extends ServiceImpl
        <UmsMemberLogMapper, UmsMemberLog> implements IUmsMemberLogService {

    @Resource
    private UmsMemberLogMapper umsMemberLogMapper;


}
