package com.zscat.mallplus.sys.service.impl;

import com.zscat.mallplus.sys.entity.SysDealerRechargeRecord;
import com.zscat.mallplus.sys.mapper.SysDealerRechargeRecordMapper;
import com.zscat.mallplus.sys.service.ISysDealerRechargeRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
/**
* @author wang
* @date 2020-05-25
*/
@Service
public class SysDealerRechargeRecordServiceImpl extends ServiceImpl
<SysDealerRechargeRecordMapper, SysDealerRechargeRecord> implements ISysDealerRechargeRecordService {

@Resource
private  SysDealerRechargeRecordMapper sysDealerRechargeRecordMapper;


}
