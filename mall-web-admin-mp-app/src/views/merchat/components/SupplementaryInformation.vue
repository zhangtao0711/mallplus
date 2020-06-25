<template>
  <div style="margin-top: 50px">
    <el-form
      :model="value"
      ref="productSaleForm"
      label-width="120px"
      style="width: 600px"
      size="small"
    >
      <el-form-item label="法人开户承诺函" prop="legalPersonCommitment">
        <single-upload-tysh
          v-model="legalPersonCommitment"
          style="width: 300px;display: inline-block;margin-left: 10px"
        ></single-upload-tysh>
        <div>
          请上传法定代表人或负责人亲笔签署的开户承诺函扫描件（
          <a
            target="_blank"
            href="https://kf.qq.com/faq/191018yUFjEj191018Vfmaei.html"
          >下载模板</a>
          ）。亲笔签名承诺函内容清晰可见，不得有涂污，破损，字迹不清晰现象。
        </div>
      </el-form-item>

      <el-form-item label="法人开户意愿视频" prop="legalPersonVideo">
        <video-upload-tysh
          v-model="legalPersonVideo"
          style="width: 300px;display: inline-block;margin-left: 10px"
        ></video-upload-tysh>

        <div>
          建议法人按如下话术录制“法人开户意愿视频”：
          <div>我是#公司全称#的法定代表人（或负责人），特此证明本公司申请的商户号为我司真实意愿开立且用于XX业务（或XX服务）。我司现有业务符合法律法规及腾讯的相关规定。</div>
        </div>
      </el-form-item>

      <el-form-item label="补充材料" prop="businessAdditionPics">
        <single-upload-tysh
          v-model="businessAdditionPics"
          style="width: 300px;display: inline-block;margin-left: 10px"
        ></single-upload-tysh>
        <div>
          根据驳回要求提供额外信息，如：
          <p>（1）业务模式不清晰时，需详细描述支付场景或提供相关材料（如业务说明/门店照/ 手持证件照等）；</p>
          <p>（2）特殊业务要求提供相关的协议材料等；</p>
        </div>
      </el-form-item>

      <el-form-item label="补充说明 " prop="businessAdditionMsg">
        <el-input type="textarea" v-model="value.businessAdditionMsg" style="width: 370px;" />
        <div>根据驳回要求提供额外信息，如：业务模式不清晰时，请详细描述支付场景。</div>
      </el-form-item>

      <el-form-item style="text-align: center">
        <el-button size="medium" @click="handlePrev">上一步，填写结算银行账号</el-button>
        <el-button type="primary" size="medium" @click="handleFinishCommit">下一步，提交</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import VideoUploadTysh from "@/components/Upload/VideoUploadTysh";
import SingleUploadTysh from "@/components/Upload/singleUploadTysh";
export default {
  name: "ProductSaleDetail",
  components: {
    VideoUploadTysh,
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
      legalPersonCommitment: {
        url: "",
        MediaID: ""
      },
      legalPersonVideo: {
        url: "",
        MediaID: ""
      },
      businessAdditionPics: {
        url: "",
        MediaID: ""
      }
    };
  },
  created() {
    if (this.isEdit) {
    } else {
    }
  },
  computed: {},
  methods: {
    handlePrev() {
      this.$emit("prevStep");
    },
    handleFinishCommit() {
      if (!this.value.legalPersonCommitmentMediaId) {
        this.value.legalPersonCommitmentMediaId = this.legalPersonCommitment.MediaID;
      }
      if (!this.value.legalPersonCommitment) {
        this.value.legalPersonCommitment = this.legalPersonCommitment.url;
      }

      if (!this.value.legalPersonVideo) {
        this.value.legalPersonVideo = this.legalPersonVideo.url;
      }
      if (!this.value.legalPersonVideoMediaId) {
        this.value.legalPersonVideoMediaId = this.legalPersonVideo.MediaID;
      }

      if (!this.value.businessAdditionPics) {
        this.value.businessAdditionPics = this.businessAdditionPics.url;
      }
      if (!this.value.businessAdditionPicsMediaId) {
        this.value.businessAdditionPicsMediaId = this.businessAdditionPics.MediaID;
      }

      this.$emit("finishCommit", this.isEdit);
    }
  }
};
</script>

<style scoped>
.littleMargin {
  margin-top: 10px;
}
a {
  text-decoration: underline;
  color: deepskyblue;
}
</style>
