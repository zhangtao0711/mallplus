package com.zscat.mallplus.sys.service.impl;

import com.zscat.mallplus.sys.entity.SysMerchantAudit;
import com.zscat.mallplus.sys.mapper.SysMerchantAuditMapper;
import com.zscat.mallplus.sys.service.ISysMerchantAuditService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
/**
* @author wang
* @date 2020-05-26
*/
@Service
public class SysMerchantAuditServiceImpl extends ServiceImpl
<SysMerchantAuditMapper, SysMerchantAudit> implements ISysMerchantAuditService {

@Resource
private  SysMerchantAuditMapper sysMerchantAuditMapper;


}
