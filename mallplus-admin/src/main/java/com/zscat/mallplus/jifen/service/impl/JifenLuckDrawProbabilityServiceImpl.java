package com.zscat.mallplus.jifen.service.impl;

import com.zscat.mallplus.jifen.entity.JifenLuckDrawProbability;
import com.zscat.mallplus.jifen.mapper.JifenLuckDrawProbabilityMapper;
import com.zscat.mallplus.jifen.service.IJifenLuckDrawProbabilityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
/**
* @author wang
* @date 2020-06-23
*/
@Service
public class JifenLuckDrawProbabilityServiceImpl extends ServiceImpl
<JifenLuckDrawProbabilityMapper, JifenLuckDrawProbability> implements IJifenLuckDrawProbabilityService {

@Resource
private  JifenLuckDrawProbabilityMapper jifenLuckDrawProbabilityMapper;


}
