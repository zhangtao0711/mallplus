package com.zscat.mallplus.water.service;

import com.zscat.mallplus.water.entity.WtWaterCardCreate;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author lyn
 * @date 2020-05-29
 */

public interface IWtWaterCardCreateService extends IService<WtWaterCardCreate> {

    boolean saveAll(WtWaterCardCreate entity, String key);
    //卡号是否重复
    boolean checkNum(Long str, Long end);
}
