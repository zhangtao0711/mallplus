<template>
  <el-card class="form-container" shadow="never">
    <el-form
      :model="wtWaterCardRecharge"
      :rules="rules"
      ref="WtWaterCardRechargeFrom"
      label-width="150px"
    >
      <el-form-item label="充值金额类型" prop="cardType">
        <el-radio-group v-model="wtWaterCardRecharge.cardType" @change="chooseCardType">
          <el-radio :label="0">实体卡</el-radio>
          <el-radio :label="1">虚拟卡</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="充值金额类型" prop="rechargeMoneyType">
        <el-radio-group
          v-model="wtWaterCardRecharge.rechargeMoneyType"
          @change="chooseRechargeMoneyType"
        >
          <el-radio :label="0">充值金额</el-radio>
          <el-radio :label="1" :disabled="disabled">充值体验金</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="充值方式" prop="rechargeType" v-show="rechargeMoneyType">
        <el-radio-group v-model="wtWaterCardRecharge.rechargeType" @change="chooseRechargeType">
          <el-radio :label="1">按卡号充值</el-radio>
          <!-- <el-radio :label="2">筛选充值</el-radio> -->
        </el-radio-group>
      </el-form-item>

      <!-- 按卡号充值 -->
      <div v-show="cardNo">
        <el-form-item label="起始卡号" prop="startNo">
          <el-input-number v-model="wtWaterCardRecharge.startNo" :min="0" style="width: 370px;" />
        </el-form-item>
        <el-form-item label="终止卡号" prop="endNo">
          <el-input-number v-model="wtWaterCardRecharge.endNo" :min="0" style="width: 370px;" />
        </el-form-item>
      </div>

      <!-- 充值金额 -->
      <div v-show="rechargeMoney">
        <el-form-item label="充值金额" prop="rechargeMoney">
          <el-input-number
            v-model="wtWaterCardRecharge.rechargeMoney"
            :precision="2"
            :step="0.1"
            :min="0"
            style="width: 370px;"
          />
        </el-form-item>
        <el-form-item label="实收金额" prop="receivedMoney">
          <el-input-number
            v-model="wtWaterCardRecharge.receivedMoney"
            :precision="2"
            :step="0.1"
            :min="0"
            style="width: 370px;"
          />
        </el-form-item>
      </div>

      <!-- 筛选充值 -->
      <!-- <div v-show="screening">
        <el-form-item label="会员卡余额">
          <el-input v-model="wtWaterCardRecharge.umsBalanceMark" style="width: 370px;"></el-input>
        </el-form-item>
        <el-form-item>
          <el-input-number
            v-model="wtWaterCardRecharge.umsBalanceWhere"
            :precision="2"
            :step="0.1"
            :min="0"
            style="width:370px"
          />
        </el-form-item>

        <el-form-item label="用水频次">
          <el-input v-model="wtWaterCardRecharge.umsUseMark" style="width: 370px;"></el-input>
        </el-form-item>
        <el-form-item>
          <el-input-number
            v-model="wtWaterCardRecharge.umsUseWhere"
            :precision="2"
            :step="0.1"
            :min="0"
            style="width: 370px;"
          />
        </el-form-item>
        <el-form-item>
          <el-select
            v-model="wtWaterCardRecharge.umsUsePeriod"
            style="width: 370px;"
            placeholder="请选择频次周期"
          >
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="用户推荐频次">
          <el-input v-model="wtWaterCardRecharge.umsRecommendMark" style="width: 370px;"></el-input>
        </el-form-item>
        <el-form-item>
          <el-input-number
            v-model="wtWaterCardRecharge.umsRecommendWhere"
            :precision="2"
            :step="0.1"
            :min="0"
            style="width: 370px;"
          />
        </el-form-item>
        <el-form-item>
          <el-select
            style="width: 370px;"
            v-model="wtWaterCardRecharge.umsRecommendPeriod"
            placeholder="请选择频次周期"
          >
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="用户社区" prop="umsCommunity">
          <el-select v-model="wtWaterCardRecharge.umsCommunity" placeholder="请选择">
            <el-option
              style="width: 370px;"
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="会员等级" prop="umsMemberLevel">
          <el-select v-model="wtWaterCardRecharge.umsMemberLevel" placeholder="请选择">
            <el-option
              style="width: 370px;"
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>
      </div> -->

      <!-- 体验金 -->
      <div v-show="experience">
        <el-form-item label="体验金" prop="experienceMoney">
          <el-input-number
            v-model="wtWaterCardRecharge.experienceMoney"
            :precision="2"
            :step="0.1"
            :min="0"
            style="width: 370px;"
          />
        </el-form-item>
        <el-form-item label="体验到期" prop="experienceEndType">
          <el-radio-group
            v-model="wtWaterCardRecharge.experienceEndType"
            @change="chooseExperienceEnd"
          >
            <el-radio :label="0">到期日</el-radio>
            <el-radio :label="1">有效天数</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="体验到期日" v-show="experienceDate">
          <el-date-picker
            v-model="wtWaterCardRecharge.experienceEndData"
            type="date"
            placeholder="请选择到期日"
            style="width: 370px;"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="体验有效天数" v-show="experienceDayNum">
          <el-input-number v-model="wtWaterCardRecharge.experienceEndDay" style="width: 370px;"></el-input-number>
        </el-form-item>
      </div>

      <el-form-item>
        <el-button type="primary" @click="onSubmit('WtWaterCardRechargeFrom')">提交</el-button>
        <el-button v-if="!isEdit" @click="resetForm('WtWaterCardRechargeFrom')">重置</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>
<script>
import {
  createWtWaterCardRecharge,
  updateWtWaterCardRecharge
} from "@/api/water/wtWaterCardRecharge";
import { fetchList } from "@/api/sms/smsRechargePackage";
import SingleUpload from "@/components/Upload/singleUpload";
import { get } from "@/utils/auth";

const defaultWtWaterCardRecharge = {
  rechargeMoneyType: 0,
  // rechargeType: 0,
  experienceEndDay: null,
  experienceEndData: null,
  cardType: 0,
  startNo: null,
  endNo: null
};
export default {
  name: "backstageRecharge",
  components: { SingleUpload },
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      disabled: false,
      experienceDate: false,
      experienceDayNum: false,
      cardNo: true,
      screening: false,
      experience: false,
      rechargeMoney: true,
      rechargeMoneyType: false,
      options: [
        {
          value: "选项1",
          label: "黄金糕"
        },
        {
          value: "选项2",
          label: "双皮奶"
        },
        {
          value: "选项3",
          label: "蚵仔煎"
        },
        {
          value: "选项4",
          label: "龙须面"
        },
        {
          value: "选项5",
          label: "北京烤鸭"
        }
      ],
      wtWaterCardRecharge: Object.assign({}, defaultWtWaterCardRecharge),
      rules: {
        cardNo: [{ required: true, message: "请输入卡号", trigger: "blur" }]
      }
    };
  },
  created() {
    if (this.isEdit) {
      getWtWaterCardRecharge(this.$route.query.id).then(response => {
        this.wtWaterCardRecharge = response.data;
      });
    } else {
      this.wtWaterCardRecharge = Object.assign({}, defaultWtWaterCardRecharge);
    }
  },
  methods: {
    chooseCardType(e) {
      if (e === 1) {
        this.disabled = true;
      } else {
        this.disabled = false;
      }
    },
    chooseRechargeMoneyType(e) {
      if (e === 1) {
        this.cardNo = false;
        this.wtWaterCardRecharge.startNo = null;
        this.wtWaterCardRecharge.endNo = null;
        this.rechargeMoneyType = true;
        this.rechargeMoney = false;
        this.experience = true;
      } else {
        this.cardNo = true;
        this.false = false;
        this.rechargeMoneyType = false;
        this.rechargeMoney = true;
        this.experience = false;
      }
    },
    chooseRechargeType(e) {
      if (e === 1) {
        this.screening = false;
        this.wtWaterCardRecharge.umsBalanceMark = null;
        this.wtWaterCardRecharge.umsBalanceWhere = null;
        this.wtWaterCardRecharge.umsUseMark = null;
        this.wtWaterCardRecharge.umsUseWhere = null;
        this.wtWaterCardRecharge.umsUsePeriod = null;
        this.wtWaterCardRecharge.umsRecommendMark = null;
        this.wtWaterCardRecharge.umsRecommendWhere = null;
        this.wtWaterCardRecharge.umsRecommendPeriod = null;
        this.wtWaterCardRecharge.umsCommunity = null;
        this.wtWaterCardRecharge.umsMemberLevel = null;
        this.cardNo = true;
      } else {
        this.cardNo = false;
        this.wtWaterCardRecharge.startNo = null;
        this.wtWaterCardRecharge.endNo = null;
        this.screening = true;
      }
    },
    chooseExperienceEnd(e) {
      if (e === 0) {
        this.experienceDayNum = false;
        this.experienceDate = true;
        this.wtWaterCardRecharge.experienceEndDay = null;
      } else {
        this.experienceDate = false;
        this.experienceDayNum = true;
        this.wtWaterCardRecharge.experienceEndData = null;
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
              updateWtWaterCardRecharge(
                this.$route.query.id,
                this.wtWaterCardRecharge
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
              this.wtWaterCardRecharge.createBy = get("userId");
              this.wtWaterCardRecharge.dealerId = get("storeId");
              this.wtWaterCardRecharge.storeId = get("storeId");
              createWtWaterCardRecharge(this.wtWaterCardRecharge).then(response => {
                if (response.code == 200) {
                  this.$refs[formName].resetFields();
                  this.wtWaterCardRecharge = Object.assign(
                    {},
                    defaultWtWaterCardRecharge
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
      this.wtWaterCardRecharge = Object.assign({}, defaultWtWaterCardRecharge);
    }
  }
};
</script>
<style>
</style>


