package com.zscat.mallplus.set.service.impl;

import com.zscat.mallplus.set.entity.SetSalesBuy;
import com.zscat.mallplus.set.mapper.SetSalesBuyMapper;
import com.zscat.mallplus.set.service.ISetSalesBuyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
/**
* @author wang
* @date 2020-05-26
*/
@Service
public class SetSalesBuyServiceImpl extends ServiceImpl
<SetSalesBuyMapper, SetSalesBuy> implements ISetSalesBuyService {

@Resource
private  SetSalesBuyMapper setSalesBuyMapper;


}
