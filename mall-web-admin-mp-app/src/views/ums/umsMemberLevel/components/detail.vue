<template>
  <el-card class="form-container" shadow="never">
    <el-form :model="umsMemberLevel" :rules="rules" ref="UmsMemberLevelFrom" label-width="150px">
      <el-form-item label="等级名称" prop="name">
        <el-input v-model="umsMemberLevel.name" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="成为会员的价格" prop="price">
        <el-input v-model="umsMemberLevel.price" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="会员折扣(0.1-10)" prop="priviledgeMemberPrice">
        <el-input v-model="umsMemberLevel.priviledgeMemberPrice" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="状态" prop="state">
        <el-radio-group v-model="umsMemberLevel.state">
          <el-radio label="0">启用</el-radio>
          <el-radio label="1">禁用</el-radio>
        </el-radio-group>

        <!-- <el-input v-model="umsMemberLevel.state" style="width: 370px;" /> -->
      </el-form-item>

      <el-form-item label="适用场景" prop="applyScene">
        <!-- <el-input v-model="umsMemberLevel.applyScene" style="width: 370px;" /> -->
        <el-radio-group v-model="umsMemberLevel.applyScene">
          <el-radio label="0">平台商品</el-radio>
          <el-radio label="1">平台积分商城</el-radio>
        </el-radio-group>
      </el-form-item>

      <!-- <el-form-item label="等级图标" prop="icon">
        <el-input v-model="umsMemberLevel.icon" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="背景色区域" prop="pic">
        <el-input v-model="umsMemberLevel.pic" style="width: 370px;" />
      </el-form-item>-->

      <!-- <el-form-item label="创建日期" prop="createTime">
        <el-input v-model="umsMemberLevel.createTime" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="创建人" prop="createBy">
        <el-input v-model="umsMemberLevel.createBy" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="更新日期" prop="updateTime">
        <el-input v-model="umsMemberLevel.updateTime" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="更新人" prop="updateBy">
        <el-input v-model="umsMemberLevel.updateBy" style="width: 370px;" />
      </el-form-item>-->

      <!-- <el-form-item label="删除状态(1-正常,0-已删除)" prop="delFlag">
        <el-input v-model="umsMemberLevel.delFlag" style="width: 370px;" />
      </el-form-item>-->

      <!-- <el-form-item label="所属店铺" prop="storeId">
        <el-input v-model="umsMemberLevel.storeId" style="width: 370px;" />
      </el-form-item>-->

      <el-form-item>
        <el-button type="primary" @click="onSubmit('UmsMemberLevelFrom')">提交</el-button>
        <el-button v-if="!isEdit" @click="resetForm('UmsMemberLevelFrom')">重置</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>
<script>
import {
  createUmsMemberLevel,
  getUmsMemberLevel,
  updateUmsMemberLevel
} from "@/api/ums/umsMemberLevel";
import SingleUpload from "@/components/Upload/singleUpload";
import { get } from "@/utils/auth";

const defaultUmsMemberLevel = {
  name: ""
};
export default {
  name: "UmsMemberLevelDetail",
  components: { SingleUpload },
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      umsMemberLevel: Object.assign({}, defaultUmsMemberLevel),
      rules: {
        name: [
          { required: true, message: "请输入品牌名称", trigger: "blur" },
          {
            min: 2,
            max: 140,
            message: "长度在 2 到 140 个字符",
            trigger: "blur"
          }
        ],
        logo: [{ required: true, message: "请输入品牌logo", trigger: "blur" }],
        sort: [{ type: "number", message: "排序必须为数字" }]
      }
    };
  },
  created() {
    if (this.isEdit) {
      getUmsMemberLevel(this.$route.query.id).then(response => {
        this.umsMemberLevel = response.data;
      });
    } else {
      this.umsMemberLevel = Object.assign({}, defaultUmsMemberLevel);
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
              this.umsMemberLevel.updateBy = get("userId");
              updateUmsMemberLevel(
                this.$route.query.id,
                this.umsMemberLevel
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
              this.umsMemberLevel.delFlag = 1;
              this.umsMemberLevel.createBy = get("userId");
              createUmsMemberLevel(this.umsMemberLevel).then(response => {
                if (response.code == 200) {
                  this.$refs[formName].resetFields();
                  this.umsMemberLevel = Object.assign(
                    {},
                    defaultUmsMemberLevel
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
              });
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
      this.umsMemberLevel = Object.assign({}, defaultUmsMemberLevel);
    }
  }
};
</script>
<style>
</style>


