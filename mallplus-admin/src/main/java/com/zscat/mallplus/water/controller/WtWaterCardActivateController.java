package com.zscat.mallplus.water.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.util.ConstantUtil;
import com.zscat.mallplus.water.entity.WtWaterCardActivate;
import com.zscat.mallplus.water.service.IWtWaterCardActivateService;
import com.zscat.mallplus.util.EasyPoiUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.wxminiapp.entity.AccountWxapp;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.List;
import java.util.Date;
import java.util.Map;

/**
 * @author lyn
 * @date 2020-05-30
 * 批量开卡
 */
@Slf4j
@RestController
@RequestMapping("/water/wtWaterCardActivate")
public class WtWaterCardActivateController {

    @Resource
    private IWtWaterCardActivateService IWtWaterCardActivateService;

    @SysLog(MODULE = "water", REMARK = "根据条件查询所有批量开卡列表")
    @ApiOperation("根据条件查询所有批量开卡列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('water:wtWaterCardActivate:read')")
    public Object getWtWaterCardActivateByPage(WtWaterCardActivate entity,
                                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                               @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IWtWaterCardActivateService.page(new Page<WtWaterCardActivate>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有批量开卡列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "保存批量开卡")
    @ApiOperation("保存批量开卡")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('water:wtWaterCardActivate:create')")
    public Object saveWtWaterCardActivate(@RequestBody WtWaterCardActivate entity) {
        try {
            //判断起始卡号和终止卡号合理性
            if(Long.valueOf(entity.getStartNo())>Long.valueOf(entity.getEndNo())){
                return new CommonResult().failed("终止卡号必须大于等于起始卡号！");
            }
            if(Long.valueOf(entity.getEndNo())> ConstantUtil.max_card_no){
                return new CommonResult().failed("制卡卡号最大值是"+ ConstantUtil.max_card_no +"！");
            }
            //开卡卡号是否重复
            if(!IWtWaterCardActivateService.checkNum(Long.valueOf(entity.getStartNo()),Long.valueOf(entity.getEndNo()))){
                return new CommonResult().failed("此区段内有已经开过的卡号，请重新设定起始卡号-终止卡号范围！");
            }
            //获取开卡开号存在数 卡号关联公众号
            Map<String,Integer> data = IWtWaterCardActivateService.getNumInfo(Long.valueOf(entity.getStartNo()),Long.valueOf(entity.getEndNo()),entity.getEndNo());
            boolean storeFlag=false;//经销商和制卡的公众号是否一致
            if (data!=null) {
                Iterator<Map.Entry<String, Integer>> it =data.entrySet().iterator();
                while(it.hasNext()){
                    Map.Entry<String, Integer> entry = it.next();
                    if(entry.getKey().equals(entity.getStoreId())){
                        storeFlag=true;
                    }
                }
                if(!storeFlag){
                    return new CommonResult().failed("制卡时绑定的公众号和经销商绑定的公众号不一致！");
                }else{
                    if(data.get(entity.getStoreId())!=Long.valueOf(entity.getEndNo())-Long.valueOf(entity.getStartNo())+1){
                        return new CommonResult().failed("此区段内有的卡号存在没有制卡的卡号！");
                    }
                }
            }else{
                return new CommonResult().failed("此区段内有的卡号存在没有制卡的卡号！");
            }

            entity.setCreateTime(new Date());
            if (IWtWaterCardActivateService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存批量开卡：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "更新批量开卡")
    @ApiOperation("更新批量开卡")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('water:wtWaterCardActivate:update')")
    public Object updateWtWaterCardActivate(@RequestBody WtWaterCardActivate entity) {
        try {
            if (IWtWaterCardActivateService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新批量开卡：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "删除批量开卡")
    @ApiOperation("删除批量开卡")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('water:wtWaterCardActivate:delete')")
    public Object deleteWtWaterCardActivate(@ApiParam("批量开卡id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("批量开卡id");
            }
            if (IWtWaterCardActivateService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除批量开卡：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "给批量开卡分配批量开卡")
    @ApiOperation("查询批量开卡明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('water:wtWaterCardActivate:read')")
    public Object getWtWaterCardActivateById(@ApiParam("批量开卡id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("批量开卡id");
            }
            WtWaterCardActivate coupon = IWtWaterCardActivateService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询批量开卡明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除批量开卡")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "water", REMARK = "批量删除批量开卡")
    @PreAuthorize("hasAuthority('water:wtWaterCardActivate:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = IWtWaterCardActivateService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "water", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, WtWaterCardActivate entity) {
        // 模拟从数据库获取需要导出的数据
        List<WtWaterCardActivate> personList = IWtWaterCardActivateService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", WtWaterCardActivate.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "water", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<WtWaterCardActivate> personList = EasyPoiUtils.importExcel(file, WtWaterCardActivate.class);
        IWtWaterCardActivateService.saveBatch(personList);
    }
}


