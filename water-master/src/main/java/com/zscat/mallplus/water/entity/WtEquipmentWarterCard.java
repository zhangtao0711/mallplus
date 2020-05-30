package com.zscat.mallplus.water.entity;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;


import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author lyn
 * @date 2020-05-30
 * 设备水卡关联信息表
 */
@Data
@TableName("wt_equipment_warter_card")
public class WtEquipmentWarterCard implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 设备号
     **/
    @TableField("eqcode")
    private String eqcode;


    /**
     * 卡号
     **/
    @TableField("card_no")
    private String cardNo;


}
