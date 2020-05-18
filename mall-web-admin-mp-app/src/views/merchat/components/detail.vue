<template> 
    <el-card class="form-container" shadow="never">
        <el-form :model="merchatBusinessMaterials" :rules="rules" ref="MerchatBusinessMaterialsFrom" label-width="150px">


                    <el-form-item
                            label="主键"
                            prop="id">
                        <el-input v-model="merchatBusinessMaterials.id" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="业务申请编号 "
                            prop="businessCode">
                        <el-input v-model="merchatBusinessMaterials.businessCode" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="超级管理员姓名"
                            prop="contactName">
                        <el-input v-model="merchatBusinessMaterials.contactName" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="超级管理员身份证件号码 "
                            prop="contactIdNumber">
                        <el-input v-model="merchatBusinessMaterials.contactIdNumber" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="超级管理员微信openid"
                            prop="openid">
                        <el-input v-model="merchatBusinessMaterials.openid" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="联系手机 "
                            prop="mobilePhone">
                        <el-input v-model="merchatBusinessMaterials.mobilePhone" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="联系邮箱"
                            prop="contactEmail">
                        <el-input v-model="merchatBusinessMaterials.contactEmail" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="主体类型"
                            prop="subjectType">
                        <el-input v-model="merchatBusinessMaterials.subjectType" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="营业执照照片"
                            prop="licenseCopy">
                        <el-input v-model="merchatBusinessMaterials.licenseCopy" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="营业执照照片"
                            prop="licenseCopyMediaId">
                        <el-input v-model="merchatBusinessMaterials.licenseCopyMediaId" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="注册号/统一社会信用代码 "
                            prop="licenseNumber">
                        <el-input v-model="merchatBusinessMaterials.licenseNumber" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="商户名称 "
                            prop="subjectMerchantName">
                        <el-input v-model="merchatBusinessMaterials.subjectMerchantName" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="个体户经营者/法人姓名"
                            prop="subjectLegalPerson">
                        <el-input v-model="merchatBusinessMaterials.subjectLegalPerson" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="登记证书照片"
                            prop="certCopy">
                        <el-input v-model="merchatBusinessMaterials.certCopy" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="登记证书照片"
                            prop="certCopyMediaId">
                        <el-input v-model="merchatBusinessMaterials.certCopyMediaId" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="登记证书类型"
                            prop="certType">
                        <el-input v-model="merchatBusinessMaterials.certType" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="证书号 "
                            prop="certNumber">
                        <el-input v-model="merchatBusinessMaterials.certNumber" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="商户名称 "
                            prop="merchantName">
                        <el-input v-model="merchatBusinessMaterials.merchantName" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="注册地址"
                            prop="companyAddress">
                        <el-input v-model="merchatBusinessMaterials.companyAddress" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="法人姓名 "
                            prop="legalPerson">
                        <el-input v-model="merchatBusinessMaterials.legalPerson" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="有效期限开始日期 "
                            prop="periodBegin">
                        <el-input v-model="merchatBusinessMaterials.periodBegin" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="有效期限结束日期"
                            prop="periodEnd">
                        <el-input v-model="merchatBusinessMaterials.periodEnd" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="组织机构代码证照片"
                            prop="organizationCopy">
                        <el-input v-model="merchatBusinessMaterials.organizationCopy" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="组织机构代码证照片"
                            prop="organizationCopyMediaId">
                        <el-input v-model="merchatBusinessMaterials.organizationCopyMediaId" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="组织机构代码"
                            prop="organizationCode">
                        <el-input v-model="merchatBusinessMaterials.organizationCode" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="组织机构代码证有效期开始日期"
                            prop="orgPeriodBegin">
                        <el-input v-model="merchatBusinessMaterials.orgPeriodBegin" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="组织机构代码证有效期结束日期"
                            prop="orgPeriodEnd">
                        <el-input v-model="merchatBusinessMaterials.orgPeriodEnd" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="单位证明函照片"
                            prop="certificateLetterCopy">
                        <el-input v-model="merchatBusinessMaterials.certificateLetterCopy" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="单位证明函照片"
                            prop="certificateLetterCopyMediaId">
                        <el-input v-model="merchatBusinessMaterials.certificateLetterCopyMediaId" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="证件类型 "
                            prop="idDocType">
                        <el-input v-model="merchatBusinessMaterials.idDocType" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="身份证人像面照片"
                            prop="idCardCopy">
                        <el-input v-model="merchatBusinessMaterials.idCardCopy" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="身份证人像面照片"
                            prop="idCardCopyMediaId">
                        <el-input v-model="merchatBusinessMaterials.idCardCopyMediaId" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="身份证国徽面照片"
                            prop="idCardNational">
                        <el-input v-model="merchatBusinessMaterials.idCardNational" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="身份证国徽面照片"
                            prop="idCardNationalMediaId">
                        <el-input v-model="merchatBusinessMaterials.idCardNationalMediaId" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="身份证姓名"
                            prop="idCardName">
                        <el-input v-model="merchatBusinessMaterials.idCardName" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="身份证号码"
                            prop="idCardNumber">
                        <el-input v-model="merchatBusinessMaterials.idCardNumber" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="身份证有效期开始时间"
                            prop="cardPeriodBegin">
                        <el-input v-model="merchatBusinessMaterials.cardPeriodBegin" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="身份证有效期结束时间 "
                            prop="cardPeriodEnd">
                        <el-input v-model="merchatBusinessMaterials.cardPeriodEnd" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="证件照片"
                            prop="idDocCopy">
                        <el-input v-model="merchatBusinessMaterials.idDocCopy" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="证件照片"
                            prop="idDocCopyMediaId">
                        <el-input v-model="merchatBusinessMaterials.idDocCopyMediaId" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="证件姓名"
                            prop="
idDocName">
                        <el-input v-model="merchatBusinessMaterials.
idDocName" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="证件号码"
                            prop="
idDocNumber">
                        <el-input v-model="merchatBusinessMaterials.
idDocNumber" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="证件有效期开始时间"
                            prop="docPeriodBegin">
                        <el-input v-model="merchatBusinessMaterials.docPeriodBegin" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="证件有效期结束时间 "
                            prop="docPeriodEnd">
                        <el-input v-model="merchatBusinessMaterials.docPeriodEnd" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="经营者/法人是否为受益人"
                            prop="owner">
                        <el-input v-model="merchatBusinessMaterials.owner" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="证件类型"
                            prop="idType">
                        <el-input v-model="merchatBusinessMaterials.idType" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="身份证人像面照片"
                            prop="uboIdCardCopy">
                        <el-input v-model="merchatBusinessMaterials.uboIdCardCopy" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="身份证人像面照片"
                            prop="uboIdCardCopyMediaId">
                        <el-input v-model="merchatBusinessMaterials.uboIdCardCopyMediaId" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="身份证国徽面照片"
                            prop="uboIdCardNational">
                        <el-input v-model="merchatBusinessMaterials.uboIdCardNational" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="身份证国徽面照片"
                            prop="uboIdCardNationalMediaId">
                        <el-input v-model="merchatBusinessMaterials.uboIdCardNationalMediaId" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="身份证姓名"
                            prop="uboIdCardName">
                        <el-input v-model="merchatBusinessMaterials.uboIdCardName" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="身份证号码"
                            prop="uboIdCardNumber">
                        <el-input v-model="merchatBusinessMaterials.uboIdCardNumber" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="身份证有效期开始时间"
                            prop="uboCardPeriodBegin">
                        <el-input v-model="merchatBusinessMaterials.uboCardPeriodBegin" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="身份证有效期结束时间 "
                            prop="uboCardPeriodEnd">
                        <el-input v-model="merchatBusinessMaterials.uboCardPeriodEnd" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="证件照片"
                            prop="uboIdDocCopy">
                        <el-input v-model="merchatBusinessMaterials.uboIdDocCopy" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="证件照片"
                            prop="uboIdDocCopyMediaId">
                        <el-input v-model="merchatBusinessMaterials.uboIdDocCopyMediaId" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="证件姓名"
                            prop="
uboIdDocName">
                        <el-input v-model="merchatBusinessMaterials.
uboIdDocName" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="证件号码"
                            prop="
uboIdDocNumber">
                        <el-input v-model="merchatBusinessMaterials.
uboIdDocNumber" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="证件有效期开始时间"
                            prop="uboDocPeriodBegin">
                        <el-input v-model="merchatBusinessMaterials.uboDocPeriodBegin" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="证件有效期结束时间 "
                            prop="uboDocPeriodEnd">
                        <el-input v-model="merchatBusinessMaterials.uboDocPeriodEnd" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="商户简称"
                            prop="merchantShortname">
                        <el-input v-model="merchatBusinessMaterials.merchantShortname" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="客服电话 "
                            prop="servicePhone">
                        <el-input v-model="merchatBusinessMaterials.servicePhone" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="经营场景类型"
                            prop="salesScenesType">
                        <el-input v-model="merchatBusinessMaterials.salesScenesType" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="门店名称"
                            prop="bizStoreName">
                        <el-input v-model="merchatBusinessMaterials.bizStoreName" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="门店省市编码 "
                            prop="bizAddressCode">
                        <el-input v-model="merchatBusinessMaterials.bizAddressCode" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="门店地址"
                            prop="bizStoreAddress">
                        <el-input v-model="merchatBusinessMaterials.bizStoreAddress" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="门店门头照片"
                            prop="storeEntrancePic">
                        <el-input v-model="merchatBusinessMaterials.storeEntrancePic" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="门店门头照片"
                            prop="storeEntrancePicMediaId">
                        <el-input v-model="merchatBusinessMaterials.storeEntrancePicMediaId" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="店内环境照片"
                            prop="indoorPic">
                        <el-input v-model="merchatBusinessMaterials.indoorPic" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="店内环境照片"
                            prop="indoorPicMediaId">
                        <el-input v-model="merchatBusinessMaterials.indoorPicMediaId" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="线下场所对应的商家APPID "
                            prop="bizSubAppid">
                        <el-input v-model="merchatBusinessMaterials.bizSubAppid" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="服务商公众号APPID "
                            prop="mpAppid">
                        <el-input v-model="merchatBusinessMaterials.mpAppid" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="商家公众号APPID"
                            prop="mpSubAppid">
                        <el-input v-model="merchatBusinessMaterials.mpSubAppid" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="公众号页面截图 "
                            prop="mpPics">
                        <el-input v-model="merchatBusinessMaterials.mpPics" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="公众号页面截图 "
                            prop="mpPicsMediaId">
                        <el-input v-model="merchatBusinessMaterials.mpPicsMediaId" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="服务商小程序APPID "
                            prop="miniProgramAppid">
                        <el-input v-model="merchatBusinessMaterials.miniProgramAppid" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="服务商小程序APPID"
                            prop="miniProgramSubAppid">
                        <el-input v-model="merchatBusinessMaterials.miniProgramSubAppid" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="服务商小程序页面截图 "
                            prop="miniProgramPics">
                        <el-input v-model="merchatBusinessMaterials.miniProgramPics" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="服务商小程序页面截图 "
                            prop="miniProgramPicsMediaId">
                        <el-input v-model="merchatBusinessMaterials.miniProgramPicsMediaId" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="服务商应用APP的APPID "
                            prop="appAppid">
                        <el-input v-model="merchatBusinessMaterials.appAppid" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="服务商应用APP的APPID"
                            prop="appSubAppid">
                        <el-input v-model="merchatBusinessMaterials.appSubAppid" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="服务商应用APP的页面截图 "
                            prop="appPics">
                        <el-input v-model="merchatBusinessMaterials.appPics" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="服务商应用APP的页面截图 "
                            prop="appPicsMediaId">
                        <el-input v-model="merchatBusinessMaterials.appPicsMediaId" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="互联网网站域名 "
                            prop="domain">
                        <el-input v-model="merchatBusinessMaterials.domain" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="网站授权函"
                            prop="webAuthorisation">
                        <el-input v-model="merchatBusinessMaterials.webAuthorisation" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="网站授权函"
                            prop="webAuthorisationMediaId">
                        <el-input v-model="merchatBusinessMaterials.webAuthorisationMediaId" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="互联网网站对应的商家APPID "
                            prop="webAppid">
                        <el-input v-model="merchatBusinessMaterials.webAppid" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="商家企业微信CorpID "
                            prop="subCorpId">
                        <el-input v-model="merchatBusinessMaterials.subCorpId" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="企业微信页面截图 "
                            prop="weworkPics">
                        <el-input v-model="merchatBusinessMaterials.weworkPics" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="企业微信页面截图 "
                            prop="weworkPicsMediaId">
                        <el-input v-model="merchatBusinessMaterials.weworkPicsMediaId" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="入驻结算规则ID"
                            prop="settlementId">
                        <el-input v-model="merchatBusinessMaterials.settlementId" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="所属行业 "
                            prop="qualificationType">
                        <el-input v-model="merchatBusinessMaterials.qualificationType" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="特殊资质图片"
                            prop="qualifications">
                        <el-input v-model="merchatBusinessMaterials.qualifications" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="特殊资质图片"
                            prop="qualificationsMediaId">
                        <el-input v-model="merchatBusinessMaterials.qualificationsMediaId" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="优惠费率活动ID "
                            prop="activitiesId">
                        <el-input v-model="merchatBusinessMaterials.activitiesId" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="优惠费率活动值"
                            prop="
activitiesRate">
                        <el-input v-model="merchatBusinessMaterials.
activitiesRate" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="优惠费率活动补充材料 "
                            prop="activitiesAdditions">
                        <el-input v-model="merchatBusinessMaterials.activitiesAdditions" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="优惠费率活动补充材料 "
                            prop="activitiesAdditionsMedia">
                        <el-input v-model="merchatBusinessMaterials.activitiesAdditionsMedia" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="账户类型"
                            prop="bankAccountType">
                        <el-input v-model="merchatBusinessMaterials.bankAccountType" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="开户名称"
                            prop="accountName">
                        <el-input v-model="merchatBusinessMaterials.accountName" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="开户银行"
                            prop="accountBank">
                        <el-input v-model="merchatBusinessMaterials.accountBank" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="开户银行省市编码"
                            prop="bankAddressCode">
                        <el-input v-model="merchatBusinessMaterials.bankAddressCode" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="开户银行联行号"
                            prop="bankBranchId">
                        <el-input v-model="merchatBusinessMaterials.bankBranchId" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="开户银行全称（含支行) "
                            prop="bankName">
                        <el-input v-model="merchatBusinessMaterials.bankName" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="银行账号"
                            prop="
accountNumber">
                        <el-input v-model="merchatBusinessMaterials.
accountNumber" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="法人开户承诺函"
                            prop="legalPersonCommitment">
                        <el-input v-model="merchatBusinessMaterials.legalPersonCommitment" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="法人开户承诺函"
                            prop="legalPersonCommitmentMediaId">
                        <el-input v-model="merchatBusinessMaterials.legalPersonCommitmentMediaId" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="法人开户意愿视频"
                            prop="legalPersonVideo">
                        <el-input v-model="merchatBusinessMaterials.legalPersonVideo" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="法人开户意愿视频"
                            prop="legalPersonVideoMediaId">
                        <el-input v-model="merchatBusinessMaterials.legalPersonVideoMediaId" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="补充材料"
                            prop="businessAdditionPics">
                        <el-input v-model="merchatBusinessMaterials.businessAdditionPics" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="补充材料"
                            prop="businessAdditionPicsMediaId">
                        <el-input v-model="merchatBusinessMaterials.businessAdditionPicsMediaId" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="补充说明 "
                            prop="businessAdditionMsg">
                        <el-input v-model="merchatBusinessMaterials.businessAdditionMsg" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="微信支付申请单号 "
                            prop="applymentId">
                        <el-input v-model="merchatBusinessMaterials.applymentId" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="申请单状态"
                            prop="applymentState">
                        <el-input v-model="merchatBusinessMaterials.applymentState" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="特约商户号 "
                            prop="subMchid">
                        <el-input v-model="merchatBusinessMaterials.subMchid" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="超级管理员签约链接"
                            prop="signUrl">
                        <el-input v-model="merchatBusinessMaterials.signUrl" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="申请状态描述 "
                            prop="applymentStateMsg">
                        <el-input v-model="merchatBusinessMaterials.applymentStateMsg" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="字段名 "
                            prop="field">
                        <el-input v-model="merchatBusinessMaterials.field" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="字段名称"
                            prop="fieldName">
                        <el-input v-model="merchatBusinessMaterials.fieldName" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="驳回原因"
                            prop="rejectReason">
                        <el-input v-model="merchatBusinessMaterials.rejectReason" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="创建人"
                            prop="createBy">
                        <el-input v-model="merchatBusinessMaterials.createBy" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="创建时间"
                            prop="createTime">
                        <el-input v-model="merchatBusinessMaterials.createTime" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="更新人"
                            prop="updateBy">
                        <el-input v-model="merchatBusinessMaterials.updateBy" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="更新时间"
                            prop="updateTime">
                        <el-input v-model="merchatBusinessMaterials.updateTime" style="width: 370px;"/>
                    </el-form-item>


            <el-form-item>
                <el-button type="primary" @click="onSubmit('MerchatBusinessMaterialsFrom')">提交</el-button>
                <el-button v-if="!isEdit" @click="resetForm('MerchatBusinessMaterialsFrom')">重置</el-button>
            </el-form-item>
        </el-form>
    </el-card>
</template>
<script>
    import {createMerchatBusinessMaterials, getMerchatBusinessMaterials, updateMerchatBusinessMaterials} from '@/api/merchat/merchatBusinessMaterials'
    import SingleUpload from '@/components/Upload/singleUpload'

    const defaultMerchatBusinessMaterials = {
        name: ''
    };
    export default {
        name: 'MerchatBusinessMaterialsDetail',
        components: {SingleUpload},
        props: {
            isEdit: {
                type: Boolean,
                default: false
            }
        },
        data() {
            return {
            merchatBusinessMaterials:
            Object.assign({},
        defaultMerchatBusinessMaterials),
            rules: {
                name: [
                    {required: true, message: '请输入品牌名称', trigger: 'blur'},
                    {min: 2, max: 140, message: '长度在 2 到 140 个字符', trigger: 'blur'}
                ],
                    logo
            :
                [
                    {required: true, message: '请输入品牌logo', trigger: 'blur'}
                ],
                    sort
            :
                [
                    {type: 'number', message: '排序必须为数字'}
                ],
            }
        }
        },
        created() {
            if (this.isEdit) {
                getMerchatBusinessMaterials(this.$route.query.id).then(response => {
                    this.merchatBusinessMaterials = response.data;
            })
                ;
            } else {
                this.merchatBusinessMaterials = Object.assign({},
            defaultMerchatBusinessMaterials)
                ;
            }
        },
        methods: {
            onSubmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if(valid) {
                        this.$confirm('是否提交数据', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).then(() => {
                            if(this.isEdit
                    )
                        {
                            updateMerchatBusinessMaterials(this.$route.query.id, this.merchatBusinessMaterials).then(response => {
                                if(response.code == 200
                        )
                            {
                                this.$refs[formName].resetFields();
                                this.$message({
                                    message: '修改成功',
                                    type: 'success',
                                    duration: 1000
                                });
                                this.$router.back();
                            }
                        else
                            {
                                this.$message({
                                    message: response.msg,
                                    type: 'error',
                                    duration: 1000
                                });
                            }

                        })
                            ;
                        }
                    else
                        {
                            createMerchatBusinessMaterials(this.merchatBusinessMaterials).then(response => {
                                if(response.code == 200
                        )
                            {
                                this.$refs[formName].resetFields();
                                this.merchatBusinessMaterials = Object.assign({},
                            defaultMerchatBusinessMaterials)
                                ;
                                this.$message({
                                    message: '提交成功',
                                    type: 'success',
                                    duration: 1000
                                });
                                this.$router.back();
                            }
                        else
                            {
                                this.$message({
                                    message: response.msg,
                                    type: 'error',
                                    duration: 1000
                                });
                            }

                        })
                            ;
                        }
                    })
                        ;

                    } else {
                        this.$message({
                            message: '验证失败',
                            type: 'error',
                            duration: 1000
                        });
                return false;
            }
            })
                ;
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
                this.merchatBusinessMaterials = Object.assign({},
            defaultMerchatBusinessMaterials)
                ;
            }
        }
    }
</script>
<style>
</style>


