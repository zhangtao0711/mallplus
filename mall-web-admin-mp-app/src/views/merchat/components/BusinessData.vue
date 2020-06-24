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
        <div class="message">
          <div>（1）不支持单纯以人名来命名，若为个体户经营，可用“个体户+经营者名称”或“经营者名 称+业务”命名，如“个体户张三”或“张三餐饮店”；</div>
          <div>（2）不支持无实际意义的文案，如“XX特约商户”、“800”、“XX客服电话XXX”；</div>
        </div>
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

      <!-- 线下门店 -->
      <div v-show="storeShow">
        <el-form-item
          label="线下门店名称"
          prop="bizStoreName"
          :rules="[
            { required: storeShow, message: '请填写线下门店名称', trigger: 'blur' },
          ]"
        >
          <el-input v-model="value.bizStoreName" style="width: 370px;" />
        </el-form-item>

        <el-form-item
          label="线下门店省市编码 "
          prop="bizAddressCode"
          :rules="[
            { required: storeShow, message: '请填写线下门店省市编码', trigger: 'blur' },
          ]"
        >
          <el-input v-model="value.bizAddressCode" style="width: 370px;" />
        </el-form-item>

        <el-form-item
          label="线下门店地址"
          prop="bizStoreAddress"
          :rules="[
            { required: storeShow, message: '请填写线下门店地址', trigger: 'blur' },
          ]"
        >
          <el-input v-model="value.bizStoreAddress" style="width: 370px;" />
        </el-form-item>

        <el-form-item
          label="线下门店门头照片"
          prop="storeEntrancePic"
          :rules="[
            { required: storeShow, message: '请上传线下门店门头照片', trigger: 'blur' },
          ]"
        >
          <single-upload-tysh
            v-model="storeEntrancePic"
            style="width: 300px;display: inline-block;margin-left: 10px"
          ></single-upload-tysh>
        </el-form-item>

        <el-form-item
          label="店内环境照片"
          prop="indoorPic"
          :rules="[
            { required: storeShow, message: '请上传店内环境照片', trigger: 'blur' },
          ]"
        >
          <single-upload-tysh
            v-model="indoorPic"
            style="width: 300px;display: inline-block;margin-left: 10px"
          ></single-upload-tysh>
        </el-form-item>

        <el-form-item
          label="线下场所对应的商家APPID "
          prop="bizSubAppid"
          :rules="[
            { required: storeShow, message: '请填写线下场所对应的商家APPID', trigger: 'blur' },
          ]"
        >
          <el-input v-model="value.bizSubAppid" style="width: 370px;" />
        </el-form-item>
      </div>

      <!-- 公众号 -->
      <div v-show="MPShow">
        <el-form-item
          label="服务商公众号/商家公众号"
          prop="choose"
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
        >
          <single-upload-tysh
            v-model="mpPics"
            style="width: 300px;display: inline-block;margin-left: 10px"
          ></single-upload-tysh>
          <!-- <multi-upload-tysh
            v-model="mpPics"
            style="width: 300px;display: inline-block;margin-left: 10px"
          ></multi-upload-tysh>-->
          <!-- <el-input v-model="mpPics" style="width: 370px;" /> -->
        </el-form-item>
      </div>

      <!-- 小程序 -->
      <div v-show="programShow">
        <el-form-item
          label="服务商小程序/商家小程序"
          prop="choose2"
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
          :rules="[
          { required: programShow, message: '请上传小程序页面截图', trigger: 'blur' },
        ]"
        >
          <single-upload-tysh
            v-model="miniProgramPics"
            style="width: 300px;display: inline-block;margin-left: 10px"
          ></single-upload-tysh>
        </el-form-item>
      </div>

      <!-- 应用 -->
      <div v-show="appShow">
        <el-form-item
          label="服务商应用/商家应用"
          prop="choose3"
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
          :rules="[
          { required: appShow, message: '请上传APP页面截图', trigger: 'blur' },
        ]"
        >
          <single-upload-tysh
            v-model="appPics"
            style="width: 300px;display: inline-block;margin-left: 10px"
          ></single-upload-tysh>
        </el-form-item>
      </div>

      <!-- 互联网 -->
      <div v-show="webShow">
        <el-form-item
          label="互联网网站域名 "
          prop="domain"
          :rules="[
            { required: webShow, message: '请填写互联网网站域名', trigger: 'blur' },
          ]"
        >
          <el-input v-model="value.domain" style="width: 370px;" />
        </el-form-item>

        <el-form-item
          label="网站授权函"
          prop="webAuthorisation"
          :rules="[
            { required: webShow, message: '请上传网站授权函', trigger: 'blur' },
          ]"
        >
          <single-upload-tysh
            v-model="webAuthorisation"
            style="width: 300px;display: inline-block;margin-left: 10px"
          ></single-upload-tysh>
        </el-form-item>

        <el-form-item
          label="互联网网站对应的商家APPID "
          prop="webAppid"
          :rules="[
            { required: webShow, message: '请填写互联网网站对应的商家APPID', trigger: 'blur' },
          ]"
        >
          <el-input v-model="value.webAppid" style="width: 370px;" />
        </el-form-item>
      </div>

      <!-- 企业微信 -->
      <div v-show="weworkShow">
        <el-form-item
          label="商家企业微信CorpID "
          prop="subCorpId"
          :rules="[
            { required: weworkShow, message: '请填写商家企业微信CorpID', trigger: 'blur' },
          ]"
        >
          <el-input v-model="value.subCorpId" style="width: 370px;" />
        </el-form-item>

        <el-form-item
          label="企业微信页面截图 "
          prop="weworkPics"
          :rules="[
            { required: weworkShow, message: '请填写企业微信页面截图', trigger: 'blur' },
          ]"
        >
          <single-upload-tysh
            v-model="weworkPics"
            style="width: 300px;display: inline-block;margin-left: 10px"
          ></single-upload-tysh>
        </el-form-item>
      </div>

      <el-form-item style="text-align: center">
        <el-button size="medium" @click="handlePrev">上一步，填写主体资料</el-button>
        <!-- <el-button type="primary" size="medium" @click="handleFinishCommit">下一步，填写结算规则</el-button> -->
        <el-button
          type="primary"
          size="medium"
          @click="handleNext('productRelationForm')"
        >下一步，填写结算规则</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import SingleUploadTysh from "@/components/Upload/singleUploadTysh";
import MultiUploadTysh from "@/components/Upload/multiUploadTysh";
export default {
  name: "ProductRelationDetail",
  components: {
    SingleUploadTysh,
    MultiUploadTysh
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
      storeEntrancePic: {
        url: "",
        MediaID: ""
      },
      indoorPic: {
        url: "",
        MediaID: ""
      },
      mpPics: {
        url: "",
        MediaID: ""
      },
      miniProgramPics: {
        url: "",
        MediaID: ""
      },
      appPics: {
        url: "",
        MediaID: ""
      },
      webAuthorisation: {
        url: "",
        MediaID: ""
      },
      weworkPics: {
        url: "",
        MediaID: ""
      },
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
          },
          { min: 2, max: 30, message: "长度在 2 到 30 个字符", trigger: "blur" }
        ],
        servicePhone: [
          {
            required: true,
            message: "请填写客服电话",
            trigger: "change"
          }
        ]
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
      this.value.salesScenesType = e.join(',')
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
          this.value.storeEntrancePic = this.storeEntrancePic.url;
          this.value.storeEntrancePicMediaId = this.storeEntrancePic.MediaID;

          this.value.indoorPic = this.indoorPic.url;
          this.value.indoorPicMediaId = this.indoorPic.MediaID;

          this.value.mpPics = this.mpPics.url;
          this.value.mpPicsMediaId = this.mpPics.MediaID;

          this.value.miniProgramPics = this.miniProgramPics.url;
          this.value.miniProgramPicsMediaId = this.miniProgramPics.MediaID;

          this.value.appPics = this.appPics.url;
          this.value.appPicsMediaId = this.appPics.MediaID;

          this.value.webAuthorisation = this.webAuthorisation.url;
          this.value.webAuthorisationMediaId = this.webAuthorisation.MediaID;

          this.value.weworkPics = this.weworkPics.url;
          this.value.weworkPicsMediaId = this.weworkPics.MediaID;
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
    }
  }
};
</script>

<style scoped>
.message{
  font-size: 12px;
  color: #666666;
}
</style>
