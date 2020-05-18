package com.zscat.mallplus.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created  on 2017/12/29 0029.
 *
 * @author zt
 */
@Component
@Data
public class AppConfiguration {
    @Value("${api.qq.map.url}")
    private String qqApiUrl;
    @Value("${cfg.tencentkey}")
    private String tencentKey;
    @Value("${cfg.tencentkey2}")
    private String tencentKey2;
    @Value("${cfg.tencentkey3}")
    private String tencentKey3;
    @Value("${cfg.baidu.map.url}")
    private String baiduApiUrl;
    @Value(("${cfg.baidu.key}"))
    private String baiduKey;
    @Value(("${cfg.baidu.key2}"))
    private String baiduKey2;

    //电商ID
    @Value("${logistics.Kdniao.EBusinessID}")
    private String EBusinessID;
    //电商加密私钥，快递鸟提供，注意保管，不要泄漏
    @Value("${logistics.Kdniao.AppKey}")
    private String AppKey;
    //请求url
    @Value("${logistics.Kdniao.ReqURL}")
    private String ReqURL;


}
