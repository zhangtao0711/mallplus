package com.zscat.mallplus.merchant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.merchant.entity.MerchantBankInfo;
import com.zscat.mallplus.merchant.mapper.MerchantBankInfoMapper;
import com.zscat.mallplus.merchant.service.IMerchantBankInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author mallplus
* @date 2020-05-14
*/
@Service
public class MerchantBankInfoServiceImpl extends ServiceImpl
<MerchantBankInfoMapper, MerchantBankInfo> implements IMerchantBankInfoService {

@Resource
private MerchantBankInfoMapper merchantBankInfoMapper;


}
