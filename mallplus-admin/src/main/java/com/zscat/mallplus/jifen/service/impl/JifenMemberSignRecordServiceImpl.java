package com.zscat.mallplus.jifen.service.impl;

import com.zscat.mallplus.jifen.entity.JifenMemberSignRecord;
import com.zscat.mallplus.jifen.mapper.JifenMemberSignRecordMapper;
import com.zscat.mallplus.jifen.service.IJifenMemberSignRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
/**
* @author wang
* @date 2020-06-22
*/
@Service
public class JifenMemberSignRecordServiceImpl extends ServiceImpl
<JifenMemberSignRecordMapper, JifenMemberSignRecord> implements IJifenMemberSignRecordService {

@Resource
private  JifenMemberSignRecordMapper jifenMemberSignRecordMapper;


}
