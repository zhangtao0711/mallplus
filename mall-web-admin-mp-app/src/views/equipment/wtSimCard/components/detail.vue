<template>
  <el-card class="form-container" shadow="never">
    <el-form :model="wtSimCard" :rules="rules" ref="WtSimCardFrom" label-width="150px">
      <el-form-item label="卡号" prop="cardno">
        <el-input v-model="wtSimCard.cardno" placeholder="请输入卡号" style="width: 370px;" />
        <el-button type="primary" @click="getChaxun">查询</el-button>
      </el-form-item>

      <el-form-item label="卡状态" prop="state">
        <el-select
          v-model="wtSimCard.state"
          :disabled="true"
          style="width: 370px;"
          placeholder="请选择卡状态"
        >
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="使用流量" prop="cardno">
        <el-input
          v-model="wtSimCard.used"
          :disabled="true"
          placeholder="使用流量"
          style="width: 370px;"
        />
      </el-form-item>

      <el-form-item label="卡有效期" prop="expiredAt">
        <el-date-picker
          :disabled="true"
          v-model="wtSimCard.expiredAt"
          style="width: 370px;"
          type="date"
          placeholder="请选择卡有效期"
        ></el-date-picker>
      </el-form-item>

      <el-form-item label="是否预充值" prop="prechargeType">
        <el-select
          v-model="wtSimCard.prechargeType"
          style="width: 370px;"
          @change="changePrechargeType"
          placeholder="请选择是否预充值"
        >
          <el-option label="是" value="1"></el-option>
          <el-option label="否" value="0"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="预充值生效日期" v-show="isShow" prop="prechargeOffsetsTime">
        <el-select
          v-model="wtSimCard.prechargeOffsetsTime"
          style="width: 370px;"
          placeholder="请选择预充值生效日期"
        >
          <el-option label="当月" value="0"></el-option>
          <el-option label="次月" value="1"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSubmit('WtSimCardFrom')">提交</el-button>
        <el-button v-if="!isEdit" @click="resetForm('WtSimCardFrom')">重置</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>
<script>
import {
  createWtSimCard,
  getWtSimCard,
  updateWtSimCard,
  getChaxun
} from "@/api/water/wtSimCard";
import { get } from "@/utils/auth";
import { formatDate } from "@/utils/date";

const defaultWtSimCard = {};
export default {
  name: "WtSimCardDetail",
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
          value: "0",
          label: "未知"
        },
        {
          value: "00",
          label: "正常"
        },
        {
          value: "01",
          label: "单向停机"
        },
        {
          value: "02",
          label: "停机"
        },
        {
          value: "03",
          label: "预销号"
        },
        {
          value: "04",
          label: "销号/拆机"
        },
        {
          value: "05",
          label: "过户"
        },
        {
          value: "06",
          label: "休眠"
        },
        {
          value: "07",
          label: "待激"
        },
        {
          value: "08",
          label: "已停用"
        },
        {
          value: "09",
          label: "库存"
        },
        {
          value: "10",
          label: "已失效"
        },
        {
          value: "11",
          label: "违章停机"
        },
        {
          value: "12",
          label: "挂失"
        },
        {
          value: "13",
          label: "用户报停"
        },
        {
          value: "14",
          label: "表示断网"
        },
        {
          value: "99",
          label: "不存在"
        }
      ],
      isShow: false,
      wtSimCard: Object.assign({}, defaultWtSimCard),
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
      getWtSimCard(this.$route.query.id).then(response => {
        this.wtSimCard = response.data;
        if (this.wtSimCard.prechargeType === "1") {
          this.isShow = true;
        }
      });
    } else {
      this.wtSimCard = Object.assign({}, defaultWtSimCard);
    }
  },
  methods: {
    getChaxun() {
      getChaxun(this.wtSimCard.cardno).then(response => {
        if (response.data) {
          const data = response.data.data;
          this.$message({
            message: response.data.msg,
            type: "success",
            duration: 1000
          });

          this.wtSimCard.state = String(data.state);
          this.wtSimCard.used = data.used;
          this.wtSimCard.expiredAt = data.expired_at;
        } else {
          this.$message({
            message: "请求超时，请重新查询",
            type: "warning",
            duration: 1000
          });
        }

        console.log(this.wtSimCard);
      });
    },
    changePrechargeType(e) {
      if (e === "1") {
        this.isShow = true;
      } else {
        this.isShow = false;
        this.wtSimCard.prechargeOffsetsTime = null
      }
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
              updateWtSimCard(this.$route.query.id, this.wtSimCard).then(
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
            } else {
              this.wtSimCard.delFlag = 1;
              this.wtSimCard.createBy = get("userId");

              let date = new Date(this.wtSimCard.expiredAt);
              this.wtSimCard.expiredAt = formatDate(
                date,
                "yyyy-MM-dd hh:mm:ss"
              );
              createWtSimCard(this.wtSimCard).then(response => {
                if (response.code == 200) {
                  this.$refs[formName].resetFields();
                  this.wtSimCard = Object.assign({}, defaultWtSimCard);
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
      this.wtSimCard = Object.assign({}, defaultWtSimCard);
    }
  }
};
</script>
<style>
</style>


