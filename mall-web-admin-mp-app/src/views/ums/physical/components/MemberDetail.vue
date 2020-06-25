<template>
  <el-card class="form-container" shadow="never">
    <el-button size="mini" type="primary" @click="reportLoss">{{ cardType }}</el-button>
    <el-button size="mini" type="primary">补卡</el-button>
    <div class="table-container">
      <el-row>
        <el-col :span="12">
          <div class="bg-purple">卡号</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElementType.cardNo }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">用户名</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElementType.nickname }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">会员等级</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElementType.memberLevelName }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">余额</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElementType.uniacName }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">状态</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">
            {{ wtFilterElementType.labelName }}
          </div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">售卡人</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElementType.sourceType }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">设备</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElementType.recommendName }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">充值金额</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElementType.recommendNum }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">赠送金额</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElementType.integration }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">体验金额</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElementType.integration }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">赠送有效期</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElementType.integration }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">公众号</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElementType.integration }}</div>
        </el-col>
      </el-row>
    </div>

    <!-- 会员卡日志 -->
    <div style="margin-top:50px">
      <h3>会员卡日志</h3>
      <div class="table-container">
        <el-table
          ref="brandTable"
          :data="memberCardlist"
          style="width: 100%"
          v-loading="memberCardlistLoading"
          border
        >
          <el-table-column label="编号" width="60" align="center">
            <template slot-scope="scope">{{scope.row.id}}</template>
          </el-table-column>

          <el-table-column label="会员卡号" width="140" align="center">
            <template slot-scope="scope">{{scope.row.nickname}}</template>
          </el-table-column>

          <el-table-column label="备注" width="140" align="center">
            <template slot-scope="scope">{{scope.row.nickname}}</template>
          </el-table-column>

          <el-table-column label="操作人" width="140" align="center">
            <template slot-scope="scope">{{scope.row.nickname}}</template>
          </el-table-column>

          <el-table-column label="时间" width="140" align="center">
            <template slot-scope="scope">{{scope.row.nickname}}</template>
          </el-table-column>
        </el-table>
      </div>
      <div class="batch-operate-container"></div>
      <div class="pagination-container">
        <el-pagination
          background
          @size-change="handleSizeMemberCardChange"
          @current-change="handleCurrentMemberCardChange"
          layout="total, sizes,prev, pager, next,jumper"
          :page-size="memberCardListQuery.pageSize"
          :page-sizes="[5,10,15]"
          :current-page.sync="memberCardListQuery.pageNum"
          :total="memberCardTotal"
        ></el-pagination>
      </div>
    </div>

    <!-- 会员日志 -->
    <div style="margin-top:50px">
      <h3>会员日志</h3>
      <div class="table-container">
        <el-table
          ref="brandTable"
          :data="memberlist"
          style="width: 100%"
          v-loading="memberlistLoading"
          border
        >
          <el-table-column label="编号" width="60" align="center">
            <template slot-scope="scope">{{scope.row.id}}</template>
          </el-table-column>

          <el-table-column label="事件" align="center">
            <template slot-scope="scope">{{scope.row.content}}</template>
          </el-table-column>

          <!-- <el-table-column label="操作人" width="140" align="center">
            <template slot-scope="scope">{{scope.row.nickname}}</template>
          </el-table-column>-->

          <el-table-column label="时间" width="180" align="center">
            <template slot-scope="scope">{{scope.row.createTime | formatDate }}</template>
          </el-table-column>
        </el-table>
      </div>

      <div class="batch-operate-container"></div>
      <div class="pagination-container">
        <el-pagination
          background
          @size-change="handleSizeMemberChange"
          @current-change="handleCurrentMemberChange"
          layout="total, sizes,prev, pager, next,jumper"
          :page-size="memberListQuery.pageSize"
          :page-sizes="[5,10,15]"
          :current-page.sync="memberListQuery.pageNum"
          :total="memberTotal"
        ></el-pagination>
      </div>
    </div>

    <!-- 备注列表 -->
    <div style="margin-top:50px">
      <el-button type="primary" @click="addNote" size="small">添加备注</el-button>
      <div class="table-container">
        <el-table
          ref="brandTable"
          :data="notelist"
          style="width: 100%"
          v-loading="notelistLoading"
          border
        >
          <el-table-column label="编号" width="60" align="center">
            <template slot-scope="scope">{{scope.row.id}}</template>
          </el-table-column>

          <el-table-column label="备注" align="center">
            <template slot-scope="scope">{{scope.row.content}}</template>
          </el-table-column>

          <el-table-column label="时间" width="180" align="center">
            <template slot-scope="scope">{{scope.row.createTime | formatDate }}</template>
          </el-table-column>
        </el-table>
      </div>

      <div class="batch-operate-container"></div>
      <div class="pagination-container">
        <el-pagination
          background
          @size-change="handleSizeNoteChange"
          @current-change="handleCurrentNoteChange"
          layout="total, sizes,prev, pager, next,jumper"
          :page-size="noteListQuery.pageSize"
          :page-sizes="[5,10,15]"
          :current-page.sync="noteListQuery.pageNum"
          :total="noteTotal"
        ></el-pagination>
      </div>
    </div>

    <!-- 添加备注 -->
    <el-dialog title="添加备注" :visible.sync="note.dialogVisible" width="40%">
      <el-form :model="note" ref="brandFrom" label-width="150px">
        <el-form-item label="备注：" prop="detail">
          <el-input v-model="note.content" />
        </el-form-item>

        <el-form-item>
          <el-button @click="note.dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="handleAddNote">确 定</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </el-card>
</template>
<script>
import {
  getMember,
  getLogList,
  createNote,
  updateWaterCard
} from "@/api/ums/member";
import { getWtWaterCard } from "@/api/water/wtWaterCard";
import axios from "axios";
import { getToken, get } from "@/utils/auth";
import { formatDate } from "@/utils/date";
const defaultWtFilterElementType = {};
export default {
  name: "BrandDetail",
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  filters: {
    formatType(value) {
      if (value === 1) {
        return "小程序";
      } else if (value === 2) {
        return "h5";
      } else if (value === 3) {
        return "app";
      }
    },
    formatDate(time) {
      if (time == null || time === "") {
        return "N/A";
      }
      let date = new Date(time);
      return formatDate(date, "yyyy-MM-dd hh:mm:ss");
    }
  },
  data() {
    return {
      cardType:"挂失",
      note: {
        dialogVisible: false,
        id: null
      },
      wtFilterElementType: Object.assign({}, defaultWtFilterElementType),
      list: [],

      memberCardListQuery: {
        pageNum: 1,
        pageSize: 5
      },
      memberCardTotal: null,
      memberCardlistLoading: false,
      memberCardlist: null,

      memberlistLoading: false,
      memberlist: null,
      memberListQuery: {
        falg: 0,
        pageNum: 1,
        pageSize: 5
      },
      memberTotal: null,

      notelistLoading: false,
      notelist: null,
      noteListQuery: {
        falg: 1,
        pageNum: 1,
        pageSize: 5
      },
      noteTotal: null
    };
  },
  created() {
    this.getWtWaterCard();
    this.getNoteList();
    this.getMemberList();
  },
  methods: {
    // 挂失
    reportLoss() {
      this.$confirm("确定挂失该卡吗", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        updateWaterCard({
          id: this.wtFilterElementType.id
        }).then(response => {
          this.$message({
            message: "删除成功",
            type: "success",
            duration: 1000
          });
          this.getWtWaterCard();
        });
      });
    },
    // 添加备注
    addNote() {
      this.note.dialogVisible = true;
    },
    handleAddNote() {
      this.$confirm("是否添加该备注", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        let data = {
          content: this.note.content,
          falg: 1,
          createBy: get('userId')
        }
        createNote(data).then(response => {
          this.$message({
            message: "添加成功",
            type: "success",
            duration: 1000
          });
          this.getNoteList();
        });
        this.note.dialogVisible = false;
      });
    },

    getWtWaterCard() {
      getWtWaterCard(this.$route.query.id).then(response => {
        this.wtFilterElementType = response.data.records[0];
        this.integration.integration = this.wtFilterElementType.integration;
        // 0正常1复制卡2挂失卡3删除卡4非经销商卡
        if (this.wtFilterElementType.state == "2") {
          this.text = "解除挂失";
        } else if (this.wtFilterElementType.state == "0") {
          this.text = "挂失";
        }
      });
    },

    // 会员日志
    getMemberList() {
      this.memberlistLoading = true;
      getLogList(this.memberListQuery).then(response => {
        this.memberlistLoading = false;
        this.memberlist = response.data.records;
        this.memberTotal = response.data.total;
        this.totalPage = response.data.pages;
        this.pageSize = response.data.size;
      });
    },
    handleSizeMemberChange(val) {
      this.memberListQuery.pageNum = 1;
      this.memberListQuery.pageSize = val;
      this.getMemberList();
    },
    handleCurrentMemberChange(val) {
      this.memberListQuery.pageNum = val;
      this.getMemberList();
    },

    handleSizeMemberCardChange(val) {
      this.memberCardListQuery.pageNum = 1;
      this.memberCardListQuery.pageSize = val;
      // this.getMemberList();
    },
    handleCurrentMemberCardChange(val) {
      this.memberCardListQuery.pageNum = val;
      // this.getMemberList();
    },
    // 备注列表
    getNoteList() {
      this.notelistLoading = true;
      getLogList(this.noteListQuery).then(response => {
        this.notelistLoading = false;
        this.notelist = response.data.records;
        this.noteTotal = response.data.total;
        // this.totalPage = response.data.pages;
        // this.pageSize = response.data.size;
      });
    },
    handleSizeNoteChange(val) {
      this.noteListQuery.pageNum = 1;
      this.noteListQuery.pageSize = val;
      this.getNoteList();
    },
    handleCurrentNoteChange(val) {
      this.noteListQuery.pageNum = val;
      this.getNoteList();
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.el-tag + .el-tag {
  margin-left: 10px;
}

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


