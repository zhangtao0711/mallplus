package com.zscat.mallplus.set.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.set.entity.SetSalesBuy;
import com.zscat.mallplus.set.entity.SetSalesOrder;
import com.zscat.mallplus.set.mapper.SetSalesBuyMapper;
import com.zscat.mallplus.set.mapper.SetSalesOrderMapper;
import com.zscat.mallplus.set.service.ISetSalesOrderService;
import com.zscat.mallplus.set.utils.TimeUtil;
import com.zscat.mallplus.sys.entity.SysPermission;
import com.zscat.mallplus.sys.entity.SysUserPermission;
import com.zscat.mallplus.sys.mapper.SysPermissionMapper;
import com.zscat.mallplus.sys.mapper.SysUserPermissionMapper;
import com.zscat.mallplus.util.ConstantUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
* @author wang
* @date 2020-05-26
*/
@Service
public class SetSalesOrderServiceImpl extends ServiceImpl
<SetSalesOrderMapper, SetSalesOrder> implements ISetSalesOrderService {

@Resource
private  SetSalesOrderMapper setSalesOrderMapper;
@Resource
private SysUserPermissionMapper userPermissionMapper;
@Resource
private SetSalesBuyMapper setSalesBuyMapper;
@Resource
private SysPermissionMapper sysPermissionMapper;


    @Override
    @Transactional
    public boolean addAudit(Long dealerId, Long permissionId, Integer storeId) {
        SysUserPermission sysUserPermission = new SysUserPermission();
        sysUserPermission.setId((long) 0);
        sysUserPermission.setAdminId(dealerId);
        sysUserPermission.setPermissionId(permissionId);
        sysUserPermission.setStoreId(storeId);
        try {
            userPermissionMapper.insert(sysUserPermission);
            SysPermission permission = new SysPermission();
            permission.setPid(permissionId);
            List<SysPermission> list = sysPermissionMapper.selectList(new QueryWrapper<>(permission));
            for (SysPermission permission1:list){
                SysUserPermission userPermission = new SysUserPermission();
                userPermission.setId((long) 0);
                userPermission.setAdminId(dealerId);
                userPermission.setPermissionId(permissionId);
                userPermission.setStoreId(storeId);
                userPermissionMapper.insert(sysUserPermission);
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    @Transactional
    public boolean saveBuyInfo(SetSalesOrder order) {
        SetSalesBuy buy = new SetSalesBuy();
        buy.setDealerId(order.getDealerId());
        buy.setPerssionId(order.getPerssionId());
        SetSalesBuy salesBuy = setSalesBuyMapper.selectOne(new QueryWrapper<>(buy));
        if (salesBuy==null){
            salesBuy.setDealerId(order.getDealerId());
            salesBuy.setPerssionId(order.getPerssionId());
            SysPermission permission = sysPermissionMapper.selectById(order.getPerssionId());
            salesBuy.setPerssionName(permission.getName());
            salesBuy.setStoreId(order.getStoreId());
            salesBuy.setStoreName(order.getStoreName());
            salesBuy.setCreateTime(new Date());
            salesBuy.setBuyTime(new Date());
            if (order.getMonth()==1) {
                salesBuy.setEndTime(DateUtil.nextMonth());
            }else if (order.getMonth()==12){
                salesBuy.setEndTime(TimeUtil.addDateYear(1));
            }
            salesBuy.setTotalBuyMonth(order.getMonth());
            salesBuy.setIsBuy(ConstantUtil.is);
            salesBuy.setTotalCredit(new BigDecimal(order.getIntegration()));
            salesBuy.setId((long) 0);
            try {
                setSalesBuyMapper.insert(salesBuy);
            }catch (Exception e){
                return false;
            }
        }else {
            salesBuy.setUpdateTime(new Date());
            salesBuy.setTotalCredit(salesBuy.getTotalCredit().add(new BigDecimal(order.getIntegration())).setScale(BigDecimal.ROUND_DOWN,0));
            salesBuy.setTotalBuyMonth(salesBuy.getTotalBuyMonth()+order.getMonth());
            salesBuy.setLastEndTime(salesBuy.getEndTime());
            salesBuy.setBuyTime(new Date());
            if (order.getMonth()==1) {
                salesBuy.setEndTime(DateUtil.nextMonth());
            }else if (order.getMonth()==12){
                salesBuy.setEndTime(TimeUtil.addDateYear(1));
            }
            salesBuy.setIsBuy(ConstantUtil.is);
            try {
                setSalesBuyMapper.updateById(salesBuy);
            }catch (Exception e){
                return false;
            }
        }
        return true;
    }

    @Override
    @Transactional
    public boolean deleteAudit(Long dealerId, Long permissionId, Integer storeId) {
        SysUserPermission sysUserPermission = new SysUserPermission();
        sysUserPermission.setAdminId(dealerId);
        sysUserPermission.setPermissionId(permissionId);
        sysUserPermission.setStoreId(storeId);
        SysUserPermission permission = userPermissionMapper.selectOne(new QueryWrapper<>(sysUserPermission));
        if (permission!=null){
            userPermissionMapper.deleteById(permission.getId());
            SysPermission sysPermission = new SysPermission();
            sysPermission.setPid(permissionId);
            List<SysPermission> list = sysPermissionMapper.selectList(new QueryWrapper<>(sysPermission));
            for (SysPermission permission1:list){
                userPermissionMapper.deleteById(sysUserPermission);
            }
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean removeBuyInfo(SetSalesOrder order) {
        SetSalesBuy buy = new SetSalesBuy();
        buy.setDealerId(order.getDealerId());
        buy.setPerssionId(order.getPerssionId());
        SetSalesBuy salesBuy = setSalesBuyMapper.selectOne(new QueryWrapper<>(buy));
        if (salesBuy==null){
            return false;
        }else {
            salesBuy.setUpdateTime(new Date());
            salesBuy.setTotalCredit(salesBuy.getTotalCredit().subtract(new BigDecimal(order.getIntegration())).setScale(BigDecimal.ROUND_DOWN,0));
            salesBuy.setTotalBuyMonth(salesBuy.getTotalBuyMonth()-order.getMonth());
            salesBuy.setBuyTime(salesBuy.getLastEndTime());
            salesBuy.setLastEndTime(salesBuy.getBuyTime());
            if (order.getMonth()==1) {
                salesBuy.setEndTime(DateUtil.lastMonth());
            }else if (order.getMonth()==12){
                salesBuy.setEndTime(TimeUtil.subDateYear(salesBuy.getEndTime(),-1));
            }
            setSalesBuyMapper.updateById(salesBuy);
        }
        return true;
    }
}
