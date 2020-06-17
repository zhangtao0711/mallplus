package com.zscat.mallplus.water.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author lyn
 * @date 2020-06-17
 * 设备详细参数
 */
@Data
@TableName("wt_equipment_detailes")
public class WtEquipmentDetailes extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 设备号
     **/
    @TableField("eqcode")
    private String eqcode;


    /**
     * 设备名称
     **/
    @TableField("equipmentName")
    @Length(min=1, max=15, message="设备名称必须介于 1 和 15 之间!")
    private String equipmentname;


    /**
     * 软件版本
     **/
    @TableField("equipmentVr")
    @Length(min=1, max=25, message="软件版本必须介于 1 和 25 之间!")
    private String equipmentvr;


    /**
     * 当前信号强度
     **/
    @TableField("sig")
    private Integer sig;


    /**
     * 当前温度（40=4.0℃）
     **/
    @TableField("wd")
    private Integer wd;


    /**
     * 当前开/关机状态(01=开机、02=停机)
     **/
    @TableField("onOff")
    private String onoff;


    /**
     * 当前温控状态(01=开启、02=关闭)
     **/
    @TableField("wdState")
    private String wdstate;


    /**
     * //当前灯箱状态(01=开启、02=关闭)
     **/
    @TableField("lampState")
    private String lampstate;


    /**
     * //当前制水状态(01=正在制水、02=水箱水满)
     **/
    @TableField("zsysState")
    private String zsysstate;


    /**
     * //当前售水状态(01=正常售水、
     **/
    @TableField("saleWaterState")
    private String salewaterstate;


    /**
     * //当前售卖状态(00=空闲,此时可接新单...)
     **/
    @TableField("saleState")
    private String salestate;


    /**
     * //为社区编号(后台生成)
     **/
    @TableField("communityNo")
    @Length(min=1, max=6, message="社区编号长度必须介于 1 和 6 之间!")
    private String communityno;


    /**
     * //服务器地址
     **/
    @TableField("ipAddress")
    private String ipaddress;


    /**
     * //端口
     **/
    @TableField("port")
    private String port;


    /**
     * //扇区
     **/
    @TableField("shaqu")
    @Length(min=1, max=2, message="扇区长度必须介于 1 和 2 之间!")
    private String shaqu;


    /**
     * //扇区密码
     **/
    @TableField("shaqumima")
    @Length(min=6, max=6, message="扇区密码长度必须6位!")
    private String shaqumima;


    /**
     * //单次消费限额（分）
     **/
    @TableField("xfxe")
    @Range(min=1,max = 9999,message = "单次消费限额（分）只能输入1-9999的正整数！")
    private String xfxe;


    /**
     * //灯箱开启时间(17时00分00秒)
     **/
    @TableField("dxkq")
    @Length(min=6, max=6, message="灯箱开启时间例如17时30分00秒需要设定【173000】!")
    private String dxkq;


    /**
     * //灯箱关闭时间(05时00分00秒)
     **/
    @TableField("dxgb")
    @Length(min=6, max=6, message="灯箱关闭时间例如05时00分00秒需要设定【050000】!")
    private String dxgb;


    /**
     * //温控启动温度值℃
     **/
    @TableField("wdqd")
    @DecimalMin(value="0.1")
    @DecimalMax(value="99.9")
    @Digits(integer=2, fraction=1,message="温控启动温度值不能设定大于99.9,小数位数只支持1位。")
    private String wdqd;


    /**
     * //温控关闭温度值℃
     **/
    @TableField("wdgb")
    @DecimalMin(value="0.1")
    @DecimalMax(value="99.9")
    @Digits(integer=2, fraction=1,message="温控关闭温度值不能设定大于99.9,小数位数只支持1位。")
    private String wdgb;


    /**
     * //臭氧启动时长s
     **/
    @TableField("sjsj")
    @Range(min=1,max = 9999,message = "臭氧启动时长（s）只能输入1-9999的正整数！")
    private String sjsj;


    /**
     * //臭氧启动周期s
     **/
    @TableField("sjjg")
    @Range(min=1,max = 9999,message = "臭氧启动周期（s）只能输入1-9999的正整数！")
    private String sjjg;


    /**
     * //开机冲洗时长
     **/
    @TableField("cxsj")
    @Range(min=1,max = 9999,message = "开机冲洗时长（s）只能输入1-9999的正整数！")
    private String cxsj;


    /**
     * //水满停机延时
     **/
    @TableField("smtj")
    @Range(min=1,max = 9999,message = "水满停机延时（s）只能输入1-9999的正整数！")
    private String smtj;


    /**
     * //制水启动延时
     **/
    @TableField("zsqd")
    @Range(min=1,max = 9999,message = "制水启动延时（s）只能输入1-9999的正整数！")
    private String zsqd;


    /**
     * 更新日期
     **/
    @TableField("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


    /**
     * 更新人
     **/
    @TableField("update_by")
    private Long updateBy;


}
