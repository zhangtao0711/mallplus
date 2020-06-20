package com.zscat.mallplus.water.service.impl;

import com.zscat.mallplus.water.entity.WtConsumeRecord;
import com.zscat.mallplus.water.mapper.WtConsumeRecordMapper;
import com.zscat.mallplus.water.service.IWtConsumeRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
* @author lyn
* @date 2020-06-17
*/
@Service
public class WtConsumeRecordServiceImpl extends ServiceImpl
<WtConsumeRecordMapper, WtConsumeRecord> implements IWtConsumeRecordService {

@Resource
private  WtConsumeRecordMapper wtConsumeRecordMapper;
    //根据条件查询所有会员表列表
    public List<Map<String,Object>> getListSum(WtConsumeRecord entity){
        return wtConsumeRecordMapper.getListSum(entity);
    }
}
