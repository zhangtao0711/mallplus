package com.zscat.mallplus.merchat.utils;

import ch.qos.logback.core.util.TimeUtil;
import com.zscat.mallplus.sdk.util.PemUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import org.apache.commons.codec.binary.Base64;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.Signature;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.zscat.mallplus.elias.config.CertPathConfig.uploadpath;

@Slf4j
public class MerchantUtil {

    public static final String receptionPrefix = "APPLYMENT";
    public static final String selfDigit = "6";

    /**
     * 自增随机数
     * 没有就从0001开始
     *
     * @param equipmentType 自增数之前的字符串
     * @param maxString     从数据库获取到的最新的一条数据的业务单号截取的后四位
     * @return
     */
    public static String createSelfIncrement(String equipmentType, String maxString) {
        String maxNo = String.format("%0" + selfDigit + "d", 1);
        if (maxString != null && !maxString.isEmpty()) {
            String maxs = maxString.substring(maxString.length() - Integer.parseInt(selfDigit), maxString.length());
            try {
                Integer max = Integer.parseInt(maxs);
                max += 1;
                maxNo = String.format("%0" + selfDigit + "d", max);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }
        return equipmentType + maxNo;
    }

    public static String dateToString(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
//        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
        return df.format(date);
    }

    public static boolean intToBool(Integer integer) {
        if (integer == 1) {
            return true;
        }
        return false;
    }

    /**
     * 本地文件上传
     *
     * @param mf   文件
     * @param bizPath 自定义路径
     * @return
     */
    public static String uploadLocal(MultipartFile mf, String bizPath) {
        try {
            String ctxPath = uploadpath;
            String fileName = null;
            String orgName = mf.getOriginalFilename();// 获取文件名
            if (!StringUtils.isEmpty(bizPath)){
                bizPath= bizPath+ "_" + new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                fileName = orgName.substring(0, orgName.lastIndexOf(".")) + orgName.substring(orgName.indexOf("."));
            }else {
                fileName = orgName.substring(0, orgName.lastIndexOf(".")) +"_" + System.currentTimeMillis()+ orgName.substring(orgName.indexOf("."));

            }
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
            return ctxPath + "/"+dbpath;
        } catch (IOException e) {
            log.info(e.getMessage() , e);
        }
        return "";
    }

    public static HttpServletResponse download(HttpServletRequest request, HttpServletResponse response) {
        String path = extractPathFromPattern(request);
        // 其余处理略
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            path = path.replace("..", "");
            if (path.endsWith(",")) {
                path = path.substring(0, path.length() - 1);
            }
            String filePath = uploadpath + File.separator + path;
            String downloadFilePath = uploadpath + File.separator + filePath;
            File file = new File(downloadFilePath);
            response.setContentType("application/force-download");// 设置强制下载不打开
            response.addHeader("Content-Disposition", "attachment;fileName=" + new String(file.getName().getBytes("UTF-8"),"iso-8859-1"));
            inputStream = new BufferedInputStream(new FileInputStream(filePath));
            outputStream = response.getOutputStream();
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
            response.flushBuffer();
        } catch (IOException e) {
            log.error("预览文件失败" + e.getMessage());
            // e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return response;
    }

    /**
     *  把指定URL后的字符串全部截断当成参数
     *  这么做是为了防止URL中包含中文或者特殊字符（/等）时，匹配不了的问题
     * @param request
     * @return
     */
    private static String extractPathFromPattern(final HttpServletRequest request) {
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        return new AntPathMatcher().extractPathWithinPattern(bestMatchPattern, path);
    }
    /**
     * 获取签名
     * method(请求类型GET、POST url(请求url)
     * body(请求body，GET请求时body传""，POST请求时body为请求参数的json串)
     * merchantId(商户号)
     * certSerialNo(API证书序列号)
     * keyPath(API证书路径)
     *
     * @param method       请求方式
     * @param url          请求路径
     * @param body         请求参数
     * @param merchantId   商户号
     * @param certSerialNo API证书序列号
     * @return
     * @throws Exception
     */
    public static String getToken(String method, String url, String body, String merchantId, String certSerialNo,String privateKeyPath) throws Exception {
        String signStr = "";
        HttpUrl httpurl = HttpUrl.parse(url);
        String nonceStr = UUID.randomUUID().toString().replaceAll("-", "");
        long timestamp = System.currentTimeMillis() / 1000;
        if (StringUtils.isEmpty(body)) {
            body = "";
        }
        assert httpurl != null;
        String message = buildMessage(method, httpurl, timestamp, nonceStr, body);
        String signature = sign(message.getBytes(StandardCharsets.UTF_8), privateKeyPath);
        signStr = "mchid=\"" + merchantId
                + "\",nonce_str=\"" + nonceStr
                + "\",timestamp=\"" + timestamp
                + "\",serial_no=\"" + certSerialNo
                + "\",signature=\"" + signature + "\"";
        return signStr;
    }

    public static String buildMessage(String method, HttpUrl url, long timestamp, String nonceStr, String body) {
        String canonicalUrl = url.encodedPath();
        if (url.encodedQuery() != null) {
            canonicalUrl += "?" + url.encodedQuery();
        }
        return method + "\n"
                + canonicalUrl + "\n"
                + timestamp + "\n"
                + nonceStr + "\n"
                + body + "\n";
    }


    public static String sign(byte[] message, String keyPath) throws Exception {
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(PemUtil.loadPrivateKey(new FileInputStream(keyPath)));
        sign.update(message);
        return Base64.encodeBase64String(sign.sign());
    }
}
