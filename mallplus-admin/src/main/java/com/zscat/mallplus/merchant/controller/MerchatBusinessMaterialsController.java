package com.zscat.mallplus.merchant.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.elias.config.CertPathConfig;
import com.zscat.mallplus.encrypt.EncryptSensitive;
import com.zscat.mallplus.merchant.entity.MerchatBusinessMaterials;
import com.zscat.mallplus.merchant.entity.MerchatFacilitatorConfig;
import com.zscat.mallplus.merchant.service.IMerchatFacilitatorConfigService;
import com.zscat.mallplus.merchat.utils.MerchantUtil;
import com.zscat.mallplus.sdk.WechatPayHttpClientBuilder;
import com.zscat.mallplus.sdk.util.PemUtil;
import com.zscat.mallplus.upload.ImgUploadUtil;
import com.zscat.mallplus.upload.VideoUploadUtil;
import com.zscat.mallplus.util.EasyPoiUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.crypto.IllegalBlockSizeException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author mallplus
 * @date 2020-05-14
 * 特约商户进件
 */
@Slf4j
@RestController
@RequestMapping("/merchat/merchatBusinessMaterials")
public class MerchatBusinessMaterialsController {

    @Resource
    private com.zscat.mallplus.merchant.service.IMerchatBusinessMaterialsService IMerchatBusinessMaterialsService;
    @Resource
    private IMerchatFacilitatorConfigService IMerchatFacilitatorConfigService;

    @SysLog(MODULE = "merchat", REMARK = "根据条件查询所有特约商户进件列表")
    @ApiOperation("根据条件查询所有特约商户进件列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('merchat:merchatBusinessMaterials:read')")
    public Object getMerchatBusinessMaterialsByPage(MerchatBusinessMaterials entity,
                                                    @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                    @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            QueryWrapper<MerchatBusinessMaterials> queryWrapper = new QueryWrapper<>(entity);
            if (queryWrapper.isEmptyOfWhere()){
                queryWrapper.wait();
            }
            return new CommonResult().success(IMerchatBusinessMaterialsService.page(new Page<MerchatBusinessMaterials>(pageNum, pageSize), queryWrapper));
        } catch (Exception e) {
            log.error("根据条件查询所有特约商户进件列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "merchat", REMARK = "保存特约商户进件")
    @ApiOperation("保存特约商户进件")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('merchat:merchatBusinessMaterials:create')")
    public Object saveMerchatBusinessMaterials(@RequestBody @Valid MerchatBusinessMaterials entity) {
        try {
            entity.setCreateTime(new Date());
            if (IMerchatBusinessMaterialsService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存特约商户进件：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "merchat", REMARK = "更新特约商户进件")
    @ApiOperation("更新特约商户进件")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('merchat:merchatBusinessMaterials:update')")
    public Object updateMerchatBusinessMaterials(@RequestBody @Valid MerchatBusinessMaterials entity) {
        try {
            if (IMerchatBusinessMaterialsService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新特约商户进件：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "merchat", REMARK = "删除特约商户进件")
    @ApiOperation("删除特约商户进件")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('merchat:merchatBusinessMaterials:delete')")
    public Object deleteMerchatBusinessMaterials(@ApiParam("特约商户进件id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("特约商户进件id");
            }
            if (IMerchatBusinessMaterialsService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除特约商户进件：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "merchat", REMARK = "给特约商户进件分配特约商户进件")
    @ApiOperation("查询特约商户进件明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('merchat:merchatBusinessMaterials:read')")
    public Object getMerchatBusinessMaterialsById(@ApiParam("特约商户进件id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("特约商户进件id");
            }
            MerchatBusinessMaterials coupon = IMerchatBusinessMaterialsService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询特约商户进件明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除特约商户进件")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "merchat", REMARK = "批量删除特约商户进件")
    @PreAuthorize("hasAuthority('merchat:merchatBusinessMaterials:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = IMerchatBusinessMaterialsService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "merchat", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, MerchatBusinessMaterials entity) {
        // 模拟从数据库获取需要导出的数据
        List<MerchatBusinessMaterials> personList = IMerchatBusinessMaterialsService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", MerchatBusinessMaterials.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "merchat", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<MerchatBusinessMaterials> personList = EasyPoiUtils.importExcel(file, MerchatBusinessMaterials.class);
        IMerchatBusinessMaterialsService.saveBatch(personList);
    }

    @SysLog(MODULE = "merchat", REMARK = "特约商户申请接口")
    @PostMapping("/applyForMerchant")
    @PreAuthorize("hasAuthority('merchat:merchatBusinessMaterials:applyForMerchant')")
    public Object applyForMerchant(@RequestBody @Valid MerchatBusinessMaterials merchatBusinessMaterials) throws IOException, IllegalBlockSizeException {
        String url = "https://api.mch.weixin.qq.com/v3/applyment4sub/applyment/";
        // 1.生成商户的申请唯一单号
        String business_code = IMerchatBusinessMaterialsService.queryMax("merchat_business_materials", "business_code");
        //2.获取配置的服务商信息
        MerchatFacilitatorConfig merchatFacilitatorConfig = IMerchatFacilitatorConfigService.getOne(new QueryWrapper<>(new MerchatFacilitatorConfig()));
        String merchantId = merchatFacilitatorConfig.getMchId();
        //3.获取序列号
        String serialNo = EncryptSensitive.getSerialNo(CertPathConfig.publicKeyPath);
        //4. 拼装请求信息 使用json请求
        merchatBusinessMaterials.setBusinessCode(business_code);
        Map<String, Object> requestMap =IMerchatBusinessMaterialsService.getBody(merchatBusinessMaterials);
        String requestParams = JSONObject.toJSONString(requestMap);
        // 平台证书路径 是必须的
        X509Certificate certificate = PemUtil.loadCertificate(new FileInputStream(CertPathConfig.publicKeyPath));
        ArrayList<X509Certificate> listCertificates = new ArrayList<>();
        listCertificates.add(certificate);
        WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                .withMerchant(merchantId, serialNo, PemUtil.loadPrivateKey(new FileInputStream(CertPathConfig.privateKeyPath)))
                .withWechatpay(listCertificates);
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Accept", "application/json");
        httpPost.setEntity(new StringEntity(requestParams, "UTF-8"));
        CloseableHttpClient httpClient = builder.build();

        CloseableHttpResponse response = httpClient.execute(httpPost);

        int statusCode = response.getStatusLine().getStatusCode();
        String body = EntityUtils.toString(response.getEntity());
        if (statusCode == 200) {
            System.out.println("body:" + body);
            JSONObject jasonObject = JSONObject.parseObject(body);
            String applyment_id = jasonObject.getString("applyment_id");
            merchatBusinessMaterials.setApplymentId(applyment_id);
            IMerchatBusinessMaterialsService.save(merchatBusinessMaterials);
            return new CommonResult().success("操作成功，请五分钟以后查询您的申请结果！");
        } else {
            System.out.println("apply failed,resp code=" + statusCode + ",body=" + body);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "merchat", REMARK = "通过业务申请编号查询申请状态")
    @PostMapping("/queryByBusinessCode")
    public Object queryByBusinessCode(@RequestParam("businessCode") String businessCode) throws IOException {
        String url = "https://api.mch.weixin.qq.com/v3/applyment4sub/applyment/business_code/"+businessCode;
        //1.获取唯一的申请信息
        MerchatBusinessMaterials materials = new MerchatBusinessMaterials();
        materials.setBusinessCode(businessCode);
        MerchatBusinessMaterials businessMaterials = IMerchatBusinessMaterialsService.getOne(new QueryWrapper<>(materials));
        //2.获取配置的服务商信息
        MerchatFacilitatorConfig merchatFacilitatorConfig = IMerchatFacilitatorConfigService.getOne(new QueryWrapper<>(new MerchatFacilitatorConfig()));
        String merchantId = merchatFacilitatorConfig.getMchId();
        //3.获取序列号
        String serialNo = EncryptSensitive.getSerialNo(CertPathConfig.publicKeyPath);
        // 平台证书路径 是必须的
        X509Certificate certificate = PemUtil.loadCertificate(new FileInputStream(CertPathConfig.publicKeyPath));
        ArrayList<X509Certificate> listCertificates = new ArrayList<>();
        listCertificates.add(certificate);
        WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                .withMerchant(merchantId, serialNo, PemUtil.loadPrivateKey(new FileInputStream(CertPathConfig.privateKeyPath)))
                .withWechatpay(listCertificates);
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Accept", "application/json");
        CloseableHttpClient httpClient = builder.build();

        CloseableHttpResponse response = httpClient.execute(httpGet);
        String body = EntityUtils.toString(response.getEntity());
        System.out.println("body:" + body);
        JSONObject jasonObject = JSONObject.parseObject(body);
        String applyment_state = jasonObject.getString("applyment_state");
        businessMaterials.setApplymentState(applyment_state);
        if (applyment_state.equals("APPLYMENT_STATE_FINISHED")) {
            String sub_mchid = jasonObject.getString("sub_mchid");
            businessMaterials.setSubMchid(sub_mchid);
        }
        String sign_url = jasonObject.getString("sign_url");
        businessMaterials.setSignUrl(sign_url);
        String applyment_state_msg = jasonObject.getString("applyment_state_msg");
        businessMaterials.setApplymentStateMsg(applyment_state_msg);
        if (applyment_state.equals("APPLYMENT_STATE_REJECTED")){
            String field  = jasonObject.getString("field");
            businessMaterials.setField(field);
            String field_name  = jasonObject.getString("field_name");
            businessMaterials.setFieldName(field_name);
            String reject_reason  = jasonObject.getString("reject_reason");
            businessMaterials.setRejectReason(reject_reason);
        }
        IMerchatBusinessMaterialsService.updateById(businessMaterials);
        return new CommonResult().success(businessMaterials);
    }

    @SysLog(MODULE = "merchat", REMARK = "通过申请单号查询申请状态")
    @PostMapping("/queryByApplymentId")
    public Object queryByApplymentId(@RequestParam("applyment_id") String applymentId) throws IOException {
        String url = "https://api.mch.weixin.qq.com/v3/applyment4sub/applyment/business_code/"+applymentId;
        //1.获取唯一的申请信息
        MerchatBusinessMaterials materials = new MerchatBusinessMaterials();
        materials.setApplymentId(applymentId);
        MerchatBusinessMaterials businessMaterials = IMerchatBusinessMaterialsService.getOne(new QueryWrapper<>(materials));
        //2.获取配置的服务商信息
        MerchatFacilitatorConfig merchatFacilitatorConfig = IMerchatFacilitatorConfigService.getOne(new QueryWrapper<>(new MerchatFacilitatorConfig()));
        String merchantId = merchatFacilitatorConfig.getMchId();
        //3.获取序列号
        String serialNo = EncryptSensitive.getSerialNo(CertPathConfig.publicKeyPath);
        // 平台证书路径 是必须的
        X509Certificate certificate = PemUtil.loadCertificate(new FileInputStream(CertPathConfig.publicKeyPath));
        ArrayList<X509Certificate> listCertificates = new ArrayList<>();
        listCertificates.add(certificate);
        WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                .withMerchant(merchantId, serialNo, PemUtil.loadPrivateKey(new FileInputStream(CertPathConfig.privateKeyPath)))
                .withWechatpay(listCertificates);
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Accept", "application/json");
        CloseableHttpClient httpClient = builder.build();

        CloseableHttpResponse response = httpClient.execute(httpGet);
        String body = EntityUtils.toString(response.getEntity());
        System.out.println("body:" + body);
        JSONObject jasonObject = JSONObject.parseObject(body);
        String applyment_state = jasonObject.getString("applyment_state");
        businessMaterials.setApplymentState(applyment_state);
        if (applyment_state.equals("APPLYMENT_STATE_FINISHED")) {
            String sub_mchid = jasonObject.getString("sub_mchid");
            businessMaterials.setSubMchid(sub_mchid);
        }
        String sign_url = jasonObject.getString("sign_url");
        businessMaterials.setSignUrl(sign_url);
        String applyment_state_msg = jasonObject.getString("applyment_state_msg");
        businessMaterials.setApplymentStateMsg(applyment_state_msg);
        if (applyment_state.equals("APPLYMENT_STATE_REJECTED")){
            String field  = jasonObject.getString("field");
            businessMaterials.setField(field);
            String field_name  = jasonObject.getString("field_name");
            businessMaterials.setFieldName(field_name);
            String reject_reason  = jasonObject.getString("reject_reason");
            businessMaterials.setRejectReason(reject_reason);
        }
        IMerchatBusinessMaterialsService.updateById(businessMaterials);
        return new CommonResult().success(businessMaterials);
    }

    @SysLog(MODULE = "merchat", REMARK = "图片上传API")
    @PostMapping("/imageUpload")
    public Object imageUpload(@RequestBody MultipartFile multipartFile) throws Exception {
        if (multipartFile==null){
            return new CommonResult().failed("上传图片不能为空！");
        }
        String fileName = multipartFile.getOriginalFilename();
        File file = File.createTempFile(fileName, fileName.substring(fileName.lastIndexOf(".")));
        multipartFile.transferTo(file);
        MerchatFacilitatorConfig merchatFacilitatorConfig = IMerchatFacilitatorConfigService.getById(1);
        String merchantId = merchatFacilitatorConfig.getMchId();
        String privateKeyPath = merchatFacilitatorConfig.getPrivateKeyPath();
        String mediaId = ImgUploadUtil.getV3MediaID(file,merchantId,privateKeyPath,merchatFacilitatorConfig.getApiclientCertP12());
        if (mediaId!=null){
            return new CommonResult().success(mediaId);
        }
        return new CommonResult().failed("上传失败！");
    }

    @SysLog(MODULE = "merchat", REMARK = "视频上传API")
    @PostMapping("/videoUpload")
    public Object videoUpload(@RequestParam File file) throws Exception {
//        String fileName = multipartFile.getOriginalFilename();
//        File file = File.createTempFile(fileName, fileName.substring(fileName.lastIndexOf(".")).substring(fileName.lastIndexOf(".")));
//        multipartFile.transferTo(file);
        MerchatFacilitatorConfig merchatFacilitatorConfig = IMerchatFacilitatorConfigService.getOne(new QueryWrapper<>(new MerchatFacilitatorConfig()));
        String merchantId = merchatFacilitatorConfig.getMchId();
        String privateKeyPath = merchatFacilitatorConfig.getPrivateKeyPath();
        String mediaId = VideoUploadUtil.getV3VideoMediaID(file,merchantId,privateKeyPath,merchatFacilitatorConfig.getApiclientCertP12());
        if (mediaId!=null){
            return new CommonResult().success(mediaId);
        }
        return new CommonResult().failed("上传失败！");
    }

    @SysLog(MODULE = "merchat", REMARK = "上传文件到本地-专用")
    @PostMapping("/uploadLocal")
    public Object uploadLocal(@RequestParam MultipartFile file){
        String path = MerchantUtil.uploadLocal(file,"");
        if (path.isEmpty()){
            return new CommonResult().failed("文件上传失败！");
        }
        return new CommonResult().success(path);
    }

    /**
     * 下载文件
     *
     * @param request
     * @param response
     */
    @GetMapping(value = "/download")
    public void download(String path,HttpServletRequest request, HttpServletResponse response) {
        response = MerchantUtil.download(request,response);
    }
}


