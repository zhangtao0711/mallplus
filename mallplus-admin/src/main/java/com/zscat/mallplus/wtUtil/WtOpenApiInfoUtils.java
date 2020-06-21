package com.zscat.mallplus.wtUtil;


import com.zscat.mallplus.water.entity.WtEquipment;
import com.zscat.mallplus.water.entity.WtOpenApiInfo;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * SIM卡第三节接口调用
 */
@Slf4j
public class WtOpenApiInfoUtils {

//    /**
//     * 查询设备型号信息
//     * @param info
//     * @return
//     * @throws NoSuchAlgorithmException
//     * @throws UnsupportedEncodingException
//     * @throws InvalidKeyException
//     * @throws Exception
//     */
//    public static JSONObject getMapping(WtOpenApiInfo info,String urlMapping, Map<String,String> queryParams) throws NoSuchAlgorithmException
//            , UnsupportedEncodingException, InvalidKeyException,Exception {
//
//        String datetime = String.valueOf(System.currentTimeMillis());//时间戳
//        //参数拼接
//        String param="";
//        if (!queryParams.isEmpty()) {
//            param +=  urlencode(queryParams);
//        }
//        // url参数拼接
//        String url = info.getUrl()+"?" +urlMapping+param;
//
//        // 签名
//        String auth = MD5(param+info.getSecureKey()).toLowerCase();
//
////        String auth = MD5("pageIndex=0&pageSize=201574993804802testSecure");
//        BufferedReader in = null;
//
//        // 请求头
//        Map<String, String> headers = new HashMap<String, String>();
//        headers.put("X-Client-Id", info.getClientId());
//        headers.put("X-Timestamp", datetime);
//        headers.put("X-Sign", auth);
//
//        try {
//            //发送GET请求
//            String getback = SendPostUtil.sendGet(url, headers);
//            //解析返回参数
////            JSONObject parse2 = (JSONObject)JSONObject.parse(getback);
//            return (JSONObject)JSONObject.parse(getback);
//           // System.out.println(result);
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
     * 保存设备
     */
    public static Map<String, String> saveDevice(WtOpenApiInfo info, WtEquipment entity){
        String url = info.getUrl()+"/api/v1/device";

        HttpRequest request = new SimpleHttpRequest(url);

        String body = "[{\n" +
                "\t\t\"id\": \""+entity.getEqcode()+"\",\n" +//设备实例ID 必填
                "\t\t\"name\": \""+entity.getEqcode()+"\",\n" +//设备实例名称 必填
                "\t\t\"productId\": \""+entity.getProductId()+"\",\n" +//型号ID 必填
//                "\t\t\"productName\": \"1236859833832701952\",\n" +//型号名称 必填
                "\t\t\"configuration\": {\n" +//设备配置信息
                "\t\t},\n" +
                "\t\t\"creatorId\": \""+entity.getCreateBy()+"\",\n" +//创建人ID
                "\t\t\"creatorName\": \""+entity.getCreateTime()+"\",\n" +//创建人名称
                "\t\t\"tags\": [{\n" +//标签
                "\t\t\t\"deviceId\": \""+entity.getEqcode()+"\",\n" +//设备实例ID 必填
                "\t\t\t\"type\": \"string\",\n" +
                "\t\t\t\"key\": \"area\",\n" +
                "\t\t\t\"name\": \"地区\",\n" +
                "\t\t\t\"value\": \"shijiazhuan\"\n" +
                "\t\t}]\n" +
                "\n" +
//                "\t},\n" +
//                "\t{\n" +
//                "\t\t\"id\": \"test003\",\n" +
//                "\t\t\"name\": \"设备名称\",\n" +
//                "\t\t\"productId\": \"1236859833832701952\",\n" +
//                "\t\t\"configuration\": {}\n" +
//                "\t},\n" +
//                "\t{\n" +
//                "\t\t\"id\": \"test004\",\n" +
//                "\t\t\"name\": \"设备名称\",\n" +
//                "\t\t\"productId\": \"1236859833832701952\",\n" +
//                "\t\t\"configuration\": {}\n" +
                "\t}\n" +
                "]";
        request.headers(HeaderUtils.createHeadersOfJsonString(info.getClientId(),info.getSecureKey(),body));
        request.requestBody(body);

        try {
            Response response = request.post();
            Map<String, String> result = WtUtils.queryStringToMap(new String(response.asBytes(), "utf8"), "utf8");
            System.out.println(result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 批量激活
     */
    public static Map<String, String> batchDeployOn(WtOpenApiInfo info,List<String> eqcodeList) {
        String url = info.getUrl()+ "/_deploy";//激活

        HttpRequest request = new SimpleHttpRequest(url);

//        String body = "[\"test003\",\"test004\"]";
        String body ="[";
        for(String eqcode:eqcodeList){
            body += "\""+eqcode+"\",";
        }
        body+="]";

        request.headers(HeaderUtils.createHeadersOfJsonString(info.getClientId(),info.getSecureKey(),body));
        request.requestBody(body);

        try {
            Response response = request.post();
            Map<String, String> result = WtUtils.queryStringToMap(new String(response.asBytes(), "utf8"), "utf8");
            System.out.println(result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 注销设备
     */
    public static Map<String, String> batchDeployOff(WtOpenApiInfo info,List<String> eqcodeList) {
        String url = info.getUrl()+ "/_unDeploy";//注销

        HttpRequest request = new SimpleHttpRequest(url);

        String body ="[";
        for(String eqcode:eqcodeList){
            body += "\""+eqcode+"\",";
        }
        body+="]";

        request.headers(HeaderUtils.createHeadersOfJsonString(info.getClientId(),info.getSecureKey(),body));
        request.requestBody(body);

        try {
            Response response = request.post();
            Map<String, String> result = WtUtils.queryStringToMap(new String(response.asBytes(), "utf8"), "utf8");
            System.out.println(result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 设备上报联网信息回复调用
     */
    public static Map<String, String> invokeFunctionNet(WtOpenApiInfo info, WtEquipment entity) {
        String functionId = "net";
        String url = info.getUrl() + "/" + entity.getEqcode() + "/function/" + functionId;
        SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmss");//设置日期格式
        System.out.println(url);
        HttpRequest request = new SimpleHttpRequest(url);
        String body = "{\n" +
                "\t\"eqid\": "+entity.getId()+",\n" +//【设备在服务器中的ID，0标识不存在】
                "\t\"sj\": "+df.format(new Date())+",\n" +//【服务器时间】
                "\t\"state\": "+entity.getState()+",\n" +// 状态值,----【0未安装2限制使用3正常使用】
                "\t\"pro\": "+entity.getStoreId()+",\n" +//【项目id】所属门店
                "\t\"gro\": "+entity.getDealerId()+",\n" +//【组id】经销商id
                "\t\"uid\": "+entity.getEqGroupId()+",\n" +//【用途id】组id
                "\t\"eqtype\": "+entity.getProductId()+"\n" +//型号ID 必填
                "}";

        request.headers(HeaderUtils.createHeadersOfJsonString(info.getClientId(),info.getSecureKey(),body));
//        System.out.println("Headers:===========>" + HeaderUtils.createHeadersOfJsonString(body));
        request.requestBody(body);

        try {
            Response response = request.post();
            Map<String, String> result = WtUtils.queryStringToMap(new String(response.asBytes(), "utf8"), "utf8");
            System.out.println(result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 平台发送初始化操作调用
     */
    public static Map<String, String> invokeFunctionLoad(WtOpenApiInfo info, WtEquipment entity) {
        String functionId = "load";
        String url = info.getUrl() + "/" + entity.getEqcode() + "/function/" + functionId;
//        SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmss");//设置日期格式
        System.out.println(url);
        HttpRequest request = new SimpleHttpRequest(url);
        String body = "{\n" +
                "\t\"eqtype\": "+entity.getProductId()+"\n" +//型号ID 必填
                "}";

        request.headers(HeaderUtils.createHeadersOfJsonString(info.getClientId(),info.getSecureKey(),body));
//        System.out.println("Headers:===========>" + HeaderUtils.createHeadersOfJsonString(body));
        request.requestBody(body);

        try {
            Response response = request.post();
            Map<String, String> result = WtUtils.queryStringToMap(new String(response.asBytes(), "utf8"), "utf8");
            System.out.println(result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 平台发送设置售水参数调用
     */
    public static Map<String, String> invokeFunctionSetss(WtOpenApiInfo info, WtEquipment entity) {
        String functionId = "setss";
        String url = info.getUrl() + "/" + entity.getEqcode() + "/function/" + functionId;
        System.out.println(url);
        HttpRequest request = new SimpleHttpRequest(url);
        String body = "{\n" +
                "\t\"tbgs\": "+entity.getId()+",\n" +//投币个数,--1-10的数
                "\t\"state\": "+entity.getState()+",\n" +// 状态值,----【0未安装2限制使用3正常使用】
                "\t\"pro\": "+entity.getStoreId()+",\n" +//【项目id】所属门店
                "\t\"gro\": "+entity.getDealerId()+",\n" +//【组id】经销商id
                "\t\"uid\": "+entity.getEqGroupId()+",\n" +//【用途id】组id
                "\t\"eqtype\": "+entity.getProductId()+"\n" +//型号ID 必填
                "}";

        request.headers(HeaderUtils.createHeadersOfJsonString(info.getClientId(),info.getSecureKey(),body));
        request.requestBody(body);

        try {
            Response response = request.post();
            Map<String, String> result = WtUtils.queryStringToMap(new String(response.asBytes(), "utf8"), "utf8");
            System.out.println(result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 根据产品ID和动态查询参数查询设备相关数据测试
     */
    public static Object queryDeviceLogTest(WtOpenApiInfo info,String productId,String deviceId) {
        String url = info.getUrl() + "/" + productId + "/log/_query/";

        System.out.println(url);
        HttpRequest request = new SimpleHttpRequest(url);
        String body = "{\n" +
                "\t\"pageSize\": 25,\n" +
                "\t\"pageIndex\": 0,\n" +
                "\t\"terms\": [{\n" +
                "\t\t\"column\": \"deviceId\",\n" +
                "\t\t\"value\": \""+deviceId+"\"\n" +
                "\t}]\n" +
                "}";

        request.headers(HeaderUtils.createHeadersOfJsonString(info.getClientId(),info.getSecureKey(),body));
//        System.out.println("Headers:===========>" + HeaderUtils.createHeadersOfJsonString(body));
        request.requestBody(body);

        try {
            Response response = request.post();
            Map<String, String> result = WtUtils.queryStringToMap(new String(response.asBytes(), "utf8"), "utf8");
            System.out.println(result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

//    /**
//     * 分页查询设备属性测试
//     */
//    @Test
//    void queryDevicePropertiesTest() {
//        String url = base_url + "/" + productId + "/properties/_query/";
//
//        System.out.println(url);
//        HttpRequest request = new SimpleHttpRequest(url);
//        String body = "{\n" +
//                "\t\"pageSize\": 25,\n" +
//                "\t\"pageIndex\": 0,\n" +
//                "\t\"terms\": [{\n" +
//                "\t\t\"column\": \"property\",\n" +
//                "\t\t\"value\": \"temperature\"\n" +
//                "\t}]\n" +
//                "}";
//
//        request.headers(HeaderUtils.createHeadersOfJsonString(body));
//        System.out.println("Headers:===========>" + HeaderUtils.createHeadersOfJsonString(body));
//        request.requestBody(body);
//
//        try {
//            Response response = request.post();
//            Map<String, String> result = Utils.queryStringToMap(new String(response.asBytes(), "utf8"), "utf8");
//            System.out.println(result);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 查询设备事件测试
//     */
//    @Test
//    void queryDeviceEventsTest() {
//        String eventId = "fire_alarm";
//        String url = base_url + "/" + productId + "/event/" + eventId + "/_query/";
//
//        System.out.println(url);
//        HttpRequest request = new SimpleHttpRequest(url);
//        String body = "{\n" +
//                "\t\"pageSize\": 25,\n" +
//                "\t\"pageIndex\": 0,\n" +
//                "\t\"terms\": [{\n" +
//                "\t\t\"column\": \"deviceId\",\n" +
//                "\t\t\"value\": \"test001\"\n" +
//                "\t}]\n" +
//                "}";
//
//        request.headers(HeaderUtils.createHeadersOfJsonString(body));
//        System.out.println("Headers:===========>" + HeaderUtils.createHeadersOfJsonString(body));
//        request.requestBody(body);
//
//        try {
//            Response response = request.post();
//            Map<String, String> result = Utils.queryStringToMap(new String(response.asBytes(), "utf8"), "utf8");
//            System.out.println(result);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
