<template>
  <el-card class="form-container" shadow="never">
    <el-form :model="productCate" :rules="rules" ref="productCateFrom" label-width="170px">
      <el-form-item label="编号：" prop="name">
        <el-input v-model="productCate.name"></el-input>
      </el-form-item>

      <el-form-item label="分类名称：" prop="name">
        <el-input v-model="productCate.name"></el-input>
      </el-form-item>

      <el-form-item label="是否默认：">
        <el-radio-group v-model="productCate.default">
          <el-radio label="0">否</el-radio>
          <el-radio label="1">是</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="计费方式：">
        <el-radio-group v-model="productCate.chargeType">
          <el-radio label="0">按重量</el-radio>
          <el-radio label="1">按件数</el-radio>
        </el-radio-group>
      </el-form-item>

      <div v-if="productCate.chargeType=='1'">
        <el-form-item label="配送区域：">
          全国（默认运费）
          <el-button type="primary">+新增配送区域</el-button>
          <div>根据件数来计算运费，当物品不足《首件数量》时，按照《首件费用》计算，超过部分按照《续件重量》和《续件费用》乘积来计算。</div>
        </el-form-item>

        <el-form-item label="首件（个）：">
          <el-input v-model="productCate.firstWeight"></el-input>
        </el-form-item>

        <el-form-item label="运费（元）：">
          <el-input v-model="productCate.firstFee"></el-input>
        </el-form-item>

        <el-form-item label="续件（个）：">
          <el-input v-model="productCate.firstWeight"></el-input>
        </el-form-item>

        <el-form-item label="续费（元）：">
          <el-input v-model="productCate.firstFee"></el-input>
        </el-form-item>

        <el-form-item label="邮费满额包邮（元）：">
          <el-input v-model="productCate.continmeFee"></el-input>
        </el-form-item>
      </div>

      <h3><span style="border:solid 3px #409EFF;margin-right:5px"></span>特殊区域设置</h3>

      <el-form-item label="类型：">
        <el-radio-group v-model="productCate.area">
          <el-radio label="0">不配送区域</el-radio>
          <el-radio label="1">只配送区域</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="不配送区域：" v-if="productCate.default=='0'">
        <el-button>选择地区</el-button>
      </el-form-item>

      <el-form-item label="只配送区域：" v-if="productCate.default=='1'">
        <el-button>选择地区</el-button>
      </el-form-item>

      <el-form-item label="状态：">
        <el-radio-group v-model="productCate.state">
          <el-radio label="0">禁用</el-radio>
          <el-radio label="1">启用</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSubmit('productCateFrom')">提交</el-button>
        <el-button v-if="!isEdit" @click="resetForm('productCateFrom')">重置</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>
import {
  fetchList,
  createFeightTemplate,
  updateFeightTemplate,
  getFeightTemplate
} from "@/api/pms/feightTemplate";

const defaultFeightTemplate = {
  description: "",
  icon: "",
  keywords: "",
  name: "",
  navStatus: 0,
  parentId: 0,
  productUnit: "",
  showStatus: 0,
  indexStatus: 0,
  sort: 0,
  productAttributeIdList: []
};
export default {
  name: "FeightTemplateDetail",
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      productCate: Object.assign({}, defaultFeightTemplate),
      selectFeightTemplateList: [],
      rules: {},
      filterAttrs: [],
      filterProductAttrList: [
        {
          value: []
        }
      ]
    };
  },
  created() {
    if (this.isEdit) {
      getFeightTemplate(this.$route.query.id).then(response => {
        this.productCate = response.data;
      });
    } else {
      this.productCate = Object.assign({}, defaultFeightTemplate);
    }
  },
  methods: {
    chargeType(e) {
      if (e == "1") {
      } else if (e == "0") {
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
              updateFeightTemplate(this.$route.query.id, this.productCate).then(
                response => {
                  this.$message({
                    message: "修改成功",
                    type: "success",
                    duration: 1000
                  });
                  this.$router.back();
                }
              );
            } else {
              createFeightTemplate(this.productCate).then(response => {
                this.$refs[formName].resetFields();
                this.resetForm(formName);
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
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.productCate = Object.assign({}, defaultFeightTemplate);
    },
    removeFilterAttr(productAttributeId) {
      if (this.filterProductAttrList.length === 1) {
        this.$message({
          message: "至少要留一个",
          type: "warning",
          duration: 1000
        });
        return;
      }
      var index = this.filterProductAttrList.indexOf(productAttributeId);
      if (index !== -1) {
        this.filterProductAttrList.splice(index, 1);
      }
    },
    handleAddFilterAttr() {
      if (this.filterProductAttrList.length === 3) {
        this.$message({
          message: "最多添加三个",
          type: "warning",
          duration: 1000
        });
        return;
      }
      this.filterProductAttrList.push({
        value: null,
        key: Date.now()
      });
    }
  },
  filters: {
    filterLabelFilter(index) {
      if (index === 0) {
        return "筛选属性：";
      } else {
        return "";
      }
    }
  }
};
</script>

<style scoped></style>
