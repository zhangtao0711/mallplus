<template>
	<view class="content">
					<!-- 空白页 -->
					<!-- 订单列表 -->
					<view v-for="(item, index) in orderList" :key="index" class="order-item" >
						<view class="i-top b-b">
							<text class="time" @click="navToDetailPage(item)">{{ item.createTime  }}</text>
							<text class="state" :style="{ color: item.stateTipColor }" @click="navToDetailPage(item)">{{ item.id }}--</text>
							<text class="state" :style="{ color: item.stateTipColor }" @click="navToDetailPage(item)">{{ item.username }}</text>
						</view>
						<view  class="goods-box-single"  >
							<img class="goods-img" :src="item.icon" mode="aspectFill"></img>
							<view class="right" @click="navToDetailPage(item)">
								<text class="title clamp">余额{{ item.blance }}</text>
								<text class="attr-box"> 积分{{ item.integration }}</text>
							</view>
						</view>


						<view class="action-box b-t">

							<button v-if="status == 1" class="action-btn" @click="updatePublishStatus(item)">下架</button>
							<button v-if="status == 2" class="action-btn recom" @click="updatePublishStatus(item)">上架</button>

						</view>
					</view>
					<uni-load-more :status="loadingType"></uni-load-more>
	</view>
</template>

<script>
import { mapState } from 'vuex';
import mallplusCopyright from '@/components/mall-copyright/mallplusCopyright.vue';
import Api from '@/common/api';
import uniLoadMore from '@/components/uni-load-more/uni-load-more.vue';
import empty from '@/components/empty';
import { formatDate } from '@/common/date';
export default {
	components: {
		uniLoadMore,
		empty
	},
	data() {
		return {
			tabCurrentIndex: 0,
			pageNum: 1,
			orderList:[],
			status:0,
			headerPosition: 'fixed',
			headerTop: '0px',
			loadingType: 'more', //加载更多状态

		};
	},

	onLoad(options) {
		/**
		 * 修复app端点击除全部订单外的按钮进入时不加载数据的问题
		 * 替换onLoad下代码即可
		 */


		this.loadData();
	},
	onPageScroll(e) {
		//兼容iOS端下拉时顶部漂移
		if (e.scrollTop >= 0) {
			this.headerPosition = 'fixed';
		} else {
			this.headerPosition = 'absolute';
		}
	},
	//下拉刷新
	onPullDownRefresh() {
		this.pageNum =  1;
		console.log('1='+this.pageNum)
		this.loadData('refresh');
	},
	//加载更多
	onReachBottom() {
		this.pageNum =  1;
		console.log('2='+this.pageNum)
		this.loadData();
	},
    computed: {
		...mapState(['hasLogin', 'userInfo'])
	},
	methods: {
		//详情
		navToDetailPage(item) {
			//测试数据没有写id，用title代替
			let id = item.id;
			uni.navigateTo({
				url: `/pages/product/product?id=${id}`
			});
		},
		//获取订单列表
		async loadData(type = 'add', loading) {
			//这里是将订单挂载到tab列表下
			let index = this.tabCurrentIndex;


			if (!this.hasLogin) {
				url = '/pages/public/login';
				uni.navigateTo({
					url
				});
			} else {

				let params = { pageNum: this.pageNum };
				let data = await Api.apiAdminCall('get', Api.admin.memberList, params);
				let orderList1 = data.records;
				let orderList = orderList1.filter(item => {
					//添加不同状态下订单的表现形式

					item.createTime = this.dateFormat(item.createTime);

					return item;
				});

				if (type === 'refresh') {
					this.orderList = [];
				}

				this.orderList = this.orderList.concat(orderList);

				//判断是否还有下一页，有是more  没有是nomore(测试数据判断大于20就没有了)
				this.loadingType = this.orderList.length > data.total ? 'nomore' : 'more';
				if (type === 'refresh') {
					if (loading == 1) {
						uni.hideLoading();
					} else {
						uni.stopPullDownRefresh();
					}
				}


			}
		},

		//swiper 切换
		changeTab(e) {
			this.tabCurrentIndex = e.target.current;
			this.loadData('refresh');
		},
		//顶部tab点击
		tabClick(index) {
			this.tabCurrentIndex = index;
		},
		// 上下架
		async updatePublishStatus(item) {

			let publishStatus =0;
			if (item.publishStatus==1){
				publishStatus=0;
			}else {
				publishStatus=1;
			}
			let params = { id: item.id,status:publishStatus };
			let data = await Api.apiAdminCall('post', Api.admin.updatePublishStatus, params);

			if (data) {
				this.$api.msg(data);
				if (item.publishStatus==1){
					this.tabCurrentIndex = 2;
				}else {
					this.tabCurrentIndex = 1;
				}

			}
		},

		//取消订单
		async cancelOrder(item) {
			let params = { orderId: item.id };
			let data = await Api.apiAdminCall('post', Api.admin.closeOrder, params);

			if (data) {
				this.$api.msg(data);
				this.tabCurrentIndex = 4;
			}

		},
//订单确认收货
		async confimDelivery(item) {

			let params = { id: item.id };
			let data = await Api.apiAdminCall('post', Api.admin.confimDelivery, params);
			console.log(data);
			if (data) {
				this.$api.msg(data);
				this.tabCurrentIndex = 4;
			}

		},
		//订单申请退款
		async applyRefund(item) {

			let params = { id: item.id };
			let data = await Api.apiAdminCall('post', Api.admin.applyRefund, params);
			if (data) {
				console.log(data);
				this.$api.msg(data);
			}
			this.tabCurrentIndex = 5;
		},
// 去评价
		toEvaluate(orderId) {
			this.$common.navigateTo(
					'/pages/order/evaluate?order_id=' + orderId
			)
		},

		//订单状态文字和颜色
		orderStateExp(value) {
			let stateTip = '',
					stateTipColor = '#fa436a';
			if (value === 1) {
				stateTipColor = '#909399';
				stateTip = '出售中';
			}if (value === 2) {
				stateTip = '已下架';
			} else if (value === 3) {
				stateTip = '已售尽';
			}

			return { stateTip, stateTipColor };
		},

		dateFormat(time) {
			if (time == null || time === '') {
				return 'N/A';
			}
			let date = new Date(time);
			return formatDate(date, 'yyyy-MM-dd hh:mm:ss');
		}
	}
};
</script>

<style lang="scss">
page,
.content {
	background: $page-color-base;
	height: 100%;
}

.swiper-box {
	height: calc(100% - 40px);
}
.list-scroll-content {
	height: 100%;
}

.navbar {
	display: flex;
	height: 40px;
	padding: 0 5px;
	background: #fff;
	box-shadow: 0 1px 5px rgba(0, 0, 0, 0.06);
	position: relative;
	z-index: 10;
	.nav-item {
		flex: 1;
		display: flex;
		justify-content: center;
		align-items: center;
		height: 100%;
		font-size: 15px;
		color: $font-color-dark;
		position: relative;
		&.current {
			color: $base-color;
			&:after {
				content: '';
				position: absolute;
				left: 50%;
				bottom: 0;
				transform: translateX(-50%);
				width: 44px;
				height: 0;
				border-bottom: 2px solid $base-color;
			}
		}
	}
}

.uni-swiper-item {
	height: auto;
}
.order-item {
	display: flex;
	flex-direction: column;
	padding-left: 30upx;
	background: #fff;
	margin-top: 16upx;
	.i-top {
		display: flex;
		align-items: center;
		height: 80upx;
		padding-right: 30upx;
		font-size: $font-base;
		color: $font-color-dark;
		position: relative;
		.time {
			flex: 1;
		}
		.state {
			color: $base-color;
		}
		.del-btn {
			padding: 10upx 0 10upx 36upx;
			font-size: $font-lg;
			color: $font-color-light;
			position: relative;
			&:after {
				content: '';
				width: 0;
				height: 30upx;
				border-left: 1px solid $border-color-dark;
				position: absolute;
				left: 20upx;
				top: 50%;
				transform: translateY(-50%);
			}
		}
	}
	/* 多条商品 */
	.goods-box {
		height: 160upx;
		padding: 20upx 0;
		white-space: nowrap;
		.goods-item {
			width: 120upx;
			height: 120upx;
			display: inline-block;
			margin-right: 24upx;
		}
		.goods-img {
			display: block;
			width: 100%;
			height: 100%;
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
}

/* load-more */
.uni-load-more {
	display: flex;
	flex-direction: row;
	height: 80upx;
	align-items: center;
	justify-content: center;
}

.uni-load-more__text {
	font-size: 28upx;
	color: #999;
}

.uni-load-more__img {
	height: 24px;
	width: 24px;
	margin-right: 10px;
}

.uni-load-more__img > view {
	position: absolute;
}

.uni-load-more__img > view view {
	width: 6px;
	height: 2px;
	border-top-left-radius: 1px;
	border-bottom-left-radius: 1px;
	background: #999;
	position: absolute;
	opacity: 0.2;
	transform-origin: 50%;
	animation: load 1.56s ease infinite;
}

.uni-load-more__img > view view:nth-child(1) {
	transform: rotate(90deg);
	top: 2px;
	left: 9px;
}

.uni-load-more__img > view view:nth-child(2) {
	transform: rotate(180deg);
	top: 11px;
	right: 0;
}

.uni-load-more__img > view view:nth-child(3) {
	transform: rotate(270deg);
	bottom: 2px;
	left: 9px;
}

.uni-load-more__img > view view:nth-child(4) {
	top: 11px;
	left: 0;
}

.load1,
.load2,
.load3 {
	height: 24px;
	width: 24px;
}

.load2 {
	transform: rotate(30deg);
}

.load3 {
	transform: rotate(60deg);
}

.load1 view:nth-child(1) {
	animation-delay: 0s;
}

.load2 view:nth-child(1) {
	animation-delay: 0.13s;
}

.load3 view:nth-child(1) {
	animation-delay: 0.26s;
}

.load1 view:nth-child(2) {
	animation-delay: 0.39s;
}

.load2 view:nth-child(2) {
	animation-delay: 0.52s;
}

.load3 view:nth-child(2) {
	animation-delay: 0.65s;
}

.load1 view:nth-child(3) {
	animation-delay: 0.78s;
}

.load2 view:nth-child(3) {
	animation-delay: 0.91s;
}

.load3 view:nth-child(3) {
	animation-delay: 1.04s;
}

.load1 view:nth-child(4) {
	animation-delay: 1.17s;
}

.load2 view:nth-child(4) {
	animation-delay: 1.3s;
}

.load3 view:nth-child(4) {
	animation-delay: 1.43s;
}

@-webkit-keyframes load {
	0% {
		opacity: 1;
	}

	100% {
		opacity: 0.2;
	}
}
</style>
