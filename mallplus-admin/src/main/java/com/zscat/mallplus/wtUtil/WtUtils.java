package com.zscat.mallplus.wtUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangzheng
 * @see
 * @since 1.0
 */
public class WtUtils {
    public static Map<String,String> queryStringToMap(String queryString, String charset){
        try {
            Map<String,String> map = new HashMap<>();

            String[] decode = URLDecoder.decode(queryString,charset).split("&");
            for (String keyValue : decode) {
                String[] kv = keyValue.split("[=]",2);
                map.put(kv[0],kv.length>1?kv[1]:"");
            }
            return map;
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }
    /**
     * 经纬度校验
     * 经度longitude: (?:[0-9]|[1-9][0-9]|1[0-7][0-9]|180)\\.([0-9]{6})
     * 纬度latitude：  (?:[0-9]|[1-8][0-9]|90)\\.([0-9]{6})
     * @return
     */
    public static boolean checkItude(String longitude,String latitude){
        String reglo = "((?:[0-9]|[1-9][0-9]|1[0-7][0-9])\\.([0-9]{0,6}))|((?:180)\\.([0]{0,6}))";
        String regla = "((?:[0-9]|[1-8][0-9])\\.([0-9]{0,6}))|((?:90)\\.([0]{0,6}))";
        longitude = longitude.trim();
        latitude = latitude.trim();
        return longitude.matches(reglo)==true?latitude.matches(regla):false;
    }
}
