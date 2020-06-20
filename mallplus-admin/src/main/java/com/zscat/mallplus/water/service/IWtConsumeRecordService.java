package com.zscat.mallplus.water.service;

import com.zscat.mallplus.water.entity.WtConsumeRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
* @author lyn
* @date 2020-06-17
*/

public interface IWtConsumeRecordService extends IService<WtConsumeRecord> {
    //根据设备查询销售信息
    List<Map<String,Object>> getListSum(WtConsumeRecord entity);
}
