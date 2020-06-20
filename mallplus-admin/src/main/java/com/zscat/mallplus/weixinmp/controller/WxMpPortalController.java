package com.zscat.mallplus.weixinmp.controller;

import com.zscat.mallplus.config.WxMpConfiguration;
import com.zscat.mallplus.weixinmp.entity.AccountWechats;
import com.zscat.mallplus.weixinmp.service.IAccountWechatsService;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/wxmp/portal/{uniacid}")
public class WxMpPortalController {
    private WxMpService wxService;
    private WxMpMessageRouter messageRouter;
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private IAccountWechatsService wechatsService;
    @Resource
    private WxMpConfiguration wxMpConfiguration;

    @GetMapping(produces = "text/plain;charset=utf-8")
    public String authGet(@PathVariable Integer uniacid,
                          @RequestParam(name = "signature", required = false) String signature,
                          @RequestParam(name = "timestamp", required = false) String timestamp,
                          @RequestParam(name = "nonce", required = false) String nonce,
                          @RequestParam(name = "echostr", required = false) String echostr) {

        log.info("\n接收到来自微信服务器的认证消息：[{}, {}, {}, {}]", signature,
            timestamp, nonce, echostr);
        List<AccountWechats> wechatsList = jdbcTemplate.query("select * from account_wechats where uniacid = "+uniacid,new BeanPropertyRowMapper<AccountWechats>(AccountWechats.class));
        if (wechatsList==null||wechatsList.size()==0){
            throw new IllegalArgumentException("数据错误，多条数据！");
        }
        AccountWechats wechats = wechatsList.get(0);
        if (wechats==null){
            throw new IllegalArgumentException("没有相关公众号数据");
        }
        if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
            wechats.setStatus(3);
            jdbcTemplate.update("update account_wechats set status = " + wechats.getStatus() +" where acid = "+wechats.getAcid());
            throw new IllegalArgumentException("请求参数非法，请核实!");
        }
        wxService = wxMpConfiguration.wxMpService();
        if (!this.wxService.switchover(wechats.getKey())) {
            wechats.setStatus(3);
            jdbcTemplate.update("update account_wechats set status = " + wechats.getStatus() +" where acid = "+wechats.getAcid());
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", wechats.getKey()));
        }

        if (wxService.checkSignature(timestamp, nonce, signature)) {
            wechats.setStatus(2);
            jdbcTemplate.update("update account_wechats set status = " + wechats.getStatus() +" where acid = "+wechats.getAcid());
            return echostr;
        }
        wechats.setStatus(3);
        jdbcTemplate.update("update account_wechats set status = " + wechats.getStatus() +" where acid = "+wechats.getAcid());
        return "非法请求";
    }

    @PostMapping(produces = "application/xml; charset=UTF-8")
    public String post( @PathVariable Integer uniacid,
                       @RequestBody String requestBody,
                       @RequestParam("signature") String signature,
                       @RequestParam("timestamp") String timestamp,
                       @RequestParam("nonce") String nonce,
                       @RequestParam("openid") String openid,
                       @RequestParam(name = "encrypt_type", required = false) String encType,
                       @RequestParam(name = "msg_signature", required = false) String msgSignature) {
        log.info("\n接收微信请求：[openid=[{}], [signature=[{}], encType=[{}], msgSignature=[{}],"
                + " timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
            openid, signature, encType, msgSignature, timestamp, nonce, requestBody);
        List<AccountWechats> wechatsList = jdbcTemplate.query("select * from account_wechats where uniacid = "+uniacid,new BeanPropertyRowMapper<AccountWechats>(AccountWechats.class));
        if (wechatsList==null||wechatsList.size()==0){
            throw new IllegalArgumentException("数据错误，多条数据！");
        }
        AccountWechats wechats = wechatsList.get(0);if (wechats==null){
            throw new IllegalArgumentException("没有相关公众号数据");
        }
        wxService = wxMpConfiguration.wxMpService();
        if (!this.wxService.switchover(wechats.getKey())) {
            wechats.setStatus(3);
            jdbcTemplate.update("update account_wechats set status = " + wechats.getStatus() +" where acid = "+wechats.getAcid());
//            wechatsService.updateById(wechats);
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", wechats.getKey()));
        }

        if (!wxService.checkSignature(timestamp, nonce, signature)) {
            wechats.setStatus(3);
            jdbcTemplate.update("update account_wechats set status = " + wechats.getStatus() +" where acid = "+wechats.getAcid());
            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
        }

        String out = null;
        if (encType == null) {
            // 明文传输的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
            WxMpXmlOutMessage outMessage = this.route(inMessage);
            if (outMessage == null) {
                return "";
            }

            out = outMessage.toXml();
        } else if ("aes".equalsIgnoreCase(encType)) {
            // aes加密的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(requestBody, wxService.getWxMpConfigStorage(),
                timestamp, nonce, msgSignature);
            log.debug("\n消息解密后内容为：\n{} ", inMessage.toString());
            WxMpXmlOutMessage outMessage = this.route(inMessage);
            if (outMessage == null) {
                return "";
            }

            out = outMessage.toEncryptedXml(wxService.getWxMpConfigStorage());
        }
        wechats.setStatus(2);
        jdbcTemplate.update("update account_wechats set status = " + wechats.getStatus() +" where acid = "+wechats.getAcid());
        log.debug("\n组装回复信息：{}", out);
        return out;
    }

    private WxMpXmlOutMessage route(WxMpXmlMessage message) {
        try {
            return this.messageRouter.route(message);
        } catch (Exception e) {
            log.error("路由消息时出现异常！", e);
        }

        return null;
    }

}
