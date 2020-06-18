package com.zscat.mallplus.config;

//import com.github.binarywang.demo.wx.mp.handler.*;
import com.zscat.mallplus.weixinmp.entity.AccountWechats;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * wechat mp configuration
 *
 * @author Binary Wang(https://github.com/binarywang)
 */
@AllArgsConstructor
@Configuration
public class WxMpConfiguration {
//    private final LogHandler logHandler;
//    private final NullHandler nullHandler;
//    private final KfSessionHandler kfSessionHandler;
//    private final StoreCheckNotifyHandler storeCheckNotifyHandler;
//    private final LocationHandler locationHandler;
//    private final MenuHandler menuHandler;
//    private final MsgHandler msgHandler;
//    private final UnsubscribeHandler unsubscribeHandler;
//    private final SubscribeHandler subscribeHandler;
//    private final ScanHandler scanHandler;
    @Resource
    private JdbcTemplate jdbcTemplate;

    @Bean
    public WxMpService wxMpService() {
        // 代码里 getConfigs()处报错的同学，请注意仔细阅读项目说明，你的IDE需要引入lombok插件！！！！
        List<AccountWechats> configs = jdbcTemplate.query("select * from account_wechats", new BeanPropertyRowMapper<AccountWechats>(AccountWechats.class));
        if (configs == null||configs.size()==0) {
            AccountWechats mp = new AccountWechats();
            mp.setUniacid(1);
            mp.setKey("1");
            mp.setEncodingaeskey("1");
            mp.setSecret("1");
            mp.setToken("1");
            configs.add(mp);
//            throw new RuntimeException("大哥，拜托先看下项目首页的说明（readme文件），添加下相关配置，注意别配错了！");
        }

        WxMpService service = new WxMpServiceImpl();
        service.setMultiConfigStorages(configs
            .stream().map(a -> {
                WxMpDefaultConfigImpl configStorage = new WxMpDefaultConfigImpl();
                configStorage.setAppId(a.getKey());
                configStorage.setSecret(a.getSecret());
                configStorage.setToken(a.getToken());
                configStorage.setAesKey(a.getEncodingaeskey());
                return configStorage;
            }).collect(Collectors.toMap(WxMpDefaultConfigImpl::getAppId, a -> a, (o, n) -> o)));
        return service;
    }

    @Bean
    public WxMpMessageRouter messageRouter(WxMpService wxMpService) {
        final WxMpMessageRouter newRouter = new WxMpMessageRouter(wxMpService);

//        // 记录所有事件的日志 （异步执行）
//        newRouter.rule().handler(this.logHandler).next();
//
//        // 接收客服会话管理事件
//        newRouter.rule().async(false).msgType(EVENT).event(KF_CREATE_SESSION)
//            .handler(this.kfSessionHandler).end();
//        newRouter.rule().async(false).msgType(EVENT).event(KF_CLOSE_SESSION)
//            .handler(this.kfSessionHandler).end();
//        newRouter.rule().async(false).msgType(EVENT).event(KF_SWITCH_SESSION)
//            .handler(this.kfSessionHandler).end();
//
//        // 门店审核事件
//        newRouter.rule().async(false).msgType(EVENT).event(POI_CHECK_NOTIFY).handler(this.storeCheckNotifyHandler).end();
//
//        // 自定义菜单事件
//        newRouter.rule().async(false).msgType(EVENT).event(EventType.CLICK).handler(this.menuHandler).end();
//
//        // 点击菜单连接事件
//        newRouter.rule().async(false).msgType(EVENT).event(EventType.VIEW).handler(this.nullHandler).end();
//
//        // 关注事件
//        newRouter.rule().async(false).msgType(EVENT).event(SUBSCRIBE).handler(this.subscribeHandler).end();
//
//        // 取消关注事件
//        newRouter.rule().async(false).msgType(EVENT).event(UNSUBSCRIBE).handler(this.unsubscribeHandler).end();
//
//        // 上报地理位置事件
//        newRouter.rule().async(false).msgType(EVENT).event(EventType.LOCATION).handler(this.locationHandler).end();
//
//        // 接收地理位置消息
//        newRouter.rule().async(false).msgType(XmlMsgType.LOCATION).handler(this.locationHandler).end();
//
//        // 扫码事件
//        newRouter.rule().async(false).msgType(EVENT).event(EventType.SCAN).handler(this.scanHandler).end();
//
//        // 默认
//        newRouter.rule().async(false).handler(this.msgHandler).end();

        return newRouter;
    }

}
