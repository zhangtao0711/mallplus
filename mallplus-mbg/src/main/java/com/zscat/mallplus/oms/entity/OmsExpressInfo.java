package com.zscat.mallplus.oms.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 物流信息表
 * </p>
 *
 * @author zscat
 * @since 2020-04-30
 */
@TableName("oms_express_info")
public class OmsExpressInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @TableField("ORDER_ID")
    private Integer orderId;

    /**
     * 物流公司编码
     */
    @TableField("EXPRESS_CORP_ID")
    private String expressCorpId;

    /**
     * 物流公司名称
     */
    @TableField("EXPRESS_CORP_NAME")
    private String expressCorpName;

    /**
     * 运单号
     */
    @TableField("EXPRESS_NO")
    private String expressNo;

    /**
     * 运单状态0.初始化 1.已发货 2 已签收
     */
    @TableField("EXPRESS_STATUS")
    private Integer expressStatus;

    /**
     * 物流详情
     */
    @TableField("EXPRESS_DETAIL")
    private String expressDetail;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    /**
     * 所属店铺
     */
    @TableField("store_id")
    private Integer storeId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getExpressCorpId() {
        return expressCorpId;
    }

    public void setExpressCorpId(String expressCorpId) {
        this.expressCorpId = expressCorpId;
    }

    public String getExpressCorpName() {
        return expressCorpName;
    }

    public void setExpressCorpName(String expressCorpName) {
        this.expressCorpName = expressCorpName;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public Integer getExpressStatus() {
        return expressStatus;
    }

    public void setExpressStatus(Integer expressStatus) {
        this.expressStatus = expressStatus;
    }

    public String getExpressDetail() {
        return expressDetail;
    }

    public void setExpressDetail(String expressDetail) {
        this.expressDetail = expressDetail;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    @Override
    public String toString() {
        return "OmsExpressInfo{" +
        ", id=" + id +
        ", orderId=" + orderId +
        ", expressCorpId=" + expressCorpId +
        ", expressCorpName=" + expressCorpName +
        ", expressNo=" + expressNo +
        ", expressStatus=" + expressStatus +
        ", expressDetail=" + expressDetail +
        ", updateTime=" + updateTime +
        ", storeId=" + storeId +
        "}";
    }
}
