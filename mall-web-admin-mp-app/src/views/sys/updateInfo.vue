<template>
  <div>
    <div class="content">
      <el-row :gutter="20">
        <el-col :span="4">
          <div class="title">用户名称：</div>
        </el-col>
        <el-col :span="16">
          <div>{{ userName }}</div>
        </el-col>
        <el-col :span="4">
          <div class="update">
            <el-button type="primary" icon="el-icon-edit" size="mini" @click="updateNickname">修改</el-button>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="4">
          <div class="title">密码：</div>
        </el-col>
        <el-col :span="16">
          <!-- <div>{{ pwd }}</div> -->
          <div>**********</div>
        </el-col>
        <el-col :span="4">
          <div class="update">
            <el-button type="primary" icon="el-icon-edit" size="mini" @click="updatePassword">修改</el-button>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="4">
          <div class="title">手机号：</div>
        </el-col>
        <el-col :span="16">
          <div>{{ phone }}</div>
        </el-col>
        <el-col :span="4">
          <div class="update">
            <el-button type="primary" icon="el-icon-edit" size="mini" @click="updatePhone">修改</el-button>
          </div>
        </el-col>
      </el-row>
    </div>

    <el-dialog title="修改密码" :visible.sync="blance.dialogVisible" width="40%">
      <el-form :model="blance" :rules="loginRules" ref="brandFrom" label-width="150px">
        <el-form-item label="旧密码：" prop="password">
          <el-input v-model="blance.password"></el-input>
        </el-form-item>
        <el-form-item label="新密码：" prop="newPassword">
          <el-input v-model="blance.newPassword"></el-input>
        </el-form-item>

        <el-form-item label="确认新密码：" prop="renewPassword">
          <el-input v-model="blance.renewPassword"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button @click="blance.dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="handleEditBlance">确 定</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <el-dialog title="修改用户名称" :visible.sync="nickname.dialogVisible" width="40%">
      <el-form :model="blance" :rules="loginRules" ref="brandFrom" label-width="150px">
        <el-form-item label="用户名称：" prop="username">
          <el-input v-model="nickname.username"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button @click="nickname.dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="handleEditNickName">确 定</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <el-dialog title="修改手机号" :visible.sync="nPhone.dialogVisible" width="40%">
      <el-form :model="blance" :rules="loginRules" ref="brandFrom" label-width="150px">
        <el-form-item label="原手机号：" prop="oldPhone">
          <el-input v-model="nPhone.oldPhone"></el-input>
        </el-form-item>
        <el-form-item label="新手机号：" prop="newPhone">
          <el-input v-model="nPhone.newPhone"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button @click="nPhone.dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="handleEditPhone">确 定</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import { get } from "@/utils/auth";
import { communityUser, updatePassword, updatePhone, updateUsername } from "@/api/admin";
export default {
  components: {},
  data() {
    return {
      userName: get('username'),
      phone: get('phone'),
      pwd: get('pwd'),
      loginRules: {
        password: [{ required: true, trigger: "blur" }],
        renewPassword: [{ required: true, trigger: "blur" }],
        newPassword: [{ required: true, trigger: "blur" }],
        username: [{ required: true, trigger: "blur" }],
        oldPhone: [{ required: true, trigger: "blur" }],
        newPhone: [{ required: true, trigger: "blur" }]
      },
      blance: {
        dialogVisible: false,
        id: null
      },
      nickname: {
        dialogVisible: false,
        id: null
      },
      nPhone: {
        dialogVisible: false,
        id: null
      },
      redList: null,
      communityUser: null
    };
  },
  computed: {},
  created() {},
  methods: {
    handleEditBlance() {
      this.$confirm("是否要修改密码", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        let params = new URLSearchParams();
        params.append("password", this.blance.password);
        params.append("renewPassword", this.blance.renewPassword);
        params.append("newPassword", this.blance.newPassword);

        updatePassword(params).then(response => {
          this.$message({
            message: "修改密码成功",
            type: "success",
            duration: 1000
          });
        });
        this.blance.dialogVisible = false;
      });
    },

    handleEditNickName() {
      this.$confirm("是否要修改用户名称", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        let params = new URLSearchParams();
        params.append("username", this.nickname.username);

        updateUsername(get('userId'),params).then(response => {
          this.$message({
            message: "修改用户名称成功",
            type: "success",
            duration: 1000
          });
        });
        this.nickname.dialogVisible = false;
      });
    },

    handleEditPhone() {
      this.$confirm("是否要修改手机号", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        let params = new URLSearchParams();
        params.append("username", this.nickname.username);

        updatePhone(get('userId'),params).then(response => {
          this.$message({
            message: "修改手机号成功",
            type: "success",
            duration: 1000
          });
        });
        this.nPhone.dialogVisible = false;
      });
    },

    updatePassword() {
      this.blance.dialogVisible = true;
    },

    updateNickname() {
      this.nickname.dialogVisible = true;
    },

    updatePhone() {
      this.nPhone.dialogVisible = true;
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.content {
  margin-top: 150px;
  .el-row {
    margin-bottom: 20px;
    &:last-child {
      margin-bottom: 0;
    }
  }
  .update,.title {
    text-align: center;
  }
}
</style>

