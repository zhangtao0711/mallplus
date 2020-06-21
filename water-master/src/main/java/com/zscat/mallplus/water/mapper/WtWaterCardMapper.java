package com.zscat.mallplus.water.mapper;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.sms.entity.SmsWaterBuyRecord;
import com.zscat.mallplus.water.entity.WtWaterCard;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zscat.mallplus.water.entity.WtWaterCardRecharge;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author lyn
 * @date 2020-05-30
 */
public interface WtWaterCardMapper extends BaseMapper<WtWaterCard> {
    //更新卡列表的经销商信息
    boolean updateStoreId(@Param("sta")Long sta, @Param("end") Long end, @Param("dealerId")Long dealerId
            , @Param("updateBy")Long updateBy, @Param("delFlag")String delFlag);

    //根据卡号获取数据库中卡号信息
    WtWaterCard getOneBy(@Param("data")WtWaterCard entity);

    /**
     * 更新卡内余额
     * @param entity 金额信息
     * @param state 被更新卡状态
     * @param type recharge/金额 experience/体验金额
     * @return
     */
    Integer updateRecharge(@Param("data")WtWaterCardRecharge entity
            ,@Param("state")String state,@Param("type")String type,@Param("delFlag")String delFlag);

    /**
     *  根据开卡id获取售出卡信息
     * @param id 开卡id
     * @param saleStateOn 售出状态
     * @return
     */
    List<WtWaterCard> getAllSaleStateOn(@Param("id")Long id, @Param("saleStateOn")String saleStateOn);

    /**
     * 根据卡号和用户id获取水卡信息
     * @param cardNo
     * @param umsMemberId
     * @param delFlag
     * @param state
     * @return
     */
    WtWaterCard getWaterCard(@Param("cardNo")String cardNo,@Param("umsMemberId")Long umsMemberId
            ,@Param("delFlag")String delFlag,@Param("state")String state);
    //根据卡号修改卡状态
    boolean updateStateByCard(@Param("cardNo")String cardNo, @Param("state")String state, @Param("updateBy")Long updateBy);
    //检索数据
    List<Map<String,Object>> selectData( @Param("entity")WtWaterCard entity);
    //查询问题卡
    List<Map<String,Object>> selectProblemData(Page<Map<String,Object>> page, @Param("entity")WtWaterCard entity,@Param("state")String state);
    //查询
    List<WtWaterCard> selectLists(@Param("entity")WtWaterCard entity);

    /**
     * 会员线上充值时更新卡内余额
     * @param entity 金额信息
     * @param state 被更新卡状态
     * @return
     */
    Integer updateRechargeForUser(@Param("data")SmsWaterBuyRecord entity
            ,@Param("state")String state,@Param("delFlag")String delFlag);
    //查询经销商的用户列表
    List<Map<String,Object>> selectByDealerId(@Param("entity")WtWaterCard entity);

    /**
     * 会员卡充值记录
     * @param cardNo 卡号
     * @param year 年度
     * @param date 日期
     * @param userName 操作人
     * @param type '0'后台 1线上
     * @return
     */
    List<Map<String,Object>> selectUserRecharge(@Param("cardNo")String cardNo
            ,@Param("year")String year,@Param("date")String date,@Param("userName")String userName
            ,@Param("type")String type);
    /**
     * 会员卡消费记录
     * @param cardNo 卡号
     * @param year 年度
     * @param date 日期
     * @param address 消费地点
     * @return
     */
    List<Map<String,Object>> selectUserConsume(@Param("cardNo")String cardNo
            ,@Param("year")String year,@Param("date")String date,@Param("address")String address);
}
