package com.zscat.mallplus.water.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lyn
 * @date 2020-05-22
 * SIM卡第三方appkey
 */
@Data
public class SimDataEntity implements Serializable {


    /**
     * 余额
     **/
    private String amount;
    /**
     * 订单号
     **/
    private String orderId;

}
