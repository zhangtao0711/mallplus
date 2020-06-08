package com.zscat.mallplus.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.set.entity.SetSalesBuy;
import com.zscat.mallplus.set.service.ISetSalesBuyService;
import com.zscat.mallplus.set.utils.TimeUtil;
import com.zscat.mallplus.sys.entity.SysDealerUse;
import com.zscat.mallplus.sys.entity.SysPermission;
import com.zscat.mallplus.sys.service.ISysDealerUseService;
import com.zscat.mallplus.sys.service.ISysPermissionService;
import com.zscat.mallplus.util.ConstantUtil;
import com.zscat.mallplus.util.EasyPoiUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author wang
 * @date 2020-05-25
 * 经销商的应用权限
 */
@Slf4j
@RestController
@RequestMapping("/sys/sysDealerUse")
public class SysDealerUseController {

    @Resource
    private ISysDealerUseService ISysDealerUseService;
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private ISetSalesBuyService setSalesBuyService;

    @SysLog(MODULE = "sys", REMARK = "根据条件查询所有经销商的应用权限列表")
    @ApiOperation("根据条件查询所有经销商的应用权限列表")
    @GetMapping(value = "/list")
//    @PreAuthorize("hasAuthority('sys:sysDealerUse:read')")
    public Object getSysDealerUseByPage(SysDealerUse entity,
                                        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(ISysDealerUseService.page(new Page<SysDealerUse>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有经销商的应用权限列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "保存经销商的应用权限")
    @ApiOperation("保存经销商的应用权限")
    @PostMapping(value = "/create")
//    @PreAuthorize("hasAuthority('sys:sysDealerUse:create')")
    public Object saveSysDealerUse(@RequestBody SysDealerUse entity) {
        SysDealerUse dealerUse = new SysDealerUse();
        dealerUse.setDealerId(entity.getDealerId());
        SysDealerUse use = ISysDealerUseService.getOne(new QueryWrapper<>(dealerUse));
        if (use!=null){
            return new CommonResult().failed("该经销商已经设置过权限，请不要重复设置！");
        }
        try {
            entity.setCreateTime(new Date());
            if (ISysDealerUseService.save(entity)) {
                //1.看设置的那些东西有没有试用时间，有的话将试用时间查出来并弄到到期时间里面去
                String permissionIds = entity.getPermissionIds().substring(0,entity.getPermissionIds().length()-1);
                List<Map<String,Object>> list = jdbcTemplate.queryForList("select * from set_sales where permission_id in (" + permissionIds+")");
                //2. 把权限添到购买权限表里面，设置到期时间什么的
                for (Map<String,Object> map:list){
                    SetSalesBuy setSalesBuy = new SetSalesBuy();
                    setSalesBuy.setId((long) 0);
                    setSalesBuy.setDealerId(entity.getDealerId());
                    setSalesBuy.setPerssionId(Long.parseLong(map.get("permission_id").toString()));
                    setSalesBuy.setPerssionName(map.get("permission_name").toString());
                    Date date = new Date();
                    setSalesBuy.setBuyTime(date);
                    Date endTime = TimeUtil.addDateMinut(date,Integer.parseInt(map.get("trial_time").toString()));
                    setSalesBuy.setEndTime(endTime);
                    setSalesBuy.setLastEndTime(endTime);
                    setSalesBuy.setCreateTime(date);
                    setSalesBuy.setStoreId(entity.getStoreId());
                    setSalesBuy.setStoreName(entity.getStoreName());
                    setSalesBuyService.save(setSalesBuy);
                }
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存经销商的应用权限：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "更新经销商的应用权限")
    @ApiOperation("更新经销商的应用权限")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('sys:sysDealerUse:update')")
    public Object updateSysDealerUse(@RequestBody SysDealerUse entity) {
        try {
            //在保存前先判定能不能修改
            //1.先找到已经授权的应用
            SetSalesBuy buy = new SetSalesBuy();
            buy.setDealerId(entity.getDealerId());
            List<SetSalesBuy> list = setSalesBuyService.list(new QueryWrapper<>(buy));
            //2.循环，判断哪些是人家已经购买的不能修改,求两次更改的交集
            List<String> in = new ArrayList<>();
            for (SetSalesBuy salesBuy:list){
                if (entity.getPermissionIds().contains(salesBuy.getPerssionId().toString())){
                    continue;
                }else {
                    //多出来的
                    if (salesBuy.getIsBuy().equals(ConstantUtil.is)){
                        return new CommonResult().failed("该经销商已购买"+salesBuy.getPerssionName()+"权限，无法修改！");
                    }else {
                        setSalesBuyService.removeById(salesBuy.getId());
                    }
                }
                in.add(salesBuy.getPerssionId().toString());
            }
            //把新的ids整成list，然后和交集求差集
            List<String> newList = new ArrayList<>(Arrays.asList(entity.getPermissionIds().split(",")));
            newList.removeAll(in);
            if (ISysDealerUseService.updateById(entity)) {
                if (newList.size()!=0) {
                    for (String id:newList){
                        Map<String,Object> map = jdbcTemplate.queryForMap("select * from set_sales where permission_id =" + id);
                        SetSalesBuy setSalesBuy = new SetSalesBuy();
                        setSalesBuy.setId((long) 0);
                        setSalesBuy.setDealerId(entity.getDealerId());
                        setSalesBuy.setPerssionId(Long.parseLong(id));
                        setSalesBuy.setPerssionName(map.get("permission_name").toString());
                        Date date = new Date();
                        setSalesBuy.setBuyTime(date);
                        Date endTime = TimeUtil.addDateMinut(date,Integer.parseInt(map.get("trial_time").toString()));
                        setSalesBuy.setEndTime(endTime);
                        setSalesBuy.setLastEndTime(endTime);
                        setSalesBuy.setCreateTime(date);
//                        setSalesBuy.setStoreId(entity.getStoreId());
                        setSalesBuy.setStoreName(entity.getStoreName());
                        setSalesBuyService.save(setSalesBuy);
                    }
                }
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新经销商的应用权限：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "删除经销商的应用权限")
    @ApiOperation("删除经销商的应用权限")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('sys:sysDealerUse:delete')")
    public Object deleteSysDealerUse(@ApiParam("经销商的应用权限id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("经销商的应用权限id");
            }
            if (ISysDealerUseService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除经销商的应用权限：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "给经销商的应用权限分配经销商的应用权限")
    @ApiOperation("查询经销商的应用权限明细")
    @GetMapping(value = "/{id}")
//    @PreAuthorize("hasAuthority('sys:sysDealerUse:read')")
    public Object getSysDealerUseById(@ApiParam("经销商的应用权限id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("经销商的应用权限id");
            }
            SysDealerUse dealerUse = new SysDealerUse();
            dealerUse.setDealerId(id);
            SysDealerUse coupon = ISysDealerUseService.getOne(new QueryWrapper<>(dealerUse));
            if (coupon==null){
                List<Map<String,Object>> permissionList = jdbcTemplate.queryForList("select * from set_sales");
                return new CommonResult().success(permissionList);
            }
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询经销商的应用权限明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除经销商的应用权限")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "sys", REMARK = "批量删除经销商的应用权限")
    @PreAuthorize("hasAuthority('sys:sysDealerUse:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = ISysDealerUseService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "sys", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, SysDealerUse entity) {
        // 模拟从数据库获取需要导出的数据
        List<SysDealerUse> personList = ISysDealerUseService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", SysDealerUse.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "sys", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<SysDealerUse> personList = EasyPoiUtils.importExcel(file, SysDealerUse.class);
        ISysDealerUseService.saveBatch(personList);
    }

    @SysLog(MODULE = "sys", REMARK = "根据条件查询经销商的可够买应用权限列表")
    @ApiOperation("根据条件查询所有经销商的应用权限列表")
    @GetMapping(value = "/getPermissionByDealerId")
//    @PreAuthorize("hasAuthority('sys:sysDealerUse:read')")
    public Object getPermissionByDealerId(@RequestParam Long dealerId) {
        try {
            SysDealerUse dealerUse = new SysDealerUse();
            dealerUse.setDealerId(dealerId);
            SysDealerUse sysDealerUse = ISysDealerUseService.getOne(new QueryWrapper<>(dealerUse));
            String permissionIds = sysDealerUse.getPermissionIds();
            List<String> list = Arrays.asList(permissionIds.split(","));
            List<SysPermission> permissionList = new ArrayList<>();
            for (String id:list){
                SysPermission permission = jdbcTemplate.queryForObject("select * from sys_permission where id="+id, SysPermission.class);
                permissionList.add(permission);
            }
            return new CommonResult().success(permissionList);
        } catch (Exception e) {
            log.error("根据条件查询经销商的可够买应用权限列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "获取应用权限的关于营销功能的数据")
    @ApiOperation("获取应用权限的关于营销功能的数据")
    @GetMapping(value = "/getPermissions")
    public Object getPermissions(@RequestParam Long dealerId) {
        SysDealerUse dealerUse = new SysDealerUse();
        dealerUse.setDealerId(dealerId);
        SysDealerUse use = ISysDealerUseService.getOne(new QueryWrapper<>(dealerUse));
        if (use==null){
            return new CommonResult().failed("该经销商未曾设置过权限，请先设置！");
        }
        String permissionIds = use.getPermissionIds().substring(0,use.getPermissionIds().length()-1);
        List<Map<String,Object>> permissionList = jdbcTemplate.queryForList("select * from set_sales where permission_id in (" + permissionIds+")");
        return new CommonResult().success(permissionList);
    }

//    @SysLog(MODULE = "sys", REMARK = "获取权限列表数据接口")
//    @ApiOperation("获取权限列表数据接口")
//    @GetMapping(value = "/getPermission")
//    public Object getPermission() {
//        List<Map<String,Object>> permissionList = jdbcTemplate.queryForList("select * from set_sales");
//        return new CommonResult().success(permissionList);
//    }
}


