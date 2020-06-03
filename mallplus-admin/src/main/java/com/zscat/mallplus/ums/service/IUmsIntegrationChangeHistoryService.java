package com.zscat.mallplus.ums.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.ums.entity.UmsIntegrationChangeHistory;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 积分变化历史记录表 服务类
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
public interface IUmsIntegrationChangeHistoryService extends IService<UmsIntegrationChangeHistory> {

    public IPage<Map<String, Object>> selectMemberRecord(Page<Map<String,Object>> page,
                                                         String dealerId,
                                                         Date startTime,
                                                         Date endTime,
                                                         String nickname,
                                                         String phone,
                                                         String dealerName,
                                                         Integer level,
                                                         String name,
                                                         String changeType,
                                                         String changeCount,
                                                         String integration,
                                                         String sourceType,
                                                         String integrationStatus,
                                                         String storeId);
}
