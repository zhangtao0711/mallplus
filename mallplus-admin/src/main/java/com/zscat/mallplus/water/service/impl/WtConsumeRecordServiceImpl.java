package com.zscat.mallplus.water.service.impl;

import com.zscat.mallplus.water.entity.WtConsumeRecord;
import com.zscat.mallplus.water.mapper.WtConsumeRecordMapper;
import com.zscat.mallplus.water.service.IWtConsumeRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
/**
* @author lyn
* @date 2020-06-17
*/
@Service
public class WtConsumeRecordServiceImpl extends ServiceImpl
<WtConsumeRecordMapper, WtConsumeRecord> implements IWtConsumeRecordService {

@Resource
private  WtConsumeRecordMapper wtConsumeRecordMapper;


}
