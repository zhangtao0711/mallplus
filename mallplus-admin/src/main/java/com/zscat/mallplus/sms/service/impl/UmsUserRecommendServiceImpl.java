package com.zscat.mallplus.sms.service.impl;

import com.zscat.mallplus.sms.entity.UmsUserRecommend;
import com.zscat.mallplus.sms.mapper.UmsUserRecommendMapper;
import com.zscat.mallplus.sms.service.IUmsUserRecommendService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
* @author zhangtao
* @date 2020-06-26
*/
@Service
public class UmsUserRecommendServiceImpl extends ServiceImpl
<UmsUserRecommendMapper, UmsUserRecommend> implements IUmsUserRecommendService {

@Resource
private  UmsUserRecommendMapper umsUserRecommendMapper;


}
