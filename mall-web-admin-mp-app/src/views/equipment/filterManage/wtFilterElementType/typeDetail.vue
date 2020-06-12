<template>
  <el-card class="form-container" shadow="never">
    <el-button size="mini" type="primary" @click="handleUpdate(wtFilterElementType.id)">编辑</el-button>
    <el-button size="mini" type="danger" @click="handleDelete(wtFilterElementType.id)">删除</el-button>
    <div class="table-container">
      <el-row>
        <el-col :span="12">
          <div class="bg-purple">滤芯名称</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElementType.filterElementTypeName }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">更换周期(天数)</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElementType.changeCycle }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">更换滤芯提前提醒(天数)</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElementType.remindDay }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">净水总量(吨)</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElementType.purifierTotal }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">创建时间</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElementType.createTime }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">更新时间</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElementType.updateTime }}</div>
        </el-col>
      </el-row>
    </div>
  </el-card>
</template>
<script>
import {
  getWtFilterElementType,
  deleteWtFilterElementType
} from "@/api/water/wtFilterElementType";

const defaultWtFilterElementType = {};
export default {
  name: "typeDetail",
  data() {
    return {
      wtFilterElementType: Object.assign({}, defaultWtFilterElementType)
    };
  },
  created() {
    getWtFilterElementType(this.$route.query.id).then(response => {
      this.wtFilterElementType = response.data;
    });
  },
  methods: {
    handleUpdate(id) {
      this.$router.push({
        path: "/equipment/updateWtFilterElementType",
        query: { id: id }
      });
    },
    handleDelete(id) {
      this.$confirm("是否要删除该类型", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        deleteWtFilterElementType(id).then(response => {
          this.$message({
            message: "删除成功",
            type: "success",
            duration: 1000
          });
          this.$router.back();
        });
      });
    }
  }
};
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
.table-container {
  margin: 20px auto;
}
.el-row {
  margin-bottom: 20px;
  text-align: center;
  height: 40px;

  &:last-child {
    margin-bottom: 0;
  }
  .bg-purple {
    height: 40px;
    padding: 10px;
    background: #d3dce6;
  }
  .bg-purple-light {
    height: 40px;
    padding: 10px;
    background: #e5e9f2;
  }
}
</style>



