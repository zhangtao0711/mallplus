package com.zscat.mallplus.water.service.impl;

import com.zscat.mallplus.util.ConstantUtil;
import com.zscat.mallplus.util.StringUtils;
import com.zscat.mallplus.water.entity.WtEquipmentWarterCard;
import com.zscat.mallplus.water.entity.WtWaterCard;
import com.zscat.mallplus.water.entity.WtWaterCardActivate;
import com.zscat.mallplus.water.mapper.WtEquipmentWarterCardMapper;
import com.zscat.mallplus.water.mapper.WtWaterCardActivateMapper;
import com.zscat.mallplus.water.mapper.WtWaterCardMapper;
import com.zscat.mallplus.water.service.IWtWaterCardActivateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author lyn
 * @date 2020-05-30
 */
@Service
public class WtWaterCardActivateServiceImpl extends ServiceImpl
        <WtWaterCardActivateMapper, WtWaterCardActivate> implements IWtWaterCardActivateService {

    @Resource
    private WtWaterCardActivateMapper wtWaterCardActivateMapper;
    @Resource
    private WtWaterCardMapper wtWaterCardMapper;
    @Resource
    private WtEquipmentWarterCardMapper wtEquipmentWarterCardMapper;

    //卡号是否重复
    public boolean checkNum(Long sta, Long end){
        List<WtWaterCardActivate> data = wtWaterCardActivateMapper.getNum(sta,end);
        if(data!=null && data.size()>0){
            return false;
        }
        return true;
    }

    //获取开卡开号存在数 卡号关联公众号
    public Map<String,Long> getNumInfo(Long sta, Long end,Long userId){
        return wtWaterCardActivateMapper.getNumInfo(sta,end,userId);
    }

    //保存
    @Transactional
    public boolean save(WtWaterCardActivate entity){
        //更新卡列表的经销商信息
        if(!wtWaterCardMapper.updateStoreId(Long.valueOf(entity.getStartNo())
                ,Long.valueOf(entity.getEndNo()),entity.getDealerId(),entity.getCreateBy(),ConstantUtil.delFlag)){
            return false;
        }
        //添加水卡和设备关联信息表
        for(Long i =Long.valueOf(entity.getStartNo());i<=Long.valueOf(entity.getEndNo());i++){
            //左补位到10位
            String num = StringUtils.padRight(i.toString(),9,'0');
            WtEquipmentWarterCard wtEquipmentWarterCard = new WtEquipmentWarterCard();
            wtEquipmentWarterCard.setEqcode(entity.getEqcode());//设备号
            wtEquipmentWarterCard.setCardNo(num);//卡号
            if(wtEquipmentWarterCardMapper.insert(wtEquipmentWarterCard)<=0){
                return false;
            }
        }
         if(wtWaterCardActivateMapper.insert(entity)>0){
            return true;
         }
         return false;
    }
}
