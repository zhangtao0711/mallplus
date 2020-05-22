<template>
  <div class="app-container">
    <el-card class="filter-container" shadow="never">
      <div>
        <i class="el-icon-search"></i>
        <span>筛选搜索</span>
        <el-button
          style="float: right"
          @click="searchMerchatBusinessMaterialsList()"
          type="primary"
          size="small"
        >查询结果</el-button>
      </div>
      <div style="margin-top: 15px">
        <el-form :inline="true" :model="listQuery" size="small" label-width="140px">
          <el-form-item label="输入搜索：">
            <el-input style="width: 203px" v-model="listQuery.keyword" placeholder="类型名称/关键字"></el-input>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <el-card class="operate-container" shadow="never">
      <i class="el-icon-tickets"></i>
      <span>数据列表</span>
      <el-button class="btn-add" @click="addMerchatBusinessMaterials()" size="mini">添加</el-button>
    </el-card>
    <div class="table-container">
      <el-table
        ref="merchatBusinessMaterialsTable"
        :data="list"
        style="width: 100%"
        @selection-change="handleSelectionChange"
        v-loading="listLoading"
        border
      >
        <el-table-column type="selection" width="60" align="center"></el-table-column>

        <el-table-column prop="id" label="主键">
          <template slot-scope="scope">{{ scope.row.id }}</template>
        </el-table-column>

        <el-table-column prop="businessCode" label="业务申请编号 ">
          <template slot-scope="scope">{{ scope.row.businessCode }}</template>
        </el-table-column>

        <el-table-column prop="contactName" label="超级管理员姓名">
          <template slot-scope="scope">{{ scope.row.contactName }}</template>
        </el-table-column>

        <el-table-column prop="contactIdNumber" label="超级管理员身份证件号码 ">
          <template slot-scope="scope">{{ scope.row.contactIdNumber }}</template>
        </el-table-column>

        <el-table-column prop="openid" label="超级管理员微信openid">
          <template slot-scope="scope">{{ scope.row.openid }}</template>
        </el-table-column>

        <el-table-column prop="mobilePhone" label="联系手机 ">
          <template slot-scope="scope">{{ scope.row.mobilePhone }}</template>
        </el-table-column>

        <el-table-column prop="contactEmail" label="联系邮箱">
          <template slot-scope="scope">{{ scope.row.contactEmail }}</template>
        </el-table-column>

        <el-table-column prop="subjectType" label="主体类型">
          <template slot-scope="scope">{{ scope.row.subjectType }}</template>
        </el-table-column>

        <el-table-column prop="licenseCopy" label="营业执照照片">
          <template slot-scope="scope">{{ scope.row.licenseCopy }}</template>
        </el-table-column>

        <el-table-column prop="licenseCopyMediaId" label="营业执照照片">
          <template slot-scope="scope">{{ scope.row.licenseCopyMediaId }}</template>
        </el-table-column>

        <el-table-column prop="licenseNumber" label="注册号/统一社会信用代码 ">
          <template slot-scope="scope">{{ scope.row.licenseNumber }}</template>
        </el-table-column>

        <el-table-column prop="subjectMerchantName" label="商户名称 ">
          <template slot-scope="scope">{{ scope.row.subjectMerchantName }}</template>
        </el-table-column>

        <el-table-column prop="subjectLegalPerson" label="个体户经营者/法人姓名">
          <template slot-scope="scope">{{ scope.row.subjectLegalPerson }}</template>
        </el-table-column>

        <el-table-column prop="certCopy" label="登记证书照片">
          <template slot-scope="scope">{{ scope.row.certCopy }}</template>
        </el-table-column>

        <el-table-column prop="certCopyMediaId" label="登记证书照片">
          <template slot-scope="scope">{{ scope.row.certCopyMediaId }}</template>
        </el-table-column>

        <el-table-column prop="certType" label="登记证书类型">
          <template slot-scope="scope">{{ scope.row.certType }}</template>
        </el-table-column>

        <el-table-column prop="certNumber" label="证书号 ">
          <template slot-scope="scope">{{ scope.row.certNumber }}</template>
        </el-table-column>

        <el-table-column prop="merchantName" label="商户名称 ">
          <template slot-scope="scope">{{ scope.row.merchantName }}</template>
        </el-table-column>

        <el-table-column prop="companyAddress" label="注册地址">
          <template slot-scope="scope">{{ scope.row.companyAddress }}</template>
        </el-table-column>

        <el-table-column prop="legalPerson" label="法人姓名 ">
          <template slot-scope="scope">{{ scope.row.legalPerson }}</template>
        </el-table-column>

        <el-table-column prop="periodBegin" label="有效期限开始日期 ">
          <template slot-scope="scope">{{ scope.row.periodBegin }}</template>
        </el-table-column>

        <el-table-column prop="periodEnd" label="有效期限结束日期">
          <template slot-scope="scope">{{ scope.row.periodEnd }}</template>
        </el-table-column>

        <el-table-column prop="organizationCopy" label="组织机构代码证照片">
          <template slot-scope="scope">{{ scope.row.organizationCopy }}</template>
        </el-table-column>

        <el-table-column prop="organizationCopyMediaId" label="组织机构代码证照片">
          <template slot-scope="scope">{{ scope.row.organizationCopyMediaId }}</template>
        </el-table-column>

        <el-table-column prop="organizationCode" label="组织机构代码">
          <template slot-scope="scope">{{ scope.row.organizationCode }}</template>
        </el-table-column>

        <el-table-column prop="orgPeriodBegin" label="组织机构代码证有效期开始日期">
          <template slot-scope="scope">{{ scope.row.orgPeriodBegin }}</template>
        </el-table-column>

        <el-table-column prop="orgPeriodEnd" label="组织机构代码证有效期结束日期">
          <template slot-scope="scope">{{ scope.row.orgPeriodEnd }}</template>
        </el-table-column>

        <el-table-column prop="certificateLetterCopy" label="单位证明函照片">
          <template slot-scope="scope">{{ scope.row.certificateLetterCopy }}</template>
        </el-table-column>

        <el-table-column prop="certificateLetterCopyMediaId" label="单位证明函照片">
          <template slot-scope="scope">{{ scope.row.certificateLetterCopyMediaId }}</template>
        </el-table-column>

        <el-table-column prop="idDocType" label="证件类型 ">
          <template slot-scope="scope">{{ scope.row.idDocType }}</template>
        </el-table-column>

        <el-table-column prop="idCardCopy" label="身份证人像面照片">
          <template slot-scope="scope">{{ scope.row.idCardCopy }}</template>
        </el-table-column>

        <el-table-column prop="idCardCopyMediaId" label="身份证人像面照片">
          <template slot-scope="scope">{{ scope.row.idCardCopyMediaId }}</template>
        </el-table-column>

        <el-table-column prop="idCardNational" label="身份证国徽面照片">
          <template slot-scope="scope">{{ scope.row.idCardNational }}</template>
        </el-table-column>

        <el-table-column prop="idCardNationalMediaId" label="身份证国徽面照片">
          <template slot-scope="scope">{{ scope.row.idCardNationalMediaId }}</template>
        </el-table-column>

        <el-table-column prop="idCardName" label="身份证姓名">
          <template slot-scope="scope">{{ scope.row.idCardName }}</template>
        </el-table-column>

        <el-table-column prop="idCardNumber" label="身份证号码">
          <template slot-scope="scope">{{ scope.row.idCardNumber }}</template>
        </el-table-column>

        <el-table-column prop="cardPeriodBegin" label="身份证有效期开始时间">
          <template slot-scope="scope">{{ scope.row.cardPeriodBegin }}</template>
        </el-table-column>

        <el-table-column prop="cardPeriodEnd" label="身份证有效期结束时间 ">
          <template slot-scope="scope">{{ scope.row.cardPeriodEnd }}</template>
        </el-table-column>

        <el-table-column prop="idDocCopy" label="证件照片">
          <template slot-scope="scope">{{ scope.row.idDocCopy }}</template>
        </el-table-column>

        <el-table-column prop="idDocCopyMediaId" label="证件照片">
          <template slot-scope="scope">{{ scope.row.idDocCopyMediaId }}</template>
        </el-table-column>

        <el-table-column prop="
idDocName" label="证件姓名">
          <template slot-scope="scope">{{ scope.row.idDocName }}</template>
        </el-table-column>

        <el-table-column prop="
idDocNumber" label="证件号码">
          <template slot-scope="scope">{{ scope.row.idDocNumber }}</template>
        </el-table-column>

        <el-table-column prop="docPeriodBegin" label="证件有效期开始时间">
          <template slot-scope="scope">{{ scope.row.docPeriodBegin }}</template>
        </el-table-column>

        <el-table-column prop="docPeriodEnd" label="证件有效期结束时间 ">
          <template slot-scope="scope">{{ scope.row.docPeriodEnd }}</template>
        </el-table-column>

        <el-table-column prop="owner" label="经营者/法人是否为受益人">
          <template slot-scope="scope">{{ scope.row.owner }}</template>
        </el-table-column>

        <el-table-column prop="idType" label="证件类型">
          <template slot-scope="scope">{{ scope.row.idType }}</template>
        </el-table-column>

        <el-table-column prop="uboIdCardCopy" label="身份证人像面照片">
          <template slot-scope="scope">{{ scope.row.uboIdCardCopy }}</template>
        </el-table-column>

        <el-table-column prop="uboIdCardCopyMediaId" label="身份证人像面照片">
          <template slot-scope="scope">{{ scope.row.uboIdCardCopyMediaId }}</template>
        </el-table-column>

        <el-table-column prop="uboIdCardNational" label="身份证国徽面照片">
          <template slot-scope="scope">{{ scope.row.uboIdCardNational }}</template>
        </el-table-column>

        <el-table-column prop="uboIdCardNationalMediaId" label="身份证国徽面照片">
          <template slot-scope="scope">{{ scope.row.uboIdCardNationalMediaId }}</template>
        </el-table-column>

        <el-table-column prop="uboIdCardName" label="身份证姓名">
          <template slot-scope="scope">{{ scope.row.uboIdCardName }}</template>
        </el-table-column>

        <el-table-column prop="uboIdCardNumber" label="身份证号码">
          <template slot-scope="scope">{{ scope.row.uboIdCardNumber }}</template>
        </el-table-column>

        <el-table-column prop="uboCardPeriodBegin" label="身份证有效期开始时间">
          <template slot-scope="scope">{{ scope.row.uboCardPeriodBegin }}</template>
        </el-table-column>

        <el-table-column prop="uboCardPeriodEnd" label="身份证有效期结束时间 ">
          <template slot-scope="scope">{{ scope.row.uboCardPeriodEnd }}</template>
        </el-table-column>

        <el-table-column prop="uboIdDocCopy" label="证件照片">
          <template slot-scope="scope">{{ scope.row.uboIdDocCopy }}</template>
        </el-table-column>

        <el-table-column prop="uboIdDocCopyMediaId" label="证件照片">
          <template slot-scope="scope">{{ scope.row.uboIdDocCopyMediaId }}</template>
        </el-table-column>

        <el-table-column prop="
uboIdDocName" label="证件姓名">
          <template slot-scope="scope">{{ scope.row.uboIdDocName }}</template>
        </el-table-column>

        <el-table-column prop="
uboIdDocNumber" label="证件号码">
          <template slot-scope="scope">{{ scope.row.uboIdDocNumber }}</template>
        </el-table-column>

        <el-table-column prop="uboDocPeriodBegin" label="证件有效期开始时间">
          <template slot-scope="scope">{{ scope.row.uboDocPeriodBegin }}</template>
        </el-table-column>

        <el-table-column prop="uboDocPeriodEnd" label="证件有效期结束时间 ">
          <template slot-scope="scope">{{ scope.row.uboDocPeriodEnd }}</template>
        </el-table-column>

        <el-table-column prop="merchantShortname" label="商户简称">
          <template slot-scope="scope">{{ scope.row.merchantShortname }}</template>
        </el-table-column>

        <el-table-column prop="servicePhone" label="客服电话 ">
          <template slot-scope="scope">{{ scope.row.servicePhone }}</template>
        </el-table-column>

        <el-table-column prop="salesScenesType" label="经营场景类型">
          <template slot-scope="scope">{{ scope.row.salesScenesType }}</template>
        </el-table-column>

        <el-table-column prop="bizStoreName" label="门店名称">
          <template slot-scope="scope">{{ scope.row.bizStoreName }}</template>
        </el-table-column>

        <el-table-column prop="bizAddressCode" label="门店省市编码 ">
          <template slot-scope="scope">{{ scope.row.bizAddressCode }}</template>
        </el-table-column>

        <el-table-column prop="bizStoreAddress" label="门店地址">
          <template slot-scope="scope">{{ scope.row.bizStoreAddress }}</template>
        </el-table-column>

        <el-table-column prop="storeEntrancePic" label="门店门头照片">
          <template slot-scope="scope">{{ scope.row.storeEntrancePic }}</template>
        </el-table-column>

        <el-table-column prop="storeEntrancePicMediaId" label="门店门头照片">
          <template slot-scope="scope">{{ scope.row.storeEntrancePicMediaId }}</template>
        </el-table-column>

        <el-table-column prop="indoorPic" label="店内环境照片">
          <template slot-scope="scope">{{ scope.row.indoorPic }}</template>
        </el-table-column>

        <el-table-column prop="indoorPicMediaId" label="店内环境照片">
          <template slot-scope="scope">{{ scope.row.indoorPicMediaId }}</template>
        </el-table-column>

        <el-table-column prop="bizSubAppid" label="线下场所对应的商家APPID ">
          <template slot-scope="scope">{{ scope.row.bizSubAppid }}</template>
        </el-table-column>

        <el-table-column prop="mpAppid" label="服务商公众号APPID ">
          <template slot-scope="scope">{{ scope.row.mpAppid }}</template>
        </el-table-column>

        <el-table-column prop="mpSubAppid" label="商家公众号APPID">
          <template slot-scope="scope">{{ scope.row.mpSubAppid }}</template>
        </el-table-column>

        <el-table-column prop="mpPics" label="公众号页面截图 ">
          <template slot-scope="scope">{{ scope.row.mpPics }}</template>
        </el-table-column>

        <el-table-column prop="mpPicsMediaId" label="公众号页面截图 ">
          <template slot-scope="scope">{{ scope.row.mpPicsMediaId }}</template>
        </el-table-column>

        <el-table-column prop="miniProgramAppid" label="服务商小程序APPID ">
          <template slot-scope="scope">{{ scope.row.miniProgramAppid }}</template>
        </el-table-column>

        <el-table-column prop="miniProgramSubAppid" label="服务商小程序APPID">
          <template slot-scope="scope">{{ scope.row.miniProgramSubAppid }}</template>
        </el-table-column>

        <el-table-column prop="miniProgramPics" label="服务商小程序页面截图 ">
          <template slot-scope="scope">{{ scope.row.miniProgramPics }}</template>
        </el-table-column>

        <el-table-column prop="miniProgramPicsMediaId" label="服务商小程序页面截图 ">
          <template slot-scope="scope">{{ scope.row.miniProgramPicsMediaId }}</template>
        </el-table-column>

        <el-table-column prop="appAppid" label="服务商应用APP的APPID ">
          <template slot-scope="scope">{{ scope.row.appAppid }}</template>
        </el-table-column>

        <el-table-column prop="appSubAppid" label="服务商应用APP的APPID">
          <template slot-scope="scope">{{ scope.row.appSubAppid }}</template>
        </el-table-column>

        <el-table-column prop="appPics" label="服务商应用APP的页面截图 ">
          <template slot-scope="scope">{{ scope.row.appPics }}</template>
        </el-table-column>

        <el-table-column prop="appPicsMediaId" label="服务商应用APP的页面截图 ">
          <template slot-scope="scope">{{ scope.row.appPicsMediaId }}</template>
        </el-table-column>

        <el-table-column prop="domain" label="互联网网站域名 ">
          <template slot-scope="scope">{{ scope.row.domain }}</template>
        </el-table-column>

        <el-table-column prop="webAuthorisation" label="网站授权函">
          <template slot-scope="scope">{{ scope.row.webAuthorisation }}</template>
        </el-table-column>

        <el-table-column prop="webAuthorisationMediaId" label="网站授权函">
          <template slot-scope="scope">{{ scope.row.webAuthorisationMediaId }}</template>
        </el-table-column>

        <el-table-column prop="webAppid" label="互联网网站对应的商家APPID ">
          <template slot-scope="scope">{{ scope.row.webAppid }}</template>
        </el-table-column>

        <el-table-column prop="subCorpId" label="商家企业微信CorpID ">
          <template slot-scope="scope">{{ scope.row.subCorpId }}</template>
        </el-table-column>

        <el-table-column prop="weworkPics" label="企业微信页面截图 ">
          <template slot-scope="scope">{{ scope.row.weworkPics }}</template>
        </el-table-column>

        <el-table-column prop="weworkPicsMediaId" label="企业微信页面截图 ">
          <template slot-scope="scope">{{ scope.row.weworkPicsMediaId }}</template>
        </el-table-column>

        <el-table-column prop="settlementId" label="入驻结算规则ID">
          <template slot-scope="scope">{{ scope.row.settlementId }}</template>
        </el-table-column>

        <el-table-column prop="qualificationType" label="所属行业 ">
          <template slot-scope="scope">{{ scope.row.qualificationType }}</template>
        </el-table-column>

        <el-table-column prop="qualifications" label="特殊资质图片">
          <template slot-scope="scope">{{ scope.row.qualifications }}</template>
        </el-table-column>

        <el-table-column prop="qualificationsMediaId" label="特殊资质图片">
          <template slot-scope="scope">{{ scope.row.qualificationsMediaId }}</template>
        </el-table-column>

        <el-table-column prop="activitiesId" label="优惠费率活动ID ">
          <template slot-scope="scope">{{ scope.row.activitiesId }}</template>
        </el-table-column>

        <el-table-column prop="
activitiesRate" label="优惠费率活动值">
          <template slot-scope="scope">{{ scope.row.activitiesRate }}</template>
        </el-table-column>

        <el-table-column prop="activitiesAdditions" label="优惠费率活动补充材料 ">
          <template slot-scope="scope">{{ scope.row.activitiesAdditions }}</template>
        </el-table-column>

        <el-table-column prop="activitiesAdditionsMedia" label="优惠费率活动补充材料 ">
          <template slot-scope="scope">{{ scope.row.activitiesAdditionsMedia }}</template>
        </el-table-column>

        <el-table-column prop="bankAccountType" label="账户类型">
          <template slot-scope="scope">{{ scope.row.bankAccountType }}</template>
        </el-table-column>

        <el-table-column prop="accountName" label="开户名称">
          <template slot-scope="scope">{{ scope.row.accountName }}</template>
        </el-table-column>

        <el-table-column prop="accountBank" label="开户银行">
          <template slot-scope="scope">{{ scope.row.accountBank }}</template>
        </el-table-column>

        <el-table-column prop="bankAddressCode" label="开户银行省市编码">
          <template slot-scope="scope">{{ scope.row.bankAddressCode }}</template>
        </el-table-column>

        <el-table-column prop="bankBranchId" label="开户银行联行号">
          <template slot-scope="scope">{{ scope.row.bankBranchId }}</template>
        </el-table-column>

        <el-table-column prop="bankName" label="开户银行全称（含支行) ">
          <template slot-scope="scope">{{ scope.row.bankName }}</template>
        </el-table-column>

        <el-table-column prop="
accountNumber" label="银行账号">
          <template slot-scope="scope">{{ scope.row.accountNumber }}</template>
        </el-table-column>

        <el-table-column prop="legalPersonCommitment" label="法人开户承诺函">
          <template slot-scope="scope">{{ scope.row.legalPersonCommitment }}</template>
        </el-table-column>

        <el-table-column prop="legalPersonCommitmentMediaId" label="法人开户承诺函">
          <template slot-scope="scope">{{ scope.row.legalPersonCommitmentMediaId }}</template>
        </el-table-column>

        <el-table-column prop="legalPersonVideo" label="法人开户意愿视频">
          <template slot-scope="scope">{{ scope.row.legalPersonVideo }}</template>
        </el-table-column>

        <el-table-column prop="legalPersonVideoMediaId" label="法人开户意愿视频">
          <template slot-scope="scope">{{ scope.row.legalPersonVideoMediaId }}</template>
        </el-table-column>

        <el-table-column prop="businessAdditionPics" label="补充材料">
          <template slot-scope="scope">{{ scope.row.businessAdditionPics }}</template>
        </el-table-column>

        <el-table-column prop="businessAdditionPicsMediaId" label="补充材料">
          <template slot-scope="scope">{{ scope.row.businessAdditionPicsMediaId }}</template>
        </el-table-column>

        <el-table-column prop="businessAdditionMsg" label="补充说明 ">
          <template slot-scope="scope">{{ scope.row.businessAdditionMsg }}</template>
        </el-table-column>

        <el-table-column prop="applymentId" label="微信支付申请单号 ">
          <template slot-scope="scope">{{ scope.row.applymentId }}</template>
        </el-table-column>

        <el-table-column prop="applymentState" label="申请单状态">
          <template slot-scope="scope">{{ scope.row.applymentState }}</template>
        </el-table-column>

        <el-table-column prop="subMchid" label="特约商户号 ">
          <template slot-scope="scope">{{ scope.row.subMchid }}</template>
        </el-table-column>

        <el-table-column prop="signUrl" label="超级管理员签约链接">
          <template slot-scope="scope">{{ scope.row.signUrl }}</template>
        </el-table-column>

        <el-table-column prop="applymentStateMsg" label="申请状态描述 ">
          <template slot-scope="scope">{{ scope.row.applymentStateMsg }}</template>
        </el-table-column>

        <el-table-column prop="field" label="字段名 ">
          <template slot-scope="scope">{{ scope.row.field }}</template>
        </el-table-column>

        <el-table-column prop="fieldName" label="字段名称">
          <template slot-scope="scope">{{ scope.row.fieldName }}</template>
        </el-table-column>

        <el-table-column prop="rejectReason" label="驳回原因">
          <template slot-scope="scope">{{ scope.row.rejectReason }}</template>
        </el-table-column>

        <el-table-column label="操作" width="200" align="center">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleUpdate(scope.$index, scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="batch-operate-container"></div>
    <div class="pagination-container">
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
  </div>
</template>
<script>
//将$都替换为$
import {
  fetchList,
  deleteMerchatBusinessMaterials
} from "@/api/merchat/merchatBusinessMaterials";
import { formatDate } from "@/utils/date";
import axios from "axios";
import { getToken } from "@/utils/auth";
import { Message } from "element-ui";

export default {
  name: "merchatBusinessMaterialsList",
  data() {
    return {
      operates: [],
      operateType: null,
      listQuery: {
        keyword: null,
        pageNum: 1,
        pageSize: 10
      },
      list: null,
      total: null,
      listLoading: true,
      multipleSelection: [],
      url: process.env.BASE_API + "/merchat/merchatBusinessMaterials/list"
    };
  },
  created() {
    this.getList();
  },
  filters: {
    formatCreateTime(time) {
      let date = new Date(time);
      return formatDate(date, "yyyy-MM-dd hh:mm:ss");
    },

    formatStatus(value) {
      if (value === 1) {
        return "未开始";
      } else if (value === 2) {
        return "活动中";
      } else if (value === 3) {
        return "已结束";
      } else if (value === 4) {
        return "已失效";
      }
    }
  },
  methods: {
    getList() {
      this.listLoading = true;

      axios({
        method: "get",
        url: this.url,
        params: this.listQuery,
        headers: {
          Authorization: getToken(),
        }
      }).then(response => {
		this.listLoading = false;
		const res = response.data;
        this.list = res.records;
        this.total = res.total;
        this.totalPage = res.pages;
        this.pageSize = res.size;

        
        if (res.code !== 200 && !res.access_token) {
          console.log(res);
          Message({
            message: res.msg,
            type: "error",
            duration: 3 * 1000
          });
        }
      });

      // fetchList(this.listQuery).then(response => {
      // 	this.listLoading = false;
      // 	this.list = response.data.records;
      // 	this.total = response.data.total;
      // 	this.totalPage = response.data.pages;
      // 	this.pageSize = response.data.size;
      // });
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    handleUpdate(index, row) {
      this.$router.push({
        path: "/merchat/updateMerchatBusinessMaterials",
        query: { id: row.id }
      });
    },
    handleDelete(index, row) {
      this.$confirm("是否要删除该类型", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        deleteMerchatBusinessMaterials(row.id).then(response => {
          this.$message({
            message: "删除成功",
            type: "success",
            duration: 1000
          });
          this.getList();
        });
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
    searchMerchatBusinessMaterialsList() {
      this.listQuery.pageNum = 1;
      this.getList();
    },
    handleBatchOperate() {
      console.log(this.multipleSelection);
      if (this.multipleSelection < 1) {
        this.$message({
          message: "请选择一条记录",
          type: "warning",
          duration: 1000
        });
        return;
      }
      let showStatus = 0;
      if (this.operateType === "showMerchatBusinessMaterials") {
        showStatus = 1;
      } else if (this.operateType === "hideMerchatBusinessMaterials") {
        showStatus = 0;
      } else {
        this.$message({
          message: "请选择批量操作类型",
          type: "warning",
          duration: 1000
        });
        return;
      }
      let ids = [];
      for (let i = 0; i < this.multipleSelection.length; i++) {
        ids.push(this.multipleSelection[i].id);
      }
      let data = new URLSearchParams();
      data.append("ids", ids);
      data.append("showStatus", showStatus);
      updateShowStatus(data).then(response => {
        this.getList();
        this.$message({
          message: "修改成功",
          type: "success",
          duration: 1000
        });
      });
    },
    addMerchatBusinessMaterials() {
      //手动将router,改成$router
      this.$router.push({ path: "/merchat/addMerchatBusinessMaterials" });
    }
  }
};
</script>
<style rel="stylesheet/scss" lang="scss" scoped></style>
