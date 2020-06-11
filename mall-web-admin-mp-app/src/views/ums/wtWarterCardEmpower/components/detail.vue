<template>
  <el-card class="form-container" shadow="never">
    <el-form
      :model="wtWarterCardEmpower"
      :rules="rules"
      ref="WtWarterCardEmpowerFrom"
      label-width="150px"
    >
      <el-form-item label="卡号" prop="cardNo">
        <el-input v-model="wtWarterCardEmpower.cardNo" placeholder="请输入会员卡号" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="授权人账号" prop="umsMemberId">
        <el-input v-model="wtWarterCardEmpower.umsMemberId" placeholder="请输入授权人账号" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="授权人昵称" prop="umsMemberNickname">
        <el-input v-model="wtWarterCardEmpower.umsMemberNickname" placeholder="请输入授权人昵称" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="被授权人账号" prop="umsMemberToId">
        <el-input v-model="wtWarterCardEmpower.umsMemberToId" placeholder="请输入被授权人账号" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="被授权人昵称" prop="umsMemberToNickname">
        <el-input v-model="wtWarterCardEmpower.umsMemberToNickname" placeholder="请输入被授权人昵称" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="与被授权人关系" prop="empowerRelation">
          <!-- (字典empower_relation) -->
        <el-select v-model="wtWarterCardEmpower.empowerRelation" style="width: 370px;" placeholder="请选择与被授权人关系">
          <el-option v-for="item in options" :key="item.value" :value="item.value"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSubmit('WtWarterCardEmpowerFrom')">提交</el-button>
        <el-button v-if="!isEdit" @click="resetForm('WtWarterCardEmpowerFrom')">重置</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>
<script>
import {
  createWtWarterCardEmpower,
  getWtWarterCardEmpower,
  updateWtWarterCardEmpower
} from "@/api/water/wtWarterCardEmpower";
import SingleUpload from "@/components/Upload/singleUpload";
import { get } from "@/utils/auth";

const defaultWtWarterCardEmpower = {};
export default {
  name: "WtWarterCardEmpowerDetail",
  components: { SingleUpload },
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      options: [
        {
          value: "父母"
        },
        {
          value: "配偶"
        },
        {
          value: "子女"
        },
        {
          value: "其他"
        }
      ],
      wtWarterCardEmpower: Object.assign({}, defaultWtWarterCardEmpower),
      rules: {
        cardNo: [{ required: true, message: "请输入会员卡号", trigger: "blur" }],
        umsMemberId: [{ required: true, message: "请输入授权人账号", trigger: "blur" }],
        umsMemberNickname: [{ required: true, message: "请输入授权人昵称", trigger: "blur" }],
        umsMemberToId: [{ required: true, message: "请输入被授权人账号", trigger: "blur" }],
        umsMemberToNickname: [{ required: true, message: "请输入被授权人昵称", trigger: "blur" }],
        empowerRelation: [{ required: true, message: "请选择与被授权人关系", trigger: "blur" }],
      }
    };
  },
  created() {
    if (this.isEdit) {
      getWtWarterCardEmpower(this.$route.query.id).then(response => {
        this.wtWarterCardEmpower = response.data;
      });
    } else {
      this.wtWarterCardEmpower = Object.assign({}, defaultWtWarterCardEmpower);
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
            if (this.isEdit) {
              updateWtWarterCardEmpower(
                this.$route.query.id,
                this.wtWarterCardEmpower
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
            } else {
              this.wtWarterCardEmpower.createBy = get("userId");
              this.wtWarterCardEmpower.dealerId = get("userId");
              this.wtWarterCardEmpower.delFlag = 1;
              createWtWarterCardEmpower(this.wtWarterCardEmpower).then(
                response => {
                  if (response.code == 200) {
                    this.$refs[formName].resetFields();
                    this.wtWarterCardEmpower = Object.assign(
                      {},
                      defaultWtWarterCardEmpower
                    );
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
                }
              );
            }
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
      this.wtWarterCardEmpower = Object.assign({}, defaultWtWarterCardEmpower);
    }
  }
};
</script>
<style>
</style>


