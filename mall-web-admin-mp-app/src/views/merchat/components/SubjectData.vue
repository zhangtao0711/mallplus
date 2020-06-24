<template>
  <div style="margin-top: 50px">
    <el-form
      :model="value"
      :rules="rules"
      ref="productAttrForm"
      label-width="150px"
      style="width: 720px"
      size="small"
    >
      <el-form-item label="主体类型" prop="subjectType">
        <el-radio-group v-model="value.subjectType" @change="businessLicense">
          <el-radio label="SUBJECT_TYPE_INDIVIDUAL">个体户</el-radio>
          <el-radio label="SUBJECT_TYPE_ENTERPRISE">企业</el-radio>
          <el-radio label="SUBJECT_TYPE_INSTITUTIONS">党政、机关及事业单位</el-radio>
          <el-radio label="SUBJECT_TYPE_OTHERS">其他组织</el-radio>
        </el-radio-group>
      </el-form-item>

      <!-- 营业执照 -->
      <div v-show="businessLicenseShow">
        <el-form-item
          label="营业执照照片(上传到本地)"
          prop="licenseCopy"
          :rules="[{ required: businessLicenseShow, message: '请上传营业执照', trigger: 'blur' }]"
        >
          <single-upload-tysh
            v-model="licenseCopy"
            style="width: 300px;display: inline-block;margin-left: 10px"
          ></single-upload-tysh>
        </el-form-item>

        <el-form-item
          label="注册号/统一社会信用代码："
          prop="licenseNumber"
          :rules="[
            { required: businessLicenseShow, message: '请输入注册号/统一社会信用代码', trigger: 'blur' },
            { validator: validateBusinessLicense, trigger: 'change' }
		      ]"
        >
          <el-input v-model="value.licenseNumber"></el-input>
        </el-form-item>

        <el-form-item
          label="商户名称："
          prop="subjectMerchantName"
          :rules="[{ required: businessLicenseShow, message: '请输入商户名称', trigger: 'blur' }, { validator: validateName, trigger: 'change' }]"
        >
          <el-input v-model="value.subjectMerchantName"></el-input>
        </el-form-item>

        <el-form-item
          label="个体户经营者/法人姓名："
          prop="subjectLegalPerson"
          :rules="[{ required: businessLicenseShow, message: '请输入个体户经营者/法人姓名', trigger: 'blur' }]"
        >
          <el-input v-model="value.subjectLegalPerson"></el-input>
        </el-form-item>
      </div>

      <!-- 登记证书 -->
      <div v-show="certificateShow">
        <el-form-item
          label="登记证书照片"
          prop="certCopy"
          :rules="[{ required: certificateShow, message: '请上传登记证书照片', trigger: 'change' }]"
        >
          <!-- <single-upload-tysh v-model="value.certCopy" style="width: 300px;display: inline-block;margin-left: 10px"></single-upload-tysh> -->
          <single-upload-tysh
            v-model="certCopy"
            style="width: 300px;display: inline-block;margin-left: 10px"
          ></single-upload-tysh>
        </el-form-item>

        <el-form-item
          label="登记证书类型"
          prop="certType"
          :rules="[{ required: certificateShow, message: '请选择登记证书类型', trigger: 'change' }]"
        >
          <el-radio-group v-model="value.certType">
            <el-radio label="CERTIFICATE_TYPE_2388">事业单位法人证书</el-radio>
            <el-radio label="CERTIFICATE_TYPE_2389">统一社会信用代码证书</el-radio>
            <el-radio label="CERTIFICATE_TYPE_2390">有偿服务许可证（军队医院适用）</el-radio>
            <el-radio label="CERTIFICATE_TYPE_2391">医疗机构执业许可证（军队医院适用）</el-radio>
            <el-radio label="CERTIFICATE_TYPE_2392">企业营业执照（挂靠企业的党组织适用）</el-radio>
            <el-radio label="CERTIFICATE_TYPE_2393">组织机构代码证（政府机关适用）</el-radio>
            <el-radio label="CERTIFICATE_TYPE_2394">社会团体法人登记证书</el-radio>
            <el-radio label="CERTIFICATE_TYPE_2395">民办非企业单位登记证书</el-radio>
            <el-radio label="CERTIFICATE_TYPE_2396">基金会法人登记证书</el-radio>
            <el-radio label="CERTIFICATE_TYPE_2397">慈善组织公开募捐资格证书</el-radio>
            <el-radio label="CERTIFICATE_TYPE_2398">农民专业合作社法人营业执照</el-radio>
            <el-radio label="CERTIFICATE_TYPE_2399">宗教活动场所登记证</el-radio>
            <el-radio label="CERTIFICATE_TYPE_2400">其他证书/批文/证明</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item
          label="证书号"
          prop="certNumber"
          :rules="[{ required: certificateShow, message: '请填写登记证书上的证书编号', trigger: 'change' }]"
        >
          <el-input v-model="value.certNumber"></el-input>
        </el-form-item>

        <el-form-item
          label="商户名称"
          prop="merchantName"
          :rules="[{ required: certificateShow, message: '请填写登记证书上的商户名称', trigger: 'change' }]"
        >
          <el-input v-model="value.merchantName"></el-input>
        </el-form-item>

        <el-form-item
          label="注册地址"
          prop="companyAddress"
          :rules="[{ required: certificateShow, message: '请填写登记证书上的注册地址', trigger: 'change' }]"
        >
          <el-input v-model="value.companyAddress"></el-input>
        </el-form-item>

        <el-form-item
          label="法人姓名"
          prop="legalPerson"
          :rules="[{ required: certificateShow, message: '请填写登记证书上的法定代表人姓名', trigger: 'change' }]"
        >
          <el-input v-model="value.legalPerson"></el-input>
        </el-form-item>

        <el-form-item
          label="有效期限开始日期"
          prop="periodBegin"
          :rules="[{ required: certificateShow, message: '请选择有效期限开始日期', trigger: 'change' }, { validator: validateBeginTime, trigger: 'change' }]"
        >
          <el-date-picker v-model="value.periodBegin" type="date" placeholder="请选择有效期限开始日期"></el-date-picker>
        </el-form-item>

        <el-form-item
          label="有效期限结束日期"
          prop="periodEnd"
          :rules="[{ required: certificateShow, message: '请选择有效期限结束日期', trigger: 'change' }, { validator: validateEndTime, trigger: 'change' }]"
        >
          <el-date-picker v-model="value.periodEnd" type="date" placeholder="请选择有效期限结束日期"></el-date-picker>
        </el-form-item>
      </div>

      <!-- 组织机构 -->
      <div v-show="organization">
        <el-form-item
          label="组织机构代码证照片"
          prop="organizationCopy"
          :rules="[{ required: organization, message: '请上传组织机构代码证', trigger: 'change' }]"
        >
          <!-- <single-upload-tysh v-model="value.organizationCopy" style="width: 300px;display: inline-block;margin-left: 10px"></single-upload-tysh> -->
          <single-upload-tysh
            v-model="organizationCopy"
            style="width: 300px;display: inline-block;margin-left: 10px"
          ></single-upload-tysh>
        </el-form-item>

        <el-form-item
          label="组织机构代码"
          prop="organizationCode"
          :rules="[
						{ required: organization, message: '请填写组织机构代码证上的组织机构代码', trigger: 'change' },
						{ validator: validateOrgPeriodCode, trigger: 'change' }
					]"
        >
          <el-input v-model="value.organizationCode" style="width: 370px;" />
        </el-form-item>

        <el-form-item
          label="组织机构代码证有效期开始日期"
          prop="orgPeriodBegin"
          :rules="[{ required: organization, message: '请选择有效期限开始日期', trigger: 'change' }, { validator: validateOrgPeriodBegin, trigger: 'change' }]"
        >
          <el-date-picker v-model="value.orgPeriodBegin" type="date" placeholder="请选择有效期限开始日期"></el-date-picker>
        </el-form-item>

        <el-form-item
          label="组织机构代码证有效期结束日期"
          prop="orgPeriodEnd"
          :rules="[{ required: organization, message: '请选择有效期限结束日期', trigger: 'change' }, { validator: validateOrgPeriodEnd, trigger: 'change' }]"
        >
          <el-date-picker v-model="value.orgPeriodEnd" type="date" placeholder="请选择有效期限结束日期"></el-date-picker>
        </el-form-item>
      </div>

      <!-- 单位证明函 -->
      <div v-show="letterShow">
        <el-form-item
          label="单位证明函照片"
          prop="certificateLetterCopy"
          :rules="[{ required: letterShow, message: '请上传单位证明函照片' }]"
        >
          <!-- <el-input v-model="value.certificateLetterCopy" style="width: 370px;" /> -->
          <single-upload-tysh
            v-model="certificateLetterCopy"
            style="width: 300px;display: inline-block;margin-left: 10px"
          ></single-upload-tysh>
        </el-form-item>

        <!-- <el-form-item label="单位证明函照片" prop="certificateLetterCopyMediaId" :rules="[{ required: letterShow, message: '请上传单位证明函照片' }]">
					<el-input v-model="value.certificateLetterCopyMediaId" style="width: 370px;" />
        </el-form-item>-->
      </div>

      <el-form-item label="经营者/法人身份证件类型" prop="idDocType">
        <el-radio-group v-model="value.idDocType" @change="idDocTypeChange">
          <el-radio label="IDENTIFICATION_TYPE_IDCARD">中国大陆居民-身份证</el-radio>
          <el-radio label="IDENTIFICATION_TYPE_OVERSEA_PASSPORT">其他国家或地区居民-护照</el-radio>
          <el-radio label="IDENTIFICATION_TYPE_HONGKONG_PASSPORT">中国香港居民-来往内地通行证</el-radio>
          <el-radio label="IDENTIFICATION_TYPE_MACAO_PASSPORT">中国澳门居民-来往内地通行证</el-radio>
          <el-radio label="IDENTIFICATION_TYPE_TAIWAN_PASSPORT">中国台湾居民-来往大陆通行证</el-radio>
        </el-radio-group>
      </el-form-item>

      <!-- 身份证 -->
      <div v-show="idCardShow">
        <el-form-item
          label="身份证人像面照片"
          prop="idCardCopy"
          :rules="[{ required: idCardShow, message: '请上传身份证人像面照片' }]"
        >
          <single-upload-tysh
            v-model="idCardCopy"
            style="width: 300px;display: inline-block;margin-left: 10px"
          ></single-upload-tysh>
        </el-form-item>

        <el-form-item
          label="身份证国徽面照片"
          prop="idCardNational"
          :rules="[{ required: idCardShow, message: '请上传身份证国徽面照片' }]"
        >
          <!-- <single-upload-tysh
            v-model="value.idCardNational"
            style="width: 300px;display: inline-block;margin-left: 10px"
          ></single-upload-tysh>-->
          <single-upload-tysh
            v-model="idCardNational"
            style="width: 300px;display: inline-block;margin-left: 10px"
          ></single-upload-tysh>
        </el-form-item>

        <el-form-item
          label="身份证姓名"
          prop="idCardName"
          :rules="[{ required: idCardShow, message: '请填写身份证姓名', trigger: 'change' }]"
        >
          <el-input v-model="value.idCardName" style="width: 370px;" />
        </el-form-item>

        <el-form-item
          label="身份证号码"
          prop="idCardNumber"
          :rules="[{ required: idCardShow, message: '请填写身份证号码', trigger: 'change' }, { validator: validateIdCard, trigger: 'change' }]"
        >
          <el-input v-model="value.idCardNumber" style="width: 370px;" />
        </el-form-item>

        <el-form-item
          label="身份证有效期开始时间"
          prop="cardPeriodBegin"
          :rules="[{ required: idCardShow, message: '请选择身份证有效期开始时间' }, { validator: validateIdCardBegin, trigger: 'change' }]"
        >
          <el-date-picker v-model="value.cardPeriodBegin" type="date" placeholder="请选择身份证有效期开始时间"></el-date-picker>
        </el-form-item>

        <el-form-item
          label="身份证有效期结束时间 "
          prop="cardPeriodEnd"
          :rules="[{ required: idCardShow, message: '请选择身份证有效期结束时间' }, { validator: validateIdCardEnd, trigger: 'change' }]"
        >
          <el-date-picker v-model="value.cardPeriodEnd" type="date" placeholder="请选择身份证有效期结束时间"></el-date-picker>
        </el-form-item>
      </div>

      <!-- 其他证件类型 -->
      <div v-show="otherCardShow">
        <el-form-item
          label="证件照片"
          prop="idDocCopy"
          :rules="[{ required: otherCardShow, message: '请上传证件照片' }]"
        >
          <single-upload-tysh
            v-model="idDocCopy"
            style="width: 300px;display: inline-block;margin-left: 10px"
          ></single-upload-tysh>
        </el-form-item>

        <el-form-item
          label="证件姓名"
          prop="idDocName"
          :rules="[{ required: otherCardShow, message: '请填写证件姓名', trigger: 'change' }]"
        >
          <el-input v-model="value.idDocName" style="width: 370px;" />
        </el-form-item>

        <el-form-item
          label="证件号码"
          prop="idDocNumber"
          :rules="[{ required: otherCardShow, message: '请填写证件号码', trigger: 'change' }]"
        >
          <!-- { validator: validateIdDoc, trigger: 'change' } -->
          <el-input v-model="value.idDocNumber" style="width: 370px;" />
        </el-form-item>

        <el-form-item
          label="证件有效期开始时间"
          prop="docPeriodBegin"
          :rules="[{ required: otherCardShow, message: '请选择证件有效期开始时间', trigger: 'change' }, { validator: validateIdDocBegin, trigger: 'change' }]"
        >
          <el-date-picker v-model="value.docPeriodBegin" type="date" placeholder="请选择证件有效期开始时间"></el-date-picker>
        </el-form-item>

        <el-form-item
          label="证件有效期结束时间 "
          prop="docPeriodEnd"
          :rules="[{ required: otherCardShow, message: '请选择证件有效期结束时间', trigger: 'change' }, { validator: validateIdDocEnd, trigger: 'change' }]"
        >
          <el-date-picker v-model="value.docPeriodEnd" type="date" placeholder="请选择证件有效期结束时间"></el-date-picker>
        </el-form-item>
      </div>

      <el-form-item label="经营者/法人是否为受益人" prop="owner">
        <el-radio-group v-model="value.owner" @change="chooseOwner">
          <el-radio label="1">是</el-radio>
          <el-radio label="0">否</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item
        label="证件类型"
        prop="idType"
        v-show="isOwner"
        :rules="[{ required: isOwner, message: '请选择受益人证件类型' }]"
      >
        <el-radio-group v-model="value.idType" @change="chooseIdType">
          <el-radio label="IDENTIFICATION_TYPE_IDCARD">中国大陆居民-身份证</el-radio>
          <el-radio label="IDENTIFICATION_TYPE_OVERSEA_PASSPORT">其他国家或地区居民-护照</el-radio>
          <el-radio label="IDENTIFICATION_TYPE_HONGKONG_PASSPORT">中国香港居民-来往内地通行证</el-radio>
          <el-radio label="IDENTIFICATION_TYPE_MACAO_PASSPORT">中国澳门居民-来往内地通行证</el-radio>
          <el-radio label="IDENTIFICATION_TYPE_TAIWAN_PASSPORT">中国台湾居民-来往大陆通行证</el-radio>
        </el-radio-group>
      </el-form-item>

      <!-- 受益人证件类型-身份证 -->
      <div v-show="uboIdCardShow && isOwner">
        <el-form-item
          label="身份证人像面照片"
          prop="uboIdCardCopy"
          :rules="[{ required: uboIdCardShow, message: '请上传身份证人像面照片' }]"
        >
          <single-upload-tysh
            v-model="uboIdCardCopy"
            style="width: 300px;display: inline-block;margin-left: 10px"
          ></single-upload-tysh>
        </el-form-item>

        <el-form-item
          label="身份证国徽面照片"
          prop="uboIdCardNational"
          :rules="[{ required: uboIdCardShow, message: '请上传身份证国徽面照片' }]"
        >
          <single-upload-tysh
            v-model="uboIdCardNational"
            style="width: 300px;display: inline-block;margin-left: 10px"
          ></single-upload-tysh>
        </el-form-item>

        <el-form-item
          label="身份证姓名"
          prop="uboIdCardName"
          :rules="[{ required: uboIdCardShow, message: '请填写身份证姓名' }]"
        >
          <el-input v-model="value.uboIdCardName" style="width: 370px;" />
        </el-form-item>

        <el-form-item
          label="身份证号码"
          prop="uboIdCardNumber"
          :rules="[{ required: uboIdCardShow, message: '请填写身份证号码', trigger: 'change' }, { validator: validateUboIdCard, trigger: 'change' }]"
        >
          <el-input v-model="value.uboIdCardNumber" style="width: 370px;" />
        </el-form-item>

        <el-form-item
          label="身份证有效期开始时间"
          prop="uboCardPeriodBegin"
          :rules="[{ required: uboIdCardShow, message: '请选择证件有效期开始时间', trigger: 'change' }, { validator: validateuboCardBegin, trigger: 'change' }]"
        >
          <el-date-picker v-model="value.uboCardPeriodBegin" type="date" placeholder="请选择证件有效期开始时间"></el-date-picker>
        </el-form-item>

        <el-form-item
          label="身份证有效期结束时间 "
          prop="uboCardPeriodEnd"
          :rules="[{ required: uboIdCardShow, message: '请选择证件有效期结束时间', trigger: 'change' }, { validator: validateuboCardEnd, trigger: 'change' }]"
        >
          <el-date-picker v-model="value.uboCardPeriodEnd" type="date" placeholder="请选择证件有效期结束时间"></el-date-picker>
        </el-form-item>
      </div>

      <!-- 受益人证件类型-其他 -->
      <div v-show="uboIdDocShow && isOwner">
        <el-form-item
          label="证件照片"
          prop="uboIdDocCopy"
          :rules="[{ required: uboIdDocShow, message: '请上传证件照片' }]"
        >
          <single-upload-tysh
            v-model="uboIdDocCopy"
            style="width: 300px;display: inline-block;margin-left: 10px"
          ></single-upload-tysh>
        </el-form-item>

        <el-form-item
          label="证件姓名"
          prop="uboIdDocName"
          :rules="[{ required: uboIdDocShow, message: '请填写证件姓名', trigger: 'change' }]"
        >
          <el-input v-model="value.uboIdDocName" style="width: 370px;" />
        </el-form-item>

        <el-form-item
          label="证件号码"
          prop="uboIdDocNumber"
          :rules="[{ required: uboIdDocShow, message: '请填写证件号码', trigger: 'change' }]"
        >
          <el-input v-model="value.uboIdDocNumber" style="width: 370px;" />
        </el-form-item>

        <el-form-item
          label="证件有效期开始时间"
          prop="uboDocPeriodBegin"
          :rules="[{ required: uboIdDocShow, message: '请选择证件有效期开始时间', trigger: 'change' }, { validator: validateuboDocBegin, trigger: 'change' }]"
        >
          <el-date-picker v-model="value.uboDocPeriodBegin" type="date" placeholder="请选择证件有效期开始时间"></el-date-picker>
        </el-form-item>

        <el-form-item
          label="证件有效期结束时间 "
          prop="uboDocPeriodEnd"
          :rules="[{ required: uboIdDocShow, message: '请选择证件有效期结束时间', trigger: 'change' }, { validator: validateuboDocEnd, trigger: 'change' }]"
        >
          <el-date-picker v-model="value.uboDocPeriodEnd" type="date" placeholder="请选择证件有效期开始时间"></el-date-picker>
        </el-form-item>
      </div>

      <el-form-item style="text-align: center">
        <el-button size="medium" @click="handlePrev">上一步，填写超级管理员信息</el-button>
        <el-button type="primary" size="medium" @click="handleNext">下一步，填写经营资料</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import SingleUpload from "@/components/Upload/singleUpload";
import Tinymce from "@/components/Tinymce";
import SingleUploadTysh from "@/components/Upload/singleUploadTysh";

export default {
  name: "ProductAttrDetail",
  components: {
    SingleUpload,
    Tinymce,
    SingleUploadTysh
  },
  props: {
    value: Object,
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      licenseCopy: {
        url: "",
        MediaID: ""
      },
      certCopy: {
        url: "",
        MediaID: ""
      },
      organizationCopy: {
        url: "",
        MediaID: ""
      },
      certificateLetterCopy: {
        url: "",
        MediaID: ""
      },
      idCardCopy: {
        url: "",
        MediaID: ""
      },
      idCardNational: {
        url: "",
        MediaID: ""
      },
      idDocCopy: {
        url: "",
        MediaID: ""
      },
      uboIdCardCopy: {
        url: "",
        MediaID: ""
      },
      uboIdCardNational: {
        url: "",
        MediaID: ""
      },
      uboIdDocCopy: {
        url: "",
        MediaID: ""
      },
      organization: false,
      businessLicenseShow: false,
      certificateShow: false,
      letterShow: false,
      idCardShow: false,
      otherCardShow: false,
      isOwner: false,
      uboIdCardShow: true,
      uboIdDocShow: false,
      //编辑模式时是否初始化成功
      hasEditCreated: false,
      rules: {
        // subjectType: [
        //   {
        //     required: true,
        //     message: "请选择主体类型",
        //     trigger: "change"
        //   }
        // ],
        // idDocType: [
        //   {
        //     required: true,
        //     message: "请选择经营者/法人身份证件类型",
        //     trigger: "change"
        //   }
        // ],
        // owner: [
        //   {
        //     required: true,
        //     message: "请选择经营者/法人是否为受益人",
        //     trigger: "change"
        //   }
        // ]
      }
    };
  },
  created() {
    // this.getProductAttrCateList();
  },
  watch: {
    // productId: function(newValue) {
    //   if (!this.isEdit) return;
    //   if (this.hasEditCreated) return;
    //   if (newValue === undefined || newValue == null || newValue === 0) return;
    // }
  },
  methods: {
    validateBeginTime(rule, value, callback) {
      let year = value.getFullYear();
      let month = value.getMonth() + 1;
      let day = value.getDate();
      month = month > 9 ? month : "0" + month;
      day = day > 9 ? day : "0" + day;
      var chooseDay = year + "-" + month + "-" + day;
      if (this.certificateShow === true) {
        const date = new Date();
        let yearN = date.getFullYear();
        let monthN = date.getMonth() + 1;
        let dayN = date.getDate();
        monthN = monthN > 9 ? monthN : "0" + monthN;
        dayN = dayN > 9 ? dayN : "0" + dayN;
        var nowDay = yearN + "-" + monthN + "-" + dayN;

        var cDate = chooseDay.replace("-", "").replace("-", "");
        var nDate = nowDay.replace("-", "").replace("-", "");

        if (Number(cDate) >= Number(nDate)) {
          callback(new Error("开始日期需大于当前日期"));
        } else {
          callback();
        }
      } else {
        callback();
      }
    },
    validateEndTime(rule, value, callback) {
      let year = value.getFullYear();
      let month = value.getMonth() + 1;
      let day = value.getDate();
      month = month > 9 ? month : "0" + month;
      day = day > 9 ? day : "0" + day;
      var chooseDay = year + "-" + month + "-" + day;
      if (this.certificateShow === true) {
        let yearN = this.value.periodBegin.getFullYear();
        let monthN = this.value.periodBegin.getMonth() + 1;
        let dayN = this.value.periodBegin.getDate();
        monthN = monthN > 9 ? monthN : "0" + monthN;
        dayN = dayN > 9 ? dayN : "0" + dayN;
        var nowDay = yearN + "-" + monthN + "-" + dayN;

        var cDate = chooseDay.replace("-", "").replace("-", "");
        var nDate = nowDay.replace("-", "").replace("-", "");

        var startDate = Date.parse(chooseDay.replace("/-/g", "/"));
        var endDate = Date.parse(nowDay.replace("/-/g", "/"));
        //   var diffDate=(endDate-startDate)+1*24*60*60*1000;
        var days =
          (Number(startDate) - Number(endDate)) / (1 * 24 * 60 * 60 * 1000);

        if (Number(cDate) <= Number(nDate)) {
          callback(new Error("结束日期大于开始日期"));
        } else if (days < 60) {
          callback(new Error("有效期必须大于60天"));
        } else {
          callback();
        }
      } else {
        callback();
      }
    },
    validateOrgPeriodBegin(rule, value, callback) {
      let year = value.getFullYear();
      let month = value.getMonth() + 1;
      let day = value.getDate();
      month = month > 9 ? month : "0" + month;
      day = day > 9 ? day : "0" + day;
      var chooseDay = year + "/" + month + "/" + day;
      if (this.organization === true && this.value.orgPeriodEnd) {
        let yearN = this.value.orgPeriodEnd.getFullYear();
        let monthN = this.value.orgPeriodEnd.getMonth() + 1;
        let dayN = this.value.orgPeriodEnd.getDate();
        monthN = monthN > 9 ? monthN : "0" + monthN;
        dayN = dayN > 9 ? dayN : "0" + dayN;
        var nowDay = yearN + "/" + monthN + "/" + dayN;

        var startDate = Date.parse(chooseDay);
        var endDate = Date.parse(nowDay);
        //   var diffDate=(endDate-startDate)+1*24*60*60*1000;
        var days =
          (Number(startDate) - Number(endDate)) / (1 * 24 * 60 * 60 * 1000);
        if (days < 60) {
          callback(new Error("有效期必须大于60天"));
        } else {
          callback();
        }
      } else {
        callback();
      }
    },
    validateOrgPeriodEnd(rule, value, callback) {
      let year = value.getFullYear();
      let month = value.getMonth() + 1;
      let day = value.getDate();
      month = month > 9 ? month : "0" + month;
      day = day > 9 ? day : "0" + day;
      var chooseDay = year + "-" + month + "-" + day;
      if (this.organization === true) {
        let yearN = this.value.periodBegin.getFullYear();
        let monthN = this.value.periodBegin.getMonth() + 1;
        let dayN = this.value.periodBegin.getDate();
        monthN = monthN > 9 ? monthN : "0" + monthN;
        dayN = dayN > 9 ? dayN : "0" + dayN;
        var nowDay = yearN + "-" + monthN + "-" + dayN;

        var cDate = chooseDay.replace("-", "").replace("-", "");
        var nDate = nowDay.replace("-", "").replace("-", "");

        var startDate = Date.parse(chooseDay.replace("/-/g", "/"));
        var endDate = Date.parse(nowDay.replace("/-/g", "/"));
        //   var diffDate=(endDate-startDate)+1*24*60*60*1000;
        var days =
          (Number(startDate) - Number(endDate)) / (1 * 24 * 60 * 60 * 1000);

        if (Number(cDate) <= Number(nDate)) {
          callback(new Error("结束日期大于开始日期"));
        } else if (days < 60) {
          callback(new Error("有效期必须大于60天"));
        } else {
          callback();
        }
      } else {
        callback();
      }
    },
    validateBusinessLicense(rule, value, callback) {
      if (this.businessLicenseShow === true) {
        if (
          new RegExp(
            /(^(?:(?![IOZSV])[\dA-Z]){2}\d{6}(?:(?![IOZSV])[\dA-Z]){10}$)|(^\d{15}$)/
          ).test(value)
        ) {
          callback();
        } else {
          callback(new Error("请输入正确格式的注册号/统一社会信用代码"));
        }
      } else {
        callback();
      }
    },
    validateName(rule, value, callback) {
      if (this.businessLicenseShow === true) {
        console.log(value.length);
        if (value.length < 2 || value.length > 110) {
          callback(new Error("商户名称长度需在 2 到 110 个字符"));
        } else {
          callback();
        }
      } else {
        callback();
      }
    },
    validateOrgPeriodCode(rule, value, callback) {
      if (this.organization === true) {
        if (
          new RegExp(/[0-9A-HJ-NPQRTUWXY]{8}-[0-9A-HJ-NPQRTUWXY]/).test(value)
        ) {
          callback();
        } else {
          callback(new Error("请输入正确格式的组织机构代码"));
        }
      } else {
        callback();
      }
    },
    validateIdCard(rule, value, callback) {
      if (this.idCardShow === true) {
        if (
          new RegExp(
            /(^[1-9]\d{5}(19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}[0-9Xx]$)/
          ).test(value)
        ) {
          callback();
        } else {
          callback(new Error("请输入正确格式的身份证号"));
        }
      } else {
        callback();
      }
    },
    validateIdCardBegin(rule, value, callback) {
      let year = value.getFullYear();
      let month = value.getMonth() + 1;
      let day = value.getDate();
      month = month > 9 ? month : "0" + month;
      day = day > 9 ? day : "0" + day;
      var chooseDay = year + "-" + month + "-" + day;
      if (this.idCardShow === true && this.value.cardPeriodEnd) {
        let yearN = this.value.cardPeriodEnd.getFullYear();
        let monthN = this.value.cardPeriodEnd.getMonth() + 1;
        let dayN = this.value.cardPeriodEnd.getDate();
        monthN = monthN > 9 ? monthN : "0" + monthN;
        dayN = dayN > 9 ? dayN : "0" + dayN;
        var endDay = yearN + "-" + monthN + "-" + dayN;

        var cDate = chooseDay.replace("-", "").replace("-", "");
        var eDate = endDay.replace("-", "").replace("-", "");

        if (Number(cDate) >= Number(eDate)) {
          callback(new Error("开始日期需早于结束日期"));
        } else {
          callback();
        }
      } else {
        callback();
      }
    },
    validateIdCardEnd(rule, value, callback) {
      let year = value.getFullYear();
      let month = value.getMonth() + 1;
      let day = value.getDate();
      month = month > 9 ? month : "0" + month;
      day = day > 9 ? day : "0" + day;
      var chooseDay = year + "-" + month + "-" + day;
      if (this.idCardShow === true && this.value.cardPeriodBegin) {
        let yearN = this.value.cardPeriodBegin.getFullYear();
        let monthN = this.value.cardPeriodBegin.getMonth() + 1;
        let dayN = this.value.cardPeriodBegin.getDate();
        monthN = monthN > 9 ? monthN : "0" + monthN;
        dayN = dayN > 9 ? dayN : "0" + dayN;
        var nowDay = yearN + "-" + monthN + "-" + dayN;

        var cDate = chooseDay.replace("-", "").replace("-", "");
        var nDate = nowDay.replace("-", "").replace("-", "");

        var startDate = Date.parse(chooseDay.replace("/-/g", "/"));
        var endDate = Date.parse(nowDay.replace("/-/g", "/"));
        //   var diffDate=(endDate-startDate)+1*24*60*60*1000;
        var days =
          (Number(startDate) - Number(endDate)) / (1 * 24 * 60 * 60 * 1000);

        if (Number(cDate) <= Number(nDate)) {
          callback(new Error("结束日期需大于开始日期"));
        } else if (days < 60) {
          callback(new Error("有效期必须大于60天"));
        } else {
          callback();
        }
      } else {
        callback();
      }
    },
    validateIdDoc(rule, value, callback) {
      if (this.otherCardShow === true) {
        if (
          new RegExp(
            /(^[1-9]\d{5}(19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}[0-9Xx]$)/
          ).test(value)
        ) {
          callback();
        } else {
          callback(new Error("请输入正确格式的证件号码"));
        }
      } else {
        callback();
      }
    },
    validateIdDocBegin(rule, value, callback) {
      let year = value.getFullYear();
      let month = value.getMonth() + 1;
      let day = value.getDate();
      month = month > 9 ? month : "0" + month;
      day = day > 9 ? day : "0" + day;
      var chooseDay = year + "-" + month + "-" + day;
      if (this.otherCardShow === true && this.value.docPeriodEnd) {
        let yearN = this.value.docPeriodEnd.getFullYear();
        let monthN = this.value.docPeriodEnd.getMonth() + 1;
        let dayN = this.value.docPeriodEnd.getDate();
        monthN = monthN > 9 ? monthN : "0" + monthN;
        dayN = dayN > 9 ? dayN : "0" + dayN;
        var endDay = yearN + "-" + monthN + "-" + dayN;

        var cDate = chooseDay.replace("-", "").replace("-", "");
        var eDate = endDay.replace("-", "").replace("-", "");

        if (Number(cDate) >= Number(eDate)) {
          callback(new Error("开始日期需早于结束日期"));
        } else {
          callback();
        }
      } else {
        callback();
      }
    },
    validateIdDocEnd(rule, value, callback) {
      let year = value.getFullYear();
      let month = value.getMonth() + 1;
      let day = value.getDate();
      month = month > 9 ? month : "0" + month;
      day = day > 9 ? day : "0" + day;
      var chooseDay = year + "-" + month + "-" + day;
      if (this.otherCardShow === true && this.value.docPeriodBegin) {
        let yearN = this.value.docPeriodBegin.getFullYear();
        let monthN = this.value.docPeriodBegin.getMonth() + 1;
        let dayN = this.value.docPeriodBegin.getDate();
        monthN = monthN > 9 ? monthN : "0" + monthN;
        dayN = dayN > 9 ? dayN : "0" + dayN;
        var nowDay = yearN + "-" + monthN + "-" + dayN;

        var cDate = chooseDay.replace("-", "").replace("-", "");
        var nDate = nowDay.replace("-", "").replace("-", "");

        var startDate = Date.parse(chooseDay.replace("/-/g", "/"));
        var endDate = Date.parse(nowDay.replace("/-/g", "/"));
        //   var diffDate=(endDate-startDate)+1*24*60*60*1000;
        var days =
          (Number(startDate) - Number(endDate)) / (1 * 24 * 60 * 60 * 1000);

        if (Number(cDate) <= Number(nDate)) {
          callback(new Error("结束日期需大于开始日期"));
        } else if (days < 60) {
          callback(new Error("有效期必须大于60天"));
        } else {
          callback();
        }
      } else {
        callback();
      }
    },
    businessLicense(e) {
      // 个体户
      if (e == "SUBJECT_TYPE_INDIVIDUAL") {
        this.businessLicenseShow = true;
        this.certificateShow = false;
        this.organization = false;
        this.letterShow = false;
      }
      //   企业
      if (e == "SUBJECT_TYPE_ENTERPRISE") {
        this.organization = true;
        this.businessLicenseShow = true;
        this.certificateShow = false;
        this.letterShow = false;
      }
      //   党政、机关及事业单位
      if (e == "SUBJECT_TYPE_INSTITUTIONS") {
        this.certificateShow = true;
        this.businessLicenseShow = false;
        this.organization = true;
        this.letterShow = true;
      }
      //   其他组织
      if (e == "SUBJECT_TYPE_OTHERS") {
        this.certificateShow = true;
        this.businessLicenseShow = false;
        this.organization = true;
        this.letterShow = false;
      }
    },
    idDocTypeChange(e) {
      if (e == "IDENTIFICATION_TYPE_IDCARD") {
        this.idCardShow = true;
        this.otherCardShow = false;
      } else {
        this.idCardShow = false;
        this.otherCardShow = true;
      }
    },
    chooseIdType(e) {
      if (e == "IDENTIFICATION_TYPE_IDCARD") {
        this.uboIdCardShow = true;
        this.uboIdDocShow = false;
      } else {
        this.uboIdCardShow = false;
        this.uboIdDocShow = true;
      }
    },
    chooseOwner(e) {
      if (e == '1') {
        this.isOwner = false;
      } else {
        this.isOwner = true;
      }
    },
    handlePrev() {
      this.$emit("prevStep");
    },
    handleNext(formName) {
      //   this.$refs[formName].validate(valid => {
      //     if (valid) {
      this.value.licenseCopyMediaId = this.licenseCopy.MediaID;
      this.value.licenseCopy = this.licenseCopy.url;

      this.value.certCopyMediaId = this.certCopy.MediaID;
      this.value.certCopy = this.certCopy.url;

      this.value.organizationCopyMediaId = this.organizationCopy.MediaID;
      this.value.organizationCopy = this.organizationCopy.url;

      this.value.certificateLetterCopyMediaId = this.certificateLetterCopy.MediaID;
      this.value.certificateLetterCopy = this.certificateLetterCopy.url;

      this.value.idCardCopy = this.idCardCopy.url;
      this.value.idCardCopyMediaId = this.idCardCopy.MediaID;

      this.value.idCardNational = this.idCardNational.url;
      this.value.idCardNationalMediaId = this.idCardNational.MediaID;

      this.value.idDocCopy = this.idDocCopy.url;
      this.value.idDocCopyMediaId = this.idDocCopy.MediaID;

      this.value.uboIdCardCopy = this.uboIdCardCopy.url;
      this.value.uboIdCardCopyMediaId = this.uboIdCardCopy.MediaID;

      this.value.uboIdCardNational = this.uboIdCardNational.url;
      this.value.uboIdCardNationalMediaId = this.uboIdCardNational.MediaID;

      this.value.uboIdDocCopy = this.uboIdDocCopy.url;
      this.value.uboIdDocCopyMediaId = this.uboIdDocCopy.MediaID;

      this.$emit("nextStep");
      //     } else {
      //       this.$message({
      //         message: "验证失败",
      //         type: "error",
      //         duration: 1000
      //       });
      //       return false;
      //     }
      //   });
    },
    validateUboIdCard(rule, value, callback) {
      if (this.uboIdCardShow === true && this.isOwner === true) {
        if (
          new RegExp(
            /(^[1-9]\d{5}(19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}[0-9Xx]$)/
          ).test(value)
        ) {
          callback();
        } else {
          callback(new Error("请输入正确格式的身份证号"));
        }
      } else {
        callback();
      }
    },
    validateuboCardBegin(rule, value, callback) {
      let year = value.getFullYear();
      let month = value.getMonth() + 1;
      let day = value.getDate();
      month = month > 9 ? month : "0" + month;
      day = day > 9 ? day : "0" + day;
      var chooseDay = year + "-" + month + "-" + day;
      if (
        this.uboIdCardShow === true &&
        this.isOwner === true &&
        this.value.uboCardPeriodEnd
      ) {
        let yearN = this.value.uboCardPeriodEnd.getFullYear();
        let monthN = this.value.uboCardPeriodEnd.getMonth() + 1;
        let dayN = this.value.uboCardPeriodEnd.getDate();
        monthN = monthN > 9 ? monthN : "0" + monthN;
        dayN = dayN > 9 ? dayN : "0" + dayN;
        var endDay = yearN + "-" + monthN + "-" + dayN;

        var cDate = chooseDay.replace("-", "").replace("-", "");
        var eDate = endDay.replace("-", "").replace("-", "");

        if (Number(cDate) >= Number(eDate)) {
          callback(new Error("开始日期需早于结束日期"));
        } else {
          callback();
        }
      } else {
        callback();
      }
    },
    validateuboCardEnd(rule, value, callback) {
      let year = value.getFullYear();
      let month = value.getMonth() + 1;
      let day = value.getDate();
      month = month > 9 ? month : "0" + month;
      day = day > 9 ? day : "0" + day;
      var chooseDay = year + "-" + month + "-" + day;
      if (
        this.uboIdCardShow === true &&
        this.isOwner === true &&
        this.value.uboCardPeriodBegin
      ) {
        let yearN = this.value.uboCardPeriodBegin.getFullYear();
        let monthN = this.value.uboCardPeriodBegin.getMonth() + 1;
        let dayN = this.value.uboCardPeriodBegin.getDate();
        monthN = monthN > 9 ? monthN : "0" + monthN;
        dayN = dayN > 9 ? dayN : "0" + dayN;
        var nowDay = yearN + "-" + monthN + "-" + dayN;

        var cDate = chooseDay.replace("-", "").replace("-", "");
        var nDate = nowDay.replace("-", "").replace("-", "");

        var startDate = Date.parse(chooseDay.replace("/-/g", "/"));
        var endDate = Date.parse(nowDay.replace("/-/g", "/"));
        //   var diffDate=(endDate-startDate)+1*24*60*60*1000;
        var days =
          (Number(startDate) - Number(endDate)) / (1 * 24 * 60 * 60 * 1000);

        if (Number(cDate) <= Number(nDate)) {
          callback(new Error("结束日期需大于开始日期"));
        } else if (days < 60) {
          callback(new Error("有效期必须大于60天"));
        } else {
          callback();
        }
      } else {
        callback();
      }
    },
    validateuboDocBegin(rule, value, callback) {
      let year = value.getFullYear();
      let month = value.getMonth() + 1;
      let day = value.getDate();
      month = month > 9 ? month : "0" + month;
      day = day > 9 ? day : "0" + day;
      var chooseDay = year + "-" + month + "-" + day;
      if (
        this.uboIdDocShow === true &&
        this.isOwner === true &&
        this.value.uboDocPeriodEnd
      ) {
        let yearN = this.value.uboDocPeriodEnd.getFullYear();
        let monthN = this.value.uboDocPeriodEnd.getMonth() + 1;
        let dayN = this.value.uboDocPeriodEnd.getDate();
        monthN = monthN > 9 ? monthN : "0" + monthN;
        dayN = dayN > 9 ? dayN : "0" + dayN;
        var endDay = yearN + "-" + monthN + "-" + dayN;

        var cDate = chooseDay.replace("-", "").replace("-", "");
        var eDate = endDay.replace("-", "").replace("-", "");

        if (Number(cDate) >= Number(eDate)) {
          callback(new Error("开始日期需早于结束日期"));
        } else {
          callback();
        }
      } else {
        callback();
      }
    },
    validateuboDocEnd(rule, value, callback) {
      let year = value.getFullYear();
      let month = value.getMonth() + 1;
      let day = value.getDate();
      month = month > 9 ? month : "0" + month;
      day = day > 9 ? day : "0" + day;
      var chooseDay = year + "-" + month + "-" + day;
      if (
        this.uboIdDocShow === true &&
        this.isOwner === true &&
        this.value.uboDocPeriodBegin
      ) {
        let yearN = this.value.uboDocPeriodBegin.getFullYear();
        let monthN = this.value.uboDocPeriodBegin.getMonth() + 1;
        let dayN = this.value.uboDocPeriodBegin.getDate();
        monthN = monthN > 9 ? monthN : "0" + monthN;
        dayN = dayN > 9 ? dayN : "0" + dayN;
        var nowDay = yearN + "-" + monthN + "-" + dayN;

        var cDate = chooseDay.replace("-", "").replace("-", "");
        var nDate = nowDay.replace("-", "").replace("-", "");

        var startDate = Date.parse(chooseDay.replace("/-/g", "/"));
        var endDate = Date.parse(nowDay.replace("/-/g", "/"));
        //   var diffDate=(endDate-startDate)+1*24*60*60*1000;
        var days =
          (Number(startDate) - Number(endDate)) / (1 * 24 * 60 * 60 * 1000);

        if (Number(cDate) <= Number(nDate)) {
          callback(new Error("结束日期需大于开始日期"));
        } else if (days < 60) {
          callback(new Error("有效期必须大于60天"));
        } else {
          callback();
        }
      } else {
        callback();
      }
    }
  }
};
</script>

<style scoped>
.littleMarginLeft {
  margin-left: 10px;
}

.littleMarginTop {
  margin-top: 10px;
}

.paramInput {
  width: 250px;
}

.paramInputLabel {
  display: inline-block;
  width: 100px;
  text-align: right;
  padding-right: 10px;
}

.cardBg {
  background: #f8f9fc;
}
</style>
