package com.zscat.mallplus.merchant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.merchant.entity.MerchatFacilitatorConfig;
import com.zscat.mallplus.merchant.mapper.MerchatFacilitatorConfigMapper;
import com.zscat.mallplus.merchant.service.IMerchatFacilitatorConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author mallplus
* @date 2020-05-14
*/
@Service
public class MerchatFacilitatorConfigServiceImpl extends ServiceImpl
<MerchatFacilitatorConfigMapper, MerchatFacilitatorConfig> implements IMerchatFacilitatorConfigService {

@Resource
private MerchatFacilitatorConfigMapper merchatFacilitatorConfigMapper;


}
