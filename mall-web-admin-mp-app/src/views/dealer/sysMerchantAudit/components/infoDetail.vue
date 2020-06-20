<template>
  <el-card class="form-container" shadow="never">
    <el-button size="mini" type="primary" v-if="wtFilterElementType.status=='0'" @click="agreeAuthorize(wtFilterElementType.id)">同意授权</el-button>
    <!-- <el-button size="mini" type="primary" @click="handleUpdate(wtFilterElementType.id)">编辑</el-button>
    <el-button size="mini" type="danger" @click="handleDelete(wtFilterElementType.id)">删除</el-button>-->
    <div class="table-container">
      <el-row>
        <el-col :span="12">
          <div class="bg-purple">编号</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElementType.id }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">发起商家</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElementType.initiateId }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">授权商家</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElementType.dealerId }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">消费授权</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElementType.sale }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">组内消费授权</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElementType.groupSale }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">授权状态</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElementType.status | formatStatus }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">修改时间</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElementType.updateTime | formatUpdateTime }}</div>
        </el-col>
      </el-row>
    </div>
  </el-card>
</template>
<script>
import {
  createSysMerchantAudit,
  getSysMerchantAudit,
  updateSysMerchantAudit
} from "@/api/dealer/sysMerchantAudit";
import { formatDate } from "@/utils/date";

const defaultWtFilterElementType = {};
export default {
  name: "typeDetail",
  data() {
    return {
      wtFilterElementType: Object.assign({}, defaultWtFilterElementType)
    };
  },
  created() {
    getSysMerchantAudit(this.$route.query.id).then(response => {
      this.wtFilterElementType = response.data;
    });
  },
  filters: {
    formatUpdateTime(time) {
      let date = new Date(time);
      return formatDate(date, "yyyy-MM-dd hh:mm:ss");
    },
    formatStatus(value) {
      if (value == "0") {
        return "等待确认";
      } else {
        return "授权成功";
      }
    }
  },
  methods: {
    agreeAuthorize(id) {
      let data = {
        status: 1
      }
      updateSysMerchantAudit(id, data).then(
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
    },

    handleUpdate(id) {
      this.$router.push({
        path: "/equipment/updateWtFilterElementType",
        query: { id: id }
      });
    },
    handleDelete(id) {
      this.$confirm("是否要删除该数据", "提示", {
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



