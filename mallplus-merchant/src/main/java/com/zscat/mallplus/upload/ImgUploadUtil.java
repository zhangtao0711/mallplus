package com.zscat.mallplus.upload;

import com.alibaba.fastjson.JSONObject;
import com.zscat.mallplus.elias.config.CertPathConfig;
import com.zscat.mallplus.encrypt.EncryptSensitive;
import com.zscat.mallplus.merchat.utils.MerchantUtil;
import com.zscat.mallplus.merchat.utils.SSLContextUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * 图片上传工具
 */
public class ImgUploadUtil {

    private static final String uploadUrl = "https://api.mch.weixin.qq.com/v3/merchant/media/upload";

    /**
     * 图片上传到微信
     * @param file
     * @param mchId
     * @return
     * @throws IOException
     */
    public static String getV3MediaID(File file,String mchId,String privateKeyPath,String apiclientCertP12,String apiclientCertPath) throws Exception {
        String serialNo = EncryptSensitive.getSerialNo(apiclientCertPath);
        String reqdata ="{\"filename\":"+ "\""+file.getName()+"\"," +"\"sha256\":" + "\""+ DigestUtils.sha256Hex(new FileInputStream(file)) + "\"}";
        System.out.println(reqdata);
        String token = MerchantUtil.getToken("POST",uploadUrl,reqdata,mchId,serialNo,privateKeyPath);
        System.out.println("签名：******"+token);
        String authorization = "WECHATPAY2-SHA256-RSA2048 " + token;
        HttpPost httpPost = new HttpPost(uploadUrl);
//        httpPost.setHeader("Wechatpay-Serial", serialNo);
        httpPost.setHeader("Accept", "application/json");
//        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("user-agent", "application/json");//Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.108 Safari/537.36
        httpPost.setHeader("Content-Type","multipart/form-data;boundary=boundary");
        httpPost.setHeader("Authorization", authorization);

        //创建MultipartEntityBuilder
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create().setMode(HttpMultipartMode.RFC6532);
        //设置boundary
        multipartEntityBuilder.setBoundary("boundary");
        multipartEntityBuilder.setCharset(Charset.forName("UTF-8"));
        //设置meta内容
        multipartEntityBuilder.addTextBody("meta","{\"filename\":\""+file.getName()+"\",\"sha256\":\""+DigestUtils.sha256Hex(new FileInputStream(file))+"\"}", ContentType.APPLICATION_JSON);
        //设置图片内容
        multipartEntityBuilder.addBinaryBody("file", file, ContentType.create("image/jpg"), file.getName());
        //放入内容
        httpPost.setEntity(multipartEntityBuilder.build());

        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        CloseableHttpClient httpClient = httpClientBuilder.setSSLContext(SSLContextUtils.getSSLContext(apiclientCertP12,mchId)).build();
        CloseableHttpResponse response = httpClient.execute(httpPost);
        int statusCode = response.getStatusLine().getStatusCode();
        String body = EntityUtils.toString(response.getEntity());
        response.close();
        if (statusCode == 200) {
            System.out.println("body:" + body);
            JSONObject jasonObject = JSONObject.parseObject(body);
            return jasonObject.getString("media_id");
        } else {
            System.out.println("upload failed,resp code=" + statusCode + ",body=" + body);
            throw new IOException("request failed");
        }
    }

    public static void main(String[] args) throws Exception {
        String path = "C:\\Users\\Administrator\\Pictures\\temp\\zhengmian.jpeg";
        File file = new File(path);
        if (!file.exists()){
            System.out.println("图片不存在");
        }
//        getV3MediaID(file,"1527256251",CertPathConfig.privateKeyPath,CertPathConfig.apiclient_cert_P12);
//        String path1 = "/opt/merchant/upload/学习_1589532762390.txt";
    }
}
