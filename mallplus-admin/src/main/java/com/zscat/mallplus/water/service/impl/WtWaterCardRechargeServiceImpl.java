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
    public boolean getStoreId(Long sta, Long end, Integer storeId){
        Map<String,Integer> data = wtWaterCardRechargeMapper.getStoreId(sta, end);
        if (data!=null && data.size()==1) {
            Iterator<Map.Entry<String, Integer>> it =data.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry<String, Integer> entry = it.next();
                if(entry.getKey().equals(storeId)){
                    return true;
                }
            }
        }
        return false;
    }
    //保存充值记录
    @Override
    public boolean save(WtWaterCardRecharge entity){
        //更新卡内余额
        if(entity.getRechargeMoneyType().equals(ConstantUtil.recharge_money_type_0)){
            wtWaterCardMapper.updateRecharge(entity,ConstantUtil.water_code_state_0,"recharge");
        }else{
            //到期天数转换成到期日期
            if(entity.getExperienceEndType().equals(ConstantUtil.experience_end_type_1)){
                entity.setExperienceEndData(DateUtils.addDay2(new Date(), entity.getExperienceEndDay()));
            }
            //按卡号充值
            if(entity.getRechargeType().equals(ConstantUtil.recharge_type_1)){
                wtWaterCardMapper.updateRecharge(entity,ConstantUtil.water_code_state_0,"experience");
            }else{

            }
            //将到期日期清空
            if(entity.getExperienceEndType().equals(ConstantUtil.experience_end_type_1)){
                entity.setExperienceEndData(null);
            }
        }

        //保存充值记录
        return super.save(entity);
    }
}
