package com.zscat.mallplus.water.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.util.ConstantUtil;
import com.zscat.mallplus.util.StringUtils;
import com.zscat.mallplus.water.entity.WtWaterCardCreate;
import com.zscat.mallplus.water.service.IWtWaterCardCreateService;
import com.zscat.mallplus.util.EasyPoiUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.wxminiapp.entity.AccountWxapp;
import com.zscat.mallplus.wxminiapp.service.IAccountWxappService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Date;

/**
 * @author lyn
 * @date 2020-05-29
 * 制卡
 */
@Slf4j
@RestController
@RequestMapping("/water/wtWaterCardCreate")
public class WtWaterCardCreateController {

    @Resource
    private IWtWaterCardCreateService IWtWaterCardCreateService;

    @Resource
    private IAccountWxappService iAccountWxappService;

    @SysLog(MODULE = "water", REMARK = "根据条件查询所有制卡列表")
    @ApiOperation("根据条件查询所有制卡列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('water:wtWaterCardCreate:read')")
    public Object getWtWaterCardCreateByPage(WtWaterCardCreate entity,
                                             @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                             @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IWtWaterCardCreateService.page(new Page<WtWaterCardCreate>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有制卡列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "保存制卡")
    @ApiOperation("保存制卡")
    @PostMapping(value = "/create")
//    @PreAuthorize("hasAuthority('water:wtWaterCardCreate:create')")
    public Object saveWtWaterCardCreate(@RequestBody WtWaterCardCreate entity) {
        try {
            //判断起始卡号和终止卡号合理性
            if(Long.valueOf(entity.getStartNo())>Long.valueOf(entity.getEndNo())){
                return new CommonResult().failed("终止卡号必须大于等于起始卡号！");
            }
            if(Long.valueOf(entity.getEndNo())-Long.valueOf(entity.getStartNo())+1!=Long.valueOf(entity.getNumber())){
                return new CommonResult().failed("终止卡号-起始卡号+1不等于数量！");
            }
            if(Long.valueOf(entity.getEndNo())> ConstantUtil.max_card_no){
                return new CommonResult().failed("最大制卡卡号"+ ConstantUtil.max_card_no +"以内！");
            }
            //卡号是否重复
            if(!IWtWaterCardCreateService.checkNum(Long.valueOf(entity.getStartNo()),Long.valueOf(entity.getEndNo()))){
                return new CommonResult().failed("此区段内有已经生成过的卡号存在，请重新设定起始卡号-终止卡号范围！");
            }
            AccountWxapp accountWxapp=iAccountWxappService.getById(entity.getAcid());
            if(accountWxapp.getKey().isEmpty()){
                return new CommonResult().failed("此公众号没有key，请选择其他信息完整的公众号！");
            }

            //将代号转为大写
            entity.setCode(entity.getCode().toUpperCase());
            //设定识别码
            entity.setDistinguishNum(StringUtils.getRandomString(4));

            //设定生成日期
            entity.setCreateTime(new Date());
            //设定制卡状态0未制卡
            entity.setState(ConstantUtil.card_create_state_0);

            if (IWtWaterCardCreateService.saveAll(entity,"1220")) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存制卡：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "更新制卡")
    @ApiOperation("更新制卡")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('water:wtWaterCardCreate:update')")
    public Object updateWtWaterCardCreate(@RequestBody WtWaterCardCreate entity) {
        try {
            //设定跟新日期
            entity.setUpdateTime(new Date());
            if (IWtWaterCardCreateService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新制卡：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "删除制卡")
    @ApiOperation("删除制卡")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('water:wtWaterCardCreate:delete')")
    public Object deleteWtWaterCardCreate(@ApiParam("制卡id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("制卡id");
            }
            if (IWtWaterCardCreateService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除制卡：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "给制卡分配制卡")
    @ApiOperation("查询制卡明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('water:wtWaterCardCreate:read')")
    public Object getWtWaterCardCreateById(@ApiParam("制卡id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("制卡id");
            }
            WtWaterCardCreate coupon = IWtWaterCardCreateService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询制卡明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除制卡")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "water", REMARK = "批量删除制卡")
    @PreAuthorize("hasAuthority('water:wtWaterCardCreate:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = IWtWaterCardCreateService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "water", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, WtWaterCardCreate entity) {
        // 模拟从数据库获取需要导出的数据
        List<WtWaterCardCreate> personList = IWtWaterCardCreateService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", WtWaterCardCreate.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "water", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<WtWaterCardCreate> personList = EasyPoiUtils.importExcel(file, WtWaterCardCreate.class);
        IWtWaterCardCreateService.saveBatch(personList);
    }
}


