<template>
  <el-card class="form-container" shadow="never">
    <el-form
      :model="wtWaterCardProblem"
      :rules="rules"
      ref="WtWaterCardProblemFrom"
      label-width="150px"
    >
      <el-form-item label="卡号" prop="cardNo">
        <el-input v-model="wtWaterCardProblem.cardNo" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="用户姓名" prop="umsMemberName">
        <el-input v-model="wtWaterCardProblem.umsMemberName" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="类型" prop="state">
        <el-select v-model="wtWaterCardProblem.state" placeholder="请选择">
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="备注" prop="remarks">
        <el-input
          type="textarea"
          :rows="2"
          placeholder="请输入内容"
          v-model="wtWaterCardProblem.remarks"
        />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSubmit('WtWaterCardProblemFrom')">提交</el-button>
        <el-button v-if="!isEdit" @click="resetForm('WtWaterCardProblemFrom')">重置</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>
<script>
import { updateWtWaterCardProblem } from "@/api/water/wtWaterCardProblem";
import SingleUpload from "@/components/Upload/singleUpload";
import { get } from "@/utils/auth";

const defaultWtWaterCardProblem = {};
export default {
  name: "WtWaterCardProblemDetail",
  components: { SingleUpload },
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      wtWaterCardProblem: Object.assign({}, defaultWtWaterCardProblem),
      rules: {
        cardNo: [{ required: true, message: "请输入会员卡号", trigger: "blur" }]
      },
      options: [
        {
          value: "1",
          label: "复制卡"
        },
        {
          value: "2",
          label: "挂失卡"
        },
        {
          value: "3",
          label: "删除卡"
        },
        {
          value: "4",
          label: "非经销商卡"
        }
      ]
    };
  },
  created() {
    if (this.isEdit) {
      getWtWaterCardProblem(this.$route.query.id).then(response => {
        this.wtWaterCardProblem = response.data;
      });
    } else {
      this.wtWaterCardProblem = Object.assign({}, defaultWtWaterCardProblem);
    }
  },
  methods: {
    onSubmit(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.$confirm("是否提交数据", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(() => {
            updateWtWaterCardProblem(
              this.wtWaterCardProblem.cardNo,
              this.wtWaterCardProblem
            ).then(response => {
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
            });
          });
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
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.wtWaterCardProblem = Object.assign({}, defaultWtWaterCardProblem);
    }
  }
};
</script>
<style>
</style>


