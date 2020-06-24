package com.zscat.mallplus.jifen.service.impl;

import com.zscat.mallplus.jifen.entity.JifenLuckDrawRecord;
import com.zscat.mallplus.jifen.mapper.JifenLuckDrawRecordMapper;
import com.zscat.mallplus.jifen.service.IJifenLuckDrawRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
/**
* @author wang
* @date 2020-06-23
*/
@Service
public class JifenLuckDrawRecordServiceImpl extends ServiceImpl
<JifenLuckDrawRecordMapper, JifenLuckDrawRecord> implements IJifenLuckDrawRecordService {

@Resource
private  JifenLuckDrawRecordMapper jifenLuckDrawRecordMapper;


}
