package com.zscat.mallplus.wxopen.entity;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zscat.mallplus.utils.BaseEntity;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author wang
 * @date 2020-06-17
 * 小程序创建记录
 */
@Data
@TableName("wx_open_query_auth_result")
public class WxOpenQueryAuthResult extends BaseEntity implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 创建的APPID
     **/
    @TableField("appid")
    private String appid;


    /**
     * 创建时间
     **/
    @TableField("create_time")
    private Date createTime;


    /**
     * 第三方APPID
     **/
    @TableField("third_appid")
    private String thirdAppid;


    /**
     * 状态
     * status 返回	含义
     * 100001	已下发的模板消息法人并未确认且已超时（24h），未进行身份证校验
     * 100002	已下发的模板消息法人并未确认且已超时（24h），未进行人脸识别校验
     * 100003	已下发的模板消息法人并未确认且已超时（24h）
     * 101	工商数据返回：“企业已注销”
     * 102	工商数据返回：“企业不存在或企业信息未更新”
     * 103	工商数据返回：“企业法定代表人姓名不一致”
     * 104	工商数据返回：“企业法定代表人身份证号码不一致”
     * 105	法定代表人身份证号码，工商数据未更新，请 5-15 个工作日之后尝试
     * 1000	工商数据返回：“企业信息或法定代表人信息不一致”
     * 1001	主体创建小程序数量达到上限
     * 1002	主体违规命中黑名单
     * 1003	管理员绑定账号数量达到上限
     * 1004	管理员违规命中黑名单
     * 1005	管理员手机绑定账号数量达到上限
     * 1006	管理员手机号违规命中黑名单
     * 1007	管理员身份证创建账号数量达到上限
     * 1008	管理员身份证违规命中黑名单
     * -1	企业与法人姓名不一致
     **/
    @TableField("status")
    private Integer status;


    /**
     * message
     **/
    @TableField("msg")
    private String msg;


    /**
     * xxxxx第三方授权码
     **/
    @TableField("auth_code")
    private String authCode;


    /**
     * 企业名称
     **/
    @TableField("info_name")
    private String infoName;


    /**
     * 企业代码
     **/
    @TableField("info_code")
    private String infoCode;


    /**
     *
     **/
    @TableField("info_code_type")
    private String infoCodeType;


    /**
     * 法人微信号
     **/
    @TableField("legal_person_wechant")
    private String legalPersonWechant;


    /**
     * 法人姓名
     **/
    @TableField("legal_person_name")
    private String legalPersonName;


    /**
     * 第三方联系电话
     **/
    @TableField("info_component_phone")
    private String infoComponentPhone;


    /**
     * 授权的APPID
     **/
    @TableField("authorizer_appid")
    private String authorizerAppid;


    /**
     * token
     **/
    @TableField("authorizer_access_token")
    private String authorizerAccessToken;


    /**
     *
     **/
    @TableField("expires_in")
    private Integer expiresIn;


    /**
     * 刷新token
     **/
    @TableField("authorizer_refresh_token")
    private String authorizerRefreshToken;


    /**
     *
     **/
    @TableField("func_info")
    private String funcInfo;


}
