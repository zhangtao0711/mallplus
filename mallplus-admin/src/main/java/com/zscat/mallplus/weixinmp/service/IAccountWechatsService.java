package com.zscat.mallplus.weixinmp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.weixinmp.entity.AccountWechats;

/**
* @author lyn
* @date 2020-05-22
*/

public interface IAccountWechatsService extends IService<AccountWechats> {

    Long getLastNum(Long dealerId);
}
