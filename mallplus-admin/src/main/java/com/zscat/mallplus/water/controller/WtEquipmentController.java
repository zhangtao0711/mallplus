package com.zscat.mallplus.water.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.StringUtils;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.core.kit.QrCodeKit;
import com.zscat.mallplus.util.ConstantUtil;
import com.zscat.mallplus.water.entity.WtEquipment;
import com.zscat.mallplus.water.entity.WtOpenApiInfo;
import com.zscat.mallplus.water.mapper.WtOpenApiInfoMapper;
import com.zscat.mallplus.water.service.IWtEquipmentService;
import com.zscat.mallplus.util.EasyPoiUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.wtUtil.WtOpenApiInfoUtils;
import com.zscat.mallplus.wtUtil.WtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Hashtable;
import java.util.List;
import java.util.Date;
import java.util.Map;

/**
 * @author lyn
 * @date 2020-05-22
 * 设备信息
 */
@Slf4j
@RestController
@Api(tags = "WtEquipmentController", description = "设备信息")
@RequestMapping("/water/wtEquipment")
public class WtEquipmentController {

    @Resource
    private IWtEquipmentService IWtEquipmentService;
    @Resource
    private WtOpenApiInfoMapper wtOpenApiInfoMapper;


    @SysLog(MODULE = "water", REMARK = "根据条件查询所有设备信息列表")
    @ApiOperation("根据条件查询所有设备信息列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('water:wtEquipment:read')")
    public Object getWtEquipmentByPage(WtEquipment entity,
                                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            entity.setDelFlag(ConstantUtil.delFlag);
//            return new CommonResult().success(IWtEquipmentService.page(new Page<WtEquipment>(pageNum, pageSize), new QueryWrapper<>(entity)));
            return new CommonResult().success(IWtEquipmentService.selectData(new Page<Map<String, Object>>(pageNum, pageSize),
                    entity));
        } catch (Exception e) {
            log.error("根据条件查询所有设备信息列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "保存设备信息")
    @ApiOperation("保存设备信息")
    @PostMapping(value = "/create")
//    @PreAuthorize("hasAuthority('water:wtEquipment:create')")
    public Object saveWtEquipment(@RequestBody WtEquipment entity, HttpServletRequest request) {
        try {
            //设备号
            if (ValidatorUtils.empty(entity.getEqcode())) {
                return new CommonResult().failed("设备号不能为空！");
            }
            //经纬度校验
            if(entity.getEqAddressLatitude()!=null && entity.getEqAddressLongitude()!=null){
                if(!WtUtils.checkItude(entity.getEqAddressLongitude(),entity.getEqAddressLatitude())){
                    return new CommonResult().failed("经纬度信息不正确，找不到对应的地址！");
                }
            }
            //判断设备是否存在
            if(IWtEquipmentService.getOne(new QueryWrapper<>(entity))!=null){
                return new CommonResult().failed("设备号已经存在，请勿重新添加！");
            }
            //添加设备到硬件平台
            WtOpenApiInfo coupon = new WtOpenApiInfo();
            coupon = wtOpenApiInfoMapper.selectOne(new QueryWrapper<>(coupon));
            Map<String, String> result = WtOpenApiInfoUtils.saveDevice(coupon,entity);
            //设备二维码生成
            entity.setQrCodeUrl(createQrCode(entity.getEqcode(),request));
            if(ValidatorUtils.empty(entity.getQrCodeUrl())){
                return new CommonResult().failed("二维码生成失败！");
            }
            if(result!=null && result.toString().indexOf("\"status\":200")!=-1){
                entity.setDelFlag(ConstantUtil.delFlag);
                entity.setCreateTime(new Date());
                if (IWtEquipmentService.save(entity)) {
                    return new CommonResult().success();
                }
            }else{
                log.error("硬件平台数据添加失败：%s",result.get("message"));
                return new CommonResult().failed("添加失败！");
            }

        } catch (Exception e) {
            log.error("保存设备信息：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    /**
     * 生成设备的二维码
     * eqcode：二维码内容
     */
    private String createQrCode(String eqcode, HttpServletRequest request){
        int width = 300; // 二维码图片宽度
        int height = 300; // 二维码图片高度
        String format = ".png";// 二维码的图片格式
        try{
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); // 内容所使用字符集编码
            String url=ConstantUtil.portal_url+"/api/water/getWaterCardByEq/"+eqcode;

            // 生成二维码
            // 存放上传图片的文件夹
            String path="/opt/upFiles/upload";
            File file = new File(path + File.separator);
            if (!file.exists()) {
                file.mkdirs();// 创建文件根目录
            }
            Boolean encode = QrCodeKit.encode(eqcode, BarcodeFormat.QR_CODE, 3
                    , ErrorCorrectionLevel.H, "png", width, height, file.getPath()+"\\"+eqcode+format);
//            //生成二维码保存的路径
//            log.info(ResourceUtils.getURL("classpath:").getPath());

            if(encode){
                return file.getPath()+"\\"+eqcode+".png";
            }else{
                return "";
            }
        }catch (Exception e){
            return "";
        }
    }

    @SysLog(MODULE = "water", REMARK = "激活设备")
    @ApiOperation("激活设备")
    @PostMapping(value = "/batchDeployOn")
//    @PreAuthorize("hasAuthority('water:wtEquipment:create')")
    public Object batchDeployOn(@RequestParam("eqcodes") List<String> eqcodes) {
        try {
            //激活设备硬件平台
            WtOpenApiInfo coupon = new WtOpenApiInfo();
            coupon = wtOpenApiInfoMapper.selectOne(new QueryWrapper<>(coupon));
            Map<String, String> result = WtOpenApiInfoUtils.batchDeployOn(coupon,eqcodes);

            if(result!=null && result.toString().indexOf("\"status\":200")!=-1){
                 return new CommonResult().success();
            }else{
                log.error("激活设备失败：%s",result.get("message"));
                return new CommonResult().failed("激活设备失败！");
            }
        } catch (Exception e) {
            log.error("激活设备：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
    }
    @SysLog(MODULE = "water", REMARK = "注销设备")
    @ApiOperation("注销设备")
    @PostMapping(value = "/batchDeployOff")
//    @PreAuthorize("hasAuthority('water:wtEquipment:create')")
    public Object batchDeployOff(@RequestParam("eqcodes") List<String> eqcodes) {
        try {
            //激活设备硬件平台
            WtOpenApiInfo coupon = new WtOpenApiInfo();
            coupon = wtOpenApiInfoMapper.selectOne(new QueryWrapper<>(coupon));
            Map<String, String> result = WtOpenApiInfoUtils.batchDeployOff(coupon,eqcodes);

            if(result!=null && result.toString().indexOf("\"status\":200")!=-1){
                return new CommonResult().success();
            }else{
                log.error("注销设备失败：%s",result.get("message"));
                return new CommonResult().failed("注销设备失败！");
            }
        } catch (Exception e) {
            log.error("注销设备：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
    }

    @SysLog(MODULE = "water", REMARK = "远程开机")
    @ApiOperation("远程开机")
    @PostMapping(value = "/equipmentOn/{eqcode}")
    @PreAuthorize("hasAuthority('water:wtEquipment:update')")
    public Object equipmentOn(@ApiParam("设备号") @PathVariable String eqcode) {
        //远程开机
        WtOpenApiInfo coupon = new WtOpenApiInfo();
        coupon = wtOpenApiInfoMapper.selectOne(new QueryWrapper<>(coupon));
//        Map<String, String> result = WtOpenApiInfoUtils.batchDeployOn(coupon,eqcodes);

//        if(result!=null && result.toString().indexOf("\"status\":200")!=-1){
//            return new CommonResult().success();
//        }else{
//            log.error("远程开机失败：%s",result.get("message"));
//            return new CommonResult().failed("远程开机失败！");
//        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "限制消费")
    @ApiOperation("限制消费")
    @PostMapping(value = "/equipmentLimitSet/{eqcode}")
    @PreAuthorize("hasAuthority('water:wtEquipment:update')")
    public Object equipmentLimitSet(@ApiParam("设备号") @PathVariable String eqcode) {
        //限制消费
        WtOpenApiInfo coupon = new WtOpenApiInfo();
        coupon = wtOpenApiInfoMapper.selectOne(new QueryWrapper<>(coupon));
//        Map<String, String> result = WtOpenApiInfoUtils.batchDeployOn(coupon,eqcodes);

//        if(result!=null && result.toString().indexOf("\"status\":200")!=-1){
//            return new CommonResult().success();
//        }else{
//            log.error("限制消费失败：%s",result.get("message"));
//            return new CommonResult().failed("限制消费失败！");
//        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "更新设备信息")
    @ApiOperation("更新设备信息")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('water:wtEquipment:update')")
    public Object updateWtEquipment(@RequestBody WtEquipment entity) {
        try {
            if (IWtEquipmentService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新设备信息：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "删除设备信息")
    @ApiOperation("删除设备信息")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('water:wtEquipment:delete')")
    public Object deleteWtEquipment(@ApiParam("设备信息id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("设备信息id");
            }
            if (IWtEquipmentService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除设备信息：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "water", REMARK = "给设备信息分配设备信息")
    @ApiOperation("查询设备信息明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('water:wtEquipment:read')")
    public Object getWtEquipmentById(@ApiParam("设备信息id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("设备信息id");
            }
            WtEquipment coupon = IWtEquipmentService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询设备信息明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除设备信息")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "water", REMARK = "批量删除设备信息")
    @PreAuthorize("hasAuthority('water:wtEquipment:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = IWtEquipmentService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "water", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, WtEquipment entity) {
        // 模拟从数据库获取需要导出的数据
        List<WtEquipment> personList = IWtEquipmentService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", WtEquipment.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "water", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<WtEquipment> personList = EasyPoiUtils.importExcel(file, WtEquipment.class);
        IWtEquipmentService.saveBatch(personList);
    }
}


