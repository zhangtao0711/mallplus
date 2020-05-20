<template>

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
    				<swiper-item v-for="(item, index) in jdata.options.colorGroup" :key="index" class="carousel-item" @click="navToTabPage(item.adLink)">
    					<image :src="item.adImg" />
    				</swiper-item>
    			</swiper>
    			<!-- 自定义swiper指示器 -->
    			<view class="swiper-dots">
    				<text class="num">{{ swiperCurrent + 1 }}</text>
    				<text class="sign">/</text>
    				<text class="num">{{ jdata.options.colorGroup.length }}</text>
    			</view>
    		</view>

</template>

<script>
	export default {
		name: "jshopimgSlide",
		props: {
			jdata: {
				// type: Object,
				required: true,
			}
		},
		data() {
			return {
			swiperCurrent: 0,
			titleNViewBackground: '',
			swiperLength: 0,
				swiper: {
					indicatorDots: true,
					autoplay: true,
					 interval: 2000,
					duration: 500,
				},
			};
		},
		computed: {
			count() {  
				return (this.jdata.options.colorGroup.length > 0)
			}
		},
		created() {},
		watch: {},
		methods: {
		navToTabPage(url) {
        			uni.navigateTo({
        				url: url
        			});
        		},
		//轮播图切换修改背景色
        		swiperChange(e) {
        			const index = e.detail.current;
        			this.swiperCurrent = index;
        		//	this.titleNViewBackground = this.jdata.options.colorGroup[index].background;
        		},
			// 广告点击查看详情
			showSliderInfo(type, val) {
				if (!val) {
					return;
				}
				if (type == 1) {
					if (val.indexOf('http') != -1) {
						// #ifdef H5 
						window.location.href = val
						// #endif
					} else {
						// #ifdef H5 || APP-PLUS || APP-PLUS-NVUE || MP
						if (val == '/pages/index/index' || val == '/pages/classify/classify' || val == '/pages/cart/index/index' || val == '/pages/member/index/index') {
							uni.switchTab({
								url: val
							});
							return;
						} else if (val.indexOf('/pages/coupon/coupon') > -1) {
							var id = val.replace('/pages/coupon/coupon?id=', "");
							this.receiveCoupon(id)
						} else {
							this.$common.navigateTo(val);
							return;
						}
						// #endif
					}
				} else if (type == 2) {
					// 商品详情
					this.goodsDetail(val)
				} else if (type == 3) {
					// 文章详情
					this.$common.navigateTo('/pages/article/index?id=' + val + '&id_type=1')
				} else if (type == 4) {
					// 文章列表
					this.$common.navigateTo('/pages/article/list?cid=' + val)
				} else if (type == 5) {
					//智能表单 
					this.$common.navigateTo('/pages/form/detail/form?id=' + val)
				}
			},
			//跳转到商品详情页面
			goodsDetail: function(id) {
				let url = '/pages/goods/index/index?id=' + id;
				this.$common.navigateTo(url);
			},
			// 用户领取优惠券
			receiveCoupon(couponId) {
				let data = {
					promotion_id: couponId
				}
				this.$api.getCoupon(data, res => {
					if (res.status) {
						this.$common.successToShow(res.msg)
					} else {
						this.$common.errorToShow(res.msg)
					}
				})
			},
		}
	}
</script>


<style lang="scss">
.MP-search{
	background: #FFFFFF;height: 80upx;display: flex;justify-content: center;align-items: center;position: fixed;width: 100%;z-index: 999;
}
.MP-search-input{
	font-size: 28upx;background: #F5F5F5;height: 60upx;width: 90%;border-radius: 50upx;text-align: center;
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
    	width:100%; height:auto; display:table; padding:6upx 26upx 26upx 26upx;
    }

    .other_type {
    	width:100%; height:90upx; padding-top:50upx;

    	.text { width:100%; border-top:1px solid #eeeeee; display:block; text-align:center; position:relative; }
    	.text span { width:180upx; height:40upx; line-height:40upx; color:#999999; display:block; background:#ffffff; position:absolute; left:50%; top:50%; margin-left:-90upx; margin-top:-20upx; font-size:$font-base; }
    }
}
.getPosition{
		height: 100upx;
		display: flex;
		justify-content: center;
		align-items: center;
		font-size: 32upx;
		background-color: #FFF;
	}
</style>

