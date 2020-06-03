package com.zscat.mallplus.water.mapper;


import com.zscat.mallplus.water.entity.WtWaterCard;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zscat.mallplus.water.entity.WtWaterCardRecharge;
import org.apache.ibatis.annotations.Param;

/**
 * @author lyn
 * @date 2020-05-30
 */
public interface WtWaterCardMapper extends BaseMapper<WtWaterCard> {
    //更新卡列表的经销商信息
    boolean updateStoreId(@Param("sta")Long sta, @Param("end") Long end, @Param("dealerId")Long dealerId
            , @Param("storeId")Integer storeId,@Param("updateBy")Long updateBy, @Param("delFlag")String delFlag);

    //根据卡号获取数据库中卡号信息
    WtWaterCard getOneBy(@Param("data")WtWaterCard entity);

    /**
     * 更新卡内余额
     * @param entity 金额信息
     * @param state 被更新卡状态
     * @param type recharge/金额 experience/体验金额
     * @return
     */
    Integer updateRecharge(@Param("data")WtWaterCardRecharge entity,@Param("state")String state,@Param("type")String type);
}
