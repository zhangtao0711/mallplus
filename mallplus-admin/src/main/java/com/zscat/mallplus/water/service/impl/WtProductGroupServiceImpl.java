package com.zscat.mallplus.water.service.impl;

import com.zscat.mallplus.water.entity.WtEquipment;
import com.zscat.mallplus.water.entity.WtProductGroup;
import com.zscat.mallplus.water.mapper.WtProductGroupMapper;
import com.zscat.mallplus.water.service.IWtProductGroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lyn
 * @date 2020-05-22
 */
@Service
public class WtProductGroupServiceImpl extends ServiceImpl
        <WtProductGroupMapper, WtProductGroup> implements IWtProductGroupService {

    @Resource
    private WtProductGroupMapper wtProductGroupMapper;

    //查询分组内是否有绑定的设备
    public boolean checkProductExists(Long id,String delFlag) {
        List<WtEquipment> wtEquipmentList = new ArrayList<>();
        if(wtProductGroupMapper.getProduct(id,delFlag).size()>0){
            return true;
        }
        return false;
    }
}
