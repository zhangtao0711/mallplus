package com.zscat.mallplus.merchant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 * @author mallplus
 * @date 2020-05-14
 * 特约商户进件
 */
@Data
@TableName("merchat_business_materials")
public class MerchatBusinessMaterials implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /**
     * 业务申请编号
     **/
    @TableField("business_code")
    @NotEmpty(message = "业务申请编号不能为空！")
    private String businessCode;


    /**
     * 超级管理员姓名
     **/
    @TableField("contact_name")
    @NotEmpty(message = "超级管理员姓名不能为空！")
    private String contactName;


    /**
     * 超级管理员身份证件号码
     **/
    @TableField("contact_id_number")
    private String contactIdNumber;


    /**
     * 超级管理员微信openid
     **/
    @TableField("openid")
    private String openid;


    /**
     * 联系手机
     **/
    @TableField("mobile_phone")
    @NotEmpty(message = "联系手机不能为空！")
    private String mobilePhone;


    /**
     * 联系邮箱
     **/
    @TableField("contact_email")
    @NotEmpty(message = "联系邮箱不能为空！")
    private String contactEmail;


    /**
     * 主体类型
     **/
    @TableField("subject_type")
    @NotEmpty(message = "联系手机不能为空！")
    private String subjectType;


    /**
     * 营业执照照片
     **/
    @TableField("license_copy")
    private String licenseCopy;


    /**
     * 营业执照照片
     **/
    @TableField("license_copy_media_id")
    private String licenseCopyMediaId;


    /**
     * 注册号/统一社会信用代码
     **/
    @TableField("license_number")
    private String licenseNumber;


    /**
     * 商户名称
     **/
    @TableField("subject_merchant_name")
    private String subjectMerchantName;


    /**
     * 个体户经营者/法人姓名
     **/
    @TableField("subject_legal_person")
    private String subjectLegalPerson;


    /**
     * 登记证书照片
     **/
    @TableField("cert_copy")
    private String certCopy;


    /**
     * 登记证书照片
     **/
    @TableField("cert_copy_media_id")
    private String certCopyMediaId;


    /**
     * 登记证书类型
     **/
    @TableField("cert_type")
    private String certType;


    /**
     * 证书号
     **/
    @TableField("cert_number")
    private String certNumber;


    /**
     * 商户名称
     **/
    @TableField("merchant_name")
    private String merchantName;


    /**
     * 注册地址
     **/
    @TableField("company_address")
    private String companyAddress;


    /**
     * 法人姓名
     **/
    @TableField("legal_person")
    private String legalPerson;


    /**
     * 有效期限开始日期
     **/
    @TableField("period_begin")
    private String periodBegin;


    /**
     * 有效期限结束日期
     **/
    @TableField("period_end")
    private String periodEnd;


    /**
     * 组织机构代码证照片
     **/
    @TableField("organization_copy")
    private String organizationCopy;


    /**
     * 组织机构代码证照片
     **/
    @TableField("organization_copy_media_id")
    private String organizationCopyMediaId;


    /**
     * 组织机构代码
     **/
    @TableField("organization_code")
    private String organizationCode;


    /**
     * 组织机构代码证有效期开始日期
     **/
    @TableField("org_period_begin")
    private String orgPeriodBegin;


    /**
     * 组织机构代码证有效期结束日期
     **/
    @TableField("org_period_end")
    private String orgPeriodEnd;


    /**
     * 单位证明函照片
     **/
    @TableField("certificate_letter_copy")
    private String certificateLetterCopy;


    /**
     * 单位证明函照片
     **/
    @TableField("certificate_letter_copy_media_id")
    private String certificateLetterCopyMediaId;


    /**
     * 证件类型
     **/
    @TableField("id_doc_type")
    @NotEmpty(message = "证件类型不能为空！")
    private String idDocType;


    /**
     * 身份证人像面照片
     **/
    @TableField("id_card_copy")
    private String idCardCopy;


    /**
     * 身份证人像面照片
     **/
    @TableField("id_card_copy_media_id")
    private String idCardCopyMediaId;


    /**
     * 身份证国徽面照片
     **/
    @TableField("id_card_national")
    private String idCardNational;


    /**
     * 身份证国徽面照片
     **/
    @TableField("id_card_national_media_id")
    private String idCardNationalMediaId;


    /**
     * 身份证姓名
     **/
    @TableField("id_card_name")
    private String idCardName;


    /**
     * 身份证号码
     **/
    @TableField("id_card_number")
    private String idCardNumber;


    /**
     * 身份证有效期开始时间
     **/
    @TableField("card_period_begin")
    private Date cardPeriodBegin;


    /**
     * 身份证有效期结束时间
     **/
    @TableField("card_period_end")
    private Date cardPeriodEnd;


    /**
     * 证件照片
     **/
    @TableField("id_doc_copy")
    private String idDocCopy;


    /**
     * 证件照片
     **/
    @TableField("id_doc_copy_media_id")
    private String idDocCopyMediaId;


    /**
     * 证件姓名
     **/
    @TableField(" id_doc_name")
    private String idDocName;


    /**
     * 证件号码
     **/
    @TableField(" id_doc_number")
    private String idDocNumber;


    /**
     * 证件有效期开始时间
     **/
    @TableField("doc_period_begin")
    private Date docPeriodBegin;


    /**
     * 证件有效期结束时间
     **/
    @TableField("doc_period_end")
    private Date docPeriodEnd;


    /**
     * 经营者/法人是否为受益人
     **/
    @TableField("owner")
    @NotEmpty(message = "经营者/法人是否为受益人不能为空！")
    private Integer owner;


    /**
     * 证件类型
     **/
    @TableField("id_type")
    private String idType;


    /**
     * 身份证人像面照片
     **/
    @TableField("ubo_id_card_copy")
    private String uboIdCardCopy;


    /**
     * 身份证人像面照片
     **/
    @TableField("ubo_id_card_copy_media_id")
    private String uboIdCardCopyMediaId;


    /**
     * 身份证国徽面照片
     **/
    @TableField("ubo_id_card_national")
    private String uboIdCardNational;


    /**
     * 身份证国徽面照片
     **/
    @TableField("ubo_id_card_national_media_id")
    private String uboIdCardNationalMediaId;


    /**
     * 证件照片
     **/
    @TableField("ubo_id_doc_copy")
    private String uboIdDocCopy;


    /**
     * 证件照片
     **/
    @TableField("ubo_id_doc_copy_media_id")
    private String uboIdDocCopyMediaId;


    /**
     * 证件姓名
     **/
    @TableField(" ubo_name")
    private String uboName;


    /**
     * 证件号码
     **/
    @TableField(" ubo_id_number")
    private String uboIdNumber;


    /**
     * 证件有效期开始时间
     **/
    @TableField("ubo_id_period_begin")
    private Date uboIdPeriodBegin;


    /**
     * 证件有效期结束时间
     **/
    @TableField("ubo_id_period_end")
    private Date uboIdPeriodEnd;


    /**
     * 商户简称
     **/
    @TableField("merchant_shortname")
    @NotEmpty(message = "商户简称不能为空！")
    private String merchantShortname;


    /**
     * 客服电话
     **/
    @TableField("service_phone")
    @NotEmpty(message = "联系手机不能为空！")
    private String servicePhone;


    /**
     * 经营场景类型
     **/
    @TableField("sales_scenes_type")
    @NotEmpty(message = "联系手机不能为空！")
    private String salesScenesType;


    /**
     * 门店名称
     **/
    @TableField("biz_store_name")
    private String bizStoreName;


    /**
     * 门店省市编码
     **/
    @TableField("biz_address_code")
    private String bizAddressCode;


    /**
     * 门店地址
     **/
    @TableField("biz_store_address")
    private String bizStoreAddress;


    /**
     * 门店门头照片
     **/
    @TableField("store_entrance_pic")
    private String storeEntrancePic;


    /**
     * 门店门头照片
     **/
    @TableField("store_entrance_pic_media_id")
    private String storeEntrancePicMediaId;


    /**
     * 店内环境照片
     **/
    @TableField("indoor_pic")
    private String indoorPic;


    /**
     * 店内环境照片
     **/
    @TableField("indoor_pic_media_id")
    private String indoorPicMediaId;


    /**
     * 线下场所对应的商家APPID
     **/
    @TableField("biz_sub_appid")
    private String bizSubAppid;


    /**
     * 服务商公众号APPID
     **/
    @TableField("mp_appid")
    private String mpAppid;


    /**
     * 商家公众号APPID
     **/
    @TableField("mp_sub_appid")
    private String mpSubAppid;


    /**
     * 公众号页面截图
     **/
    @TableField("mp_pics")
    private String mpPics;


    /**
     * 公众号页面截图
     **/
    @TableField("mp_pics_media_id")
    private String mpPicsMediaId;


    /**
     * 服务商小程序APPID
     **/
    @TableField("mini_program_appid")
    private String miniProgramAppid;


    /**
     * 服务商小程序APPID
     **/
    @TableField("mini_program_sub_appid")
    private String miniProgramSubAppid;


    /**
     * 服务商小程序页面截图
     **/
    @TableField("mini_program_pics")
    private String miniProgramPics;


    /**
     * 服务商小程序页面截图
     **/
    @TableField("mini_program_pics_media_id")
    private String miniProgramPicsMediaId;


    /**
     * 服务商应用APP的APPID
     **/
    @TableField("app_appid")
    private String appAppid;


    /**
     * 服务商应用APP的APPID
     **/
    @TableField("app_sub_appid")
    private String appSubAppid;


    /**
     * 服务商应用APP的页面截图
     **/
    @TableField("app_pics")
    private String appPics;


    /**
     * 服务商应用APP的页面截图
     **/
    @TableField("app_pics_media_id")
    private String appPicsMediaId;


    /**
     * 互联网网站域名
     **/
    @TableField("domain")
    private String domain;


    /**
     * 网站授权函
     **/
    @TableField("web_authorisation")
    private String webAuthorisation;


    /**
     * 网站授权函
     **/
    @TableField("web_authorisation_media_id")
    private String webAuthorisationMediaId;


    /**
     * 互联网网站对应的商家APPID
     **/
    @TableField("web_appid")
    private String webAppid;


    /**
     * 商家企业微信CorpID
     **/
    @TableField("sub_corp_id")
    private String subCorpId;


    /**
     * 企业微信页面截图
     **/
    @TableField("wework_pics")
    private String weworkPics;


    /**
     * 企业微信页面截图
     **/
    @TableField("wework_pics_media_id")
    private String weworkPicsMediaId;


    /**
     * 入驻结算规则ID
     **/
    @TableField("settlement_id")
    @NotEmpty(message = "入驻结算规则ID不能为空！")
    private String settlementId;


    /**
     * 所属行业
     **/
    @TableField("qualification_type")
    @NotEmpty(message = "所属行业不能为空！")
    private String qualificationType;


    /**
     * 特殊资质图片
     **/
    @TableField("qualifications")
    private String qualifications;


    /**
     * 特殊资质图片
     **/
    @TableField("qualifications_media_id")
    private String qualificationsMediaId;


    /**
     * 优惠费率活动ID
     **/
    @TableField("activities_id")
    private String activitiesId;


    /**
     * 优惠费率活动值
     **/
    @TableField(" activities_rate")
    private String activitiesRate;


    /**
     * 优惠费率活动补充材料
     **/
    @TableField("activities_additions")
    private String activitiesAdditions;


    /**
     * 优惠费率活动补充材料
     **/
    @TableField("activities_additions_media")
    private String activitiesAdditionsMedia;


    /**
     * 账户类型
     **/
    @TableField("bank_account_type")
    private String bankAccountType;


    /**
     * 开户名称
     **/
    @TableField("account_name")
    private String accountName;


    /**
     * 开户银行
     **/
    @TableField("account_bank")
    private String accountBank;


    /**
     * 开户银行省市编码
     **/
    @TableField("bank_address_code")
    private String bankAddressCode;


    /**
     * 开户银行联行号
     **/
    @TableField("bank_branch_id")
    private String bankBranchId;


    /**
     * 开户银行全称（含支行)
     **/
    @TableField("bank_name")
    private String bankName;


    /**
     * 银行账号
     **/
    @TableField(" account_number")
    private String accountNumber;


    /**
     * 法人开户承诺函
     **/
    @TableField("legal_person_commitment")
    private String legalPersonCommitment;


    /**
     * 法人开户承诺函
     **/
    @TableField("legal_person_commitment_media_id")
    private String legalPersonCommitmentMediaId;


    /**
     * 法人开户意愿视频
     **/
    @TableField("legal_person_video")
    private String legalPersonVideo;


    /**
     * 法人开户意愿视频
     **/
    @TableField("legal_person_video_media_id")
    private String legalPersonVideoMediaId;


    /**
     * 补充材料
     **/
    @TableField("business_addition_pics")
    private String businessAdditionPics;


    /**
     * 补充材料
     **/
    @TableField("business_addition_pics_media_id")
    private String businessAdditionPicsMediaId;


    /**
     * 补充说明
     **/
    @TableField("business_addition_msg")
    private String businessAdditionMsg;


    /**
     * 微信支付申请单号
     **/
    @TableField("applyment_id")
    private String applymentId;


    /**
     * 申请单状态
     **/
    @TableField("applyment_state")
    private String applymentState;


    /**
     * 特约商户号
     **/
    @TableField("sub_mchid")
    private String subMchid;


    /**
     * 超级管理员签约链接
     **/
    @TableField("sign_url")
    private String signUrl;


    /**
     * 申请状态描述
     **/
    @TableField("applyment_state_msg")
    private String applymentStateMsg;


    /**
     * 字段名
     **/
    @TableField("field")
    private String field;


    /**
     * 字段名称
     **/
    @TableField("field_name")
    private String fieldName;


    /**
     * 驳回原因
     **/
    @TableField("reject_reason")
    private String rejectReason;


    /**
     * 创建人
     **/
    @TableField("create_by")
    private Long createBy;


    /**
     * 创建时间
     **/
    @TableField("create_time")
    private Date createTime;


    /**
     * 更新人
     **/
    @TableField("update_by")
    private Long updateBy;


    /**
     * 更新时间
     **/
    @TableField("update_time")
    private Date updateTime;

    @TableField("store_id")
    private Integer storeId;

    @TableField("store_name")
    private String storeName;

}
