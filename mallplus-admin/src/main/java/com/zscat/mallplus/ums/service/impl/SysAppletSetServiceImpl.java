package com.zscat.mallplus.ums.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.ums.entity.SysAppletSet;
import com.zscat.mallplus.ums.mapper.SysAppletSetMapper;
import com.zscat.mallplus.ums.service.ISysAppletSetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-06-15
 */
@Service
public class SysAppletSetServiceImpl extends ServiceImpl<SysAppletSetMapper, SysAppletSet> implements ISysAppletSetService {

    @Resource
    private SysAppletSetMapper appletSetMapper;

    @Override
    public List<Map<String, Object>> getMonitorFirm(Integer level, String value, Integer storeId) {
        return appletSetMapper.getMonitorFirm(level,value,storeId);
    }
}
