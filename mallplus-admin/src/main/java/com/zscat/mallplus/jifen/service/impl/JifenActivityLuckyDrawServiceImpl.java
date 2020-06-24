package com.zscat.mallplus.jifen.service.impl;

import com.zscat.mallplus.jifen.entity.JifenActivityLuckyDraw;
import com.zscat.mallplus.jifen.mapper.JifenActivityLuckyDrawMapper;
import com.zscat.mallplus.jifen.service.IJifenActivityLuckyDrawService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
/**
* @author wang
* @date 2020-06-23
*/
@Service
public class JifenActivityLuckyDrawServiceImpl extends ServiceImpl
<JifenActivityLuckyDrawMapper, JifenActivityLuckyDraw> implements IJifenActivityLuckyDrawService {

@Resource
private  JifenActivityLuckyDrawMapper jifenActivityLuckyDrawMapper;


}
