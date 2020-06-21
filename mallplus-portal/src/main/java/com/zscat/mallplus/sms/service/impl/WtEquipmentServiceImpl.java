package com.zscat.mallplus.sms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.util.applet.StringConstantUtil;
import com.zscat.mallplus.water.entity.WtEquipment;
import com.zscat.mallplus.water.entity.WtEquipmentForPortal;
import com.zscat.mallplus.water.mapper.WtEquipmentMapper;
import com.zscat.mallplus.sms.service.IWtEquipmentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
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
        Map<String,Long> data = wtEquipmentMapper.getDealerId(eqcode, dealerId,StringConstantUtil.delFlag);
        if (data!=null && data.size()>0) {
            if(data.get("id") !=null && data.get("id").toString().equals(dealerId.toString())){
                return true;
            }
        }
        return false;
    }
    //根据条件查询所有会员表列表
    public IPage<Map<String, Object>> selectData(Page<Map<String,Object>> page, WtEquipment entity){
        return page.setRecords(wtEquipmentMapper.selectData(page,entity));
    }
    //根据设备用户查询水卡信息
    public List<WtEquipmentForPortal> getWaterCardByEq(WtEquipmentForPortal entity){
        return wtEquipmentMapper.getWaterCardByEq(entity);
    }
    //判断设备是否空闲
    public boolean checkEquipmentState(String eqcode,String on){
        if(wtEquipmentMapper.getEquipmentState(eqcode,on)!=null){
            return true;
        }else{
            return false;
        }
    }
}
