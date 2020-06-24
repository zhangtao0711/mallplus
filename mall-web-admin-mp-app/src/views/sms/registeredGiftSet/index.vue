<template>
  <el-card class="mine" shadow="never">
    <h2>注册赠送</h2>
    <div>设置注册赠送，新用户在注册后会获得指定积分或水量</div>
    <el-row
      :gutter="20"
      style="text-align: center;margin: 20px auto;display:flex;align-items: center"
    >
      <el-col :span="4">注册成功发放</el-col>
      <el-col :span="16">
        <el-input style="width:45%" size="small">
          <template slot="append">积分</template>
        </el-input>
        <el-input style="width:45%" size="small">
          <template slot="append">水费</template>
        </el-input>
      </el-col>
      <el-col :span="4">
        <span>所有的水量=水费=钱</span>
      </el-col>
    </el-row>

    <el-button type="primary" @click="onSubmit">保存设置</el-button>
  </el-card>
</template>
<script>
import {
  createSmsContent,
  getSmsContent,
  updateSmsContent
} from "@/api/sms/smsContent";
import SingleUpload from "@/components/Upload/singleUpload";

const defaultSmsContent = {
  name: ""
};
export default {
  name: "SmsContentDetail",
  components: { SingleUpload },
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      smsContent: Object.assign({}, defaultSmsContent),
      rules: {
        name: [{ required: true, message: "请输入品牌名称", trigger: "blur" }]
      }
    };
  },
  created() {
    if (this.isEdit) {
      getSmsContent(this.$route.query.id).then(response => {
        this.smsContent = response.data;
      });
    } else {
      this.smsContent = Object.assign({}, defaultSmsContent);
    }
  },
  methods: {
    onSubmit(formName) {
      this.$confirm("是否提交数据", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        if (this.isEdit) {
          updateSmsContent(this.$route.query.id, this.smsContent).then(
            response => {
              if (response.code == 200) {
                this.$refs[formName].resetFields();
                this.$message({
                  message: "修改成功",
                  type: "success",
                  duration: 1000
                });
                this.$router.back();
              } else {
                this.$message({
                  message: response.msg,
                  type: "error",
                  duration: 1000
                });
              }
            }
          );
        } else {
          createSmsContent(this.smsContent).then(response => {
            if (response.code == 200) {
              this.$refs[formName].resetFields();
              this.smsContent = Object.assign({}, defaultSmsContent);
              this.$message({
                message: "提交成功",
                type: "success",
                duration: 1000
              });
              this.$router.back();
            } else {
              this.$message({
                message: response.msg,
                type: "error",
                duration: 1000
              });
            }
          });
        }
      });
    }
  }
};
</script>
<style>
.mine {
  width: 65%;
  position: absolute;
  left: 0;
  right: 0;
  padding: 35px 35px 15px 35px;
  margin: 20px auto;
}
</style>


