<template>
  <el-card class="form-container" shadow="never">
    <el-button class="btn-add" @click="addWtFilterElementType()" size="small" type="primary">添加滤芯类型</el-button>
    <el-form :model="wtFilterElement" :rules="rules" ref="WtFilterElementFrom" label-width="150px">
      <el-form-item label="滤芯名称" prop="name">
        <el-input v-model="wtFilterElement.name" placeholder="请输入滤芯名称" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="滤芯级别" prop="filterElementLevel">
        <el-input
          v-model="wtFilterElement.filterElementLevel"
          placeholder="请选择滤芯级别"
          style="width: 370px;"
        />
      </el-form-item>

      <el-form-item label="计费模式" prop="billingMode">
        <el-input
          v-model="wtFilterElement.billingMode"
          placeholder="请选择计费模式"
          style="width: 370px;"
        />
      </el-form-item>

      <el-form-item label="滤芯更换时间" prop="changeTime">
        <el-date-picker
          v-model="wtFilterElement.changeTime"
          type="date"
          style="width: 370px;"
          placeholder="请选择滤芯更换时间"
        ></el-date-picker>
      </el-form-item>

      <el-form-item label="备注" prop="remarks">
        <el-input
          type="textarea"
          :rows="2"
          placeholder="请输入备注"
          style="width: 370px;"
          v-model="wtFilterElement.remarks"
        ></el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSubmit('WtFilterElementFrom')">提交</el-button>
        <el-button v-if="!isEdit" @click="resetForm('WtFilterElementFrom')">重置</el-button>
      </el-form-item>
    </el-form>

    <div>
      <el-dialog title="添加滤芯类型" :visible.sync="blance.dialogVisible" width="40%">
        <el-form :model="blance" :rules="blanceRules" ref="brandFrom" label-width="200px">
          <el-form-item label="滤芯类型名称" prop="filterElementTypeName">
            <el-input v-model="blance.filterElementTypeName" style="width: 370px;" />
          </el-form-item>

          <el-form-item label="更换周期（天数）" prop="changeCycle">
            <el-input-number v-model="blance.changeCycle" style="width: 370px;" />
          </el-form-item>

          <el-form-item label="更换滤芯提前提醒天数" prop="remindDay">
            <el-input-number v-model="blance.remindDay" style="width: 370px;" />
          </el-form-item>

          <el-form-item label="净水总量（吨）" prop="purifierTotal">
            <el-input-number v-model="blance.purifierTotal" style="width: 370px;" />
          </el-form-item>

          <el-form-item>
            <el-button @click="blance.dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="handleEditBlance('brandFrom')">确 定</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>
    </div>
  </el-card>
</template>
<script>
import {
  createWtFilterElement,
  getWtFilterElement,
  updateWtFilterElement
} from "@/api/water/wtFilterElement";
import { get } from "@/utils/auth";
import { createWtFilterElementType } from "@/api/water/wtFilterElementType";

const defaultWtFilterElement = {};
const defaultBlance = {
  dialogVisible: false,
  id: null
};
export default {
  name: "WtFilterElementDetail",
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      blanceRules: {
        filterElementTypeName: [
          { required: true, message: "请输入滤芯名称名称", trigger: "blur" }
        ],
        changeCycle: [
          { required: true, message: "请输入更换周期", trigger: "blur" },
          { type: "number", message: "更换周期必须为数字", trigger: "blur" }
        ],
        remindDay: [
          {
            required: true,
            message: "请输入更换滤芯提前提醒天数",
            trigger: "blur"
          },
          { type: "number", message: "更换滤芯提前提醒天数必须为数字" }
        ],
        purifierTotal: [
          {
            required: true,
            message: "请输入净水总量",
            trigger: "blur"
          },
          { type: "number", message: "净水总量必须为数字" }
        ]
      },
      blance: Object.assign({}, defaultBlance),
      wtFilterElement: Object.assign({}, defaultWtFilterElement),
      rules: {
        name: [{ required: true, message: "请输入滤芯名称", trigger: "blur" }],
        filterElementLevel: [
          { required: true, message: "请选择滤芯级别", trigger: "blur" }
        ],
        billingMode: [
          { required: true, message: "请选择计费模式", trigger: "blur" }
        ],
        changeTime: [
          { required: true, message: "请选择滤芯更换时间", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    if (this.isEdit) {
      getWtFilterElement(this.$route.query.id).then(response => {
        this.wtFilterElement = response.data;
      });
    } else {
      this.wtFilterElement = Object.assign({}, defaultWtFilterElement);
    }
  },
  methods: {
    handleEditBlance(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.$confirm("是否提交数据", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(() => {
            let data = {
              delFlag: 1,
              createBy: get("userId"),
              dealerId: get("userId"),
              filterElementTypeName: this.blance.filterElementTypeName,
              changeCycle: this.blance.changeCycle,
              remindDay: this.blance.remindDay,
              purifierTotal: this.blance.purifierTotal
            };
            createWtFilterElementType(data).then(response => {
              this.$refs[formName].resetFields();
              this.blance = Object.assign({}, defaultBlance);
              this.$message({
                message: "提交成功",
                type: "success",
                duration: 1000
              });
            });
            this.blance.dialogVisible = false;
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
    addWtFilterElementType() {
      this.blance.dialogVisible = true;
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
              this.wtFilterElement.updateBy = get("userId");
              updateWtFilterElement(
                this.$route.query.id,
                this.wtFilterElement
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
              this.wtFilterElement.delFlag = 1;
              this.wtFilterElement.createBy = get("userId");
              createWtFilterElement(this.wtFilterElement).then(response => {
                if (response.code == 200) {
                  this.$refs[formName].resetFields();
                  this.wtFilterElement = Object.assign(
                    {},
                    defaultWtFilterElement
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
      this.wtFilterElement = Object.assign({}, defaultWtFilterElement);
    }
  }
};
</script>
<style>
</style>


