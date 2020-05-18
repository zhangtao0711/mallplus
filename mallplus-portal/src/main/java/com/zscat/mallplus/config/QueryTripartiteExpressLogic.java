package com.zscat.mallplus.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zscat.mallplus.config.AppConfiguration;

import com.zscat.mallplus.oms.entity.OmsExpressInfo;
import com.zscat.mallplus.oms.mapper.OmsExpressInfoMapper;
import com.zscat.mallplus.util.KdniaoTrackQueryAPI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @tag : 查询三方物流信息
 * @ClassName: QueryTripartiteExpressLogic
 * @Description:
 * @Author: Lee
 * @Create: 2020/3/25 15:25
 * @Version: 1.0
 */
@Component
@Slf4j
public class QueryTripartiteExpressLogic {
    @Resource
    private OmsExpressInfoMapper omsExpressInfoMapper;
    @Autowired
    private AppConfiguration appConfiguration;


    public String exec(String deliverySn){

        String result = null;
        //查询订单物流表
        OmsExpressInfo omsExpressInfo = new OmsExpressInfo();
        omsExpressInfo.setExpressNo(deliverySn);//物流单号
        omsExpressInfo = omsExpressInfoMapper.selectOne(new QueryWrapper<>(omsExpressInfo));
        String expressCorpId = omsExpressInfo.getExpressCorpId();//物流公司编码


        //调用“三方快递公司-快递鸟”即时查询API
        KdniaoTrackQueryAPI api = new KdniaoTrackQueryAPI();
        try {
            log.info("调用“三方快递公司-快递鸟”即时查询AP，入参：expressCorpId={}，deliverySn={}",expressCorpId,deliverySn);
            result = api.getOrderTracesByJson(expressCorpId, deliverySn,appConfiguration);
            log.info("调用“三方快递公司-快递鸟”即时查询AP，返参：result={}",result);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("调用“三方快递公司-快递鸟”即时查询API-异常",e);
            return null;
        }

        return result;
    }


}
