<template>
  <el-card class="form-container" shadow="never">
    <el-steps :active="active" finish-status="success" align-center>
      <el-step title="业务申请编号"></el-step>
      <el-step title="超级管理员信息"></el-step>
      <el-step title="主体资料"></el-step>
      <el-step title="经营资料"></el-step>
      <el-step title="结算规则"></el-step>
      <el-step title="结算银行账户"></el-step>
      <el-step title="补充资料"></el-step>
    </el-steps>
    <!-- 业务申请编号 -->
    <business-application-number
      v-show="showStatus[0]"
      v-model="merchatBusinessMaterials"
      :is-edit="isEdit"
      @nextStep="nextStep"
    ></business-application-number>
    <!-- 超级管理员信息 -->
    <super-admin-info
      v-show="showStatus[1]"
      v-model="merchatBusinessMaterials"
      :is-edit="isEdit"
      @nextStep="nextStep"
      @prevStep="prevStep"
    ></super-admin-info>
    <!-- 主体资料 -->
    <subject-data
      v-show="showStatus[2]"
      v-model="merchatBusinessMaterials"
      :is-edit="isEdit"
      @nextStep="nextStep"
      @prevStep="prevStep"
    ></subject-data>
    <!-- 经营资料 -->
    <business-data
      v-show="showStatus[3]"
      v-model="merchatBusinessMaterials"
      :is-edit="isEdit"
      @prevStep="prevStep"
      @nextStep="nextStep"
    ></business-data>
    <!-- 结算规则 -->
    <settlement-rules
      v-show="showStatus[4]"
      v-model="merchatBusinessMaterials"
      :is-edit="isEdit"
      @prevStep="prevStep"
      @nextStep="nextStep"
    ></settlement-rules>
    <!-- 结算银行账户 -->
    <settlement-bank-account
      v-show="showStatus[5]"
      v-model="merchatBusinessMaterials"
      :is-edit="isEdit"
      @prevStep="prevStep"
      @nextStep="nextStep"
    ></settlement-bank-account>
    <!-- 补充资料 -->
    <supplementary-information
      v-show="showStatus[6]"
      v-model="merchatBusinessMaterials"
      :is-edit="isEdit"
      @prevStep="prevStep"
      @finishCommit="finishCommit"
    ></supplementary-information>
  </el-card>
</template>
<script>
import BusinessApplicationNumber from "./BusinessApplicationNumber";
import SuperAdminInfo from "./SuperAdminInfo";
import SubjectData from "./SubjectData";
import BusinessData from "./BusinessData";
import SettlementRules from "./SettlementRules"
import SettlementBankAccount from "./SettlementBankAccount"
import SupplementaryInformation from "./SupplementaryInformation"
import { createProduct, getProduct, updateProduct } from "@/api/product";
import { createMerchatBusinessMaterials, getMerchatBusinessMaterials, updateMerchatBusinessMaterials} from "@/api/merchat/merchatBusinessMaterials";
const defaultProductParam = {
  businessCode: "", // 业务申请编号
  // 超级管理员信息
  subjectType: "", //主体类型
  licenseCopy: "", //营业执照
  licenseCopyMediaId: "",
  licenseNumber: "", // 统一信用社会代码
  subjectMerchantName: "", //商户名称
  companyAddress: "", //注册地址
  legalPerson: "", //法人姓名
  periodBegin: "",// 有效期限开始日期
  periodEnd: "", //有效期限结束日期
  organizationCopy: "", //组织机构代码证照片
  organizationCopyMediaId: "", //组织机构代码证照片
  organizationCode: "", //组织机构代码
  orgPeriodBegin: "",// 组织机构代码证有效期开始日期
  orgPeriodEnd: "",// 组织机构代码证有效期结束日期
  certificateLetterCopy: "",// 单位证明函照片
  certificateLetterCopyMediaId: "",// 单位证明函照片
  idDocType: "",// 证件类型
  idCardCopy: "",// 身份证人像面照片
  idCardCopyMediaId: "",// 身份证人像面照片
  idCardNational: "",// 身份证国徽面照片
  idCardNationalMediaId: "",// 身份证国徽面照片
  idCardName: "",// 身份证姓名
  idCardNumber: "",// 身份证号码
  cardPeriodBegin: "",// 身份证有效期开始时间
  cardPeriodEnd: "",// 身份证有效期结束时间
  idDocCopy: "",// 证件照片
  idDocCopyMediaId: "",// 证件照片
  idDocName: "",// 证件姓名
  idDocNumber: "",// 证件号码
  docPeriodBegin: "",// 证件有效期开始时间
  docPeriodEnd: "",// 证件有效期结束时间
  owner: "",// 经营者/法人是否为受益人
  idType: "",// 证件类型
  uboIdCardCopy: "",// 身份证人像面照片
  uboIdCardCopyMediaId: "",// 身份证人像面照片
  uboIdCardNational: "",// 身份证国徽面照片
  uboIdCardNationalMediaId: "",// 身份证国徽面照片
  uboIdDocCopy: "",// 证件照片
  uboIdDocCopyMediaId: "",// 证件照片
  uboName: "",// 证件姓名
  uboIdNumber: "",// 证件号码
  uboIdPeriodBegin: "",// 证件有效期开始时间
  uboIdPeriodEnd: "",// 证件有效期结束时间
  merchantShortname: "",// 商户简称
  servicePhone: "",// 客服电话
  salesScenesType: "",// 经营场景类型
  bizStoreName: "",// 门店名称
  bizAddressCode: "",// 门店省市编码
  bizStoreAddress: "",// 门店地址
  storeEntrancePic: "",// 门店门头照片
  storeEntrancePicMediaId: "",// 门店门头照片
  indoorPic: "",// 店内环境照片
  indoorPicMediaId: "",// 店内环境照片
  bizSubAppid: "",// 线下场所对应的商家APPID
  mpAppid: "",// 服务商公众号APPID
  mpSubAppid: "",// 商家公众号APPID
  mpPics: "",// 公众号页面截图
  mpPicsMediaId: "",// 公众号页面截图
  miniProgramAppid: "",// 服务商小程序APPID
  miniProgramSubAppid: "",// 商家小程序APPID
  miniProgramPics: "",// 服务商小程序页面截图
  miniProgramPicsMediaId: "",// 服务商小程序页面截图
  appAppid: "",// 服务商应用APP的APPID
  appSubAppid: "",// 服务商应用APP的APPID
  appPics: "",// 服务商应用APP的页面截图
  appPicsMediaId: "",// 服务商应用APP的页面截图
  domain: "",// 互联网网站域名
  webAuthorisation: "",// 网站授权函
  webAuthorisationMediaId: "",// 网站授权函
  webAppid: "",// 互联网网站对应的商家APPID
  subCorpId: "",// 商家企业微信CorpID
  weworkPics: "",// 企业微信页面截图
  weworkPicsMediaId: "",// 企业微信页面截图
  settlementId: "",// 入驻结算规则ID
  qualificationType: "",// 所属行业
  qualifications: "",// 特殊资质图片
  qualificationsMediaId: "",// 特殊资质图片
  activitiesId: "",// 优惠费率活动ID
  activitiesRate: "",// 优惠费率活动值
  activitiesAdditions: "",// 优惠费率活动补充材料
  activitiesAdditionsMedia: "",// 优惠费率活动补充材料
  bankAccountType: "",// 账户类型
  accountName: "",// 开户名称
  accountBank: "",// 开户银行
  bankAddressCode: "",// 开户银行省市编码
  bankBranchId: "",// 开户银行联行号
  bankName: "",// 开户银行全称（含支行)
  accountNumber: "",// 银行账号
  legalPersonCommitment: "",// 法人开户承诺函
  legalPersonCommitmentMediaId: "",// 法人开户承诺函
  legalPersonVideo: "",// 法人开户意愿视频
  legalPersonVideoMediaId: "",// 法人开户意愿视频
  businessAdditionPics: "",// 补充材料
  businessAdditionPicsMediaId: "",// 补充材料
  businessAdditionMsg: "",// 补充说明
};
export default {
  name: "ProductDetail",
  components: {
    BusinessApplicationNumber,
    SuperAdminInfo,
    SubjectData,
    BusinessData,
    SettlementRules,
    SettlementBankAccount,
    SupplementaryInformation
  },
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      active: 0,
      merchatBusinessMaterials: Object.assign({}, defaultProductParam),
      showStatus: [true, false, false, false, false, false, false]
    };
  },
  created() {
    if (this.isEdit) {
      getMerchatBusinessMaterials(this.$route.query.id).then(response => {
        this.merchatBusinessMaterials = response.data;
      });
    }
  },
  methods: {
    hideAll() {
      for (let i = 0; i < this.showStatus.length; i++) {
        this.showStatus[i] = false;
      }
    },
    prevStep() {
      if (this.active > 0 && this.active < this.showStatus.length) {
        this.active--;
        this.hideAll();
        this.showStatus[this.active] = true;
      }
    },
    nextStep() {
      if (this.active < this.showStatus.length - 1) {
        this.active++;
        this.hideAll();
        this.showStatus[this.active] = true;
      }
    },
    finishCommit(isEdit) {
      console.log(this.merchatBusinessMaterials);

      this.$confirm("是否提交申请", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        if (isEdit) {
          updateMerchatBusinessMaterials(this.$route.query.id, this.merchatBusinessMaterials).then(
            response => {
              this.$message({
                type: "success",
                message: "提交成功",
                duration: 1000
              });
              this.$router.back();
            }
          );
        } else {
          createMerchatBusinessMaterials(this.merchatBusinessMaterials).then(response => {
            this.$message({
              type: "success",
              message: "提交成功",
              duration: 1000
            });
            this.$router.back();
          });
        }
      });
    }
  }
};
</script>
<style>
.form-container {
  width: 800px;
}
</style>


