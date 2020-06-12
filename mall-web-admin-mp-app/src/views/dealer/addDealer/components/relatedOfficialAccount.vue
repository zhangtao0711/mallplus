<template>
  <el-card class="form-container" shadow="never">
    <el-form
      :model="wtWaterCardActivate"
      :rules="rules"
      ref="WtWaterCardActivateFrom"
      label-width="150px"
    >
      <el-form-item label="公众号名称" prop="name">
        <el-input v-model="wtWaterCardActivate.name" placeholder="请填写公众号名称" style="width: 370px;" />
      </el-form-item>

      <el-form-item label="描述" prop="signature">
        <el-input
          v-model="wtWaterCardActivate.signature"
          placeholder="用于说明此公众号的功能及用途"
          style="width: 370px;"
        />
      </el-form-item>

      <el-form-item label="token" prop="token">
        <el-input
          v-model="wtWaterCardActivate.token"
          placeholder="请填写随机生成的密钥"
          style="width: 370px;"
        />
      </el-form-item>

      <el-form-item label="encodingaeskey" prop="encodingaeskey">
        <el-input
          v-model="wtWaterCardActivate.encodingaeskey"
          placeholder="与公众平台接入设置值一致"
          style="width: 370px;"
        />
      </el-form-item>

      <el-form-item label="所属区域">
        <el-select
          style="width: 370px;"
          v-model="wtWaterCardActivate.province"
          @change="getSecondData"
          clearable
          placeholder="请选择所在省"
        >
          <el-option
            v-for="item in parentArea"
            :key="item.id"
            :label="item.extName"
            :value="item.id"
          ></el-option>
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-select
          style="width: 370px;"
          v-model="wtWaterCardActivate.city"
          clearable
          placeholder="请选择所在市"
        >
          <el-option
            v-for="item in secondArea"
            :key="item.id"
            :label="item.extName"
            :value="item.id"
          ></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="公众号账号" prop="account">
        <el-input
          v-model="wtWaterCardActivate.account"
          placeholder="请填写公众号账号"
          style="width: 370px;"
        />
      </el-form-item>

      <el-form-item label="原始Id" prop="original">
        <el-input
          v-model="wtWaterCardActivate.original"
          placeholder="请填写原始Id"
          style="width: 370px;"
        />
        <p>原始ID不能修改，请谨慎填写</p>
      </el-form-item>

      <el-form-item label="类型" prop="level">
        <el-select v-model="wtWaterCardActivate.level" placeholder="请选择">
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
        <p>注意：即使公众平台显示为“未认证”，但只要【公众号设置】/【账号详情】下【认证情况】显示资质审核通过，即可认定为认证号</p>
      </el-form-item>

      <el-form-item label="AppId" prop="key">
        <el-input
          v-model="wtWaterCardActivate.key"
          placeholder="请填写微信公众平台后台的AppId"
          style="width: 370px;"
        />
      </el-form-item>

      <el-form-item label="AppSecret" prop="secret">
        <el-input
          v-model="wtWaterCardActivate.secret"
          placeholder="请填写微信公众平台后台的AppSecret"
          style="width: 370px;"
        />
      </el-form-item>

      <el-form-item label="公众号状态">
        <el-radio-group v-model="wtWaterCardActivate.status">
          <el-radio-button :label="0">关闭</el-radio-button>
          <el-radio-button :label="1">开启</el-radio-button>
          <el-radio-button :label="2">已连接</el-radio-button>
          <el-radio-button :label="3">连接失败</el-radio-button>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="Oauth 2.0">在微信公众号号请求用户授权之前，开发者需要先到公众平台网站的【开发者中心】网页服务中配置授权回调域名。</el-form-item>

      <el-form-item label="头像" prop="logo">
        <single-upload-img v-model="wtWaterCardActivate.logo"></single-upload-img>
      </el-form-item>

      <el-form-item label="二维码" prop="qrCode">
        <single-upload-img v-model="wtWaterCardActivate.qrCode"></single-upload-img>
      </el-form-item>

      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>第一步</span>
        </div>
        <div>
          <p>
            登录
            <span style="color:#3fabd8">微信公众平台</span>，点击左侧菜单最后一项，进入[
            <span style="color:#ea1a1a">开发者中心</span> ]
          </p>
          <p>
            <el-image :src="guide01"></el-image>
          </p>
          <p>
            #如果您未成为开发者，请勾选页面上的同意协议，再点击[
            <span style="color:#ea1a1a">成为开发</span> ]按钮
          </p>
        </div>
      </el-card>

      <el-card class="box-card" style="margin: 20px auto">
        <div slot="header" class="clearfix">
          <span>第二步</span>
        </div>
        <div>
          <p>
            在开发者中心找到[
            <span style="color:#ea1a1a">服务器配置</span> ] 栏目下URL和Token设置
          </p>
          <el-image :src="guide02"></el-image>
          <p>#将链接填入下面对应的输入框</p>
          <div style="width:85%;margin:0 auto;">
            <p>URL：</p>
            <p>Token：</p>
            <p>EncodingAESKey：</p>
          </div>
          <p>
            #如果以前填写过URL和Token，请点击[
            <span style="color:#ea1a1a">修改配置</span> ] 再填写上述链接
          </p>
          <p>
            #请点击[
            <span style="color:#ea1a1a">启用</span> ]，以启用服务器配置：
          </p>
          <el-image :src="guide03"></el-image>
        </div>
      </el-card>

      <el-card class="box-card" style="margin: 20px auto">
        <div slot="header" class="clearfix">
          <span>第三步</span>
        </div>
        <div>
          <p>
            <span style="color:#ea1a1a">公众号XXX正在等待接入……请及时按照以上步骤操作接入公众平台</span>
          </p>
          <p>#检查公众平台配置</p>
          <p>#编辑公众号XXX</p>
        </div>
      </el-card>

      <el-form-item>
        <el-button type="primary" @click="onSubmit('WtWaterCardActivateFrom')">提交</el-button>
        <!-- <el-button>检测是否接入成功</el-button> -->
      </el-form-item>

      <!-- <el-form-item>
        <el-button>暂不接入，先去查看管理功能</el-button>
        <el-button @click="goback">返回公众号列表</el-button>
      </el-form-item>-->
    </el-form>
  </el-card>
</template>
<script>
import { getAreaList } from "@/api/dealer/dealer";
import {
  createAccount,
  updateAccount,
  getAccount
} from "@/api/dealer/miniProgramOfficialAccount";
import { get } from "@/utils/auth";
import SingleUploadImg from "@/components/Upload/singleUploadPublic";
import guide01 from "@/assets/images/guide-01.png";
import guide02 from "@/assets/images/guide-02.png";
import guide03 from "@/assets/images/guide-03.png";

const defaultWtWaterCardActivate = {};
export default {
  name: "relatedOfficialAccount",
  props: {
    dealerId: String
  },
  components: {
    SingleUploadImg
  },
  data() {
    return {
      parentArea: [],
      secondArea: [],
      isEdit: false,
      guide01: guide01,
      guide02: guide02,
      guide03: guide03,
      proportionShow: false,
      zproportionShow: false,
      accountShow: false,
      appShow: false,
      jxsShow: false,
      fxsShow: false,
      ownerShow: false,
      options: [
        {
          value: "1",
          label: "普通服务号"
        },
        {
          value: "2",
          label: "认证服务号"
        }
        // {
        //   value: "1",
        //   label: "普通订阅号"
        // },
        // {
        //   value: "2",
        //   label: "普通服务号"
        // },
        // {
        //   value: "3",
        //   label: "认证订阅号"
        // },
        // {
        //   value: "4",
        //   label: "认证服务号/认证媒体/政府订阅号"
        // }
      ],
      wtWaterCardActivate: Object.assign({}, defaultWtWaterCardActivate),
      rules: {
        name: [
          { required: true, message: "请输入公众号名称", trigger: "blur" }
        ],
        account: [
          { required: true, message: "请输入公众号账号", trigger: "blur" }
        ],
        original: [{ required: true, message: "请输入原始Id" }],
        level: [{ required: true, message: "请选择公众号类型" }],
        key: [{ required: true, message: "请填写微信公众平台后台的AppId" }],
        secret: [
          { required: true, message: "请填写微信公众平台后台的AppSecret" }
        ]
      }
    };
  },
  created() {
    if (this.$route.query.id) {
      this.dealerId = this.$route.query.id;
    }
    if (this.dealerId) {
      this.getAccount();
    }

    this.getAreaList();
  },
  methods: {
    getAreaList() {
      let param = {
        deep: 0
      };
      getAreaList(param).then(response => {
        for (var i = 0; i < response.data.pages; i++) {
          getAreaList({ deep: 0, pageNum: i + 1 }).then(res => {
            for (var j = 0; j < res.data.records.length; j++) {
              this.parentArea.push(res.data.records[j]);
            }
          });
        }
      });
    },
    getSecondData(e) {
      this.secondArea = [];
      let param = {
        pid: e
      };
      getAreaList(param).then(response => {
        for (var i = 0; i < response.data.pages; i++) {
          getAreaList({ pid: e, pageNum: i + 1 }).then(res => {
            for (var j = 0; j < res.data.records.length; j++) {
              this.secondArea.push(res.data.records[j]);
            }
          });
        }
      });
    },
    getAccount() {
      getAccount(this.dealerId).then(response => {
        if (response.data) {
          this.isEdit = true;
          this.wtWaterCardActivate = response.data;
          this.wtWaterCardActivate.province = Number(
            this.wtWaterCardActivate.province
          );
          this.getSecondData(Number(this.wtWaterCardActivate.province));
          this.wtWaterCardActivate.city = Number(this.wtWaterCardActivate.city);
          this.wtWaterCardActivate.level = String(
            this.wtWaterCardActivate.level
          );
        } else {
          this.isEdit = false;
          this.wtWaterCardActivate = Object.assign(
            {},
            defaultWtWaterCardActivate
          );
        }
      });
    },
    goback() {},
    level(e) {
      if (e == "1") {
        this.ownerShow = false;
        this.jxsShow = false;
        this.fxsShow = false;
        this.proportionShow = false;
        this.zproportionShow = false;
      } else if (e == "2") {
        this.ownerShow = true;
        this.jxsShow = true;
        this.fxsShow = false;
        this.proportionShow = false;
        this.zproportionShow = true;
      } else if (e == "3") {
        this.ownerShow = true;
        this.jxsShow = false;
        this.fxsShow = true;
        this.proportionShow = true;
        this.zproportionShow = true;
      }
    },
    selftypeChange(e) {
      if (e == "1") {
        this.appShow = true;
        this.accountShow = false;
      } else if (e == "2") {
        this.appShow = false;
        this.accountShow = true;
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
              this.wtWaterCardActivate.lastupdate = this.wtWaterCardActivate.createTime;
              updateAccount(
                this.wtWaterCardActivate.acid,
                this.wtWaterCardActivate
              ).then(response => {
                if (response.code == 200) {
                  this.$refs[formName].resetFields();
                  this.$message({
                    message: "修改成功",
                    type: "success",
                    duration: 1000
                  });
                  location.reload();
                } else {
                  this.$message({
                    message: response.msg,
                    type: "error",
                    duration: 1000
                  });
                }
              });
            } else {
              this.wtWaterCardActivate.createBy = this.dealerId;
              this.wtWaterCardActivate.styleid = 1;
              this.wtWaterCardActivate.subscribeurl = "测试";
              this.wtWaterCardActivate.country = "中国";
              this.wtWaterCardActivate.authRefreshToken = this.wtWaterCardActivate.token;
              var now = new Date();
              let year = now.getFullYear();
              let month =
                now.getMonth() + 1 < 10
                  ? "0" + (now.getMonth() + 1)
                  : now.getMonth() + 1;
              let day =
                now.getDate() < 10 ? "0" + (now.getDate() - 1) : now.getDate();
              this.wtWaterCardActivate.lastupdate =
                year + "-" + month + "-" + day;
              createAccount(this.wtWaterCardActivate).then(response => {
                if (response.code == 200) {
                  this.$refs[formName].resetFields();
                  this.wtWaterCardActivate = Object.assign(
                    {},
                    defaultWtWaterCardActivate
                  );
                  this.$message({
                    message: "提交成功",
                    type: "success",
                    duration: 1000
                  });
                  // this.$router.back();
                  location.reload();
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
    }
  }
};
</script>
<style>
</style>


