package com.zscat.mallplus.sys.service;

import com.zscat.mallplus.sys.entity.SysDealerRechargeRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
* @author wang
* @date 2020-05-25
*/

public interface ISysDealerRechargeRecordService extends IService<SysDealerRechargeRecord> {
    public List<Map<String,Object>> getSaleList(Long dealerId);
}
