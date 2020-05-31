package com.zscat.mallplus.ums.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.ums.entity.UmsIntegrationChangeHistory;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 积分变化历史记录表 Mapper 接口
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
public interface UmsIntegrationChangeHistoryMapper extends BaseMapper<UmsIntegrationChangeHistory> {

    public List<Map<String,Object>> selectMemberRecord(Page<Map<String,Object>> page,
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
