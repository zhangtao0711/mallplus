import Vue from 'vue'
import Router from 'vue-router'
/* Layout */
import Layout from '../views/layout/Layout'

Vue.use(Router)

/**
 * hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu, whatever its child routes length
 *                                if not set alwaysShow, only more than one route under the children
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noredirect           if `redirect:noredirect` will no redirct in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    title: 'title'               the name show in submenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar,
  }
 **/
export const constantRouterMap = [
  { path: '/acceptStore', component: () => import('@/views/login/acceptStore'), hidden: true },
  { path: '/login', component: () => import('@/views/login/index'), hidden: true },
  { path: '/forgetPassword', component: () => import('@/views/login/forgetPassword'), hidden: true },
  { path: '/retrievePassword', component: () => import('@/views/login/retrievePassword'), hidden: true },
  { path: '/newPasword', component: () => import('@/views/login/newPasword'), hidden: true },
  { path: '/comPlete', component: () => import('@/views/login/comPlete'), hidden: true },
  { path: '/contactAdmin', component: () => import('@/views/login/contactAdmin'), hidden: true },
  { path: '/index1', component: () => import('@/views/home/index1'), hidden: true },
  { path: '/404', component: () => import('@/views/404'), hidden: true },
  {
    path: '/freightTemplate',
    component: Layout,
    redirect: '/freightTemplate/FeightTemplate',
    name: 'freightTemplate',
    meta: { title: '运费模板', icon: 'home' },
    children: [
      {
        path: 'FeightTemplate',
        name: 'FeightTemplate',
        component: () => import('@/views/pms/feightTemplate/index'),
        meta: { title: '运费模版', icon: 'product-list' }
      },
      {
        path: 'addFeightTemplate',
        name: 'addFeightTemplate',
        component: () => import('@/views/pms/feightTemplate/add'),
        meta: { title: '添加运费模版' },
        hidden: true
      },
      {
        path: 'updateFeightTemplate',
        name: 'updateFeightTemplate',
        component: () => import('@/views/pms/feightTemplate/update'),
        meta: { title: '编辑运费模版' },
        hidden: true
      }
    ]
  },
  {
    path: '/merchandise',
    component: Layout,
    redirect: '/merchandise/smsWaterPage',
    name: 'merchandise',
    meta: { title: '商品列表', icon: 'home' },
    children: [
      // 配件商品
      {
        path: 'fittingsProduct',
        name: 'fittingsProduct',
        component: () => import('@/views/merchandise/product/index'),
        meta: { title: '配件商品列表', icon: 'product-list' }
      },
      {
        path: 'addFittingsProduct',
        name: 'addFittingsProduct',
        component: () => import('@/views/merchandise/product/add'),
        meta: { title: '添加配件商品', icon: 'product-add' }
      },
      {
        path: 'updateFittingsProduct',
        name: 'updateFittingsProduct',
        component: () => import('@/views/merchandise/product/update'),
        meta: { title: '修改配件商品', icon: 'product-add' },
        hidden: true
      },
      {
        path: 'productFittingsRecycle',
        name: 'productFittingsRecycle',
        component: () => import('@/views/merchandise/product/index'),
        meta: { title: '商品回收站', icon: 'product-recycle' },
        hidden: true
      },
      // 积分商品
      {
        path: 'integrationProduct',
        name: 'integrationProduct',
        component: () => import('@/views/merchandise/integrationProduct/index'),
        meta: { title: '积分商品列表', icon: 'product-list' }
      },
      {
        path: 'addIntegrationProduct',
        name: 'addIntegrationProduct',
        component: () => import('@/views/merchandise/integrationProduct/add'),
        meta: { title: '添加积分商品', icon: 'product-add' }
      },
      {
        path: 'updateIntegrationProduct',
        name: 'updateIntegrationProduct',
        component: () => import('@/views/merchandise/integrationProduct/update'),
        meta: { title: '修改积分商品', icon: 'product-add' },
        hidden: true
      },
      {
        path: 'productIntegrationRecycle',
        name: 'productIntegrationRecycle',
        component: () => import('@/views/merchandise/integrationProduct/index'),
        meta: { title: '商品回收站', icon: 'product-recycle' },
        hidden: true
      },
      // 普通商品
      {
        path: 'ordinaryProduct',
        name: 'ordinaryProduct',
        component: () => import('@/views/merchandise/ordinaryProduct/index'),
        meta: { title: '普通商品列表', icon: 'product-list' }
      },
      {
        path: 'addOrdinaryProduct',
        name: 'addOrdinaryProduct',
        component: () => import('@/views/merchandise/ordinaryProduct/add'),
        meta: { title: '普通积分商品', icon: 'product-add' }
      },
      {
        path: 'updateOrdinaryProduct',
        name: 'updateOrdinaryProduct',
        component: () => import('@/views/merchandise/ordinaryProduct/update'),
        meta: { title: '普通积分商品', icon: 'product-add' },
        hidden: true
      },
      {
        path: 'productOrdinaryRecycle',
        name: 'productOrdinaryRecycle',
        component: () => import('@/views/merchandise/ordinaryProduct/index'),
        meta: { title: '普通回收站', icon: 'product-recycle' },
        hidden: true
      },
      // 标签管理
      {
        path: 'umsMemberTag',
        name: 'umsMemberTag',
        component: () => import('@/views/merchandise/umsMemberTag/index'),
        meta: { title: '标签列表', icon: 'product-list' }
      },

      {
        path: 'addCommodityLabel',
        name: 'addCommodityLabel',
        component: () => import('@/views/merchandise/umsMemberTag/add'),
        meta: { title: '添加标签' },
        hidden: true
      },
      {
        path: 'updateCommodityLabel',
        name: 'updateCommodityLabel',
        component: () => import('@/views/merchandise/umsMemberTag/update'),
        meta: { title: '编辑标签' },
        hidden: true
      },
      // 商品分类
      {
        path: 'productCate',
        name: 'productCate',
        component: () => import('@/views/merchandise/productCate/index'),
        meta: { title: '商品分类', icon: 'product-cate' }
      },
      {
        path: 'addProductCate',
        name: 'addProductCate',
        component: () => import('@/views/merchandise/productCate/add'),
        meta: { title: '添加商品分类' },
        hidden: true
      },
      {
        path: 'updateProductCate',
        name: 'updateProductCate',
        component: () => import('@/views/merchandise/productCate/update'),
        meta: { title: '修改商品分类' },
        hidden: true
      },

      // 购水
      {
        path: 'smsWaterPage',
        name: 'smsWaterPage',
        component: () => import('@/views/merchandise/smsWaterPage/index'),
        meta: { title: '购水页面列表', icon: 'product-list' }
      },
      {
        path: 'addSmsWaterPage',
        name: 'addSmsWaterPage',
        component: () => import('@/views/merchandise/smsWaterPage/add'),
        meta: { title: '添加购水页面' },
        hidden: true
      },
      {
        path: 'updateSmsWaterPage',
        name: 'updateSmsWaterPage',
        component: () => import('@/views/merchandise/smsWaterPage/update'),
        meta: { title: '编辑购水页面' },
        hidden: true
      },

      // 套餐
      {
        path: 'smsRechargePackage',
        name: 'smsRechargePackage',
        component: () => import('@/views/merchandise/smsRechargePackage/index'),
        meta: { title: '充值套餐列表', icon: 'product-list' }
      },
      {
        path: 'addSmsRechargePackage',
        name: 'addSmsRechargePackage',
        component: () => import('@/views/merchandise/smsRechargePackage/add'),
        meta: { title: '添加充值套餐' },
        hidden: true
      },
      {
        path: 'updateSmsRechargePackage',
        name: 'updateSmsRechargePackage',
        component: () => import('@/views/merchandise/smsRechargePackage/update'),
        meta: { title: '编辑充值套餐' },
        hidden: true
      },
    ]
  },
  {
    path: '/equipment',
    component: Layout,
    redirect: '/equipment/filterList',
    name: 'equipment',
    meta: { title: '设备管理', icon: 'home' },
    children: [
      {
        path: 'simList',
        name: 'simList',
        component: () => import('@/views/equipment/wtSimCard/index'),
        meta: { title: 'SIM卡列表', icon: 'product-list' }
      },
      {
        path: 'addWtSimCard',
        name: 'addWtSimCard',
        component: () => import('@/views/equipment/wtSimCard/add'),
        meta: { title: '添加SIM卡', icon: 'product-list' }
      },
      {
        path: 'updateWtSimCard',
        name: 'updateWtSimCard',
        component: () => import('@/views/equipment/wtSimCard/update'),
        meta: { title: '修改SIM卡', icon: 'product-list' }
      },
      {
        path: 'simDetail',
        name: 'simDetail',
        component: () => import('@/views/equipment/wtSimCard/simDetail'),
        meta: { title: 'SIM卡详情', icon: 'product-list' }
      },
      {
        path: 'filterList',
        name: 'filterList',
        component: () => import('@/views/equipment/filterManage/wtFilterElement/index'),
        meta: { title: '全部滤芯', icon: 'product-list' }
      },
      {
        path: 'addWtFilterElement',
        name: 'addWtFilterElement',
        component: () => import('@/views/equipment/filterManage/wtFilterElement/add'),
        meta: { title: '添加滤芯', icon: 'product-list' }
      },
      {
        path: 'updateWtFilterElement',
        name: 'updateWtFilterElement',
        component: () => import('@/views/equipment/filterManage/wtFilterElement/update'),
        meta: { title: '修改滤芯', icon: 'product-list' }
      },
      {
        path: 'filterTypeList',
        name: 'filterTypeList',
        component: () => import('@/views/equipment/filterManage/wtFilterElementType/index'),
        meta: { title: '滤芯类型', icon: 'product-list' }
      },
      {
        path: 'addWtFilterElementType',
        name: 'addWtFilterElementType',
        component: () => import('@/views/equipment/filterManage/wtFilterElementType/add'),
        meta: { title: '添加滤芯类型', icon: 'product-list' }
      },
      {
        path: 'updateWtFilterElementType',
        name: 'updateWtFilterElementType',
        component: () => import('@/views/equipment/filterManage/wtFilterElementType/update'),
        meta: { title: '修改滤芯类型', icon: 'product-list' }
      },
      {
        path: 'typeDetail',
        name: 'typeDetail',
        component: () => import('@/views/equipment/filterManage/wtFilterElementType/typeDetail'),
        meta: { title: '滤芯类型', icon: 'product-list' }
      },
      {
        path: 'filterDetail',
        name: 'filterDetail',
        component: () => import('@/views/equipment/filterManage/wtFilterElement/filterDetail'),
        meta: { title: '滤芯类型', icon: 'product-list' }
      },
    ]
  },

  {
    path: '/dealer',
    component: Layout,
    redirect: '/dealer/dealerList',
    name: 'dealer',
    meta: { title: '经销商管理', icon: 'home' },
    children: [
      {
        path: 'sysMerchantAudit',
        name: 'sysMerchantAudit',
        component: () => import('@/views/dealer/sysMerchantAudit/index'),
        meta: { title: '经销商交叉授权列表', icon: 'product-list' }
      },
      {
        path: 'addSysMerchantAudit',
        name: 'addSysMerchantAudit',
        component: () => import('@/views/dealer/sysMerchantAudit/add'),
        meta: { title: '添加经销商交叉授权' },
        hidden: true
      },
      {
        path: 'updateSysMerchantAudit',
        name: 'updateSysMerchantAudit',
        component: () => import('@/views/dealer/sysMerchantAudit/update'),
        meta: { title: '编辑经销商交叉授权' },
        hidden: true
      },
      {
        path: 'infoDetail',
        name: 'infoDetail',
        component: () => import('@/views/dealer/sysMerchantAudit/components/infoDetail'),
        meta: { title: '交叉授权详情' },
        hidden: true
      },

      {
        path: 'detailsList',
        name: 'detailsList',
        component: () => import('@/views/dealer/dealerList/components/detailsList'),
        meta: { title: '经销商详细信息', icon: 'product-list' }
      },
      {
        path: 'consumptionRecord',
        name: 'consumptionRecord',
        component: () => import('@/views/dealer/record/consumptionRecord'),
        meta: { title: '消费记录', icon: 'product-list' }
      },
      {
        path: 'chargeRecord',
        name: 'chargeRecord',
        component: () => import('@/views/dealer/record/chargeRecord'),
        meta: { title: '充值记录', icon: 'product-list' }
      },
      {
        path: 'equipmentRecord',
        name: 'equipmentRecord',
        component: () => import('@/views/dealer/record/equipmentRecord'),
        meta: { title: '设备列表', icon: 'product-list' }
      },
      {
        path: 'dealerList',
        name: 'dealerList',
        component: () => import('@/views/dealer/dealerList/index'),
        meta: { title: '经销商列表', icon: 'product-list' }
      },
      {
        path: 'waitingList',
        name: 'waitingList',
        component: () => import('@/views/dealer/dealerList/components/waitingList'),
        meta: { title: '经销商列表', icon: 'product-list' }
      },
      {
        path: 'applyingList',
        name: 'applyingList',
        component: () => import('@/views/dealer/dealerList/components/applyingList'),
        meta: { title: '经销商列表', icon: 'product-list' }
      },
      {
        path: 'settledInList',
        name: 'settledInList',
        component: () => import('@/views/dealer/dealerList/components/settledInList'),
        meta: { title: '经销商列表', icon: 'product-list' }
      },
      {
        path: 'rejectedList',
        name: 'rejectedList',
        component: () => import('@/views/dealer/dealerList/components/rejectedList'),
        meta: { title: '经销商列表', icon: 'product-list' }
      },
      {
        path: 'addDealer',
        name: 'addDealer',
        component: () => import('@/views/dealer/addDealer/add'),
        meta: { title: '添加经销商', icon: 'product-list' }
      },
      {
        path: 'updateDealer',
        name: 'updateDealer',
        component: () => import('@/views/dealer/addDealer/update'),
        meta: { title: '修改经销商', icon: 'product-list' }
      },
      {
        path: 'auditDealer',
        name: 'auditDealer',
        component: () => import('@/views/dealer/updateDealer/update'),
        meta: { title: '修改经销商', icon: 'product-list' }
      },
      {
        path: 'addDealerInfo',
        name: 'addDealerInfo',
        component: () => import('@/views/dealer/addDealer/components/dealerInfo'),
        meta: { title: '添加经销商', icon: 'product-add' }
      },
      {
        path: 'updatePermissions',
        name: 'updatePermissions',
        component: () => import('@/views/dealer/addDealer/components/applicationPermissions'),
        meta: { title: '修改应用权限', icon: 'product-add' }
      },
    ]
  },

  {
    path: '/store',
    component: () => import('@/views/home/store'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/home',
    children: [{
      path: 'home',
      name: 'home',
      component: () => import('@/views/home/index'),
      meta: { title: '首页', icon: 'home' }
    }]
  },
  {
    path: '/Info',
    component: Layout,
    redirect: '/Info/updateInfo',
    children: [{
      path: 'updateInfo',
      name: 'updateInfo',
      component: () => import('@/views/sys/updateInfo'),
      meta: { title: '修改基础信息', icon: 'home' }
    }]
  },
  {
    path: '/@/components/templatePage',
    name: 'templateComponents',
    meta: {
      title: '组件测试',
      showflg: true,
      showrole: true,
      roles: "-2",
      level1: 'common',
    },
    component: () =>
      import('@/components/templatePage'),
  },
  {
    path: '/@/components/templatePage/index1',
    name: 'templateComponents1',
    meta: {
      title: '组件测试',
      showflg: true,
      showrole: true,
      // roles: "-2",
      level1: 'common',
    },
    component: () =>
      import('@/components/templatePage/index1'),
  },
  {
    path: '/pms',
    component: Layout,
    redirect: '/pms/product',
    name: 'pms',
    meta: { title: '商品', icon: 'product' },
    children: [{
      path: 'product',
      name: 'product',
      component: () => import('@/views/pms/product/index'),
      meta: { title: '商品列表', icon: 'product-list' }
    },
    {
      path: 'addProduct',
      name: 'addProduct',
      component: () => import('@/views/pms/product/add'),
      meta: { title: '添加商品', icon: 'product-add' }
    },
    {
      path: 'updateProduct',
      name: 'updateProduct',
      component: () => import('@/views/pms/product/update'),
      meta: { title: '修改商品', icon: 'product-add' },
      hidden: true
    },
    {
      path: 'productRecycle',
      name: 'productRecycle',
      component: () => import('@/views/pms/product/index'),
      meta: { title: '商品回收站', icon: 'product-recycle' },
      hidden: true
    },
    {
      path: 'productComment',
      name: 'productComment',
      component: () => import('@/views/pms/product/index'),
      meta: { title: '商品评价', icon: 'product-comment' },
      hidden: true
    },
    {
      path: 'productCate',
      name: 'productCate',
      component: () => import('@/views/pms/productCate/index'),
      meta: { title: '商品分类', icon: 'product-cate' }
    },
    {
      path: 'addProductCate',
      name: 'addProductCate',
      component: () => import('@/views/pms/productCate/add'),
      meta: { title: '添加商品分类' },
      hidden: true
    },
    {
      path: 'updateProductCate',
      name: 'updateProductCate',
      component: () => import('@/views/pms/productCate/update'),
      meta: { title: '修改商品分类' },
      hidden: true
    },
    {
      path: 'productAttr',
      name: 'productAttr',
      component: () => import('@/views/pms/productAttr/index'),
      meta: { title: '商品类型', icon: 'product-attr' }
    },
    {
      path: 'productAttrList',
      name: 'productAttrList',
      component: () => import('@/views/pms/productAttr/productAttrList'),
      meta: { title: '商品属性列表' },
      hidden: true
    },
    {
      path: 'addProductAttr',
      name: 'addProductAttr',
      component: () => import('@/views/pms/productAttr/addProductAttr'),
      meta: { title: '添加商品属性' },
      hidden: true
    },
    {
      path: 'updateProductAttr',
      name: 'updateProductAttr',
      component: () => import('@/views/pms/productAttr/updateProductAttr'),
      meta: { title: '修改商品属性' },
      hidden: true
    },
    {
      path: 'brand',
      name: 'brand',
      component: () => import('@/views/pms/brand/index'),
      meta: { title: '品牌管理', icon: 'product-brand' }
    },
    {
      path: 'addBrand',
      name: 'addBrand',
      component: () => import('@/views/pms/brand/add'),
      meta: { title: '添加品牌' },
      hidden: true
    },
    {
      path: 'updateBrand',
      name: 'updateBrand',
      component: () => import('@/views/pms/brand/update'),
      meta: { title: '编辑品牌' },
      hidden: true
    },
    //小程序首页分类图标管理
    {
      path: 'smallNaviconCategory',
      name: 'smallNaviconCategory',
      component: () => import('@/views/pms/smallNaviconCategory/index'),
      meta: { title: '小程序首页nav管理列表}' }
    },
    {
      path: 'addSmallNaviconCategory',
      name: 'addSmallNaviconCategory',
      component: () => import('@/views/pms/smallNaviconCategory/add'),
      meta: { title: '添加小程序首页nav管理', icon: 'smallNaviconCategory-add' }
    },
    {
      path: 'updateSmallNaviconCategory',
      name: 'updateSmallNaviconCategory',
      component: () => import('@/views/pms/smallNaviconCategory/update'),
      meta: { title: '修改小程序首页nav管理', icon: 'smallNaviconCategory-update' },
      hidden: true
    },
    {
      path: 'PmsGifts',
      name: 'PmsGifts',
      component: () => import('@/views/pms/Gifts/index'),
      meta: { title: '赠品管理', icon: 'product-list' }
    },

    {
      path: 'addPmsGifts',
      name: 'addPmsGifts',
      component: () => import('@/views/pms/Gifts/add'),
      meta: { title: '添加赠品' },
      hidden: true
    },
    {
      path: 'updatePmsGifts',
      name: 'updatePmsGifts',
      component: () => import('@/views/pms/Gifts/update'),
      meta: { title: '编辑赠品' },
      hidden: true
    },
    {
      path: 'PmsGiftsCategory',
      name: 'PmsGiftsCategory',
      component: () => import('@/views/pms/GiftsCategory/index'),
      meta: { title: '赠品分类', icon: 'product-list' }
    },

    {
      path: 'addPmsGiftsCategory',
      name: 'addPmsGiftsCategory',
      component: () => import('@/views/pms/GiftsCategory/add'),
      meta: { title: '添加赠品分类' },
      hidden: true
    },
    {
      path: 'updatePmsGiftsCategory',
      name: 'updatePmsGiftsCategory',
      component: () => import('@/views/pms/GiftsCategory/update'),
      meta: { title: '编辑赠品分类' },
      hidden: true
    },
    {
      path: 'ProductConsult',
      name: 'ProductConsult',
      component: () => import('@/views/pms/productConsult/index'),
      meta: { title: '商品评论' },
      hidden: true
    }

    ]
  },
  {
    path: '/oms',
    component: Layout,
    redirect: '/oms/order',
    name: 'oms',
    meta: { title: '订单', icon: 'order' },
    children: [
      {
        path: 'omsCompanyAddress',
        name: 'omsCompanyAddress',
        component: () => import('@/views/oms/omsCompanyAddress/index'),
        meta: { title: '发货地址列表', icon: 'product-list' }
      },

      {
        path: 'addOmsCompanyAddress',
        name: 'addOmsCompanyAddress',
        component: () => import('@/views/oms/omsCompanyAddress/add'),
        meta: { title: '添加发货地址' },
        hidden: true
      },
      {
        path: 'updateOmsCompanyAddress',
        name: 'updateOmsCompanyAddress',
        component: () => import('@/views/oms/omsCompanyAddress/update'),
        meta: { title: '编辑发货地址' },
        hidden: true
      },

      {
        path: 'allOrders',
        name: 'allOrders',
        component: () => import('@/views/oms/order/allOrders'),
        meta: { title: '全部订单', icon: 'product-list' }
      },
      {
        path: 'toBeDelivered',
        name: 'toBeDelivered',
        component: () => import('@/views/oms/order/toBeDelivered'),
        meta: { title: '待发货', icon: 'product-list' }
      },
      {
        path: 'goodsToBeReceived',
        name: 'goodsToBeReceived',
        component: () => import('@/views/oms/order/goodsToBeReceived'),
        meta: { title: '待收货', icon: 'product-list' }
      },
      {
        path: 'obligations',
        name: 'obligations',
        component: () => import('@/views/oms/order/obligations'),
        meta: { title: '待付款', icon: 'product-list' }
      },
      {
        path: 'finished',
        name: 'finished',
        component: () => import('@/views/oms/order/finished'),
        meta: { title: '已完成', icon: 'product-list' }
      },
      {
        path: 'cancelled',
        name: 'cancelled',
        component: () => import('@/views/oms/order/cancelled'),
        meta: { title: '已取消', icon: 'product-list' }
      },

      {
        path: 'order',
        name: 'order',
        component: () => import('@/views/oms/order/index1'),
        meta: { title: '订单列表', icon: 'product-list' }
      },
      {
        path: 'orderDetail',
        name: 'orderDetail',
        component: () => import('@/views/oms/order/orderDetail'),
        meta: { title: '订单详情' },
        hidden: true
      },
      {
        path: 'deliverOrderList',
        name: 'deliverOrderList',
        component: () => import('@/views/oms/order/deliverOrderList'),
        meta: { title: '发货列表' },
        hidden: true
      },

      {
        path: 'returnApply',
        name: 'returnApply',
        component: () => import('@/views/oms/apply/index'),
        meta: { title: '退货申请处理', icon: 'order-return' }
      },

      {
        path: 'returnApplyDetail',
        name: 'returnApplyDetail',
        component: () => import('@/views/oms/apply/applyDetail'),
        meta: { title: '退货原因详情' },
        hidden: true
      },
      {
        path: 'OrderProductConsult',
        name: 'OrderProductConsult',
        component: () => import('@/views/oms/orderConsult/index'),
        meta: { title: '订单评论' },
        hidden: true
      },

    ]
  },
  {
    path: '/marking',
    component: Layout,
    redirect: '/marking/smsDiyPage',
    name: 'marking',
    meta: { title: '运营管理', icon: 'sms' },
    children: [
      {
        path: 'sysShop',
        name: 'sysShop',
        component: () => import('@/views/sys/sysShop/index'),
        meta: { title: '门店管理列表', icon: 'product-list' }
      },

      {
        path: 'addSysShop',
        name: 'addSysShop',
        component: () => import('@/views/sys/sysShop/add'),
        meta: { title: '添加门店管理' },
        hidden: true
      },
      {
        path: 'updateSysShop',
        name: 'updateSysShop',
        component: () => import('@/views/sys/sysShop/update'),
        meta: { title: '编辑门店管理' },
        hidden: true
      },
      {
        path: 'smsContentMsg',
        name: 'smsContentMsg',
        component: () => import('@/views/sms/smsContentMsg/index'),
        meta: { title: '短信记录列表', icon: 'product-list' }
      },
      {
        path: 'smsContent',
        name: 'smsContent',
        component: () => import('@/views/sms/smsContent/index'),
        meta: { title: '短信模版列表', icon: 'product-list' }
      },

      {
        path: 'addSmsContent',
        name: 'addSmsContent',
        component: () => import('@/views/sms/smsContent/add'),
        meta: { title: '添加短信模版' },
        hidden: true
      },
      {
        path: 'updateSmsContent',
        name: 'updateSmsContent',
        component: () => import('@/views/sms/smsContent/update'),
        meta: { title: '编辑短信模版' },
        hidden: true
      },
      {
        path: 'smsDiyPage',
        name: 'smsDiyPage',
        component: () => import('@/views/sms/page/index'),
        meta: { title: '添加页面模版', icon: 'sms-flash' }
      },
      {
        path: 'smsDiypageTemplateCategory',
        name: 'smsDiypageTemplateCategory',
        component: () => import('@/views/sms/smsDiypageTemplateCategory/index'),
        meta: { title: '页面模版列表', icon: 'product-list' }
      },

      {
        path: 'addSmsDiypageTemplateCategory',
        name: 'addSmsDiypageTemplateCategory',
        component: () => import('@/views/sms/smsDiypageTemplateCategory/add'),
        meta: { title: '添加页面模版' },
        hidden: true
      },
      {
        path: 'updateSmsDiypageTemplateCategory',
        name: 'updateSmsDiypageTemplateCategory',
        component: () => import('@/views/sms/smsDiypageTemplateCategory/update'),
        meta: { title: '编辑页面模版' },
        hidden: true
      },
      {
        path: 'homeBrand',
        name: 'homeBrand',
        component: () => import('@/views/sms/brand/index'),
        meta: { title: '品牌推荐', icon: 'product-brand' }
      },
      {
        path: 'homeNew',
        name: 'homeNew',
        component: () => import('@/views/sms/new/index'),
        meta: { title: '新品推荐', icon: 'sms-new' }
      },
      {
        path: 'homeHot',
        name: 'homeHot',
        component: () => import('@/views/sms/hot/index'),
        meta: { title: '人气推荐', icon: 'sms-hot' }
      },
      {
        path: 'homeSubject',
        name: 'homeSubject',
        component: () => import('@/views/sms/subject/index'),
        meta: { title: '专题推荐', icon: 'sms-subject' }
      },
      {
        path: 'homeAdvertise',
        name: 'homeAdvertise',
        component: () => import('@/views/sms/advertise/index'),
        meta: { title: '广告列表', icon: 'sms-ad' }
      },
      {
        path: 'addHomeAdvertise',
        name: 'addHomeAdvertise',
        component: () => import('@/views/sms/advertise/add'),
        meta: { title: '添加广告' },
        hidden: true
      },
      {
        path: 'updateHomeAdvertise',
        name: 'updateHomeAdvertise',
        component: () => import('@/views/sms/advertise/update'),
        meta: { title: '编辑广告' },
        hidden: true
      },

    ]
  },
  {
    path: '/sms',
    component: Layout,
    redirect: '/sms/coupon',
    name: 'sms',
    meta: { title: '营销', icon: 'sms' },
    children: [
      {
        path: 'rechargePackage',
        name: 'rechargePackage',
        component: () => import('@/views/sms/rechargePackage/index'),
        meta: { title: '充值套餐列表', icon: 'sms-flash' }
      },

      {
        path: 'flashPromotion',
        name: 'flashPromotion',
        component: () => import('@/views/sms/flash/index'),
        meta: { title: '秒杀活动列表', icon: 'sms-flash' }
      },
      {
        path: 'flashSession',
        name: 'flashSession',
        component: () => import('@/views/sms/flash/sessionList'),
        meta: { title: '秒杀时间段列表' },
        hidden: true
      },
      {
        path: 'selectSession',
        name: 'selectSession',
        component: () => import('@/views/sms/flash/selectSessionList'),
        meta: { title: '秒杀时间段选择' },
        hidden: true
      },
      {
        path: 'flashProductRelation',
        name: 'flashProductRelation',
        component: () => import('@/views/sms/flash/productRelationList'),
        meta: { title: '秒杀商品列表' },
        hidden: true
      },
      {
        path: 'coupon',
        name: 'coupon',
        component: () => import('@/views/sms/coupon/index'),
        meta: { title: '优惠券列表', icon: 'sms-coupon' }
      },
      {
        path: 'addCoupon',
        name: 'addCoupon',
        component: () => import('@/views/sms/coupon/add'),
        meta: { title: '添加优惠券' },
        hidden: true
      },
      {
        path: 'updateCoupon',
        name: 'updateCoupon',
        component: () => import('@/views/sms/coupon/update'),
        meta: { title: '修改优惠券' },
        hidden: true
      },
      {
        path: 'couponHistory',
        name: 'couponHistory',
        component: () => import('@/views/sms/coupon/history'),
        meta: { title: '优惠券领取详情' },
        hidden: true
      },

      {
        path: 'group',
        name: 'group',
        component: () => import('@/views/sms/group/index'),
        meta: { title: '拼团列表', icon: 'product-list' }
      },
      {
        path: 'groupHistory',
        name: 'groupHistory',
        component: () => import('@/views/sms/group/detail'),
        meta: { title: '拼团详情' },
        hidden: true
      },
      {
        path: 'addGroup',
        name: 'addGroup',
        component: () => import('@/views/sms/group/add'),
        meta: { title: '添加拼团' },
        hidden: true
      },
      {
        path: 'updateGroup',
        name: 'updateGroup',
        component: () => import('@/views/sms/group/update'),
        meta: { title: '编辑拼团' },
        hidden: true
      },
      {
        path: 'redPacket',
        name: 'redPacket',
        component: () => import('@/views/sms/redPacket/index'),
        meta: { title: '红包列表', icon: 'product-list' }
      },

      {
        path: 'addRedPacket',
        name: 'addRedPacket',
        component: () => import('@/views/sms/redPacket/add'),
        meta: { title: '添加红包' },
        hidden: true
      },
      {
        path: 'updateRedPacket',
        name: 'updateRedPacket',
        component: () => import('@/views/sms/redPacket/update'),
        meta: { title: '编辑红包' },
        hidden: true
      },
      {
        path: 'basicMark',
        name: 'basicMark',
        component: () => import('@/views/sms/basicMark/index'),
        meta: { title: '基本营销', icon: 'product-list' }
      },

      {
        path: 'addBasicMark',
        name: 'addBasicMark',
        component: () => import('@/views/sms/basicMark/add'),
        meta: { title: '添加基本营销' },
        hidden: true
      },
      {
        path: 'updateBasicMark',
        name: 'updateBasicMark',
        component: () => import('@/views/sms/basicMark/update'),
        meta: { title: '编辑基本营销' },
        hidden: true
      },
      {
        path: 'smsBasicGifts',
        name: 'smsBasicGifts',
        component: () => import('@/views/sms/SmsBasicGifts/index'),
        meta: { title: '赠品营销', icon: 'product-list' }
      },

      {
        path: 'addSmsBasicGifts',
        name: 'addSmsBasicGifts',
        component: () => import('@/views/sms/SmsBasicGifts/add'),
        meta: { title: '添加赠品营销' },
        hidden: true
      },
      {
        path: 'updateSmsBasicGifts',
        name: 'updateSmsBasicGiftsk',
        component: () => import('@/views/sms/SmsBasicGifts/update'),
        meta: { title: '编辑赠品营销' },
        hidden: true
      }
      ,
      {
        path: 'groupActivity',
        name: 'groupActivity',
        component: () => import('@/views/sms/groupActivity/index'),
        meta: { title: '团购管理', icon: 'product-list' }
      },

      {
        path: 'addgroupActivity',
        name: 'addgroupActivity',
        component: () => import('@/views/sms/groupActivity/add'),
        meta: { title: '添加团购' },
        hidden: true
      },
      {
        path: 'updategroupActivity',
        name: 'updategroupActivity',
        component: () => import('@/views/sms/groupActivity/update'),
        meta: { title: '编辑团购' },
        hidden: true
      }, {
        path: 'smsBargainConfig',
        name: 'smsBargainConfig',
        component: () => import('@/views/sms/smsBargainConfig/index'),
        meta: { title: '砍价商品列表', icon: 'product-list' }
      },

      {
        path: 'addSmsBargainConfig',
        name: 'addSmsBargainConfig',
        component: () => import('@/views/sms/smsBargainConfig/add'),
        meta: { title: '添加砍价商品' },
        hidden: true
      },
      {
        path: 'updateSmsBargainConfig',
        name: 'updateSmsBargainConfig',
        component: () => import('@/views/sms/smsBargainConfig/update'),
        meta: { title: '编辑砍价商品' },
        hidden: true
      }, {
        path: 'smsDraw',
        name: 'smsDraw',
        component: () => import('@/views/sms/draw/index'),
        meta: { title: '抽奖活动', icon: 'product-list' }
      }, {
        path: 'addsmsDraw',
        name: 'addsmsDraw',
        component: () => import('@/views/sms/draw/add'),
        meta: { title: '添加抽奖' },
        hidden: true
      },
      {
        path: 'updatesmsDraw',
        name: 'updatesmsDraw',
        component: () => import('@/views/sms/draw/update'),
        meta: { title: '编辑抽奖' },
        hidden: true
      }, {
        path: 'smsPaimai',
        name: 'smsPaimai',
        component: () => import('@/views/sms/paimai/index'),
        meta: { title: '竞拍活动', icon: 'product-list' }
      }, {
        path: 'addsmsPaimai',
        name: 'addsmsPaimai',
        component: () => import('@/views/sms/paimai/add'),
        meta: { title: '添加竞拍' },
        hidden: true
      },
      {
        path: 'updatesmsPaimai',
        name: 'updatesmsPaimai',
        component: () => import('@/views/sms/paimai/update'),
        meta: { title: '编辑竞拍' },
        hidden: true
      }
    ]
  },


  {
    path: '/jifen',
    component: Layout,
    redirect: '/jifen/jifenCoupon',
    name: 'jifen',
    meta: { title: '积分商城', icon: 'home' },
    children: [

      {
        path: 'jifenCoupon',
        name: 'jifenCoupon',
        component: () => import('@/views/jifen/jifenCoupon/index'),
        meta: { title: '积分券列表', icon: 'product-list' }
      },

      {
        path: 'addJifenCoupon',
        name: 'addJifenCoupon',
        component: () => import('@/views/jifen/jifenCoupon/add'),
        meta: { title: '添加积分券' },
        hidden: true
      },
      {
        path: 'updateJifenCoupon',
        name: 'updateJifenCoupon',
        component: () => import('@/views/jifen/jifenCoupon/update'),
        meta: { title: '编辑积分券' },
        hidden: true
      },
      {
        path: 'jifenDonateRule',
        name: 'jifenDonateRule',
        component: () => import('@/views/jifen/jifenDonateRule/index'),
        meta: { title: '积分赠送规则列表', icon: 'product-list' }
      },

      {
        path: 'addJifenDonateRule',
        name: 'addJifenDonateRule',
        component: () => import('@/views/jifen/jifenDonateRule/add'),
        meta: { title: '添加积分赠送规则' },
        hidden: true
      },
      {
        path: 'updateJifenDonateRule',
        name: 'updateJifenDonateRule',
        component: () => import('@/views/jifen/jifenDonateRule/update'),
        meta: { title: '编辑积分赠送规则' },
        hidden: true
      },
      {
        path: 'jifenSignRule',
        name: 'jifenSignRule',
        component: () => import('@/views/jifen/jifenSignRule/index'),
        meta: { title: '积分签到规则列表', icon: 'product-list' }
      },

      {
        path: 'addJifenSignRule',
        name: 'addJifenSignRule',
        component: () => import('@/views/jifen/jifenSignRule/add'),
        meta: { title: '添加积分签到规则' },
        hidden: true
      },
      {
        path: 'updateJifenSignRule',
        name: 'updateJifenSignRule',
        component: () => import('@/views/jifen/jifenSignRule/update'),
        meta: { title: '编辑积分签到规则' },
        hidden: true
      },


    ]
  },
  {
    path: '/fenxiao',
    component: Layout,
    redirect: '/fenxiao/fenxiaoChecks',
    name: 'fenxiao',
    meta: { title: '分销商城', icon: 'home' },
    children: [
      {
        path: 'fenxiaoChecks',
        name: 'fenxiaoChecks',
        component: () => import('@/views/fenxiao/fenxiaoChecks/index'),
        meta: { title: '分销审核列表', icon: 'product-list' }
      },

      {
        path: 'addFenxiaoChecks',
        name: 'addFenxiaoChecks',
        component: () => import('@/views/fenxiao/fenxiaoChecks/add'),
        meta: { title: '添加分销审核' },
        hidden: true
      },
      {
        path: 'updateFenxiaoChecks',
        name: 'updateFenxiaoChecks',
        component: () => import('@/views/fenxiao/fenxiaoChecks/update'),
        meta: { title: '编辑分销审核' },
        hidden: true
      },

      {
        path: 'fenxiaoRecords',
        name: 'fenxiaoRecords',
        component: () => import('@/views/fenxiao/fenxiaoRecords/index'),
        meta: { title: '分销记录列表', icon: 'product-list' }
      },

      {
        path: 'addFenxiaoRecords',
        name: 'addFenxiaoRecords',
        component: () => import('@/views/fenxiao/fenxiaoRecords/add'),
        meta: { title: '添加分销记录' },
        hidden: true
      },
      {
        path: 'updateFenxiaoRecords',
        name: 'updateFenxiaoRecords',
        component: () => import('@/views/fenxiao/fenxiaoRecords/update'),
        meta: { title: '编辑分销记录' },
        hidden: true
      },

      {
        path: 'fenxiaoUserRelate',
        name: 'fenxiaoUserRelate',
        component: () => import('@/views/fenxiao/fenxiaoUserRelate/index'),
        meta: { title: '分销关系列表', icon: 'product-list' }
      },

      {
        path: 'addFenxiaoUserRelate',
        name: 'addFenxiaoUserRelate',
        component: () => import('@/views/fenxiao/fenxiaoUserRelate/add'),
        meta: { title: '添加分销关系' },
        hidden: true
      },
      {
        path: 'updateFenxiaoUserRelate',
        name: 'updateFenxiaoUserRelate',
        component: () => import('@/views/fenxiao/fenxiaoUserRelate/update'),
        meta: { title: '编辑分销关系' },
        hidden: true
      },

    ]
  },
  {
    path: '/ums',
    component: Layout,
    redirect: '/ums/member',
    name: 'ums',
    meta: { title: '会员', icon: 'home' },
    children: [
      {
        path: 'member',
        name: 'member',
        component: () => import('@/views/ums/member/index'),
        meta: { title: '会员列表', icon: 'product-list' }
      },
      {
        path: 'addMember',
        name: 'addMember',
        component: () => import('@/views/ums/member/add'),
        meta: { title: '添加会员' },
        hidden: true
      },
      {
        path: 'updateMember',
        name: 'updateMember',
        component: () => import('@/views/ums/member/update'),
        meta: { title: '编辑会员' },
        hidden: true
      }, {
        path: 'memberLevel',
        name: 'memberLevel',
        component: () => import('@/views/ums/memberLevel/index'),
        meta: { title: '会员等级列表', icon: 'product-list' }
      },
      {
        path: 'addMemberLevel',
        name: 'addMemberLevel',
        component: () => import('@/views/ums/memberLevel/add'),
        meta: { title: '添加会员等级' },
        hidden: true
      },
      {
        path: 'updateMemberLevel',
        name: 'updateMemberLevel',
        component: () => import('@/views/ums/memberLevel/update'),
        meta: { title: '编辑会员等级' },
        hidden: true
      },
      {
        path: 'umsMemberLevel',
        name: 'umsMemberLevel',
        component: () => import('@/views/ums/umsMemberLevel/index'),
        meta: { title: '会员等级列表', icon: 'product-list' }
      },
      {
        path: 'addUmsMemberLevel',
        name: 'addUmsMemberLevel',
        component: () => import('@/views/ums/umsMemberLevel/add'),
        meta: { title: '添加会员等级' },
        hidden: true
      },
      {
        path: 'updateUmsMemberLevel',
        name: 'updateUmsMemberLevel',
        component: () => import('@/views/ums/umsMemberLevel/update'),
        meta: { title: '编辑会员等级' },
        hidden: true
      },

      {
        path: 'wtWaterCardCreateList',
        name: 'wtWaterCardCreateList',
        component: () => import('@/views/ums/wtWaterCardCreate/index'),
        meta: { title: '批量制卡列表', icon: 'product-list' }
      },
      {
        path: 'addWtWaterCardCreate',
        name: 'addWtWaterCardCreate',
        component: () => import('@/views/ums/wtWaterCardCreate/add'),
        meta: { title: '批量制卡' },
        hidden: true
      },
      {
        path: 'updateWtWaterCardCreate',
        name: 'updateWtWaterCardCreate',
        component: () => import('@/views/ums/wtWaterCardCreate/update'),
        meta: { title: '编辑制卡' },
        hidden: true
      },

      {
        path: 'wtWaterCardActivateList',
        name: 'wtWaterCardActivateList',
        component: () => import('@/views/ums/wtWaterCardActivate/index'),
        meta: { title: '批量开卡列表', icon: 'product-list' }
      },
      {
        path: 'addWtWaterCardActivate',
        name: 'addWtWaterCardActivate',
        component: () => import('@/views/ums/wtWaterCardActivate/add'),
        meta: { title: '批量开卡' },
        hidden: true
      },
      {
        path: 'updateWtWaterCardActivate',
        name: 'updateWtWaterCardActivate',
        component: () => import('@/views/ums/wtWaterCardActivate/update'),
        meta: { title: '编辑开卡' },
        hidden: true
      },

      {
        path: 'wtWaterCardRechargeList',
        name: 'wtWaterCardRechargeList',
        component: () => import('@/views/ums/wtWaterCardRecharge/index'),
        meta: { title: '充值管理', icon: 'product-list' }
      },
      {
        path: 'addWtWaterCardRecharge',
        name: 'addWtWaterCardRecharge',
        component: () => import('@/views/ums/wtWaterCardRecharge/add'),
        meta: { title: '批量充值' },
        hidden: true
      },
      {
        path: 'backstageRecharge',
        name: 'backstageRecharge',
        component: () => import('@/views/ums/wtWaterCardRecharge/backstageRecharge'),
        meta: { title: '后台充值' },
        hidden: true
      },
      {
        path: 'batchRecharge',
        name: 'batchRecharge',
        component: () => import('@/views/ums/wtWaterCardRecharge/batchRecharge'),
        meta: { title: '批量充值' },
        hidden: true
      },
      {
        path: 'importRecharge',
        name: 'importRecharge',
        component: () => import('@/views/ums/wtWaterCardRecharge/importRecharge'),
        meta: { title: '批量充值' },
        hidden: true
      },
      {
        path: 'batchCardBinding',
        name: 'batchCardBinding',
        component: () => import('@/views/ums/wtWaterBindCard/batchCardBinding'),
        meta: { title: '批量绑卡' },
        hidden: true
      },

      {
        path: 'warterCardEmpowerList',
        name: 'warterCardEmpowerList',
        component: () => import('@/views/ums/wtWarterCardEmpower/index'),
        meta: { title: '授权列表' },
        hidden: true
      },
      {
        path: 'addWtWarterCardEmpower',
        name: 'addWtWarterCardEmpower',
        component: () => import('@/views/ums/wtWarterCardEmpower/add'),
        meta: { title: '新增授权' },
        hidden: true
      },
      {
        path: 'updateWtWarterCardEmpower',
        name: 'updateWtWarterCardEmpower',
        component: () => import('@/views/ums/wtWarterCardEmpower/update'),
        meta: { title: '新增授权' },
        hidden: true
      },
      {
        path: 'wtWaterCardLimit',
        name: 'wtWaterCardLimit',
        component: () => import('@/views/ums/wtWaterCardLimit/index'),
        meta: {title: '水卡限制消费列表', icon: 'product-list'}
        },
        {
        path: 'addWtWaterCardLimit',
        name: 'addWtWaterCardLimit',
        component: () => import('@/views/ums/wtWaterCardLimit/add'),
        meta: {title: '添加水卡限制消费'},
        hidden: true
        },
        {
          path: 'addWtWaterCardLimitBatch',
          name: 'addWtWaterCardLimitBatch',
          component: () => import('@/views/ums/wtWaterCardLimit/addBatch'),
          meta: {title: '添加水卡限制消费'},
          hidden: true
          },
        {
        path: 'updateWtWaterCardLimit',
        name: 'updateWtWaterCardLimit',
        component: () => import('@/views/ums/wtWaterCardLimit/update'),
        meta: {title: '编辑水卡限制消费'},
        hidden: true
        },
        {
          path: 'wtWaterCardProblem',
          name: 'wtWaterCardProblem',
          component: () => import('@/views/ums/wtWaterCardProblem/index'),
          meta: {title: '问题卡列表', icon: 'product-list'}
        },
        {
          path: 'addWtWaterCardProblem',
          name: 'addWtWaterCardProblem',
          component: () => import('@/views/ums/wtWaterCardProblem/add'),
          meta: {title: '添加问题卡'},
          hidden: true
        },



      {
        path: 'school',
        name: 'school',
        component: () => import('@/views/ums/school/index'),
        meta: { title: '学校列表', icon: 'product-list' }
      },
      {
        path: 'addSchool',
        name: 'addSchool',
        component: () => import('@/views/ums/school/add'),
        meta: { title: '添加学校' },
        hidden: true
      },
      {
        path: 'updateSchool',
        name: 'updateSchool',
        component: () => import('@/views/ums/school/update'),
        meta: { title: '编辑学校' },
        hidden: true
      }, {
        path: 'memberBlanceLog',
        name: 'memberBlanceLog',
        component: () => import('@/views/ums/memberBlanceLog/index'),
        meta: { title: '余额日志记录', icon: 'product-list' }
      }, {
        path: 'memberIntegration',
        name: 'memberIntegration',
        component: () => import('@/views/ums/memberIntegration/index'),
        meta: { title: '积分日志记录', icon: 'product-list' }
      }
    ]
  },

  {
    path: '/build',
    component: Layout,
    redirect: '/build/community',
    name: 'build',
    meta: { title: '物业管理', icon: 'home' },
    children: [
      {
        path: 'companyCommunity',
        name: 'companyCommunity',
        component: () => import('@/views/build/wuyeCompany/companyCommunity'),
        meta: { title: '绑定小区', icon: 'product-list' }
      },
      {
        path: 'community',
        name: 'community',
        component: () => import('@/views/build/community/index'),
        meta: { title: '小区列表', icon: 'product-list' }
      },

      {
        path: 'addCommunity',
        name: 'addCommunity',
        component: () => import('@/views/build/community/add'),
        meta: { title: '添加小区' },
        hidden: true
      },
      {
        path: 'updateCommunity',
        name: 'updateCommunity',
        component: () => import('@/views/build/community/update'),
        meta: { title: '编辑小区' },
        hidden: true
      },
      {
        path: 'room',
        name: 'room',
        component: () => import('@/views/build/room/index'),
        meta: { title: '房间列表', icon: 'product-list' }
      },

      {
        path: 'addRoom',
        name: 'addRoom',
        component: () => import('@/views/build/room/add'),
        meta: { title: '添加房间' },
        hidden: true
      },
      {
        path: 'updateRoom',
        name: 'updateRoom',
        component: () => import('@/views/build/room/update'),
        meta: { title: '编辑房间' },
        hidden: true
      },
      {
        path: 'floor',
        name: 'floor',
        component: () => import('@/views/build/floor/index'),
        meta: { title: '楼栋列表', icon: 'product-list' }
      },

      {
        path: 'addFloor',
        name: 'addFloor',
        component: () => import('@/views/build/floor/add'),
        meta: { title: '添加楼栋' },
        hidden: true
      },
      {
        path: 'updateFloor',
        name: 'updateFloor',
        component: () => import('@/views/build/floor/update'),
        meta: { title: '编辑楼栋' },
        hidden: true
      },
      {
        path: 'unit',
        name: 'unit',
        component: () => import('@/views/build/unit/index'),
        meta: { title: '单元列表', icon: 'product-list' }
      },

      {
        path: 'addUnit',
        name: 'addUnit',
        component: () => import('@/views/build/unit/add'),
        meta: { title: '添加单元' },
        hidden: true
      },
      {
        path: 'updateUnit',
        name: 'updateUnit',
        component: () => import('@/views/build/unit/update'),
        meta: { title: '编辑单元' },
        hidden: true
      },
      {
        path: 'owner',
        name: 'owner',
        component: () => import('@/views/build/owner/index'),
        meta: { title: '业主列表', icon: 'product-list' }
      },

      {
        path: 'addOwner',
        name: 'addOwner',
        component: () => import('@/views/build/owner/add'),
        meta: { title: '添加业主列表' },
        hidden: true
      },
      {
        path: 'updateOwner',
        name: 'updateOwner',
        component: () => import('@/views/build/owner/update'),
        meta: { title: '编辑业主列表' },
        hidden: true
      },


      {
        path: 'adv',
        name: 'adv',
        component: () => import('@/views/build/adv/index'),
        meta: { title: '社区广告', icon: 'product-list' }
      },

      {
        path: 'addAdv',
        name: 'addAdv',
        component: () => import('@/views/build/adv/add'),
        meta: { title: '添加社区广告' },
        hidden: true
      },
      {
        path: 'updateAdv',
        name: 'updateAdv',
        component: () => import('@/views/build/adv/update'),
        meta: { title: '编辑社区广告' },
        hidden: true
      },
      {
        path: 'buildGroup',
        name: 'buildGroup',
        component: () => import('@/views/build/group/index'),
        meta: { title: '社区团购', icon: 'product-list' }
      },

      {
        path: 'addBuildGroup',
        name: 'addBuildGroup',
        component: () => import('@/views/build/group/add'),
        meta: { title: '添加社区团购' },
        hidden: true
      },
      {
        path: 'updateBuildGroup',
        name: 'updateBuildGroup',
        component: () => import('@/views/build/group/update'),
        meta: { title: '编辑社区团购' },
        hidden: true
      },
      {
        path: 'buildGroupHistory',
        name: 'buildGroupHistory',
        component: () => import('@/views/build/group/detail'),
        meta: { title: '拼团详情' },
        hidden: true
      },
      {
        path: 'notice',
        name: 'notice',
        component: () => import('@/views/build/notice/index'),
        meta: { title: '社区公告', icon: 'product-list' }
      },

      {
        path: 'addNotice',
        name: 'addNotice',
        component: () => import('@/views/build/notice/add'),
        meta: { title: '添加社区公告' },
        hidden: true
      },
      {
        path: 'updateNotice',
        name: 'updateNotice',
        component: () => import('@/views/build/notice/update'),
        meta: { title: '编辑社区公告' },
        hidden: true
      },
      {
        path: 'repair',
        name: 'repair',
        component: () => import('@/views/build/repair/index'),
        meta: { title: '社区报修', icon: 'product-list' }
      },

      {
        path: 'addRepair',
        name: 'addRepair',
        component: () => import('@/views/build/repair/add'),
        meta: { title: '添加社区报修' },
        hidden: true
      },
      {
        path: 'updateRepair',
        name: 'updateRepair',
        component: () => import('@/views/build/repair/update'),
        meta: { title: '编辑社区报修' },
        hidden: true
      },
      {
        path: 'typePrice',
        name: 'typePrice',
        component: () => import('@/views/build/typePrice/index'),
        meta: { title: '费用类型', icon: 'product-list' }
      },

      {
        path: 'addTypePrice',
        name: 'addTypePrice',
        component: () => import('@/views/build/typePrice/add'),
        meta: { title: '添加费用类型' },
        hidden: true
      },
      {
        path: 'updateTypePrice',
        name: 'updateTypePrice',
        component: () => import('@/views/build/typePrice/update'),
        meta: { title: '编辑费用类型' },
        hidden: true
      },
      {
        path: 'wuyeCompany',
        name: 'wuyeCompany',
        component: () => import('@/views/build/wuyeCompany/index'),
        meta: { title: '物业公司', icon: 'product-list' }
      },

      {
        path: 'addWuyeCompany',
        name: 'addWuyeCompany',
        component: () => import('@/views/build/wuyeCompany/add'),
        meta: { title: '添加物业公司' },
        hidden: true
      },
      {
        path: 'updateWuyeCompany',
        name: 'updateWuyeCompany',
        component: () => import('@/views/build/wuyeCompany/update'),
        meta: { title: '编辑物业公司' },
        hidden: true
      },
      {
        path: 'wuyePrice',
        name: 'wuyePrice',
        component: () => import('@/views/build/wuyePrice/index'),
        meta: { title: '缴费记录', icon: 'product-list' }
      },

      {
        path: 'addWuyePrice',
        name: 'addWuyePrice',
        component: () => import('@/views/build/wuyePrice/add'),
        meta: { title: '添加缴费记录' },
        hidden: true
      },
      {
        path: 'updateWuyePrice',
        name: 'updateWuyePrice',
        component: () => import('@/views/build/wuyePrice/update'),
        meta: { title: '编辑缴费记录' },
        hidden: true
      },
    ]
  },
  {
    path: '/setting',
    component: Layout,
    redirect: '/setting/appletSetting',
    name: 'setting',
    meta: { title: '商城配置', icon: 'home' },
    children: [
      {
        path: 'umsMemberTag',
        name: 'umsMemberTag',
        component: () => import('@/views/ums/umsMemberTag/index'),
        meta: { title: '标签管理列表', icon: 'product-list' }
      },

      {
        path: 'addUmsMemberTag',
        name: 'addUmsMemberTag',
        component: () => import('@/views/ums/umsMemberTag/add'),
        meta: { title: '添加标签管理' },
        hidden: true
      },
      {
        path: 'updateUmsMemberTag',
        name: 'updateUmsMemberTag',
        component: () => import('@/views/ums/umsMemberTag/update'),
        meta: { title: '编辑标签管理' },
        hidden: true
      },


      {
        path: 'jifenSetting',
        name: 'jifenSetting',
        component: () => import('@/views/ums/jifensetting'),
        meta: { title: '积分设置', icon: 'order-setting' }
      },
      {
        path: 'omsPayments',
        name: 'omsPayments',
        component: () => import('@/views/oms/omsPayments/index'),
        meta: { title: '支付配置列表', icon: 'product-list' }
      },

      {
        path: 'addOmsPayments',
        name: 'addOmsPayments',
        component: () => import('@/views/oms/omsPayments/add'),
        meta: { title: '添加支付配置' },
        hidden: true
      },
      {
        path: 'updateOmsPayments',
        name: 'updateOmsPayments',
        component: () => import('@/views/oms/omsPayments/update'),
        meta: { title: '编辑支付配置' },
        hidden: true
      },
      {
        path: 'orderSetting',
        name: 'orderSetting',
        component: () => import('@/views/oms/order/setting'),
        meta: { title: '订单设置', icon: 'order-setting' }
      },
      {
        path: 'returnReason',
        name: 'returnReason',
        component: () => import('@/views/oms/apply/reason'),
        meta: { title: '退货原因设置', icon: 'order-return-reason' }
      },
      {
        path: 'appletSetting',
        name: 'appletSetting',
        component: () => import('@/views/ums/setting'),
        meta: { title: '登录支付设置', icon: 'order-setting' }
      },
      {
        path: 'sysApaySet',
        name: 'sysApaySet',
        component: () => import('@/views/sysApaySet/sysApaySet'),
        meta: { title: '付款设置', icon: 'order-setting' }
      },
      {
        path: 'fenxiaoConfig',
        name: 'fenxiaoConfig',
        component: () => import('@/views/fenxiao/fenxiaoConfig/index'),
        meta: { title: '分销配置列表', icon: 'product-list' }
      },
      {
        path: 'addFenxiaoConfig',
        name: 'addFenxiaoConfig',
        component: () => import('@/views/fenxiao/fenxiaoConfig/add'),
        meta: { title: '添加分销配置' },
        hidden: true
      },
      {
        path: 'updateFenxiaoConfig',
        name: 'updateFenxiaoConfig',
        component: () => import('@/views/fenxiao/fenxiaoConfig/update'),
        meta: { title: '编辑分销配置' },
        hidden: true
      },
    ]
  },

  {
    path: '/tool',
    component: Layout,
    redirect: '/tool/email',
    name: 'tool',
    meta: { title: '工具调试', icon: 'home' },
    children: [
      {
        path: 'webLog',
        name: 'webLog',
        component: () => import('@/views/sys/webLog/index'),
        meta: { title: '前台日志列表', icon: 'product-list' }
      }, {
        path: 'adminLog',
        name: 'adminLog',
        component: () => import('@/views/sys/adminLog/index'),
        meta: { title: '后台日志列表', icon: 'product-list' }
      }, {
        path: 'adminLogStatic',
        name: 'adminLogStatic',
        component: () => import('@/views/sys/adminLog/adminLogStatic'),
        meta: { title: '后台日志列表', icon: 'product-list' }
      },
      {
        path: 'webLogStatic',
        name: 'webLogStatic',
        component: () => import('@/views/sys/webLog/webLogStatic'),
        meta: { title: '后台日志列表', icon: 'product-list' }
      },
      {
        path: 'alipay',
        name: 'alipay',
        component: () =>
          import('@/views/sys/aliPay/index'),
        meta:
        {
          title: '支付宝测试',
          icon: 'dict-update'
        },
        hidden: true
      },
      {
        path: 'email',
        name: 'email',
        component: () =>
          import('@/views/sys/email/index'),
        meta:
        {
          title: '邮件测试',
          icon: 'dict-update'
        },
        hidden: true
      },
    ]
  },
  {
    path: '/store',
    component: Layout,
    redirect: '/store/admin',
    name: 'store',
    meta: { title: '店铺管理', icon: 'home' },
    children: [
      {
        path: 'sysStoreComment',
        name: 'sysStoreComment',
        component: () => import('@/views/sys/sysStoreComment/index'),
        meta: { title: '商户评价列表', icon: 'product-list' }
      },



      {
        path: 'sysStoreClass',
        name: 'sysStoreClass',
        component: () => import('@/views/sys/sysStoreClass/index'),
        meta: { title: '商户分类列表', icon: 'product-list' }
      },

      {
        path: 'addSysStoreClass',
        name: 'addSysStoreClass',
        component: () => import('@/views/sys/sysStoreClass/add'),
        meta: { title: '添加商户分类' },
        hidden: true
      },
      {
        path: 'updateSysStoreClass',
        name: 'updateSysStoreClass',
        component: () => import('@/views/sys/sysStoreClass/update'),
        meta: { title: '编辑商户分类' },
        hidden: true
      },
      {
        path: 'sysStoreCash',
        name: 'sysStoreCash',
        component: () => import('@/views/sys/sysStoreCash/index'),
        meta: { title: '商家提现', icon: 'product-list' }
      },

      {
        path: 'addSysStoreCash',
        name: 'addSysStoreCash',
        component: () => import('@/views/sys/sysStoreCash/add'),
        meta: { title: '添加商家提现' },
        hidden: true
      },
      {
        path: 'updateSysStoreCash',
        name: 'updateSysStoreCash',
        component: () => import('@/views/sys/sysStoreCash/update'),
        meta: { title: '编辑商家提现' },
        hidden: true
      },

      {
        path: 'sysStoreDepositLog',
        name: 'sysStoreDepositLog',
        component: () => import('@/views/sys/sysStoreDepositLog/index'),
        meta: { title: '商家预存款', icon: 'product-list' }
      },

      {
        path: 'addSysStoreDepositLog',
        name: 'addSysStoreDepositLog',
        component: () => import('@/views/sys/sysStoreDepositLog/add'),
        meta: { title: '添加商家预存款' },
        hidden: true
      },
      {
        path: 'updateSysStoreDepositLog',
        name: 'updateSysStoreDepositLog',
        component: () => import('@/views/sys/sysStoreDepositLog/update'),
        meta: { title: '编辑商家预存款' },
        hidden: true
      },

      {
        path: 'store',
        name: 'store',
        component: () => import('@/views/sys/store/index'),
        meta: { title: '商家', icon: 'product-list' }
      },

      {
        path: 'addStore',
        name: 'addStore',
        component: () => import('@/views/sys/store/add'),
        meta: { title: '添加商家' },
        hidden: true
      },
      {
        path: 'updateStore',
        name: 'updateStore',
        component: () => import('@/views/sys/store/update'),
        meta: { title: '编辑商家' },
        hidden: true
      },
      {
        path: 'adminDayStatics',
        name: 'adminDayStatics',
        component: () => import('@/views/sys/adminDayStatics/index'),
        meta: { title: '商户日统计列表', icon: 'product-list' }
      },
      {
        path: 'sysStoreRank',
        name: 'sysStoreRank',
        component: () => import('@/views/sys/sysStoreRank/index'),
        meta: { title: '商户等级列表', icon: 'product-list' }
      },

      {
        path: 'addSysStoreRank',
        name: 'addSysStoreRank',
        component: () => import('@/views/sys/sysStoreRank/add'),
        meta: { title: '添加商户等级' },
        hidden: true
      },
      {
        path: 'updateSysStoreRank',
        name: 'updateSysStoreRank',
        component: () => import('@/views/sys/sysStoreRank/update'),
        meta: { title: '编辑商户等级' },
        hidden: true
      },
    ]
  },
  {
    path: '/merchat',
    component: Layout,
    redirect: '/merchat/merchatBusinessMaterialsList',
    name: 'merchat',
    meta: { title: '特约商户申请列表', icon: 'home' },
    children: [
      {
        path: 'merchatBusinessMaterialsList',
        name: 'merchatBusinessMaterialsList',
        component: () => import('@/views/merchat/index'),
        meta: { title: '特约商户申请列表', icon: 'product-list' }
      },
      {
        path: 'addMerchatBusinessMaterials',
        name: 'addMerchatBusinessMaterials',
        component: () => import('@/views/merchat/add'),
        meta: { title: '添加商户号', icon: 'product-list' },
        hidden: true
      },
      {
        path: 'updateMerchatBusinessMaterials',
        name: 'updateMerchatBusinessMaterials',
        component: () => import('@/views/merchat/update'),
        meta: { title: '修改商户号', icon: 'product-list' },
        hidden: true
      },
    ]
  },
  {
    path: '/sys',
    component: Layout,
    redirect: '/sys/admin',
    name: 'sys',
    meta: { title: '系统管理', icon: 'home' },
    children: [
      {
        path: 'merchatFacilitatorConfig',
        name: 'merchatFacilitatorConfig',
        component: () => import('@/views/sys/merchatFacilitatorConfig/add'),
        meta: { title: '服务商配置', icon: 'product-list' }
      },
      // {
      // path: 'addmerchat',
      // name: 'addmerchat',
      // src\views\sys\merchat\add.vue
      // component: () => import('./views/sys/merchatFacilitatorConfig/add'),
      // meta: {title: '添加商户号', icon: 'permissionCategory-add'},
      // hidden: true
      // },
      // {
      //   path: 'editmerchat',
      //   name: 'editmerchat',
      //   component: () => import('@/views/sys/merchatFacilitatorConfig/update'),
      //   meta: {title: '修改商户号', icon: 'permissionCategory-update'},
      //   hidden: true
      // },
      {
        path: 'admin',
        name: 'admin',
        component: () => import('@/views/sys/admin/index'),
        meta: { title: '用户列表', icon: 'product-list' }
      },
      {
        path: 'userCommunity',
        name: 'userCommunity',
        component: () => import('@/views/sys/admin/userCommunity'),
        meta: { title: '绑定小区', icon: 'product-list' }
      },
      {
        path: 'gen',
        name: 'gen',
        component: () => import('@/views/sys/gen/index'),
        meta: { title: '代码生成', icon: 'product-list' }
      }, {
        path: 'permissionCategory',
        name: 'permissionCategory',
        component: () => import('@/views/sys/permissionCategory/index'),
        meta: { title: '菜单类别列表}' }
      },
      {
        path: 'addPermissionCategory',
        name: 'addPermissionCategory',
        component: () => import('@/views/sys/permissionCategory/add'),
        meta: { title: '添加菜单类别', icon: 'permissionCategory-add' },
        hidden: true
      },
      {
        path: 'updatePermissionCategory',
        name: 'updatePermissionCategory',
        component: () => import('@/views/sys/permissionCategory/update'),
        meta: { title: '修改菜单类别', icon: 'permissionCategory-update' },
        hidden: true
      },
      {
        path: 'addAdmin',
        name: 'addAdmin',
        component: () => import('@/views/sys/admin/add'),
        meta: { title: '添加用户' },
        hidden: true
      },
      {
        path: 'updateAdmin',
        name: 'updateAdmin',
        component: () => import('@/views/sys/admin/update'),
        meta: { title: '编辑用户' },
        hidden: true
      }, {
        path: 'role',
        name: 'role',
        component: () => import('@/views/sys/role/index'),
        meta: { title: '角色列表', icon: 'product-list' }
      },
      {
        path: 'addRole',
        name: 'addRole',
        component: () => import('@/views/sys/role/add'),
        meta: { title: '添加角色' },
        hidden: true
      },
      {
        path: 'updateRole',
        name: 'updateRole',
        component: () => import('@/views/sys/role/update'),
        meta: { title: '编辑角色' },
        hidden: true
      }, {
        path: 'permission',
        name: 'permission',
        component: () => import('@/views/sys/permission/index'),
        meta: { title: '权限列表', icon: 'product-list' }
      },
      {
        path: 'addPermission',
        name: 'addPermission',
        component: () => import('@/views/sys/permission/add'),
        meta: { title: '添加权限' },
        hidden: true
      },
      {
        path: 'updatePermission',
        name: 'updatePermission',
        component: () => import('@/views/sys/permission/update'),
        meta: { title: '编辑权限' },
        hidden: true
      },
      {
        path: 'area',
        name: 'area',
        component: () => import('@/views/sys/area/index'),
        meta: { title: '区域列表', icon: 'product-list' }
      },
      {
        path: 'addArea',
        name: 'addArea',
        component: () => import('@/views/sys/area/add'),
        meta: { title: '添加区域' },
        hidden: true
      },
      {
        path: 'updateArea',
        name: 'updateArea',
        component: () => import('@/views/sys/area/update'),
        meta: { title: '编辑区域' },
        hidden: true
      },
      {
        path: 'dict',
        name: 'dict',
        component: () => import('@/views/sys/dict/index'),
        meta:
        {
          title: '系统配置信息表列表}'
        }
      },
      {
        path: 'addDict',
        name: 'addDict',
        component: () => import('@/views/sys/dict/add'),
        meta: {
          title: '添加系统配置信息表',
          icon: 'dict-add'
        }
      },
      {
        path: 'updateDict',
        name: 'updateDict',
        component: () =>
          import('@/views/sys/dict/update'),
        meta:
        {
          title: '修改系统配置信息表',
          icon: 'dict-update'
        },
        hidden: true
      }, {
        path: 'job',
        name: 'job',
        component: () => import('@/views/sys/job/index'),
        meta:
        {
          title: '定时任务息表列表'
        }
      }, {
        path: 'joblog',
        name: 'joblog',
        component: () => import('@/views/sys/job/joblog'),
        meta:
        {
          title: '定时任务息表列表'
        }
      },
      {
        path: 'addJob',
        name: 'addJob',
        component: () => import('@/views/sys/job/add'),
        meta: {
          title: '添加定时任务',
          icon: 'dict-add'
        }
      },
      {
        path: 'updateJob',
        name: 'updateJob',
        component: () =>
          import('@/views/sys/job/update'),
        meta:
        {
          title: '修改定时任务',
          icon: 'dict-update'
        },
        hidden: true
      },

    ]
  },

  {
    path: '/help',
    component: Layout,
    redirect: '/help/help',
    name: 'help',
    meta: { title: '帮助中心', icon: 'home' },
    children: [
      {
        path: 'help',
        name: 'help',
        component: () => import('@/views/cms/help/index'),
        meta: { title: '帮助列表', icon: 'product-list' }
      }, {
        path: 'addHelp',
        name: 'addHelp',
        component: () => import('@/views/cms/help/add'),
        meta: { title: '添加帮助' },
        hidden: true
      },
      {
        path: 'updateHelp',
        name: 'updateHelp',
        component: () => import('@/views/cms/help/update'),
        meta: { title: '编辑帮助' },
        hidden: true
      },
      {
        path: 'helpCategory',
        name: 'helpCategory',
        component: () => import('@/views/cms/helpCategory/index'),
        meta: { title: '专题分类列表', icon: 'product-list' }
      },
      {
        path: 'addHelpCategory',
        name: 'addHelpCategory',
        component: () => import('@/views/cms/helpCategory/add'),
        meta: { title: '添加专题分类' },
        hidden: true
      },
      {
        path: 'updateHelpCategory',
        name: 'updateHelpCategory',
        component: () => import('@/views/cms/helpCategory/update'),
        meta: { title: '编辑专题分类' },
        hidden: true
      },
      {
        path: 'helpSet',
        name: 'helpSet',
        component: () => import('@/views/cms/helpSet/index'),
        meta: { title: '帮助设置', icon: 'product-list' }
      }
    ]
  },
 

  {
    path: '/cms',
    component: Layout,
    redirect: '/cms/help',
    name: 'cms',
    meta: { title: '内容管理', icon: 'home' },
    children: [
      {
        path: 'cmsZhaoPin',
        name: 'cmsZhaoPin',
        component: () => import('@/views/cms/cmsZhaoPin/index'),
        meta: { title: '招聘管理列表', icon: 'product-list' }
      },

      {
        path: 'addCmsZhaoPin',
        name: 'addCmsZhaoPin',
        component: () => import('@/views/cms/cmsZhaoPin/add'),
        meta: { title: '添加招聘管理' },
        hidden: true
      },
      {
        path: 'updateCmsZhaoPin',
        name: 'updateCmsZhaoPin',
        component: () => import('@/views/cms/cmsZhaoPin/update'),
        meta: { title: '编辑招聘管理' },
        hidden: true
      },

      {
        path: 'subject',
        name: 'subject',
        component: () => import('@/views/cms/subject/index'),
        meta: { title: '专题列表', icon: 'product-list' }
      }, {
        path: 'addSubject',
        name: 'addSubject',
        component: () => import('@/views/cms/subject/add'),
        meta: { title: '添加专题' },
        hidden: true
      },
      {
        path: 'updateSubject',
        name: 'updateSubject',
        component: () => import('@/views/cms/subject/update'),
        meta: { title: '编辑专题' },
        hidden: true
      },
      {
        path: 'memberReport',
        name: 'memberReport',
        component: () => import('@/views/cms/memberReport/index'),
        meta: { title: '帮助列表', icon: 'product-list' }
      }, {
        path: 'addMemberReport',
        name: 'addMemberReport',
        component: () => import('@/views/cms/memberReport/add'),
        meta: { title: '添加权限' },
        hidden: true
      },
      {
        path: 'updateMemberReport',
        name: 'updateMemberReport',
        component: () => import('@/views/cms/memberReport/update'),
        meta: { title: '编辑权限' },
        hidden: true
      }
      ,
      {
        path: 'prefrenceArea',
        name: 'prefrenceArea',
        component: () => import('@/views/cms/prefrenceArea/index'),
        meta: { title: '偏爱专区列表', icon: 'product-list' }
      }, {
        path: 'addPrefrenceArea',
        name: 'addPrefrenceArea',
        component: () => import('@/views/cms/prefrenceArea/add'),
        meta: { title: '添加偏爱专区' },
        hidden: true
      },
      {
        path: 'updatePrefrenceArea',
        name: 'updatePrefrenceArea',
        component: () => import('@/views/cms/prefrenceArea/update'),
        meta: { title: '编辑偏爱专区' },
        hidden: true
      }

      ,
      {
        path: 'subjectCategory',
        name: 'subjectCategory',
        component: () => import('@/views/cms/subjectCategory/index'),
        meta: { title: '帮助列表', icon: 'product-list' }
      }, {
        path: 'addSubjectCategory',
        name: 'addSubjectCategory',
        component: () => import('@/views/cms/subjectCategory/add'),
        meta: { title: '添加权限' },
        hidden: true
      },
      {
        path: 'updateSubjectCategory',
        name: 'updateSubjectCategory',
        component: () => import('@/views/cms/subjectCategory/update'),
        meta: { title: '编辑权限' },
        hidden: true
      }

      ,
      {
        path: 'subjectComment',
        name: 'subjectComment',
        component: () => import('@/views/cms/subjectComment/index'),
        meta: { title: '帮助列表', icon: 'product-list' }
      }, {
        path: 'addSubjectComment',
        name: 'addSubjectComment',
        component: () => import('@/views/cms/subjectComment/add'),
        meta: { title: '添加权限' },
        hidden: true
      },
      {
        path: 'updateSubjectComment',
        name: 'updateSubjectComment',
        component: () => import('@/views/cms/subjectComment/update'),
        meta: { title: '编辑权限' },
        hidden: true
      }

      , {
        path: 'topic',
        name: 'topic',
        component: () => import('@/views/cms/topic/index'),
        meta: { title: '帮助列表', icon: 'product-list' }
      }, {
        path: 'addTopic',
        name: 'addTopic',
        component: () => import('@/views/cms/topic/add'),
        meta: { title: '添加权限' },
        hidden: true
      },
      {
        path: 'updateTopic',
        name: 'updateTopic',
        component: () => import('@/views/cms/topic/update'),
        meta: { title: '编辑权限' },
        hidden: true
      }

      , {
        path: 'topicCategory',
        name: 'topicCategory',
        component: () => import('@/views/cms/topicCategory/index'),
        meta: { title: '帮助列表', icon: 'product-list' }
      }, {
        path: 'addTopicCategory',
        name: 'addTopicCategory',
        component: () => import('@/views/cms/topicCategory/add'),
        meta: { title: '添加权限' },
        hidden: true
      },
      {
        path: 'updateTopicCategory',
        name: 'updateTopicCategory',
        component: () => import('@/views/cms/topicCategory/update'),
        meta: { title: '编辑权限' },
        hidden: true
      }

      , {
        path: 'topicComment',
        name: 'topicComment',
        component: () => import('@/views/cms/topicComment/index'),
        meta: { title: '帮助列表', icon: 'product-list' }
      }

    ]
  },

  { path: '*', redirect: '/404', hidden: true }
]

export default new Router({
  // mode: 'history', //后端支持可开
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})

