package com.zscat.mallplus.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.ums.entity.SysAppletSet;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zscat
 * @since 2019-06-15
 */
public interface ISysAppletSetService extends IService<SysAppletSet> {

    List<Map<String,Object>> getMonitorFirm(Integer level,String value, Integer storeId);
}
