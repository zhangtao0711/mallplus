<template>
  <div style="margin-top: 50px">
    <el-form
      :model="value"
      ref="productAttrForm"
      label-width="150px"
      style="width: 720px"
      size="small"
    >
      <el-form-item label="商品编码：">
        <el-input v-model="value.productSn"></el-input>
      </el-form-item>

      <el-form-item label="商品条码：">
        <el-input v-model="value.barcode"></el-input>
      </el-form-item>

      <el-form-item label="商品重量：">
        <el-input v-model="value.weight" style="width: 300px"></el-input>
        <!-- <span style="margin-left: 20px">千克</span> -->
      </el-form-item>

      <el-form-item label="商品库存：">
        <el-input v-model="value.stock"></el-input>

        <el-radio-group v-model="value.inventoryReduction" size="small">
          <el-radio-button label="0">拍下减库存</el-radio-button>
          <el-radio-button label="1">付款减库存</el-radio-button>
          <el-radio-button label="2">永不减库存</el-radio-button>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="启用商品规格：">
        <el-radio-group v-model="value.enableSpecification" size="small">
          <el-radio-button label="0">否</el-radio-button>
          <el-radio-button label="1">是</el-radio-button>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="商品规格：">
        <el-card shadow="never" class="cardBg">
          <!-- <div v-for="(productAttr,idx) in selectProductAttr" :key="idx"> -->
            <!-- {{productAttr.name}}： -->
            颜色：
            <!-- <el-checkbox-group
              v-if="productAttr.handAddStatus===0"
              v-model="selectProductAttr[idx].values"
            >
              <el-checkbox
                v-for="item in getInputListArr(productAttr.inputList)"
                :label="item"
                :key="item"
                class="littleMarginLeft"
              ></el-checkbox>
            </el-checkbox-group>
            <div v-else>-->
            <el-checkbox-group>
              <div
                v-for="(item,index) in selectProductAttr"
                :key="index"
                style="display: inline-block"
                class="littleMarginLeft"
              >
                <el-checkbox :label="item" :key="item"></el-checkbox>
                <el-button
                  type="text"
                  class="littleMarginLeft"
                  @click="handleRemoveProductAttrValue(index)"
                >删除</el-button>
              </div>
            </el-checkbox-group>
            <el-input
              v-model="addProductAttrValue"
              style="width: 160px;margin-left: 10px"
              clearable
            ></el-input>
            <el-button class="littleMarginLeft" @click="handleAddProductAttrValue()">增加</el-button>
            <!-- </div> -->
          <!-- </div> -->
        </el-card>

        <el-table style="width: 100%;margin-top: 20px" :data="value.skuStockList" border>
          <el-table-column
            v-for="(item,index) in selectProductAttr"
            :label="item.name"
            :key="item.id"
            align="center"
          >
            <template slot-scope="scope">{{getProductSkuSp(scope.row,index)}}</template>
          </el-table-column>

          <el-table-column label="商品库存" width="80" align="center">
            <template slot-scope="scope">
              <el-input v-model="scope.row.stock"></el-input>
            </template>
          </el-table-column>

          <el-table-column label="公开价格" width="80" align="center">
            <template slot-scope="scope">
              <el-input v-model="scope.row.price"></el-input>
            </template>
          </el-table-column>

          <el-table-column label="总经销商价格" width="80" align="center">
            <template slot-scope="scope">
              <el-input v-model="scope.row.price"></el-input>
            </template>
          </el-table-column>

          <el-table-column label="经销商价格" width="80" align="center">
            <template slot-scope="scope">
              <el-input v-model="scope.row.price"></el-input>
            </template>
          </el-table-column>

          <el-table-column label="分销商价格" width="80" align="center">
            <template slot-scope="scope">
              <el-input v-model="scope.row.price"></el-input>
            </template>
          </el-table-column>

          <el-table-column label="编码" width="80" align="center">
            <template slot-scope="scope">
              <el-input v-model="scope.row.lowStock"></el-input>
            </template>
          </el-table-column>

          <el-table-column label="条码" width="80" align="center">
            <template slot-scope="scope">
              <el-input v-model="scope.row.price"></el-input>
            </template>
          </el-table-column>

          <el-table-column label="重量" width="80" align="center">
            <template slot-scope="scope">
              <el-input v-model="scope.row.price"></el-input>
            </template>
          </el-table-column>

          <el-table-column label="操作" width="80" align="center">
            <template slot-scope="scope">
              <el-button type="text" @click="handleRemoveProductSku(scope.$index, scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-button type="primary" style="margin-top: 20px" @click="handleRefreshProductSkuList">刷新列表</el-button>
        <!-- <el-button type="primary" style="margin-top: 20px" @click="handleSyncProductSkuPrice">同步价格</el-button> -->
      </el-form-item>

      <el-form-item style="text-align: center">
        <el-button size="medium" @click="handlePrev">上一步，填写商品促销</el-button>
        <el-button type="primary" size="medium"  @click="handleFinishCommit">完成，提交商品</el-button>
        
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { fetchList as fetchProductAttrCateList } from "@/api/productAttrCate";
import { fetchList as fetchProductAttrList } from "@/api/productAttr";
import SingleUpload from "@/components/Upload/singleUpload";
import MultiUpload from "@/components/Upload/multiUpload";
import Tinymce from "@/components/Tinymce";

export default {
  name: "ProductAttrDetail",
  components: { SingleUpload, MultiUpload, Tinymce },
  props: {
    value: Object,
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      //编辑模式时是否初始化成功
      hasEditCreated: false,
      //选中的商品属性
      selectProductAttr: [],
      //选中的商品参数
      selectProductParam: [],
      //选中的商品属性图片
      selectProductAttrPics: [],
      //可手动添加的商品属性
      addProductAttrValue: "",
      //商品富文本详情激活类型
      activeHtmlName: "pc"
    };
  },
  computed: {
    //是否有商品属性图片
    hasAttrPic() {
      if (this.selectProductAttrPics.length < 1) {
        return false;
      }
      return true;
    },
    //商品的编号
    productId() {
      return this.value.id;
    }
  },
  created() {},
  watch: {
    productId: function(newValue) {
      if (!this.isEdit) return;
      if (this.hasEditCreated) return;
      if (newValue === undefined || newValue == null || newValue === 0) return;
      this.handleEditCreated();
    }
  },
  methods: {
    handleEditCreated() {
      //根据商品属性分类id获取属性和参数
      if (this.value.productAttributeCategoryId != null) {
        this.handleProductAttrChange(this.value.productAttributeCategoryId);
      }
      this.hasEditCreated = true;
    },
    getProductAttrList(type, cid) {
      let param = { pageNum: 1, pageSize: 3, type: type };
      fetchProductAttrList(cid, param).then(response => {
        let list = response.data.records;
        if (type === 0) {
          this.selectProductAttr = [];
          for (let i = 0; i < list.length; i++) {
            let options = [];
            let values = [];
            if (this.isEdit) {
              if (list[i].handAddStatus === 1) {
                //编辑状态下获取手动添加编辑属性
                options = this.getEditAttrOptions(list[i].id);
              }
              //编辑状态下获取选中属性
              values = this.getEditAttrValues(i);
            }
            this.selectProductAttr.push({
              id: list[i].id,
              name: list[i].name,
              handAddStatus: list[i].handAddStatus,
              inputList: list[i].inputList,
              values: values,
              options: options
            });
          }
          if (this.isEdit) {
            //编辑模式下刷新商品属性图片
            this.refreshProductAttrPics();
          }
        } else {
          this.selectProductParam = [];
          for (let i = 0; i < list.length; i++) {
            let value = null;
            if (this.isEdit) {
              //编辑模式下获取参数属性
              value = this.getEditParamValue(list[i].id);
            }
            this.selectProductParam.push({
              id: list[i].id,
              name: list[i].name,
              value: value,
              inputType: list[i].inputType,
              inputList: list[i].inputList
            });
          }
        }
      });
    },
    //获取设置的可手动添加属性值
    getEditAttrOptions(id) {
      let options = [];
      for (let i = 0; i < this.value.productAttributeValueList.length; i++) {
        let attrValue = this.value.productAttributeValueList[i];
        if (attrValue.productAttributeId === id) {
          let strArr = attrValue.value.split(",");
          for (let j = 0; j < strArr.length; j++) {
            options.push(strArr[j]);
          }
          break;
        }
      }
      return options;
    },
    //获取选中的属性值
    getEditAttrValues(index) {
      let values = [];
      if (index === 0) {
        for (let i = 0; i < this.value.skuStockList.length; i++) {
          let sku = this.value.skuStockList[i];
          if (sku.sp1 != null && values.indexOf(sku.sp1) === -1) {
            values.push(sku.sp1);
          }
        }
      } else if (index === 1) {
        for (let i = 0; i < this.value.skuStockList.length; i++) {
          let sku = this.value.skuStockList[i];
          if (sku.sp2 != null && values.indexOf(sku.sp2) === -1) {
            values.push(sku.sp2);
          }
        }
      } else {
        for (let i = 0; i < this.value.skuStockList.length; i++) {
          let sku = this.value.skuStockList[i];
          if (sku.sp3 != null && values.indexOf(sku.sp3) === -1) {
            values.push(sku.sp3);
          }
        }
      }

      return values;
    },
    //获取属性的值
    getEditParamValue(id) {
      for (let i = 0; i < this.value.productAttributeValueList.length; i++) {
        if (id === this.value.productAttributeValueList[i].productAttributeId) {
          return this.value.productAttributeValueList[i].value;
        }
      }
    },
    handleProductAttrChange(value) {
      this.getProductAttrList(0, value);
      this.getProductAttrList(1, value);
    },
    getInputListArr(inputList) {
      return inputList.split(",");
    },
    handleAddProductAttrValue() {
      let options = this.selectProductAttr;
      if (this.addProductAttrValue == null || this.addProductAttrValue == "") {
        this.$message({
          message: "属性值不能为空",
          type: "warning",
          duration: 1000
        });
        return;
      }
      if (options.indexOf(this.addProductAttrValue) !== -1) {
        this.$message({
          message: "属性值不能重复",
          type: "warning",
          duration: 1000
        });
        return;
      }
      this.selectProductAttr.push(this.addProductAttrValue);
      console.log(this.selectProductAttr)
      this.addProductAttrValue = null;
    },
    handleRemoveProductAttrValue(idx, index) {
      this.selectProductAttr[idx].options.splice(index, 1);
    },
    getProductSkuSp(row, index) {
      if (index === 0) {
        return row.sp1;
      } else if (index === 1) {
        return row.sp2;
      } else {
        return row.sp3;
      }
    },
    handleRefreshProductSkuList() {
      this.$confirm("是否要刷新列表", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        // this.refreshProductAttrPics();
        this.refreshProductSkuList();
      });
    },
    handleSyncProductSkuPrice() {
      this.$confirm("将同步第一个sku的价格到所有sku,是否继续", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        if (
          this.value.skuStockList !== null &&
          this.value.skuStockList.length > 0
        ) {
          let price = this.value.skuStockList[0].price;
          for (let i = 0; i < this.value.skuStockList.length; i++) {
            this.value.skuStockList[i].price = price;
          }
        }
      });
    },
    refreshProductSkuList() {
      this.value.skuStockList = [];
      let skuList = this.value.skuStockList;
      //只有一个属性时
      if (this.selectProductAttr.length === 1) {
        let values = this.selectProductAttr[0].values;
        for (let i = 0; i < values.length; i++) {
          skuList.push({
            sp1: values[i]
          });
        }
      } else if (this.selectProductAttr.length === 2) {
        let values0 = this.selectProductAttr[0].values;
        let values1 = this.selectProductAttr[1].values;
        for (let i = 0; i < values0.length; i++) {
          if (values1.length === 0) {
            skuList.push({
              sp1: values0[i]
            });
            continue;
          }
          for (let j = 0; j < values1.length; j++) {
            skuList.push({
              sp1: values0[i],
              sp2: values1[j]
            });
          }
        }
      } else {
        let values0 = this.selectProductAttr[0].values;
        let values1 = this.selectProductAttr[1].values;
        let values2 = this.selectProductAttr[2].values;
        for (let i = 0; i < values0.length; i++) {
          if (values1.length === 0) {
            skuList.push({
              sp1: values0[i]
            });
            continue;
          }
          for (let j = 0; j < values1.length; j++) {
            if (values2.length === 0) {
              skuList.push({
                sp1: values0[i],
                sp2: values1[j]
              });
              continue;
            }
            for (let k = 0; k < values2.length; k++) {
              skuList.push({
                sp1: values0[i],
                sp2: values1[j],
                sp3: values2[k]
              });
            }
          }
        }
      }
    },
    refreshProductAttrPics() {
      this.selectProductAttrPics = [];
      if (this.selectProductAttr.length >= 1) {
        let values = this.selectProductAttr[0].values;
        for (let i = 0; i < values.length; i++) {
          let pic = null;
          if (this.isEdit) {
            //编辑状态下获取图片
            pic = this.getProductSkuPic(values[i]);
          }
          this.selectProductAttrPics.push({ name: values[i], pic: pic });
        }
      }
    },
    //获取商品相关属性的图片
    getProductSkuPic(name) {
      for (let i = 0; i < this.value.skuStockList.length; i++) {
        if (name === this.value.skuStockList[i].sp1) {
          return this.value.skuStockList[i].pic;
        }
      }
      return null;
    },
    //合并商品属性
    mergeProductAttrValue() {
      this.value.productAttributeValueList = [];
      for (let i = 0; i < this.selectProductAttr.length; i++) {
        let attr = this.selectProductAttr[i];
        if (
          attr.handAddStatus === 1 &&
          attr.options != null &&
          attr.options.length > 0
        ) {
          this.value.productAttributeValueList.push({
            productAttributeId: attr.id,
            name: attr.name,
            type: 1,
            value: this.getOptionStr(attr.options)
          });
        }
      }
      for (let i = 0; i < this.selectProductParam.length; i++) {
        let param = this.selectProductParam[i];
        this.value.productAttributeValueList.push({
          productAttributeId: param.id,
          name: param.name,
          type: 2,
          value: param.value
        });
      }
    },
    //合并商品属性图片
    mergeProductAttrPics() {
      for (let i = 0; i < this.selectProductAttrPics.length; i++) {
        for (let j = 0; j < this.value.skuStockList.length; j++) {
          if (
            this.value.skuStockList[j].sp1 ===
            this.selectProductAttrPics[i].name
          ) {
            this.value.skuStockList[j].pic = this.selectProductAttrPics[i].pic;
          }
        }
      }
    },
    getOptionStr(arr) {
      let str = "";
      for (let i = 0; i < arr.length; i++) {
        str += arr[i];
        if (i != arr.length - 1) {
          str += ",";
        }
      }
      return str;
    },
    handleRemoveProductSku(index, row) {
      let list = this.value.skuStockList;
      if (list.length === 1) {
        list.pop();
      } else {
        list.splice(index, 1);
      }
    },
    getParamInputList(inputList) {
      return inputList.split(",");
    },
    handlePrev() {
      this.$emit("prevStep");
    },
    handleFinishCommit(){
        this.$emit('finishCommit',this.isEdit);
      }
  }
};
</script>

<style scoped>
.littleMarginLeft {
  margin-left: 10px;
}

.littleMarginTop {
  margin-top: 10px;
}

.paramInput {
  width: 250px;
}

.paramInputLabel {
  display: inline-block;
  width: 100px;
  text-align: right;
  padding-right: 10px;
}

.cardBg {
  background: #f8f9fc;
}
</style>
