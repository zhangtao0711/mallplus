package com.zscat.mallplus.water.entity;

import cn.afterturn.easypoi.entity.ImageEntity;
import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zscat.mallplus.util.BaseEntity;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.util.Date;

//import javax.persistence.Transient;
/**
 * @author lyn
 * @date 2020-05-29
 * 制卡
 */
@Data
public class WtWaterCardExcel extends BaseEntity implements Serializable {

    /**
     * 代号
     **/
    @TableField("code")
    @Excel(name = "代号" )
    private String code;

    /**
     * 卡号
     **/
    @TableField("card_no")
    @Excel(name = "卡号" )
    private String cardNo;

    /**
     * 关联公众号名称
     **/
    @TableField("acName")
    @Excel(name = "公众号名称" )
    private String acName;

    /**
     * 识别码（自动生成）
     **/
    @TableField("distinguish_num")
    @Excel(name = "识别码" )
    private String distinguishNum;

    /**
     * 二维码
     **/
    @TableField("qr_code")
    private String qrCode;

    /**
     * 二维码图片
     **/
    @TableField("qr_code")
    @Excel(name = "二维码图片" )
    private ImageEntity qrCodeFile;



}
