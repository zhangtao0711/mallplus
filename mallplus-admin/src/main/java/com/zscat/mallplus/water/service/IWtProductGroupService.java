package com.zscat.mallplus.water.service;

import com.zscat.mallplus.water.entity.WtEquipment;
import com.zscat.mallplus.water.entity.WtProductGroup;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author lyn
 * @date 2020-05-22
 */

public interface IWtProductGroupService extends IService<WtProductGroup> {
    //查询分组内是否有绑定的设备
    boolean checkProductExists(Long id,String delFlag);
    //清空分组内绑定的设备
    boolean removeProduct(Long id,Long updateUser,String delFlag);
    //查询分组内绑定的设备
    List<WtEquipment> getProduct(Long id, String delFlag);
}
