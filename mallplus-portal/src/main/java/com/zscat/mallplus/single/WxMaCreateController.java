package com.zscat.mallplus.single;

import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.core.kit.WxPayKit;
import com.zscat.mallplus.pay.utils.StringUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.wxminiapp.service.WxOpenServiceDemo;
import com.zscat.mallplus.vo.wxminiapp.FastRegisterWeappVo;
import com.zscat.mallplus.wxopen.mapper.WxOpenQueryAuthResultMapper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.open.api.WxOpenConfigStorage;
import me.chanjar.weixin.open.bean.auth.WxOpenAuthorizationInfo;
import me.chanjar.weixin.open.bean.message.WxOpenXmlMessage;
import me.chanjar.weixin.open.bean.result.WxOpenQueryAuthResult;
import me.chanjar.weixin.open.bean.result.WxOpenResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.util.Date;
import java.util.Map;

/**
 * <pre>
 *  第三方平台快速创建小程序接口
 *  Created by BinaryWang on 2017/6/16.
 * </pre>
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Slf4j
@RestController
@RequestMapping("/api/home/create")
public class WxMaCreateController {

    @Autowired
    private WxOpenServiceDemo wxOpenServiceDemo;
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private WxOpenQueryAuthResultMapper resultMapper;

    /**
     * 快速创建小程序
     */
    @SysLog(MODULE = "wxapp", REMARK = "快速创建小程序")
    @ApiOperation("快速创建小程序")
    @PostMapping("/fastRegisterWeapp")
    public Object fastRegisterWeapp(@RequestBody FastRegisterWeappVo entity) {
        if (entity == null || StringUtils.isBlank(entity.getName()) || StringUtils.isBlank(entity.getCode())
                || StringUtils.isBlank(entity.getCodeType()) || StringUtils.isBlank(entity.getComponentPhone())
                || StringUtils.isBlank(entity.getLegalPersonaName()) || StringUtils.isBlank(entity.getLegalPersonaWechat())) {
            return new CommonResult().failed("参数不能为空！");
        }
        try {
            Map<String, Object> dsn = jdbcTemplate.queryForMap("select * from admin_dsn_domin where id =1");
            wxOpenServiceDemo.getWxOpenConfigStorage().setComponentVerifyTicket(dsn.get("component_verify_ticket").toString());
            WxOpenResult result = wxOpenServiceDemo.getWxOpenComponentService().fastRegisterWeapp(entity.getName(),
                    entity.getCode(),
                    entity.getCodeType(),
                    entity.getLegalPersonaWechat(),
                    entity.getLegalPersonaName(),
                    entity.getComponentPhone());
            if (result.isSuccess()) {
                return new CommonResult().success("创建成功！");
            } else {
                return new CommonResult().failed("创建失败，错误日志：" + result.toString());
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
            return new CommonResult().failed("创建失败，请重试！");
        }

    }

    /**
     * 快速创建小程序查询
     */
    @SysLog(MODULE = "wxapp", REMARK = "快速创建小程序查询")
    @ApiOperation("快速创建小程序查询")
    @PostMapping("/fastRegisterWeappSearch")
    public Object fastRegisterWeappSearch(FastRegisterWeappVo entity) {
        try {
            Map<String, Object> dsn = jdbcTemplate.queryForMap("select * from admin_dsn_domin where id =1");
            wxOpenServiceDemo.getWxOpenConfigStorage().setComponentVerifyTicket(dsn.get("component_verify_ticket").toString());
            WxOpenResult result = wxOpenServiceDemo.getWxOpenComponentService().fastRegisterWeappSearch(entity.getName(),
                    entity.getLegalPersonaWechat(),
                    entity.getLegalPersonaName());
            if (result.isSuccess()) {
                return new CommonResult().success("创建成功！");
            } else {
                return new CommonResult().failed("创建失败，错误日志：" + result.toString());
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
            return new CommonResult().failed("创建失败，请重试！");
        }

    }

    /**
     * 接收component_verify_ticket 或 authorized事件
     */
    @ApiOperation(value = "接收component_verify_ticket 或 authorized事件", notes = "接收component_verify_ticket 或 authorized事件", response = String.class)
    @RequestMapping(value = "/getComponentVerifyTicket")
    public String getComponentVerifyTicket(@RequestParam("timestamp") String timestamp, @RequestParam("nonce") String nonce,
                                           @RequestParam("msg_signature") String msgSignature, @RequestBody String postData) throws WxErrorException {
        log.info("接收component_verify_ticket 或 authorized事件");
        log.info("nonce: " + nonce);
        log.info("timestamp: " + timestamp);
        log.info("msgSignature: " + msgSignature);
        log.info("postData", postData.toString());
        WxOpenXmlMessage wxOpenXmlMessage = WxOpenXmlMessage.fromEncryptedXml(postData, wxOpenServiceDemo.getWxOpenConfigStorage(), timestamp, nonce, msgSignature);
        if (wxOpenXmlMessage == null) {
            throw new NullPointerException("message is empty");
        }
        if (org.apache.commons.lang3.StringUtils.equalsIgnoreCase(wxOpenXmlMessage.getInfoType(), "component_verify_ticket")) {
            log.info("component_verify_ticket:", wxOpenXmlMessage.getComponentVerifyTicket().toString());
            jdbcTemplate.update("update admin_dsn_domin set component_verify_ticket = '" + wxOpenXmlMessage.getComponentVerifyTicket() + "' where id = 1");
            wxOpenServiceDemo.getWxOpenConfigStorage().setComponentVerifyTicket(wxOpenXmlMessage.getComponentVerifyTicket());
            return "success";
        }
        //新增、更新授权
        if (org.apache.commons.lang3.StringUtils.equalsAnyIgnoreCase(wxOpenXmlMessage.getInfoType(), "authorized", "updateauthorized")) {
            Map<String, Object> dsn = jdbcTemplate.queryForMap("select * from admin_dsn_domin where id =1");
            wxOpenServiceDemo.getWxOpenConfigStorage().setComponentVerifyTicket(dsn.get("component_verify_ticket").toString());
            WxOpenQueryAuthResult queryAuth = wxOpenServiceDemo.getWxOpenComponentService().getQueryAuth(wxOpenXmlMessage.getAuthorizationCode());
            if (queryAuth == null || queryAuth.getAuthorizationInfo() == null || queryAuth.getAuthorizationInfo().getAuthorizerAppid() == null) {
                throw new NullPointerException("getQueryAuth");
            }
            return "success";
        }
        //快速创建小程序
        if (org.apache.commons.lang3.StringUtils.equalsIgnoreCase(wxOpenXmlMessage.getInfoType(), "notify_third_fasteregister") && wxOpenXmlMessage.getStatus() == 0) {
            Map<String, Object> dsn = jdbcTemplate.queryForMap("select * from admin_dsn_domin where id =1");
            wxOpenServiceDemo.getWxOpenConfigStorage().setComponentVerifyTicket(dsn.get("component_verify_ticket").toString());
            WxOpenQueryAuthResult queryAuth = wxOpenServiceDemo.getWxOpenComponentService().getQueryAuth(wxOpenXmlMessage.getAuthCode());
            // 需不需要做什么业务，存到数据库里面，存那些数据？都存了
            com.zscat.mallplus.wxopen.entity.WxOpenQueryAuthResult result = new com.zscat.mallplus.wxopen.entity.WxOpenQueryAuthResult();
            result.setAppid(wxOpenXmlMessage.getRegistAppId());
            result.setThirdAppid(wxOpenXmlMessage.getAppId());
            result.setCreateTime(new Date());
            result.setStatus(wxOpenXmlMessage.getStatus());
            result.setMsg(wxOpenXmlMessage.getMsg());
            result.setAuthCode(wxOpenXmlMessage.getAuthCode());
            result.setInfoName(wxOpenXmlMessage.getInfo().getName());
            result.setInfoCode(wxOpenXmlMessage.getInfo().getCode());
            result.setLegalPersonName(wxOpenXmlMessage.getInfo().getLegalPersonaName());
            result.setLegalPersonWechant(wxOpenXmlMessage.getInfo().getLegalPersonaWechat());
            result.setInfoComponentPhone(wxOpenXmlMessage.getInfo().getComponentPhone());
            if (queryAuth == null || queryAuth.getAuthorizationInfo() == null || queryAuth.getAuthorizationInfo().getAuthorizerAppid() == null) {
                resultMapper.insert(result);
                throw new NullPointerException("getQueryAuth");
            }
            WxOpenAuthorizationInfo authorizationInfo = queryAuth.getAuthorizationInfo();
            result.setAuthorizerAppid(authorizationInfo.getAuthorizerAppid());
            if (authorizationInfo.getAuthorizerAccessToken() != null) {
                result.setAuthorizerAccessToken(authorizationInfo.getAuthorizerAccessToken());
                result.setExpiresIn(authorizationInfo.getExpiresIn());
            }
            if (authorizationInfo.getAuthorizerRefreshToken() != null) {
                result.setAuthorizerRefreshToken(authorizationInfo.getAuthorizerRefreshToken());
            }
            if (authorizationInfo.getFuncInfo()!=null||authorizationInfo.getFuncInfo().size()!=0){
                result.setFuncInfo(authorizationInfo.getFuncInfo().toString());
            }
            resultMapper.insert(result);
            return "success";
        }
        return "";
    }
}
