package com.zscat.mallplus.fenxiao.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author zscat
 * @since 2020-05-12
 */
@Setter
@Getter
@TableName("fenxiao_member_total")
public class FenxiaoMemberTotal implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会员编号
     */
    private Long id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 总金额
     */
    @TableField("total_amount")
    private BigDecimal totalAmount;

    /**
     * 当前金额
     */
    @TableField("current_amout")
    private BigDecimal currentAmout;

    /**
     * 冻结金额
     */
    @TableField("frezz_amount")
    private BigDecimal frezzAmount;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;


}
