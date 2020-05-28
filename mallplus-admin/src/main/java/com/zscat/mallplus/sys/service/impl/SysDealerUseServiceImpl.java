package com.zscat.mallplus.sys.service.impl;

import com.zscat.mallplus.sys.entity.SysDealerUse;
import com.zscat.mallplus.sys.mapper.SysDealerUseMapper;
import com.zscat.mallplus.sys.service.ISysDealerUseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
/**
* @author wang
* @date 2020-05-25
*/
@Service
public class SysDealerUseServiceImpl extends ServiceImpl
<SysDealerUseMapper, SysDealerUse> implements ISysDealerUseService {

@Resource
private  SysDealerUseMapper sysDealerUseMapper;


}
