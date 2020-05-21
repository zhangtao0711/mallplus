package com.zscat.mallplus.cms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zscat.mallplus.cms.entity.CmsHelp;

import java.util.List;

/**
 * <p>
 * 帮助表 Mapper 接口
 * </p>
 *
 * @author zscat
 * @since 2019-04-17
 */
public interface CmsHelpMapper extends BaseMapper<CmsHelp> {
    /**
     * 列表查询
     *
     * @param entity
     * @return
     */
    List<CmsHelp> pageList( CmsHelp entity);
}
