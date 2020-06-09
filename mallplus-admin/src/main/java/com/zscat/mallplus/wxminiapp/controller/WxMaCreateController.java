package com.zscat.mallplus.wxminiapp.controller;

import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.wxminiapp.service.WxOpenServiceDemo;
import com.zscat.mallplus.wxminiapp.vo.FastRegisterWeappVo;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.open.bean.result.WxOpenResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 *  第三方平台快速创建小程序接口
 *  Created by BinaryWang on 2017/6/16.
 * </pre>
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@RestController
@RequestMapping("/wx/create/wxapp")
public class WxMaCreateController {

    @Autowired
    private WxOpenServiceDemo wxOpenServiceDemo;
    /**
     * 快速创建小程序
     *
     */
    @SysLog(MODULE = "wxapp", REMARK = "快速创建小程序")
    @ApiOperation("快速创建小程序")
    @PostMapping("/fastRegisterWeapp")
    public Object fastRegisterWeapp(FastRegisterWeappVo entity) {
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
}
