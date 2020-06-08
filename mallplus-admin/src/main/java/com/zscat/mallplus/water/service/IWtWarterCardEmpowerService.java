package com.zscat.mallplus.water.service;

import com.zscat.mallplus.water.entity.WtWarterCardEmpower;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author lyn
 * @date 2020-06-05
 */

public interface IWtWarterCardEmpowerService extends IService<WtWarterCardEmpower> {
    //水卡下绑定用户数
    Integer getUmsNum(String cardNo, String delFlag);
}
