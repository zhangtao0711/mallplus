package com.zscat.mallplus.water.service.impl;

import com.zscat.mallplus.util.ConstantUtil;
import com.zscat.mallplus.util.DateUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.water.entity.WtWaterCardRecharge;
import com.zscat.mallplus.water.mapper.WtWaterCardMapper;
import com.zscat.mallplus.water.mapper.WtWaterCardRechargeMapper;
import com.zscat.mallplus.water.service.IWtWaterCardRechargeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * @author lyn
 * @date 2020-05-31
 */
@Service
public class WtWaterCardRechargeServiceImpl extends ServiceImpl
        <WtWaterCardRechargeMapper, WtWaterCardRecharge> implements IWtWaterCardRechargeService {

    @Resource
    private WtWaterCardRechargeMapper wtWaterCardRechargeMapper;
    @Resource
    private WtWaterCardMapper wtWaterCardMapper;

    //获取充值卡号是否是当前经销商下的
    public boolean getDealerId(Long sta, Long end, Long dealerId){
        Map<String,Long> data = wtWaterCardRechargeMapper.getDealerId(sta, end);
        if (data!=null && data.size()>0) {
            if(data.get("id") !=null && data.get("id").toString().equals(dealerId.toString())){
                return true;
            }
        }
        return false;
    }
    //保存充值记录
    @Override
    @Transactional
    public boolean save(WtWaterCardRecharge entity){
        //更新卡内余额
        if(entity.getRechargeMoneyType().equals(ConstantUtil.recharge_money_type_0)){
            wtWaterCardMapper.updateRecharge(entity,ConstantUtil.water_code_state_0,"recharge",ConstantUtil.delFlag);
        }else{
            //到期天数转换成到期日期
            if(entity.getExperienceEndType().equals(ConstantUtil.experience_end_type_1)){
                entity.setExperienceEndData(DateUtils.addDay2(new Date(), entity.getExperienceEndDay()));
            }
            //按卡号充值
            if(entity.getRechargeType().equals(ConstantUtil.recharge_type_1)){
                wtWaterCardMapper.updateRecharge(entity,ConstantUtil.water_code_state_0,"experience",ConstantUtil.delFlag);
            }else if(entity.getRechargeType().equals(ConstantUtil.recharge_money_type_0)){
                wtWaterCardMapper.updateRecharge(entity,ConstantUtil.water_code_state_0,"recharge",ConstantUtil.delFlag);
            }else{
                //更新用户标签使用次数
                wtWaterCardRechargeMapper.updateSalesCount(entity,ConstantUtil.ums_label_perssion_id);
                //获取用户标签数据
//                SELECT a.id FROM
//                ums_member a
//                LEFT JOIN wt_water_card b ON a.id = b.ums_member_id
//                and b.state='0' and b.del_flag='0' and b.store_id='d'
//                and b.card_money =50
//                WHERE a.status='1'
//                and a.store_id='d'
//                and a.member_level_id='1'

            }
            //将到期日期清空
            if(entity.getExperienceEndType().equals(ConstantUtil.experience_end_type_1)){
                entity.setExperienceEndData(null);
            }
        }

        //保存充值记录
        return super.save(entity);
    }

    //获取经销商用户标签使用次数
    public Integer getSalesCount(WtWaterCardRecharge entity,String perssionId){
        return wtWaterCardRechargeMapper.getSalesCount(entity,perssionId);
    }
    //获取经销商充值套餐权限
    public boolean getSalesInfo(Long dealerId, String perssionId){
        if(wtWaterCardRechargeMapper.getSalesInfo(dealerId,perssionId)!=null){
            return true;
        }
        return false;
    }
}
