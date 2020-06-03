package com.zscat.mallplus.water.service;

import com.zscat.mallplus.water.entity.WtWaterCardActivate;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @author lyn
 * @date 2020-05-30
 */

public interface IWtWaterCardActivateService extends IService<WtWaterCardActivate> {
    //卡号是否重复
    boolean checkNum(Long str, Long end);
    //获取开卡开号存在数 卡号关联公众号
    Map<String,Integer> getNumInfo(Long str, Long end,Integer userId);

    boolean save(WtWaterCardActivate entity);
}
