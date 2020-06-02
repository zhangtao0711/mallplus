package com.zscat.mallplus.sys.mapper;


import com.zscat.mallplus.sys.entity.SysDealerRechargeRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
* @author wang
* @date 2020-05-25
*/
public interface SysDealerRechargeRecordMapper extends BaseMapper<SysDealerRechargeRecord> {

    public List<Map<String,Object>> getSaleList(Long dealerId);
}
