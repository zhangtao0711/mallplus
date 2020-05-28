package com.zscat.mallplus.wxminiapp.service.impl;

import com.zscat.mallplus.wxminiapp.entity.AccountWxapp;
import com.zscat.mallplus.wxminiapp.mapper.AccountWxappMapper;
import com.zscat.mallplus.wxminiapp.service.IAccountWxappService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
/**
* @author wang
* @date 2020-05-23
*/
@Service
public class AccountWxappServiceImpl extends ServiceImpl
<AccountWxappMapper, AccountWxapp> implements IAccountWxappService {

@Resource
private AccountWxappMapper accountWxappMapper;


}
