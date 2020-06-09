package com.zscat.mallplus.water.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 * @author lyn
 * @date 2020-05-22
 * SIM卡第三方appkey
 */
@Data
public class SimEntity {

    /**
     * 物联网卡状态
     **/
    private String status;
    /**
     * 物联网卡状态描述
     **/
    private String message;
    /**
     * 	卡有效期
     **/
    private Date expired_at;
    /**
     * 卡号
     **/
    private String cardno;
    /**
     * 使用流量(-1表示未知，单位KB，时间当月)
     **/
    private String used;
    /**
     * 剩余流量(单位KB)
     **/
    private String surplus;
    /**
     * 订单号
     **/
    private String order_id;
}
