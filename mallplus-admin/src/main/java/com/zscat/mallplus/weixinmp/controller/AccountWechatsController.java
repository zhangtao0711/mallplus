package com.zscat.mallplus.weixinmp.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.weixinmp.entity.AccountWechats;
import com.zscat.mallplus.weixinmp.service.IAccountWechatsService;
import com.zscat.mallplus.util.EasyPoiUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.wxminiapp.service.IAccountWxappService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * @author lyn
 * @date 2020-05-22
 * 微信公众号
 */
@Slf4j
@RestController
@RequestMapping("/weixin/accountWechats")
public class AccountWechatsController {

    @Resource
    private IAccountWechatsService IAccountWechatsService;
    @Resource
    private IAccountWxappService accountWxappService;
    @Resource
    private JdbcTemplate jdbcTemplate;

    @SysLog(MODULE = "weixin", REMARK = "根据条件查询所有微信公众号列表")
    @ApiOperation("根据条件查询所有微信公众号列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('weixin:imsAccountWechats:read')")
    public Object getImsAccountWechatsByPage(AccountWechats entity,
                                             @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                             @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IAccountWechatsService.page(new Page<AccountWechats>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有微信公众号列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "weixin", REMARK = "保存微信公众号")
    @ApiOperation("保存微信公众号")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('weixin:imsAccountWechats:create')")
    public Object saveImsAccountWechats(@RequestBody @Valid AccountWechats entity) {
        //经销商和公众号直接是一对一关系
        AccountWechats accountWechats = new AccountWechats();
        accountWechats.setCreateBy(entity.getCreateBy());
        AccountWechats wechats = IAccountWechatsService.getOne(new QueryWrapper<>(accountWechats));
        if (wechats != null){
            return new CommonResult().failed("该经销商已经关联过公众号，请勿重复关联！");
        }
        Integer count = accountWxappService.getCount();
        entity.setUniacid(count);
        try {
            entity.setCreateTime(new Date());
            Map<String,Object> map = jdbcTemplate.queryForMap("select mp_url from admin_dsn_domin where id =1");
            entity.setAppdomain(map.get("mp_url").toString()+count.toString());
            if (IAccountWechatsService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存微信公众号：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "weixin", REMARK = "更新微信公众号")
    @ApiOperation("更新微信公众号")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('weixin:imsAccountWechats:update')")
    public Object updateImsAccountWechats(@RequestBody @Valid AccountWechats entity) {
        try {
            if (IAccountWechatsService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新微信公众号：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "weixin", REMARK = "删除微信公众号")
    @ApiOperation("删除微信公众号")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('weixin:imsAccountWechats:delete')")
    public Object deleteImsAccountWechats(@ApiParam("微信公众号id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("微信公众号id");
            }
            if (IAccountWechatsService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除微信公众号：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "weixin", REMARK = "给微信公众号分配微信公众号")
    @ApiOperation("查询微信公众号明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('weixin:imsAccountWechats:read')")
    public Object getImsAccountWechatsById(@ApiParam("微信公众号id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("微信公众号id");
            }
            AccountWechats wechats = new AccountWechats();
            wechats.setCreateBy(id);
            AccountWechats coupon = IAccountWechatsService.getOne(new QueryWrapper<>(wechats));
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询微信公众号明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除微信公众号")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "weixin", REMARK = "批量删除微信公众号")
    @PreAuthorize("hasAuthority('weixin:imsAccountWechats:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = IAccountWechatsService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "weixin", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, AccountWechats entity) {
        // 模拟从数据库获取需要导出的数据
        List<AccountWechats> personList = IAccountWechatsService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", AccountWechats.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "weixin", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<AccountWechats> personList = EasyPoiUtils.importExcel(file, AccountWechats.class);
        IAccountWechatsService.saveBatch(personList);
    }

    @ApiOperation(value = "生成3-32位的token")
    @RequestMapping(value = "/getToken", method = RequestMethod.GET)
    @SysLog(MODULE = "weixin", REMARK = "获取token-或者刷新token")
    public Object getToken(){
        String token = UUID.randomUUID().toString().trim().replaceAll("-", "").toUpperCase();
        return new CommonResult().success(token);
    }
}


