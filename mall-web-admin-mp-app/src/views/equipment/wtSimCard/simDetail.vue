<template>
  <el-card class="form-container" shadow="never">
    <el-button size="mini" type="success" @click="handleRecharge(wtSimCard.cardno)">充值</el-button>
    <el-button size="mini" type="primary" @click="handleActivation(wtSimCard.cardno)">激活</el-button>
    <!-- <el-button size="mini" type="primary" @click="handleUpdate(wtSimCard.id)">编辑</el-button> -->
    <el-button size="mini" type="danger" @click="handleDelete(wtSimCard.id)">删除</el-button>
    <div class="table-container">
      <el-row>
        <el-col :span="12">
          <div class="bg-purple">卡号</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtSimCard.cardno }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">卡状态</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtSimCard.state | formatStatus }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">主板设备号</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtSimCard.deviceId }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">地址</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtSimCard.address }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">商家</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtSimCard.state }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">套餐价格</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtSimCard.billingMode }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">运营商</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtSimCard.purifierNum }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">卡有效期</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtSimCard.purifiertotal }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">预充值生效日期</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtSimCard.changeCycle }}</div>
        </el-col>
      </el-row>
    </div>
  </el-card>
</template>
<script>
import {
  getWtSimCard,
  deleteWtSimCard,
  rechargeSIM
} from "@/api/water/wtSimCard";

const defaultWtSimCard = {};
export default {
  name: "simDetail",
  data() {
    return {
      wtSimCard: Object.assign({}, defaultWtSimCard)
    };
  },
  created() {
    this.getWtSimCard();
  },
  filters: {
    formatStatus(value) {
      if (value == "0") {
        return "未知";
      } else if (value == "00") {
        return "正常";
      } else if (value == "01") {
        return "单向停机";
      } else if (value == "02") {
        return "停机";
      } else if (value == "03") {
        return "预销号";
      } else if (value == "04") {
        return "销号/拆机";
      } else if (value == "05") {
        return "过户";
      } else if (value == "06") {
        return "休眠";
      } else if (value == "07") {
        return "待激";
      } else if (value == "08") {
        return "已停用";
      } else if (value == "09") {
        return "库存";
      } else if (value == "10") {
        return "已失效";
      } else if (value == "11") {
        return "违章停机";
      } else if (value == "12") {
        return "挂失";
      } else if (value == "13") {
        return "用户报停";
      } else if (value == "14") {
        return "断网";
      } else if (value == "99") {
        return "不存在";
      }
    }
  },
  methods: {
    getWtSimCard() {
      getWtSimCard(this.$route.query.id).then(response => {
        this.wtSimCard = response.data;
      });
    },
    handleRecharge(cardno) {
      let data = {
        cardno: cardno
      };
      rechargeSIM(data).then(response => {
        this.$message({
          message: response.msg,
          type: "success",
          duration: 1000
        });
        this.getWtSimCard();
      });
    },
    handleActivation(cardno) {
      let data = {
        cardno: cardno
      };
      rechargeSIM(data).then(response => {
        this.$message({
          message: response.msg,
          type: "success",
          duration: 1000
        });
        this.getWtSimCard();
      });
    },
    handleUpdate(id) {
      this.$router.push({
        path: "/equipment/updateWtSimCard",
        query: { id: id }
      });
    },
    handleDelete(id) {
      this.$confirm("是否要删除该SIM卡", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        deleteWtSimCard(id).then(response => {
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



