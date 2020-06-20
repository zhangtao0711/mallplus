package com.zscat.mallplus.wxminiapp.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaMessage;
import cn.binarywang.wx.miniapp.constant.WxMaConstants;
import com.zscat.mallplus.config.WxMaConfiguration;
import com.zscat.mallplus.wxminiapp.entity.AccountWxapp;
import com.zscat.mallplus.wxminiapp.service.IAccountWxappService;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@RestController
@RequestMapping("/wxma/portal/{uniacid}")
public class WxMaPortalController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private IAccountWxappService wxappService;

    @GetMapping(produces = "text/plain;charset=utf-8")
    public String authGet(@PathVariable Integer uniacid,
                          @RequestParam(name = "signature", required = false) String signature,
                          @RequestParam(name = "timestamp", required = false) String timestamp,
                          @RequestParam(name = "nonce", required = false) String nonce,
                          @RequestParam(name = "echostr", required = false) String echostr) {
        this.logger.info("\n接收到来自微信服务器的认证消息：signature = [{}], timestamp = [{}], nonce = [{}], echostr = [{}]",
            signature, timestamp, nonce, echostr);
        List<AccountWxapp> wxappList = jdbcTemplate.query("select * from account_wxapp where uniacid = "+uniacid,new BeanPropertyRowMapper<AccountWxapp>(AccountWxapp.class));
        if (wxappList==null||wxappList.size()==0){
            throw new IllegalArgumentException("数据错误，多条数据！");
        }
        AccountWxapp wxapp = wxappList.get(0);
        if (wxapp==null){
            throw new IllegalArgumentException("没有相关小程序数据");
        }
        if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
            wxapp.setStatus(3);
            jdbcTemplate.update("update account_wxapp set status = " + wxapp.getStatus() +" where acid = "+wxapp.getAcid());
            throw new IllegalArgumentException("请求参数非法，请核实!");
        }

        final WxMaService wxService = WxMaConfiguration.getMaService(wxapp.getKey());

        if (wxService.checkSignature(timestamp, nonce, signature)) {
            wxapp.setStatus(2);
            jdbcTemplate.update("update account_wxapp set status = " + wxapp.getStatus() +" where acid = "+wxapp.getAcid());
            return echostr;
        }
        wxapp.setStatus(3);
        jdbcTemplate.update("update account_wxapp set status = " + wxapp.getStatus() +" where acid = "+wxapp.getAcid());
        return "非法请求";
    }

    @PostMapping(produces = "application/xml; charset=UTF-8")
    public String post(@PathVariable Integer uniacid,
                       @RequestBody String requestBody,
                       @RequestParam(name = "msg_signature", required = false) String msgSignature,
                       @RequestParam(name = "encrypt_type", required = false) String encryptType,
                       @RequestParam(name = "signature", required = false) String signature,
                       @RequestParam("timestamp") String timestamp,
                       @RequestParam("nonce") String nonce) {
        this.logger.info("\n接收微信请求：[msg_signature=[{}], encrypt_type=[{}], signature=[{}]," +
                " timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
            msgSignature, encryptType, signature, timestamp, nonce, requestBody);
        List<AccountWxapp> wxappList = jdbcTemplate.query("select * from account_wxapp where uniacid = "+uniacid,new BeanPropertyRowMapper<AccountWxapp>(AccountWxapp.class));
        if (wxappList==null||wxappList.size()==0){
            throw new IllegalArgumentException("数据错误，多条数据！");
        }
        AccountWxapp wxapp = wxappList.get(0);
        if (wxapp==null){
            throw new IllegalArgumentException("没有相关小程序数据");
        }
        final WxMaService wxService = WxMaConfiguration.getMaService(wxapp.getKey());

        final boolean isJson = Objects.equals(wxService.getWxMaConfig().getMsgDataFormat(),
            WxMaConstants.MsgDataFormat.JSON);
        if (StringUtils.isBlank(encryptType)) {
            // 明文传输的消息
            WxMaMessage inMessage;
            if (isJson) {
                inMessage = WxMaMessage.fromJson(requestBody);
            } else {//xml
                inMessage = WxMaMessage.fromXml(requestBody);
            }
            wxapp.setStatus(2);
            jdbcTemplate.update("update account_wxapp set status = " + wxapp.getStatus() +" where acid = "+wxapp.getAcid());
            this.route(inMessage, wxapp.getKey());
            return "success";
        }

        if ("aes".equals(encryptType)) {
            // 是aes加密的消息
            WxMaMessage inMessage;
            if (isJson) {
                inMessage = WxMaMessage.fromEncryptedJson(requestBody, wxService.getWxMaConfig());
            } else {//xml
                inMessage = WxMaMessage.fromEncryptedXml(requestBody, wxService.getWxMaConfig(),
                    timestamp, nonce, msgSignature);
            }
            wxapp.setStatus(2);
            jdbcTemplate.update("update account_wxapp set status = " + wxapp.getStatus() +" where acid = "+wxapp.getAcid());
            this.route(inMessage, wxapp.getKey());
            return "success";
        }
        wxapp.setStatus(3);
        jdbcTemplate.update("update account_wxapp set status = " + wxapp.getStatus() +" where acid = "+wxapp.getAcid());
        throw new RuntimeException("不可识别的加密类型：" + encryptType);
    }

    private void route(WxMaMessage message, String appid) {
        try {
            WxMaConfiguration.getRouter(appid).route(message);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }
    }

}
