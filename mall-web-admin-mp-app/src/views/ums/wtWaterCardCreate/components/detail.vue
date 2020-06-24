<template>
  <el-card class="form-container" shadow="never">
    <el-form
      :model="wtWaterCardCreate"
      :rules="rules"
      ref="WtWaterCardCreateFrom"
      label-width="150px"
    >
      <!-- <el-form-item label="id" prop="id">
        <el-input v-model="wtWaterCardCreate.id" style="width: 370px;" />
      </el-form-item>-->

      <el-form-item label="代号（英文字符大写）" prop="code">
        <el-input v-model="wtWaterCardCreate.code" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="起始卡号" prop="startNo">
        <el-input-number
          v-model="wtWaterCardCreate.startNo"
          @change="startNo"
          style="width: 370px;"
        />
      </el-form-item>

      <el-form-item label="终止卡号" prop="endNo">
        <el-input-number v-model="wtWaterCardCreate.endNo" @change="endNo" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="数量" prop="number">
        <el-input v-model="wtWaterCardCreate.number" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="关联公众号id" prop="acid">
        <!-- <el-input v-model="wtWaterCardCreate.acid" style="width: 370px;" /> -->
        <el-input placeholder="请选择关联公众号" v-model="wtWaterCardCreate.acid" class="input-with-select">
          <el-button slot="append" @click="chooseZ" icon="el-icon-search">选择</el-button>
        </el-input>
      </el-form-item>

      <el-form-item label="识别码" prop="distinguishNum">
        <el-input
          v-model="wtWaterCardCreate.distinguishNum"
          placeholder="识别码自动生成"
          :disabled="true"
          style="width: 370px;"
        />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSubmit('WtWaterCardCreateFrom')">提交</el-button>
        <el-button v-if="!isEdit" @click="resetForm('WtWaterCardCreateFrom')">重置</el-button>
      </el-form-item>
    </el-form>

    <el-dialog title="数据选择器" :visible.sync="blance.dialogVisible" width="40%">
      <el-table
        ref="merchatBusinessMaterialsTable"
        :data="list"
        style="width: 100%;"
        v-loading="listLoading"
        border
      >
        <el-table-column prop="acid" label="公众号id" width="80" align="center">
          <template slot-scope="scope">{{ scope.row.acid }}</template>
        </el-table-column>

        <el-table-column prop="realname" label="公众号名称 " align="center">
          <template slot-scope="scope">{{ scope.row.name }}</template>
        </el-table-column>

        <el-table-column label="操作" width="150" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleChoose(scope.$index, scope.row)">选择</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="batch-operate-container"></div>
      <div style="text-align:right">
        <el-pagination
          background
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          layout="total, sizes,prev, pager, next,jumper"
          :page-size="listQuery.pageSize"
          :page-sizes="[5, 10, 15]"
          :current-page.sync="listQuery.pageNum"
          :total="total"
        ></el-pagination>
      </div>
      <div style="text-align:right;margin-top:20px">
        <el-button type="primary" @click="handleEditBlance">确 定</el-button>
      </div>
    </el-dialog>
  </el-card>
</template>
<script>
import {
  createWtWaterCardCreate,
  getWtWaterCardCreate,
  updateWtWaterCardCreate
} from "@/api/water/wtWaterCardCreate";
import { fetchList } from "@/api/dealer/miniProgramOfficialAccount";
import SingleUpload from "@/components/Upload/singleUpload";
import { get } from "@/utils/auth";

const defaultWtWaterCardCreate = {
  code: "",
  startNo: "",
  endNo: "",
  number: "",
  acid: "",
  distinguishNum: ""
};
export default {
  name: "WtWaterCardCreateDetail",
  components: { SingleUpload },
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    const validateCode = (rule, value, callback) => {
      if (new RegExp(/^[A-Z]+$/).test(value)) {
        callback();
      } else {
        callback(new Error("请输入正确格式的代号"));
      }
    };
    const validateStartNo = (rule, value, callback) => {
      if (this.wtWaterCardCreate.endNo) {
        if (Number(value) > Number(this.wtWaterCardCreate.endNo)) {
          callback(new Error("起始卡号不可大于终止卡号"));
        }
      }
    };
    const validateEndNo = (rule, value, callback) => {
      if (this.wtWaterCardCreate.startNo) {
        if (Number(value) < Number(this.wtWaterCardCreate.startNo)) {
          callback(new Error("终止卡号不可小于起始卡号"));
        }
      }
    };
    return {
      wtWaterCardCreate: Object.assign({}, defaultWtWaterCardCreate),
      blance: {
        dialogVisible: false,
        id: null
      },
      listQuery: {
        pageNum: 1,
        pageSize: 10
      },
      list: null,
      total: null,
      listLoading: true,
      rules: {
        code: [
          {
            required: true,
            message: "请输入代号",
            trigger: ["blur", "change"]
          },
          {
            min: 2,
            max: 2,
            message: "长度为 2 个字符",
            trigger: ["blur", "change"]
          },
          { validator: validateCode, trigger: ["blur", "change"] }
        ],
        startNo: [
          { required: true, message: "请输入起始卡号", trigger: "blur" }
          //   { validator: validateStartNo, trigger: "blur" }
        ],
        endNo: [
          { required: true, message: "请输入起始卡号", trigger: "blur" }
          //   { validator: validateEndNo, trigger: "blur" }
        ],
        acid: [
          { required: true, message: "请输入关联公众号id！", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList()
    if (this.isEdit) {
      getWtWaterCardCreate(this.$route.query.id).then(response => {
        this.wtWaterCardCreate = response.data;
      });
    } else {
      this.wtWaterCardCreate = Object.assign({}, defaultWtWaterCardCreate);
    }
  },
  methods: {
    chooseZ() {
      // this.listQuery = {
      //   pageNum: 1,
      //   pageSize: 10
      // };
      // this.list = null;
      // this.listLoading = false;
      this.blance.dialogVisible = true;
    },
    handleEditBlance() {
      this.blance.dialogVisible = false;
    },
    getList() {
      this.listLoading = true;
      fetchList(this.listQuery).then(response => {
        this.listLoading = false;
        this.list = response.data.records;
        this.total = response.data.total;
        this.totalPage = response.data.pages;
        this.pageSize = response.data.size;
      });
    },
    handleSizeChange(val) {
      this.listQuery.pageNum = 1;
      this.listQuery.pageSize = val;
      this.getList();
    },
    handleCurrentChange(val) {
      this.listQuery.pageNum = val;
      this.getList();
    },
    handleChoose(index, row) {
      this.wtWaterCardCreate.acid = row.acid;
      this.blance.dialogVisible = false;
    },
    startNo(e) {
      if (this.wtWaterCardCreate.endNo) {
        this.wtWaterCardCreate.number =
          Number(this.wtWaterCardCreate.endNo) - Number(e) + 1;
      }
    },
    endNo(e) {
      if (this.wtWaterCardCreate.startNo) {
        this.wtWaterCardCreate.number =
          Number(e) - Number(this.wtWaterCardCreate.startNo) + 1;
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
              updateWtWaterCardCreate(
                this.$route.query.id,
                this.wtWaterCardCreate
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
              this.wtWaterCardCreate.createBy = get("userId");
              this.wtWaterCardCreate.dealerId = get("userId");
              createWtWaterCardCreate(this.wtWaterCardCreate).then(response => {
                if (response.code == 200) {
                  this.$refs[formName].resetFields();
                  this.wtWaterCardCreate = Object.assign(
                    {},
                    defaultWtWaterCardCreate
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
      this.wtWaterCardCreate = Object.assign({}, defaultWtWaterCardCreate);
    }
  }
};
</script>
<style>
</style>


