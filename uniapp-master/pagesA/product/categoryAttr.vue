<template>
	<view class="content" style="display: flex;flex-direction: column;">

		<!-- #ifdef MP-WEIXIN -->
        <nav-bar>分类</nav-bar>
		<view class="search" @click="search()">
			<input class="search_input" type="text" disabled="true" placeholder="输入关键字搜索" />
		</view>
		<!-- #endif -->

		<!-- #ifdef MP-WEIXIN -->
		<view class="category-wrapper" :style="{'height':scroll_height+'px'}">
		<!-- #endif -->
		<!-- #ifdef APP-PLUS || APP-PLUS-NVUE || H5 -->
		<view class="category-wrapper">
		<!-- #endif -->
			<!-- 左边导航 -->
			<scroll-view scroll-y="true" class="left-wrapper" scroll-with-animation="true" :scroll-top="left_scroll">
				<view class="left-content column">
					<view class="title-content" :class="select_index === index ? 'onSelected' : ''" v-for="(title, index) in catrgoryList" :key="title.id" @tap="choose(index)">
						{{ title.name || '分类' + (index + 1) }}
					</view>
				</view>
			</scroll-view>
			<!-- 右边内容 -->
			<scroll-view scroll-y="true" class="right-wrapper" scroll-with-animation="true" :scroll-top="right_scroll" @scroll="myscroll">
				<view class="right-content">
					<!-- 产品区 -->
					<view class="product-wrapper column with-100">
						<view class="category-item column" :id="'list' + c_index" v-for="(c_item, c_index) in catrgoryList" :key="c_item.id">
							<!-- 广告图 -->
							<view class="banner-item"><image class="banner" :src="c_item.pic == '' || c_item.pic == null ? '' : c_item.pic"></image></view>
							<view class="category-content">
								<view class="product-item" v-for="(p_item, p_index) in c_item.goodsList" :key="p_item.id" @click="toCategory(p_item)">
									<image class="product-img" :src="p_item.pic == '' || p_item.pic == null ? '' : p_item.pic"></image>
									<text class="product-title">{{ p_item.name |formatLongText }}</text>
								</view>
							</view>
						</view>
					</view>
				</view>
			</scroll-view>
		</view>
	</view>
	</view>
</template>

<script>
import Api from '@/common/api';
import navBar from "@/components/zhouWei-navBar";
export default {
	components:{
		navBar
	},
	data() {
		return {
			select_index: 0,
			windows_height: 0, //屏幕高度
			right_height: [], //右侧每个内容的高度集合
			right_scroll: 0, //右侧的滑动值
			left_height: 0, //左侧总高度
			left_scroll: 0, //左侧滑动值
			last: 0,

			scroll_height: '',
			statusBarHeight: '',

			catrgoryList: [

			]
		};
	},
	onLoad() {
		this.statusBarHeight = uni.getSystemInfoSync().statusBarHeight;
		this.scroll_height = uni.getSystemInfoSync().windowHeight - (this.statusBarHeight + 44 + 40);
		console.log('------',this.scroll_height)
		this.windows_height = uni.getSystemInfoSync().windowHeight;
		this.getData();
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
		search() {
			uni.navigateTo({
				url: '/pages/search/search'
			});
		},
		async getData() {
			let params = {};
			this.catrgoryList = await Api.apiCall('get', Api.goods.categoryAndGoodsList, params);
			this.$nextTick(function() {
				this.getHeightList();
			});
			console.log('-----', this.catrgoryList);
		},
		toCategory(item) {
			uni.navigateTo({
				url: `../../pagesA/product/product?id=${item.id}`
			});

		},
		getHeightList() {
			let _this = this;
			let selectorQuery = uni.createSelectorQuery();
			selectorQuery.select('.left-content').boundingClientRect(function(rects) {
				_this.left_height = rects.height;
			});
			selectorQuery
				.selectAll('.category-item')
				.boundingClientRect(function(rects) {
					_this.right_height = rects.map(item => item.top);
					console.log(_this.right_height);
				})
				.exec();
		},
		choose(index) {
			console.log('--choose--', index);
			console.log('--choose--', this.select_index);
			if (this.select_index === index) {
				return;
			}
			this.select_index = index;
			// 加入防抖
			if (this.timeout) {
				clearTimeout(this.timeout); //清除计时器
			}
			this.timeout = setTimeout(() => {
				this.right_scroll = this.right_height[index] - 180;
			}, 300);
			console.log('--this.right_scroll--', this.right_scroll);
		},
		myscroll(e) {
			//引入节流
			let right_content_height = e.detail.scrollHeight - this.windows_height;
			if (right_content_height == e.detail.scrollTop) {
			console.log('--this.myscroll--', this.right_scroll);
				return;
			}
			let scroll_top = e.detail.scrollTop + 110; //110是banner图的高度
			//判断当前的scrollTop在哪个区间内;
			let now = +new Date();
			if (now - this.last > 140) {
				this.last = now;
				let active_index = this.right_height.findIndex((value, index, arr) => value <= scroll_top && scroll_top < arr[index + 1]);
				this.select_index = active_index;
				if (this.left_height - this.windows_height) {
					//如果有超出部分
					let diff = this.left_height - this.windows_height;
					this.left_scroll = Math.round((active_index * diff) / (this.catrgoryList.length - 1));
				}
			}
		}
	}
};
</script>

<style lang="less">
page,
.content {
	height: 100%;
	background-color: #f8f8f8;
}

.content {
	display: flex;
}
.statusBar {
	width: 100%;
	position: fixed;
	top: 0;
	z-index: 99;
	background: #ffffff;
}
.getPosition {
	height: 90upx;
	position: fixed;
	z-index: 99;
	width: 100%;
	display: flex;
	justify-content: center;
	align-items: center;
	font-size: 32upx;
	background-color: #fff;
}
.search {
	background: #ffffff;
	height: 80upx;
	display: flex;
	justify-content: center;
	align-items: center;
	border-bottom: 1upx solid #eeeeee;
}
.search_input {
	font-size: 28upx;
	background: #f5f5f5;
	height: 60upx;
	width: 90%;
	border-radius: 50upx;
	text-align: center;
}
.banner-item {
	width: 94%;
	height: 180upx;
	border-radius: 20upx;
	margin-left: 3%;
}
.banner {
	width: 100%;
	height: 180upx;
	border-radius: 20upx;
	overflow: hidden;
	box-shadow: 0upx 5upx 20upx rgba(0, 0, 0, 0.3);
}
.category-wrapper {
	width: 100%;
	display: flex;
	flex-direction: row;
	// position: absolute;
	// top: 100upx;
	// bottom: 0;
	.left-wrapper {
		width: 25%;
		flex: 0 0 200rpx;
		background-color: #ffffff;
		.left-content {
			.title-content {
				width: 100%;
				height: 100rpx;
				display: flex;
				justify-content: center;
				align-items: center;
				font-size: 30upx;
				border-bottom: 1px solid #eeeeee;
				cursor: pointer;
				&:last-child {
					// border-bottom: 0;
				}
				&.onSelected {
					background-color: #fff;
					position: relative;
					// color: #4aa849;
					color: #f44142;

					&::before {
						content: '';
						position: absolute;
						left: 0;
						top: 0;
						width: 5upx;
						height: 100%;
						background: #f44142;
						// background: linear-gradient(124deg, #feb436 0%, #fb7c22 100%);
					}
				}
			}
		}
	}
	.right-wrapper {
		flex: 1;
		width: 75%;
		background-color: #ffffff;
		.right-content {
			width: 100%;
			padding-top: 20rpx;
			border-left: 1rpx solid #efefef;
			box-sizing: border-box;
			.banner-wrapper {
				padding: 0 20rpx;
				.swiper-content {
					width: 100%;
					height: 180rpx;
					margin-bottom: 30rpx;
					.swiper-item {
						.swiper-img {
							width: 100%;
							height: 180rpx;
						}
					}
				}
			}
			.product-wrapper {
				.category-item {
					.category-title {
						height: 60rpx;
						font-size: 26rpx;
						line-height: 60rpx;
						font-weight: 500;
						background-color: #f6f6f6;
						padding-left: 20rpx;
						color: #000;
					}
					.category-content {
						display: flex;
						flex-direction: row;
						flex-flow: wrap;
						padding: 40rpx 20rpx 0;
						margin-bottom: 20rpx;
						.product-item {
							width: 33%;
							display: flex;
							flex-direction: column;
							justify-content: center;
							align-items: center;
							margin-bottom: 20rpx;
							.product-img {
								width: 120rpx;
								height: 120rpx;
								border-radius: 50%;
								margin-bottom: 10rpx;
							}
							.product-title {
								font-size: 28rpx;
							}
						}
					}
				}
			}
		}
	}
}
</style>
