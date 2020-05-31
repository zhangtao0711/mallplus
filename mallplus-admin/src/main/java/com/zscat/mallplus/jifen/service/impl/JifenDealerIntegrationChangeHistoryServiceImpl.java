package com.zscat.mallplus.jifen.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.jifen.entity.JifenDealerIntegrationChangeHistory;
import com.zscat.mallplus.jifen.mapper.JifenDealerIntegrationChangeHistoryMapper;
import com.zscat.mallplus.jifen.service.IJifenDealerIntegrationChangeHistoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
* @author wang
* @date 2020-05-29
*/
@Service
public class JifenDealerIntegrationChangeHistoryServiceImpl extends ServiceImpl
<JifenDealerIntegrationChangeHistoryMapper, JifenDealerIntegrationChangeHistory> implements IJifenDealerIntegrationChangeHistoryService {

@Resource
private  JifenDealerIntegrationChangeHistoryMapper jifenDealerIntegrationChangeHistoryMapper;


    @Override
    public IPage<Map<String, Object>> selectBusinessRecord(Page<Map<String, Object>> page, Date startTime, Date endTime, String dealerName, String realname, String dealerPhone, Integer level, String changeType, String changeCount, String integration, String sourceType, String integrationStatus) {
        return page.setRecords(jifenDealerIntegrationChangeHistoryMapper.selectBusinessRecord(page,startTime,endTime,dealerName,realname,dealerPhone,level,changeType,changeCount,integration,sourceType,integrationStatus));
    }
}
