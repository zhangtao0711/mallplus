package com.zscat.mallplus.water.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.water.entity.WtFilterElementType;
import com.zscat.mallplus.water.mapper.WtFilterElementTypeMapper;
import com.zscat.mallplus.water.service.IWtFilterElementTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lyn
 * @date 2020-05-20
 */
@Service
public class WtFilterElementTypeServiceImpl extends ServiceImpl
        <WtFilterElementTypeMapper, WtFilterElementType> implements IWtFilterElementTypeService {

    @Resource
    private WtFilterElementTypeMapper wtFilterElementTypeMapper;


}
