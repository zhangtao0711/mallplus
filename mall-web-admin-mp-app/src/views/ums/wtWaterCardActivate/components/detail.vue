<template>
  <el-card class="form-container" shadow="never">
    <el-form
      :model="wtWaterCardActivate"
      :rules="rules"
      ref="WtWaterCardActivateFrom"
      label-width="150px"
    >
      <el-form-item label="起始卡号" prop="startNo">
        <el-input v-model="wtWaterCardActivate.startNo" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="终止卡号" prop="endNo">
        <el-input v-model="wtWaterCardActivate.endNo" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="设备号" prop="eqcode">
        <el-input v-model="wtWaterCardActivate.eqcode" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="商家账号" prop="dealerId">
        <el-input v-model="wtWaterCardActivate.dealerId" style="width: 370px;" />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSubmit('WtWaterCardActivateFrom')">提交</el-button>
        <el-button v-if="!isEdit" @click="resetForm('WtWaterCardActivateFrom')">重置</el-button>
      </el-form-item>
    </el-form>


    <div class="table-container">
      <el-table
        ref="wtWaterCardActivateTable"
        :data="list"
        style="width: 100%"
        v-loading="listLoading"
        border
      >
        <el-table-column prop="id" label="编号" align="center">
          <template slot-scope="scope">{{scope.row.id }}</template>
        </el-table-column>
        <el-table-column prop="dealerId" label="商家" align="center">
          <template slot-scope="scope">{{scope.row.dealerId }}</template>
        </el-table-column>
        <el-table-column prop="startNo" label="起始卡号" align="center">
          <template slot-scope="scope">{{scope.row.startNo }}</template>
        </el-table-column>
        <el-table-column prop="endNo" label="终止卡号" align="center">
          <template slot-scope="scope">{{scope.row.endNo }}</template>
        </el-table-column>
        <!-- <el-table-column prop="eqcode" label="名字">
          <template slot-scope="scope">{{scope.row.eqcode }}</template>
        </el-table-column> -->
      </el-table>
    </div>
  </el-card>
</template>
<script>
import {
  createWtWaterCardActivate,
  getWtWaterCardActivate,
  updateWtWaterCardActivate,
  validNumber
} from "@/api/water/wtWaterCardActivate";
import SingleUpload from "@/components/Upload/singleUpload";
import { get } from "@/utils/auth";

const defaultWtWaterCardActivate = {
  dealerId: "",
  endNo: "",
  eqcode: "",
  startNo: "",
  storeId: ""
};
export default {
  name: "WtWaterCardActivateDetail",
  components: { SingleUpload },
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      listQuery: {
        keyword: null,
        pageNum: 1,
        pageSize: 10,
        dealerId: get('storeId')
      },
      list: null,
      total: null,
      listLoading: true,
      wtWaterCardActivate: Object.assign({}, defaultWtWaterCardActivate),
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
    this.getList();
    if (this.isEdit) {
      getWtWaterCardActivate(this.$route.query.id).then(response => {
        this.wtWaterCardActivate = response.data;
      });
    } else {
      this.wtWaterCardActivate = Object.assign({}, defaultWtWaterCardActivate);
    }
  },
  methods: {
    getList() {
      this.listLoading = true;
      validNumber(this.listQuery).then(response => {
        this.listLoading = false;
        this.list = response.data.records;
        this.total = response.data.total;
        this.totalPage = response.data.pages;
        this.pageSize = response.data.size;
      });
    },
    onSubmit(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.$confirm("是否提交数据", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(() => {
            if (this.isEdit) {
              updateWtWaterCardActivate(
                this.$route.query.id,
                this.wtWaterCardActivate
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
              this.wtWaterCardActivate.createBy = get("userId");
              createWtWaterCardActivate(this.wtWaterCardActivate).then(
                response => {
                  if (response.code == 200) {
                    this.$refs[formName].resetFields();
                    this.wtWaterCardActivate = Object.assign(
                      {},
                      defaultWtWaterCardActivate
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
      this.wtWaterCardActivate = Object.assign({}, defaultWtWaterCardActivate);
    }
  }
};
</script>
<style>
</style>


