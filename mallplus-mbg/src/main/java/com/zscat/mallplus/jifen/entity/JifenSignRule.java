package com.zscat.mallplus.jifen.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author mallplus
 * @date 2019-12-17
 * 积分签到规则
 */
@Data
@TableName("jifen_sign_rule")
public class JifenSignRule implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    @TableField("contineu_count")
    @NotNull(message = "连续几天不能为空！")
    @Min(1)
    private Integer contineuCount;


    @TableField("donate_integrtion")
    private Integer donateIntegrtion;

    @TableField("dealer_id")
    private Long dealerId;

    @TableField(exist = false)
    private Integer status;//状态 0未达标 1领取 2 已领取

}
