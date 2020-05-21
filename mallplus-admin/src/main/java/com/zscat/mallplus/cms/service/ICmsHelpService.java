package com.zscat.mallplus.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.cms.entity.CmsHelp;

import java.util.List;

/**
 * <p>
 * 帮助表 服务类
 * </p>
 *
 * @author zscat
 * @since 2019-04-17
 */
public interface ICmsHelpService extends IService<CmsHelp> {

    boolean saves(CmsHelp entity);

    IPage<CmsHelp> pageList(IPage<CmsHelp> page, CmsHelp entity);
}
