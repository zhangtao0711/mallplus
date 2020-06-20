<template>
  <div style="margin-top: 50px">
    <el-form
      :model="value"
      :rules="rules"
      ref="productInfoForm"
      label-width="120px"
      style="width: 600px"
      size="small"
    >
      <el-form-item label="排序：">
        <el-input v-model="value.sort"></el-input>
      </el-form-item>

      <el-form-item label="商品分类：" prop="productCategoryId">
        <el-cascader
          change-on-select
          v-model="selectProductCateValue"
          :options="productCateOptions"
        ></el-cascader>
      </el-form-item>

      <el-form-item label="商品名称：" prop="name">
        <el-input v-model="value.name"></el-input>
      </el-form-item>

      <el-form-item label="副标题：" prop="subTitle">
        <el-input v-model="value.subTitle"></el-input>
      </el-form-item>

      <el-form-item label="商品关键字：">
        <el-input v-model="value.keywords"></el-input>
      </el-form-item>

      <el-form-item label="商品相册：">
        <multi-upload v-model="selectProductPics"></multi-upload>
      </el-form-item>

      <el-form-item label="区域：" prop="areaId">
        <el-cascader change-on-select v-model="selectAreaValue" :options="areaOptions"></el-cascader>
      </el-form-item>

      <el-form-item label="标签" prop="tags">
        <el-select
          v-model="value.tagList"
          style="width: 437px"
          multiple
          placeholder="请选择"
          @remove-tag="deleteTag"
          @change="changeRole"
        >
          <el-option v-for="item in TagList" :key="item.name" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>

      <el-form-item label="公开价格：">
        <el-input v-model="value.originalPrice"></el-input>
      </el-form-item>

      <el-form-item label="总经销商价格：">
        <el-input v-model="value.gPrice"></el-input>
      </el-form-item>

      <el-form-item label="经销商价格：">
        <el-input v-model="value.pPrice"></el-input>
      </el-form-item>

      <el-form-item label="分销商价格：">
        <el-input v-model="value.sPrice"></el-input>
      </el-form-item>

      <el-form-item label="已售数量：">
        <el-input v-model="value.saleNum"></el-input>
      </el-form-item>

      <el-form-item label="确认收货时间：" prop="startTime">
        <el-date-picker type="datetime" placeholder="选择日期" v-model="value.startTime"></el-date-picker>
      </el-form-item>

      <el-form-item label="固定运费：">
        <el-input v-model="value.transfee" style="width: 300px"></el-input>
        <span style="margin-left: 20px">优先于运费模版</span>
      </el-form-item>

      <el-form-item label="运费模版：">
        <el-select v-model="value.feightTemplateId" placeholder="请选择运费模版">
          <el-option
            v-for="item in feightTemplateOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="支付方式：">
        <el-checkbox-group v-model="value.payStyle">
          <el-checkbox label="goods_title">
            <span>线上支付</span>
          </el-checkbox>
          <el-checkbox label="goods_price">
            <span>积分支付</span>
          </el-checkbox>
        </el-checkbox-group>
      </el-form-item>

      <el-form-item label="自动发货：">
        <el-radio-group v-model="value.automaticDelivery">
          <el-radio label="1">
            <span>是</span>
          </el-radio>
          <el-radio label="0">
            <span>否</span>
          </el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item style="text-align: center">
        <el-button type="primary" size="medium" @click="handleNext('productInfoForm')">下一步，填写商品促销</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import MultiUpload from "@/components/Upload/multiUpload";
import { fetchListWithChildren } from "@/api/productCate";
import { fetchAreaListWithChildren, getArea } from "@/api/area";
import { fetchList as fetchTagList } from "@/api/ums/umsMemberTag";
import { fetchList as fetchBrandList } from "@/api/brand";
import { fetchList as fetchStoreClassList } from "@/api/sys/sysStoreClass";

import { fetchList as fetchFeightTemplateList } from "@/api/pms/feightTemplate";
import { getProduct } from "@/api/product";
let userRoles = [];
export default {
  name: "ProductInfoDetail",
  components: { MultiUpload },
  props: {
    value: Object,
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      hasEditCreated: false,
      //选中商品分类的值
      selectProductCateValue: [],
      productCateOptions: [],
      //选中商品分类的值
      selectAreaValue: [],
      areaOptions: [],
      brandOptions: [],
      storeClassOptions: [],
      area: null,
      TagList: [],
      areaList: [],
      feightTemplateOptions: [],
      unitOptions: [
        {
          id: "个",
          name: "个"
        },
        {
          id: "箱",
          name: "箱"
        },
        {
          id: "条",
          name: "条"
        },

        {
          id: "盒",
          name: "盒"
        },
        {
          id: "包",
          name: "包"
        },
        {
          id: "斤",
          name: "斤"
        },
        {
          id: "两",
          name: "两"
        },
        {
          id: "件",
          name: "件"
        },
        {
          id: "瓶",
          name: "瓶"
        },
        {
          id: "千克",
          name: "千克"
        }
      ],
      rules: {
        name: [
          { required: true, message: "请输入商品名称", trigger: "blur" },
          {
            min: 2,
            max: 140,
            message: "长度在 2 到 140 个字符",
            trigger: "blur"
          }
        ],
        subTitle: [
          { required: true, message: "请输入商品副标题", trigger: "blur" }
        ],
        productCategoryId: [
          { required: true, message: "请选择商品分类", trigger: "blur" }
        ],

        brandId: [
          { required: true, message: "请选择商品品牌", trigger: "blur" }
        ],
        description: [
          { required: true, message: "请输入商品介绍", trigger: "blur" }
        ],
        requiredProp: [
          { required: true, message: "该项为必填项", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getProductCateList();
    this.getAreaList();
    this.getBrandList();
    this.getTagList();
    this.getFeightTemplateList();
    this.getStoreClassList();
  },
  computed: {
    //商品的编号
    productId() {
      return this.value.id;
    },
    //商品的主图和画册图片
    selectProductPics: {
      get: function() {
        let pics = [];
        if (
          this.value.pic === undefined ||
          this.value.pic == null ||
          this.value.pic === ""
        ) {
          return pics;
        }
        pics.push(this.value.pic);
        if (
          this.value.albumPics === undefined ||
          this.value.albumPics == null ||
          this.value.albumPics === ""
        ) {
          return pics;
        }
        let albumPics = this.value.albumPics.split(",");
        for (let i = 0; i < albumPics.length; i++) {
          pics.push(albumPics[i]);
        }
        return pics;
      },
      set: function(newValue) {
        if (newValue == null || newValue.length === 0) {
          this.value.pic = null;
          this.value.albumPics = null;
        } else {
          this.value.pic = newValue[0];
          this.value.albumPics = "";
          if (newValue.length > 1) {
            for (let i = 1; i < newValue.length; i++) {
              this.value.albumPics += newValue[i];
              if (i !== newValue.length - 1) {
                this.value.albumPics += ",";
              }
            }
          }
        }
      }
    }
  },
  watch: {
    productId: function(newValue) {
      if (!this.isEdit) return;
      if (this.hasEditCreated) return;
      console.log(newValue);
      if (newValue === undefined || newValue == null || newValue === 0) return;
      this.handleEditCreated();
    },
    selectProductCateValue: function(newValue) {
      if (newValue != null) {
        this.value.productCategoryId =
          newValue[newValue.length - 1 > 0 ? newValue.length - 1 : 0];
        this.value.productCategoryName = this.getCateNameById(
          this.value.productCategoryId
        );
      } else {
        this.value.productCategoryId = null;
        this.value.productCategoryName = null;
      }
    },
    selectAreaValue: function(newValue) {
      if (newValue != null) {
        console.log(newValue);
        this.value.areaId =
          newValue[newValue.length - 1 > 0 ? newValue.length - 1 : 0];
        getArea(this.value.areaId).then(response => {
          this.value.areaName = response.data.name;
        });
      } else {
        this.value.areaId = null;
        this.value.areaName = null;
      }
    }
  },
  methods: {
    changeRole(value) {
      userRoles = [];
      value.forEach(function(data, index) {
        const role = { id: data };
        userRoles.push(role);
      });
    },
    deleteTag(value) {
      userRoles.forEach(function(data, index) {
        if (data.id === value) {
          userRoles.splice(index, value);
        }
      });
    },
    afterErrorMethod(crud) {
      // 恢复select
      const initRoles = [];
      userRoles.forEach(function(role, index) {
        initRoles.push(role.id);
      });
      this.value.tagList = initRoles;
    },
    //处理编辑逻辑
    handleEditCreated() {
      if (this.value.productCategoryId != null) {
        this.selectProductCateValue.push(this.value.cateParentId);
        this.selectProductCateValue.push(this.value.productCategoryId);
      }
      console.log(this.selectProductCateValue);
      if (this.value.areaId != null) {
        getArea(this.value.areaId).then(response => {
          this.selectAreaValue.push(response.data.pid);
          this.selectAreaValue.push(this.value.areaId);
        });
      }
      console.log(this.selectAreaValue);
      userRoles = [];
      const roles = [];
      this.value.tagList.forEach(function(role, index) {
        roles.push(role.id);
        // 初始化编辑时候的角色
        const rol = { id: role.id };
        userRoles.push(rol);
      });
      console.log(this.value.tagList);
      this.value.tagList = roles;
      this.hasEditCreated = true;
    },

    getAreaList() {
      fetchAreaListWithChildren().then(response => {
        let list = response.data;
        this.areaList = list;
        this.areaOptions = [];
        for (let i = 0; i < list.length; i++) {
          let children = [];
          if (list[i].children != null && list[i].children.length > 0) {
            for (let j = 0; j < list[i].children.length; j++) {
              children.push({
                label: list[i].children[j].name,
                value: list[i].children[j].id
              });
            }
          }
          this.areaOptions.push({
            label: list[i].name,
            value: list[i].id,
            children: children
          });
        }
      });
    },

    getCateNameById(id) {
      let name = null;

      for (let i = 0; i < this.productCateOptions.length; i++) {
        for (let j = 0; i < this.productCateOptions[i].children.length; j++) {
          if (
            this.productCateOptions[i].children[j].value != undefined &&
            this.productCateOptions[i].children[j].value === id
          ) {
            name = this.productCateOptions[i].children[j].label;
            return name;
          }
        }
      }
      return name;
    },
    getProductCateList() {
      fetchListWithChildren().then(response => {
        let list = response.data;
        this.productCateOptions = [];
        for (let i = 0; i < list.length; i++) {
          let children = [];
          if (list[i].children != null && list[i].children.length > 0) {
            for (let j = 0; j < list[i].children.length; j++) {
              children.push({
                label: list[i].children[j].name,
                value: list[i].children[j].id
              });
            }
          }
          this.productCateOptions.push({
            label: list[i].name,
            value: list[i].id,
            children: children
          });
        }
      });
    },
    getFeightTemplateList() {
      fetchFeightTemplateList({ pageNum: 1, pageSize: 100 }).then(response => {
        this.feightTemplateOptions = [];
        let feightTemplateList = response.data.records;
        for (let i = 0; i < feightTemplateList.length; i++) {
          this.feightTemplateOptions.push({
            label: feightTemplateList[i].name,
            value: feightTemplateList[i].id
          });
        }
      });
    },
    getBrandList() {
      fetchBrandList({ pageNum: 1, pageSize: 100 }).then(response => {
        this.brandOptions = [];
        let brandList = response.data.records;
        for (let i = 0; i < brandList.length; i++) {
          this.brandOptions.push({
            label: brandList[i].name,
            value: brandList[i].id
          });
        }
      });
    },
    getStoreClassList() {
      fetchStoreClassList({ pageNum: 1, pageSize: 100 }).then(response => {
        this.storeClassOptions = [];
        let brandList = response.data.records;
        for (let i = 0; i < brandList.length; i++) {
          this.storeClassOptions.push({
            label: brandList[i].name,
            value: brandList[i].id
          });
        }
      });
    },

    getTagList() {
      fetchTagList({ pageNum: 1, pageSize: 100, type: 2, status: 1 }).then(
        response => {
          this.TagList = response.data.records;
        }
      );
    },

    handleNext(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.$emit("nextStep");
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

    handleBrandChange(val) {
      let brandName = "";
      for (let i = 0; i < this.brandOptions.length; i++) {
        if (this.brandOptions[i].value === val) {
          brandName = this.brandOptions[i].label;
          break;
        }
      }
      this.value.brandName = brandName;
    }
  }
};
</script>

<style scoped>
</style>
