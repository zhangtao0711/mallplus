<template>
  <div style="margin-top: 50px">
    <el-form
      :model="value"
      :rules="rules"
      ref="productRelationForm"
      label-width="120px"
      style="width: 680px"
      size="small"
    >
      <el-form-item label="商户简称" prop="merchantShortname">
        <el-input v-model="value.merchantShortname" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="客服电话 " prop="servicePhone">
        <el-input v-model="value.servicePhone" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="经营场景类型" prop="salesScenesType">
        <el-checkbox-group v-model="salesScenesType" @change="salesScenesTypeChange">
          <el-checkbox label="SALES_SCENES_STORE" name="salesScenesType">线下门店</el-checkbox>
          <el-checkbox label="SALES_SCENES_MP" name="salesScenesType">公众号</el-checkbox>
          <el-checkbox label="SALES_SCENES_MINI_PROGRAM" name="salesScenesType">小程序</el-checkbox>
          <el-checkbox label="SALES_SCENES_WEB" name="salesScenesType">互联网</el-checkbox>
          <el-checkbox label="SALES_SCENES_APP" name="salesScenesType">APP</el-checkbox>
          <el-checkbox label="SALES_SCENES_WEWORK" name="salesScenesType">企业微信</el-checkbox>
        </el-checkbox-group>
      </el-form-item>

      <el-form-item
        label="线下门店名称"
        prop="bizStoreName"
        v-show="storeShow"
        :rules="[
      { required: storeShow, message: '请填写线下门店名称', trigger: 'blur' },
    ]"
      >
        <el-input v-model="value.bizStoreName" style="width: 370px;" />
      </el-form-item>

      <el-form-item
        label="线下门店省市编码 "
        prop="bizAddressCode"
        v-show="storeShow"
        :rules="[
      { required: storeShow, message: '请填写线下门店省市编码', trigger: 'blur' },
    ]"
      >
        <el-input v-model="value.bizAddressCode" style="width: 370px;" />
      </el-form-item>

      <el-form-item
        label="线下门店地址"
        prop="bizStoreAddress"
        v-show="storeShow"
        :rules="[
      { required: storeShow, message: '请填写线下门店地址', trigger: 'blur' },
    ]"
      >
        <el-input v-model="value.bizStoreAddress" style="width: 370px;" />
      </el-form-item>

      <el-form-item
        label="线下门店门头照片"
        prop="storeEntrancePic"
        v-show="storeShow"
        :rules="[
      { required: storeShow, message: '请上传线下门店门头照片', trigger: 'blur' },
    ]"
      >
        <el-input v-model="value.storeEntrancePic" style="width: 370px;" />
      </el-form-item>

      <el-form-item
        label="线下门店门头照片"
        prop="storeEntrancePicMediaId"
        v-show="storeShow"
        :rules="[
      { required: storeShow, message: '请上传线下门店门头照片', trigger: 'blur' },
    ]"
      >
        <el-input v-model="value.storeEntrancePicMediaId" style="width: 370px;" />
      </el-form-item>

      <el-form-item
        label="店内环境照片"
        prop="indoorPic"
        v-show="storeShow"
        :rules="[
      { required: storeShow, message: '请上传店内环境照片', trigger: 'blur' },
    ]"
      >
        <el-input v-model="value.indoorPic" style="width: 370px;" />
      </el-form-item>

      <el-form-item
        label="店内环境照片"
        prop="indoorPicMediaId"
        v-show="storeShow"
        :rules="[
      { required: storeShow, message: '请上传店内环境照片', trigger: 'blur' },
    ]"
      >
        <el-input v-model="value.indoorPicMediaId" style="width: 370px;" />
      </el-form-item>

      <el-form-item
        label="线下场所对应的商家APPID "
        prop="bizSubAppid"
        v-show="storeShow"
        :rules="[
      { required: storeShow, message: '请填写线下场所对应的商家APPID', trigger: 'blur' },
    ]"
      >
        <el-input v-model="value.bizSubAppid" style="width: 370px;" />
      </el-form-item>

      <el-form-item
        label="服务商公众号/商家公众号"
        prop="choose"
        v-show="MPShow"
        :rules="[
      { required: MPShow, message: '服务商公众号/商家公众号，至少选一个', trigger: 'blur' },
    ]"
      >
        <el-radio-group v-model="choose" @change="chooseOne">
          <el-radio label="服务商公众号"></el-radio>
          <el-radio label="商家公众号"></el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item
        label="服务商公众号APPID "
        prop="mpAppid"
        v-show="MPShow && fwsShow"
        :rules="[
      { required: fwsShow, message: '请填写服务商公众号APPID', trigger: 'blur' },
    ]"
      >
        <el-input v-model="value.mpAppid" style="width: 370px;" />
      </el-form-item>

      <el-form-item
        label="商家公众号APPID"
        prop="mpSubAppid"
        v-show="MPShow && sjShow"
        :rules="[
      { required: sjShow, message: '请填写商家公众号APPID', trigger: 'blur' },
    ]"
      >
        <el-input v-model="value.mpSubAppid" style="width: 370px;" />
      </el-form-item>

      <el-form-item
        label="公众号页面截图 "
        prop="mpPics"
        v-show="MPShow"
        :rules="[
      { required: MPShow, message: '请上传公众号页面截图', trigger: 'blur' },
    ]"
      >
        <el-input v-model="value.mpPics" style="width: 370px;" />
      </el-form-item>

      <el-form-item
        label="公众号页面截图 "
        prop="mpPicsMediaId"
        v-show="MPShow"
        :rules="[
      { required: MPShow, message: '请上传公众号页面截图', trigger: 'blur' },
    ]"
      >
        <el-input v-model="value.mpPicsMediaId" style="width: 370px;" />
      </el-form-item>

      <el-form-item
        label="服务商小程序/商家小程序"
        prop="choose2"
        v-show="programShow"
        :rules="[
      { required: programShow, message: '服务商小程序/商家小程序，至少选一个', trigger: 'blur' },
    ]"
      >
        <el-radio-group v-model="choose2" @change="chooseTwo">
          <el-radio label="服务商小程序"></el-radio>
          <el-radio label="商家小程序"></el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item
        label="服务商小程序APPID "
        prop="miniProgramAppid"
        v-show="programShow && fwsPShow"
        :rules="[
      { required: fwsPShow, message: '请填写服务商小程序APPID', trigger: 'blur' },
    ]"
      >
        <el-input v-model="value.miniProgramAppid" style="width: 370px;" />
      </el-form-item>

      <el-form-item
        label="商家小程序APPID"
        prop="miniProgramSubAppid"
        v-show="programShow && sjPShow"
        :rules="[
      { sjPShow: programShow, message: '请填写商家小程序APPID', trigger: 'blur' },
    ]"
      >
        <el-input v-model="value.miniProgramSubAppid" style="width: 370px;" />
      </el-form-item>

      <el-form-item
        label="小程序页面截图 "
        prop="miniProgramPics"
        v-show="programShow"
        :rules="[
      { required: programShow, message: '请上传小程序页面截图', trigger: 'blur' },
    ]"
      >
        <el-input v-model="value.miniProgramPics" style="width: 370px;" />
      </el-form-item>

      <el-form-item
        label="小程序页面截图 "
        prop="miniProgramPicsMediaId"
        v-show="programShow"
        :rules="[
      { required: programShow, message: '请上传小程序页面截图', trigger: 'blur' },
    ]"
      >
        <el-input v-model="value.miniProgramPicsMediaId" style="width: 370px;" />
      </el-form-item>

      <el-form-item
        label="服务商应用/商家应用"
        prop="choose3"
        v-show="appShow"
        :rules="[
      { required: appShow, message: '服务商应用/商家应用，至少选一个', trigger: 'blur' },
    ]"
      >
        <el-radio-group v-model="choose3" @change="chooseThree">
          <el-radio label="服务商应用"></el-radio>
          <el-radio label="商家应用"></el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item
        label="服务商应用APPID "
        prop="appAppid"
        v-show="appShow && fwsAppShow"
        :rules="[
      { required: fwsAppShow, message: '请填写服务商应用APPID', trigger: 'blur' },
    ]"
      >
        <el-input v-model="value.appAppid" style="width: 370px;" />
      </el-form-item>

      <el-form-item
        label="商家应用APPID"
        prop="appSubAppid"
        v-show="appShow && sjAppShow"
        :rules="[
      { required: sjAppShow, message: '请填写商家应用APPID', trigger: 'blur' },
    ]"
      >
        <el-input v-model="value.appSubAppid" style="width: 370px;" />
      </el-form-item>

      <el-form-item
        label="APP页面截图 "
        prop="appPics"
        v-show="appShow"
        :rules="[
      { required: appShow, message: '请上传APP页面截图', trigger: 'blur' },
    ]"
      >
        <el-input v-model="value.appPics" style="width: 370px;" />
      </el-form-item>

      <el-form-item
        label="APP页面截图 "
        prop="appPicsMediaId"
        v-show="appShow"
        :rules="[
      { required: appShow, message: '请上传APP页面截图', trigger: 'blur' },
    ]"
      >
        <el-input v-model="value.appPicsMediaId" style="width: 370px;" />
      </el-form-item>

      <el-form-item
        label="互联网网站域名 "
        prop="domain"
        v-show="webShow"
        :rules="[
      { required: webShow, message: '请填写互联网网站域名', trigger: 'blur' },
    ]"
      >
        <el-input v-model="value.domain" style="width: 370px;" />
      </el-form-item>

      <el-form-item
        label="网站授权函"
        prop="webAuthorisation"
        v-show="webShow"
        :rules="[
      { required: webShow, message: '请上传网站授权函', trigger: 'blur' },
    ]"
      >
        <el-input v-model="value.webAuthorisation" style="width: 370px;" />
      </el-form-item>

      <el-form-item
        label="网站授权函"
        prop="webAuthorisationMediaId"
        v-show="webShow"
        :rules="[
      { required: webShow, message: '请上传网站授权函', trigger: 'blur' },
    ]"
      >
        <el-input v-model="value.webAuthorisationMediaId" style="width: 370px;" />
      </el-form-item>

      <el-form-item
        label="互联网网站对应的商家APPID "
        prop="webAppid"
        v-show="webShow"
        :rules="[
      { required: webShow, message: '请填写互联网网站对应的商家APPID', trigger: 'blur' },
    ]"
      >
        <el-input v-model="value.webAppid" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="商家企业微信CorpID " prop="subCorpId" v-show="weworkShow"
      :rules="[
      { required: weworkShow, message: '请填写商家企业微信CorpID', trigger: 'blur' },
    ]">
        <el-input v-model="value.subCorpId" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="企业微信页面截图 " prop="weworkPics" v-show="weworkShow"
      :rules="[
      { required: weworkShow, message: '请填写企业微信页面截图', trigger: 'blur' },
    ]">
        <el-input v-model="value.weworkPics" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="企业微信页面截图 " prop="weworkPicsMediaId" v-show="weworkShow"
      :rules="[
      { required: weworkShow, message: '请填写企业微信页面截图', trigger: 'blur' },
    ]">
        <el-input v-model="value.weworkPicsMediaId" style="width: 370px;" />
      </el-form-item>

      <el-form-item style="text-align: center">
        <el-button size="medium" @click="handlePrev">上一步，填写主体资料</el-button>
        <!-- <el-button type="primary" size="medium" @click="handleFinishCommit">下一步，填写结算规则</el-button> -->
        <el-button type="primary" size="medium" @click="handleNext('productRelationForm')">下一步，填写结算规则</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: "ProductRelationDetail",
  props: {
    value: Object,
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      storeShow: false,
      MPShow: false,
      programShow: false,
      webShow: false,
      appShow: false,
      weworkShow: false,
      fwsShow: false,
      sjShow: false,
      fwsPShow: false,
      sjPShow: false,
      fwsAppShow: false,
      sjAppShow: false,
      salesScenesType: [],
      choose: "",
      choose2: "",
      choose3: "",
      rules: {
        merchantShortname: [
          {
            required: true,
            message: "请填写商户简称",
            trigger: "change"
          }
        ],
        servicePhone: [
          {
            required: true,
            message: "请填写客服电话",
            trigger: "change"
          }
        ],
        // salesScenesType: [
        //   {
        //     type: "array",
        //     required: true,
        //     message: "请至少选择一个经营场景类型"
        //   }
        // ]
      }
    };
  },
  created() {},
  computed: {},
  methods: {
    chooseOne(e) {
      if (e == "服务商公众号") {
        this.fwsShow = true;
        this.sjShow = false;
      } else if (e == "商家公众号") {
        this.fwsShow = false;
        this.sjShow = true;
      }
    },
    chooseTwo(e) {
      if (e == "服务商小程序") {
        this.fwsPShow = true;
        this.sjPShow = false;
      } else if (e == "商家小程序") {
        this.fwsPShow = false;
        this.sjPShow = true;
      }
    },
    chooseThree(e) {
      if (e == "服务商应用") {
        this.fwsAppShow = true;
        this.sjAppShow = false;
      } else if (e == "商家应用") {
        this.fwsAppShow = false;
        this.sjAppShow = true;
      }
    },
    salesScenesTypeChange(e) {
      if (e.indexOf("SALES_SCENES_STORE") != "-1") {
        this.storeShow = true;
      } else {
        this.storeShow = false;
      }

      if (e.indexOf("SALES_SCENES_MP") != "-1") {
        this.MPShow = true;
      } else {
        this.MPShow = false;
      }

      if (e.indexOf("SALES_SCENES_MINI_PROGRAM") != "-1") {
        this.programShow = true;
      } else {
        this.programShow = false;
      }

      if (e.indexOf("SALES_SCENES_WEB") != "-1") {
        this.webShow = true;
      } else {
        this.webShow = false;
      }

      if (e.indexOf("SALES_SCENES_APP") != "-1") {
        this.appShow = true;
      } else {
        this.appShow = false;
      }

      if (e.indexOf("SALES_SCENES_WEWORK") != "-1") {
        this.weworkShow = true;
      } else {
        this.weworkShow = false;
      }
    },
    handlePrev() {
      this.$emit("prevStep");
    },
    handleNext(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.$emit("nextStep");
        } else {
          this.$message({
            message: "验证失败",
            type: "error",
            duration: 1000
          });
          return false;
        }
      });
    },
    // handleFinishCommit() {
    //   this.$emit("finishCommit", this.isEdit);
    // }
  }
};
</script>

<style scoped>
</style>
