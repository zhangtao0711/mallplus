package com.zscat.mallplus.jifen.mapper;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.jifen.entity.JifenDealerIntegrationChangeHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author wang
* @date 2020-05-29
*/
public interface JifenDealerIntegrationChangeHistoryMapper extends BaseMapper<JifenDealerIntegrationChangeHistory> {

    public List<Map<String,Object>> selectBusinessRecord(Page<Map<String,Object>> page,
                                                         Date startTime,
                                                         Date endTime,
                                                         String dealerName,
                                                         String realname,
                                                         String dealerPhone,
                                                         Integer level,
                                                         String changeType,
                                                         String changeCount,
                                                         String integration,
                                                         String sourceType,
                                                         String integrationStatus);
}
