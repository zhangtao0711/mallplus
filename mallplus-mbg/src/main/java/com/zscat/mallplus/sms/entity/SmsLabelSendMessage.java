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
    private Long dealerId;


    /**
     * 1指定标签组2全部发送
     **/
    @TableField("send_type")
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
    private Integer pushMode;


    /**
     * 消息
     **/
    @TableField("message")
    private String message;


    /**
     * 所属店铺
     **/
    @TableField("store_id")
    private Integer storeId;

    @TableField("create_time")
    private Date createTime;

    @TableField("msg_id")
    private String msgId;

    @TableField("status")
    private Integer status;
}
