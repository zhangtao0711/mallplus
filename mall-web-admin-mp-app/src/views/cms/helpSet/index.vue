<template>
  <div class="app-container">
    <el-form :model="helpSet" :rules="rules" ref="helpSetFrom" label-width="150px">
      <el-form-item label="个人中心显示：">
        <el-radio-group v-model="helpSet.userCentreShow">
          <el-radio label="1">是</el-radio>
          <el-radio label="0">否</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item>
        <el-button
          type="primary"
          @click="onSubmit('helpSetFrom')"
          element-loading-text="数据提交中..."
        >提交</el-button>
      </el-form-item>
      <!-- v-loading.fullscreen.lock="fullscreenLoading" -->
    </el-form>
  </div>
</template>
<script>
import { fetchList, updateHelpSet, createHelpSet } from "@/api/cms/helpSet";
import { get } from "@/utils/auth";
const defaultHelpSet = {};
export default {
  name: "helpsetList",
  data() {
    return {
      helpSet: Object.assign({}, defaultHelpSet),
      listQuery: {
        keyword: null,
        pageNum: 1,
        pageSize: 10
      },
      list: null,
      rules: {
        name: [{ required: true, message: "请输入品牌名称", trigger: "blur" }]
      },
      fullscreenLoading: false,
      isEdit: false
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      fetchList(this.listQuery).then(response => {
        
        if (response.data.records.length != 0) {
          this.isEdit = true;
          this.helpSet = response.data.records[0];
        } else {
          this.isEdit = false;
        }
      });
    },
    onSubmit(formName) {
      // this.fullscreenLoading = true;
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.$confirm("是否提交数据", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(() => {
            if (this.isEdit) {
              this.helpSet.updateBy = get('userId')
              updateHelpSet(this.helpSet.id, this.helpSet).then(
                response => {
                  // this.fullscreenLoading = false;
                  this.getList();
                  this.$message({
                    message: "修改成功",
                    type: "success",
                    duration: 1000
                  });
                }
              );
            } else {
              this.helpSet.createBy = get('userId')
              createHelpSet(this.helpSet).then(response => {
                this.fullscreenLoading = false;
                this.getList();
                this.$message({
                  message: "提交成功",
                  type: "success",
                  duration: 1000
                });
              });
            }
          });
        } else {
          this.$message({
            message: "验证失败",
            type: "error",
            duration: 1000
          });
          // this.fullscreenLoading = false;
          return false;
        }
      });
    }
  }
};
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
</style>


