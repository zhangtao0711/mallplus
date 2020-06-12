package com.zscat.mallplus.util;


import com.alibaba.fastjson.JSONObject;
import com.zscat.mallplus.water.entity.WtOpenApiInfo;
import com.zscat.mallplus.water.entity.WtSimUrlInfo;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * SIM卡第三节接口调用
 */
@Slf4j
public class WtOpenApiInfoUtils {


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
//    /** 极速数据*/
//    public static String getVin(String vin) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
//
//        String secretKey = "ca3b79957a2b1a29";
//        String source = "market";
//
//        Calendar cd = Calendar.getInstance();
//        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
//        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
//        String datetime = sdf.format(cd.getTime());
//        // 请求方法
//        String method = "POST";
//        // 请求头
//        Map<String, String> headers = new HashMap<String, String>();
//        headers.put("X-Source", source);
//        headers.put("X-Date", datetime);
////        headers.put("Authorization", auth);
//
//        // 查询参数
//        Map<String, String> queryParams = new HashMap<String, String>();
//
//        // body参数
//        Map<String, String> bodyParams = new HashMap<String, String>();
//        bodyParams.put("vin", vin);
//        // url参数拼接
//        String url = "https://api.jisuapi.com/vin/query?appkey="+secretKey+"&vin="+vin;
////        String url = "https://api.jisuapi.com/vin/query?appkey="+secretKey+"&vin=LSVAL41Z882104202";
//
//        BufferedReader in = null;
//        try {
//            URL realUrl = new URL(url);
//            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
//            conn.setConnectTimeout(5000);
//            conn.setReadTimeout(5000);
//            conn.setRequestMethod(method);
//
//            // request headers
//            for (Map.Entry<String, String> entry : headers.entrySet()) {
//                conn.setRequestProperty(entry.getKey(), entry.getValue());
//            }
//
//            // request body
//            Map<String, Boolean> methods = new HashMap<>();
//            methods.put("POST", true);
//            methods.put("PUT", true);
//            methods.put("PATCH", true);
//            Boolean hasBody = methods.get(method);
//            if (hasBody != null) {
//                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//
//                conn.setDoOutput(true);
//                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
//                out.writeBytes(urlencode(bodyParams));
//                out.flush();
//                out.close();
//            }
//
//            // 定义 BufferedReader输入流来读取URL的响应
//            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            String line;
//            String result = "";
//            while ((line = in.readLine()) != null) {
//                result += line;
//            }
//
////            Map map=new HashMap();
////            map.put("String",result);
//            return result;
////            return map;
//            // System.out.println(result);
//
//        } catch (Exception e) {
//            System.out.println(e);
//            e.printStackTrace();
//        } finally {
//            try {
//                if (in != null) {
//                    in.close();
//                }
//            } catch (Exception e2) {
//                e2.printStackTrace();
//            }
//        }
//        return null;
//    }
    /**
     * 查询设备型号信息
     * @param info
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     * @throws InvalidKeyException
     * @throws Exception
     */
    public static JSONObject getMapping(WtOpenApiInfo info,String urlMapping, Map<String,String> queryParams) throws NoSuchAlgorithmException
            , UnsupportedEncodingException, InvalidKeyException,Exception {

        String datetime = String.valueOf(System.currentTimeMillis());//时间戳
        //参数拼接
        String param="";
        if (!queryParams.isEmpty()) {
            param +=  urlencode(queryParams);
        }
        // url参数拼接
        String url = info.getUrl()+"?" +urlMapping+param;

        // 签名
        String auth = MD5(param+info.getSecureKey()).toLowerCase();

//        String auth = MD5("pageIndex=0&pageSize=201574993804802testSecure");
        BufferedReader in = null;

        // 请求头
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("X-Client-Id", info.getClientId());
        headers.put("X-Timestamp", datetime);
        headers.put("X-Sign", auth);

        try {
            //发送GET请求
            String getback = SendPostUtil.sendGet(url, headers);
            //解析返回参数
//            JSONObject parse2 = (JSONObject)JSONObject.parse(getback);
            return (JSONObject)JSONObject.parse(getback);
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
