package com.zscat.mallplus.water.service.impl;

import com.zscat.mallplus.util.SimCodeUtil;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.water.entity.SimEntity;
import com.zscat.mallplus.water.entity.WtSimUrlInfo;
import com.zscat.mallplus.water.mapper.WtSimUrlInfoMapper;
import com.zscat.mallplus.water.service.IWtSimUrlInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lyn
 * @date 2020-05-22
 */
@Service
@Slf4j
public class WtSimUrlInfoServiceImpl extends ServiceImpl
        <WtSimUrlInfoMapper, WtSimUrlInfo> implements IWtSimUrlInfoService {

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
            log.error("查询SIM卡第三方appkey明细：%s", e.getMessage(), e);
            return null;
        }
    }


    /**
     * 获取账号余额
     * @param id
     * @return
     */
    public SimEntity getAmount(Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return null;
            }
            WtSimUrlInfo coupon = wtSimUrlInfoMapper.selectById(id);
            String ss= SimCodeUtil.getAmount(coupon);
            return null;
        } catch (Exception e) {
            log.error("物联网卡余量查询：%s", e.getMessage(), e);
            return null;
        }
    }
}
