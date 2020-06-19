package com.zscat.mallplus.wxminiapp.controller;


import cn.binarywang.wx.miniapp.api.WxMaCodeService;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.code.WxMaCodeCommitRequest;
import cn.binarywang.wx.miniapp.bean.code.WxMaCodeExtConfig;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.config.WxMaConfiguration;
import com.zscat.mallplus.util.EasyPoiUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.wxminiapp.entity.AccountWxapp;
import com.zscat.mallplus.wxminiapp.entity.AccountWxappVersion;
import com.zscat.mallplus.wxminiapp.service.IAccountWxappService;
import com.zscat.mallplus.wxminiapp.service.IAccountWxappVersionService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wang
 * @date 2020-05-25
 * 小程序版本
 */
@Slf4j
@RestController
@RequestMapping("/wxminiapp/accountWxappVersion")
public class AccountWxappVersionController {

    @Resource
    private IAccountWxappVersionService IAccountWxappVersionService;
    @Resource
    private IAccountWxappService appService;
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private WxMaConfiguration maConfiguration;

    @SysLog(MODULE = "wxminiapp", REMARK = "根据条件查询所有小程序版本列表")
    @ApiOperation("根据条件查询所有小程序版本列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('wxminiapp:imsAccountWxappVersion:read')")
    public Object getImsAccountWxappVersionByPage(AccountWxappVersion entity,
                                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                  @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IAccountWxappVersionService.page(new Page<AccountWxappVersion>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有小程序版本列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "wxminiapp", REMARK = "保存小程序版本")
    @ApiOperation("保存小程序版本")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('wxminiapp:imsAccountWxappVersion:create')")
    public Object saveImsAccountWxappVersion(@RequestBody @Valid AccountWxappVersion entity) {
        try {
            entity.setCreateTime(new Date());
            if (IAccountWxappVersionService.save(entity)) {
                //调用wx-java的接口实现上传代码到微信
                AccountWxapp app = appService.getById(entity.getUniacid());
                if (app==null){
                    return new CommonResult().failed("数据错误,请联系管理员！");
                }
                Map<String,Object> map = jdbcTemplate.queryForMap("select template_id from admin_dsn_domin where id =1");
                maConfiguration.init();
                final WxMaService wxService = WxMaConfiguration.getMaService(app.getKey());
                String themeColor = "#0074d9";
                String themeFontColor = "#ffffff";

                Map<String, Object> ext = new HashMap<>();
                ext.put("appName", entity.getName());
                ext.put("verified", true);
                ext.put("navigationBarBackgroundColor", themeColor);
                ext.put("navigationBarTextStyle", themeFontColor);
                ext.put("companyId", 4128);
                ext.put("companyFullName", "xxx有限公司");

                WxMaCodeService wxMaCodeService = wxService.getCodeService();
                WxMaCodeCommitRequest commitRequest = WxMaCodeCommitRequest
                        .builder()
                        .templateId(Long.valueOf(map.get("template_id").toString()))
                        .userVersion(entity.getVersion())
                        .userDesc(entity.getVersionDescribed())
                        .extConfig(WxMaCodeExtConfig.builder()
                                .extAppid(app.getKey())
                                .extEnable(true)
                                .ext(ext)
                                .window(
                                        WxMaCodeExtConfig.PageConfig
                                                .builder()
                                                .navigationBarBackgroundColor(themeColor)
                                                .navigationBarTextStyle(themeFontColor)
                                                .build()
                                )
                                .build())
                        .build();
                wxMaCodeService.commit(commitRequest);
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存小程序版本：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "wxminiapp", REMARK = "更新小程序版本")
    @ApiOperation("更新小程序版本")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('wxminiapp:imsAccountWxappVersion:update')")
    public Object updateImsAccountWxappVersion(@RequestBody AccountWxappVersion entity) {
        try {
            if (IAccountWxappVersionService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新小程序版本：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "wxminiapp", REMARK = "删除小程序版本")
    @ApiOperation("删除小程序版本")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('wxminiapp:imsAccountWxappVersion:delete')")
    public Object deleteImsAccountWxappVersion(@ApiParam("小程序版本id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("小程序版本id");
            }
            if (IAccountWxappVersionService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除小程序版本：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "wxminiapp", REMARK = "给小程序版本分配小程序版本")
    @ApiOperation("查询小程序版本明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('wxminiapp:imsAccountWxappVersion:read')")
    public Object getImsAccountWxappVersionById(@ApiParam("小程序版本id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("小程序版本id");
            }
            AccountWxappVersion coupon = IAccountWxappVersionService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询小程序版本明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除小程序版本")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "wxminiapp", REMARK = "批量删除小程序版本")
    @PreAuthorize("hasAuthority('wxminiapp:imsAccountWxappVersion:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = IAccountWxappVersionService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "wxminiapp", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, AccountWxappVersion entity) {
        // 模拟从数据库获取需要导出的数据
        List<AccountWxappVersion> personList = IAccountWxappVersionService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", AccountWxappVersion.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "wxminiapp", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<AccountWxappVersion> personList = EasyPoiUtils.importExcel(file, AccountWxappVersion.class);
        IAccountWxappVersionService.saveBatch(personList);
    }

    @SysLog(MODULE = "wxminiapp", REMARK = "获取体验版二维码")
    @ApiOperation("获取体验版二维码")
    @PostMapping(value = "/getQrCode")
//    @PreAuthorize("hasAuthority('wxminiapp:imsAccountWxappVersion:update')")
    public Object getQrCode(@RequestBody AccountWxappVersion entity) {
        AccountWxapp app = appService.getById(entity.getUniacid());
        if (app==null){
            return new CommonResult().failed("数据错误,请联系管理员！");
        }
        Map<String,Object> map = jdbcTemplate.queryForMap("select ma_path from admin_dsn_domin where id =1");
        final WxMaService wxService = WxMaConfiguration.getMaService(app.getKey());
        WxMaCodeService wxMaCodeService = wxService.getCodeService();
        try {
            if (map==null){
                byte[] qrCode = wxMaCodeService.getQrCode(null);
                return new CommonResult().success(qrCode);
            }
            byte[] qrCode = wxMaCodeService.getQrCode(map.get("ma_path").toString());
            return new CommonResult().success(qrCode);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return new CommonResult().failed("获取体验二维码失败！");
    }
}


