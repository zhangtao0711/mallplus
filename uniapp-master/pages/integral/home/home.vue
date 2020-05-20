<template>
	<view class="content column">
		<view class="top bg-green column with-100 align-height">
			<text class="font-bold top-integral text-while">{{integrals || 0}}</text>
			<view class="text-size-below-normal center-algin" style="margin-left: -40upx;">
				<image class="top-warn" src="/static/images/icon_sigh.png"></image>
				<text class="text-while">兑换余额</text>
			</view>
			<view class="top-bot row with-100">
				<view class="bill center-algin">
					<text class="text-while">积分明细</text>
				</view>
				<view class="bill center-algin">
					<text class="text-while">积分订单</text>
				</view>
				<view class="bill center-algin">
					<text class="text-while">积分等级</text>
				</view>
			</view>
		</view>
		
		<view class="with-100 margin-top-20 column">
			<view class="content-title padding-width-20 bg-white center-algin">
				<text class="">积分兑换商品</text>
			</view>
		</view>
		<view class="hot-goods-content bg-white">
			<view class="hot-goods-item" hover-class="hot-goods-item-hover" v-for="(item,index) in newProductList" :key="index"
			 :class="index % 2 === 2 ? 'no-border-right' : ''" @click="toGoods(item)" >
				<view class="items column padding-20">
					<view class="center-algin item-images">
						<image class="hot-goods-image"  :src="item.orderSn"></image>
					</view>
					<view class="hot-goods-name ellipsis-oneline">{{item.goodsName}}</view>
					<view class="row align-height hot-goods-price space-between-algin">
						<text class="text-grey text-size-mim">{{item.totalAmount}}积分</text>
						<text class="level text-size-24 text-green padding-width-15">{{item.levelName}}</text>
					</view>
				</view>
			</view>
		</view>
		
		
	</view>
</template>

<script>
import mallplusCopyright from '@/components/mall-copyright/mallplusCopyright.vue';
import Api from '@/common/api';
import uniLoadMore from '@/components/uni-load-more/uni-load-more.vue';
	export default {
	components: {
		mallplusCopyright,
    			uniLoadMore,

    		},
		data(){
			return{
				pageNum: 1,
                        				loadingType: 'more', //加载更多状态
			integrals:0,
				newProductList: [],


			}
		},
			//加载更多
                	onReachBottom() {
                		this.pageNum = this.pageNum + 1;
                		this.getNewProductList();
                	},
            	onPullDownRefresh() {
            	this.pageNum = this.pageNum + 1;
            	this.getNewProductList('refresh');


            		setTimeout(function () {
            			uni.stopPullDownRefresh();
            		}, 2000);
            	},

		async onShow() {
		this.getNewProductList('refresh');
		 let params = {orderType:5  };


                                     				let data1 = await Api.apiCall('get', Api.member.currentMember, params);
                         					this.integrals = data1.integration;
		},
		onPullDownRefresh() {
			
		},
		methods:{
		/**
            		     * 获取新品上市信息
            		     */
            		    async getNewProductList(type = 'add', loading){
            		    //没有更多直接返回
                        			if (type === 'add') {
                        				if (this.loadingType === 'nomore') {
                        					return;
                        				}
                        				this.loadingType = 'loading';
                        			} else {
                        				this.loadingType = 'more';
                        			}
            		      let params = { pageNum: this.pageNum,orderType:5};
            		      let list = await Api.apiCall('get', Api.order.sampleOrderList, params);


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
			toGoods(item){
            			let id = item.goodsId;
                        			uni.navigateTo({
                        				url: `../../pagesA/product/giftProduct?id=${id}`
                        			});
            		}
		}
	}
</script>

<style>
@import url("./home.css");
page{background-color: #F3F3F3;}
view {font-size: 30upx;line-height: 1.8;display: flex;}
.with-100{width: 100%;}
.row{flex-direction: row;}
.column {flex-direction: column;}
.text-size-max{font-size: 80upx;}
.center-algin{justify-content: center;align-items: center;}
.justify-center{justify-content: center;}
.justify-between{justify-content: space-between;}
.align-height{align-items: center;}
.bg-white{background: #FFFFFF;}
.bg-green {background: #F44142;}
.text-size-24 {font-size: 24upx;}
.text-size-mim {font-size: 26upx;}
.text-grey {color: #9B9B9B;}
.text-while{color: #FFFFFF;}
.font-bold{font-weight: bold;}
.text-size-below-normal {font-size: 28upx;}
.bottom-line{border-bottom: #EEEEEE solid 1upx}
.margin-bottom-20{margin-bottom: 20upx;}
.margin-top-20{margin-top: 20upx;}
.margin-width-20{margin:0 20upx;}
.padding-width-20{padding: 0 20upx;}
.content{width: 100%;}
</style>
