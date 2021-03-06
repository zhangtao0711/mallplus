package com.zscat.mallplus.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.sys.entity.SysArea;
import com.zscat.mallplus.ums.entity.OmsShip;

/**
 * <p>
 * 配送方式表 服务类
 * </p>
 *
 * @author zscat
 * @since 2019-09-16
 */
public interface IPositionService  {

    Object pois(String geoHash);

    SysArea guessCity(String clientIp);

    SysArea findById(Long cityId);

    Object searchPlace(String cityName, String keyword);
}
