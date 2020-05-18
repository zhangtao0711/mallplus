package com.zscat.mallplus.oms.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by Administrator on 2019/9/15.
 */
@Data
public class StoreDataParam implements Serializable {

    private Integer storeId;

    //优惠券id
    private Long couponId;
    private Long memberCouponId;
    private String desc;
}
