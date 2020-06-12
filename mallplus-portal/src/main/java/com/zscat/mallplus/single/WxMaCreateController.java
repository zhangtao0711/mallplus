package com.zscat.mallplus.single;

import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.pay.utils.StringUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.wxminiapp.service.WxOpenServiceDemo;
import com.zscat.mallplus.vo.wxminiapp.FastRegisterWeappVo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.open.bean.message.WxOpenXmlMessage;
import me.chanjar.weixin.open.bean.result.WxOpenResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;

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
    /**
     * 快速创建小程序
     *
     */
    @SysLog(MODULE = "wxapp", REMARK = "快速创建小程序")
    @ApiOperation("快速创建小程序")
    @PostMapping("/fastRegisterWeapp")
    public Object fastRegisterWeapp(@RequestBody FastRegisterWeappVo entity) {
        if (entity==null|| StringUtils.isBlank(entity.getName())||StringUtils.isBlank(entity.getCode())
                ||StringUtils.isBlank(entity.getCodeType())||StringUtils.isBlank(entity.getComponentPhone())
        ||StringUtils.isBlank(entity.getLegalPersonaName())||StringUtils.isBlank(entity.getLegalPersonaWechat())){
            return new CommonResult().failed("参数不能为空！");
        }
        try {
            WxOpenResult result =  wxOpenServiceDemo.getWxOpenComponentService().fastRegisterWeapp(entity.getName(),
                    entity.getCode(),
                    entity.getCodeType(),
                    entity.getLegalPersonaWechat(),
                    entity.getLegalPersonaName(),
                    entity.getComponentPhone());
            if (result.isSuccess()){
                return new CommonResult().success("创建成功！");
            }else {
                return new CommonResult().failed("创建失败，错误日志："+result.toString());
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
            return new CommonResult().failed("创建失败，请重试！");
        }

    }

    /**
     * 快速创建小程序查询
     *
     */
    @SysLog(MODULE = "wxapp", REMARK = "快速创建小程序查询")
    @ApiOperation("快速创建小程序查询")
    @PostMapping("/fastRegisterWeappSearch")
    public Object fastRegisterWeappSearch(FastRegisterWeappVo entity) {
        try {
            WxOpenResult result =  wxOpenServiceDemo.getWxOpenComponentService().fastRegisterWeappSearch(entity.getName(),
                    entity.getLegalPersonaWechat(),
                    entity.getLegalPersonaName());
            if (result.isSuccess()){
                return new CommonResult().success("创建成功！");
            }else {
                return new CommonResult().failed("创建失败，错误日志："+result.toString());
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
    public String getComponentVerifyTicket(@RequestParam("timestamp")String timestamp, @RequestParam("nonce")String nonce,
                                           @RequestParam("msg_signature")String msgSignature, @RequestBody String postData ){
        log.info("接收component_verify_ticket 或 authorized事件");
        log.info("nonce: " + nonce);
        log.info("timestamp: " + timestamp);
        log.info("msgSignature: " + msgSignature);
        WxOpenXmlMessage wxOpenXmlMessage = WxOpenXmlMessage.fromEncryptedXml(postData,wxOpenServiceDemo.getWxOpenConfigStorage(),timestamp,nonce,msgSignature);
        jdbcTemplate.update("update admin_dsn_domin set component_verify_ticket = "+wxOpenXmlMessage.getComponentVerifyTicket()+"where id = 1");
        wxOpenServiceDemo.getWxOpenConfigStorage().setComponentVerifyTicket(wxOpenXmlMessage.getComponentVerifyTicket());
        return "success";
    }
}
