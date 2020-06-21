package com.zscat.mallplus.water.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.water.entity.WtWaterCard;
import com.zscat.mallplus.water.mapper.WtWaterCardMapper;
import com.zscat.mallplus.water.service.IWtWaterCardService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author lyn
 * @date 2020-05-30
 */
@Service
public class WtWaterCardServiceImpl extends ServiceImpl
        <WtWaterCardMapper, WtWaterCard> implements IWtWaterCardService {

    @Resource
    private WtWaterCardMapper wtWaterCardMapper;

    //根据卡号获取数据库中卡号信息
    public WtWaterCard getOneBy(WtWaterCard entity){
        return wtWaterCardMapper.getOneBy(entity);
    }
    //根据开卡id获取售出卡信息
    public List<WtWaterCard> getAllSaleStateOn(Long id, String saleStateOn){
        return wtWaterCardMapper.getAllSaleStateOn(id, saleStateOn);
    }

    /**
     * 会员卡和授权人是否关联
     * @param cardNo
     * @param umsMemberId
     * @param delFlag
     * @param state 正常卡
     * @return
     */
    public boolean checkCardUms(String cardNo, Long umsMemberId, String delFlag, String state){
        if(wtWaterCardMapper.getWaterCard(cardNo, umsMemberId, delFlag, state)!=null){
            return true;
        }else{
            return false;
        }
    }
    //根据卡号修改卡状态
    public boolean updateStateByCard(String cardNo, String state, Long updateBy){
        return wtWaterCardMapper.updateStateByCard(cardNo, state, updateBy);
    }
    //检索数据
    public IPage<Map<String, Object>> selectData(Page<Map<String,Object>> page, WtWaterCard entity){
        return page.setRecords(wtWaterCardMapper.selectData(entity));
    }

    //查询问题卡
    public IPage<Map<String, Object>> selectProblemData(Page<Map<String,Object>> page, WtWaterCard entity,String state){
        return page.setRecords(wtWaterCardMapper.selectProblemData(page,entity,state));
    }
    //查询经销商的用户列表
    public IPage<Map<String, Object>> selectByDealerId(Page<Map<String,Object>> page, WtWaterCard entity){
        return page.setRecords(wtWaterCardMapper.selectByDealerId(entity));
    }

    /**
     * 会员卡充值记录
     * @param cardNo 卡号
     * @param year 年度
     * @param date 日期
     * @param userName 操作人
     * @param type '0'后台 1线上
     * @return
     */
    public IPage<Map<String, Object>> selectUserRecharge(Page<Map<String,Object>> page, String cardNo
            , String year, String date, String userName, String type){
        return page.setRecords(wtWaterCardMapper.selectUserRecharge( cardNo
                , year, date, userName, type));
    }
    /**
     * 会员卡消费记录
     * @param cardNo 卡号
     * @param year 年度
     * @param date 日期
     * @param address 消费地点
     * @return
     */
    public IPage<Map<String, Object>> selectUserConsume(Page<Map<String,Object>> page, String cardNo
            , String year, String date, String address){
        return page.setRecords(wtWaterCardMapper.selectUserConsume( cardNo
                , year, date, address));
    }
}
