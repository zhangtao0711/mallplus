package com.zscat.mallplus.set.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.set.entity.SetSalesBuy;
import com.zscat.mallplus.set.entity.SetSalesOrder;

/**
* @author wang
* @date 2020-05-26
*/

public interface ISetSalesOrderService extends IService<SetSalesOrder> {

    /**
     * 购买营销功能添加授权
     * @param dealerId
     * @param permissionId
     * @return
     */
    public boolean addAudit(Long dealerId, Long permissionId, Integer storeId);

    /**'
     * 添加购买记录或者权限
     * @param order
     * @return
     */
    public boolean saveBuyInfo(SetSalesOrder order);

    /**
     * 去掉权限
     * @param dealerId
     * @param permissionId
     * @param storeId
     * @return
     */
    public boolean deleteAudit(Long dealerId, Long permissionId, Integer storeId);

    /**
     * 删除明细
     * @param order
     * @return
     */
    public boolean removeBuyInfo(SetSalesOrder order);
}
