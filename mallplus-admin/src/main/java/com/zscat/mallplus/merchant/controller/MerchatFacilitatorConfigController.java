package com.zscat.mallplus.merchant.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.elias.CertificateDownloader;
import com.zscat.mallplus.elias.config.CertPathConfig;
import com.zscat.mallplus.encrypt.EncryptSensitive;
import com.zscat.mallplus.merchant.entity.MerchatFacilitatorConfig;
import com.zscat.mallplus.merchant.service.IMerchatFacilitatorConfigService;
import com.zscat.mallplus.util.EasyPoiUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import picocli.CommandLine;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mallplus
 * @date 2020-05-14
 * 服务商配置
 */
@Slf4j
@RestController
@RequestMapping("/merchat/merchatFacilitatorConfig")
public class MerchatFacilitatorConfigController {

    @Resource
    private IMerchatFacilitatorConfigService IMerchatFacilitatorConfigService;

    @SysLog(MODULE = "merchat", REMARK = "根据条件查询所有服务商配置列表")
    @ApiOperation("根据条件查询所有服务商配置列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('merchat:merchatFacilitatorConfig:read')")
    public Object getMerchatFacilitatorConfigByPage(MerchatFacilitatorConfig entity,
                                                    @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                    @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IMerchatFacilitatorConfigService.page(new Page<MerchatFacilitatorConfig>(pageNum, pageSize),new QueryWrapper<>(entity)));//
        } catch (Exception e) {
            log.error("根据条件查询所有服务商配置列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "merchat", REMARK = "保存服务商配置")
    @ApiOperation("保存服务商配置")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('merchat:merchatFacilitatorConfig:create')")
    public Object saveMerchatFacilitatorConfig(@RequestBody @Valid MerchatFacilitatorConfig entity) {
        try {
            entity.setCreateTime(new Date());
            if (IMerchatFacilitatorConfigService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存服务商配置：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "merchat", REMARK = "更新服务商配置")
    @ApiOperation("更新服务商配置")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('merchat:merchatFacilitatorConfig:update')")
    public Object updateMerchatFacilitatorConfig(@RequestBody @Valid MerchatFacilitatorConfig entity) {
        try {
            if (IMerchatFacilitatorConfigService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新服务商配置：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "merchat", REMARK = "删除服务商配置")
    @ApiOperation("删除服务商配置")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('merchat:merchatFacilitatorConfig:delete')")
    public Object deleteMerchatFacilitatorConfig(@ApiParam("服务商配置id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("服务商配置id");
            }
            if (IMerchatFacilitatorConfigService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除服务商配置：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "merchat", REMARK = "给服务商配置分配服务商配置")
    @ApiOperation("查询服务商配置明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('merchat:merchatFacilitatorConfig:read')")
    public Object getMerchatFacilitatorConfigById(@ApiParam("服务商配置id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("服务商配置id");
            }
            MerchatFacilitatorConfig coupon = IMerchatFacilitatorConfigService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询服务商配置明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除服务商配置")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "merchat", REMARK = "批量删除服务商配置")
    @PreAuthorize("hasAuthority('merchat:merchatFacilitatorConfig:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = IMerchatFacilitatorConfigService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "merchat", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, MerchatFacilitatorConfig entity) {
        // 模拟从数据库获取需要导出的数据
        List<MerchatFacilitatorConfig> personList = IMerchatFacilitatorConfigService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", MerchatFacilitatorConfig.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "merchat", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<MerchatFacilitatorConfig> personList = EasyPoiUtils.importExcel(file, MerchatFacilitatorConfig.class);
        IMerchatFacilitatorConfigService.saveBatch(personList);
    }

    @SysLog(MODULE = "merchat", REMARK = "第一次下载微信支付平台证书")
    @ApiOperation("保存服务商配置")
    @PostMapping(value = "/downloadPlatformCredential")
    @PreAuthorize("hasAuthority('merchat:merchatFacilitatorConfig:downloadPlatformCredential')")
    public Object downloadPlatformCredential(@RequestBody MerchatFacilitatorConfig entity) {
        String mchSerialNo = null;
        try {
            mchSerialNo = EncryptSensitive.getSerialNo(entity.getApiclientCert());
        } catch (IOException e) {
            e.printStackTrace();
            return new CommonResult().failed("获取序列号失败！");
        }
        String[] args = {"-k", entity.getApiv3key(), "-m", entity.getMchId(), "-f", entity.getPrivateKeyPath(),

                "-s", mchSerialNo, "-o", CertPathConfig.publicKeyPath, "-c", ""};
        try {
            CommandLine.run(new CertificateDownloader(), args);
        }catch (Exception e){
            return new CommonResult().failed();
        }
        Map<String,String> map = new HashMap<>();
        map.put("path",CertPathConfig.publicKeyPath+ File.separator + "wechatpay_" + mchSerialNo +"-" + System.currentTimeMillis() + ".pem");
        return new CommonResult().success(map);
    }

}


