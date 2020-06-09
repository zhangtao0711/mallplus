package com.zscat.mallplus.ums.service.impl;

import com.zscat.mallplus.ums.entity.SysApaySet;
import com.zscat.mallplus.ums.mapper.SysApaySetMapper;
import com.zscat.mallplus.ums.service.ISysApaySetService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
/**
* @author wang
* @date 2020-06-07
*/
@Service
public class SysApaySetServiceImpl extends ServiceImpl
<SysApaySetMapper, SysApaySet> implements ISysApaySetService {

@Resource
private  SysApaySetMapper sysApaySetMapper;


}
