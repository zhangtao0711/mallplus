package com.zscat.mallplus.ums.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zscat.mallplus.sms.entity.SmsLabelSet;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 会员表
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Data
@TableName("ums_member")
public class UmsMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("uniacid")
    private Integer uniacid;
    @TableField("uid")
    private Integer uid;
    @TableField("groupid")
    private String groupid;
    @TableField("pid")
    private Long pid;
    @TableField("gid")
    private Long gid;

    @TableField("member_level_id")
    private Long memberLevelId;
    @TableField("member_level_name")
    private String memberLevelName;
    @TableField("area_id")
    private Long areaId;
    @TableField("area_name")
    private String areaName;
    @TableField("school_name")
    private String schoolName;
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 帐号启用状态:0->禁用；1->启用
     */
    private Integer status;

    /**
     * 注册时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 头像
     */
    private String icon;

    /**
     * 性别：0->未知；1->男；2->女
     */
    private Integer gender;

    /**
     * 生日
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date birthday;

    /**
     * 所做城市
     */
    private String city;

    /**
     * 上级用户昵称
     */
    private String job;

    /**
     * 个性签名
     */
    @TableField("personalized_signature")
    private String personalizedSignature;

    /**
     * 用户来源 1 小程序 2 h5 3 app
     */
    @TableField("source_type")
    private Integer sourceType;

    /**
     * 积分
     */
    private Integer integration;

    @TableField("room_nums")
    private String roomNums;
    @TableField("room_desc")
    private String roomDesc;
    /**
     * 成长值
     */
    private Integer growth;

    /**
     * 剩余抽奖次数
     */
    @TableField("luckey_count")
    private Integer luckeyCount;

    /**
     * 历史积分数量
     */
    @TableField("history_integration")
    private Integer historyIntegration;

    private String avatar;

    @TableField("weixin_openid")
    private String weixinOpenid;

    /**
     * 上级用户编号
     */
    private String invitecode;

    @TableField("buy_count")
    private Integer buyCount;
    @TableField("buy_money")
    private BigDecimal buyMoney;
    @TableField("store_id")
    private Integer storeId;
    /**
     * 余额
     */
    private BigDecimal blance;

    @TableField("school_id")
    private Long schoolId;
    /**
     * 经销商id
     */
    @TableField("dealer_id")
    private Long dealerId;

    @TableField(exist = false)
    private String confimpassword;

    @TableField(exist = false)
    private String phonecode;

    @TableField(exist = false)
    private Integer buyCountss;

    @TableField("weixin")
    private String weixin;

    /**
     * 积分状态 积分状态0冻结1正常
     */
    @TableField("integration_status")
    private Integer integrationStatus;
    /**
     * 实体卡号
     */
    private transient String cardNo;
//    /**
//     * 虚拟卡号
//     */
//    private transient String cardNoInvented;
    /**
     * 余额
     */
    private transient BigDecimal cardMoney;
    /**
     * 用户标签
     */
    private transient List<SmsLabelSet> labelList;
    /**
     * 公众号名称
     */
    private transient String uniacName;
    /**
     * 推荐人名称
     */
    private transient String recommendName;
    /**
     * 推荐用户数
     */
    private transient String recommendNum;
    /**
     * 用户标签
     */
    private transient String labelName;

    /**
     * 实体卡数量
     */
    private transient String cardNum;
    /**
     * 虚拟卡数量
     */
    private transient String cardInventedNum;
    /**
     * 体验金额
     **/
    private transient BigDecimal experienceMoney;
    /**
     * 体验到期日
     **/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private transient Date experienceEndData;

    /**
     * 赠送金额
     **/
    private transient BigDecimal giveMoney;

//    /**
//     * 用户标签ids
//     */
//    private String labelIds;

}
