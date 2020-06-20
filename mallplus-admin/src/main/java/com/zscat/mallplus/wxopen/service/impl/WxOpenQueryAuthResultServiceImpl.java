package com.zscat.mallplus.wxopen.service.impl;

import com.zscat.mallplus.wxopen.entity.WxOpenQueryAuthResult;
import com.zscat.mallplus.wxopen.mapper.WxOpenQueryAuthResultMapper;
import com.zscat.mallplus.wxopen.service.IWxOpenQueryAuthResultService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
/**
* @author wang
* @date 2020-06-17
*/
@Service
public class WxOpenQueryAuthResultServiceImpl extends ServiceImpl
<WxOpenQueryAuthResultMapper, WxOpenQueryAuthResult> implements IWxOpenQueryAuthResultService {

@Resource
private  WxOpenQueryAuthResultMapper wxOpenQueryAuthResultMapper;


}
