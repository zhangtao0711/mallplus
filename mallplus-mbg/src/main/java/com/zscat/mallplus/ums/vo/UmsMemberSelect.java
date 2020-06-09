package com.zscat.mallplus.ums.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zscat.mallplus.sms.entity.SmsLabelSet;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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
public class UmsMemberSelect implements Serializable {

    /**
     * 会员卡余额-大于
     */
    private BigDecimal cardMoneyGreater;
    /**
     * 会员卡余额-等于
     */
    private BigDecimal cardMoneyEqual;
    /**
     * 会员卡余额-小于
     */
    private BigDecimal cardMoneyLimit;

    /**
     * 用水频次 0数量1天2周3月4季5年
     **/
    private Integer useWaterFrequency;
    /**
     * 用水频次-大于
     */
    private Integer useWaterGreater;
    /**
     * 用水频次-等于
     */
    private Integer useWaterEqual;
    /**
     * 用水频次-小于
     */
    private Integer useWaterLimit;

    /**
     * 用户推荐频次 0数量1天2周3月4季5年
     **/
    private Integer referrerFrequency;
    /**
     * 用户推荐频次-大于
     */
    private Integer referrerGreater;
    /**
     * 用户推荐频次-等于
     */
    private Integer referrerEqual;
    /**
     * 用户推荐频次-小于
     */
    private Integer referrerLimit;
    /**
     * 会员等级
     */
    private Long memberLevelId;
    /**
     * 用户社区
     */
    private Long objId;
    /**
     * 经销商id
     */
    private Long dealerId;

    private Integer storeId;
}
