package com.zscat.mallplus.wxminiapp.service.impl;

import com.zscat.mallplus.wxminiapp.entity.ImsAccountWxapp;
import com.zscat.mallplus.wxminiapp.mapper.ImsAccountWxappMapper;
import com.zscat.mallplus.wxminiapp.service.IImsAccountWxappService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
/**
* @author wang
* @date 2020-05-23
*/
@Service
public class ImsAccountWxappServiceImpl extends ServiceImpl
<ImsAccountWxappMapper, ImsAccountWxapp> implements IImsAccountWxappService {

@Resource
private  ImsAccountWxappMapper imsAccountWxappMapper;


}
