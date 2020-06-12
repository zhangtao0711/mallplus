<template>
  <el-card class="form-container" shadow="never">
    <el-button size="mini" type="primary" @click="handleUpdate(wtFilterElement.id)">编辑</el-button>
    <el-button size="mini" type="danger" @click="handleDelete(wtFilterElement.id)">删除</el-button>
    <div class="table-container">
      <el-row>
        <el-col :span="12">
          <div class="bg-purple">机器编号</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElement.eqcode }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">滤芯名称</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElement.name }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">滤芯级别</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElement.filterElementLevel }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">已使用百分比</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElement.usePercent }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">滤芯状态</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElement.state }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">计费模式</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElement.billingMode }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">过滤水量（吨）</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElement.purifierNum }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">水量标准（吨）</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElement.purifiertotal }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">有效周期（天数）</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElement.changeCycle }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">滤芯更换时间</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElement.changeTime }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">到期时间</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElement.endTime }}</div>
        </el-col>
      </el-row>

      <!-- <el-row>
        <el-col :span="12">
          <div class="bg-purple">信息更新时间</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElement.updateTime }}</div>
        </el-col>
      </el-row>-->

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">备注</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElement.remarks }}</div>
        </el-col>
      </el-row>
    </div>
  </el-card>
</template>
<script>
import {
  getWtFilterElement,
  deleteWtFilterElement
} from "@/api/water/wtFilterElement";

const defaultWtFilterElement = {};
export default {
  name: "filterDetail",
  data() {
    return {
      wtFilterElement: Object.assign({}, defaultWtFilterElement)
    };
  },
  created() {
    getWtFilterElement(this.$route.query.id).then(response => {
      this.wtFilterElement = response.data;
    });
  },
  methods: {
    handleUpdate(id) {
      this.$router.push({
        path: "/equipment/updateWtFilterElement",
        query: { id: id }
      });
    },
    handleDelete(id) {
      this.$confirm("是否要删除该类型", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        deleteWtFilterElement(id).then(response => {
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



