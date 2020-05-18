<template>
	<view class="container">
		<!-- 小程序头部兼容 -->
		<!-- #ifdef MP -->
		<nav-bar>mallcloud商城</nav-bar>
		<view class="MP-search" @click="search()" style=""><input class="MP-search-input" type="text" disabled="true" placeholder="输入关键字搜索" /></view>
		<!-- #endif -->

		<!-- 头部轮播 -->
		<!-- #ifdef MP -->
		<view class="carousel-section" style="margin-top: 80upx;">
			<!-- #endif -->
			<!-- #ifndef MP -->
			<view class="carousel-section">
				<!-- #endif -->
				<!-- 标题栏和状态栏占位符 -->
				<view class="titleNview-placing"></view>
				<!-- 背景色区域 -->
				<view class="titleNview-background" :style="{ backgroundColor: titleNViewBackground }"></view>
				<swiper class="carousel" circular @change="swiperChange">
					<swiper-item v-for="(item, index) in carouselList" :key="index" class="carousel-item" @click="navToDetailPageL(item.url)">
						<image :src="item.pic" />
					</swiper-item>
				</swiper>
				<!-- 自定义swiper指示器 -->
				<view class="swiper-dots">
					<text class="num">{{ swiperCurrent + 1 }}</text>
					<text class="sign">/</text>
					<text class="num">{{ swiperLength }}</text>
				</view>
			</view>
			<!-- 分类 -->
			<view class="cate-section">
				<view class="cate-item" @click="navToTabPage('../../pagesA/product/groupActivityList')">
					<image src="/static/temp/c3.png"></image>
					<text>套餐商品</text>
				</view>
				<view class="cate-item" @click="navToTabPage('../../pagesA/product/groupList')">
					<image src="/static/temp/c5.png"></image>
					<text>拼团活动</text>
				</view>
				<view class="cate-item" @click="navToTabPage('../../pagesA/product/giftList')">
					<image src="/static/temp/c6.png"></image>
					<text>积分商城</text>
				</view>
				<view class="cate-item" @click="navToTabPage('/pages/index/secskill')">
					<image src="/static/temp/c7.png"></image>
					<text>限时秒杀</text>
				</view>
				<view class="cate-item" @click="navToTabPage('../../pagesA/build/communityList')">
					<image src="/static/temp/c8.png"></image>
					<text>社区商城</text>
				</view>
			</view>
			<view class="cate-section">
				<view class="cate-item" @click="navToTabPage('../../pagesA/product/list?isFenxiao=1')">
					<image src="/static/temp/c3.png"></image>
					<text>分销商品</text>
				</view>
				<view class="cate-item" @click="navToTabPage('../../pagesA/product/list?isVip=1')">
					<image src="/static/temp/c5.png"></image>
					<text>会员商品</text>
				</view>
				<view class="cate-item" @click="navToTabPage('/pages/category/areaGoods')">
					<image src="/static/temp/c6.png"></image>
					<text>区域商品</text>
				</view>
				<view class="cate-item" @click="navToTabPage('../../pagesC/seller/index')">
					<image src="/static/temp/c7.png"></image>
					<text>卖家中心</text>
				</view>
				<view class="cate-item" @click="navToTabPage('../../pagesA/product/tag')">
					<image src="/static/temp/c8.png"></image>
					<text>精选标签</text>
				</view>
			</view>

			<!-- 秒杀楼层 https://s.click.taobao.com/Wds7c1w -->
			<view class="seckill-section m-t" v-if="homeFlashPromotion.flashSessionInfoList && homeFlashPromotion.flashSessionInfoList.length > 0">
				<view class="s-header">
					<view class="" style="width: 80%;display: flex;flex-direction: row;align-items: center;">
						<image class="s-img" src="/static/temp/secskill-img.jpg" mode="widthFix" @click="navToDetailPageL('https://s.click.taobao.com/Wds7c1w')"></image>
						<text class="tip" style="margin-left: 20upx;">{{ homeFlashPromotion.flashName }}</text>
						<view class="s-header" style="width:35%;margin-left: 20upx;">
							<text class="tip">{{ homeFlashPromotion.startTime || '18:00' }}</text>
							<text class="tip">~</text>
							<text class="tip">{{ homeFlashPromotion.endTime || '24:00' }}</text>
						</view>
					</view>
					<text class="yticon icon-you" @click="navToTabPage('/pages/index/secskill')"></text>
				</view>
				<view v-if="item2.productList && item2.productList.length > 0" v-for="(item2, index2) in homeFlashPromotion.flashSessionInfoList" :key="index2">
					<scroll-view class="floor-list" scroll-x>
						<view class="scoll-wrapper">
							<view v-for="(item, index) in item2.productList" :key="index" class="floor-item" @click="navToSkillDetailPage(item)">
								<image :src="item.productImg" mode="aspectFill"></image>
								<text class="title clamp">{{ item.productName }}</text>
								<text class="price">￥{{ item.flashPromotionPrice }}</text>
							</view>
						</view>
					</scroll-view>
				</view>
			</view>
			<view class="f-header m-t" @click="navToTabPage('../../pagesC/store/storeList')">
				<image src="/static/temp/nav1.png"></image>
				<view class="tit-box">
					<text class="tit">精品店铺</text>
					<text class="tit2">Guess You Like It</text>
				</view>
				<text class="yticon icon-you"></text>
			</view>
			<view class="cate-section">
				<view class="cate-item" @click="navToTabPageStore(item1.id)" v-for="(item1, index) in storeList" :key="index">
					<image v-if="item1.logo" :src="item1.logo"></image>
					<image  v-else src="/static/missing-face.png"></image>
					<text>{{ item1.name |formatLongText }}</text>
				</view>
			</view>

			<!-- 优惠券  https://s.click.taobao.com/OPh3c1w -->
			<coupon v-for="(item, index) in couponList" :key="index" v-bind:item="item" theme="#ff0000"></coupon>

			<!-- 团购楼层 -->
			<view class="f-header m-t" @click="navToTabPage('../../pagesA/product/groupList')" v-if="groupHotGoodsList.length > 0">
				<image src="/static/temp/h1.png"></image>
				<view class="tit-box">
					<text class="tit">精品团购</text>
					<text class="tit2">Boutique Group Buying</text>
				</view>
				<text class="yticon icon-you"></text>
			</view>
			<view class="group-section">
				<swiper class="g-swiper" :duration="500">
					<swiper-item class="g-swiper-item" v-for="(item, index) in groupHotGoodsList" :key="index" v-if="index % 2 === 0" @click="navToGroupDetailPage(item)">
						<view class="g-item left">
							<image :src="item.goods.pic" mode="aspectFill"></image>
							<view class="t-box">
								<text class="title clamp">{{ item.goods.title }}</text>
								<view class="price-box">
									<text class="price">{{ item.groupPrice }}</text>
									<text class="m-price">￥{{ item.goods.price }}</text>
								</view>
								<view class="pro-box">
									<view class="progress-box"><progress percent="72" activeColor="#fa436a" active stroke-width="6" /></view>
									<text>{{ item.maxPeople }}人成团</text>
								</view>
							</view>
						</view>
					</swiper-item>
				</swiper>
			</view>

			<!-- 分类推荐楼层 -->
			<view class="f-header m-t" @click="navToTabPage('../../pagesA/product/list')" v-if="false">
				<image src="/static/temp/h1.png"></image>
				<view class="tit-box">
					<text class="tit">分类精选</text>
					<text class="tit2">Competitive Products For You</text>
				</view>
				<text class="yticon icon-you"></text>
			</view>
			<view class="hot-floor" v-if="item1.goodsList.length > 0" v-for="(item1, index) in cat_list" :key="index">
				<view class="floor-img-box"><image class="floor-img" :src="item1.pic" mode="scaleToFill"></image></view>
				<scroll-view class="floor-list" scroll-x>
					<view class="scoll-wrapper">
						<view v-for="(item, index) in item1.goodsList" :key="index" class="floor-item" @click="navToDetailPage(item)">
							<image :src="item.pic" mode="aspectFill"></image>
							<text class="title clamp">{{ item.name }}</text>
							<text class="price">￥{{ item.price }}</text>
						</view>
						<view class="more" @click="navToList()">
							<text>查看全部</text>
							<text>More+</text>
						</view>
					</view>
				</scroll-view>
			</view>

<!-- 新品推荐 -->
			<view v-if='homeNewProductList && homeNewProductList.length>0' class="f-header m-t" @click="navToTabPage('../../pagesA/product/list')">
				<image src="/static/temp/h1.png"></image>
				<view class="tit-box">
					<text class="tit">新品推荐</text>
					<text class="tit2">Guess You Like It</text>
				</view>
				<text class="yticon icon-you"></text>
			</view>

			<view class="guess-section">
				<view v-for="(item, index) in homeNewProductList" :key="index" class="guess-item" @click="navToDetailPage1(item)">
					<view class="image-wrapper"><image :src="item.pic" mode="aspectFill"></image></view>
					<text class="title clamp">{{ item.productName }}</text>
					<text class="price">￥{{ item.price }}</text>

				</view>

			</view>
			<!-- 人气推荐 -->
            			<view v-if='homeRecommendProductList && homeRecommendProductList.length>0' class="f-header m-t" @click="navToTabPage('../../pagesA/product/list')">
            				<image src="/static/temp/h1.png"></image>
            				<view class="tit-box">
            					<text class="tit">人气推荐</text>
            					<text class="tit2">Guess You Like It</text>
            				</view>
            				<text class="yticon icon-you"></text>
            			</view>

            			<view class="guess-section">
            				<view v-for="(item, index) in homeRecommendProductList" :key="index" class="guess-item" @click="navToDetailPage1(item)">
            					<view class="image-wrapper"><image :src="item.pic" mode="aspectFill"></image></view>
            					<text class="title clamp">{{ item.name }}</text>
            					<text class="price">￥{{ item.price }}</text>

            				</view>
            				
            			</view>
			<!-- 新品上市 -->
			<view class="f-header m-t" @click="navToTabPage('../../pagesA/product/list')">
				<image src="/static/temp/h1.png"></image>
				<view class="tit-box">
					<text class="tit">新品上市</text>
					<text class="tit2">Guess You Like It</text>
				</view>
				<text class="yticon icon-you"></text>
			</view>

			<view class="guess-section">
				<view v-for="(item, index) in newProductList" :key="index" class="guess-item" @click="navToDetailPage(item)">
					<view class="image-wrapper"><image :src="item.pic" mode="aspectFill"></image></view>
					<text class="title clamp">{{ item.name }}</text>
					<text class="price">￥{{ item.price }}</text>
					<text>【{{ item.storeName }}】</text>
				</view>
				<uni-load-more :status="loadingType"></uni-load-more>
			</view>
			<mallplusCopyright></mallplusCopyright>
		</view>
	</view>
</template>

<script>
import mallplusCopyright from '@/components/mall-copyright/mallplusCopyright.vue';
import Api from '@/common/api';
import coupon from '@/components/coolc-coupon/coolc-coupon';

import { formatDate } from '@/common/date';
import { mapState } from 'vuex';
import navBar from '@/components/zhouWei-navBar';
import uniLoadMore from '@/components/uni-load-more/uni-load-more.vue';
export default {
	components: {
		coupon,
		mallplusCopyright,
		uniLoadMore,
		navBar
	},
	data() {
		return {
			keyword: '',
			pageNum: 1,
			loadingType: 'more', //加载更多状态
			titleNViewBackground: '',
			swiperCurrent: 0,
			swiperLength: 0,
			homeNewProductList: [],
            homeRecommendProductList: [],
			carouselList: [],
			hotProductList: [],
			brandList: [], // 推荐品牌
			homeFlashPromotion: [], // 当前秒杀场次
			subjectList: [], //推荐专题
			cat_list: [],
			groupHotGoodsList: [],
			couponList: [],
			storeList: [],
			newProductList: []
		};
	},

	//加载更多
	onReachBottom() {
		this.pageNum = this.pageNum + 1;

		this.getNewProductList();
	},
	onPullDownRefresh() {
		this.pageNum = this.pageNum + 1;
		this.getNewProductList('refresh');

		this.loadData();
		setTimeout(function() {
			uni.stopPullDownRefresh();
		}, 2000);
	},
	onShareAppMessage() {},
	onLoad(ops) {
		let isWeiXin = this.$common.isWeiXinBrowser();
		this.getNewProductList('refresh');
		if (ops.invitecode) {
			this.$db.set('invitecode', ops.invitecode);
		}
		this.loadData();
	},
filters: {
                    		  formatCreateTime(time) {
                                     let date = new Date(time);
                                     return formatDate(date, 'yyyy-MM-dd hh:mm:ss')
                                   },
                                  formatNull(value) {
                                    if(value===undefined||value===null||value===''){
                                      return '暂无';
                                    }else{
                                      return value;
                                    }
                                  },
                                  formatLongText(value) {
                                    if(value===undefined||value===null||value===''){
                                      return '暂无';
                                    }else if(value.length>4){
                                      return value.substr(0, 4) + '...';
                                    }else{
                                      return value;
                                    }
                                  },
                                  formatPayType(value) {
                                    if (value === 2) {
                                      return '支付宝';
                                    } else if (value === 1) {
                                      return '微信小程序';
                                    } else  if (value === 3){
                                      return '余额支付';
                                    } else  if (value === 5){
                                               return '积分兑换';
                                             }
                                  },
                                  },
	methods: {
		getCode() {
			var code = this.$common.getQueryString('code');
			code && this.getOpenId(code);
		},
		async getOpenId(code) {
			console.log(code);
			let params = {
				code: code,
				scope: 2,
				state: this.state
			};
			//模拟接口
			let res = await Api.apiCall('get', Api.order.authCodeToOpenid, params);
			console.log(res);
			if (res) {
				this.login(data);
				console.log(data);
				this.$db.set('userInfos', data.userInfo);
				this.$db.set('token', data.tokenHead + data.token);
				uni.switchTab({
					url: '/pages/index/index'
				});

				//this.openid = res.data
				//  this.toPayHandler('wechatpay')
			} else {
				this.$common.errorToShow(res);
			}
		},
		checkWXJSBridge(data) {
			let that = this;
			let interval = setInterval(() => {
				if (typeof window.WeixinJSBridge != 'undefined') {
					clearTimeout(interval);
					that.onBridgeReady(data);
				}
			}, 200);
		},
		onBridgeReady(data) {
			var _this = this;
			window.WeixinJSBridge.invoke(
				'getBrandWCPayRequest',
				{
					appId: data.appid, // 公众号名称，由商户传入
					timeStamp: data.timeStamp, // 时间戳，自1970年以来的秒数
					nonceStr: data.nonceStr, // 随机串
					package: data.package,
					signType: data.signType, // 微信签名方式：
					paySign: data.paySign // 微信签名
				},
				function(res) {
					if (res.err_msg === 'get_brand_wcpay_request:ok') {
						_this.$common.successToShow('支付成功');
					} else if (res.err_msg === 'get_brand_wcpay_request:cancel') {
						_this.$common.errorToShow('取消支付');
					} else {
						_this.$common.errorToShow('支付失败');
					}
					setTimeout(() => {
						_this.$common.redirectTo('/pages/order/payment/result?id=' + data.id);
					}, 1000);
				}
			);
		},

		/**
		 *
		 * 加载首页数据
		 */
		async loadData() {
			this.getBanner();
			this.getHotGoodsList();
			//this.getFlashPromotion();
			this.getCouponList();
			this.getStoreList();
			this.homeNewProduct();
			this.homeRecommendProduct();
		},

        /**
		 * 获取新品推荐
		 */
		async homeNewProduct() {
			let params = {};
			let groupHotGoodsList = await Api.apiCall('get', Api.index.homeNewProduct, params);
			console.log(groupHotGoodsList)
			if(groupHotGoodsList){
			this.homeNewProductList = groupHotGoodsList.records;
			}


		},
		/**
		 * 获取人气推荐
		 */
		async homeRecommendProduct() {
			let params = {};
			let groupHotGoodsList = await Api.apiCall('get', Api.index.homeRecommendProduct, params);
			this.homeRecommendProductList = groupHotGoodsList.records;
		},
		/**
		 * 获取轮播图
		 */
		async getBanner() {
			let params = { storeId: 0 };
			console.log(uni.getSystemInfoSync().platform);
			switch (uni.getSystemInfoSync().platform) {
				case 'android':
					params.type = 2;
					break;
				case 'ios':
					params.type = 3;
					break;
				default:
					params.type = 5;
					break;
			}
			let data = await Api.apiCall('get', Api.index.bannerList, params);
			if (data) {
				this.carouselList = data || [];
				this.swiperLength = this.carouselList.length;
				this.titleNViewBackground = 'rgb(203, 87, 60)';
			}
		},
		/**
		 * 获取轮播图
		 */
		async getCouponList() {
			let params = { pageSize: 3 };
			let data = await Api.apiCall('get', Api.index.selectNotRecive, params);
			if (data) {
				this.couponList = data || [];
			}
		},
		/**
		 * 获取新品上市信息
		 */
		async getNewProductList(type = 'add', loading) {
			//没有更多直接返回
			if (type === 'add') {
				if (this.loadingType === 'nomore') {
					return;
				}
				this.loadingType = 'loading';
			} else {
				this.loadingType = 'more';
			}
			let params = { pageNum: this.pageNum };
			let list = await Api.apiCall('get', Api.goods.goodsList, params);

			let goodsList = list.records;

			if (type === 'refresh') {
				this.newProductList = [];
			}

			this.newProductList = this.newProductList.concat(goodsList);

			//判断是否还有下一页，有是more  没有是nomore(测试数据判断大于20就没有了)
			this.loadingType = this.newProductList.length > list.total ? 'nomore' : 'more';
			if (type === 'refresh') {
				if (loading == 1) {
					uni.hideLoading();
				} else {
					uni.stopPullDownRefresh();
				}
			}
		},
		/**
		 * 获取团购信息
		 */
		async getStoreList() {
			let params = { pageSize: 5, isBoutique: 1,sort:2 };
			let groupHotGoodsList = await Api.apiCall('get', Api.member.storeList, params);
			this.storeList = groupHotGoodsList.records;
		},
		/**
		 * 获取团购信息
		 */
		async getHotGoodsList() {
			let params = {};
			let groupHotGoodsList = await Api.apiCall('get', Api.goods.groupHotGoodsList, params);
			this.groupHotGoodsList = groupHotGoodsList;
		},

		/**
		 * 获取秒杀列表信息
		 */
		async getFlashPromotion() {
			let params = {};
			let data = await Api.apiCall('get', Api.index.homeFlashPromotionList, params);
			if (data) {
				let homeFlashPromotion = data;
				if (homeFlashPromotion.length > 0) {
					this.homeFlashPromotion = homeFlashPromotion[0];
				}
			}
		},

		/**
		 * 获取分类精选列表信息
		 */
		async getCatList() {
			let params = {};
			let data = await Api.apiCall('get', Api.goods.newProductList, params);
			if (data) {
				let cat_list = data;
				this.cat_list = cat_list || [];
			}
		},

		/**
		 * 请求静态数据只是为了代码不那么乱
		 * 分次请求未作整合
		 */
		async loadData2() {
			this.logining = true;
			let params = {};
			let data = await Api.apiCall('get', Api.index.home, params);
			let groupHotGoodsList = await Api.apiCall('get', Api.goods.groupHotGoodsList, params);
			this.groupHotGoodsList = groupHotGoodsList;
			this.logining = false;

			if (data) {
				this.carouselList = data.advertiseList || [];
				this.swiperLength = this.carouselList.length;
				this.titleNViewBackground = 'rgb(203, 87, 60)';
				let brandList = data.brandList;
				this.brandList = brandList || [];
				let cat_list = data.cat_list;
				this.cat_list = cat_list || [];
				let homeFlashPromotion = data.homeFlashPromotion;
				this.homeFlashPromotion = homeFlashPromotion || [];

				let data1 = await Api.apiCall('get', Api.index.home1, params);
				let newProductList = data1.newProductList;
				this.newProductList = newProductList || [];

				let hotProductList = data1.hotProductList;
				this.hotProductList = hotProductList || [];

				this.couponList = data1.couponList || [];
				this.orderList = this.couponList.filter(item => {
					item.createTime = this.dateFormat(item.createTime);
					return item;
				});
			}
			var userInfo = this.$db.get('userInfos');
			if (userInfo) {
				/*uni.getLocation({
					type: 'wgs84',
					success: function(res) {
						console.log(res);
						let params = { longitude: res.longitude, latitude: res.latitude, memberId: userInfo.id, userName: userInfo.username, pic: userInfo.icon};
						Api.apiCall('post', Api.index.submitLocaltion, params);

					}
				});
*/
			}
		},
		dateFormat(time) {
			if (time == null || time === '') {
				return 'N/A';
			}
			let date = new Date(time);
			return formatDate(date, 'yyyy-MM-dd hh:mm:ss');
		},
		//轮播图切换修改背景色
		swiperChange(e) {
			const index = e.detail.current;
			this.swiperCurrent = index;
			this.titleNViewBackground = this.carouselList[index].background;
		},
		navToTabPage(url) {
			uni.navigateTo({
				url: url
			});
		},
		navToTabPageStore(item) {
		//url: `../../pagesC/store/store?id=${id}`
			// url: `../../pagesC/store/businessDetails?id=${id}`
			let id = item;
			uni.navigateTo({
				url: `../../pagesC/store/businessDetails?id=${id}`
			});
		},
		//详情页
		navToDetailPage(item) {
			//测试数据没有写id，用title代替
			let id = item.id;
			uni.navigateTo({
				url: `../../pagesA/product/product?id=${id}`
			});
		},
		//详情页
        		navToDetailPage1(item) {
        			//测试数据没有写id，用title代替
        			let id = item.productId;
        			uni.navigateTo({
        				url: `../../pagesA/product/product?id=${id}`
        			});
        		},
		//详情页
		navToDetailPageL(url) {
			window.location.href = url;
		},
		//详情页
		navToCouponDetailPage(item) {
			//测试数据没有写id，用title代替
			let id = item.id;
			uni.navigateTo({
				url: `../../pagesA/product/product?id=${id}`
			});
		},
		async acceptCoupon(item) {
			uni.showLoading({
				title: '请稍后'
			});

			let params = { couponId: item.id };
			let data = await Api.apiCall('post', Api.index.acceptCoupon, params);
			console.log(data);
			if (data) {
				this.$api.msg(data);
			}
			uni.hideLoading();
		},
		navToSkillDetailPage(item) {
			//测试数据没有写id，用title代替
			let id = item.id;

			uni.navigateTo({
				url: `../../pagesA/product/secskillDetail?id=${id}`
			});
		},

		navToGroupDetailPage(item) {
			//测试数据没有写id，用title代替
			let id = item.goodsId;
			let groupId = item.groupId;
			uni.navigateTo({
				url: `../../pagesA/product/groupProduct?id=${id}&&groupId=&{groupId}`
			});
		},
		navToList() {
			uni.navigateTo({
				url: `../../pagesA/product/list`
			});
		},
		search() {
			uni.navigateTo({
				url: '/pages/search/search'
			});
		}
	},

	// 标题栏input搜索框点击
	onNavigationBarSearchInputClicked: async function(e) {
		uni.navigateTo({
			url: '/pages/search/search'
		});
	},
	//点击导航栏 buttons 时触发
	onNavigationBarButtonTap(e) {
		const index = e.index;
		if (index === 0) {
			this.$api.msg('点击了扫描');
		} else if (index === 1) {
			// #ifdef APP-PLUS
			const pages = getCurrentPages();
			const page = pages[pages.length - 1];
			const currentWebview = page.$getAppWebview();
			currentWebview.hideTitleNViewButtonRedDot({
				index
			});
			// #endif
			uni.navigateTo({
				url: '../../pagesU/notice/notice'
			});
		}
	}
};
</script>

<style lang="scss">
.MP-search {
	background: #ffffff;
	height: 80upx;
	display: flex;
	justify-content: center;
	align-items: center;
	position: fixed;
	width: 100%;
	z-index: 999;
}
.MP-search-input {
	font-size: 28upx;
	background: #f5f5f5;
	height: 60upx;
	width: 90%;
	border-radius: 50upx;
	text-align: center;
}
.mp-search-box {
	position: absolute;
	left: 0;
	top: 30upx;
	z-index: 9999;
	width: 100%;
	padding: 0 80upx;
	.ser-input {
		flex: 1;
		height: 60upx;
		line-height: 60upx;
		text-align: center;
		font-size: 28upx;
		color: $font-color-base;
		border-radius: 20px;
		background: rgba(255, 255, 255, 0.6);
	}
}
page {
	.cate-section {
		position: relative;
		z-index: 5;
		border-radius: 16upx 16upx 0 0;
		margin-top: -20upx;
	}
	.carousel-section {
		padding: 0;
		.titleNview-placing {
			padding-top: 0;
			height: 0;
		}
		.carousel {
			.carousel-item {
				padding: 0;
			}
		}
		.swiper-dots {
			left: 45upx;
			bottom: 40upx;
		}
	}
}

page {
	background: #f5f5f5;
}
.m-t {
	margin-top: 20upx;
}
/* 头部 轮播图 */
.carousel-section {
	position: relative;
	padding-top: 10px;

	.titleNview-placing {
		height: var(--status-bar-height);
		padding-top: 44px;
		box-sizing: content-box;
	}

	.titleNview-background {
		position: absolute;
		top: 0;
		left: 0;
		width: 100%;
		height: 426upx;
		transition: 0.4s;
	}
}
.carousel {
	width: 100%;
	height: 350upx;

	.carousel-item {
		width: 100%;
		height: 100%;
		padding: 0 28upx;
		overflow: hidden;
	}

	image {
		width: 100%;
		height: 100%;
		border-radius: 10upx;
	}
}
.swiper-dots {
	display: flex;
	position: absolute;
	left: 60upx;
	bottom: 15upx;
	width: 72upx;
	height: 36upx;
	background-image: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMgAAABkCAYAAADDhn8LAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyZpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTMyIDc5LjE1OTI4NCwgMjAxNi8wNC8xOS0xMzoxMzo0MCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6OTk4MzlBNjE0NjU1MTFFOUExNjRFQ0I3RTQ0NEExQjMiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6OTk4MzlBNjA0NjU1MTFFOUExNjRFQ0I3RTQ0NEExQjMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTcgKFdpbmRvd3MpIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6Q0E3RUNERkE0NjExMTFFOTg5NzI4MTM2Rjg0OUQwOEUiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6Q0E3RUNERkI0NjExMTFFOTg5NzI4MTM2Rjg0OUQwOEUiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz4Gh5BPAAACTUlEQVR42uzcQW7jQAwFUdN306l1uWwNww5kqdsmm6/2MwtVCp8CosQtP9vg/2+/gY+DRAMBgqnjIp2PaCxCLLldpPARRIiFj1yBbMV+cHZh9PURRLQNhY8kgWyL/WDtwujjI8hoE8rKLqb5CDJaRMJHokC6yKgSCR9JAukmokIknCQJpLOIrJFwMsBJELFcKHwM9BFkLBMKFxNcBCHlQ+FhoocgpVwwnv0Xn30QBJGMC0QcaBVJiAMiec/dcwKuL4j1QMsVCXFAJE4s4NQA3K/8Y6DzO4g40P7UcmIBJxbEesCKWBDg8wWxHrAiFgT4fEGsB/CwIhYE+AeBAAdPLOcV8HRmWRDAiQVcO7GcV8CLM8uCAE4sQCDAlHcQ7x+ABQEEAggEEAggEEAggEAAgQACASAQQCCAQACBAAIBBAIIBBAIIBBAIABe4e9iAe/xd7EAJxYgEGDeO4j3EODp/cOCAE4sYMyJ5cwCHs4rCwI4sYBxJ5YzC84rCwKcXxArAuthQYDzC2JF0H49LAhwYUGsCFqvx5EF2T07dMaJBetx4cRyaqFtHJ8EIhK0i8OJBQxcECuCVutxJhCRoE0cZwMRyRcFefa/ffZBVPogePihhyCnbBhcfMFFEFM+DD4m+ghSlgmDkwlOgpAl4+BkkJMgZdk4+EgaSCcpVX7bmY9kgXQQU+1TgE0c+QJZUUz1b2T4SBbIKmJW+3iMj2SBVBWz+leVfCQLpIqYbp8b85EskIxyfIOfK5Sf+wiCRJEsllQ+oqEkQfBxmD8BBgA5hVjXyrBNUQAAAABJRU5ErkJggg==);
	background-size: 100% 100%;

	.num {
		width: 36upx;
		height: 36upx;
		border-radius: 50px;
		font-size: 24upx;
		color: #fff;
		text-align: center;
		line-height: 36upx;
	}

	.sign {
		position: absolute;
		top: 0;
		left: 50%;
		line-height: 36upx;
		font-size: 12upx;
		color: #fff;
		transform: translateX(-50%);
	}
}
/* 分类 */
.cate-section {
	display: flex;
	justify-content: space-around;
	align-items: center;
	flex-wrap: wrap;
	padding: 30upx 22upx;
	background: #fff;
	.cate-item {
		display: flex;
		flex-direction: column;
		align-items: center;
		font-size: $font-sm + 2upx;
		color: $font-color-dark;
	}
	/* 原图标颜色太深,不想改图了,所以加了透明度 */
	image {
		width: 88upx;
		height: 88upx;
		margin-bottom: 14upx;
		border-radius: 50%;
		opacity: 0.7;
		box-shadow: 4upx 4upx 20upx rgba(250, 67, 106, 0.3);
	}
}
.ad-1 {
	width: 100%;
	height: 210upx;
	padding: 10upx 0;
	background: #fff;
	image {
		width: 100%;
		height: 100%;
	}
}
/* 秒杀专区 */
.seckill-section {
	padding: 0upx 20upx 20upx;
	background: #fff;
	.s-header {
		display: flex;
		align-items: center;
		height: 90upx;
		line-height: 1;
		.s-img {
			width: 140upx;
			height: 30upx;
		}
		.tip {
			font-size: $font-base;
			color: $font-color-light;
			// margin: 0 20upx 0 40upx;
		}
		.timer {
			display: inline-block;
			width: 40upx;
			height: 36upx;
			text-align: center;
			line-height: 36upx;
			margin-right: 14upx;
			font-size: $font-sm + 2upx;
			color: #fff;
			border-radius: 2px;
			background: rgba(0, 0, 0, 0.8);
		}
		.icon-you {
			font-size: $font-lg;
			color: $font-color-light;
			flex: 1;
			text-align: right;
		}
	}
	.floor-list {
		white-space: nowrap;
	}
	.scoll-wrapper {
		display: flex;
		align-items: flex-start;
	}
	.floor-item {
		width: 150upx;
		margin-right: 20upx;
		font-size: $font-sm + 2upx;
		color: $font-color-dark;
		line-height: 1.8;
		image {
			width: 150upx;
			height: 150upx;
			border-radius: 6upx;
		}
		.price {
			color: $uni-color-primary;
		}
	}
}

.f-header {
	display: flex;
	align-items: center;
	height: 140upx;
	padding: 0upx 20upx;
	background: #fff;
	image {
		flex-shrink: 0;
		width: 80upx;
		height: 80upx;
		margin-right: 20upx;
	}
	.tit-box {
		flex: 1;
		display: flex;
		flex-direction: column;
	}
	.tit {
		font-size: $font-lg + 2upx;
		color: #font-color-dark;
		line-height: 1.3;
	}
	.tit2 {
		font-size: $font-sm;
		color: $font-color-light;
	}
	.icon-you {
		font-size: $font-lg + 2upx;
		color: $font-color-light;
	}
}
/* 团购楼层 */
.group-section {
	background: #fff;
	.g-swiper {
		height: 650upx;
		padding-bottom: 20upx;
	}
	.g-swiper-item {
		width: 100%;
		padding: 0 20upx;
		display: flex;
	}
	image {
		width: 100%;
		height: 460upx;
		border-radius: 4px;
	}
	.g-item {
		display: flex;
		flex-direction: column;
		overflow: hidden;
	}
	.left {
		flex: 1.2;
		// margin-right: 24upx;
		.t-box {
			padding-top: 20upx;
		}
	}
	.right {
		flex: 0.8;
		flex-direction: column-reverse;
		.t-box {
			padding-bottom: 20upx;
		}
	}
	.t-box {
		height: 160upx;
		font-size: $font-base + 2upx;
		color: $font-color-dark;
		line-height: 1.6;
	}
	.price {
		color: $uni-color-primary;
	}
	.m-price {
		font-size: $font-sm + 2upx;
		text-decoration: line-through;
		color: $font-color-light;
		margin-left: 8upx;
	}
	.pro-box {
		display: flex;
		align-items: center;
		margin-top: 10upx;
		font-size: $font-sm;
		color: $font-base;
		padding-right: 10upx;
	}
	.progress-box {
		flex: 1;
		border-radius: 10px;
		overflow: hidden;
		margin-right: 8upx;
	}
}
/* 分类推荐楼层 */
.hot-floor {
	width: 100%;
	overflow: hidden;
	margin-bottom: 20upx;
	.floor-img-box {
		width: 100%;
		height: 320upx;
		position: relative;
		&:after {
			content: '';
			position: absolute;
			left: 0;
			top: 0;
			width: 100%;
			height: 100%;
			background: linear-gradient(rgba(255, 255, 255, 0.06) 30%, #f8f8f8);
		}
	}
	.floor-img {
		width: 100%;
		height: 100%;
	}
	.floor-list {
		white-space: nowrap;
		padding: 20upx;
		padding-right: 50upx;
		border-radius: 6upx;
		margin-top: -140upx;
		margin-left: 30upx;
		background: #fff;
		box-shadow: 1px 1px 5px rgba(0, 0, 0, 0.2);
		position: relative;
		z-index: 1;
	}
	.scoll-wrapper {
		display: flex;
		align-items: flex-start;
	}
	.floor-item {
		width: 180upx;
		margin-right: 20upx;
		font-size: $font-sm + 2upx;
		color: $font-color-dark;
		line-height: 1.8;
		image {
			width: 180upx;
			height: 180upx;
			border-radius: 6upx;
		}
		.price {
			color: $uni-color-primary;
		}
	}
	.more {
		display: flex;
		align-items: center;
		justify-content: center;
		flex-direction: column;
		flex-shrink: 0;
		width: 180upx;
		height: 180upx;
		border-radius: 6upx;
		background: #f3f3f3;
		font-size: $font-base;
		color: $font-color-light;
		text:first-child {
			margin-bottom: 4upx;
		}
	}
}
/* 单条商品 */
.goods-box-single {
	display: flex;
	padding: 20upx 0;
	.goods-img {
		display: block;
		width: 120upx;
		height: 120upx;
	}
	.right {
		flex: 1;
		display: flex;
		flex-direction: column;
		padding: 0 30upx 0 24upx;
		overflow: hidden;
		.title {
			font-size: $font-base + 2upx;
			color: $font-color-dark;
			line-height: 1;
		}
		.attr-box {
			font-size: $font-sm + 2upx;
			color: $font-color-light;
			padding: 10upx 12upx;
		}
		.price {
			font-size: $font-base + 2upx;
			color: $font-color-dark;
			&:before {
				content: '￥';
				font-size: $font-sm;
				margin: 0 2upx 0 8upx;
			}
		}
	}
}

.price-box {
	display: flex;
	justify-content: flex-end;
	align-items: baseline;
	padding: 20upx 30upx;
	font-size: $font-sm + 2upx;
	color: $font-color-light;
	.num {
		margin: 0 8upx;
		color: $font-color-dark;
	}
	.price {
		font-size: $font-lg;
		color: $font-color-dark;
		&:before {
			content: '￥';
			font-size: $font-sm;
			margin: 0 2upx 0 8upx;
		}
	}
}
.action-box {
	display: flex;
	justify-content: flex-end;
	align-items: center;
	height: 100upx;
	position: relative;
	padding-right: 30upx;
}
.action-btn {
	width: 160upx;
	height: 60upx;
	margin: 0;
	margin-left: 24upx;
	padding: 0;
	text-align: center;
	line-height: 60upx;
	font-size: $font-sm + 2upx;
	color: $font-color-dark;
	background: #fff;
	border-radius: 100px;
	&:after {
		border-radius: 100px;
	}
	&.recom {
		background: #fff9f9;
		color: $base-color;
		&:after {
			border-color: #f7bcc8;
		}
	}
}

/* 猜你喜欢 */
.guess-section {
	display: flex;
	flex-wrap: wrap;
	padding: 0 30upx;
	background: #fff;
	.guess-item {
		display: flex;
		flex-direction: column;
		width: 48%;
		padding-bottom: 40upx;
		&:nth-child(2n + 1) {
			margin-right: 4%;
		}
	}
	.image-wrapper {
		width: 100%;
		height: 330upx;
		border-radius: 3px;
		overflow: hidden;
		image {
			width: 100%;
			height: 100%;
			opacity: 1;
		}
	}
	.title {
		font-size: $font-lg;
		color: $font-color-dark;
		line-height: 80upx;
	}
	.price {
		font-size: $font-lg;
		color: $uni-color-primary;
		line-height: 1;
	}
	.coupon_box {
		width: 100%;
		height: auto;
		display: table;
		padding: 6upx 26upx 26upx 26upx;
	}

	.other_type {
		width: 100%;
		height: 90upx;
		padding-top: 50upx;

		.text {
			width: 100%;
			border-top: 1px solid #eeeeee;
			display: block;
			text-align: center;
			position: relative;
		}
		.text span {
			width: 180upx;
			height: 40upx;
			line-height: 40upx;
			color: #999999;
			display: block;
			background: #ffffff;
			position: absolute;
			left: 50%;
			top: 50%;
			margin-left: -90upx;
			margin-top: -20upx;
			font-size: $font-base;
		}
	}
}
.getPosition {
	height: 100upx;
	display: flex;
	justify-content: center;
	align-items: center;
	font-size: 32upx;
	background-color: #fff;
}
</style>
