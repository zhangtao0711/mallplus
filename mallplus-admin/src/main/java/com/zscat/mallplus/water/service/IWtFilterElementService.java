package com.zscat.mallplus.water.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.util.ConstantUtil;
import com.zscat.mallplus.water.entity.WtFilterElement;

/**
 * @author lyn
 * @date 2020-05-20
 */

public interface IWtFilterElementService extends IService<WtFilterElement> {
    //自动更新滤芯计时类型状态区分
    void updateState();
}
