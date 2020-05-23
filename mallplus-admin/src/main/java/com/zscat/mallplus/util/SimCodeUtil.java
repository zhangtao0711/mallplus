package com.zscat.mallplus.util;


import com.alibaba.fastjson.JSONObject;
import com.zscat.mallplus.water.entity.WtSimUrlInfo;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


@Slf4j
public class SimCodeUtil {

//    public static String calcAuthorization(String source, String secretId, String secretKey, String datetime)
//            throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
//        String signStr = "x-date: " + datetime + "\n" + "x-source: " + source;
//        Mac mac = Mac.getInstance("HmacSHA1");
//        Key sKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), mac.getAlgorithm());
//        mac.init(sKey);
//        byte[] hash = mac.doFinal(signStr.getBytes("UTF-8"));
//        Encoder encoder = Base64.getEncoder();
//        String result = encoder.encodeToString(hash);
//        //String sig = new BASE64Encoder().encode(hash);
//
//        String auth = "hmac id=\"" + secretId + "\", algorithm=\"hmac-sha1\", headers=\"x-date x-source\", signature=\"" + result + "\"";
//        return auth;
//    }
    public static String MD5(String data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] array = md.digest(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }
    public static String urlencode(Map<?, ?> map) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(String.format("%s=%s",
                    URLEncoder.encode(entry.getKey().toString(), "UTF-8"),
                    URLEncoder.encode(entry.getValue().toString(), "UTF-8")
            ));
        }
        return sb.toString();
    }

    /** 妙月科技 */

    /**
     * 获取账号余额
     * @param info
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     * @throws InvalidKeyException
     * @throws Exception
     */
    public static String gerAmount(WtSimUrlInfo info) throws NoSuchAlgorithmException
            , UnsupportedEncodingException, InvalidKeyException,Exception {

//        Calendar cd = Calendar.getInstance();
//        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
//        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        String datetime = String.valueOf((int) (System.currentTimeMillis() / 1000));
        // 签名
        String auth = MD5("userId="+info.getAppid()+"&apikey="+info.getAppkey()+"&times="+datetime);
                ;
        // 请求方法
        String method = "GET";
//        // 请求头
//        Map<String, String> headers = new HashMap<String, String>();
//        headers.put("X-Source", source);
//        headers.put("X-Date", datetime);
//        headers.put("Authorization", auth);

        // 查询参数
        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("userId",info.getAppid());
        queryParams.put("times",datetime);
        queryParams.put("sign",auth);

//        // body参数
//        Map<String, String> bodyParams = new HashMap<String, String>();
//        bodyParams.put("vin", vin);
        // url参数拼接
        String url = info.getUrl()+"/api/v1/gerAmount";
        if (!queryParams.isEmpty()) {
            url += "?" + urlencode(queryParams);
        }

        BufferedReader in = null;

        //设置请求头
        HashMap<String, String > stringSendGetUtilHashMap = new HashMap<>();
        try {
            //发送GET请求
            String getback = SendPostUtil.sendGet(url, null);
            //解析返回参数
            JSONObject parse2 = (JSONObject)JSONObject.parse(getback);
            return parse2.getString("data");
           // System.out.println(result);

        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return null;
    }


}
