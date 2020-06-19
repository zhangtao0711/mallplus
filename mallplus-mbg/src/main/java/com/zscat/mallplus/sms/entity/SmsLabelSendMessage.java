package com.zscat.mallplus.sms.entity;

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
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author wang
 * @date 2020-05-30
 * 手动发送记录
 */
@Data
@TableName("sms_label_send_message")
public class SmsLabelSendMessage extends BaseEntity implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 经销商id
     **/
    @TableField("dealer_id")
    @NotNull(message = "经销商不能为空！")
    private Long dealerId;


    /**
     * 1指定标签组2全部发送
     **/
    @TableField("send_type")
    @NotNull(message = "发送类型不能为空！")
    @Min(1)
    @Max(2)
    private Integer sendType;


    /**
     * 标签组id
     **/
    @TableField("label_id")
    private Long labelId;


    /**
     * 1自定义消息
     **/
    @TableField("push_mode")
    @NotNull(message = "推送模式不能为空！")
    @Min(1)
    @Max(1)
    private Integer pushMode;


    /**
     * 消息
     **/
    @TableField("message")
    @NotEmpty(message = "消息内容不能为空！")
    @Length(min = 1,max = 100,message = "消息内容在1-100字之间")
    private String message;


    /**
     * 所属店铺
     **/
    @TableField("store_id")
    @NotNull(message = "所属门店不能为空！")
    private Integer storeId;

    @TableField("create_time")
    private Date createTime;

    @TableField("msg_id")
    private String msgId;

    @TableField("status")
    @NotNull(message = "发送状态不能为空！")
    private Integer status;
}
