<template>
	<view class="container">
		<view class="carousel">
			<swiper indicator-dots circular="true" duration="400">
				<swiper-item class="swiper-item" v-for="(item, index) in small" :key="index">
					<view class="image-wrapper"><image :src="item" class="loaded" mode="aspectFill"></image></view>
				</swiper-item>
			</swiper>
		</view>

		<view class="introduce-section">
			<text class="title">{{ goods.name }}</text>
			<view class="price-box">
				<text class="price-tip">¥</text>
				<text class="price" v-if="goods.vipPrice && goods.vipPrice > 0">{{ (goods.price * goods.memberRate) / 10 }}</text>
				<text class="price" v-else-if="!goods.vipPrice">{{ goods.price }}</text>
				<text class="m-price" v-if="!goods.vipPrice">¥{{ goods.originalPrice }}</text>
				<text class="m-price" v-else-if="goods.vipPrice && goods.vipPrice > 0">¥{{ goods.price }}</text>
				<text class="price" v-if="goods.isFenxiao == 1 && goods.fenxiaoPrice > 0">佣金 ¥{{ goods.fenxiaoPrice }}</text>
				<text class="coupon-tip" v-if="goods.memberRate && goods.memberRate != 10">会员折扣{{ goods.memberRate }}折</text>
			</view>
			<view class="bot-row">
				<text>销量: {{ goods.sale }}</text>
				<text>库存: {{ goods.stock }}</text>
				<text>浏览量: {{ goods.hit }}</text>
			</view>
		</view>
		<view class="share-section" @click="navToTabPageStore(goods.storeId)">
			<view class="share-icon">
				<text class="yticon icon-xingxing"></text>
				铺
			</view>
			<text class="tit"></text>
			<text class="yticon icon-bangzhu1"></text>
			<view class="share-btn">
				进入店铺
				<text class="yticon icon-you"></text>
			</view>
		</view>

		<!--分享-->
		<view class="share-section" @click="share">
			<view class="share-icon">
				<text class="yticon icon-xingxing"></text>
				返
			</view>
			<text class="tit">该商品分享可领49减10红包</text>
			<text class="yticon icon-bangzhu1"></text>
			<view class="share-btn">
				立即分享
				<text class="yticon icon-you"></text>
			</view>
		</view>

		<view class="c-list">
			<view class="c-row b-b" v-if="skuList && skuList.length > 0" @click="toggleSpec">
				<text class="tit">规格</text>
				<view class="con">
					<text class="selected-text" v-for="(sItem, sIndex) in specSelected" :key="sIndex">{{ sItem.name }}</text>
				</view>
				<text class="yticon icon-you"></text>
			</view>
			<view class="c-row b-b" v-if="couponList.length > 0" @click="toggleMask('show')">
				<text class="tit">优惠券</text>
				<text class="con t-r red">领取优惠券</text>
				<text class="yticon icon-you"></text>
			</view>

			<view class="c-row b-b" v-if="serviceList && serviceList.length > 0">
				<text class="tit">服务</text>
				<view class="bz-list con">
					<text v-for="(item1, index1) in serviceList" :key="index1">{{ item1 }}</text>
				</view>
			</view>
		</view>

		<!-- 评价 -->
		<view class="eva-section">
			<view class="e-header">
				<text class="tit">评价</text>
				<text>({{ consultCount.all }})</text>
				<text class="tit">好评</text>
				<text>({{ consultCount.goods }})</text>

				<text class="tit">一般</text>
				<text>({{ consultCount.general }})</text>

				<text class="tit">差评</text>
				<text>({{ consultCount.bad }})</text>
				<text class="tip" v-if="consultCount.persent != 200">好评率 {{ consultCount.persent }}%</text>
				<text class="yticon icon-you"></text>
			</view>
			<view class="eva-box" v-for="(item, index) in consultList" :key="item.id">
				<image :src="item.pic" class="portrait" mode="aspectFill"></image>
				<view class="right">
					<text class="name">{{ item.memberName }}</text>
					<text class="con">{{ item.consultContent }}</text>
					<view class="bot">
						<text class="attr">购买类型：{{ item.attr }}</text>
						<text class="time">{{ item.consultAddtime }}</text>
					</view>
				</view>
			</view>
		</view>

		<view class="detail-desc">
			<view class="d-header"><text>图文详情</text></view>
			<rich-text :nodes="desc |formatRichText"></rich-text>
		</view>

		<!-- 底部操作菜单 -->
		<view class="page-bottom">
			<navigator url="/pages/index/index" open-type="switchTab" class="p-b-btn">
				<text class="yticon icon-xiatubiao--copy"></text>
				<text>首页</text>
			</navigator>
			<navigator url="/pages/cart/cart" open-type="switchTab" class="p-b-btn">
				<text class="yticon icon-gouwuche"></text>
				<text>购物车</text>
			</navigator>
			<view class="p-b-btn" :class="{ active: favorite }" @click="toFavorite(goods)">
				<text class="yticon icon-shoucang"></text>
				<text>收藏</text>
			</view>

			<view class="action-btn-group">
				<button type="primary" class=" action-btn no-border buy-now-btn" @click="buy(goods)">立即购买</button>
				<button type="primary" class=" action-btn no-border add-cart-btn" @click="addCart(goods)">加入购物车</button>
			</view>
		</view>

		<view class="mask" :class="maskState === 0 ? 'none' : maskState === 1 ? 'show' : ''" @click="toggleMask">
			<view class="mask-content" @click.stop.prevent="stopPrevent">
				<view @click="obtainCoupon(item)" class="coupon-item" v-for="(item, index) in couponList" :key="index">
					<view class="con">
						<view class="left">
							<text class="title">{{ item.name }}</text>
							<text class="time">在{{ item.endTime }}前有效。 可领{{ item.perLimit }}张</text>
						</view>
						<view class="right">
							<text class="price">{{ item.amount }}</text>
							<text>满{{ item.minPoint }}可减{{ item.amount }}</text>
						</view>
						<view class="circle l"></view>
						<view class="circle r"></view>
					</view>
					<text class="tips">{{ item.categoryTitle ? '限' + item.categoryTitle + '可用' : '全品类可用' }}</text>
				</view>
			</view>
		</view>

		<!-- 规格-模态层弹窗 -->
		<view class="popup spec" :class="specClass" @touchmove.stop.prevent="stopPrevent" @click="toggleSpec">
			<!-- 遮罩层 -->
			<view class="mask"></view>
			<view class="layer attr-content" @click.stop="stopPrevent">
				<view class="a-t">
					<image :src="sku.pic"></image>
					<view class="right">
						<text class="price">¥{{ (sku.price * goods.memberRate) / 10 }} 原价 ¥{{ sku.price }}</text>
						<text class="stock">库存：{{ sku.stock }}件</text>
						<view class="selected">
							已选：
							<text class="selected-text" v-for="(sItem, sIndex) in specSelected" :key="sIndex">{{ sItem.name }}</text>
						</view>
					</view>
				</view>
				<view v-for="(item, index) in specList" :key="index" class="attr-list">
					<text>{{ item.name }}</text>
					<view class="item-list">
						<text
							v-for="(childItem, childIndex) in specChildList"
							v-if="childItem.pid === item.id"
							:key="childIndex"
							class="tit"
							:class="{ selected: childItem.selected }"
							@click="selectSpec(childIndex, childItem.pid)"
						>
							{{ childItem.name }}
						</text>
					</view>
				</view>
				<button class="btn" @click="toggleSpec">完成</button>
			</view>
		</view>
		<!-- 分享 -->
		<share ref="share" :contentHeight="580" :shareList="shareList"></share>
	</view>
</template>

<script>
import mallplusCopyright from '@/components/mall-copyright/mallplusCopyright.vue';
import Api from '@/common/api';
import share from '@/components/share';
import { mapState } from 'vuex';
	import { formatDate } from '@/common/date';

export default {
	components: {
		share
	},
	data() {
		return {
		maskState: 0, //优惠券面板显示状态
			specClass: 'none',
			specSelected: [],
			small: null,
			sku: [],
			detailData: [],
			serviceList:[],
			goods: [],
			favorite: true,
			shareList: [],
			consultList: [],
			couponList:[],
			basicMarkingList:[],
			basicGiftsList:[],
			consultCount: {
				all: 0,
				bad: 0,
				general: 0,
				goods: 0
			},
			imgList: [
				{
					src: 'https://gd3.alicdn.com/imgextra/i3/0/O1CN01IiyFQI1UGShoFKt1O_!!0-item_pic.jpg_400x400.jpg'
				},
				{
					src: 'https://gd3.alicdn.com/imgextra/i3/TB1RPFPPFXXXXcNXpXXXXXXXXXX_!!0-item_pic.jpg_400x400.jpg'
				},
				{
					src: 'https://gd2.alicdn.com/imgextra/i2/38832490/O1CN01IYq7gu1UGShvbEFnd_!!38832490.jpg_400x400.jpg'
				}
			],
			desc: '',
			skuList: [],
			specList: [],
			specChildList: []
		};
	},
	async onLoad(ops) {
		//接收传值,id里面放的是标题，因为测试数据并没写id
		let id = ops.id;
		if (id) {
			this.logining = true;
			let params = { id: ops.id };
			let data = await Api.apiCall('get', Api.goods.goodsDetail, params);
			this.logining = false;

			if (data) {
				let detailData = data.goods;
				let goods = detailData.goods;
				this.goods = goods;
				this.favorite = data.favorite;
				this.typeGoodsList = detailData.typeGoodsList;
				var subImages = goods.albumPics;
				this.desc = goods.detailHtml;
				this.small = subImages.split(',');
				//await this.$api.json('detailData');
				let shareList = await this.$api.json('shareList');
				this.loaded = true;

				var sers = goods.serviceIds;
				if (sers && sers.indexOf("1") != -1){
					this.serviceList.push('无忧退货');
				}
				if (sers && sers.indexOf("2") != -1){
					this.serviceList.push('快速退款');
				}
				if (sers && sers.indexOf("3") != -1){
					this.serviceList.push('正品保证');
				}
				this.shareList = shareList;
				this.specList = detailData.productAttributeValueList;
				this.skuList = detailData.skuStockList;
				if (this.specList) {
					this.specList.forEach(item => {
						let valuesA = item.value.split(',');
						if (valuesA) {
							for (let cItem of valuesA) {
								let att = {};
								att.pid = item.id;
								att.name = cItem;
								this.specChildList.push(att);
							}
						}
					});
				}

				uni.setNavigationBarTitle({
					title: goods.name
				});
			}
			if (this.hasLogin) {
				let params = { goodsId: ops.id ,pic:this.goods.pic};
				await Api.apiCall('post', Api.goods.addView, params);
			}
			let params1 = { goodsId: ops.id };
			let consoltL = await Api.apiCall('get', Api.goods.consultList, params1);
			this.consultList = consoltL.list;
			this.consultCount = consoltL.count;

			let params3 = { };
            let couponList1 = await Api.apiCall('get', Api.index.couponList, params3);
            this.couponList = couponList1;

		}

		//规格 默认选中第一条
		if (this.specList) {
			var specs = '';
			this.specList.forEach(item => {
				for (let cItem of this.specChildList) {
					if (cItem.pid === item.id) {
						this.$set(cItem, 'selected', true);
						this.specSelected.push(cItem);
						specs = cItem.name + ',' + specs;
						break; //forEach不能使用break
					}
				}
			});
			let valuesA = specs.substr(0, specs.length - 1).split(',');

			this.skuList.forEach(item => {
				if (valuesA.length == 1 && item.sp1 == valuesA[0]) {
					this.sku = item;
				}
				if (valuesA.length == 2 && item.sp2 == valuesA[0] && item.sp1 == valuesA[1]) {
					this.sku = item;
				}
				if (valuesA.length == 3 && item.sp3 == valuesA[0] && item.sp2 == valuesA[1] && item.sp1 == valuesA[2]) {
					this.sku = item;
				}
				if (!this.sku.pic) {
					this.sku.pic = this.goods.pic;
				}
				if (!this.sku.stock) {
					this.sku.stock = 0;
				}
				this.goods.price=this.sku.price

			});
		}

	},
	computed: {
		...mapState(['hasLogin', 'userInfo'])
	},
	filters: {
	 /**
    			 * 处理富文本里的图片宽度自适应
    			 * 1.去掉img标签里的style、width、height属性
    			 * 2.img标签添加style属性：max-width:100%;height:auto
    			 * 3.修改所有style里的width属性为max-width:100%
    			 * 4.去掉<br/>标签
    			 * @param html
    			 * @returns {void|string|*}
    			 */
    			formatRichText (html) { //控制小程序中图片大小
            if (html) {
                let newContent= html.replace(/<img[^>]*>/gi,function(match,capture){
                    match = match.replace(/style="[^"]+"/gi, '').replace(/style='[^']+'/gi, '');
                    match = match.replace(/width="[^"]+"/gi, '').replace(/width='[^']+'/gi, '');
                    match = match.replace(/height="[^"]+"/gi, '').replace(/height='[^']+'/gi, '');
                    return match;
                });
                newContent = newContent.replace(/style="[^"]+"/gi,function(match,capture){
                    match = match.replace(/width:[^;]+;/gi, 'max-width:100%;').replace(/width:[^;]+;/gi, 'max-width:100%;');
                    return match;
                });
                newContent = newContent.replace(/<br[^>]*\/>/gi, '');
                newContent = newContent.replace(/\<img/gi, '<img style="max-width:100%;height:auto;display:inline-block;margin:10rpx auto;"');
                return newContent;
            } else {
                return '暂无商品详情'
            }
    			},
                  formatCreateTime(time) {
                    let date = new Date(time);
                    return formatDate(date, 'yyyy-MM-dd hh:mm:ss')
                  },
                },
	methods: {
		navToTabPageStore(item) {

                             			let id = item;
                             			uni.navigateTo({
                             				url: `../../pagesC/store/store?id=${id}`
                             			});
                             		},
	toggleMask(type) {
    				let timer = type === 'show' ? 10 : 300;
    				let state = type === 'show' ? 1 : 0;
    				this.maskState = 2;
    				setTimeout(() => {
    					this.maskState = state;
    				}, timer)
    			},
    			//领取优惠券
    		async	obtainCoupon(index) {
    			if (!this.hasLogin) {
    			this.$api.msg('请先登录');
    			}
    			             uni.showLoading({
                        				title: '请稍后'
                        			});

                        			let params = { couponId: index.id };
                        			let data = await Api.apiCall('post', Api.index.acceptCoupon, params);
                                        console.log(data);
                        			if (data) {
                        				this.$api.msg(data);
                        				this.toggleMask()
                        			}
                        			uni.hideLoading();


    			},
		//规格弹窗开关
		toggleSpec() {
			if (this.specClass === 'show') {
				this.specClass = 'hide';
				setTimeout(() => {
					this.specClass = 'none';
				}, 250);
			} else if (this.specClass === 'none') {
				this.specClass = 'show';
			}
		},
		//选择规格
		selectSpec(index, pid) {
			let list = this.specChildList;
			list.forEach(item => {
				if (item.pid === pid) {
					this.$set(item, 'selected', false);
				}
			});

			this.$set(list[index], 'selected', true);
			//存储已选择
			/**
			 * 修复选择规格存储错误
			 * 将这几行代码替换即可
			 * 选择的规格存放在specSelected中
			 */
			var specs = '';
			this.specSelected = [];
			list.forEach(item => {
				if (item.selected === true) {
					this.specSelected.push(item);
					specs = item.name + ',' + specs;
				}
			});
			let valuesA = specs.substr(0, specs.length - 1).split(',');
			this.sku =null;
			this.skuList.forEach(item => {

				if (valuesA.length == 1 && item.sp1 == valuesA[0]) {
					this.sku = item;
				}
				if (valuesA.length == 2 && item.sp2 == valuesA[0] && item.sp1 == valuesA[1]) {
					this.sku = item;
				}
				if (valuesA.length == 3 && item.sp3 == valuesA[0] && item.sp2 == valuesA[1] && item.sp1 == valuesA[2]) {
					this.sku = item;
				}
			});
			if (!this.sku) {
				uni.showToast({title:"商品不存在！"});
			}
			if (!this.sku.pic) {
				this.sku.pic = this.goods.pic;
			}
			if (!this.sku.stock) {
				this.sku.stock = 0;
			}

				this.goods.price = this.sku.price;

			console.log(this.sku)
		},
		//分享
		share() {
			this.$refs.share.toggleMask();
		},
		//收藏
		toFavorite(item) {
			if (!this.hasLogin) {
				let url = '/pages/public/login';
				uni.navigateTo({
					url
				});
			} else {
				this.favorite = !this.favorite;
				let params = { objId: item.id, type: 1, name: item.name, meno1: item.pic, meno2: item.price, meno3: item.sale };
				Api.apiCall('post', Api.goods.favoriteSave, params);
			}
		},
		async buy(item) {
			if (!this.hasLogin) {
				let url = '/pages/public/login';
				uni.navigateTo({
					url
				});
			}
			let userInfo = uni.getStorageSync('userInfo')
			if(userInfo && userInfo.storeId && userInfo.storeId==this.goods.storeId){
				uni.showToast({title:"不能购买自己店铺的商品！"});
				return;
			}
			let data;
			let id = item.id;
			console.log(id);
			if (this.sku && this.sku.id) {
			if(this.sku.stock<1){
            			   uni.showToast({title:"库存不够！"});
            			   return;
            			}
				let params = { goodsId: id, skuId: this.sku.id };
				// data = await Api.apiCall('post', Api.order.addCart, params);
				let dataJson ={};

                            			dataJson.goodsId=this.goods.id;

                            			dataJson.type=1;
                            			dataJson.skuId=this.sku.id;
                            			let url='/pages/order/createOrder?dataJson=' + JSON.stringify(dataJson)

                				uni.navigateTo({
                					url: url
                				});

			} else {
			if(this.goods.stock<1){
                        			   uni.showToast({title:"库存不够！"});
                        			   return;
                        			}
				let params = { goodsId: id };
			//	data = await Api.apiCall('post', Api.order.addCart, params);
let dataJson ={};

                            			dataJson.goodsId=this.goods.id;

                            			dataJson.type=1;

                            			let url='/pages/order/createOrder?dataJson=' + JSON.stringify(dataJson)

                				uni.navigateTo({
                					url: url
                				});
			}
		},
		async addCart(item) {
			if (!this.hasLogin) {
				let url = '/pages/public/login';
				uni.navigateTo({
					url
				});
				return false;
			}
			let userInfo = uni.getStorageSync('userInfo')
			console.log(userInfo)
			console.log(this.goods.storeId)
			if(userInfo && userInfo.storeId && userInfo.storeId==this.goods.storeId){
				uni.showToast({title:"不能购买自己店铺的商品！"});
				return;
			}
			let id = item.id;
			let data;

			if (this.sku && this.sku.id) {
			if(this.sku.stock<1){
            			   uni.showToast({title:"库存不够！"});
            			   return;
            			}
				let params = { goodsId: id, skuId: this.sku.id };
				data = await Api.apiCall('post', Api.order.addCart, params);
			} else {
			if(this.goods.stock<1){
                        			   uni.showToast({title:"库存不够！"});
                        			   return;
                        			}
				let params = { goodsId: id };
				data = await Api.apiCall('post', Api.order.addCart, params);
			}

			if (data) {
				this.$api.msg('添加购物车成功!');
			} else {
				this.$api.msg('加入购物车错误');
			}
		},
		stopPrevent() {}
	}
};
</script>

<style lang="scss">
page {
	background: $page-color-base;
	padding-bottom: 160upx;
}
.icon-you {
	font-size: $font-base + 2upx;
	color: #888;
}
.carousel {
	height: 722upx;
	position: relative;
	swiper {
		height: 100%;
	}
	.image-wrapper {
		width: 100%;
		height: 100%;
	}
	.swiper-item {
		display: flex;
		justify-content: center;
		align-content: center;
		height: 750upx;
		overflow: hidden;
		image {
			width: 100%;
			height: 100%;
		}
	}
}

/* 标题简介 */
.introduce-section {
	background: #fff;
	padding: 20upx;

	.title {
		font-size: 32upx;
		color: $font-color-dark;
		height: 50upx;
		line-height: 50upx;
	}
	.price-box {
		display: flex;
		align-items: baseline;
		height: 64upx;
		padding: 10upx 0;
		font-size: 26upx;
		color: $uni-color-primary;
	}
	.price {
		font-size: $font-lg + 2upx;
	}
	.m-price {
		margin: 0 12upx;
		color: $font-color-light;
		text-decoration: line-through;
	}
	.coupon-tip {
		align-items: center;
		padding: 4upx 10upx;
		background: $uni-color-primary;
		font-size: $font-sm;
		color: #fff;
		border-radius: 6upx;
		line-height: 1;
		transform: translateY(-4upx);
	}
	.bot-row {
		display: flex;
		align-items: center;
		height: 50upx;
		font-size: $font-sm;
		color: $font-color-light;
		text {
			flex: 1;
		}
	}
}
/* 分享 */
.share-section {
	display: flex;
	align-items: center;
	color: $font-color-base;
	background: linear-gradient(left, #fdf5f6, #fbebf6);
	padding: 12upx 30upx;
	.share-icon {
		display: flex;
		align-items: center;
		width: 70upx;
		height: 30upx;
		line-height: 1;
		border: 1px solid $uni-color-primary;
		border-radius: 4upx;
		position: relative;
		overflow: hidden;
		font-size: 22upx;
		color: $uni-color-primary;
		&:after {
			content: '';
			width: 50upx;
			height: 50upx;
			border-radius: 50%;
			left: -20upx;
			top: -12upx;
			position: absolute;
			background: $uni-color-primary;
		}
	}
	.icon-xingxing {
		position: relative;
		z-index: 1;
		font-size: 24upx;
		margin-left: 2upx;
		margin-right: 10upx;
		color: #fff;
		line-height: 1;
	}
	.tit {
		font-size: $font-base;
		margin-left: 10upx;
	}
	.icon-bangzhu1 {
		padding: 10upx;
		font-size: 30upx;
		line-height: 1;
	}
	.share-btn {
		flex: 1;
		text-align: right;
		font-size: $font-sm;
		color: $uni-color-primary;
	}
	.icon-you {
		font-size: $font-sm;
		margin-left: 4upx;
		color: $uni-color-primary;
	}
}

.c-list {
	font-size: $font-sm + 2upx;
	color: $font-color-base;
	background: #fff;
	.c-row {
		display: flex;
		align-items: center;
		padding: 20upx;
		position: relative;
	}
	.tit {
		width: 140upx;
	}
	.con {
		flex: 1;
		color: $font-color-dark;
		.selected-text {
			margin-right: 10upx;
		}
	}
	.bz-list {
		height: 40upx;
		font-size: $font-sm + 2upx;
		color: $font-color-dark;
		text {
			display: inline-block;
			margin-right: 30upx;
		}
	}
	.con-list {
		flex: 1;
		display: flex;
		flex-direction: column;
		color: $font-color-dark;
		line-height: 40upx;
	}
	.red {
		color: $uni-color-primary;
	}
}

/* 评价 */
.eva-section {
	display: flex;
	flex-direction: column;
	padding: 20upx;
	background: #fff;
	margin-top: 16upx;
	.e-header {
		display: flex;
		align-items: center;
		height: 70upx;
		font-size: $font-sm + 2upx;
		color: $font-color-light;
		.tit {
			font-size: $font-base + 2upx;
			color: $font-color-dark;
			margin-right: 4upx;
		}
		.tip {
			flex: 1;
			text-align: right;
		}
		.icon-you {
			margin-left: 10upx;
		}
	}
}
.eva-box {
	display: flex;
	padding: 20upx 0;
	.portrait {
		flex-shrink: 0;
		width: 80upx;
		height: 80upx;
		border-radius: 100px;
	}
	.right {
		flex: 1;
		display: flex;
		flex-direction: column;
		font-size: $font-base;
		color: $font-color-base;
		padding-left: 26upx;
		.con {
			font-size: $font-base;
			color: $font-color-dark;
			padding: 20upx 0;
		}
		.bot {
			display: flex;
			justify-content: space-between;
			font-size: $font-sm;
			color: $font-color-light;
		}
	}
}
/*  详情 */
.detail-desc {
	background: #fff;
	margin-top: 16upx;
	width: 100%;
	.d-header {
		display: flex;
		justify-content: center;
		align-items: center;
		height: 80upx;
		font-size: $font-base + 2upx;
		color: $font-color-dark;
		position: relative;

		text {
			padding: 0 20upx;
			background: #fff;
			position: relative;
			z-index: 1;
		}
		&:after {
			position: absolute;
			left: 50%;
			top: 50%;
			transform: translateX(-50%);
			width: 300upx;
			height: 0;
			content: '';
			border-bottom: 1px solid #ccc;
		}
	}
}

/* 规格选择弹窗 */
.attr-content {
	padding: 10upx 30upx;
	.a-t {
		display: flex;
		image {
			width: 170upx;
			height: 170upx;
			flex-shrink: 0;
			margin-top: -40upx;
			border-radius: 8upx;
		}
		.right {
			display: flex;
			flex-direction: column;
			padding-left: 24upx;
			font-size: $font-sm + 2upx;
			color: $font-color-base;
			line-height: 42upx;
			.price {
				font-size: $font-lg;
				color: $uni-color-primary;
				margin-bottom: 10upx;
			}
			.selected-text {
				margin-right: 10upx;
			}
		}
	}
	.attr-list {
		display: flex;
		flex-direction: column;
		font-size: $font-base + 2upx;
		color: $font-color-base;
		padding-top: 30upx;
		padding-left: 10upx;
	}
	.item-list {
		padding: 20upx 0 0;
		display: flex;
		flex-wrap: wrap;
		text {
			display: flex;
			align-items: center;
			justify-content: center;
			background: #eee;
			margin-right: 20upx;
			margin-bottom: 20upx;
			border-radius: 100upx;
			min-width: 60upx;
			height: 60upx;
			padding: 0 20upx;
			font-size: $font-base;
			color: $font-color-dark;
		}
		.selected {
			background: #fbebee;
			color: $uni-color-primary;
		}
	}
}

/*  弹出层 */
.popup {
	position: fixed;
	left: 0;
	top: 0;
	right: 0;
	bottom: 0;
	z-index: 99;

	&.show {
		display: block;
		.mask {
			animation: showPopup 0.2s linear both;
		}
		.layer {
			animation: showLayer 0.2s linear both;
		}
	}
	&.hide {
		.mask {
			animation: hidePopup 0.2s linear both;
		}
		.layer {
			animation: hideLayer 0.2s linear both;
		}
	}
	&.none {
		display: none;
	}
	.mask {
		position: fixed;
		top: 0;
		width: 100%;
		height: 100%;
		z-index: 1;
		background-color: rgba(0, 0, 0, 0.4);
	}
	.layer {
		position: fixed;
		z-index: 99;
		bottom: 0;
		width: 100%;
		min-height: 40vh;
		border-radius: 10upx 10upx 0 0;
		background-color: #fff;
		.btn {
			height: 66upx;
			line-height: 66upx;
			border-radius: 100upx;
			background: $uni-color-primary;
			font-size: $font-base + 2upx;
			color: #fff;
			margin: 30upx auto 20upx;
		}
	}
	@keyframes showPopup {
		0% {
			opacity: 0;
		}
		100% {
			opacity: 1;
		}
	}
	@keyframes hidePopup {
		0% {
			opacity: 1;
		}
		100% {
			opacity: 0;
		}
	}
	@keyframes showLayer {
		0% {
			transform: translateY(120%);
		}
		100% {
			transform: translateY(0%);
		}
	}
	@keyframes hideLayer {
		0% {
			transform: translateY(0);
		}
		100% {
			transform: translateY(120%);
		}
	}
}

/* 底部操作菜单 */
.page-bottom {
	position: fixed;
	left: 30upx;
	bottom: 30upx;
	z-index: 95;
	display: flex;
	justify-content: center;
	align-items: center;
	width: 690upx;
	height: 100upx;
	background: rgba(255, 255, 255, 0.9);
	box-shadow: 0 0 20upx 0 rgba(0, 0, 0, 0.5);
	border-radius: 16upx;

	.p-b-btn {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		font-size: $font-sm;
		color: $font-color-base;
		width: 96upx;
		height: 80upx;
		.yticon {
			font-size: 40upx;
			line-height: 48upx;
			color: $font-color-light;
		}
		&.active,
		&.active .yticon {
			color: $uni-color-primary;
		}
		.icon-fenxiang2 {
			font-size: 42upx;
			transform: translateY(-2upx);
		}
		.icon-shoucang {
			font-size: 46upx;
		}
	}
	.action-btn-group {
		display: flex;
		height: 76upx;
		border-radius: 100px;
		overflow: hidden;
		box-shadow: 0 20upx 40upx -16upx #fa436a;
		box-shadow: 1px 2px 5px rgba(219, 63, 96, 0.4);
		background: linear-gradient(to right, #ffac30, #fa436a, #f56c6c);
		margin-left: 20upx;
		position: relative;
		&:after {
			content: '';
			position: absolute;
			top: 50%;
			right: 50%;
			transform: translateY(-50%);
			height: 28upx;
			width: 0;
			border-right: 1px solid rgba(255, 255, 255, 0.5);
		}
		.action-btn {
			display: flex;
			align-items: center;
			justify-content: center;
			width: 180upx;
			height: 100%;
			font-size: $font-base;
			padding: 0;
			border-radius: 0;
			background: transparent;
		}
	}
}

/* 优惠券面板 */
.mask {
	display: flex;
	align-items: flex-end;
	position: fixed;
	left: 0;
	top: var(--window-top);
	bottom: 0;
	width: 100%;
	background: rgba(0, 0, 0, 0);
	z-index: 9995;
	transition: 0.3s;

	.mask-content {
		width: 100%;
		min-height: 30vh;
		max-height: 70vh;
		background: #f3f3f3;
		transform: translateY(100%);
		transition: 0.3s;
		overflow-y: scroll;
	}
	&.none {
		display: none;
	}
	&.show {
		background: rgba(0, 0, 0, 0.4);

		.mask-content {
			transform: translateY(0);
		}
	}
}

/* 优惠券列表 */
.coupon-item {
	display: flex;
	flex-direction: column;
	margin: 20upx 24upx;
	background: #fff;
	.con {
		display: flex;
		align-items: center;
		position: relative;
		height: 120upx;
		padding: 0 30upx;
		&:after {
			position: absolute;
			left: 0;
			bottom: 0;
			content: '';
			width: 100%;
			height: 0;
			border-bottom: 1px dashed #f3f3f3;
			transform: scaleY(50%);
		}
	}
	.left {
		display: flex;
		flex-direction: column;
		justify-content: center;
		flex: 1;
		overflow: hidden;
		height: 100upx;
	}
	.title {
		font-size: 32upx;
		color: $font-color-dark;
		margin-bottom: 10upx;
	}
	.time {
		font-size: 24upx;
		color: $font-color-light;
	}
	.right {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		font-size: 26upx;
		color: $font-color-base;
		height: 100upx;
	}
	.price {
		font-size: 44upx;
		color: $base-color;
		&:before {
			content: '￥';
			font-size: 34upx;
		}
	}
	.tips {
		font-size: 24upx;
		color: $font-color-light;
		line-height: 60upx;
		padding-left: 30upx;
	}
	.circle {
		position: absolute;
		left: -6upx;
		bottom: -10upx;
		z-index: 10;
		width: 20upx;
		height: 20upx;
		background: #f3f3f3;
		border-radius: 100px;
		&.r {
			left: auto;
			right: -6upx;
		}
	}
}
/* 分类推荐楼层 */
.hot-floor {
	width: 100%;
	overflow: hidden;
	margin-bottom: 20upx;
	.floor-img-box {
		width: 100%;
		height: 220upx;
		position: relative;
		display: flex;
		&:after {
			padding: 10 30upx;
			content: '';
			position: absolute;
			width: 100%;
			height: 100%;
			background: linear-gradient(rgba(255, 255, 255, 0.06) 30%, #f8f8f8);
		}
	}
	.name {
		font-size: 30upx;
		color: $font-color-base;
		margin-left: 24upx;
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
			width: 100upx;
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
</style>
