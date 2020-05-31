package com.zscat.mallplus.jifen.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.jifen.entity.JifenDealerIntegrationChangeHistory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.Map;

/**
* @author wang
* @date 2020-05-29
*/

public interface IJifenDealerIntegrationChangeHistoryService extends IService<JifenDealerIntegrationChangeHistory> {

    public IPage<Map<String, Object>> selectBusinessRecord(Page<Map<String,Object>> page,
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
