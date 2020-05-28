package com.zscat.mallplus.wxminiapp.service.impl;

import com.zscat.mallplus.wxminiapp.entity.AccountWxappVersion;
import com.zscat.mallplus.wxminiapp.mapper.AccountWxappVersionMapper;
import com.zscat.mallplus.wxminiapp.service.IAccountWxappVersionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
/**
* @author wang
* @date 2020-05-25
*/
@Service
public class AccountWxappVersionServiceImpl extends ServiceImpl
<AccountWxappVersionMapper, AccountWxappVersion> implements IAccountWxappVersionService {

@Resource
private AccountWxappVersionMapper accountWxappVersionMapper;


}
