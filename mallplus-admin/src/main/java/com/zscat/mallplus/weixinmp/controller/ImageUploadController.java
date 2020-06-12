package com.zscat.mallplus.weixinmp.controller;


import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.set.utils.TimeUtil;
import com.zscat.mallplus.utils.CommonResult;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static com.zscat.mallplus.elias.config.CertPathConfig.uploadpath;

/**
 * @author lyn
 * @date 2020-05-22
 * 上传图片到本地公共接口
 */
@Slf4j
@RestController
@RequestMapping("/sys/upload")
public class ImageUploadController {
    public static final String path = "/opt/upFiles/upload";

    @SysLog(MODULE = "sys", REMARK = "上传图片到本地公共接口")
    @ApiOperation("上传图片到本地公共接口")
    @PostMapping(value = "/upload")
    public Object upload(@RequestParam MultipartFile mf) {
        try {
            String ctxPath = path;
            String bizPath = TimeUtil.getDate();
            String orgName = mf.getOriginalFilename();// 获取文件名
            String fileName = orgName.substring(0, orgName.lastIndexOf(".")) +"_" + System.currentTimeMillis()+ orgName.substring(orgName.indexOf("."));
            File file = new File(ctxPath + File.separator + bizPath + File.separator);
            if (!file.exists()) {
                file.mkdirs();// 创建文件根目录
            }
            String savePath = file.getPath() + File.separator + fileName;
            File savefile = new File(savePath);
            FileCopyUtils.copy(mf.getBytes(), savefile);
            String dbpath = null;
            if (!StringUtils.isEmpty(bizPath)) {
                dbpath = bizPath + File.separator + fileName;
            } else {
                dbpath = fileName;
            }
            if (dbpath.contains("\\")) {
                dbpath = dbpath.replace("\\", "/");
            }
            return new CommonResult().success(ctxPath + "/"+dbpath);
        } catch (IOException e) {
            log.info(e.getMessage() , e);
        }
        return new CommonResult().failed();
    }
}


