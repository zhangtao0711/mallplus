package com.zscat.mallplus.weixinmp.service.impl;

import com.zscat.mallplus.weixinmp.entity.AccountWechats;
import com.zscat.mallplus.weixinmp.mapper.AccountWechatsMapper;
import com.zscat.mallplus.weixinmp.service.IAccountWechatsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
/**
* @author lyn
* @date 2020-05-22
*/
@Service
public class AccountWechatsServiceImpl extends ServiceImpl
<AccountWechatsMapper, AccountWechats> implements IAccountWechatsService {

@Resource
private AccountWechatsMapper accountWechatsMapper;


}
