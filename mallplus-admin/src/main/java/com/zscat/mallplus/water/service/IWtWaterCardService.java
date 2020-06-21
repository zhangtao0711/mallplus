package com.zscat.mallplus.water.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.water.entity.WtWaterCard;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @author lyn
 * @date 2020-05-30
 */

public interface IWtWaterCardService extends IService<WtWaterCard> {
    //根据卡号获取数据库中卡号信息
    WtWaterCard getOneBy(WtWaterCard entity);
    //根据开卡id获取售出卡信息
    List<WtWaterCard> getAllSaleStateOn(Long id,String saleStateOn);
    //会员卡和授权人是否关联
    boolean checkCardUms(String cardNo, Long umsMemberId, String delFlag, String water_code_state_0);

    //根据卡号修改卡状态
    boolean updateStateByCard(String cardNo, String state, Long updateBy);
    //检索数据
    IPage<Map<String, Object>> selectData(Page<Map<String,Object>> page, WtWaterCard entity);
    //查询问题卡
    IPage<Map<String, Object>> selectProblemData(Page<Map<String,Object>> mapPage, WtWaterCard entity,String state);
    //查询经销商的用户列表
    IPage<Map<String, Object>> selectByDealerId(Page<Map<String,Object>> mapPage, WtWaterCard entity);

    /**
     * 会员卡充值记录
     * @param cardNo 卡号
     * @param year 年度
     * @param date 日期
     * @param userName 操作人
     * @param type '0'后台 1线上
     * @return
     */
    IPage<Map<String, Object>> selectUserRecharge(Page<Map<String,Object>> mapPage, String cardNo
            , String year, String date, String userName, String type);
    /**
     * 会员卡消费记录
     * @param cardNo 卡号
     * @param year 年度
     * @param date 日期
     * @param address 消费地点
     * @return
     */
    IPage<Map<String, Object>> selectUserConsume(Page<Map<String,Object>> mapPage, String cardNo
            , String year, String date, String address);
}
