package com.zscat.mallplus.water.service.impl;

import com.zscat.mallplus.water.entity.WtEquipmentRemarks;
import com.zscat.mallplus.water.mapper.WtEquipmentRemarksMapper;
import com.zscat.mallplus.water.service.IWtEquipmentRemarksService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
/**
* @author lyn
* @date 2020-06-19
*/
@Service
public class WtEquipmentRemarksServiceImpl extends ServiceImpl
<WtEquipmentRemarksMapper, WtEquipmentRemarks> implements IWtEquipmentRemarksService {

@Resource
private  WtEquipmentRemarksMapper wtEquipmentRemarksMapper;


}
