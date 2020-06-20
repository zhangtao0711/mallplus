package com.zscat.mallplus.water.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.water.entity.SimEntity;
import com.zscat.mallplus.water.entity.WtSimCard;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.water.entity.WtSimUrlInfo;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author lyn
 * @date 2020-05-22
 */

public interface IWtSimCardService extends IService<WtSimCard> {

    //根据卡号查询第三方SIM卡信息
    WtSimCard getByCardnoUrl(String cardno);
    //根据卡号查询本数据库SIM卡信息
    WtSimCard getByCardno(String cardno);
    //物联网卡余量查询
    JSONObject getChaxun(String cardno);
    //物联网卡停机
    SimEntity stop(String cardno);
    //物联网卡复机
    SimEntity start(String cardno);
    //物联网卡充值
    SimEntity sendOrder(String cardno);

    //根据条件查询SIM卡
    IPage<Map<String, Object>> selectData(Page<Map<String,Object>> mapPage, WtSimCard entity);
}
