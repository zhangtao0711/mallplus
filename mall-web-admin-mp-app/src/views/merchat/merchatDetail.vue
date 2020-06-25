<template>
  <el-card class="form-container" shadow="never" style="width:75%">
    <el-button type="primary55" @click="goback">返回列表</el-button>
    <div class="table-container">
      <el-row>
        <el-col :span="8">
          <div class="bg-purple">业务申请编号</div>
        </el-col>
        <el-col :span="16">
          <div class="bg-purple-light">{{ resultList.businessCode }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <div class="bg-purple">超级管理员姓名</div>
        </el-col>
        <el-col :span="16">
          <div class="bg-purple-light">{{ resultList.contactName }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <div class="bg-purple">超级管理员身份证件号码</div>
        </el-col>
        <el-col :span="16">
          <div class="bg-purple-light">{{ resultList.contactIdNumber }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <div class="bg-purple">超级管理员微信openid</div>
        </el-col>
        <el-col :span="16">
          <div class="bg-purple-light">{{ resultList.openid }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <div class="bg-purple">联系手机</div>
        </el-col>
        <el-col :span="16">
          <div class="bg-purple-light">{{ resultList.mobilePhone }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <div class="bg-purple">联系邮箱</div>
        </el-col>
        <el-col :span="16">
          <div class="bg-purple-light">{{ resultList.contactEmail }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <div class="bg-purple">主体类型</div>
        </el-col>
        <el-col :span="16">
          <div class="bg-purple-light">{{ resultList.subjectType | formatStatus }}</div>
        </el-col>
      </el-row>

      <!-- 营业执照 -->
      <p
        v-if="resultList.subjectType=='SUBJECT_TYPE_INDIVIDUAL' || resultList.subjectType=='SUBJECT_TYPE_ENTERPRISE' "
      >
        <el-row>
          <el-col :span="8">
            <div class="bg-purple">营业执照照片</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">
              <el-image :src="myurl + resultList.licenseCopy" style="width:100px;height:100px"></el-image>
            </div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <div class="bg-purple">注册号/统一社会信用代码</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">{{ resultList.licenseNumber }}</div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <div class="bg-purple">商户名称</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">{{ resultList.subjectMerchantName }}</div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <div class="bg-purple">个体户经营者/法人姓名</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">{{ resultList.subjectLegalPerson }}</div>
          </el-col>
        </el-row>
      </p>

      <!-- 登记证书 -->
      <p
        v-if="resultList.subjectType=='SUBJECT_TYPE_INSTITUTIONS' || resultList.subjectType=='SUBJECT_TYPE_OTHERS' "
      >
        <el-row>
          <el-col :span="8">
            <div class="bg-purple">登记证书照片</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">
              <el-image :src="myurl + resultList.certCopy" style="width:100px;height:100px"></el-image>
            </div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <div class="bg-purple">登记证书类型</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">{{ resultList.certType }}</div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <div class="bg-purple">证书号</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">{{ resultList.certNumber }}</div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <div class="bg-purple">商户名称</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">{{ resultList.merchantName }}</div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <div class="bg-purple">注册地址</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">{{ resultList.companyAddress }}</div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <div class="bg-purple">法人姓名</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">{{ resultList.legalPerson }}</div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <div class="bg-purple">有效期限开始日期</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">{{ resultList.periodBegin }}</div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <div class="bg-purple">有效期限结束日期</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">{{ resultList.periodEnd }}</div>
          </el-col>
        </el-row>
      </p>

      <!-- 组织机构代码证 -->
      <p
        v-if="resultList.subjectType=='SUBJECT_TYPE_ENTERPRISE' || resultList.subjectType=='SUBJECT_TYPE_INSTITUTIONS' || resultList.subjectType=='SUBJECT_TYPE_OTHERS' "
      >
        <el-row>
          <el-col :span="8">
            <div class="bg-purple">组织机构代码证照片</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">
              <el-image :src="myurl + resultList.organizationCopy" style="width:100px;height:100px"></el-image>
            </div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <div class="bg-purple">组织机构代码</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">{{ resultList.organizationCode }}</div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <div class="bg-purple">组织机构代码证有效期开始日期</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">{{ resultList.orgPeriodBegin }}</div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <div class="bg-purple">组织机构代码证有效期结束日期</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">{{ resultList.orgPeriodEnd }}</div>
          </el-col>
        </el-row>
      </p>

      <!-- 单位证明函 -->
      <p v-if="resultList.subjectType=='SUBJECT_TYPE_INSTITUTIONS'">
        <el-row>
          <el-col :span="8">
            <div class="bg-purple">单位证明函照片</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">
              <el-image
                :src="myurl + resultList.certificateLetterCopy"
                style="width:100px;height:100px"
              ></el-image>
            </div>
          </el-col>
        </el-row>
      </p>

      <el-row>
        <el-col :span="8">
          <div class="bg-purple">证件类型</div>
        </el-col>
        <el-col :span="16">
          <div class="bg-purple-light">{{ resultList.idDocType | formatIdDocType }}</div>
        </el-col>
      </el-row>

      <!-- 身份证 -->
      <p v-if="resultList.idDocType=='IDENTIFICATION_TYPE_IDCARD'">
        <el-row>
          <el-col :span="8">
            <div class="bg-purple">身份证人像面照片</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">
              <el-image :src="myurl + resultList.idCardCopy" style="width:100px;height:100px"></el-image>
            </div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <div class="bg-purple">身份证国徽面照片</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">
              <el-image :src="myurl + resultList.idCardNational" style="width:100px;height:100px"></el-image>
            </div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <div class="bg-purple">身份证姓名</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">{{ resultList.idCardName }}</div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <div class="bg-purple">身份证号码</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">{{ resultList.idCardNumber }}</div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <div class="bg-purple">身份证有效期开始时间</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">{{ resultList.cardPeriodBegin | formatDate }}</div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <div class="bg-purple">身份证有效期结束时间</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">{{ resultList.cardPeriodEnd | formatDate }}</div>
          </el-col>
        </el-row>
      </p>
      <!-- 其他证件类型 -->
      <p v-else>
        <el-row>
          <el-col :span="8">
            <div class="bg-purple">证件照片</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">
              <el-image :src="myurl + resultList.idDocCopy" style="width:100px;height:100px"></el-image>
            </div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <div class="bg-purple">证件姓名</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">{{ resultList.idDocName }}</div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <div class="bg-purple">证件号码</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">{{ resultList.idDocNumber }}</div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <div class="bg-purple">证件有效期开始时间</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">{{ resultList.docPeriodBegin | formatDate }}</div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <div class="bg-purple">证件有效期结束时间</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">{{ resultList.docPeriodEnd | formatDate }}</div>
          </el-col>
        </el-row>
      </p>

      <el-row>
        <el-col :span="8">
          <div class="bg-purple">经营者/法人是否为受益人</div>
        </el-col>
        <el-col :span="16">
          <div class="bg-purple-light">{{ resultList.owner | formatOwner }}</div>
        </el-col>
      </el-row>

      <p v-if="resultList.owner=='0'">
        <el-row>
          <el-col :span="8">
            <div class="bg-purple">证件类型</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">{{ resultList.idType | formatIdDocType }}</div>
          </el-col>
        </el-row>
      </p>

      <p v-if="resultList.idType =='IDENTIFICATION_TYPE_IDCARD' && resultList.owner=='0'">
        <el-row>
          <el-col :span="8">
            <div class="bg-purple">身份证人像面照片</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">
              <el-image :src="myurl + resultList.uboIdCardCopy" style="width:100px;height:100px"></el-image>
            </div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <div class="bg-purple">身份证国徽面照片</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">
              <el-image
                :src="myurl + resultList.uboIdCardNational"
                style="width:100px;height:100px"
              ></el-image>
            </div>
          </el-col>
        </el-row>
      </p>
      <p v-else-if="resultList.idType !='IDENTIFICATION_TYPE_IDCARD'&&resultList.owner=='0'">
        <el-row>
          <el-col :span="8">
            <div class="bg-purple">证件照片</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">
              <el-image :src="myurl + resultList.uboIdDocCopy" style="width:100px;height:100px"></el-image>
            </div>
          </el-col>
        </el-row>
      </p>
      <p v-if="resultList.owner=='0'">
        <el-row>
          <el-col :span="8">
            <div class="bg-purple">证件姓名</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">{{ resultList.uboName }}</div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <div class="bg-purple">证件号码</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">{{ resultList.uboIdNumber }}</div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <div class="bg-purple">证件有效期开始时间</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">{{ resultList.uboIdPeriodBegin }}</div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <div class="bg-purple">证件有效期结束时间</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">{{ resultList.uboIdPeriodEnd }}</div>
          </el-col>
        </el-row>
      </p>

      <el-row>
        <el-col :span="8">
          <div class="bg-purple">商户简称</div>
        </el-col>
        <el-col :span="16">
          <div class="bg-purple-light">{{ resultList.merchantShortname }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <div class="bg-purple">客服电话</div>
        </el-col>
        <el-col :span="16">
          <div class="bg-purple-light">{{ resultList.servicePhone }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <div class="bg-purple">经营场景类型</div>
        </el-col>
        <el-col :span="16">
          <div class="bg-purple-light">{{ resultList.salesScenesType | formatScenesType }}</div>
        </el-col>
      </el-row>

      <!-- 线下门店 -->
      <p v-if="resultList.salesScenesType=='SALES_SCENES_STORE'">
        <el-row>
          <el-col :span="8">
            <div class="bg-purple">门店名称</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">{{ resultList.bizStoreName }}</div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <div class="bg-purple">门店省市编码</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">{{ resultList.bizAddressCode }}</div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <div class="bg-purple">门店地址</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">{{ resultList.bizStoreAddress }}</div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <div class="bg-purple">门店门头照片</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">
              <el-image :src="myurl + resultList.storeEntrancePic" style="width:100px;height:100px"></el-image>
            </div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <div class="bg-purple">店内环境照片</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">
              <el-image :src="myurl + resultList.indoorPic" style="width:100px;height:100px"></el-image>
            </div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <div class="bg-purple">线下场所对应的商家APPID</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">{{ resultList.bizSubAppid }}</div>
          </el-col>
        </el-row>
      </p>

      <!-- 公众号 -->
      <p v-else-if="resultList.salesScenesType=='SALES_SCENES_MP'">
        <el-row>
          <el-col :span="8">
            <div class="bg-purple">服务商公众号APPID</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">{{ resultList.mpAppid }}</div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <div class="bg-purple">商家公众号APPID</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">{{ resultList.mpSubAppid }}</div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <div class="bg-purple">公众号页面截图</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">
              <el-image :src="myurl + resultList.mpPics" style="width:100px;height:100px"></el-image>
            </div>
          </el-col>
        </el-row>
      </p>
      <!-- 小程序 -->
      <p v-else-if="resultList.salesScenesType=='SALES_SCENES_MINI_PROGRAM'">
        <el-row>
          <el-col :span="8">
            <div class="bg-purple">服务商小程序APPID</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">{{ resultList.miniProgramAppid }}</div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <div class="bg-purple">商家小程序APPID</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">{{ resultList.miniProgramSubAppid }}</div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <div class="bg-purple">服务商小程序页面截图</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">
              <el-image :src="myurl + resultList.miniProgramPics" style="width:100px;height:100px"></el-image>
            </div>
          </el-col>
        </el-row>
      </p>
      <!-- APP -->
      <p v-else-if="resultList.salesScenesType=='SALES_SCENES_APP'">
        <el-row>
          <el-col :span="8">
            <div class="bg-purple">服务商应用APP的APPID</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">{{ resultList.appAppid }}</div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <div class="bg-purple">商家应用APP的APPID</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">{{ resultList.appSubAppid }}</div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <div class="bg-purple">服务商应用APP的页面截图</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">
              <el-image :src="myurl + resultList.appPics" style="width:100px;height:100px"></el-image>
            </div>
          </el-col>
        </el-row>
      </p>
      <!-- 互联网 -->
      <p v-else-if="resultList.salesScenesType=='SALES_SCENES_WEB'">
        <el-row>
          <el-col :span="8">
            <div class="bg-purple">互联网网站域名</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">{{ resultList.domain }}</div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <div class="bg-purple">网站授权函</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">
              <el-image :src="myurl + resultList.webAuthorisation" style="width:100px;height:100px"></el-image>
            </div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <div class="bg-purple">互联网网站对应的商家APPID</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">{{ resultList.webAppid }}</div>
          </el-col>
        </el-row>
      </p>
      <!-- 企业微信 -->
      <p v-else-if="resultList.salesScenesType=='SALES_SCENES_WEWORK'">
        <el-row>
          <el-col :span="8">
            <div class="bg-purple">商家企业微信CorpID</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">{{ resultList.subCorpId }}</div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <div class="bg-purple">企业微信页面截图</div>
          </el-col>
          <el-col :span="16">
            <div class="bg-purple-light">
              <el-image :src="myurl + resultList.weworkPics" style="width:100px;height:100px"></el-image>
            </div>
          </el-col>
        </el-row>
      </p>
      <el-row>
        <el-col :span="8">
          <div class="bg-purple">入驻结算规则ID</div>
        </el-col>
        <el-col :span="16">
          <div class="bg-purple-light">{{ resultList.settlementId }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <div class="bg-purple">所属行业</div>
        </el-col>
        <el-col :span="16">
          <div class="bg-purple-light">{{ resultList.qualificationType }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <div class="bg-purple">特殊资质图片</div>
        </el-col>
        <el-col :span="16">
          <div class="bg-purple-light">
            <el-image :src="myurl + resultList.qualifications" style="width:100px;height:100px"></el-image>
          </div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <div class="bg-purple">优惠费率活动ID</div>
        </el-col>
        <el-col :span="16">
          <div class="bg-purple-light">{{ resultList.activitiesId }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <div class="bg-purple">优惠费率活动值</div>
        </el-col>
        <el-col :span="16">
          <div class="bg-purple-light">{{ resultList.activitiesRate }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <div class="bg-purple">优惠费率活动补充材料</div>
        </el-col>
        <el-col :span="16">
          <div class="bg-purple-light">{{ resultList.activitiesAdditions }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <div class="bg-purple">账户类型</div>
        </el-col>
        <el-col :span="16">
          <div class="bg-purple-light">{{ resultList.bankAccountType | formatBankType }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <div class="bg-purple">开户名称</div>
        </el-col>
        <el-col :span="16">
          <div class="bg-purple-light">{{ resultList.accountName }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <div class="bg-purple">开户银行</div>
        </el-col>
        <el-col :span="16">
          <div class="bg-purple-light">{{ resultList.accountBank }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <div class="bg-purple">开户银行省市编码</div>
        </el-col>
        <el-col :span="16">
          <div class="bg-purple-light">{{ resultList.bankAddressCode }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <div class="bg-purple">开户银行联行号</div>
        </el-col>
        <el-col :span="16">
          <div class="bg-purple-light">{{ resultList.bankBranchId }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <div class="bg-purple">开户银行全称（含支行）</div>
        </el-col>
        <el-col :span="16">
          <div class="bg-purple-light">{{ resultList.bankName }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <div class="bg-purple">银行账号</div>
        </el-col>
        <el-col :span="16">
          <div class="bg-purple-light">{{ resultList.accountNumber }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <div class="bg-purple">法人开户承诺函</div>
        </el-col>
        <el-col :span="16">
          <div class="bg-purple-light">
            <el-image
              :src="myurl + resultList.legalPersonCommitment"
              style="width:100px;height:100px"
            ></el-image>
          </div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <div class="bg-purple">法人开户意愿视频</div>
        </el-col>
        <el-col :span="16">
          <div class="bg-purple-light">
            <el-image :src="myurl + resultList.legalPersonVideo" style="width:100px;height:100px"></el-image>
          </div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <div class="bg-purple">补充材料</div>
        </el-col>
        <el-col :span="16">
          <div class="bg-purple-light">
            <el-image
              :src="myurl + resultList.businessAdditionPics"
              style="width:100px;height:100px"
            ></el-image>
          </div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <div class="bg-purple">补充说明</div>
        </el-col>
        <el-col :span="16">
          <div class="bg-purple-light">{{ resultList.businessAdditionMsg }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <div class="bg-purple">微信支付申请单号</div>
        </el-col>
        <el-col :span="16">
          <div class="bg-purple-light">{{ resultList.applymentId }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <div class="bg-purple">申请单状态</div>
        </el-col>
        <el-col :span="16">
          <div class="bg-purple-light">{{ resultList.applymentState | formatApplymentState }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <div class="bg-purple">特约商户号</div>
        </el-col>
        <el-col :span="16">
          <div class="bg-purple-light">{{ resultList.subMchid }}</div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <div class="bg-purple">超级管理员签约链接</div>
        </el-col>
        <el-col :span="16">
          <div class="bg-purple-light">
            <a :href="resultList.signUrl" target="_blank">超级管理员签约链接</a>
          </div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <div class="bg-purple">驳回原因</div>
        </el-col>
        <el-col :span="16">
          <div class="bg-purple-light">{{ resultList.rejectReason }}</div>
        </el-col>
      </el-row>
    </div>
  </el-card>
</template>
<script>
import { formatDate } from "@/utils/date";
import { queryByBusinessCode } from "@/api/merchat/merchatBusinessMaterials";
export default {
  name: "BrandDetail",
  filters: {
    formatStatus(value) {
      if (value === "SUBJECT_TYPE_INDIVIDUAL") {
        return "个体户";
      } else if (value === "SUBJECT_TYPE_ENTERPRISE") {
        return "企业";
      } else if (value === "SUBJECT_TYPE_INSTITUTIONS") {
        return "党政、机关及事业单位";
      } else if (value === "SUBJECT_TYPE_OTHERS") {
        return "其他组织";
      }
    },
    formatIdDocType(value) {
      if (value === "IDENTIFICATION_TYPE_IDCARD") {
        return "中国大陆居民-身份证";
      } else if (value === "IDENTIFICATION_TYPE_OVERSEA_PASSPORT") {
        return "其他国家或地区居民-护照";
      } else if (value === "IDENTIFICATION_TYPE_HONGKONG_PASSPORT") {
        return "中国香港居民-来往内地通行证";
      } else if (value === "IDENTIFICATION_TYPE_MACAO_PASSPORT") {
        return "中国澳门居民-来往内地通行证";
      } else if (value === "IDENTIFICATION_TYPE_TAIWAN_PASSPORT") {
        return "中国台湾居民-来往大陆通行证";
      }
    },
    formatScenesType(value) {
      if (value === "SALES_SCENES_STORE") {
        return "线下门店";
      } else if (value === "SALES_SCENES_MP") {
        return "公众号";
      } else if (value === "SALES_SCENES_MINI_PROGRAM") {
        return "小程序";
      } else if (value === "SALES_SCENES_WEB") {
        return "互联网";
      } else if (value === "SALES_SCENES_APP") {
        return "APP";
      } else if (value === "SALES_SCENES_WEWORK") {
        return "企业微信";
      }
    },
    formatBankType(val) {
      if (val == "BANK_ACCOUNT_TYPE_CORPORATE") {
        return "对公银行账户";
      } else if (val == "BANK_ACCOUNT_TYPE_PERSONAL") {
        return "经营者个人银行卡";
      }
    },
    formatApplymentState(value) {
      if (value === "APPLYMENT_STATE_EDITTING") {
        return "编辑中：提交申请发生错误导致，请尝试重新提交";
      } else if (value === "APPLYMENT_STATE_AUDITING") {
        return "审核中：申请单正在审核中，超级管理员用微信打开“签约链接”，完成绑定微信号后，申请单进度将通过微信公众号通知超级管理员，引导完成后续步骤";
      } else if (value === "APPLYMENT_STATE_REJECTED") {
        return "已驳回：请按照驳回原因修改申请资料，超级管理员用微信打开“签约链接”，完成绑定微信号，后续申请单进度将通过微信公众号通知超级管理员";
      } else if (value === "APPLYMENT_STATE_TO_BE_CONFIRMED") {
        return "待账户验证：请超级管理员使用微信打开返回的“签约链接”，根据页面指引完成账户验证";
      } else if (value === "APPLYMENT_STATE_TO_BE_SIGNED") {
        return "待签约：请超级管理员使用微信打开返回的“签约链接”，根据页面指引完成签约";
      } else if (value === "APPLYMENT_STATE_SIGNING") {
        return "开通中：系统开通相关权限中，请耐心等待";
      } else if (value === "APPLYMENT_STATE_FINISHED") {
        return "已完成：商户入驻申请已完成";
      } else if (value === "APPLYMENT_STATE_CANCELED") {
        return "已作废：申请单已被撤销";
      }
    },
    formatOwner(val) {
      if (val == "1") {
        return "是";
      } else if (val == "0") {
        return "否";
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
      resultList: null,
      myurl: process.env.BASE_API
    };
  },
  created() {
    this.getMember();
  },
  methods: {
    goback() {
      this.$router.back();
    },
    getMember() {
      queryByBusinessCode({
        businessCode: this.$route.query.businessCode
      }).then(response => {
        this.resultList = response.data;
      });
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
a {
  color: dodgerblue;
  text-decoration: underline;
}
.el-tag + .el-tag {
  margin-left: 10px;
}

.table-container {
  margin: 20px auto;
}
.el-row {
  margin-bottom: 20px;
  text-align: center;
  min-height: 40px;

  &:last-child {
    margin-bottom: 0;
  }
  .bg-purple {
    padding: 10px;
    background: #d3dce6;
  }
  .bg-purple-light {
    padding: 10px;
    background: #e5e9f2;
  }
}
</style>


