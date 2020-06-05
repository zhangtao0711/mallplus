package com.zscat.mallplus.water.service.impl;

import com.zscat.mallplus.util.ConstantUtil;
import com.zscat.mallplus.water.entity.WtEquipment;
import com.zscat.mallplus.water.mapper.WtEquipmentMapper;
import com.zscat.mallplus.water.service.IWtEquipmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author lyn
 * @date 2020-05-22
 */
@Service
public class WtEquipmentServiceImpl extends ServiceImpl
        <WtEquipmentMapper, WtEquipment> implements IWtEquipmentService {

    @Resource
    private WtEquipmentMapper wtEquipmentMapper;


    //获取设备是否是当前经销商下的
    public boolean getDealerId(String eqcode, Long dealerId){
        Map<String,Long> data = wtEquipmentMapper.getDealerId(eqcode, dealerId,ConstantUtil.delFlag);
        if (data!=null && data.size()>0) {
            if(data.get("id") !=null && data.get("id").toString().equals(dealerId.toString())){
                return true;
            }
        }
        return false;
    }
}
