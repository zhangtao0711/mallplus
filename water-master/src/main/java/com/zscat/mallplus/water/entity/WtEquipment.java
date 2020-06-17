package com.zscat.mallplus.water.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zscat.mallplus.util.BaseEntity;
import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author lyn
 * @date 2020-05-22
 * 设备信息
 */
@Data
@TableName("wt_equipment")
public class WtEquipment extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /**
     * 设备号
     **/
    @TableField("eqcode")
    @NotEmpty(message="设备号不能为空")
    @Length(min=1,max=16,message = "设备号长度必须在1-16位之间！")
    private String eqcode;


//    /**
//     * 设备归属者id
//     **/
//    @TableField("eq_belongs_to")
//    private String eqBelongsTo;

    /**
     * 设备状态【0未安装2限制使用3正常使用】
     **/
    @TableField("state")
    @NotEmpty(message="设备状态不能为空")
    private String state;

    /**
     * 设备型号
     **/
    @TableField("product_id")
    @NotEmpty(message="请选择设备型号！")
    private String productId;

    /**
     * 设备型号名称
     **/
    private transient String productName;

    /**
     * 设备归属分组id
     **/
    @TableField("eq_group_id")
    private Long eqGroupId;

    /**
     * 设备地址
     **/
    @TableField("eq_address")
    @NotEmpty(message="设备地址不能为空")
    @Length(min=1,max=200,message = "设备地址长度必须在1-200位之间！")
    private String eqAddress;


    /**
     * 设备地址经度
     **/
    @TableField("eq_address_longitude")
    @NotEmpty(message="设备地址经度不能为空")
    @Length(min=1,max=20,message = "设备地址经度长度必须在1-20位之间！")
    private String eqAddressLongitude;


    /**
     * 设备地纬度
     **/
    @TableField("eq_address_latitude")
    @NotEmpty(message="设备地纬度不能为空")
    @Length(min=1,max=20,message = "设备地纬度长度必须在1-20位之间！")
    private String eqAddressLatitude;


    /**
     * 设备使用SIM卡
     **/
    @TableField("eq_simcode")
    private String eqSimcode;


    /**
     * 创建日期
     **/
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    /**
     * 创建人
     **/
    @TableField("create_by")
    private Long createBy;


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


    /**
     * 删除状态(0-正常,1-已删除)
     **/
    @TableField("del_flag")
    @TableLogic
    private String delFlag;

    /**
     * 经销商id
     **/
    @TableField("dealer_id")
    private Long dealerId;

    /**--------------------售水参数------------------*/
    /**
     * 投币个数
     */
    @Range(min=1,max = 10,message = "滤芯级别只能输入1-10的正整数！")
    private int tbgs;
    /**
     * 流量检测,--【流量计故障是否切换时间计费--0切换1不切换】
     */
    private int lljc;
    /**
     *  投币器设置,--1一元，2一元五角，0不开启投币器（支持191212及以后）
     */
    private int tbqsz;
    /**
     *  金额回收时间,--【15-240秒】
     */
    @Range(min=15,max = 240,message = "金额回收时间只能输入15-240的正整数（秒）！")
    private int jehssj;
    /**
     *  打水灯延时关闭,--【15-240秒】
     */
    @Range(min=15,max = 240,message = "打水灯延时关闭时间只能输入15-240的正整数（秒）！")
    private int dsdys;
    /**
     *  取水门锁延时,--【15-240秒】
     */
    @Range(min=15,max = 240,message = "取水门锁延时只能输入15-240的正整数（秒）！")
    private int qsmsys;

    /**--------------------制水参数------------------*/
    /**
     * 制水开启延时--【1-5分钟】
     */
    @Range(min=1,max = 5,message = "制水开启延时只能输入1-5的正整数（分钟）！")
    private int zsys;
    /**
     * 冲洗时间,--【2-240秒】
     */
    @Range(min=2,max = 240,message = "冲洗时间只能输入2-240的正整数（秒）！")
    private int cxsj;
    /**
     *  冲洗间隔,--【30-240分钟】
     */
    @Range(min=30,max = 240,message = "制水开启延时只能输入30-240的正整数（分钟）！")
    private int cxjg;
    /**
     *  臭氧杀菌时间,----【1-240秒】
     */
    @Range(min=1,max = 240,message = "臭氧杀菌时间只能输入1-240的正整数（秒）！")
    private int sjsj;
    /**
     *  臭氧杀菌间隔,----【1-240分钟】
     */
    @Range(min=1,max = 240,message = "臭氧杀菌间隔只能输入1-240的正整数（分钟）！")
    private int sjjg;
    /**
     *  保温开关,----【0关闭1开启】
     */
    private int bwkg;
    /**
     *  加热温度下限,----【0-5度】
     */
    @Range(min=0,max = 5,message = "加热温度下限只能输入0-5的正整数（度）！")
    private int wdxx;
    /**
     *  加热温度上限,----【5-35度】
     */
    @Range(min=5,max = 35,message = "加热温度上限只能输入5-35的正整数（度）！")
    private int wdsx;
    /**
     *  广告灯开启时间”,----【HHmm（24小时制）】
     */
    private String ggdkq;
    /**
     *  广告灯关闭时间”,----【HHmm（24小时制）】
     */
    private String ggdgb;
    /**
     *  Ro膜冲洗时间,----【1-255秒】
     */
    @Range(min=1,max = 255,message = "Ro膜冲洗时间只能输入1-255的正整数（秒）！")
    private int rocxsj;
    /**
     *  RO膜冲洗间隔,----【1-36小时】
     */
    @Range(min=1,max = 36,message = "RO膜冲洗间隔只能输入1-36的正整数（小时）！")
    private int rocxjg;
    /**
     *  泵连续工作时长----【1-240分钟】
     */
    @Range(min=1,max = 240,message = "泵连续工作时长只能输入1-240的正整数（分钟）！")
    private int dcbsc;

    /**--------------------设置设备费率------------------*/
    /**
     *  设备用途,--【1单路2双路售水】
     */
    private int purpose;
    /**
     *  扣费类型
     */
    private int ykft;
    /**
     *  预扣费
     */
    private int ykf;
    /**
     *  计费方式,-->1计时2流量
     */
    private int jffs;
    /**
     *  一升水时间,--单位（0.1秒）
     */
    private int ysssj;
    /**
     *  一升水脉冲,--脉冲数
     */
    private int yssmc;
    /**
     *  会员售水水量---毫升
     */
    private int mwq;
    /**
     *  游客售水水量--毫升
     */
    private int twq;

    /**--------------------刷卡询问响应------------------*/
    /**
     *  卡内号或卡编号
     */
    private Long CardNo;

}
