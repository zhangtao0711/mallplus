package com.zscat.mallplus.ums.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.ums.entity.UmsIntegrationChangeHistory;
import com.zscat.mallplus.ums.mapper.UmsIntegrationChangeHistoryMapper;
import com.zscat.mallplus.ums.service.IUmsIntegrationChangeHistoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 积分变化历史记录表 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Service
public class UmsIntegrationChangeHistoryServiceImpl extends ServiceImpl<UmsIntegrationChangeHistoryMapper, UmsIntegrationChangeHistory> implements IUmsIntegrationChangeHistoryService {

    @Resource
    private UmsIntegrationChangeHistoryMapper umsIntegrationChangeHistoryMapper;
    @Override
    public IPage<Map<String, Object>> selectMemberRecord(Page<Map<String, Object>> page, String dealerId, Date startTime, Date endTime, String nickname, String phone, String dealerName, Integer level, String name, String changeType, String changeCount, String integration, String sourceType, String integrationStatus, String storeId) {
        return page.setRecords(umsIntegrationChangeHistoryMapper.selectMemberRecord(page,
                dealerId,startTime,endTime,nickname,phone,dealerName,level,name,changeType,changeCount,integration,sourceType,
                integrationStatus,storeId));
    }
}
