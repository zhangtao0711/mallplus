package com.zscat.mallplus.water.service.impl;

import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.water.entity.WtSimCard;
import com.zscat.mallplus.water.entity.WtSimUrlInfo;
import com.zscat.mallplus.water.mapper.WtSimCardMapper;
import com.zscat.mallplus.water.mapper.WtSimUrlInfoMapper;
import com.zscat.mallplus.water.service.IWtSimCardService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.water.service.IWtSimUrlInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lyn
 * @date 2020-05-22
 */
@Service
public class WtSimCardServiceImpl extends ServiceImpl
        <WtSimCardMapper, WtSimCard> implements IWtSimCardService {

    @Resource
    private WtSimCardMapper wtSimCardMapper;
    @Resource
    private WtSimUrlInfoMapper wtSimUrlInfoMapper;

    /**
     * 查询SIM卡第三方接口和key等信息
     * @param id
     * @return
     */
    private WtSimUrlInfo getSimUrlInfo(Long id) {

        try {
            if (ValidatorUtils.empty(id)) {
                return null;
            }
            WtSimUrlInfo coupon = wtSimUrlInfoMapper.selectById(id);
            return coupon;
        } catch (Exception e) {
//            log.error("查询SIM卡第三方appkey明细：%s", e.getMessage(), e);
            return null;
        }
    }


}
