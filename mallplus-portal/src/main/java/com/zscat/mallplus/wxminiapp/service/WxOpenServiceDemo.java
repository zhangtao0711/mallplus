package com.zscat.mallplus.wxminiapp.service;

import com.zscat.mallplus.config.JedisConfig;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.open.api.impl.WxOpenInRedisConfigStorage;
import me.chanjar.weixin.open.api.impl.WxOpenMessageRouter;
import me.chanjar.weixin.open.api.impl.WxOpenServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;

/**
 * @author <a href="https://github.com/007gzs">007</a>
 */
@Service
@Slf4j
public class WxOpenServiceDemo extends WxOpenServiceImpl {
    private WxOpenMessageRouter wxOpenMessageRouter;
    @Autowired
    private JedisConfig jedisConfig;
    @Resource
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        Map<String,Object> dsn = jdbcTemplate.queryForMap("select * from admin_dsn_domin where id =1");
        WxOpenInRedisConfigStorage inRedisConfigStorage = new WxOpenInRedisConfigStorage(jedisConfig.redisPoolFactory());
        inRedisConfigStorage.setComponentAppId(dsn.get("component_appid").toString());
        inRedisConfigStorage.setComponentAppSecret(dsn.get("component_app_secret").toString());
        inRedisConfigStorage.setComponentToken(dsn.get("component_token").toString());
        inRedisConfigStorage.setComponentAesKey(dsn.get("component_aes_key").toString());
        setWxOpenConfigStorage(inRedisConfigStorage);
        wxOpenMessageRouter = new WxOpenMessageRouter(this);
        wxOpenMessageRouter.rule().handler((wxMpXmlMessage, map, wxMpService, wxSessionManager) -> {
            log.info("\n接收到 {} 公众号请求消息，内容：{}", wxMpService.getWxMpConfigStorage().getAppId(), wxMpXmlMessage);
            return null;
        }).next();
    }
    public WxOpenMessageRouter getWxOpenMessageRouter(){
        return wxOpenMessageRouter;
    }
}
