package com.zscat.mallplus.water.service.impl;

import com.zscat.mallplus.water.entity.WtEquipmentCostControl;
import com.zscat.mallplus.water.mapper.WtEquipmentCostControlMapper;
import com.zscat.mallplus.water.service.IWtEquipmentCostControlService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
/**
* @author lyn
* @date 2020-06-19
*/
@Service
public class WtEquipmentCostControlServiceImpl extends ServiceImpl
<WtEquipmentCostControlMapper, WtEquipmentCostControl> implements IWtEquipmentCostControlService {

@Resource
private  WtEquipmentCostControlMapper wtEquipmentCostControlMapper;


}
