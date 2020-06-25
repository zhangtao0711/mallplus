<template>
  <el-card class="form-container" shadow="never">
    <el-button size="mini" type="primary" @click="addIntegration">积分管理</el-button>
    <el-button size="mini" :disabled="true" type="primary" @click="sendMessage">发送通知</el-button>
    <el-button
      size="mini"
      type="primary"
      @click="handleDelete(wtFilterElementType.id)"
    >{{ text }}黑名单</el-button>
    <div class="table-container">
      <el-row>
        <el-col :span="12">
          <div class="bg-purple">用户昵称</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElementType.nickname }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">手机号</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElementType.phone }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">实体卡</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElementType.cardNum }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">虚拟卡</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElementType.cardInventedNum }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">公众号</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElementType.uniacName }}</div>
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
          <div class="bg-purple">标签</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">
            <!-- <el-tag
              :key="tag"
              v-for="tag in dynamicTags"
              closable
              :disable-transitions="false"
              @close="handleClose(tag)"
            >{{tag}}</el-tag>-->
            <el-tag
              v-if="wtFilterElementType.labelName"
              closable
              :disable-transitions="false"
              @close="handleClose(wtFilterElementType.labelName)"
            >{{ wtFilterElementType.labelName }}</el-tag>

            <el-button size="small" style="float:right" @click="addTag">添加标签</el-button>
          </div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">来源</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElementType.sourceType | formatType }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">推荐人</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElementType.recommendName }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">推荐人数</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElementType.recommendNum }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <div class="bg-purple">积分</div>
        </el-col>
        <el-col :span="12">
          <div class="bg-purple-light">{{ wtFilterElementType.integration }}</div>
        </el-col>
      </el-row>
    </div>

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

    <!-- 积分管理 -->
    <el-dialog title="积分管理" :visible.sync="integration.dialogVisible" width="40%">
      <el-form :model="integration" ref="brandFrom" label-width="150px">
        <el-form-item label="当前积分：" prop="detail">
          <el-input-number size="medium" v-model="integration.integration"></el-input-number>
        </el-form-item>

        <el-form-item>
          <el-button @click="integration.dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="handleEditIntegration">确 定</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <!-- 添加标签 -->
    <el-dialog title="添加标签" :visible.sync="tag.dialogVisible" width="40%">
      <el-form :model="integration" ref="brandFrom" label-width="150px">
        <el-form-item label="标签：" prop="detail">
          <el-select v-model="tag.labelName" placeholder="请选择用户标签">
            <el-option v-for="item in list" :key="item.name" :label="item.name" :value="item.name"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button @click="tag.dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="handleAddTag">确 定</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <!-- 发送消息 -->
    <el-dialog title="发送消息" :visible.sync="msg.dialogVisible" width="40%">
      <el-form :model="integration" ref="brandFrom" label-width="150px">
        <el-form-item label="选择模版消息：" prop="detail">
          <el-select v-model="msg.labelName" placeholder="请选择模版消息">
            <el-option v-for="item in list" :key="item.name" :label="item.name" :value="item.name"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button @click="msg.dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="handleSendMsg">确 定</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <!-- 添加备注 -->
    <el-dialog title="添加备注" :visible.sync="note.dialogVisible" width="40%">
      <el-form :model="integration" ref="brandFrom" label-width="150px">
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
  updateIntegration,
  updateStatus,
  addLabel,
  removeLabel,
  getLogList,
  createNote
} from "@/api/ums/member";
import { fetchList } from "@/api/ums/umsMemberTag";
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
      text: "",
      integration: {
        dialogVisible: false,
        id: null
      },
      tag: {
        dialogVisible: false,
        id: null
      },
      msg: {
        dialogVisible: false,
        id: null
      },
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
    this.getMember();
    this.getList();
    this.getNoteList();
    this.getMemberList();
  },
  methods: {
    addNote() {
      this.note.dialogVisible = true;
    },
    handleClose(tag) {
      // this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1);
      removeLabel({ umsMemberId: this.$route.query.id, labelName: tag }).then(
        response => {
          this.getMember();
        }
      );
    },
    addTag() {
      this.tag.dialogVisible = true;
    },
    handleAddTag() {
      if (this.tag.labelName == null) {
        this.$message({
          message: "请选择用户标签",
          type: "warning",
          duration: 1000
        });
        return;
      }
      this.$confirm("是否添该改标签", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        axios({
          method: "POST",
          url: process.env.BASE_API + "/ums/UmsMember/addLabel",
          headers: {
            Authorization: getToken()
          },
          data: {
            umsMemberId: this.$route.query.id,
            labelName: this.tag.labelName
          }
        })
          .then(response => {
            if (response.data.code == 200) {
              this.$message({
                message: "操作成功",
                type: "success",
                duration: 1000
              });
              this.getMember();
            } else {
              this.$message.error(response.data.msg);
            }
          })
          .catch(function(err) {
            console.log(err);
          });
        this.tag.dialogVisible = false;
      });
    },
    getList() {
      let listQuery = {
        keyword: null,
        pageNum: 1,
        pageSize: 10,
        genType: 2
      };

      fetchList(listQuery).then(response => {
        for (var i = 0; i < response.data.pages; i++) {
          fetchList(listQuery).then(res => {
            for (var j = 0; j < res.data.records.length; j++) {
              this.list.push(res.data.records[j]);
            }
          });
        }
      });
    },
    getMember() {
      getMember(this.$route.query.id).then(response => {
        this.wtFilterElementType = response.data.records[0];
        this.integration.integration = this.wtFilterElementType.integration;
        // 0冻结1正常
        if (this.wtFilterElementType.status == "1") {
          this.text = "拉入";
        } else {
          this.text = "取消";
        }
      });
    },
    addIntegration() {
      this.integration.dialogVisible = true;
    },
    handleEditIntegration() {
      this.$confirm("是否对该用户的积分进行操作", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        // let params = new URLSearchParams();
        // params.append("id", this.integration.id);
        // params.append("integration", this.integration.integration);

        axios({
          method: "POST",
          url: process.env.BASE_API + "/ums/UmsMember/updateIntegration",
          headers: {
            Authorization: getToken()
          },
          data: {
            id: this.integration.id,
            integration: this.integration.integration
          }
        })
          .then(response => {
            if (response.data.code == 200) {
              this.$message({
                message: "操作成功",
                type: "success",
                duration: 1000
              });
              this.getMember();
            } else {
              this.$message.error(response.data.msg);
            }
          })
          .catch(function(err) {
            console.log(err);
          });

        this.integration.dialogVisible = false;
      });
    },
    sendMessage() {
      this.msg.dialogVisible = true;
    },
    handleSendMsg() {
      if (this.tag.labelName == null) {
        this.$message({
          message: "请选择模版消息",
          type: "warning",
          duration: 1000
        });
        return;
      }
      this.$confirm("是否添加该标签", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        axios({
          method: "POST",
          url: process.env.BASE_API + "/ums/UmsMember/addLabel",
          headers: {
            Authorization: getToken()
          },
          data: {
            umsMemberId: this.$route.query.id,
            labelName: this.tag.labelName
          }
        })
          .then(response => {
            if (response.data.code == 200) {
              this.$message({
                message: "操作成功",
                type: "success",
                duration: 1000
              });
              this.getMember();
            } else {
              this.$message.error(response.data.msg);
            }
            this.tag.dialogVisible = false;
          })
          .catch(function(err) {
            console.log(err);
          });
      });
    },
    handleDelete(id) {
      this.$confirm("是否要将该用户" + this.text + "黑名单", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        updateStatus({
          id: this.wtFilterElementType.id
        }).then(response => {
          this.$message({
            message: "删除成功",
            type: "success",
            duration: 1000
          });
          this.getMember();
        });
      });
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
          createBy: get('userId'),
          memberId: this.$route.query.id
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

    getMemberList() {
      this.memberlistLoading = true;
      this.memberListQuery.memberId = this.$route.query.id
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

    getNoteList() {
      this.notelistLoading = true;
      this.noteListQuery.memberId = this.$route.query.id
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


