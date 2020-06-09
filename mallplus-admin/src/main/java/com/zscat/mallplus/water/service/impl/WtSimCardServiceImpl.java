package com.zscat.mallplus.water.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.google.gson.Gson;
import com.zscat.mallplus.sms.entity.SmsLabelSet;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.util.SimCodeUtil;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.water.entity.SimEntity;
import com.zscat.mallplus.water.entity.WtSimCard;
import com.zscat.mallplus.water.entity.WtSimUrlInfo;
import com.zscat.mallplus.water.entity.WtWaterCard;
import com.zscat.mallplus.water.mapper.WtSimCardMapper;
import com.zscat.mallplus.water.mapper.WtSimUrlInfoMapper;
import com.zscat.mallplus.water.service.IWtSimCardService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.water.service.IWtSimUrlInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lyn
 * @date 2020-05-22
 */
@Service
@Slf4j
public class WtSimCardServiceImpl extends ServiceImpl
        <WtSimCardMapper, WtSimCard> implements IWtSimCardService {

    @Resource
    private WtSimCardMapper wtSimCardMapper;
    @Resource
    private WtSimUrlInfoMapper wtSimUrlInfoMapper;
    /**
     * 物联网卡余量查询
     * @param cardno
     * @return
     */
    public SimEntity getChaxun(String cardno) {
        try {
            if (ValidatorUtils.empty(cardno)) {
                return null;
            }
            Gson gson = new Gson();
            WtSimUrlInfo coupon = new WtSimUrlInfo();
            coupon = wtSimUrlInfoMapper.selectOne(new QueryWrapper<>(coupon));
            JSONObject data= SimCodeUtil.getChaxun(coupon,cardno);

            return gson.fromJson(data.toJSONString(), SimEntity.class);
        } catch (Exception e) {
            log.error("物联网卡余量查询：%s", e.getMessage(), e);
            return null;
        }
    }

    //根据卡号查询第三方SIM卡信息
    public WtSimCard getByCardnoUrl(String cardno){
        try{
            WtSimUrlInfo coupon = new WtSimUrlInfo();
            coupon = wtSimUrlInfoMapper.selectOne(new QueryWrapper<>(coupon));
            if(coupon!=null){
                String ss= SimCodeUtil.wlkpacksearch(coupon,cardno);
                return null;
            }
            return null;
        }catch (Exception e){
            log.error("物联网卡余量查询：%s", e.getMessage(), e);
            return null;
        }
    }
    //物联网卡停机
    public SimEntity stop(String cardno){
        try{
            Gson gson = new Gson();
            WtSimUrlInfo coupon = new WtSimUrlInfo();
            coupon = wtSimUrlInfoMapper.selectOne(new QueryWrapper<>(coupon));
            JSONObject data= SimCodeUtil.stop(coupon,cardno);
            return gson.fromJson(data.toJSONString(), SimEntity.class);
        }catch (Exception e){
            log.error("物联网卡停机：%s", e.getMessage(), e);
            return null;
        }
    }
    //物联网卡复机
    public SimEntity start(String cardno){
        try{
            Gson gson = new Gson();
            WtSimUrlInfo coupon = new WtSimUrlInfo();
            coupon = wtSimUrlInfoMapper.selectOne(new QueryWrapper<>(coupon));
            JSONObject data= SimCodeUtil.start(coupon,cardno);
            return gson.fromJson(data.toJSONString(), SimEntity.class);
        }catch (Exception e){
            log.error("物联网卡复机：%s", e.getMessage(), e);
            return null;
        }
    }
    //物联网卡充值
    public SimEntity sendOrder(String cardno){
        try{
            Gson gson = new Gson();
            WtSimUrlInfo coupon = new WtSimUrlInfo();
            coupon = wtSimUrlInfoMapper.selectOne(new QueryWrapper<>(coupon));
            JSONObject data= SimCodeUtil.sendOrder(coupon,cardno);
            return gson.fromJson(data.toJSONString(), SimEntity.class);
        }catch (Exception e){
            log.error("物联网卡充值：%s", e.getMessage(), e);
            return null;
        }
    }
    //根据卡号查询本数据库SIM卡信息
    public WtSimCard getByCardno(String cardno){
        return wtSimCardMapper.getByCardno(cardno);
    }
}
