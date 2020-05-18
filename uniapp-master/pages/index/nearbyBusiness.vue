<template>
	<view class="content">

		<!-- 顶部搜索 -->
		<view class="getPosition">
			<image src="/static/position.png" @click="openMap()" style="width: 50upx;height: 50upx;" mode=""></image>

			<text @click="toAddressList()" >{{ addressData.province }}-{{ addressData.city }}-{{ addressData.region }}</text>
		</view>
		<!-- 头部 -->
		<view class="title-box">
			<view @click="scanCode()" class="scan"><image src="http://rs.eonfox.cc/clzy/static/scan-ico.png" mode="" style="width:40upx; height:40upx;  "></image></view>
			<view class="input-box" @click="openSearch">
				<view style="float: left;">
					<image src="/static/search.png" mode="" style="width:40upx; height:40upx; margin-left: 10upx; margin-right: 10upx; margin-top: 10upx; "></image>
				</view>
				<view style=" float:left; height:100%;"><input type="text" value="" placeholder="输入关键字搜索" class="input" /></view>
			</view>
		</view>


		 <view class="topTitle">
				<image src="http://rs.eonfox.cc/clzy/static/star-ico.png" mode="" style="width:68upx;height:68upx;float:left;margin-right: -10upx;"></image>
			    <text class="topText">全部商家</text>
		</view>
		<view class="checkGroup">
			<radio-group>
			 	 <label class="radio" v-for="(item,index) in disType" :key='index'><radio  value="r1"  class="radio" @click="gain(item.id)"/>{{item.name}}</label>
			 	</radio-group>
		</view>
		 	<view class="classification">
			<view class="allClassification typeList" @click="gosort">
				<uni-icon type="bars" size="22" class="bars" color="8e8e8e"></uni-icon>
				<text>全部分类</text>
			</view>
			<view class="city typeList" @click="load">
				<uni-icon type="refreshempty" size="25" color="8e8e8e"></uni-icon>
				<text>加载全部</text>
			</view>

		</view>

		<!-- #ifdef MP-WEIXIN -->
		<view class="shop-box" style="margin-top: 80upx;">
		<!-- #endif -->
		<!-- #ifndef MP-WEIXIN -->
		<view class="shop-box">
		<!-- #endif -->
			<view v-if="!storeList" style="text-align: center; color:#5C5C5C; font-size: 30upx;">暂无商家记录</view>
			<view class="shop" v-for="(item, index) in storeList" @click="gobrand" :data-Businessid="item.id" :key="index">
				<image :src="item.logo" mode="" class="image" width="100%" height="100%"></image>
				<view class="shopText">
					<text class="name">{{ item.name }}</text>
					<view class="evaluate">
						<uni-icon type="star-filled" size="15" class="star-filled" color="8e8e8e"></uni-icon>
						<uni-icon type="star-filled" size="15" class="star-filled" color="8e8e8e"></uni-icon>
						<uni-icon type="star-filled" size="15" class="star-filled" color="8e8e8e"></uni-icon>
						<uni-icon type="star-filled" size="15" class="star-filled" color="8e8e8e"></uni-icon>
						<uni-icon type="star-filled" size="15" class="star-filled" color="8e8e8e"></uni-icon>
						 <text class="garde" style="margin-right:10upx; color:#666666; ">收藏 {{ item.collect }}</text>
					</view>
					<view class="bottomInformation">
						<text v-if="item.addressDetail" class="crux" style="margin-top: 30upx; color:#666666; overflow: hidden; display: inline-block; height: 40upx; text-overflow:ellipsis;">
							{{ item.addressDetail }}
						</text>
						<text v-else class="crux" style="margin-top: 30upx; color:#666666; overflow: hidden; display: inline-block; height: 40upx; text-overflow:ellipsis;">
                        							{{ item.addressProvince }}-{{ item.addressCity }}
                        						</text>
						<text class="distance" style=" color:#666666;">{{ item.distance.toFixed(4) }}</text>
					</view>
				</view>
			</view>
			<!--<uni-load-more :loadingType="loadingType"  :contentText="contentrefresh"></uni-load-more>-->
		</view>
		<view class="mask" v-show="display" @click="Colse"></view>
		<view class="sortBox" v-show="display">
			<view class="sortBox-title">
				<view>请选择商家类别</view>
				<view @click="Colse"><uni-icon type="closeempty" size="30"></uni-icon></view>
			</view>
			<view class="sortBoxBox" v-for="(items, index1) in Parent" :key="index1">
				<view class="sortRight">
					<text v-for="(item, index2) in items.son" :key="index2" @click="sublevel(item.type_id)">{{ item.name }}</text>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
import uniLoadMore from '@/components/uni-load-more/uni-load-more.vue';
import uniIcon from '@/components/uni-icon/uni-icon.vue';
import eonfox from '@/components/eonfox/eonfox.js';
import fns from '@/components/eonfox/fns.js';
import QQMapWX from '@/components/eonfox/qqmap-wx-jssdk.js';
import mallplusCopyright from '@/components/mall-copyright/mallplusCopyright.vue';
import Api from '@/common/api';
import navBar from "@/components/zhouWei-navBar";
var qqmapsdk = new QQMapWX({
	key: '5XABZ-AQ764-SMHUQ-DABAC-R7E4H-37FM2'
});
// #ifdef H5
let jweixin = require('jweixin-module');
// #endif
var ef = new eonfox();
export default {
components: {
		mallplusCopyright,uniIcon
	},

	data() {
		return {
			sjnumber: [],
			storeList: [],
			name: '',
			address: '',
			page: 10,
			
			merchant: [],
			distance:20,
			loadingType: 0,
			contentText: {
				contentdown: '上拉显示更多',
				contentrefresh: '正在加载...',
				contentnomore: '没有更多数据了'
			},
			disType:[
            						{
            					id: 5,
            					name: "5km",
            				     },
            					{
            						id: 10,
            						name: "10km",
            					},
            					{
            						id: 20,
            						name: "20km",
            					},
            					{
            						id: 40,
            						name: "40km",
            					},
            				],
			Parent: [],
			display: false,
			addressData: {
			longitude:0.0,
			latitude:0.0,
			}, //默认收货地址
			addressId: '', //地址id

			statusBarHeight: ''
		};
	},
	components: {
		uniIcon,
		uniLoadMore,
		navBar
	},
	onShow: function() {

		(this.latitude = ''), (this.longitude = '');
		this.load();
	},
	async onLoad(options) {
	    this.load();
	},
	onReachBottom() {

	},
	methods: {
		openSearch(){
			console.log('搜索')
		},
		Colse() {
			this.display = !this.display;
		},
		toAddressList() {
			var type = '1'; //不知道这个type有什么值，取大一点
			uni.navigateTo({
				url: '../../pagesU/address/address?source=' + type
			});
		},
		gobrand: function(e) {
			console.log(e);
			// return
			var sjid = e.currentTarget.dataset.businessid;
			console.log('目标', sjid);
			uni.navigateTo({
				url: '../../pagesC/store/businessDetails?id=' + sjid
				+'&lon='+this.addressData.longitude
                +'&lat='+this.addressData.latitude
				// 参数的传递 newsid
			});
		},
		gosort() {
			this.display = !this.display;
		},
		openMap() {
        			var _this = this;
        			uni.chooseLocation({

        				success: function(res) {
        			console.log('res', res);
                    _this.addressData.longitude=res.longitude;
                    _this.addressData.latitude=res.latitude;
                    _this.addressId = 1 ;
        					_this.addressData.province = res.address ;
        					_this.addressData.name =  res.name;
        					console.log('经度：' + res.longitude);
        					console.log('详细地址：' + res.address);
        					console.log('纬度：' + res.latitude);
        					_this.storeList = {};
        				}
        			});

        		},
		async load() {
			var that = this;
			let params = {};
			if(!that.addressId){
			    that.addressData = await Api.apiCall('get', Api.goods.getItemDefautl, params);

			}

            console.log(that.addressData);
             if(that.addressData.longitude){
                     params = { geohash: that.addressData.latitude+','+ that.addressData.longitude, pageSize: that.page,distance:this.distance };
                            				console.log(params);
                            				let list = await Api.apiCall('get', Api.index.nearStoreList, params);
                    	                	that.storeList = list;
                    }
                      console.log('that.addressId：' + that.addressId);
		},
		gain(id){
        				this.distance=id ;
        				this.load();

        			},
		//点击查询商家分类
		sublevel(id) {
			var that = this;
			that.display = !that.display;
			ef.submit({
				request: {
					s: ['MERCHANTLIST', [{ lon: that.longitude, lat: that.latitude, search: { type_id: id } }]] //子级
				},
				callback: function(data) {
					var res = fns.checkError(data, 's', function(errno, error) {
						fns.err(error);
					});

					that.sjnumber = data.data.s.data.data;

					console.log('分类', data);
				},
				error: function(err) {
					fns.err('加载失败', err, 1);
				}
			});
		},



		//二维码
		scanCode() {
			var _this = this;
			uni.showToast({
				// #ifdef APP-PLUS
				title: '这是APP',
				icon: 'none',
				// #endif
				// #ifdef H5
				title: '这是H5',
				icon: 'none',
				// #endif
				// #ifdef MP-WEIXIN
				title: '这是小程序',
				icon: 'none',
				// #endif
				success() {
					setTimeout(function() {}, 1500);
				}
			});
			uni.scanCode({
				onlyFromCamera: true,
				success: function(res) {
					if (res.result) {
						var data = JSON.parse(res.result);
						console.log('res:', data);
						if (!data.errno) {
							if (data.type != 'merchant_money_plus') {
								uni.showToast({
									title: '该二维码非收款二维码'
								});
							} else if (data.data.merchant_id) {
								_this.merchant_id = data.data.merchant_id;

								if (data.data && data.data.user_id) {
									var user_id = data.data.user_id;
								} else {
									var user_id = '';
								}
								uni.navigateTo({
									url: '../../pagesB/payUser/payUser?mch_id=' + data.data.merchant_id + '&user_id=' + data.data.user_id
								});
							} else {
								uni.showToast({
									title: '扫码失败',
									icon: 'none'
								});
							}
						} else {
							uni.showToast({
								title: data.error,
								icon: 'none'
							});
						}
					}
				}
			});
		}
	}
};
</script>

<style scoped lang="stylus" ref="stylesheet/stylus">
.content
	width 100%
	overflow hidden
	.title-box
		display flex
		justify-content center // 水平居中
		align-items center // 垂直居中
		flex-direction row // 左到右
		width 750upx
		padding 0 20upx
		height 100upx
		.scan
			width 40upx
			height 40upx
		.input-box
			// margin-left 20upx
			width 700upx
			height 58upx
			// border 1upx solid #d3d3d3
			border-radius 50upx
			.search
				display inline-block
				position absolute
				top 0
				left 0
			.input
				width 80%
				display inline-block
				font-size 28upx
				text-align center
				// text-align left
				line-height 100%
				height 100%
		.chat
			flex 1
	.topTitle
		width 100%
		height 80upx
		display flex
		align-items center // 垂直居中
		flex-direction row
		padding 0 20upx
		color #333
		font-size 30upx
		.topText{
			margin-left 10upx
		}
		.leftBox
			width 300upx
			text-align right
	.checkGroup{
		padding-left 26upx
		margin-bottom 10upx
		font-size 30upx
		.uni-label-pointer {
			margin-right 16upx
		}
	}
	.classification
		height 80upx
		display flex
		align-items center
		justify-content center // 垂直居中
		flex-direction row
		font-size 28upx
		.typeList
			text-align center
			line-height 80upx
			border 1px solid #d3d3d3
			width 50%
			height 100%
			position relative
		.typeList:before
			content ""
			position absolute
			top 34upx
			right 66upx
			width: 0px;
			height: 0px;
			border-left: 12upx solid transparent;
			border-right: 12upx solid transparent;
			border-top: 12upx solid #bfbfbf;
		.allClassification{
			border-left 0
		}
		.city{
			border-right 0
		}
	.place
		font-size 30upx
		height 100upx
		text-align left
		line-height 100upx
		border-bottom 1px solid #d3d3d3
		padding-left 20px
	.shop-box, width 100%
		height auto
	.shop
		height 200upx
		border-bottom 1px solid #eee
		display flex
		align-items center
		justify-content flex-start
		flex-direction row
		.image
			width 160upx
			height 160upx
			margin-left 25upx
			border-radius 50%
		.shopText
			width 500upx
			height 220upx
			margin-left 25upx
			padding-top 25upx
			.name
				color #333
				font-weight 600
				font-size 30upx
				width 100%
				height 30px
			.evaluate
				font-size 20upx
				margin-top 20upx
			.bottomInformation
				align-items center
				justify-content space-between
				flex-direction row
				width 100%
				color #333
				font-size 28upx
				.crux
					width 240upx
					display inline-block
				.distance
					width calc(100% - 250upx)
					float right
					text-align right
					margin-top 30upx
			.icoimg
				width 48upx
				height 48upx
</style>
<style>
.search-weixin-input {
	font-size: 28upx;
	background: #f5f5f5;
	height: 60upx;
	width: 90%;
	border-radius: 50upx;
	text-align: center;
}
.search-weixin {
	background: #ffffff;
	height: 80upx;
	width: 100%;
	display: flex;
	justify-content: center;
	align-items: center;
	border-bottom: 1upx solid #eeeeee;
	position: fixed;
}
.sortBox {
	position: fixed;
	width: 650upx;
	height: auto;
	padding-top: 20upx;
	padding-bottom: 40upx;
	top: 25vh;
	background-color: #ffffff;
	margin-left: 50upx;
	font-size: 28upx;
	border: #e3e3e3 solid 3upx;
	border-radius: 25upx;
	z-index: 1000;
}
.sortBox-title {
	width: 610upx;
	padding: 20upx 20upx;
	display: flex;
	justify-content: space-between;
	align-items: center;
}
.mask {
	position: fixed;
	top: 0;
	left: 0;
	z-index: 4;
	width: 100%;
	height: 100vh;
	background: rgba(0, 0, 0, 0.4);
}
.sortBoxBox {
	width: 610upx;
	padding: 20upx 20upx;
	display: flex;
	flex-wrap: wrap;
}
.sortRight {
	width: auto;
	padding-bottom: 20upx;
	height: auto;
}
.sortRight text {
	padding: 10upx;
	border: #cccccc solid 1upx;
	border-radius: 10upx;
	color: #dd524d;
	margin-left: 20upx;
	float: left;
	margin-bottom: 10upx;
}
.getPosition {
	height: 100upx;
	display: flex;
	justify-content: center;
	align-items: center;
	font-size: 32upx;
	background-color: #fff;
}
.getPosition-weixin {
	height: 90upx;
	position: fixed;
	z-index: 99;
	width: 100%;
}
.getPosition-top {
	width: 100%;
	position: fixed;
	top: 0;
	z-index: 99;
	background: #ffffff;
}
</style>
