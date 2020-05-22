package com.zscat.mallplus.weixinmp.service.impl;

import com.zscat.mallplus.weixinmp.entity.ImsAccountWechats;
import com.zscat.mallplus.weixinmp.mapper.ImsAccountWechatsMapper;
import com.zscat.mallplus.weixinmp.service.IImsAccountWechatsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
/**
* @author lyn
* @date 2020-05-22
*/
@Service
public class ImsAccountWechatsServiceImpl extends ServiceImpl
<ImsAccountWechatsMapper, ImsAccountWechats> implements IImsAccountWechatsService {

@Resource
private  ImsAccountWechatsMapper imsAccountWechatsMapper;


}
