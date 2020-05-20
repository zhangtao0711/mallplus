<template>
  <view>
    <block v-for="(item,index) in jdata" :key="index">
      <jshopsearch :jdata="item" v-if="item.components=='search' "></jshopsearch>
	  <jshoptabbar :jdata="item" v-if="item.components=='tabBar' "></jshoptabbar>
      <jshopnotice :jdata="item" v-if="item.components=='notice' "></jshopnotice>
      <jshopimgSlide :jdata="item" v-if="item.components=='banner' "></jshopimgSlide>
      <jshopcoupon :jdata="item" v-if="item.components=='coupon' "></jshopcoupon>
      <jshopblank :jdata="item" v-if="item.components=='blank' "></jshopblank>
      <jshoptextarea :jdata="item" v-if="item.components=='textarea' "></jshoptextarea>
      <jshopvideo :jdata="item" v-if="item.components=='video' "></jshopvideo>
      <jshopimgWindow :jdata="item" v-if="item.components=='imgWin' "></jshopimgWindow>
      <jshopimgSingle :jdata="item" v-if="item.components=='imgImgSlide' "></jshopimgSingle>
      <jshopgoods :jdata="item" v-if="item.components=='shuanglieshangpin' "></jshopgoods>
       <jshopslidegoods :jdata="item" v-if="item.components=='danlieshangpin' "></jshopslidegoods>

      <jshoparticle :jdata="item" v-if="item.components=='article' "></jshoparticle>
      <jshoparticleClassify :jdata="item" v-if="item.components=='articleClassify' "></jshoparticleClassify>
      <jshopnavBar :jdata="item" v-if="item.components=='xuanxiangqia' "></jshopnavBar>
      <jshopgroupPurchase :jdata="item" v-if="item.components=='groupPurchase' "></jshopgroupPurchase>
      <jshoprecord :jdata="item" v-if="item.components=='record' "></jshoprecord>
	  <jshoppintuan :jdata="item" v-if="item.components=='pintuan' "></jshoppintuan>
	  <jshopadpop :jdata="item" v-if="item.components=='imgGroup' "></jshopadpop>
    </block>
  </view>
</template>

<script>
/**
 * 吉海科技jshop小程序插件集合。
 * author:novice
 * date:2019:05:20
 */
import uniCountdown from '@/components/uni-countdown/uni-countdown.vue'
import jshopimgSlide from '@/components/jshop/jshop-imgSlide.vue'
import jshopsearch from '@/components/jshop/jshop-search.vue'
import jshopnotice from '@/components/jshop/jshop-notice.vue'
import jshopcoupon from '@/components/jshop/jshop-coupon.vue'
import jshopblank from '@/components/jshop/jshop-blank.vue'
import jshoptextarea from '@/components/jshop/jshop-textarea.vue'
import jshopvideo from '@/components/jshop/jshop-video.vue'
import jshopimgWindow from '@/components/jshop/jshop-imgWindow.vue'
import jshopimgSingle from '@/components/jshop/jshop-imgSingle.vue'
import jshopgoods from '@/components/jshop/jshop-goods.vue'
import jshoponegoods from '@/components/jshop/jshop-onegoods.vue'
import jshopslidegoods from '@/components/jshop/jshop-slidegoods.vue'
import jshoparticle from '@/components/jshop/jshop-article.vue'
import jshoparticleClassify from '@/components/jshop/jshop-articleClassify.vue'
import jshopnavBar from '@/components/jshop/jshop-navBar.vue'
import jshopgroupPurchase from '@/components/jshop/jshop-groupPurchase.vue'
import jshoprecord from '@/components/jshop/jshop-record.vue'
import jshoppintuan from '@/components/jshop/jshop-pintuan.vue'
import jshoptabbar from '@/components/jshop/jshop-tabbar.vue'
import jshopadpop from '@/components/jshop/jshop-adpop.vue'

export default {
  name: 'jshop',
  components: {
    jshopimgSlide,
    jshopsearch,
    jshopnotice,
    jshoponegoods,
    jshopslidegoods,
    jshopcoupon,
    jshopblank,
    jshoptextarea,
    jshopvideo,
    jshopimgWindow,
    jshopimgSingle,
    jshopgoods,
    jshoparticle,
    jshoparticleClassify,
    jshopnavBar,
    jshopgroupPurchase,
    jshoprecord,
	jshoppintuan,
	jshoptabbar,
	jshopadpop
  },
  props: {
    jdata: {
      default: function() {
        return []
      }
    }
  }
}
</script>
