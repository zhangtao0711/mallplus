<template>
  <div class="app-container">
    <el-form :model="helpSet" :rules="rules" ref="helpSetFrom" label-width="150px">
      <el-form-item label="个人中心显示：">
        <el-radio-group v-model="helpSet.showStatus">
          <el-radio label="1">是</el-radio>
          <el-radio label="0">否</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item>
        <el-button
          type="primary"
          :disabled="true"
          @click="onSubmit('helpSetFrom')"
          v-loading.fullscreen.lock="fullscreenLoading"
          element-loading-text="数据提交中..."
        >提交</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import { fetchList, deleteHelpCategory } from "@/api/cms/helpCategory";
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
      total: null,
      listLoading: true,
      multipleSelection: [],
      rules: {
        name: [
          { required: true, message: "请输入品牌名称", trigger: "blur" },
          {
            min: 2,
            max: 140,
            message: "长度在 2 到 140 个字符",
            trigger: "blur"
          }
        ]
      },
      fullscreenLoading: false
    };
  },
  created() {
    this.getList();

    // getHelpCategory(this.$route.query.id).then(response => {
    //   this.helpCategory = response.data;
    // });
  },
  methods: {
    getList() {
      this.listLoading = true;
      fetchList(this.listQuery).then(response => {
        this.listLoading = false;
        this.list = response.data.records;
        this.total = response.data.total;
        this.totalPage = response.data.pages;
        this.pageSize = response.data.size;
      });
    },
    onSubmit(formName) {
      this.fullscreenLoading = true;
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.$confirm("是否提交数据", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(() => {
            if (this.isEdit) {
              updateHelpCategory(this.$route.query.id, this.helpSet).then(
                response => {
                  this.fullscreenLoading = false;
                  this.$refs[formName].resetFields();
                  this.$message({
                    message: "修改成功",
                    type: "success",
                    duration: 1000
                  });
                }
              );
            } else {
              createHelpCategory(this.helpSet).then(response => {
                this.fullscreenLoading = false;
                this.$refs[formName].resetFields();
                this.helpSet = Object.assign({}, defaultHelpCategory);
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
          this.fullscreenLoading = false;
          return false;
        }
      });
    }
  }
};
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
</style>


