package com.zscat.mallplus.water.service.impl;

import com.zscat.mallplus.water.entity.WtEquipmentDetailes;
import com.zscat.mallplus.water.mapper.WtEquipmentDetailesMapper;
import com.zscat.mallplus.water.service.IWtEquipmentDetailesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
/**
* @author lyn
* @date 2020-06-17
*/
@Service
public class WtEquipmentDetailesServiceImpl extends ServiceImpl
<WtEquipmentDetailesMapper, WtEquipmentDetailes> implements IWtEquipmentDetailesService {

@Resource
private  WtEquipmentDetailesMapper wtEquipmentDetailesMapper;


}
